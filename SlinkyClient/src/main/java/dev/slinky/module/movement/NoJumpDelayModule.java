package dev.slinky.module.movement;

import dev.slinky.client.ClientContext;
import dev.slinky.client.MinecraftFields;
import dev.slinky.client.event.SemanticEvent;
import dev.slinky.module.CatalogModule;
import dev.slinky.module.ModuleCategory;

public final class NoJumpDelayModule extends CatalogModule {
   public NoJumpDelayModule() {
      super("nojumpdelay", "No Jump Delay", ModuleCategory.MOVEMENT);
   }

   @Override
   public boolean listens(int type) {
      return type == 17;
   }

   @Override
   public void onEvent(ClientContext context, SemanticEvent event) {
      if (context.inputReady()) {
         MinecraftFields.setJumpTicks(context.player(), 0);
      }
   }
}
