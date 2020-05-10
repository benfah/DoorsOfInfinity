package me.benfah.doorsofinfinity.dimension;

import me.benfah.doorsofinfinity.dimension.chunkgen.EmptyChunkGeneratorConfig;
import me.benfah.doorsofinfinity.init.DOFDimensions;
import me.benfah.doorsofinfinity.utils.MCUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.biome.provider.CheckerboardBiomeProviderSettings;
import net.minecraft.world.biome.provider.SingleBiomeProviderSettings;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.ChunkGeneratorType;

import javax.annotation.Nullable;

public class InfinityDimension extends Dimension
{
	
    private static final Vec3d FOG_COLOR = new Vec3d(0, 0, 0);
	
	public InfinityDimension(World world, DimensionType type)
	{
		super(world, type, 0.5F);
		world.getGameRules().get(GameRules.DO_MOB_SPAWNING).set(false, world.getServer());
	}

	@Override
	public ChunkGenerator<?> createChunkGenerator()
	{
        EmptyChunkGeneratorConfig emptyChunkGenConfig = new EmptyChunkGeneratorConfig();

		SingleBiomeProviderSettings biomeProvider = BiomeProviderType.FIXED.func_226840_a_(world.getWorldInfo()).setBiome(DOFDimensions.EMPTY_BIOME.get());
        return DOFDimensions.EMPTY_CHUNK_GEN.get().create(world, BiomeProviderType.FIXED.create(biomeProvider), emptyChunkGenConfig);
	}

	@Nullable
	@Override
	public BlockPos findSpawn(ChunkPos chunkPosIn, boolean checkValid)
	{
		return null;
	}

	@Nullable
	@Override
	public BlockPos findSpawn(int posX, int posZ, boolean checkValid)
	{
		return null;
	}

	@Override
	public float calculateCelestialAngle(long worldTime, float partialTicks)
	{
		return 0;
	}

	@Override
	public boolean isSurfaceWorld()
	{
		return false;
	}

	@Override
	public Vec3d getFogColor(float celestialAngle, float partialTicks)
	{
		return FOG_COLOR;
	}

	@Override
	public boolean canRespawnHere()
	{
		return false;
	}

	@Override
	public boolean doesXZShowFog(int x, int z)
	{
		return false;
	}
}
