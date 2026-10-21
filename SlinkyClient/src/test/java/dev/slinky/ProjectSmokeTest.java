package dev.slinky;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.slinky.client.ClientContext;
import dev.slinky.config.ConfigurationJson;
import dev.slinky.config.json.JsonRuntime;
import dev.slinky.inject.Mappings;
import dev.slinky.inject.transform.SlinkyPatches;
import dev.slinky.module.CoreModules;
import dev.slinky.module.Module;
import dev.slinky.module.ModuleManager;
import dev.slinky.shaded.asm.ClassReader;
import dev.slinky.shaded.asm.Opcodes;
import dev.slinky.shaded.asm.tree.AbstractInsnNode;
import dev.slinky.shaded.asm.tree.ClassNode;
import dev.slinky.shaded.asm.tree.FieldNode;
import dev.slinky.shaded.asm.tree.InsnNode;
import dev.slinky.shaded.asm.tree.LdcInsnNode;
import dev.slinky.shaded.asm.tree.MethodInsnNode;
import dev.slinky.shaded.asm.tree.MethodNode;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ProjectSmokeTest {
   private static final Pattern CLIENT_CLASS = Pattern.compile("dev/slinky/[A-Za-z0-9_$/]+");

   public static void main(String[] args) throws Exception {
      ModuleManager manager = new ModuleManager(new ClientContext(null));
      CoreModules.register(manager);
      assert manager.all().size() == 66 : "Module catalog no longer registers all 66 implementations";
      assert CoreModules.sourceOnlyIntervals().size() == 2;
      for (Module module : manager.all()) {
         assert module.getClass().getPackage().getName().startsWith("dev.slinky.module.");
      }

      JsonObject configuration = new JsonObject();
      configuration.addProperty("profile", "caf\u00e9 \u4e16\u754c");
      configuration.addProperty("__bind", 4294967295L);
      JsonArray settings = new JsonArray();
      settings.add(configuration.get("profile"));
      settings.add(configuration.get("__bind"));
      configuration.add("settings", settings);
      assert ConfigurationJson.parse(ConfigurationJson.print(configuration)).equals(configuration);
      try {
         ConfigurationJson.parse(JsonRuntime.cString("{invalid"));
         throw new AssertionError("Malformed configuration was accepted");
      } catch (IOException expected) {
         assert expected.getMessage() != null;
      }

      ClassNode transformer = new ClassNode();
      new ClassReader("dev.slinky.inject.transform.SlinkyPatches").accept(transformer, 0);
      for (FieldNode field : transformer.fields) {
         checkClassReferences(field.value);
      }
      for (MethodNode method : transformer.methods) {
         for (AbstractInsnNode instruction : method.instructions) {
            if (instruction instanceof LdcInsnNode) {
               checkClassReferences(((LdcInsnNode)instruction).cst);
            }
         }
      }

      Mappings.load(null);
      boolean commandPatched = false;
      for (SlinkyPatches.ClassEdits patch : SlinkyPatches.create(false)) {
         for (SlinkyPatches.MethodEdit edit : patch.edits) {
            if (edit.name.equals("sendChatMessage")) {
               MethodNode method = new MethodNode(Opcodes.ACC_PUBLIC, edit.name, edit.desc, null, null);
               method.instructions.add(new InsnNode(Opcodes.RETURN));
               assert edit.apply(method) == edit.expected;
               for (AbstractInsnNode instruction : method.instructions) {
                  if (instruction instanceof MethodInsnNode) {
                     MethodInsnNode call = (MethodInsnNode)instruction;
                     assert call.owner.equals("dev/slinky/client/event/ClientEventBridge") : call.owner;
                     Class<?> owner = Class.forName(call.owner.replace('/', '.'), false, ProjectSmokeTest.class.getClassLoader());
                     assert owner.getMethod(call.name, String.class).getReturnType() == boolean.class;
                     commandPatched = true;
                  }
               }
            }
         }
      }
      assert commandPatched : "Chat command patch was not exercised";
      System.out.println("Passed: 66 module registrations, JSON round trip and rejection, transformer class references, chat command patch.");
   }

   private static void checkClassReferences(Object value) {
      if (value instanceof String) {
         Matcher matcher = CLIENT_CLASS.matcher((String)value);
         while (matcher.find()) {
            String path = "/" + matcher.group() + ".class";
            assert ProjectSmokeTest.class.getResource(path) != null : "Missing injected class " + path;
         }
      }
   }
}
