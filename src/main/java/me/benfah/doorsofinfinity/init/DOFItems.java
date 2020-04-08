package me.benfah.doorsofinfinity.init;

import me.benfah.doorsofinfinity.DOFMod;
import me.benfah.doorsofinfinity.item.InfinityBlockItem;
import me.benfah.doorsofinfinity.item.InfinityDoorBlockItem;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.TallBlockItem;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;

public class DOFItems
{

	public static final String ANCIENT_MADE = Util.createTranslationKey("lore",
			new Identifier(DOFMod.MOD_ID, "ancient_made"));

	public static TallBlockItem INFINITY_DOOR;
	public static TallBlockItem SIMULATED_INFINITY_DOOR;

	public static BlockItem BLOCK_OF_INFINITY;
	public static BlockItem SIMULATED_BLOCK_OF_INFINITY;

	public static void init()
	{
		INFINITY_DOOR = registerTallBlockItem(DOFBlocks.INFINITY_DOOR, ANCIENT_MADE);
		SIMULATED_INFINITY_DOOR = registerTallBlockItem(DOFBlocks.SIMULATED_INFINITY_DOOR);

		BLOCK_OF_INFINITY = registerBlockItem(DOFBlocks.BLOCK_OF_INFINITY, ANCIENT_MADE);
		SIMULATED_BLOCK_OF_INFINITY = registerBlockItem(DOFBlocks.SIMULATED_BLOCK_OF_INFINITY);
	}

	public static TallBlockItem registerTallBlockItem(Block block, String... translatableLoreText)
	{
		InfinityDoorBlockItem blockItem;
		Settings settings = new Settings().group(ItemGroup.MISC);
		
		blockItem = new InfinityDoorBlockItem(block, settings, translatableLoreText);

		Identifier identifier = Registry.BLOCK.getId(block);
		return Registry.register(Registry.ITEM, identifier, blockItem);
	}

	public static BlockItem registerBlockItem(Block block, String... translatableLoreText)
	{
		InfinityBlockItem blockItem;
		Settings settings = new Settings().group(ItemGroup.MISC);

		blockItem = new InfinityBlockItem(block, settings, translatableLoreText);

		Identifier identifier = Registry.BLOCK.getId(block);
		return Registry.register(Registry.ITEM, identifier, blockItem);
	}

}
