package me.benfah.doorsofinfinity.utils;

import com.qouteall.immersive_portals.portal.Portal;
import com.qouteall.immersive_portals.portal.PortalManipulation;

import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class PortalCreationHelper
{

	public static Portal spawn(World world, Vec3d pos, double width, double height, Direction axisW,
			DimensionType dimensionTo, Vec3d dest, boolean teleportable, Quaternion rot)
	{
		Portal portal = new Portal(Portal.entityType, world);
		
		portal.width = width;
		portal.height = height;
		portal.axisH = new Vec3d(Direction.UP.getUnitVector());
		portal.axisW = new Vec3d(axisW.getUnitVector());
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
		PortalManipulation.completeBiWayPortal(portal, Portal.entityType);
		return portal;
	}
	
}
