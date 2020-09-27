package me.benfah.doorsofinfinity.init;

import me.benfah.doorsofinfinity.DOFMod;
import me.benfah.doorsofinfinity.block.entity.InfinityDoorBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class DOFBlockEntities
{

	public static BlockEntityType<InfinityDoorBlockEntity> INFINITY_DOOR;
	
	public static void init()
	{
		INFINITY_DOOR = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(DOFMod.MOD_ID, "infinity_door"), BlockEntityType.Builder.create(InfinityDoorBlockEntity::new, DOFBlocks.GENERATED_INFINITY_DOOR, DOFBlocks.INFINITY_DOOR).build(null));
	}

}
