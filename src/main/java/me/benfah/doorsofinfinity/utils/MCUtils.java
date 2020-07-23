package me.benfah.doorsofinfinity.utils;

import java.lang.ref.WeakReference;

import net.minecraft.server.MinecraftServer;

public class MCUtils
{
	
	public static WeakReference<MinecraftServer> mcServerReference;
	
	public static MinecraftServer getServer()
	{
		return mcServerReference.get();
	}


	public static boolean immersivePortalsPresent = false;

	
}
