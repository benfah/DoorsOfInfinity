package me.benfah.doorsofinfinity.init;

import me.benfah.doorsofinfinity.DOFMod;
import me.benfah.doorsofinfinity.block.InfinityBlock;
import me.benfah.doorsofinfinity.block.InfinityDoorBlock;
import me.benfah.doorsofinfinity.block.PhotonTransmitterBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class DOFBlocks
{

	private static DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, DOFMod.MOD_ID);

	public static RegistryObject<Block> GENERATED_BLOCK_OF_INFINITY = BLOCKS.register("generated_block_of_infinity", () -> new InfinityBlock(Block.Properties.from(Blocks.BEDROCK)));
	public static RegistryObject<Block> BLOCK_OF_INFINITY = BLOCKS.register("block_of_infinity", () -> new Block(Block.Properties.from(Blocks.STONE)));

	public static RegistryObject<InfinityDoorBlock> GENERATED_INFINITY_DOOR = BLOCKS.register("generated_infinity_door", () -> new InfinityDoorBlock(Block.Properties.from(Blocks.IRON_DOOR).hardnessAndResistance(-1.0F, 3600000.0F).sound(SoundType.STONE)));
	public static RegistryObject<InfinityDoorBlock> INFINITY_DOOR = BLOCKS.register("infinity_door", () -> new InfinityDoorBlock(Block.Properties.from(Blocks.IRON_DOOR).sound(SoundType.STONE)));

	public static RegistryObject<PhotonTransmitterBlock> PHOTON_TRANSMITTER = BLOCKS.register("photon_transmitter", () -> new PhotonTransmitterBlock(Block.Properties.create(Material.GLASS).variableOpacity()));

	public static DeferredRegister<Block> getRegister()
	{
		return BLOCKS;
	}
}
