package me.benfah.doorsofinfinity.item;

import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Function;

public class LoreBlockItem extends BlockItem
{

    Function<ItemStack, Text>[] functions;

    public LoreBlockItem(Block block, Settings settings, Function<ItemStack, Text>... functions)
    {
        super(block, settings);
        this.functions = functions;
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context)
    {
        super.appendTooltip(stack, world, tooltip, context);
        for(Function<ItemStack, Text> func : functions)
        {
            Text text = func.apply(stack);
            if(text != null)
                tooltip.add(text);
        }
    }
}
