package me.benfah.doorsofinfinity.dimension.chunkgen;

import net.minecraft.world.BlockView;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class EmptyChunkGenerator extends ChunkGenerator<EmptyChunkGeneratorConfig>
{

	public EmptyChunkGenerator(IWorld world, BiomeSource biomeSource, EmptyChunkGeneratorConfig config)
	{
		super(world, biomeSource, config);
	}

	@Override
	public void buildSurface(ChunkRegion chunkRegion, Chunk chunk)
	{
		
	}

	@Override
	public int getSpawnHeight()
	{
		return 10;
	}

	@Override
	public void populateNoise(IWorld world, StructureAccessor structureAccessor, Chunk chunk)
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
		return null;
	}


	@Override
	public int getHeightOnGround(int x, int z, Type heightmapType)
	{
		return 0;
	}

}
