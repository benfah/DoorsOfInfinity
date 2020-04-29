package me.benfah.doorsofinfinity.init;

import com.qouteall.immersive_portals.render.PortalEntityRenderer;
import me.benfah.doorsofinfinity.DOFMod;
import me.benfah.doorsofinfinity.entity.BreakablePortal;
import me.benfah.doorsofinfinity.utils.MCUtils;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class DOFEntities
{

    public static EntityType<?> BREAKABLE_PORTAL;

    public static void init()
    {
            BREAKABLE_PORTAL = Registry.register(Registry.ENTITY_TYPE, new Identifier(DOFMod.MOD_ID, "breakable_portal"), FabricEntityTypeBuilder.create(EntityCategory.MISC,
                BreakablePortal::new).size(EntityDimensions.fixed(1, 1)).setImmuneToFire().build());
    }

    public static void initClient()
    {
            EntityRendererRegistry.INSTANCE.register(BREAKABLE_PORTAL, (entityRenderDispatcher, context) -> new PortalEntityRenderer(entityRenderDispatcher));
    }

}
