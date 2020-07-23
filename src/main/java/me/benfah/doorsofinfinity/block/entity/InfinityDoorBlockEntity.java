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
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.lwjgl.system.CallbackI;

public class InfinityDoorBlockEntity extends BlockEntity implements Tickable, BlockEntityClientSerializable
{

	public static final int MAX_UPGRADES = 4;

	public PersonalDimension link;

	public BlockPos syncDoorPos;
	public World syncDoorWorld;

	public Portal localPortal;

	public int installedUpgrades = 0;

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

		int upgrades = Math.max(entity.installedUpgrades, this.installedUpgrades);

		this.installedUpgrades = upgrades;
		entity.installedUpgrades = upgrades;

		this.sync();
		entity.sync();
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

			if (MCUtils.immersivePortalsPresent && world.getRegistryKey().equals(DOFDimensions.INFINITY_DIM)
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
			portal.remove();
		});
	}

	private void createSyncedPortals()
	{
		Direction direction = getCachedState().get(InfinityDoorBlock.FACING);
		Direction rightDirection = Direction.fromHorizontal(direction.getHorizontal() + 1);
		Vec3d portalPos = new Vec3d(pos.getX(), pos.getY(), pos.getZ()).add(0.5, 1, 0.5);
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



		syncWith((InfinityDoorBlockEntity) otherWorld.getBlockEntity(otherPos));
		createSyncedPortals();
	}

	@Override
	public void fromClientTag(CompoundTag compoundTag)
	{
		installedUpgrades = compoundTag.getInt("Upgrades");
	}

	@Override
	public CompoundTag toClientTag(CompoundTag compoundTag)
	{
		compoundTag.putInt("Upgrades", installedUpgrades);
		return compoundTag;
	}

	@Override
	public void sync()
	{
		markDirty();
		BlockEntityClientSerializable.super.sync();
	}

	public void syncWithDoor()
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
	public void fromTag(BlockState state, CompoundTag tag)
	{


		if (tag.contains("SyncDoorDimName"))
		{
			syncDoorWorld = MCUtils.getServer().getWorld(RegistryKey.of(Registry.DIMENSION, new Identifier(tag.getString("SyncDoorDimName"))));
			syncDoorPos = new BlockPos(tag.getInt("SyncDoorX"), tag.getInt("SyncDoorY"), tag.getInt("SyncDoorZ"));
		}

		if(tag.contains("Upgrades"))
		{
			installedUpgrades = tag.getInt("Upgrades");
		}

		if (tag.contains("DimOffset"))
			link = InfinityDimHelper.getPersonalDimension(tag.getInt("DimOffset"), installedUpgrades);

		super.fromTag(state, tag);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag)
	{
		if (link != null)
			tag.putInt("DimOffset", link.getDimensionOffset());
		if (syncDoorWorld != null)
		{
			tag.putString("SyncDoorDimName", syncDoorWorld.getRegistryKey().getValue().toString());

			tag.putInt("SyncDoorX", syncDoorPos.getX());
			tag.putInt("SyncDoorY", syncDoorPos.getY());
			tag.putInt("SyncDoorZ", syncDoorPos.getZ());
		}

		tag.putInt("Upgrades", installedUpgrades);

		return super.toTag(tag);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void tick()
	{
		if(!world.isClient && !MCUtils.immersivePortalsPresent && getCachedState().get(InfinityDoorBlock.HALF) == DoubleBlockHalf.LOWER && syncDoorWorld != null)
		{
			world.getEntities(null, BoxUtils.getBoxInclusive(pos, pos.up())).forEach(entity ->
			{
				FabricDimensions.teleport(entity, (ServerWorld) syncDoorWorld, (teleported, destination, direction, v, v1) -> {
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
