package me.benfah.doorsofinfinity.dimension;

import java.util.function.IntPredicate;

import me.benfah.doorsofinfinity.init.DOFBlocks;
import me.benfah.doorsofinfinity.init.DOFDimensions;
import me.benfah.doorsofinfinity.utils.MCUtils;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class InfinityDimHelper
{
	
	public static PersonalDimension getEmptyPersonalDimension()
	{
		ServerWorld infinityDim = MCUtils.getServer().getWorld(DOFDimensions.INFINITY_DIM);
		BlockPos basePos = new BlockPos(0, 3, 0);
		int offset = 0;
		while(!infinityDim.getBlockState(basePos.add(offset * 200, 0, 0)).isAir())
		{
			offset++;
		}
		return new PersonalDimension(offset, infinityDim);
	}
	
	public static PersonalDimension getPersonalDimension(int id)
	{
		ServerWorld infinityDim = MCUtils.getServer().getWorld(DOFDimensions.INFINITY_DIM);
		return new PersonalDimension(id, infinityDim);
	}
	
	public PersonalDimension getPersonalDimensionAt(BlockPos pos)
	{
		return new PersonalDimension(pos.getX() / 200, MCUtils.getServer().getWorld(DOFDimensions.INFINITY_DIM));
	}
	
	public static ServerWorld getInfinityDimension()
	{
		return MCUtils.getServer().getWorld(DOFDimensions.INFINITY_DIM);
	}
	
	public static class PersonalDimension
	{
		private static final int INNER_SIZE = 9;
		private static final int WALL_THICKNESS = 2;

		private int dimId;
		private ServerWorld world;
		
		public PersonalDimension(int dimOffset, ServerWorld world)
		{
			this.dimId = dimOffset;
			this.world = world;
		}
		
		public Vec3d getBasePosition()
		{
			return new Vec3d(200 * dimId, 3, 0);
		}
		
		public int getDimensionOffset()
		{
			return dimId;
		}
		
		public Vec3d getPlayerPosCentered()
		{
			return new Vec3d(getPlayerPos()).add(0.5, 0, 0.5);
		}
		
		public BlockPos getPlayerPos()
		{
			return new BlockPos(getBasePosition().add(Math.floor(INNER_SIZE / 2) + WALL_THICKNESS, 3, INNER_SIZE + WALL_THICKNESS));
		}
		
		public void generate()
		{
			Vec3d basePos = getBasePosition();
			
			for(int i = 0; i < INNER_SIZE + WALL_THICKNESS * 2; i++)
			{
				for(int j = 0; j < INNER_SIZE + WALL_THICKNESS * 2; j++)
				{
					for(int k = 0; k < INNER_SIZE + WALL_THICKNESS * 2; k++)
					{
						BlockPos pos = new BlockPos(basePos.x + i, basePos.y + j, basePos.z + k);
						IntPredicate allowed = (a) -> a <= WALL_THICKNESS - 1 || a >= INNER_SIZE + WALL_THICKNESS;
						if(allowed.test(i) || allowed.test(j) || allowed.test(k))
						world.setBlockState(pos, DOFBlocks.BLOCK_OF_INFINITY.getDefaultState());
					}
				}
			}
			BlockPos spawnPos = new BlockPos(getPlayerPosCentered());
			world.setBlockState(spawnPos, Blocks.AIR.getDefaultState());
			world.setBlockState(spawnPos.up(), Blocks.AIR.getDefaultState());
		}
		
	}
	
}
