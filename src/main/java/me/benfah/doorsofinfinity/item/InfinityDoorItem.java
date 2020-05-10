package me.benfah.doorsofinfinity.item;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TallBlockItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.List;

public class InfinityDoorItem extends TallBlockItem
{

    public InfinityDoorItem(Block block, Properties settings)
    {
        super(block, settings);
    }



    @Override
    public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag context)
    {
        CompoundNBT blockEntityTag = stack.getChildTag("BlockEntityTag");
        if(blockEntityTag != null && !blockEntityTag.isEmpty())
        {
            int dimOffset = blockEntityTag.getInt("DimOffset");
            int upgrades = blockEntityTag.getInt("Upgrades");
            tooltip.add(new TranslationTextComponent("lore.doorsofinfinity.dim_offset", dimOffset).applyTextStyle(TextFormatting.GRAY));
            tooltip.add(new TranslationTextComponent("text.doorsofinfinity.installed_upgrades", upgrades).applyTextStyle(TextFormatting.GRAY));
        }
        super.addInformation(stack, world, tooltip, context);
    }
}
