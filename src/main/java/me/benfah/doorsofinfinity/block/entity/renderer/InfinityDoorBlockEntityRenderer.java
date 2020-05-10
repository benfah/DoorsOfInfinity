package me.benfah.doorsofinfinity.block.entity.renderer;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.benfah.doorsofinfinity.block.InfinityDoorBlock;
import me.benfah.doorsofinfinity.block.entity.InfinityDoorBlockEntity;
import me.benfah.doorsofinfinity.utils.MCUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;


import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class InfinityDoorBlockEntityRenderer extends TileEntityRenderer<InfinityDoorBlockEntity>
{
    private static Random RANDOM = Util.make(new Random(), r -> r.setSeed(31100));

    private static final List<RenderType> endLayers = (List<RenderType>) IntStream.range(0, 16).mapToObj((i) -> {
        return RenderType.getEndPortal(i + 1);
    }).collect(ImmutableList.toImmutableList());

    private static Minecraft client = Minecraft.getInstance();

    public InfinityDoorBlockEntityRenderer(TileEntityRendererDispatcher dispatcher)
    {
        super(dispatcher);
    }

    public static RenderType getOwnRenderLayer()
    {
        RenderType.State params = RenderType.State.getBuilder().transparency(new RenderState.TransparencyState("translucent_transparency", () -> {
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.defaultBlendFunc();
        }, () -> {
            RenderSystem.disableBlend();
            RenderSystem.disableAlphaTest();
        })).build(true);

        return RenderType.makeType("colored_upgrade", DefaultVertexFormats.POSITION_COLOR, 7, 256, false, true, params);

    }

    @Override
    public void render(InfinityDoorBlockEntity entity, float tickDelta, MatrixStack matrices, IRenderTypeBuffer vertexConsumers, int light, int overlay)
    {
        Direction direction = entity.getBlockState().get(InfinityDoorBlock.FACING);

        if(!MCUtils.immersivePortalsPresent)
            drawEndTexture(entity, tickDelta, matrices, vertexConsumers, direction);

        RayTraceResult rayTrace = client.player.pick(client.playerController.getBlockReachDistance(), tickDelta, false);

        if(client.player.isCrouching() && entity.getBlockState().get(InfinityDoorBlock.HALF) == DoubleBlockHalf.LOWER)
        {
            BlockRayTraceResult blockHitResult = (BlockRayTraceResult) rayTrace;

            if(blockHitResult.getPos().equals(entity.getPos()) || blockHitResult.getPos().equals(entity.getPos().up()))
            {
                matrices.push();
                transformToFace(matrices, direction);
                matrices.translate(0.5, 1.9, -0.001);
                matrices.rotate(Vector3f.ZP.rotationDegrees(180));
                matrices.scale(0.01F, 0.01F, 1);
                drawCenteredTextWithRect(new TranslationTextComponent("text.doorsofinfinity.installed_upgrades", entity.installedUpgrades).getFormattedText(), 0, 0xFFFFFF, false, matrices.getLast().getMatrix(), vertexConsumers, false, 0xFFFFFF, light, 0.168F, 0.341F, 0.156F, 0.5F);

                matrices.pop();
            }
        }
    }

    public static void drawRect(IRenderTypeBuffer vertexConsumerProvider, Matrix4f matrix, int x1, int y1, int x2, int y2, float r, float g, float b, float a)
    {
        IVertexBuilder consumer = vertexConsumerProvider.getBuffer(getOwnRenderLayer());

        consumer.pos(matrix, x1, y2, 0).color(r, g, b, a).endVertex();
        consumer.pos(matrix, x2, y2, 0).color(r, g, b, a).endVertex();
        consumer.pos(matrix, x2, y1, 0).color(r, g, b, a).endVertex();
        consumer.pos(matrix, x1, y1, 0).color(r, g, b, a).endVertex();
    }

    public static void transformToFace(MatrixStack stack, Direction direction)
    {
        stack.translate(.5, 0, .5);
        Quaternion rotationQuaternion = direction.getRotation();
        rotationQuaternion.multiply(Vector3f.XP.rotationDegrees(-90.0F));

        stack.rotate(rotationQuaternion);
        stack.translate(-.5, 0, -.5);
    }

    public void drawRightBoundText(String text, int x, int y, int color, boolean shadow, Matrix4f matrix, IRenderTypeBuffer vertexConsumers, boolean seeThrough, int backgroundColor, int light)
    {
        int textLength = renderDispatcher.getFontRenderer().getStringWidth(text);
        renderDispatcher.getFontRenderer().renderString(text, x - textLength, y, color, shadow, matrix, vertexConsumers, seeThrough, backgroundColor, light);
    }

    public void drawCenteredText(String text, int y, int color, boolean shadow, Matrix4f matrix, IRenderTypeBuffer vertexConsumers, boolean seeThrough, int backgroundColor, int light)
    {
        int textLength = renderDispatcher.getFontRenderer().getStringWidth(text);
        renderDispatcher.getFontRenderer().renderString(text, -textLength / 2, y, color, shadow, matrix, vertexConsumers, seeThrough, backgroundColor, light);
    }

    public void drawCenteredTextWithRect(String text, int y, int color, boolean shadow, Matrix4f matrix, IRenderTypeBuffer vertexConsumers, boolean seeThrough, int backgroundColor, int light, float r, float g, float b, float a)
    {
        int textLength = renderDispatcher.getFontRenderer().getStringWidth(text);
        int border = 4;

        drawRect(vertexConsumers, matrix, -textLength / 2 - border, y - 2, +textLength / 2 + border, y + renderDispatcher.getFontRenderer().FONT_HEIGHT + 2, r, g, b, a);
        drawCenteredText(text, y, color, shadow, matrix, vertexConsumers, seeThrough, backgroundColor, light);
    }

    public void drawRightBoundTextWithRect(String text, int x, int y, int color, boolean shadow, Matrix4f matrix, IRenderTypeBuffer vertexConsumers, boolean seeThrough, int backgroundColor, int light, float r, float g, float b, float a)
    {
        int border = 4;
        int textLength = renderDispatcher.getFontRenderer().getStringWidth(text);
        drawRect(vertexConsumers, matrix, x - textLength - border * 2, y - 2, x, y + renderDispatcher.getFontRenderer().FONT_HEIGHT + 2, r, g, b, a);
        drawRightBoundText(text, x - border, y, color, shadow, matrix, vertexConsumers, seeThrough, backgroundColor, light);
    }

    public void drawEndTexture(InfinityDoorBlockEntity endPortalBlockEntity, float f, MatrixStack matrixStack, IRenderTypeBuffer vertexConsumerProvider, Direction direction)
    {
        matrixStack.push();
        matrixStack.translate(direction.getDirectionVec().getX() * 0.1, 0, direction.getDirectionVec().getZ() * 0.1);
        RANDOM.setSeed(31100L);
        double d = endPortalBlockEntity.getPos().distanceSq(this.renderDispatcher.renderInfo.getProjectedView(), true);
        int k = this.getLayersToRender(d);

        Matrix4f matrix4f = matrixStack.getLast().getMatrix();
        this.drawEndLayer(endPortalBlockEntity, 0.15F, matrix4f, vertexConsumerProvider.getBuffer(endLayers.get(0)));

        for(int l = 1; l < 16; ++l) {
            this.drawEndLayer(endPortalBlockEntity, 2.0F / (float)(18 - l), matrix4f, vertexConsumerProvider.getBuffer(endLayers.get(l)));
        }
        matrixStack.pop();
    }

    private void drawEndLayer(InfinityDoorBlockEntity endPortalBlockEntity, float g, Matrix4f matrix4f, IVertexBuilder vertexConsumer) {
        float h = (RANDOM.nextFloat() * 0.5F + 0.1F) * g;
        float i = (RANDOM.nextFloat() * 0.5F + 0.4F) * g;
        float j = (RANDOM.nextFloat() * 0.5F + 0.5F) * g;
        this.drawEndLayerFace(endPortalBlockEntity, matrix4f, vertexConsumer, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, h, i, j, Direction.SOUTH);
        this.drawEndLayerFace(endPortalBlockEntity, matrix4f, vertexConsumer, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, h, i, j, Direction.NORTH);
        this.drawEndLayerFace(endPortalBlockEntity, matrix4f, vertexConsumer, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, h, i, j, Direction.EAST);
        this.drawEndLayerFace(endPortalBlockEntity, matrix4f, vertexConsumer, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, h, i, j, Direction.WEST);
        this.drawEndLayerFace(endPortalBlockEntity, matrix4f, vertexConsumer, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, h, i, j, Direction.DOWN);
    }

    private void drawEndLayerFace(InfinityDoorBlockEntity endPortalBlockEntity, Matrix4f matrix4f, IVertexBuilder vertexConsumer, float f, float g, float h, float i, float j, float k, float l, float m, float n, float o, float p, Direction direction) {

        if(direction == endPortalBlockEntity.getBlockState().get(InfinityDoorBlock.FACING).getOpposite())
        {
            vertexConsumer.pos(matrix4f, f, h, j).color(n, o, p, 1.0F).endVertex();
            vertexConsumer.pos(matrix4f, g, h, k).color(n, o, p, 1.0F).endVertex();
            vertexConsumer.pos(matrix4f, g, i, l).color(n, o, p, 1.0F).endVertex();
            vertexConsumer.pos(matrix4f, f, i, m).color(n, o, p, 1.0F).endVertex();
        }
    }

    protected int getLayersToRender(double d) {
        if (d > 36864.0D) {
            return 1;
        } else if (d > 25600.0D) {
            return 3;
        } else if (d > 16384.0D) {
            return 5;
        } else if (d > 9216.0D) {
            return 7;
        } else if (d > 4096.0D) {
            return 9;
        } else if (d > 1024.0D) {
            return 11;
        } else if (d > 576.0D) {
            return 13;
        } else {
            return d > 256.0D ? 14 : 15;
        }
    }

}
