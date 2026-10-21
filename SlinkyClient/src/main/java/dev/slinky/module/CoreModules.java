package dev.slinky.module;

import dev.slinky.module.catalog.ModuleCatalog;
import dev.slinky.module.combat.AimAssistModule;
import dev.slinky.module.combat.AutoBlockModule;
import dev.slinky.module.combat.AutoClickerModule;
import dev.slinky.module.combat.AutoWeaponModule;
import dev.slinky.module.combat.BacktrackModule;
import dev.slinky.module.combat.CriticalsModule;
import dev.slinky.module.combat.HitSelectModule;
import dev.slinky.module.combat.HitboxesModule;
import dev.slinky.module.combat.KnockbackDelayModule;
import dev.slinky.module.combat.KnockbackDisplaceModule;
import dev.slinky.module.combat.LagRangeModule;
import dev.slinky.module.combat.PiercingModule;
import dev.slinky.module.combat.ReachModule;
import dev.slinky.module.combat.SprintResetModule;
import dev.slinky.module.combat.TimerRangeModule;
import dev.slinky.module.combat.VelocityModule;
import dev.slinky.module.combat.WeaponsModule;
import dev.slinky.module.movement.FastAccelModule;
import dev.slinky.module.movement.InstantStopModule;
import dev.slinky.module.movement.InvWalkModule;
import dev.slinky.module.movement.KeepSprintModule;
import dev.slinky.module.movement.NoJumpDelayModule;
import dev.slinky.module.movement.NoSlowModule;
import dev.slinky.module.movement.NullMoveModule;
import dev.slinky.module.movement.SafeWalkModule;
import dev.slinky.module.movement.SpeedModule;
import dev.slinky.module.movement.SprintModule;
import dev.slinky.module.movement.StrafeModule;
import dev.slinky.module.render.AntiDebuffModule;
import dev.slinky.module.render.BlockEspModule;
import dev.slinky.module.render.ChamsModule;
import dev.slinky.module.render.ChestEspModule;
import dev.slinky.module.render.FreeLookModule;
import dev.slinky.module.render.GuiModule;
import dev.slinky.module.render.IndicatorsModule;
import dev.slinky.module.render.ItemEspModule;
import dev.slinky.module.render.ModOverlayModule;
import dev.slinky.module.render.NameTagsModule;
import dev.slinky.module.render.NoHurtCamModule;
import dev.slinky.module.render.NotificationsModule;
import dev.slinky.module.render.PlayerEspModule;
import dev.slinky.module.render.PointersModule;
import dev.slinky.module.render.TrajectoriesModule;
import dev.slinky.module.utility.AntiBotModule;
import dev.slinky.module.utility.AutoPotModule;
import dev.slinky.module.utility.AutoRodModule;
import dev.slinky.module.utility.AutoSoupModule;
import dev.slinky.module.utility.BlinkModule;
import dev.slinky.module.utility.ChestStealerModule;
import dev.slinky.module.utility.DebugModule;
import dev.slinky.module.utility.FakeLagModule;
import dev.slinky.module.utility.FriendsModule;
import dev.slinky.module.utility.InvManagerModule;
import dev.slinky.module.utility.ItemUseFixModule;
import dev.slinky.module.utility.NoHitDelayModule;
import dev.slinky.module.utility.NoItemReleaseModule;
import dev.slinky.module.utility.NoUseDelayModule;
import dev.slinky.module.utility.PingFixModule;
import dev.slinky.module.utility.RefillModule;
import dev.slinky.module.utility.TeamsModule;
import dev.slinky.module.utility.TimerModule;
import dev.slinky.module.world.AutoToolModule;
import dev.slinky.module.world.BlockInModule;
import dev.slinky.module.world.BridgeAssistModule;
import dev.slinky.module.world.ClutchModule;
import dev.slinky.module.world.FastMineModule;
import dev.slinky.module.world.FastPlaceModule;
import dev.slinky.module.world.GhostHandModule;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class CoreModules {
   private CoreModules() {
   }

   public static void register(ModuleManager manager) {
      WeaponsModule weapons = new WeaponsModule();
      List<Module> implemented = new ArrayList<>();
      implemented.add(weapons);
      implemented.add(new TimerModule());
      implemented.add(new AntiDebuffModule());
      implemented.add(new NoJumpDelayModule());
      implemented.add(new NullMoveModule());
      implemented.add(new NoHitDelayModule());
      implemented.add(new FastMineModule());
      implemented.add(new NoUseDelayModule());
      implemented.add(new AutoToolModule());
      implemented.add(new AutoWeaponModule(weapons));
      implemented.add(new NoHurtCamModule());
      implemented.add(new SprintModule());
      implemented.add(new NoSlowModule());
      implemented.add(new ItemUseFixModule());
      implemented.add(new ReachModule());
      implemented.add(new HitboxesModule());
      implemented.add(new SafeWalkModule());
      implemented.add(new FastAccelModule(weapons));
      implemented.add(new AntiBotModule());
      implemented.add(new TeamsModule());
      implemented.add(new FriendsModule());
      implemented.add(new PiercingModule(weapons));
      implemented.add(new GhostHandModule());
      implemented.add(new NoItemReleaseModule());
      implemented.add(new PingFixModule());
      implemented.add(new AimAssistModule(weapons));
      implemented.add(new FreeLookModule());
      implemented.add(new StrafeModule(weapons));
      implemented.add(new InstantStopModule());
      implemented.add(new KeepSprintModule());
      implemented.add(new SprintResetModule(weapons));
      implemented.add(new VelocityModule(weapons));
      implemented.add(new FakeLagModule(weapons));
      implemented.add(new CriticalsModule(weapons));
      implemented.add(new BlinkModule(manager));
      implemented.add(new BacktrackModule(weapons));
      implemented.add(new LagRangeModule(weapons));
      implemented.add(new TimerRangeModule(weapons));
      implemented.add(new KnockbackDelayModule(weapons));
      implemented.add(new DebugModule());
      implemented.add(new KnockbackDisplaceModule(weapons, manager));
      implemented.add(new AutoSoupModule(manager));
      implemented.add(new AutoPotModule(manager));
      implemented.add(new AutoRodModule(manager, weapons));
      implemented.add(new RefillModule(manager));
      implemented.add(new InvWalkModule());
      implemented.add(new InvManagerModule());
      implemented.add(new AutoClickerModule(manager, weapons));
      implemented.add(new AutoBlockModule(manager));
      implemented.add(new BridgeAssistModule());
      implemented.add(new FastPlaceModule());
      implemented.add(new HitSelectModule(weapons));
      implemented.add(new ClutchModule(manager));
      implemented.add(new BlockInModule(manager));
      implemented.add(new PlayerEspModule());
      implemented.add(new ChestEspModule());
      implemented.add(new NameTagsModule());
      implemented.add(new PointersModule());
      implemented.add(new IndicatorsModule());
      implemented.add(new BlockEspModule());
      implemented.add(new ItemEspModule());
      implemented.add(new TrajectoriesModule());
      implemented.add(new ChamsModule());
      implemented.add(new ModOverlayModule(manager));
      implemented.add(new NotificationsModule(manager));
      implemented.add(new GuiModule(manager));
      Map<String, Module> byName = new LinkedHashMap<>();

      for (Module module : implemented) {
         if (byName.put(module.getName(), module) != null) {
            throw new IllegalStateException("Duplicate implementation " + module.getName());
         }
      }

      for (String name : ModuleCatalog.moduleNames()) {
         if (ModuleCatalog.descriptorCategory(name) >= 0) {
            Module modulex = byName.remove(name);
            if (modulex == null) {
               throw new IllegalStateException("Missing implementation " + name);
            }

            manager.register(modulex);
         }
      }

      if (!byName.isEmpty()) {
         throw new IllegalStateException("Unmatched implementations " + byName.keySet());
      }
   }

   public static List<Module> sourceOnlyIntervals() {
      return Collections.unmodifiableList(Arrays.asList(new Module[]{new ChestStealerModule(), new SpeedModule()}));
   }
}
