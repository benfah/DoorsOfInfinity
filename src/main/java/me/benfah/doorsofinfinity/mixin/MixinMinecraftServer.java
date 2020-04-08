package me.benfah.doorsofinfinity.mixin;

import java.io.File;
import java.lang.ref.WeakReference;
import java.net.Proxy;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.datafixers.DataFixer;

import me.benfah.doorsofinfinity.utils.MCUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.UserCache;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer
{

	@Inject(method = "<init>", at = @At(value = "RETURN"))
	public void onInit(File gameDir, Proxy proxy, DataFixer dataFixer, CommandManager commandManager,
			YggdrasilAuthenticationService authService, MinecraftSessionService sessionService,
			GameProfileRepository gameProfileRepository, UserCache userCache,
			WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory, String levelName, CallbackInfo info)
	{
		MCUtils.mcServerReference = new WeakReference<>((MinecraftServer) ((Object) this));
	}

}
