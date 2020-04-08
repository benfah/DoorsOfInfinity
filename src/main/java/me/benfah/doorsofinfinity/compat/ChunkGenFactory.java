package me.benfah.doorsofinfinity.compat;

import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;

@FunctionalInterface
public interface ChunkGenFactory<C extends ChunkGeneratorConfig, T extends ChunkGenerator<C>>
{
	T create(World world, BiomeSource biomeSource, C config);
}
