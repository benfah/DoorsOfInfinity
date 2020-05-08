package me.benfah.doorsofinfinity.block.entity;

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
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class InfinityDoorBlockEntity extends BlockEntity implements Tickable
{

	public PersonalDimension link;

	public BlockPos syncDoorPos;
	public World syncDoorWorld;

	public Portal localPortal;

	public InfinityDoorBlockEntity()
	{
		super(DOFBlockEntities.INFINITY_DOOR);
	}

	public void syncWith(InfinityDoorBlockEntity entity)
	{
		entity.syncDoorPos = this.pos;
		entity.syncDoorWorld = this.world;
		this.syncDoorPos = entity.pos;
		this.syncDoorWorld = entity.world;
	}

	public void updateSyncDoor()
	{
		if (isSyncPresent())
		{
			syncDoorWorld.setBlockState(syncDoorPos,
					getSyncEntity().getWorld().getBlockState(getSyncEntity().getPos())
							.with(InfinityDoorBlock.HINGE, getCachedState().get(InfinityDoorBlock.HINGE))
							.with(InfinityDoorBlock.OPEN, getCachedState().get(InfinityDoorBlock.OPEN)),
					10);

			if (MCUtils.immersivePortalsPresent && world.getDimension().getType() == DOFDimensions.INFINITY_DIM
					&& world.getEntities(Portal.class, BoxUtils.getBoxInclusive(pos, pos.up()), null).isEmpty())
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
		world.getEntities(Portal.class, BoxUtils.getBoxInclusive(pos, pos.up()), null).forEach((portal) ->
		{
//			PortalManipulation.removeConnectedPortals(portal, (t) ->
//			{
//			});
			portal.remove();
		});
	}

	public void createSyncedPortals()
	{
		Direction direction = getCachedState().get(InfinityDoorBlock.FACING);
		Direction rightDirection = Direction.fromHorizontal(direction.getHorizontal() + 1);
		Vec3d portalPos = new Vec3d(pos).add(0.5, 1, 0.5);
		Quaternion rot = new Quaternion(Vector3f.POSITIVE_Y, direction.getOpposite().getHorizontal() * 90, true);

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
		BlockState state = getCachedState();
			otherWorld.setBlockState(otherPos,
					DOFBlocks.INFINITY_DOOR.getDefaultState()
							.with(InfinityDoorBlock.HINGE, state.get(InfinityDoorBlock.HINGE))
							.with(InfinityDoorBlock.FACING, MCUtils.immersivePortalsPresent ? Direction.NORTH : Direction.SOUTH)
							.with(InfinityDoorBlock.HALF, DoubleBlockHalf.LOWER));
			otherWorld.setBlockState(otherPos.up(),
					DOFBlocks.INFINITY_DOOR.getDefaultState()
							.with(InfinityDoorBlock.HINGE, state.get(InfinityDoorBlock.HINGE))
							.with(InfinityDoorBlock.FACING, MCUtils.immersivePortalsPresent ? Direction.NORTH : Direction.SOUTH)
							.with(InfinityDoorBlock.HALF, DoubleBlockHalf.UPPER));

		InfinityDoorBlockEntity dimInfinityDoor = (InfinityDoorBlockEntity) otherWorld.getBlockEntity(otherPos);
		syncWith(dimInfinityDoor);
	}

	public void sync()
	{
		syncWith(getSyncEntity());
	}

	public InfinityDoorBlockEntity getSyncEntity()
	{
		if (syncDoorWorld == null)
			return null;

		return (InfinityDoorBlockEntity) syncDoorWorld.getBlockEntity(syncDoorPos);
	}

	public PersonalDimension getOrCreateLinkedDimension()
	{
		if (link == null)
		{
			link = InfinityDimHelper.getEmptyPersonalDimension();
			link.generate();
		}
		return link;
	}

	@Override
	public void fromTag(CompoundTag tag)
	{
		if (tag.contains("SyncDoorDimId"))
		{
			syncDoorWorld = MCUtils.getServer().getWorld(DimensionType.byRawId(tag.getInt("SyncDoorDimId")));
			syncDoorPos = new BlockPos(tag.getInt("SyncDoorX"), tag.getInt("SyncDoorY"), tag.getInt("SyncDoorZ"));
		}

		super.fromTag(tag);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag)
	{
		if (link != null)
			tag.putInt("DimOffset", link.getDimensionOffset());
		if (syncDoorWorld != null)
		{
			tag.putInt("SyncDoorDimId", syncDoorWorld.getDimension().getType().getRawId());

			tag.putInt("SyncDoorX", syncDoorPos.getX());
			tag.putInt("SyncDoorY", syncDoorPos.getY());
			tag.putInt("SyncDoorZ", syncDoorPos.getZ());
		}
		return super.toTag(tag);
	}

	@Override
	public void tick()
	{
		if(!world.isClient && !MCUtils.immersivePortalsPresent && getCachedState().get(InfinityDoorBlock.HALF) == DoubleBlockHalf.LOWER && syncDoorWorld != null)
		{
			world.getEntities(null, BoxUtils.getBoxInclusive(pos, pos.up())).forEach(entity ->
			{
				FabricDimensions.teleport(entity, syncDoorWorld.getDimension().getType(), (entity1, serverWorld, direction, v, v1) -> {
					if(link != null)
						return new BlockPattern.TeleportTarget(link.getPlayerPosCentered().add(0, 0, -1), Vec3d.ZERO, 180);
					else
					{
						Direction facing = getSyncEntity().getCachedState().get(InfinityDoorBlock.FACING);
						BlockPos pos = syncDoorPos.add(facing.getOpposite().getVector());
						return new BlockPattern.TeleportTarget(new Vec3d(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5), Vec3d.ZERO, 180);
					}
				});
			});

		}
	}
}
