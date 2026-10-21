package dev.slinky.module.combat;

import dev.slinky.client.ClientContext;
import dev.slinky.client.action.ModuleActionService;
import dev.slinky.client.event.SemanticEvent;
import dev.slinky.client.input.AttackInput;
import dev.slinky.client.target.RelationshipService;
import dev.slinky.module.CatalogModule;
import dev.slinky.module.ModuleCategory;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.MovingObjectPosition;

public final class AutoWeaponModule extends CatalogModule {
   private final WeaponsModule weapons;
   public final AutoWeaponModule.State state = new AutoWeaponModule.State();

   public AutoWeaponModule(WeaponsModule weapons) {
      super("autoweapon", "Auto Weapon", ModuleCategory.COMBAT);
      this.weapons = weapons;
   }

   @Override
   public boolean listens(int type) {
      return type == 25;
   }

   @Override
   public void onEvent(ClientContext context, SemanticEvent event) {
      if (context.inputReady() && !context.player().isUsingItem() && (!this.flag("require_mouse_pressed") || AttackInput.pressed(context))) {
         MovingObjectPosition hit = context.minecraft().objectMouseOver;
         if (hit != null
            && hit.typeOfHit == MovingObjectType.ENTITY
            && hit.entityHit instanceof EntityOtherPlayerMP
            && RelationshipService.hostile(hit.entityHit)
            && !this.weapons.isWeapon(context.player())) {
            int current = context.player().inventory.currentItem;
            int selected = this.weapons.firstAllowedSlot(context.player().inventory);
            if (selected == -1 || selected == current) {
               this.state.reset();
            } else if (!this.state.waiting(this.number("activation_time"))) {
               if (ModuleActionService.claim(this)) {
                  try {
                     context.player().inventory.currentItem = selected;
                  } finally {
                     ModuleActionService.release(this);
                  }
               }
            }
         } else {
            this.state.reset();
         }
      } else {
         this.state.reset();
      }
   }

   @Override
   protected void onDisable(ClientContext context) {
      this.state.reset();
      ModuleActionService.release(this);
   }

   @Override
   public void onWorldUnload(ClientContext context) {
      this.state.reset();
      ModuleActionService.release(this);
   }

   public static final class State {
      public int selectionDelayTicks;

      public boolean waiting(float activationTime) {
         if (activationTime > (float)this.selectionDelayTicks) {
            this.selectionDelayTicks++;
            return true;
         } else {
            return false;
         }
      }

      public void reset() {
         this.selectionDelayTicks = 0;
      }
   }
}
