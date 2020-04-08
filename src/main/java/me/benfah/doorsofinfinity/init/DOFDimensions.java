package me.benfah.doorsofinfinity.init;

import me.benfah.doorsofinfinity.DOFMod;
import me.benfah.doorsofinfinity.compat.CompatChunkGeneratorType;
import me.benfah.doorsofinfinity.dimension.InfinityDimension;
import me.benfah.doorsofinfinity.dimension.biome.EmptyBiome;
import me.benfah.doorsofinfinity.dimension.chunkgen.EmptyChunkGenerator;
import me.benfah.doorsofinfinity.dimension.chunkgen.EmptyChunkGeneratorConfig;
import net.fabricmc.fabric.api.dimension.v1.EntityPlacer;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensionType;
import net.minecraft.block.pattern.BlockPattern.TeleportTarget;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;

public class DOFDimensions
{
    public static final FabricDimensionType INFINITY_DIM = FabricDimensionType.builder()
    		.defaultPlacer(new EntityPlacer()
			{
				
				@Override
				public TeleportTarget placeEntity(Entity teleported, ServerWorld destination, Direction portalDir,
						double horizontalOffset, double verticalOffset)
				{
					return new TeleportTarget(new Vec3d(0, 0, 0), new Vec3d(0, 100, 0), 0);
				}
			})
            .factory(InfinityDimension::new)
            .skyLight(false)
            .buildAndRegister(new Identifier(DOFMod.MOD_ID, "infinity_dimension"));
    
	public static ChunkGeneratorType<EmptyChunkGeneratorConfig, EmptyChunkGenerator> EMPTY_CHUNK_GEN = new CompatChunkGeneratorType<>(EmptyChunkGenerator::new, false, EmptyChunkGeneratorConfig::new);
	
	public static EmptyBiome EMPTY_BIOME = new EmptyBiome();
    
    public static void init()
    {
    	Registry.register(Registry.BIOME, new Identifier(DOFMod.MOD_ID, "empty_biome"), EMPTY_BIOME);
    	Registry.register(Registry.CHUNK_GENERATOR_TYPE, new Identifier(DOFMod.MOD_ID, "empty"), EMPTY_CHUNK_GEN);
    }
}
