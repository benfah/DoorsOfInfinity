package me.benfah.doorsofinfinity.chunkgen;

import java.util.HashMap;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.BlockView;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.EmptyBlockView;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.StructuresConfig;

public class EmptyChunkGenerator extends ChunkGenerator
{

	public static final Codec<EmptyChunkGenerator> CODEC = RecordCodecBuilder.create((instance) -> instance
			.group(BiomeSource.CODEC.fieldOf("biome_source").forGetter((generator) -> generator.biomeSource))
			.apply(instance, instance.stable(EmptyChunkGenerator::new)));

	public EmptyChunkGenerator(BiomeSource biomeSource)
	{
		super(biomeSource, new StructuresConfig(Optional.empty(), new HashMap<>()));
	}

	@Override
	protected Codec<? extends ChunkGenerator> getCodec()
	{
		return CODEC;
	}

	@Override
	public ChunkGenerator withSeed(long seed)
	{
		return this;
	}

	@Override
	public void buildSurface(ChunkRegion region, Chunk chunk)
	{

	}

	@Override
	public void populateNoise(WorldAccess world, StructureAccessor accessor, Chunk chunk)
	{

	}

	@Override
	public int getHeight(int x, int z, Type heightmapType)
	{
		return 0;
	}

	@Override
	public BlockView getColumnSample(int x, int z)
	{
		return EmptyBlockView.INSTANCE;
	}

}
