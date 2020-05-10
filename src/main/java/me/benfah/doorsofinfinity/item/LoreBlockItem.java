package me.benfah.doorsofinfinity.item;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Function;

public class LoreBlockItem extends BlockItem
{

    Function<ItemStack, ITextComponent>[] functions;

    public LoreBlockItem(Block block, Properties settings, Function<ItemStack, ITextComponent>... functions)
    {
        super(block, settings);
        this.functions = functions;
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag context)
    {
        super.addInformation(stack, world, tooltip, context);
        for(Function<ItemStack, ITextComponent> func : functions)
        {
            ITextComponent text = func.apply(stack);
            if(text != null)
                tooltip.add(text);
        }
    }
}
