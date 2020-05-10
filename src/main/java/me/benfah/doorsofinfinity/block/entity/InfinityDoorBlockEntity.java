package me.benfah.doorsofinfinity.block.entity;

import com.qouteall.immersive_portals.McHelper;
import com.qouteall.immersive_portals.portal.Portal;
import com.qouteall.immersive_portals.portal.PortalManipulation;
import me.benfah.doorsofinfinity.block.InfinityDoorBlock;
import me.benfah.doorsofinfinity.dimension.InfinityDimHelper;
import me.benfah.doorsofinfinity.dimension.InfinityDimHelper.PersonalDimension;
import me.benfah.doorsofinfinity.init.DOFBlockEntities;
import me.benfah.doorsofinfinity.init.DOFBlocks;
import me.benfah.doorsofinfinity.init.DOFDimensions;
import me.benfah.doorsofinfinity.utils.BoxUtils;
import me.benfah.doorsofinfinity.utils.MCUtils;
import me.benfah.doorsofinfinity.utils.PortalCreationHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ITeleporter;

import java.util.function.Function;

public class InfinityDoorBlockEntity extends TileEntity implements ITickableTileEntity
{

	public PersonalDimension link;

	public BlockPos syncDoorPos;
	public World syncDoorWorld;

	public Portal localPortal;

	public int installedUpgrades = 0;

	public InfinityDoorBlockEntity()
	{
		super(DOFBlockEntities.INFINITY_DOOR.get());
	}

	public void syncWith(InfinityDoorBlockEntity entity)
	{
		entity.syncDoorPos = this.pos;
		entity.syncDoorWorld = this.world;
		this.syncDoorPos = entity.pos;
		this.syncDoorWorld = entity.world;

		int upgrades = Math.max(entity.installedUpgrades, this.installedUpgrades);

		this.installedUpgrades = upgrades;
		entity.installedUpgrades = upgrades;

		world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
		entity.world.notifyBlockUpdate(entity.pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
	}

	public void updateSyncDoor()
	{
		if (isSyncPresent())
		{
			syncDoorWorld.setBlockState(syncDoorPos,
					getSyncEntity().getWorld().getBlockState(getSyncEntity().getPos())
							.with(InfinityDoorBlock.HINGE, getBlockState().get(InfinityDoorBlock.HINGE))
							.with(InfinityDoorBlock.OPEN, getBlockState().get(InfinityDoorBlock.OPEN)),
					10);
			if (MCUtils.immersivePortalsPresent && world.getDimension().getType() == DOFDimensions.INFINITY_DIM
					&& world.getEntitiesWithinAABB(Portal.class, BoxUtils.getBoxInclusive(pos, pos.up()), null).isEmpty())
			{
				deleteSyncPortal();
				PortalManipulation.completeBiWayPortal(getSyncEntity().localPortal, Portal.entityType);
			}
		}

	}

	public boolean isSyncPresent()
	{
		return syncDoorPos != null && syncDoorWorld != null && !syncDoorWorld.getBlockState(syncDoorPos).isAir();
	}
	
	public void deleteLocalPortal()
	{
		deletePortals(world, pos);
	}
	
	public void deleteSyncPortal()
	{
		deletePortals(syncDoorWorld, syncDoorPos);
	}
	
	private void deletePortals(World world, BlockPos pos)
	{
		world.getEntitiesWithinAABB(Portal.class, BoxUtils.getBoxInclusive(pos, pos.up()), null).forEach((portal) ->
		{
			portal.remove();
		});
	}

	public void createSyncedPortals()
	{
		Direction direction = getBlockState().get(InfinityDoorBlock.FACING);
		Direction rightDirection = Direction.byHorizontalIndex(direction.getHorizontalIndex() + 1);
		Vec3d portalPos = new Vec3d(pos).add(0.5, 1, 0.5);
		Quaternion rot = new Quaternion(Vector3f.YP, direction.getOpposite().getHorizontalIndex() * 90, true);

		PersonalDimension personalDim = getOrCreateLinkedDimension();
		if(MCUtils.immersivePortalsPresent)
		{
			deleteSyncPortal();

			localPortal = PortalCreationHelper.spawn(world, portalPos, 1, 2, rightDirection, DOFDimensions.INFINITY_DIM,
					personalDim.getPlayerPosCentered().add(0, 1, 0), true, rot);
		}
		updateSyncDoor();
	}

	public void placeSyncedDoor(World otherWorld, BlockPos otherPos)
	{
		BlockState state = getBlockState();
			otherWorld.setBlockState(otherPos,
					DOFBlocks.GENERATED_INFINITY_DOOR.get().getDefaultState()
							.with(InfinityDoorBlock.HINGE, state.get(InfinityDoorBlock.HINGE))
							.with(InfinityDoorBlock.FACING, MCUtils.immersivePortalsPresent ? Direction.NORTH : Direction.SOUTH)
							.with(InfinityDoorBlock.HALF, DoubleBlockHalf.LOWER));
			otherWorld.setBlockState(otherPos.up(),
					DOFBlocks.GENERATED_INFINITY_DOOR.get().getDefaultState()
							.with(InfinityDoorBlock.HINGE, state.get(InfinityDoorBlock.HINGE))
							.with(InfinityDoorBlock.FACING, MCUtils.immersivePortalsPresent ? Direction.NORTH : Direction.SOUTH)
							.with(InfinityDoorBlock.HALF, DoubleBlockHalf.UPPER));

		InfinityDoorBlockEntity dimInfinityDoor = (InfinityDoorBlockEntity) otherWorld.getTileEntity(otherPos);
		syncWith(dimInfinityDoor);
		this.createSyncedPortals();
	}

	public void sync()
	{
		syncWith(getSyncEntity());
	}

	public InfinityDoorBlockEntity getSyncEntity()
	{
		if (syncDoorWorld == null)
			return null;

		return (InfinityDoorBlockEntity) syncDoorWorld.getTileEntity(syncDoorPos);
	}

	public PersonalDimension getOrCreateLinkedDimension()
	{
		if (link == null)
		{
			link = InfinityDimHelper.getEmptyPersonalDimension(world.getServer());
			link.generate();
		}
		return link;
	}

	private CompoundNBT writeClientData(CompoundNBT compound)
	{
		compound.putInt("Upgrades", installedUpgrades);
		return compound;
	}

	private void readClientData(CompoundNBT compound)
	{
		installedUpgrades = compound.getInt("Upgrades");
	}


	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(getPos(), -1, writeClientData(new CompoundNBT()));
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		super.onDataPacket(net, pkt);
		readClientData(pkt.getNbtCompound());
	}

