package dev.slinky.module.utility;

import dev.slinky.client.ClientContext;
import dev.slinky.client.event.SemanticEvent;
import dev.slinky.module.CatalogModule;
import dev.slinky.module.ModuleCategory;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.server.S00PacketKeepAlive;

public final class PingFixModule extends CatalogModule {
   public PingFixModule() {
      super("pingfix", "Ping Fix", ModuleCategory.UTILITY);
   }

   @Override
   public boolean listens(int type) {
      return type == 2 || type == 3;
   }

   @Override
   public void onEvent(ClientContext context, SemanticEvent event) {
      if (matches(event.type, event.packet)) {
         event.dispatchMode = 3;
      }
   }

   public static boolean matches(int type, Packet<?> packet) {
      return type == 2 && packet instanceof S00PacketKeepAlive || type == 3 && packet instanceof C00PacketKeepAlive;
   }
}
