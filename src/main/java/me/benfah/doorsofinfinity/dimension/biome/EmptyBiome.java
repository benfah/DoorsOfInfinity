package me.benfah.doorsofinfinity.dimension.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;

public class EmptyBiome extends Biome
{

	public EmptyBiome()
	{
		super((new Biome.Settings()).configureSurfaceBuilder(SurfaceBuilder.NOPE, SurfaceBuilder.AIR_CONFIG)
				.precipitation(Biome.Precipitation.NONE).category(Biome.Category.NONE).depth(0.1F).scale(0.2F)
				.temperature(0.5F).downfall(0.0F).effects(new BiomeEffects.Builder().waterColor(4159204).fogColor(12638463).waterFogColor(329011).build()).parent((String) null));
	}

}
