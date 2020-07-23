package me.benfah.doorsofinfinity.mixin;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.datafixers.DataFixer;
import me.benfah.doorsofinfinity.utils.MCUtils;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.UserCache;
import net.minecraft.util.registry.RegistryTracker;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.ref.WeakReference;
import java.net.Proxy;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer
{

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    public void onInit(Thread thread, RegistryTracker.Modifiable modifiable, LevelStorage.Session session, 
    		SaveProperties saveProperties, ResourcePackManager<ResourcePackProfile> resourcePackManager, 
    		Proxy proxy, DataFixer dataFixer, ServerResourceManager serverResourceManager, 
    		MinecraftSessionService minecraftSessionService, GameProfileRepository gameProfileRepository,
    		UserCache userCache, WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory, 
    		CallbackInfo info)
    {
        MCUtils.mcServerReference = new WeakReference<>((MinecraftServer) ((Object) this));
    }

}
