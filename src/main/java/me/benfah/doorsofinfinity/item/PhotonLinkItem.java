package me.benfah.doorsofinfinity.item;

import com.qouteall.immersive_portals.Helper;
import com.qouteall.immersive_portals.my_util.IntBox;
import me.benfah.doorsofinfinity.utils.BoxUtils;
import me.benfah.doorsofinfinity.utils.MCUtils;
import me.benfah.doorsofinfinity.utils.PortalCreationHelper;
import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import java.util.List;

public class PhotonLinkItem extends Item
{
    public PhotonLinkItem(Properties settings)
    {
        super(settings);
    }


    @Override
    public ActionResultType onItemUse(ItemUseContext context)
    {
        if(context.getItem().getChildTag("PhotonLink") != null && !context.getWorld().isRemote && MCUtils.immersivePortalsPresent)
        {
            CompoundNBT tag = context.getItem().getChildTag("PhotonLink");

            Direction savedFacing = Direction.byHorizontalIndex(tag.getInt("Direction")).getOpposite();
            Direction currentFacing = context.getFace();

            BoxUtils.PlaneInfo savedPlane = new BoxUtils.PlaneInfo(tag);
            IntBox savedIntBox = new IntBox(savedPlane.pos, savedPlane.pos.add((savedPlane.width - 1) * savedPlane.axisW.getX(), savedPlane.height - 1, (savedPlane.width - 1) * savedPlane.axisW.getZ()));






            IntBox currentIntBox = Helper.expandRectangle(context.getPos(), (pos) -> context.getWorld().getBlockState(pos).getBlock() instanceof AbstractGlassBlock, context.getFace().getAxis());

            BoxUtils.PlaneInfo currentPlane = BoxUtils.getPlaneFromIntBox(currentIntBox, context.getFace());

            Vec3d entityVec = currentIntBox.getCenterVec().add(0.495 * currentFacing.getXOffset(), 0, 0.495 * currentFacing.getZOffset());
            Vec3d vecToRender = savedIntBox.getCenterVec().subtract(savedFacing.getXOffset() != 0 ? 0.5 * savedFacing.getXOffset() : 0, 0, savedFacing.getZOffset() != 0 ? 0.5 * savedFacing.getZOffset() : 0);

            int difference = BoxUtils.getAbsoluteHorizontal(currentFacing.getHorizontalIndex() - savedFacing.getHorizontalIndex());

            DimensionType type = DimensionType.getById(tag.getInt("DimensionId"));

            if(savedPlane.equals(currentPlane))
            {
                PortalCreationHelper.spawnBreakable(context.getWorld(), entityVec, currentPlane.width,
                        currentPlane.height, currentPlane.axisW, currentPlane.axisH,
                        type, vecToRender, false,
                        Vector3f.YP.rotationDegrees(difference * 90), false, savedIntBox, currentIntBox, context.getWorld().getServer().getWorld(type));

                context.getItem().removeChildTag("PhotonLink");

                return ActionResultType.SUCCESS;
            }

        }
        return ActionResultType.CONSUME;
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag context)
    {
        CompoundNBT tag = stack.getChildTag("PhotonLink");

        if(!MCUtils.immersivePortalsPresent)
        tooltip.add(new TranslationTextComponent("lore.doorsofinfinity.ip_not_present").applyTextStyle(TextFormatting.GRAY));

        if(tag != null)
        {
            tooltip.add(new TranslationTextComponent("lore.doorsofinfinity.photo_link_size", tag.getInt("Width"), tag.getInt("Height")).applyTextStyle(TextFormatting.GRAY));
        }

        super.addInformation(stack, world, tooltip, context);
    }
}
