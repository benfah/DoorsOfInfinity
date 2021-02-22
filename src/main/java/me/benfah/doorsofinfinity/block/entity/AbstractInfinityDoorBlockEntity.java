package me.benfah.doorsofinfinity.block.entity;

import com.qouteall.immersive_portals.portal.Portal;
import me.benfah.doorsofinfinity.block.InfinityDoorBlock;
import me.benfah.doorsofinfinity.utils.BoxUtils;
import me.benfah.doorsofinfinity.utils.MCUtils;
import me.benfah.doorsofinfinity.utils.PortalCreationHelper;
import me.benfah.doorsofinfinity.utils.VecUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public abstract class AbstractInfinityDoorBlockEntity<S extends AbstractInfinityDoorBlockEntity<S>> extends BlockEntity
{


	public BlockPos syncDoorPos;
	public World syncDoorWorld;

	public Portal localPortal;

	public AbstractInfinityDoorBlockEntity(BlockEntityType<?> type)
	{
		super(type);
	}


	private boolean isUpper()
	{
		return getCachedState().get(InfinityDoorBlock.HALF) == DoubleBlockHalf.UPPER;
	}

	public void syncWith(S entity)
	{
		entity.syncDoorPos = this.pos;
		entity.syncDoorWorld = this.world;
		this.syncDoorPos = entity.pos;
		this.syncDoorWorld = entity.world;
		
		if(localPortal != null && localPortal.isAlive())
		{
			createSyncedPortals();
		}
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
		world.getEntitiesByClass(Portal.class, BoxUtils.getBoxInclusive(pos, pos.up()), null).forEach((portal) ->
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

		if(MCUtils.isIPPresent())
		{
			deleteSyncPortal();

			localPortal = PortalCreationHelper.spawn(world, portalPos, 1, 2, rightDirection, syncDoorWorld.getRegistryKey(),
					VecUtils.toVec3d(syncDoorPos), true, rot);
		}
		updateSyncDoor();
	}


	public void syncWithDoor()
	{
		syncWith(getSyncEntity());
	}

	public S getSyncEntity()
	{
		if (syncDoorWorld == null)
			return null;

		return (S) syncDoorWorld.getBlockEntity(syncDoorPos);
	}



	@Override
	public void fromTag(BlockState state, CompoundTag tag)
	{


		if (tag.contains("SyncDoorDimName"))
		{
			syncDoorWorld = MCUtils.getServer().getWorld(RegistryKey.of(Registry.DIMENSION, new Identifier(tag.getString("SyncDoorDimName"))));
			syncDoorPos = new BlockPos(tag.getInt("SyncDoorX"), tag.getInt("SyncDoorY"), tag.getInt("SyncDoorZ"));
		}

		super.fromTag(state, tag);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag)
	{
		if (syncDoorWorld != null)
		{
			tag.putString("SyncDoorDimName", syncDoorWorld.getRegistryKey().getValue().toString());

			tag.putInt("SyncDoorX", syncDoorPos.getX());
			tag.putInt("SyncDoorY", syncDoorPos.getY());
			tag.putInt("SyncDoorZ", syncDoorPos.getZ());
		}

		return super.toTag(tag);
	}
	
}
