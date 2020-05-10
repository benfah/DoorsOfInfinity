package me.benfah.doorsofinfinity.item;

import me.benfah.doorsofinfinity.block.InfinityDoorBlock;
import me.benfah.doorsofinfinity.block.entity.InfinityDoorBlockEntity;
import me.benfah.doorsofinfinity.init.DOFBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class DimensionalShardItem extends Item
{
    public DimensionalShardItem(Properties settings)
    {
        super(settings);
    }



    @Override
    public ActionResultType onItemUse(ItemUseContext context)
    {
        BlockState state = context.getWorld().getBlockState(context.getPos());
        if(!context.getWorld().isRemote && state.getBlock() == DOFBlocks.INFINITY_DOOR.get() && context.getPlayer().isCrouching() && context.getHand() == Hand.MAIN_HAND)
        {
            BlockPos blockEntityPos = state.get(InfinityDoorBlock.HALF) == DoubleBlockHalf.LOWER ? context.getPos() : context.getPos().down();
            InfinityDoorBlockEntity blockEntity = (InfinityDoorBlockEntity) context.getWorld().getTileEntity(blockEntityPos);
            if(blockEntity.getOrCreateLinkedDimension().upgrade())
            {
                context.getPlayer().getHeldItemMainhand().shrink(1);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }



}
