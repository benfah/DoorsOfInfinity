package me.benfah.doorsofinfinity.init;

import com.qouteall.immersive_portals.McHelper;
import me.benfah.doorsofinfinity.DOFMod;
import me.benfah.doorsofinfinity.item.InfinityDoorItem;
import me.benfah.doorsofinfinity.item.LoreBlockItem;
import me.benfah.doorsofinfinity.item.PhotonLinkItem;
import me.benfah.doorsofinfinity.utils.MCUtils;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.item.Item.Settings;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;

import java.util.function.BiFunction;
import java.util.function.Function;

public class DOFItems
{


	public static final String ANCIENT_MADE = Util.createTranslationKey("lore",
			new Identifier(DOFMod.MOD_ID, "ancient_made"));

	public static Item INFINITY_DOOR;
	public static Item SIMULATED_INFINITY_DOOR;

	public static Item BLOCK_OF_INFINITY;
	public static Item SIMULATED_BLOCK_OF_INFINITY;

	public static Item PHOTON_TRANSMITTER;
	public static Item PHOTON_LINK;


	public static final ItemGroup DOF_GROUP = FabricItemGroupBuilder.build(new Identifier(DOFMod.MOD_ID, "items"), () -> new ItemStack(INFINITY_DOOR));

	public static void init()
	{
		INFINITY_DOOR = registerBlockItem(DOFBlocks.INFINITY_DOOR, new Settings(), InfinityDoorItem::new);
		SIMULATED_INFINITY_DOOR = registerBlockItem(DOFBlocks.SIMULATED_INFINITY_DOOR, new Settings().group(DOF_GROUP).maxCount(1), InfinityDoorItem::new);

		BLOCK_OF_INFINITY = registerBlockItem(DOFBlocks.BLOCK_OF_INFINITY, new Settings(), BlockItem::new);
		SIMULATED_BLOCK_OF_INFINITY = registerBlockItem(DOFBlocks.SIMULATED_BLOCK_OF_INFINITY, new Settings().group(DOF_GROUP), BlockItem::new);

		PHOTON_TRANSMITTER = registerBlockItem(DOFBlocks.PHOTON_TRANSMITTER, new Settings().group(DOF_GROUP),
				(block, settings) -> new LoreBlockItem(block, settings,
						stack -> !MCUtils.immersivePortalsPresent ? new TranslatableText("lore.doorsofinfinity.ip_not_present").formatted(Formatting.GRAY) : null));

		PHOTON_LINK = registerItem("photon_link", new PhotonLinkItem(new Settings().group(DOF_GROUP).maxCount(1)));
	}

	public static <T extends Item> T registerItem(String name, T item)
	{
		return Registry.register(Registry.ITEM, new Identifier(DOFMod.MOD_ID, name), item);
	}

	public static <T extends Item> T registerBlockItem(Block block, Settings itemSettings, BiFunction<Block, Settings, T> itemFactory)
	{
		T item = itemFactory.apply(block, itemSettings);
		return registerItem(Registry.BLOCK.getId(block).getPath(), item);
	}

}
