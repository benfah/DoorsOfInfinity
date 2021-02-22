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
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class InfinityDoorBlockEntity extends AbstractInfinityDoorBlockEntity<InfinityDoorBlockEntity> implements BlockEntityClientSerializable
{

	public PersonalDimension link;

	public InfinityDoorBlockEntity()
	{
		super(DOFBlockEntities.INFINITY_DOOR);
	}
	
	@Override
	public void syncWith(InfinityDoorBlockEntity entity)
	{
		super.syncWith(entity);
		if(link != null)
		{
			entity.link = link;
		}
		this.sync();
		entity.sync();
	}
	
	public void updateSyncDoor()
	{
		super.updateSyncDoor();
		if (isSyncPresent())
		{
			if (MCUtils.isIPPresent() && world.getRegistryKey().equals(DOFDimensions.INFINITY_DIM)
					&& world.getEntitiesByClass(Portal.class, BoxUtils.getBoxInclusive(pos, pos.up()), null).isEmpty())
			{
				deleteSyncPortal();
				PortalManipulation.completeBiWayPortal(getSyncEntity().localPortal, Portal.entityType);
			}
		}

	}

	private void createSyncedPortals()
	{
		Direction direction = getCachedState().get(InfinityDoorBlock.FACING);
		Direction rightDirection = Direction.fromHorizontal(direction.getHorizontal() + 1);
		Vec3d portalPos = new Vec3d(pos.getX(), pos.getY(), pos.getZ()).add(0.5, 1, 0.5);
		Quaternion rot = new Quaternion(Vector3f.POSITIVE_Y, direction.getOpposite().getHorizontal() * 90, true);

		PersonalDimension personalDim = getOrCreateLinkedDimension();
		if(MCUtils.isIPPresent())
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
					DOFBlocks.GENERATED_INFINITY_DOOR.getDefaultState()
							.with(InfinityDoorBlock.HINGE, state.get(InfinityDoorBlock.HINGE))
							.with(InfinityDoorBlock.FACING, MCUtils.isIPPresent() ? Direction.NORTH : Direction.SOUTH)
							.with(InfinityDoorBlock.HALF, DoubleBlockHalf.LOWER));
			otherWorld.setBlockState(otherPos.up(),
					DOFBlocks.GENERATED_INFINITY_DOOR.getDefaultState()
							.with(InfinityDoorBlock.HINGE, state.get(InfinityDoorBlock.HINGE))
							.with(InfinityDoorBlock.FACING, MCUtils.isIPPresent() ? Direction.NORTH : Direction.SOUTH)
							.with(InfinityDoorBlock.HALF, DoubleBlockHalf.UPPER));



		syncWith((InfinityDoorBlockEntity) otherWorld.getBlockEntity(otherPos));
		createSyncedPortals();
	}

	@Override
	public void fromClientTag(CompoundTag compoundTag)
	{
		if(compoundTag.contains("PersonalDimension"))
		this.link = InfinityDimHelper.fromTag(compoundTag.getCompound("PersonalDimension"), true);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag compoundTag)
	{
		if(link != null)
		compoundTag.put("PersonalDimension", link.toTag(new CompoundTag()));
		return compoundTag;
	}

	@Override
	public void sync()
	{
		markDirty();
		BlockEntityClientSerializable.super.sync();
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
	public void fromTag(BlockState state, CompoundTag tag)
	{
		if (tag.contains("DimOffset"))
			link = InfinityDimHelper.getPersonalDimension(tag.getInt("DimOffset"), tag.contains("Upgrades") ? tag.getInt("Upgrades") : 0, true);
		
		if(tag.contains("PersonalDimension"))
		{
			link = InfinityDimHelper.fromTag(tag.getCompound("PersonalDimension"), true);
		}
		
		super.fromTag(state, tag);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag)
	{

		if(link != null)
		{
			tag.put("PersonalDimension", link.toTag(new CompoundTag()));
		}

		return super.toTag(tag);
	}
	
}
