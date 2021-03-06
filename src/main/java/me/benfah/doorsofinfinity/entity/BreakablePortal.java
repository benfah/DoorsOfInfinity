package me.benfah.doorsofinfinity.entity;

import com.qouteall.immersive_portals.my_util.IntBox;
import com.qouteall.immersive_portals.portal.Portal;
import me.benfah.doorsofinfinity.init.DOFBlocks;
import me.benfah.doorsofinfinity.init.DOFEntities;
import me.benfah.doorsofinfinity.utils.BoxUtils;
import me.benfah.doorsofinfinity.utils.MCUtils;
import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class BreakablePortal extends Portal
{

    public IntBox transmitterArea;
    public IntBox glassArea;

    public World transmitterWorld;

    public BreakablePortal(World world_1)
    {
        super(DOFEntities.BREAKABLE_PORTAL, world_1);
    }

    @Override
    protected void readCustomDataFromTag(CompoundTag compoundTag)
    {
        super.readCustomDataFromTag(compoundTag);
        transmitterArea = new IntBox(new BlockPos(BoxUtils.vecFromTag(compoundTag.getCompound("PhotonTransmitterL"))), new BlockPos(BoxUtils.vecFromTag(compoundTag.getCompound("PhotonTransmitterH"))));
        glassArea = new IntBox(new BlockPos(BoxUtils.vecFromTag(compoundTag.getCompound("GlassAreaL"))), new BlockPos(BoxUtils.vecFromTag(compoundTag.getCompound("GlassAreaH"))));
        transmitterWorld = MCUtils.getServer().getWorld(DimensionType.byRawId(compoundTag.getInt("WorldId")));
    }

    @Override
    protected void writeCustomDataToTag(CompoundTag compoundTag)
    {
        super.writeCustomDataToTag(compoundTag);
        compoundTag.put("PhotonTransmitterL", BoxUtils.vecToTag(transmitterArea.l));
        compoundTag.put("PhotonTransmitterH", BoxUtils.vecToTag(transmitterArea.h));

        compoundTag.put("GlassL", BoxUtils.vecToTag(glassArea.l));
        compoundTag.put("GlassH", BoxUtils.vecToTag(glassArea.h));

        compoundTag.putInt("WorldId", transmitterWorld.getDimension().getType().getRawId());

    }

    private void checkIntegrity() {
        boolean transmittersValid = transmitterArea.fastStream().allMatch(
                blockPos -> transmitterWorld.getBlockState(blockPos).getBlock() == DOFBlocks.PHOTON_TRANSMITTER);

        boolean glassValid = glassArea.fastStream().allMatch(
                blockPos -> world.getBlockState(blockPos).getBlock() instanceof AbstractGlassBlock);

        if (!transmittersValid || !glassValid) {
            removed = true;
        }
    }

    @Override
    public void tick()
    {
        super.tick();
        if (!world.isClient)
        {
            if (world.getTime() % 10 == getEntityId() % 10)
            {
                checkIntegrity();
            }
        }

    }
}
