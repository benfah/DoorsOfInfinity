package me.benfah.doorsofinfinity;

import com.qouteall.immersive_portals.render.PortalEntityRenderer;
import me.benfah.doorsofinfinity.block.entity.renderer.InfinityDoorBlockEntityRenderer;
import me.benfah.doorsofinfinity.config.DOFConfig;
import me.benfah.doorsofinfinity.dimension.InfinityModDimension;
import me.benfah.doorsofinfinity.init.*;
import me.benfah.doorsofinfinity.utils.MCUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DOFMod.MOD_ID)
public class DOFMod
{
	public static final String MOD_ID = "doorsofinfinity";

	public DOFMod()
	{
		DOFBlocks.getRegister().register(FMLJavaModLoadingContext.get().getModEventBus());
		DOFBlockEntities.getRegister().register(FMLJavaModLoadingContext.get().getModEventBus());
		DOFEntities.getDeferredRegister().register(FMLJavaModLoadingContext.get().getModEventBus());
		DOFItems.getRegister().register(FMLJavaModLoadingContext.get().getModEventBus());
		DOFDimensions.getRegisters().forEach(reg -> reg.register(FMLJavaModLoadingContext.get().getModEventBus()));

		MinecraftForge.EVENT_BUS.register(this);
		DOFConfig.init();
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::initializeClient);

	}

	@SubscribeEvent
	public void initializeDimensions(RegisterDimensionsEvent modDimensionRegister)
	{
		DOFDimensions.registerDimension();
	}

	public void initializeClient(final FMLClientSetupEvent event)
	{
		MCUtils.immersivePortalsPresent = ModList.get().isLoaded("immersive_portals");

		RenderTypeLookup.setRenderLayer(DOFBlocks.INFINITY_DOOR.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(DOFBlocks.GENERATED_INFINITY_DOOR.get(), RenderType.getCutout());
		ClientRegistry.bindTileEntityRenderer(DOFBlockEntities.INFINITY_DOOR.get(), InfinityDoorBlockEntityRenderer::new);

		Minecraft.getInstance().getRenderManager().register(DOFEntities.BREAKABLE_PORTAL.get(), new PortalEntityRenderer(Minecraft.getInstance().getRenderManager()));
	}

}
