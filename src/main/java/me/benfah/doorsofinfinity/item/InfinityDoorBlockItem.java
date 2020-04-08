package me.benfah.doorsofinfinity.item;

import java.util.List;

import me.benfah.doorsofinfinity.DOFMod;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TallBlockItem;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.World;

public class InfinityDoorBlockItem extends TallBlockItem
{
	public static String DIM_OFFSET = Util.createTranslationKey("lore", new Identifier(DOFMod.MOD_ID, "dim_offset"));

	
	String[] translatableLoreTexts;
	public InfinityDoorBlockItem(Block block, Settings settings, String... translatableLoreTexts)
	{
		super(block, settings);
		this.translatableLoreTexts = translatableLoreTexts;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context)
	{
		for(String loreText : translatableLoreTexts)
		tooltip.add(new TranslatableText(loreText).formatted(Formatting.GRAY));
		
		if (stack.getSubTag("BlockEntityTag") != null)
			tooltip.add(new TranslatableText(DIM_OFFSET, stack.getSubTag("BlockEntityTag").getInt("DimOffset")).formatted(Formatting.GRAY));
		
		super.appendTooltip(stack, world, tooltip, context);
		
	}
	
}
