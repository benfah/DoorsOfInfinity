package me.benfah.doorsofinfinity.init;

import me.benfah.doorsofinfinity.DOFMod;
import me.benfah.doorsofinfinity.block.InfinityBlock;
import me.benfah.doorsofinfinity.block.InfinityDoorBlock;
import me.benfah.doorsofinfinity.block.PhotonTransmitterBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class DOFBlocks
{
	
	public static Block GENERATED_INFINITY_BLOCK = new InfinityBlock(FabricBlockSettings.copy(Blocks.BEDROCK));
	public static Block INFINITY_BLOCK = new Block(FabricBlockSettings.copy(Blocks.STONE));

	public static InfinityDoorBlock GENERATED_INFINITY_DOOR = new InfinityDoorBlock(FabricBlockSettings.copy(Blocks.IRON_DOOR).strength(-1.0F, 3600000.0F).sounds(BlockSoundGroup.STONE));
	public static InfinityDoorBlock INFINITY_DOOR = new InfinityDoorBlock(FabricBlockSettings.copy(Blocks.IRON_DOOR).sounds(BlockSoundGroup.STONE));

	public static PhotonTransmitterBlock PHOTON_TRANSMITTER = new PhotonTransmitterBlock(FabricBlockSettings.of(Material.GLASS).nonOpaque());

	public static void init()
	{
		Registry.register(Registry.BLOCK, new Identifier(DOFMod.MOD_ID, "block_of_infinity"), GENERATED_INFINITY_BLOCK);
		Registry.register(Registry.BLOCK, new Identifier(DOFMod.MOD_ID, "simulated_block_of_infinity"), INFINITY_BLOCK);

		Registry.register(Registry.BLOCK, new Identifier(DOFMod.MOD_ID, "infinity_door"), GENERATED_INFINITY_DOOR);
		Registry.register(Registry.BLOCK, new Identifier(DOFMod.MOD_ID, "simulated_infinity_door"), INFINITY_DOOR);

		Registry.register(Registry.BLOCK, new Identifier(DOFMod.MOD_ID, "photon_transmitter"), PHOTON_TRANSMITTER);

	}
	
}
