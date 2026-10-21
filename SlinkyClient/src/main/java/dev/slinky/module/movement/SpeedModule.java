package dev.slinky.module.movement;

import dev.slinky.module.CatalogModule;
import dev.slinky.module.ModuleCategory;

public final class SpeedModule extends CatalogModule {
   public SpeedModule() {
      super("speed", "Speed", ModuleCategory.MOVEMENT);
      this.reorderSettings(new String[]{"speed", "require_ground"});
   }
}
