package me.benfah.doorsofinfinity.item;

import me.benfah.doorsofinfinity.block.InfinityDoorBlock;
import me.benfah.doorsofinfinity.block.entity.InfinityDoorBlockEntity;
import me.benfah.doorsofinfinity.init.DOFBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class DimensionalShardItem extends Item
{
    public DimensionalShardItem(Settings settings)
    {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context)
    {
        BlockState state = context.getWorld().getBlockState(context.getBlockPos());
        if(!context.getWorld().isClient &&state.getBlock() == DOFBlocks.SIMULATED_INFINITY_DOOR && context.getPlayer().isSneaking() && context.getHand() == Hand.MAIN_HAND)
        {
            BlockPos blockEntityPos = state.get(InfinityDoorBlock.HALF) == DoubleBlockHalf.LOWER ? context.getBlockPos() : context.getBlockPos().down();
            InfinityDoorBlockEntity blockEntity = (InfinityDoorBlockEntity) context.getWorld().getBlockEntity(blockEntityPos);
            if(blockEntity.getOrCreateLinkedDimension().upgrade())
            {
                context.getPlayer().getMainHandStack().decrement(1);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

}
