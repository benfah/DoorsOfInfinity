package me.benfah.doorsofinfinity.utils;

import com.qouteall.immersive_portals.chunk_loading.ChunkVisibilityManager;
import com.qouteall.immersive_portals.chunk_loading.DimensionalChunkPos;
import com.qouteall.immersive_portals.chunk_loading.NewChunkTrackingGraph;
import com.qouteall.immersive_portals.portal.Portal;
import com.qouteall.immersive_portals.portal.PortalManipulation;
import net.minecraft.util.math.*;
import com.qouteall.immersive_portals.my_util.IntBox;
import com.qouteall.immersive_portals.portal.Portal;
import com.qouteall.immersive_portals.portal.PortalManipulation;

import me.benfah.doorsofinfinity.entity.BreakablePortal;
import me.benfah.doorsofinfinity.init.DOFEntities;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class PortalCreationHelper
{


	public static Portal spawn(World world, Vec3d pos, double width, double height, Vec3i axisW, Vec3i axisH,
			DimensionType dimensionTo, Vec3d dest, boolean teleportable, Quaternion rot, boolean biWay)
	{
		Portal portal = new Portal(Portal.entityType, world);
		
		portal.width = width;
		portal.height = height;
		portal.axisH = new Vec3d(axisH.getX(), axisH.getY(), axisH.getZ());
		portal.axisW = new Vec3d(axisW.getX(), axisW.getY(), axisW.getZ());
		portal.dimensionTo = dimensionTo;
		portal.destination = dest;
		portal.teleportable = teleportable;
		portal.cullableXEnd = 0;
		portal.cullableYEnd = 0;
		portal.cullableXStart = 0;
		portal.cullableYStart = 0;
		if(rot != null)
		portal.rotation = rot;
		portal.setPos(pos.getX(), pos.getY(), pos.getZ());
		world.spawnEntity(portal);
		if(biWay)
		PortalManipulation.completeBiWayPortal(portal, Portal.entityType);
		return portal;
	}

	public static BreakablePortal spawnBreakable(World world, Vec3d pos, double width, double height, Vec3i axisW,
												 Vec3i axisH, DimensionType dimensionTo, Vec3d dest,
												 boolean teleportable, Quaternion rot, boolean biWay,
												 IntBox transmitterBox, IntBox glassBox, World transmitterWorld)
	{
		BreakablePortal portal = new BreakablePortal(world);

		portal.width = width;
		portal.height = height;
		portal.axisH = new Vec3d(axisH.getX(), axisH.getY(), axisH.getZ());
		portal.axisW = new Vec3d(axisW.getX(), axisW.getY(), axisW.getZ());
		portal.dimensionTo = dimensionTo;
		portal.destination = dest;
		portal.teleportable = teleportable;
		portal.cullableXEnd = 0;
		portal.cullableYEnd = 0;
		portal.cullableXStart = 0;
		portal.cullableYStart = 0;

		portal.transmitterArea = transmitterBox;
		portal.transmitterWorld = transmitterWorld;

		portal.glassArea = glassBox;

		if(rot != null)
			portal.rotation = rot;
		portal.setPos(pos.getX(), pos.getY(), pos.getZ());
		world.spawnEntity(portal);
		if(biWay)
			PortalManipulation.completeBiWayPortal(portal, Portal.entityType);
		return portal;
	}

	public static Portal spawn(World world, Vec3d pos, double width, double height, Direction axisW,
							   DimensionType dimensionTo, Vec3d dest, boolean teleportable, Quaternion rot, boolean biWay)
	{
		return spawn(world, pos, width, height, axisW.getVector(), Direction.UP.getVector(), dimensionTo, dest, teleportable, rot, biWay);
	}

	public static Portal spawn(World world, Vec3d pos, double width, double height, Direction axisW,
							   DimensionType dimensionTo, Vec3d dest, boolean teleportable, Quaternion rot)
	{
		return spawn(world, pos, width, height, axisW.getVector(), Direction.UP.getVector(), dimensionTo, dest, teleportable, rot, true);
	}

}