	@Override
	public CompoundNBT getUpdateTag()
	{
		return writeClientData(super.getUpdateTag());
	}

	@Override
	public void handleUpdateTag(CompoundNBT tag)
	{
		super.handleUpdateTag(tag);
		readClientData(tag);
	}

	@Override
	public void read(CompoundNBT tag)
	{
		if (tag.contains("SyncDoorDimId"))
		{
			syncDoorWorld = McHelper.getServer().getWorld(DimensionType.getById(tag.getInt("SyncDoorDimId")));
			syncDoorPos = new BlockPos(tag.getInt("SyncDoorX"), tag.getInt("SyncDoorY"), tag.getInt("SyncDoorZ"));
		}

		if(tag.contains("Upgrades"))
		{
			installedUpgrades = tag.getInt("Upgrades");
		}

		if (tag.contains("DimOffset"))
			link = InfinityDimHelper.getPersonalDimension(tag.getInt("DimOffset"), installedUpgrades, McHelper.getServer());
		super.read(tag);

	}



	@Override
	public CompoundNBT write(CompoundNBT tag)
	{
		if (link != null)
			tag.putInt("DimOffset", link.getDimensionOffset());
		if (syncDoorWorld != null)
		{
			tag.putInt("SyncDoorDimId", syncDoorWorld.getDimension().getType().getId());

			tag.putInt("SyncDoorX", syncDoorPos.getX());
			tag.putInt("SyncDoorY", syncDoorPos.getY());
			tag.putInt("SyncDoorZ", syncDoorPos.getZ());
		}

		tag.putInt("Upgrades", installedUpgrades);

		return super.write(tag);
	}

	@Override
	public void tick()
	{
		if(!world.isRemote && !MCUtils.immersivePortalsPresent && getBlockState().get(InfinityDoorBlock.HALF) == DoubleBlockHalf.LOWER && syncDoorWorld != null)
		{
			world.getEntitiesWithinAABB(PlayerEntity.class, BoxUtils.getBoxInclusive(pos, pos.up())).forEach(entity ->
			{
				entity.changeDimension(syncDoorWorld.getDimension().getType(), new ITeleporter() {
					@Override
					public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity)
					{

						if(link != null)
						{
							Vec3d pos = link.getPlayerPosCentered().add(0, 0, -1);
							entity.setPositionAndRotation(pos.getX(), pos.getY(), pos.getZ(), 180, 0);
							return entity;
						}
						else
						{
							Direction facing = getSyncEntity().getBlockState().get(InfinityDoorBlock.FACING);
							BlockPos pos = syncDoorPos.add(facing.getOpposite().getDirectionVec());
							entity.setPositionAndRotation(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 180, 0);
							return entity;
						}
					}
				});
			});

		}
	}
}
