package me.benfah.doorsofinfinity;

import me.benfah.doorsofinfinity.config.DOFConfig;
import me.benfah.doorsofinfinity.init.*;
import me.benfah.doorsofinfinity.utils.MCUtils;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class DOFMod implements ModInitializer
{
	
	public static final String MOD_ID = "doorsofinfinity";
	
	@Override
	public void onInitialize()
	{
		MCUtils.immersivePortalsPresent = FabricLoader.getInstance().isModLoaded("immersive_portals");

		DOFBlocks.init();
		DOFItems.init();
		DOFBlockEntities.init();
		DOFDimensions.init();
		if(MCUtils.immersivePortalsPresent)
		DOFEntities.init();

		AutoConfig.register(DOFConfig.class, JanksonConfigSerializer::new);
		
	}

}
