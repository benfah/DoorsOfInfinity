package me.benfah.doorsofinfinity.block;


import com.mojang.datafixers.util.Pair;
import com.qouteall.immersive_portals.Helper;
import com.qouteall.immersive_portals.my_util.IntBox;
import me.benfah.doorsofinfinity.init.DOFItems;
import me.benfah.doorsofinfinity.utils.BoxUtils;
import me.benfah.doorsofinfinity.utils.MCUtils;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;

import net.minecraft.item.BlockItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;

import net.minecraft.util.math.BlockPos;

import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class PhotonTransmitterBlock extends GlassBlock
{
    public static DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    public PhotonTransmitterBlock(Properties settings)
    {
        super(settings);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }



    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        if(hand == Hand.MAIN_HAND && !world.isRemote && !player.getHeldItemMainhand().isEmpty() && MCUtils.immersivePortalsPresent)
        {
            if(player.getHeldItemMainhand().getItem() == DOFItems.PHOTON_LINK.get())
            {
                Direction direction = state.get(PhotonTransmitterBlock.FACING);

                IntBox box = Helper.expandRectangle(pos,
                        testPos -> BoxUtils.hasSamePropertyValue(PhotonTransmitterBlock.FACING, world.getBlockState(testPos), state.get(PhotonTransmitterBlock.FACING)),
                        direction.getAxis());
                Pair<BlockPos, BlockPos> anchorPositions = BoxUtils.getAnchors(box);

                CompoundNBT tag = player.getHeldItemMainhand().getOrCreateChildTag("PhotonLink");

                BoxUtils.PlaneInfo planeInfo = BoxUtils.getPlaneFromIntBox(box, direction);

                planeInfo.toCompound(tag);

                tag.putInt("Direction", direction.getHorizontalIndex());
                tag.putInt("DimensionId", world.getDimension().getType().getId());

                player.sendMessage(new TranslationTextComponent("lore.doorsofinfinity.linked"));

                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }


    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return (BlockState)this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

}
