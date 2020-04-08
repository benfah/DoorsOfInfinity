package me.benfah.doorsofinfinity.dimension;

import me.benfah.doorsofinfinity.dimension.chunkgen.EmptyChunkGeneratorConfig;
import me.benfah.doorsofinfinity.init.DOFDimensions;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeSourceType;
import net.minecraft.world.biome.source.FixedBiomeSourceConfig;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class InfinityDimension extends Dimension
{
	
    private static final Vec3d FOG_COLOR = new Vec3d(1, 1, 1);
	
	public InfinityDimension(World world, DimensionType type)
	{
		super(world, type, 0.5F);
	}
	
	@Override
	public ChunkGenerator<?> createChunkGenerator()
	{
        EmptyChunkGeneratorConfig emptyChunkGenConfig = new EmptyChunkGeneratorConfig();
        FixedBiomeSourceConfig biomeConfig = BiomeSourceType.FIXED.getConfig(world.getLevelProperties()).setBiome(DOFDimensions.EMPTY_BIOME);
        
        return DOFDimensions.EMPTY_CHUNK_GEN.create(world, BiomeSourceType.FIXED.applyConfig(biomeConfig), emptyChunkGenConfig);
	}

	@Override
	public BlockPos getSpawningBlockInChunk(ChunkPos chunkPos, boolean checkMobSpawnValidity)
	{
		return null;
	}

	@Override
	public BlockPos getTopSpawningBlockPosition(int x, int z, boolean checkMobSpawnValidity)
	{
		return null;
	}

	@Override
	public float getSkyAngle(long timeOfDay, float tickDelta)
	{
		return 0;
	}

	@Override
	public boolean hasVisibleSky()
	{
		return false;
	}

	@Override
	public Vec3d getFogColor(float skyAngle, float tickDelta)
	{
		return FOG_COLOR;
	}

	@Override
	public boolean canPlayersSleep()
	{
		return false;
	}

	@Override
	public boolean isFogThick(int x, int z)
	{
		return false;
	}
	
	
	
	@Override
	public DimensionType getType()
	{
		return DOFDimensions.INFINITY_DIM;
	}

}
