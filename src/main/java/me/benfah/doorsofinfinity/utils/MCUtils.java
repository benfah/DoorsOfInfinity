package me.benfah.doorsofinfinity.utils;

import java.lang.ref.WeakReference;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;

public class MCUtils
{
	
	public static WeakReference<MinecraftServer> mcServerReference;
	
	public static MinecraftServer getServer()
	{
		return mcServerReference.get();
	}

	public static void init()
	{
		immersivePortalsPresent = FabricLoader.getInstance().isModLoaded("imm_ptl_core");
	}
	
	public static boolean isIPPresent()
	{
		return immersivePortalsPresent;
	}
	
	private static boolean immersivePortalsPresent = false;

	
}
