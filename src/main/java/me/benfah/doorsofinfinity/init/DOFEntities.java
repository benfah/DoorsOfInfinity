package me.benfah.doorsofinfinity.init;

import me.benfah.doorsofinfinity.DOFMod;
import me.benfah.doorsofinfinity.entity.BreakablePortal;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class DOFEntities
{

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = new DeferredRegister<>(ForgeRegistries.ENTITIES, DOFMod.MOD_ID);

    public static final RegistryObject<EntityType<BreakablePortal>> BREAKABLE_PORTAL = ENTITY_TYPES.register("breakable_portal", () -> EntityType.Builder
            .create(BreakablePortal::new, EntityClassification.MISC)
            .size(1, 1)
            .immuneToFire().build(""));

    public static DeferredRegister getDeferredRegister()
    {
            return ENTITY_TYPES;
    }


}
