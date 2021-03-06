package me.benfah.doorsofinfinity.dimension;

import java.util.function.Function;
import java.util.function.IntPredicate;

import me.benfah.doorsofinfinity.block.InfinityBlock;
import me.benfah.doorsofinfinity.block.entity.InfinityDoorBlockEntity;
import me.benfah.doorsofinfinity.config.DOFConfig;
import me.benfah.doorsofinfinity.init.DOFBlocks;
import me.benfah.doorsofinfinity.init.DOFDimensions;
import me.benfah.doorsofinfinity.utils.MCUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

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
	
	public static PersonalDimension getPersonalDimension(int id, int upgrades)
	{
		ServerWorld infinityDim = MCUtils.getServer().getWorld(DOFDimensions.INFINITY_DIM);
		return new PersonalDimension(id, upgrades, infinityDim);
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
		private static int INNER_SIZE = DOFConfig.getInstance().dimensionSize;
		private static int WALL_THICKNESS = 2;
		private static int UPGRADE_MULTIPLIER = 8;

		private int dimId;
		private ServerWorld world;
		private int upgrades = 0;

		public PersonalDimension(int dimOffset, int upgrades, ServerWorld world)
		{
			this(dimOffset, world);
			this.upgrades = upgrades;
		}

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

		public int getInnerSize()
		{
			return INNER_SIZE + getUpgrades() * UPGRADE_MULTIPLIER;
		}

		public int getUpgrades()
		{
			return upgrades;
		}

		public boolean upgrade()
		{
			if(this.upgrades >= DOFConfig.getInstance().maxUpgrades)
			{
				return false;
			}
			int prevInnerSize = getInnerSize();

			InfinityDoorBlockEntity linkedBlockEntity = getBlockEntity().getSyncEntity();

			linkedBlockEntity.deleteLocalPortal();
			linkedBlockEntity.deleteSyncPortal();


			linkedBlockEntity.installedUpgrades++;
			upgrades = linkedBlockEntity.installedUpgrades;

			generateCube(getBasePosition(), prevInnerSize, WALL_THICKNESS, vec -> {
				if(vec.getY() >= WALL_THICKNESS)
					return Blocks.AIR.getDefaultState();
				else
					return DOFBlocks.BLOCK_OF_INFINITY.getDefaultState().with(InfinityBlock.COLOR, InfinityBlock.Color.WHITE);
			});



			generateCube(getBasePosition(), getInnerSize(), WALL_THICKNESS, vec -> {
				if(vec.getY() >= WALL_THICKNESS)
					return DOFBlocks.BLOCK_OF_INFINITY.getDefaultState();
				else
					return DOFBlocks.BLOCK_OF_INFINITY.getDefaultState().with(InfinityBlock.COLOR, InfinityBlock.Color.WHITE);
			});

			linkedBlockEntity.placeSyncedDoor(world, getPlayerPos());
			return true;
		}

		private InfinityDoorBlockEntity getBlockEntity()
		{
			return (InfinityDoorBlockEntity) world.getBlockEntity(getPlayerPos());
		}

		public BlockPos getPlayerPos()
		{
			return new BlockPos(getBasePosition().add(Math.floor(getInnerSize() / 2) + WALL_THICKNESS, 3, getInnerSize() + WALL_THICKNESS));
		}
		
		public void generate()
		{
			/*Vec3d basePos = getBasePosition();
			
			for(int i = 0; i < INNER_SIZE + WALL_THICKNESS * 2; i++)
			{
				for(int j = 0; j < INNER_SIZE + WALL_THICKNESS * 2; j++)
				{
					for(int k = 0; k < INNER_SIZE + WALL_THICKNESS * 2; k++)
					{
						BlockPos pos = new BlockPos(basePos.x + i, basePos.y + j, basePos.z + k);
						IntPredicate allowed = (a) -> a <= WALL_THICKNESS - 1 || a >= INNER_SIZE + WALL_THICKNESS;
						if(allowed.test(i) || allowed.test(j) || allowed.test(k))
						{
							if(j >= WALL_THICKNESS)
								world.setBlockState(pos, DOFBlocks.BLOCK_OF_INFINITY.getDefaultState());
							else
								world.setBlockState(pos, DOFBlocks.BLOCK_OF_INFINITY.getDefaultState().with(InfinityBlock.COLOR, InfinityBlock.Color.WHITE));

						}
					}
				}
			}*/

			generateCube(getBasePosition(), getInnerSize(), WALL_THICKNESS, vec -> {
				if(vec.getY() >= WALL_THICKNESS)
					return DOFBlocks.BLOCK_OF_INFINITY.getDefaultState();
				else
					return DOFBlocks.BLOCK_OF_INFINITY.getDefaultState().with(InfinityBlock.COLOR, InfinityBlock.Color.WHITE);
			});

			resetDoor();
		}

		private void resetDoor()
		{
			BlockPos spawnPos = getPlayerPos();
			world.setBlockState(spawnPos, Blocks.AIR.getDefaultState());
			world.setBlockState(spawnPos.up(), Blocks.AIR.getDefaultState());
		}

		public void generateCube(Vec3d basePosition, int innerSize, int wallThickness, Function<Vec3i, BlockState> stateFunction)
		{

			for(int i = 0; i < innerSize + wallThickness * 2; i++)
			{
				for(int j = 0; j < innerSize + wallThickness * 2; j++)
				{
					for(int k = 0; k < innerSize + wallThickness * 2; k++)
					{
						BlockPos pos = new BlockPos(basePosition.x + i, basePosition.y + j, basePosition.z + k);
						IntPredicate allowed = (a) -> a <= wallThickness - 1 || a >= innerSize + wallThickness;
						if(allowed.test(i) || allowed.test(j) || allowed.test(k))
						{
							BlockState state = stateFunction.apply(new Vec3i(i, j, k));

							if(state != null)
								world.setBlockState(pos, state);
						}
					}
				}
			}
		}

	}
	
}
