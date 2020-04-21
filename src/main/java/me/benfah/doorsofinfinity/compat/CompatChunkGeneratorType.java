package me.benfah.doorsofinfinity.compat;

import java.util.function.Supplier;

import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;

public class CompatChunkGeneratorType<C extends ChunkGeneratorConfig, T extends ChunkGenerator<C>>
		extends ChunkGeneratorType<C, T>
{
	
	private ChunkGenFactory<C, T> newFactory;
	
	public CompatChunkGeneratorType(ChunkGenFactory<C, T> factory, boolean buffetScreenOption,
			Supplier<C> settingsSupplier)
	{
		super(null, buffetScreenOption, settingsSupplier);
		this.newFactory = factory;
	}

	@Override
	public T create(IWorld iWorld, BiomeSource biomeSource, C chunkGeneratorConfig)
	{
		return newFactory.create(iWorld, biomeSource, chunkGeneratorConfig);
	}
}
