package me.benfah.doorsofinfinity.item;

import java.util.List;

import me.benfah.doorsofinfinity.DOFMod;
import me.benfah.doorsofinfinity.init.DOFItems;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.World;

public class InfinityBlockItem extends BlockItem
{
	
	
	String[] translatableLoreTexts;

	public InfinityBlockItem(Block block, Settings settings, String... translatableLoreTexts)
	{
		super(block, settings);
		this.translatableLoreTexts = translatableLoreTexts;
	}
	
//	public static void appendDimensionId(ItemStack stack, World)
//	{
//		stack.getItem().appendTooltip(stack, world, tooltip, context);
//	}
	
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context)
	{
		for (String loreText : translatableLoreTexts)
			tooltip.add(new TranslatableText(loreText).formatted(Formatting.GRAY));

		super.appendTooltip(stack, world, tooltip, context);
		
	}

}
