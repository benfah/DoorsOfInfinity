package me.benfah.doorsofinfinity.block;

import me.benfah.doorsofinfinity.block.entity.InfinityDoorBlockEntity;
import me.benfah.doorsofinfinity.config.DOFConfig;
import me.benfah.doorsofinfinity.dimension.InfinityDimHelper;
import me.benfah.doorsofinfinity.dimension.InfinityDimHelper.PersonalDimension;
import me.benfah.doorsofinfinity.init.DOFBlocks;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.function.Predicate;

public class InfinityDoorBlock extends AbstractInfinityDoorBlock<InfinityDoorBlockEntity>
{
    public static final DirectionProperty FACING;
    public static final BooleanProperty OPEN;
    public static final EnumProperty<DoorHinge> HINGE;
    public static final EnumProperty<DoubleBlockHalf> HALF;

    public InfinityDoorBlock(Settings settings) {
        super(settings);
    }


    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack)
    {
        super.onPlaced(world, pos, state, placer, itemStack);
        if(state.get(HALF) == DoubleBlockHalf.LOWER && !world.isClient)
        {
            InfinityDoorBlockEntity blockEntity = (InfinityDoorBlockEntity) world.getBlockEntity(pos);

            PersonalDimension personalDim = blockEntity.getOrCreateLinkedDimension();

            blockEntity.placeSyncedDoor(InfinityDimHelper.getInfinityDimension(), personalDim.getPlayerPos());
        }
    }

    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos)
    {
        if(!DOFConfig.getInstance().requireDoorBorder)
        {
            return super.canPlaceAt(state, world, pos);
        }
        pos = state.get(HALF) == DoubleBlockHalf.LOWER ? pos : pos.down();

        return topAndBottomMatch(pos, world)
                && ((sideMatches(pos, world, Direction.NORTH) && sideMatches(pos, world, Direction.SOUTH))
                || (sideMatches(pos, world, Direction.EAST) && sideMatches(pos, world, Direction.WEST)));

    }

    private boolean topAndBottomMatch(BlockPos pos, WorldView world)
    {
        Predicate<Block> matchingBlocks = (block) -> block == DOFBlocks.INFINITY_BLOCK
                || block == DOFBlocks.GENERATED_INFINITY_BLOCK;
        return matchingBlocks.test(world.getBlockState(pos.down()).getBlock())
                && matchingBlocks.test(world.getBlockState(pos.up(2)).getBlock());
    }

    private boolean sideMatches(BlockPos pos, WorldView world, Direction d)
    {
        Predicate<Block> matchingBlocks = (block) -> block == DOFBlocks.INFINITY_BLOCK
                || block == DOFBlocks.GENERATED_INFINITY_BLOCK;
        BlockPos sidePos = pos.add(d.getVector());

        return matchingBlocks.test(world.getBlockState(sidePos).getBlock())
                && matchingBlocks.test(world.getBlockState(sidePos.up()).getBlock());
    }

    @Override
    public InfinityDoorBlockEntity createBlockEntity(BlockView view)
    {
        return new InfinityDoorBlockEntity();
    }

    static
    {
        FACING = HorizontalFacingBlock.FACING;
        OPEN = Properties.OPEN;
        HINGE = Properties.DOOR_HINGE;
        HALF = Properties.DOUBLE_BLOCK_HALF;
    }


}
