package me.benfah.doorsofinfinity.init;

import me.benfah.doorsofinfinity.DOFMod;
import me.benfah.doorsofinfinity.block.entity.InfinityDoorBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class DOFBlockEntities
{

	private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, DOFMod.MOD_ID);

	public static RegistryObject<TileEntityType<InfinityDoorBlockEntity>> INFINITY_DOOR = TILE_ENTITIES.register("infinity_door", () -> TileEntityType.Builder.create(InfinityDoorBlockEntity::new, DOFBlocks.GENERATED_INFINITY_DOOR.get(), DOFBlocks.INFINITY_DOOR.get()).build(null));

	public static DeferredRegister<?> getRegister()
	{
		return TILE_ENTITIES;
	}

}
