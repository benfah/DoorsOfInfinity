package me.benfah.doorsofinfinity;

import me.benfah.doorsofinfinity.command.TestCommand;
import me.benfah.doorsofinfinity.config.DOFConfig;
import me.benfah.doorsofinfinity.init.DOFBlockEntities;
import me.benfah.doorsofinfinity.init.DOFBlocks;
import me.benfah.doorsofinfinity.init.DOFChunkGen;
import me.benfah.doorsofinfinity.init.DOFDimensions;
import me.benfah.doorsofinfinity.init.DOFItems;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.fabricmc.loader.api.FabricLoader;

public class DOFMod implements ModInitializer
{
	
	public static final String MOD_ID = "doorsofinfinity";
	
	@Override
	public void onInitialize()
	{
		DOFBlocks.init();
		DOFItems.init();
		DOFBlockEntities.init();
		DOFChunkGen.init();
		DOFDimensions.init();

		AutoConfig.register(DOFConfig.class, JanksonConfigSerializer::new);

		CommandRegistry.INSTANCE.register(false, (dispatcher) -> TestCommand.register(dispatcher));
	}

}
