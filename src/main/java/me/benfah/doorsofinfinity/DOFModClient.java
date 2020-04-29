package me.benfah.doorsofinfinity;

import me.benfah.doorsofinfinity.block.entity.renderer.InfinityDoorBlockEntityRenderer;
import me.benfah.doorsofinfinity.init.DOFBlockEntities;
import me.benfah.doorsofinfinity.init.DOFBlocks;
import me.benfah.doorsofinfinity.init.DOFEntities;
import me.benfah.doorsofinfinity.utils.MCUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

public class DOFModClient implements ClientModInitializer
{

	@Override
	public void onInitializeClient()
	{
		BlockRenderLayerMap.INSTANCE.putBlock(DOFBlocks.INFINITY_DOOR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(DOFBlocks.SIMULATED_INFINITY_DOOR, RenderLayer.getCutout());
//		BlockRenderLayerMap.INSTANCE.putBlock(DOFBlocks.PHOTON_TRANSMITTER, RenderLayer.getCutout());

		if(!MCUtils.immersivePortalsPresent)
		{
			BlockEntityRendererRegistry.INSTANCE.register(DOFBlockEntities.INFINITY_DOOR, InfinityDoorBlockEntityRenderer::new);
		}
		else
		{
			DOFEntities.initClient();
		}
	}

}
