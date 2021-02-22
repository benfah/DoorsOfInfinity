package me.benfah.doorsofinfinity.item;

import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TallBlockItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class InfinityDoorItem extends TallBlockItem
{

    public InfinityDoorItem(Block block, Settings settings)
    {
        super(block, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context)
    {
        CompoundTag blockEntityTag = stack.getSubTag("BlockEntityTag");
        if(blockEntityTag != null && !blockEntityTag.isEmpty())
        {
            int dimOffset;
            int upgrades;
            if(blockEntityTag.contains("PersonalDimension"))
            {
                CompoundTag personalDimTag = blockEntityTag.getCompound("PersonalDimension");
                dimOffset = personalDimTag.getInt("DimensionId");
                upgrades = personalDimTag.getInt("Upgrades");
            }
            else
            {
                dimOffset = blockEntityTag.getInt("DimOffset");
                upgrades = blockEntityTag.getInt("Upgrades");
            }

            tooltip.add(new TranslatableText("lore.doorsofinfinity.dim_offset", dimOffset).formatted(Formatting.GRAY));
            tooltip.add(new TranslatableText("text.doorsofinfinity.installed_upgrades", upgrades).formatted(Formatting.GRAY));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
