package me.benfah.doorsofinfinity.init;

import me.benfah.doorsofinfinity.DOFMod;

import me.benfah.doorsofinfinity.dimension.InfinityModDimension;
import me.benfah.doorsofinfinity.dimension.biome.EmptyBiome;
import me.benfah.doorsofinfinity.dimension.chunkgen.EmptyChunkGenerator;
import me.benfah.doorsofinfinity.dimension.chunkgen.EmptyChunkGeneratorConfig;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.List;

public class DOFDimensions
{

	public static final DeferredRegister<ChunkGeneratorType<?, ?>> CHUNK_GENERATOR_TYPE = new DeferredRegister<>(ForgeRegistries.CHUNK_GENERATOR_TYPES, DOFMod.MOD_ID);
	public static final DeferredRegister<Biome> BIOMES = new DeferredRegister<>(ForgeRegistries.BIOMES, DOFMod.MOD_ID);
	public static final DeferredRegister<ModDimension> DIMENSIONS = new DeferredRegister<>(ForgeRegistries.MOD_DIMENSIONS, DOFMod.MOD_ID);

	public static DimensionType INFINITY_DIM;

	public static final RegistryObject<ChunkGeneratorType<EmptyChunkGeneratorConfig, EmptyChunkGenerator>> EMPTY_CHUNK_GEN = CHUNK_GENERATOR_TYPE.register("empty", () -> new ChunkGeneratorType<>(EmptyChunkGenerator::new, false, EmptyChunkGeneratorConfig::new));

	public static final RegistryObject<ModDimension> INFINITY_MOD_DIM = DIMENSIONS.register("infinity_dimension", () -> new InfinityModDimension());

	public static final RegistryObject<Biome> EMPTY_BIOME = BIOMES.register("empty", EmptyBiome::new);
    
    public static List<DeferredRegister<?>> getRegisters()
	{
		return Arrays.asList(BIOMES, CHUNK_GENERATOR_TYPE, DIMENSIONS);
	}

	public static void registerDimension()
	{
		ResourceLocation resourceLocation = new ResourceLocation(DOFMod.MOD_ID, "infinity_dimension");
		INFINITY_DIM = DimensionManager.registerOrGetDimension(resourceLocation, INFINITY_MOD_DIM.get(), null, false);

	}
}
