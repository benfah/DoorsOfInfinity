package me.benfah.doorsofinfinity.utils;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class VecUtils
{
	
	public static Vec3d toVec3d(Vec3i vec3i)
	{
		return new Vec3d(vec3i.getX(), vec3i.getY(), vec3i.getZ());
	}
	
}
