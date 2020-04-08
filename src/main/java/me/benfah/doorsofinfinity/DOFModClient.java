package me.benfah.doorsofinfinity;

import me.benfah.doorsofinfinity.init.DOFBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class DOFModClient implements ClientModInitializer
{

	@Override
	public void onInitializeClient()
	{
		BlockRenderLayerMap.INSTANCE.putBlock(DOFBlocks.INFINITY_DOOR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(DOFBlocks.SIMULATED_INFINITY_DOOR, RenderLayer.getCutout());

	}

}
