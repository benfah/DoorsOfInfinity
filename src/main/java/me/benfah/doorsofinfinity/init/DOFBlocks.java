package me.benfah.doorsofinfinity.init;

import me.benfah.doorsofinfinity.DOFMod;
import me.benfah.doorsofinfinity.block.InfinityDoorBlock;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class DOFBlocks
{
	
	public static Block BLOCK_OF_INFINITY = new Block(FabricBlockSettings.copy(Blocks.BEDROCK).build());
	public static Block SIMULATED_BLOCK_OF_INFINITY = new Block(FabricBlockSettings.copy(Blocks.STONE).build());

	
	public static InfinityDoorBlock INFINITY_DOOR = new InfinityDoorBlock(FabricBlockSettings.copy(Blocks.IRON_DOOR).strength(-1.0F, 3600000.0F).sounds(BlockSoundGroup.STONE).build());
	public static InfinityDoorBlock SIMULATED_INFINITY_DOOR = new InfinityDoorBlock(FabricBlockSettings.copy(Blocks.IRON_DOOR).sounds(BlockSoundGroup.STONE).build());

	public static void init()
	{
		Registry.register(Registry.BLOCK, new Identifier(DOFMod.MOD_ID, "block_of_infinity"), BLOCK_OF_INFINITY);
		Registry.register(Registry.BLOCK, new Identifier(DOFMod.MOD_ID, "simulated_block_of_infinity"), SIMULATED_BLOCK_OF_INFINITY);

		Registry.register(Registry.BLOCK, new Identifier(DOFMod.MOD_ID, "infinity_door"), INFINITY_DOOR);
		Registry.register(Registry.BLOCK, new Identifier(DOFMod.MOD_ID, "simulated_infinity_door"), SIMULATED_INFINITY_DOOR);

	}
	
}
