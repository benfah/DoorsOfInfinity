package me.benfah.doorsofinfinity.block;

import me.benfah.doorsofinfinity.block.entity.InfinityDoorBlockEntity;
import me.benfah.doorsofinfinity.config.DOFConfig;
import me.benfah.doorsofinfinity.dimension.InfinityDimHelper;
import me.benfah.doorsofinfinity.dimension.InfinityDimHelper.PersonalDimension;
import me.benfah.doorsofinfinity.init.DOFBlocks;
import me.benfah.doorsofinfinity.init.DOFDimensions;
import me.benfah.doorsofinfinity.utils.MCUtils;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.*;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.DimensionManager;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class InfinityDoorBlock extends ContainerBlock
{

    public static final DirectionProperty FACING;
    public static final BooleanProperty OPEN;
    public static final EnumProperty<DoorHingeSide> HINGE;
    public static final EnumProperty<DoubleBlockHalf> HALF;
    protected static final VoxelShape SOUTH_SHAPE;
    protected static final VoxelShape NORTH_SHAPE;
    protected static final VoxelShape WEST_SHAPE;
    protected static final VoxelShape EAST_SHAPE;

    public InfinityDoorBlock(Block.Properties settings)
    {
        super(settings);
        this.setDefaultState((((((this.getDefaultState()).with(FACING, Direction.NORTH)).with(OPEN, false)).with(HINGE, DoorHingeSide.LEFT)).with(HALF, DoubleBlockHalf.LOWER)));
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Direction direction = state.get(FACING);
        boolean flag = !state.get(OPEN);
        boolean flag1 = state.get(HINGE) == DoorHingeSide.RIGHT;
        switch(direction) {
            case EAST:
            default:
                return flag ? EAST_SHAPE : (flag1 ? NORTH_SHAPE : SOUTH_SHAPE);
            case SOUTH:
                return flag ? SOUTH_SHAPE : (flag1 ? EAST_SHAPE : WEST_SHAPE);
            case WEST:
                return flag ? WEST_SHAPE : (flag1 ? SOUTH_SHAPE : NORTH_SHAPE);
            case NORTH:
                return flag ? NORTH_SHAPE : (flag1 ? WEST_SHAPE : EAST_SHAPE);
        }
    }

    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState neighborState,
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
            return doubleBlockHalf == DoubleBlockHalf.LOWER && facing == Direction.DOWN && !state.isValidPosition(world, pos)
                    ? Blocks.AIR.getDefaultState()
                    : super.updatePostPlacement(state, facing, neighborState, world, pos, neighborPos);
        }
    }

    public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, TileEntity blockEntity,
                           ItemStack stack)
    {
        super.harvestBlock(world, player, pos, Blocks.AIR.getDefaultState(), blockEntity, stack);

    }


    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player)
    {
        BlockPos lowerPos = state.get(HALF) == DoubleBlockHalf.LOWER ? pos : pos.down();
        BlockState lowerBlockState = world.getBlockState(lowerPos);
        InfinityDoorBlockEntity firstEntity = (InfinityDoorBlockEntity) world.getTileEntity(lowerPos);


        DoubleBlockHalf doubleBlockHalf = (DoubleBlockHalf) state.get(HALF);
        BlockPos otherPos = doubleBlockHalf == DoubleBlockHalf.LOWER ? pos.up() : pos.down();
        BlockState otherblockState = world.getBlockState(otherPos);
        InfinityDoorBlockEntity lowerBlockEntity = (InfinityDoorBlockEntity) world.getTileEntity(lowerPos);

        if(otherblockState.getBlock() == this && otherblockState.get(HALF) != doubleBlockHalf)
        {
            world.playEvent(player, 2001, otherPos, Block.getStateId(otherblockState));
            if(!world.isRemote && !player.isCreative() && player.canHarvestBlock(otherblockState))
            {

                Block.spawnDrops(lowerBlockState, world, lowerPos, world.getTileEntity(lowerPos));
            }
            world.setBlockState(otherPos, Blocks.AIR.getDefaultState(), 35);
        }
        if(!world.isRemote && MCUtils.immersivePortalsPresent)
        {
            lowerBlockEntity.deleteLocalPortal();
        }
        super.onBlockHarvested(world, pos, state, player);
    }



    @Override
    public float getPlayerRelativeBlockHardness(BlockState state, PlayerEntity player, IBlockReader world, BlockPos pos)
    {
        if(world instanceof World)
        {
            if(((World) world).getDimension().getType() == DOFDimensions.INFINITY_DIM)
            {
                return 0;
            }
        }
        return super.getPlayerRelativeBlockHardness(state, player, world, pos);
    }

    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        switch(type) {
            case LAND:
                return state.get(OPEN);
            case WATER:
                return false;
            case AIR:
                return state.get(OPEN);
            default:
                return false;
        }
    }

    private int getCloseSound() {
        return this.material == Material.IRON ? 1011 : 1012;
    }

    private int getOpenSound() {
        return this.material == Material.IRON ? 1005 : 1006;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos blockpos = context.getPos();
        if (blockpos.getY() < 255 && context.getWorld().getBlockState(blockpos.up()).isReplaceable(context)) {
            World world = context.getWorld();
            boolean flag = world.isBlockPowered(blockpos) || world.isBlockPowered(blockpos.up());
            return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing()).with(HINGE, this.getHinge(context)).with(OPEN, Boolean.valueOf(flag)).with(HALF, DoubleBlockHalf.LOWER);
        } else {
            return null;
        }
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        worldIn.setBlockState(pos.up(), state.with(HALF, DoubleBlockHalf.UPPER), 3);
        if(state.get(HALF) == DoubleBlockHalf.LOWER && !worldIn.isRemote)
        {
            if(stack != null && stack.getChildTag("BlockEntity") != null)
            {
                InfinityDoorBlockEntity blockEntity = new InfinityDoorBlockEntity();
                blockEntity.updateSyncDoor();
            }
            InfinityDoorBlockEntity blockEntity = (InfinityDoorBlockEntity) worldIn.getTileEntity(pos);

            PersonalDimension personalDim = blockEntity.getOrCreateLinkedDimension();

            blockEntity.placeSyncedDoor(worldIn.getServer().getWorld(DOFDimensions.INFINITY_DIM), personalDim.getPlayerPos());
        }
    }


    private DoorHingeSide getHinge(BlockItemUseContext ctx)
    {
        IBlockReader blockView = ctx.getWorld();
        BlockPos blockPos = ctx.getPos();
        Direction direction = ctx.getPlacementHorizontalFacing();
        BlockPos blockPos2 = blockPos.up();
        Direction direction2 = direction.rotateYCCW();
        BlockPos blockPos3 = blockPos.offset(direction2);
        BlockState blockState = blockView.getBlockState(blockPos3);
        BlockPos blockPos4 = blockPos2.offset(direction2);
        BlockState blockState2 = blockView.getBlockState(blockPos4);
        Direction direction3 = direction.rotateY();
        BlockPos blockPos5 = blockPos.offset(direction3);
        BlockState blockState3 = blockView.getBlockState(blockPos5);
        BlockPos blockPos6 = blockPos2.offset(direction3);
        BlockState blockState4 = blockView.getBlockState(blockPos6);
        int i = (blockState.isNormalCube(blockView, blockPos3) ? -1 : 0)
                + (blockState2.isNormalCube(blockView, blockPos4) ? -1 : 0)
                + (blockState3.isNormalCube(blockView, blockPos5) ? 1 : 0)
                + (blockState4.isNormalCube(blockView, blockPos6) ? 1 : 0);
        boolean bl = blockState.getBlock() == this && blockState.get(HALF) == DoubleBlockHalf.LOWER;
        boolean bl2 = blockState3.getBlock() == this && blockState3.get(HALF) == DoubleBlockHalf.LOWER;
        if((!bl || bl2) && i <= 0)
        {
            if((!bl2 || bl) && i >= 0)
            {
                int j = direction.getXOffset();
                int k = direction.getZOffset();
                Vec3d vec3d = ctx.getHitVec();
                double d = vec3d.x - (double) blockPos.getX();
                double e = vec3d.z - (double) blockPos.getZ();
                return (j >= 0 || e >= 0.5D) && (j <= 0 || e <= 0.5D) && (k >= 0 || d <= 0.5D) && (k <= 0 || d >= 0.5D)
                        ? DoorHingeSide.LEFT
                        : DoorHingeSide.RIGHT;
            }
            else
            {
                return DoorHingeSide.LEFT;
            }
        }
        else
        {
            return DoorHingeSide.RIGHT;
        }
    }


    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        state = (BlockState) state.cycle(OPEN);
        world.setBlockState(pos, state, 10);
        world.playEvent(player, (Boolean) state.get(OPEN) ? this.getCloseSound() : this.getOpenSound(), pos, 0);

        if(!world.isRemote)
        {

            BlockPos lowerPos = state.get(HALF) == DoubleBlockHalf.LOWER ? pos : pos.down();

            InfinityDoorBlockEntity blockEntity = (InfinityDoorBlockEntity) world.getTileEntity(lowerPos);
            blockEntity.updateSyncDoor();
        }
        return ActionResultType.SUCCESS;

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



    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos neighborPos,
                               boolean moved)
    {
        pos = state.get(HALF) == DoubleBlockHalf.LOWER ? pos : pos.down();
        state = world.getBlockState(pos);
        InfinityDoorBlockEntity blockEntity = (InfinityDoorBlockEntity) world.getTileEntity(pos);
        if(!state.isAir() && !isValidPosition(state, world, pos))
        {
            blockEntity.deleteLocalPortal();
            world.setBlockState(pos.up(), Blocks.AIR.getDefaultState(), 35);
            world.destroyBlock(pos, true);

        }
    }



    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos)
    {
        if(!DOFConfig.getInstance().requireDoorBorder.get())
        {
            BlockPos blockPos = pos.down();
            BlockState blockState = world.getBlockState(blockPos);
            if(state.get(HALF) == DoubleBlockHalf.LOWER)
            {
                return blockState.isSolidSide(world, blockPos, Direction.UP);
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

    private boolean topAndBottomMatch(BlockPos pos, IWorldReader world)
    {
        Predicate<Block> matchingBlocks = (block) -> block == DOFBlocks.BLOCK_OF_INFINITY.get()
                || block == DOFBlocks.GENERATED_BLOCK_OF_INFINITY.get();
        return matchingBlocks.test(world.getBlockState(pos.down()).getBlock())
                && matchingBlocks.test(world.getBlockState(pos.up(2)).getBlock());
    }

    private boolean sideMatches(BlockPos pos, IWorldReader world, Direction d)
    {
        Predicate<Block> matchingBlocks = (block) -> block == DOFBlocks.BLOCK_OF_INFINITY.get()
                || block == DOFBlocks.GENERATED_BLOCK_OF_INFINITY.get();
        BlockPos sidePos = pos.add(d.getDirectionVec());

        return matchingBlocks.test(world.getBlockState(sidePos).getBlock())
                && matchingBlocks.test(world.getBlockState(sidePos.up()).getBlock());
    }

    private void playOpenCloseSound(World world, BlockPos pos, boolean open)
    {
        world.playEvent((PlayerEntity) null, open ? this.getCloseSound() : this.getOpenSound(), pos,
                0);
    }

    public PushReaction getPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return mirrorIn == Mirror.NONE ? state : state.rotate(mirrorIn.toRotation(state.get(FACING))).cycle(HINGE);
    }

    @OnlyIn(Dist.CLIENT)
    public long getPositionRandom(BlockState state, BlockPos pos) {
        return MathHelper.getCoordinateRandom(pos.getX(), pos.down(state.get(HALF) == DoubleBlockHalf.LOWER ? 0 : 1).getY(), pos.getZ());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(HALF, FACING, OPEN, HINGE);
    }

    static
    {
        FACING = HorizontalBlock.HORIZONTAL_FACING;
        OPEN = BlockStateProperties.OPEN;
        HINGE = BlockStateProperties.DOOR_HINGE;
        HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
        SOUTH_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D);
        NORTH_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D);
        WEST_SHAPE = Block.makeCuboidShape(13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
        EAST_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 16.0D);
    }



    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn)
    {
        return new InfinityDoorBlockEntity();
    }
}
