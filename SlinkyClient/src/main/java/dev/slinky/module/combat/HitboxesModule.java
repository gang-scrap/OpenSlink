package dev.slinky.module.combat;

import dev.slinky.client.ClientContext;
import dev.slinky.client.event.SemanticEvent;
import dev.slinky.module.CatalogModule;
import dev.slinky.module.ModuleCategory;
import dev.slinky.module.catalog.StatusText;

public final class HitboxesModule extends CatalogModule {
   public HitboxesModule() {
      super("hitboxes", "Hitboxes", ModuleCategory.COMBAT);
   }

   @Override
   public boolean listens(int type) {
      return type == 8;
   }

   @Override
   public void onEvent(ClientContext context, SemanticEvent event) {
      event.enabled = true;
      event.expand = (double)this.number("expand");
   }

   @Override
   public String statusText() {
      return !this.findSetting("expand").isConfigured() ? null : StatusText.number(this.getName(), (double)this.number("expand"));
   }
}
