package me.benfah.doorsofinfinity.utils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class BoxUtils
{
	
	public static Box getBoxInclusive(BlockPos pos1, BlockPos pos2)
	{
		return new Box(new Vec3d(pos1.getX(), pos1.getY(), pos1.getZ()), new Vec3d(pos2.getX() + 1, pos2.getY() + 1, pos2.getZ() + 1));
	}
	
}
