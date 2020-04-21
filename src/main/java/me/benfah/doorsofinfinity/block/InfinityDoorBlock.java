package me.benfah.doorsofinfinity.block;

import me.benfah.doorsofinfinity.block.entity.InfinityDoorBlockEntity;
import me.benfah.doorsofinfinity.config.DOFConfig;
import me.benfah.doorsofinfinity.dimension.InfinityDimHelper;
import me.benfah.doorsofinfinity.dimension.InfinityDimHelper.PersonalDimension;
import me.benfah.doorsofinfinity.init.DOFBlocks;
import me.benfah.doorsofinfinity.init.DOFDimensions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.function.Predicate;

public class InfinityDoorBlock extends BlockWithEntity
{

    public static final DirectionProperty FACING;
    public static final BooleanProperty OPEN;
    public static final EnumProperty<DoorHinge> HINGE;
    public static final EnumProperty<DoubleBlockHalf> HALF;
    protected static final VoxelShape NORTH_SHAPE;
    protected static final VoxelShape SOUTH_SHAPE;
    protected static final VoxelShape EAST_SHAPE;
    protected static final VoxelShape WEST_SHAPE;

    public InfinityDoorBlock(Block.Settings settings)
    {
        super(settings);
        this.setDefaultState(
                (BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.stateManager
                        .getDefaultState()).with(FACING, Direction.NORTH)).with(OPEN, false)).with(HINGE,
                        DoorHinge.LEFT)).with(HALF, DoubleBlockHalf.LOWER)));
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context)
    {
        Direction direction = (Direction) state.get(FACING);
        boolean bl = !(Boolean) state.get(OPEN);
        boolean bl2 = state.get(HINGE) == DoorHinge.RIGHT;
        switch(direction)
        {
            case EAST:
            default:
                return bl ? WEST_SHAPE : (bl2 ? SOUTH_SHAPE : NORTH_SHAPE);
            case SOUTH:
                return bl ? NORTH_SHAPE : (bl2 ? WEST_SHAPE : EAST_SHAPE);
            case WEST:
                return bl ? EAST_SHAPE : (bl2 ? NORTH_SHAPE : SOUTH_SHAPE);
            case NORTH:
                return bl ? SOUTH_SHAPE : (bl2 ? EAST_SHAPE : WEST_SHAPE);
        }
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState,
                                                IWorld world, BlockPos pos, BlockPos neighborPos)
    {
        DoubleBlockHalf doubleBlockHalf = (DoubleBlockHalf) state.get(HALF);
        if(facing.getAxis() == Direction.Axis.Y
                && doubleBlockHalf == DoubleBlockHalf.LOWER == (facing == Direction.UP))
        {
            return neighborState.getBlock() == this && neighborState.get(HALF) != doubleBlockHalf
                    ? (BlockState) ((BlockState) ((BlockState) ((BlockState) state.with(FACING,
                    neighborState.get(FACING))).with(OPEN, neighborState.get(OPEN))).with(HINGE,
                    neighborState.get(HINGE)))
                    : Blocks.AIR.getDefaultState();
        }
        else
        {
            return doubleBlockHalf == DoubleBlockHalf.LOWER && facing == Direction.DOWN && !state.canPlaceAt(world, pos)
                    ? Blocks.AIR.getDefaultState()
                    : super.getStateForNeighborUpdate(state, facing, neighborState, world, pos, neighborPos);
        }
    }

    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity,
                           ItemStack stack)
    {
        super.afterBreak(world, player, pos, Blocks.AIR.getDefaultState(), blockEntity, stack);

    }


    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player)
    {
        BlockPos lowerPos = state.get(HALF) == DoubleBlockHalf.LOWER ? pos : pos.down();
        BlockState lowerBlockState = world.getBlockState(lowerPos);
        InfinityDoorBlockEntity firstEntity = (InfinityDoorBlockEntity) world.getBlockEntity(lowerPos);


        DoubleBlockHalf doubleBlockHalf = (DoubleBlockHalf) state.get(HALF);
        BlockPos otherPos = doubleBlockHalf == DoubleBlockHalf.LOWER ? pos.up() : pos.down();
        BlockState otherblockState = world.getBlockState(otherPos);
        InfinityDoorBlockEntity lowerBlockEntity = (InfinityDoorBlockEntity) world.getBlockEntity(lowerPos);

        if(otherblockState.getBlock() == this && otherblockState.get(HALF) != doubleBlockHalf)
        {
            world.playLevelEvent(player, 2001, otherPos, Block.getRawIdFromState(otherblockState));
            if(!world.isClient && !player.isCreative() && player.isUsingEffectiveTool(otherblockState))
            {

                Block.dropStacks(lowerBlockState, world, lowerPos, world.getBlockEntity(lowerPos));
            }
            world.setBlockState(otherPos, Blocks.AIR.getDefaultState(), 35);
        }
        if(!world.isClient)
        {
            lowerBlockEntity.deleteLocalPortal();
        }
        super.onBreak(world, pos, state, player);
    }

    @Override
    public float calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos)
    {
        if(world instanceof World)
        {
            if(((World) world).getDimension().getType() == DOFDimensions.INFINITY_DIM)
            {
                return 0;
            }
        }
        return super.calcBlockBreakingDelta(state, player, world, pos);
    }

    public boolean canPathfindThrough(BlockState world, BlockView view, BlockPos pos, NavigationType env)
    {
        switch(env)
        {
            case LAND:
            case AIR:
                return (Boolean) world.get(OPEN);
            default:
                return false;
        }
    }

    private int getOpenSoundEventId()
    {
        return this.material == Material.METAL ? 1011 : 1012;
    }

    private int getCloseSoundEventId()
    {
        return this.material == Material.METAL ? 1005 : 1006;
    }

    public BlockState getPlacementState(ItemPlacementContext ctx)
    {
        BlockPos blockPos = ctx.getBlockPos();
        if(blockPos.getY() < 255 && ctx.getWorld().getBlockState(blockPos.up()).canReplace(ctx))
        {
            World world = ctx.getWorld();
            boolean bl = world.isReceivingRedstonePower(blockPos) || world.isReceivingRedstonePower(blockPos.up());
            return (BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.getDefaultState()
                    .with(FACING, ctx.getPlayerFacing())).with(HINGE, this.getHinge(ctx)))).with(OPEN, bl)).with(HALF,
                    DoubleBlockHalf.LOWER);
        }
        else
        {
            return null;
        }
    }

    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack)
    {
        world.setBlockState(pos.up(), (BlockState) state.with(HALF, DoubleBlockHalf.UPPER), 3);

        if(state.get(HALF) == DoubleBlockHalf.LOWER && !world.isClient)
        {
            if(itemStack != null && itemStack.getSubTag("BlockEntity") != null)
            {
                InfinityDoorBlockEntity blockEntity = new InfinityDoorBlockEntity();
                blockEntity.updateSyncDoor();
            }
            InfinityDoorBlockEntity blockEntity = (InfinityDoorBlockEntity) world.getBlockEntity(pos);

            PersonalDimension personalDim = blockEntity.getOrCreateLinkedDimension();


            blockEntity.placeSyncedDoor(InfinityDimHelper.getInfinityDimension(), personalDim.getPlayerPos());
            blockEntity.createSyncedPortals();
        }
    }


    private DoorHinge getHinge(ItemPlacementContext ctx)
    {
        BlockView blockView = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();
        Direction direction = ctx.getPlayerFacing();
        BlockPos blockPos2 = blockPos.up();
        Direction direction2 = direction.rotateYCounterclockwise();
        BlockPos blockPos3 = blockPos.offset(direction2);
        BlockState blockState = blockView.getBlockState(blockPos3);
        BlockPos blockPos4 = blockPos2.offset(direction2);
        BlockState blockState2 = blockView.getBlockState(blockPos4);
        Direction direction3 = direction.rotateYClockwise();
        BlockPos blockPos5 = blockPos.offset(direction3);
        BlockState blockState3 = blockView.getBlockState(blockPos5);
        BlockPos blockPos6 = blockPos2.offset(direction3);
        BlockState blockState4 = blockView.getBlockState(blockPos6);
        int i = (blockState.isFullCube(blockView, blockPos3) ? -1 : 0)
                + (blockState2.isFullCube(blockView, blockPos4) ? -1 : 0)
                + (blockState3.isFullCube(blockView, blockPos5) ? 1 : 0)
                + (blockState4.isFullCube(blockView, blockPos6) ? 1 : 0);
        boolean bl = blockState.getBlock() == this && blockState.get(HALF) == DoubleBlockHalf.LOWER;
        boolean bl2 = blockState3.getBlock() == this && blockState3.get(HALF) == DoubleBlockHalf.LOWER;
        if((!bl || bl2) && i <= 0)
        {
            if((!bl2 || bl) && i >= 0)
            {
                int j = direction.getOffsetX();
                int k = direction.getOffsetZ();
                Vec3d vec3d = ctx.getHitPos();
                double d = vec3d.x - (double) blockPos.getX();
                double e = vec3d.z - (double) blockPos.getZ();
                return (j >= 0 || e >= 0.5D) && (j <= 0 || e <= 0.5D) && (k >= 0 || d <= 0.5D) && (k <= 0 || d >= 0.5D)
                        ? DoorHinge.LEFT
                        : DoorHinge.RIGHT;
            }
            else
            {
                return DoorHinge.LEFT;
            }
        }
        else
        {
            return DoorHinge.RIGHT;
        }
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
                              BlockHitResult hit)
    {

        state = (BlockState) state.cycle(OPEN);
        world.setBlockState(pos, state, 10);
        world.playLevelEvent(player,
                (Boolean) state.get(OPEN) ? this.getCloseSoundEventId() : this.getOpenSoundEventId(), pos, 0);

        if(!world.isClient)
        {
            BlockPos lowerPos = state.get(HALF) == DoubleBlockHalf.LOWER ? pos : pos.down();

            InfinityDoorBlockEntity blockEntity = (InfinityDoorBlockEntity) world.getBlockEntity(lowerPos);

            blockEntity.updateSyncDoor();
        }
        return ActionResult.SUCCESS;

    }

    public void setOpen(World world, BlockPos pos, boolean open)
    {
        BlockState blockState = world.getBlockState(pos);
        if((Boolean) blockState.get(OPEN) != open)
        {
            world.setBlockState(pos, (BlockState) blockState.with(OPEN, open), 10);
            this.playOpenCloseSound(world, pos, open);
        }
    }

    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos neighborPos,
                               boolean moved)
    {
        pos = state.get(HALF) == DoubleBlockHalf.LOWER ? pos : pos.down();
        state = world.getBlockState(pos);
        InfinityDoorBlockEntity blockEntity = (InfinityDoorBlockEntity) world.getBlockEntity(pos);
        if(!state.isAir() && !canPlaceAt(state, world, pos))
        {
            blockEntity.deleteLocalPortal();
            world.setBlockState(pos.up(), Blocks.AIR.getDefaultState(), 35);
            world.breakBlock(pos, true);

        }
    }

    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos)
    {
        if(!DOFConfig.getInstance().requireDoorBorder)
        {
            BlockPos blockPos = pos.down();
            BlockState blockState = world.getBlockState(blockPos);
            if(state.get(HALF) == DoubleBlockHalf.LOWER)
            {
                return blockState.isSideSolidFullSquare(world, blockPos, Direction.UP);
            }
            else
            {
                return blockState.getBlock() == this;
            }
        }
        pos = state.get(HALF) == DoubleBlockHalf.LOWER ? pos : pos.down();

        return topAndBottomMatch(pos, world)
                && ((sideMatches(pos, world, Direction.NORTH) && sideMatches(pos, world, Direction.SOUTH))
                || (sideMatches(pos, world, Direction.EAST) && sideMatches(pos, world, Direction.WEST)));

    }

    private boolean topAndBottomMatch(BlockPos pos, WorldView world)
    {
        Predicate<Block> matchingBlocks = (block) -> block == DOFBlocks.SIMULATED_BLOCK_OF_INFINITY
                || block == DOFBlocks.BLOCK_OF_INFINITY;
        return matchingBlocks.test(world.getBlockState(pos.down()).getBlock())
                && matchingBlocks.test(world.getBlockState(pos.up(2)).getBlock());
    }

    private boolean sideMatches(BlockPos pos, WorldView world, Direction d)
    {
        Predicate<Block> matchingBlocks = (block) -> block == DOFBlocks.SIMULATED_BLOCK_OF_INFINITY
                || block == DOFBlocks.BLOCK_OF_INFINITY;
        BlockPos sidePos = pos.add(d.getVector());

        return matchingBlocks.test(world.getBlockState(sidePos).getBlock())
                && matchingBlocks.test(world.getBlockState(sidePos.up()).getBlock());
    }

    private void playOpenCloseSound(World world, BlockPos pos, boolean open)
    {
        world.playLevelEvent((PlayerEntity) null, open ? this.getCloseSoundEventId() : this.getOpenSoundEventId(), pos,
                0);
    }

    public PistonBehavior getPistonBehavior(BlockState state)
    {
        return PistonBehavior.BLOCK;
    }

    public BlockState rotate(BlockState state, BlockRotation rotation)
    {
        return (BlockState) state.with(FACING, rotation.rotate((Direction) state.get(FACING)));
    }

    public BlockState mirror(BlockState state, BlockMirror mirror)
    {
        return mirror == BlockMirror.NONE ? state
                : (BlockState) state.rotate(mirror.getRotation((Direction) state.get(FACING))).cycle(HINGE);
    }

    @Environment(EnvType.CLIENT)
    public long getRenderingSeed(BlockState state, BlockPos pos)
    {
        return MathHelper.hashCode(pos.getX(), pos.down(state.get(HALF) == DoubleBlockHalf.LOWER ? 0 : 1).getY(),
                pos.getZ());
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
    {
        builder.add(HALF, FACING, OPEN, HINGE);
    }

    static
    {
        FACING = HorizontalFacingBlock.FACING;
        OPEN = Properties.OPEN;
        HINGE = Properties.DOOR_HINGE;
        HALF = Properties.DOUBLE_BLOCK_HALF;
        NORTH_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D);
        SOUTH_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D);
        EAST_SHAPE = Block.createCuboidShape(13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
        WEST_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 16.0D);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView view)
    {
        return new InfinityDoorBlockEntity();
    }

    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }
}
