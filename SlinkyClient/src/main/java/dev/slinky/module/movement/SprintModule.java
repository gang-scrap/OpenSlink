package dev.slinky.module.movement;

import dev.slinky.client.ClientContext;
import dev.slinky.client.event.SemanticEvent;
import dev.slinky.client.input.KeyState;
import dev.slinky.module.CatalogModule;
import dev.slinky.module.ModuleCategory;

public final class SprintModule extends CatalogModule {
   private boolean lastState;

   public SprintModule() {
      super("sprint", "Sprint", ModuleCategory.MOVEMENT);
   }

   @Override
   public boolean listens(int type) {
      return type == 17 || type == 21;
   }

   @Override
   public strictfp void onEvent(ClientContext context, SemanticEvent event) {
      if (event.type == 17) {
         boolean active = context.minecraft().currentScreen == null || this.flag("while_in_gui");
         KeyState.set(context.minecraft().gameSettings.keyBindSprint, active);
         if (context.player() != null) {
            if (!active) {
               if (this.lastState) {
                  context.player().setSprinting(false);
               }

               return;
            }

            if (context.player().isSprinting()) {
               context.player().setSprinting(true);
            }
         }

         this.lastState = active;
      } else if (context.ready()) {
         if (event.forward == 0.0F && event.sideways == 0.0F) {
            return;
         }

         boolean using = context.player().isUsingItem();
         if (using && !this.flag("while_using_item")) {
            return;
         }

         if (event.forward < 0.0F) {
            if (!this.flag("while_backwards")) {
               return;
            }
         } else if (event.forward == 0.0F && event.sideways != 0.0F) {
            if (!this.flag("while_sideways")) {
               return;
            }
         } else if (!using) {
            return;
         }

         event.factor = (float)((double)event.factor * 1.3);
         context.player().setSprinting(true);
      }
   }

   private void restore(ClientContext context) {
      KeyState.restore(context.minecraft().gameSettings.keyBindSprint);
      this.lastState = false;
   }

   @Override
   protected void onDisable(ClientContext context) {
      this.restore(context);
   }

   @Override
   public void onWorldUnload(ClientContext context) {
      this.restore(context);
   }
}
