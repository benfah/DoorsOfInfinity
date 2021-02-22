package me.benfah.doorsofinfinity;

import me.benfah.doorsofinfinity.config.DOFConfig;
import me.benfah.doorsofinfinity.init.*;
import me.benfah.doorsofinfinity.utils.MCUtils;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.java.games.input.Keyboard;
import net.minecraft.client.MinecraftClient;

public class DOFMod implements ModInitializer
{
	
	public static final String MOD_ID = "doorsofinfinity";
	
	@Override
	public void onInitialize()
	{
		MCUtils.init();

		DOFBlocks.init();
		DOFItems.init();
		DOFBlockEntities.init();
		DOFDimensions.init();
		
		if(MCUtils.isIPPresent())
		DOFEntities.init();
		
		AutoConfig.register(DOFConfig.class, JanksonConfigSerializer::new);
	}

}
