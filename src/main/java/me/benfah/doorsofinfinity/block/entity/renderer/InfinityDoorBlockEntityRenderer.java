package me.benfah.doorsofinfinity.block.entity.renderer;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import me.benfah.doorsofinfinity.block.InfinityDoorBlock;
import me.benfah.doorsofinfinity.block.entity.InfinityDoorBlockEntity;
import me.benfah.doorsofinfinity.utils.MCUtils;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class InfinityDoorBlockEntityRenderer extends BlockEntityRenderer<InfinityDoorBlockEntity>
{
    private static Random RANDOM = Util.make(new Random(), r -> r.setSeed(31100));

    private static final List<RenderLayer> field_21732 = (List<RenderLayer>) IntStream.range(0, 16).mapToObj((i) -> {
        return RenderLayer.getEndPortal(i + 1);
    }).collect(ImmutableList.toImmutableList());

    private MinecraftClient client = MinecraftClient.getInstance();

    public InfinityDoorBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher)
    {
        super(dispatcher);
    }

    public static RenderLayer getOwnRenderLayer()
    {
        RenderLayer.MultiPhaseParameters params = RenderLayer.MultiPhaseParameters.builder().transparency(new RenderPhase.Transparency("translucent_transparency", () -> {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
        }, () -> {
            RenderSystem.disableBlend();
        })).build(true);

        return RenderLayer.of("colored_upgrade", VertexFormats.POSITION_COLOR, 7, 256, false, true, params);

    }

    @Override
    public void render(InfinityDoorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay)
    {
        Direction direction = entity.getCachedState().get(InfinityDoorBlock.FACING);

        if(!MCUtils.isIPPresent())
        drawEndTexture(entity, tickDelta, matrices, vertexConsumers, direction);
        
        
        HitResult rayTrace = client.player.raycast(client.interactionManager.getReachDistance(), tickDelta, false);

        if(client.player.isSneaking() && entity.getCachedState().get(InfinityDoorBlock.HALF) == DoubleBlockHalf.LOWER)
        {
            BlockHitResult blockHitResult = (BlockHitResult) rayTrace;

            if(blockHitResult.getBlockPos().equals(entity.getPos()) || blockHitResult.getBlockPos().equals(entity.getPos().up()))
            {
                if(entity.link != null)
                {
                    matrices.push();
                    transformToFace(matrices, direction);
                    matrices.translate(0.5, 1.9, -0.001);
                    matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180));
                    matrices.scale(0.01F, 0.01F, 1);
                    drawCenteredTextWithRect(new TranslatableText("text.doorsofinfinity.installed_upgrades", entity.link.getUpgrades()).getString(), 0, 0xFFFFFF, false, matrices, vertexConsumers, false, 0xFFFFFF, light, 0.168F, 0.341F, 0.156F, 0.5F);

                    matrices.pop();
                }

            }
        }
    }

    public static void drawRect(VertexConsumerProvider vertexConsumerProvider, Matrix4f matrix, int x1, int y1, int x2, int y2, float r, float g, float b, float a)
    {
        VertexConsumer consumer = vertexConsumerProvider.getBuffer(getOwnRenderLayer());

        consumer.vertex(matrix, x1, y2, 0).color(r, g, b, a).next();
        consumer.vertex(matrix, x2, y2, 0).color(r, g, b, a).next();
        consumer.vertex(matrix, x2, y1, 0).color(r, g, b, a).next();
        consumer.vertex(matrix, x1, y1, 0).color(r, g, b, a).next();
    }

    public static void transformToFace(MatrixStack stack, Direction direction)
    {
        stack.translate(.5, 0, .5);
        Quaternion rotationQuaternion = direction.getRotationQuaternion();
        rotationQuaternion.hamiltonProduct(Vector3f.POSITIVE_X.getDegreesQuaternion(-90.0F));

        stack.multiply(rotationQuaternion);
        stack.translate(-.5, 0, -.5);
    }

    public void drawRightBoundText(String text, int x, int y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumers, boolean seeThrough, int backgroundColor, int light)
    {
        int textLength = dispatcher.getTextRenderer().getWidth(text);
        dispatcher.getTextRenderer().draw(text, x - textLength, y, color, shadow, matrix, vertexConsumers, seeThrough, backgroundColor, light);
    }

    public void drawCenteredText(String text, int y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumers, boolean seeThrough, int backgroundColor, int light)
    {
        int textLength = dispatcher.getTextRenderer().getWidth(text);
        dispatcher.getTextRenderer().draw(text, -textLength / 2, y, color, shadow, matrix, vertexConsumers, seeThrough, backgroundColor, light);
    }

    public void drawCenteredTextWithRect(String text, int y, int color, boolean shadow, MatrixStack matrices, VertexConsumerProvider vertexConsumers, boolean seeThrough, int backgroundColor, int light, float r, float g, float b, float a)
    {
        int textLength = dispatcher.getTextRenderer().getWidth(text);
        int border = 4;

        drawRect(vertexConsumers, matrices.peek().getModel(), -textLength / 2 - border, y - 2, +textLength / 2 + border, y + dispatcher.getTextRenderer().fontHeight + 2, r, g, b, a);
        matrices.push();
        matrices.translate(0, 0, -0.001);
        drawCenteredText(text, y, color, shadow, matrices.peek().getModel(), vertexConsumers, seeThrough, backgroundColor, light);
        matrices.pop();
    }

    public void drawRightBoundTextWithRect(String text, int x, int y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumers, boolean seeThrough, int backgroundColor, int light, float r, float g, float b, float a)
    {
        int border = 4;
        int textLength = dispatcher.getTextRenderer().getWidth(text);
        drawRect(vertexConsumers, matrix, x - textLength - border * 2, y - 2, x, y + dispatcher.getTextRenderer().fontHeight + 2, r, g, b, a);
        drawRightBoundText(text, x - border, y, color, shadow, matrix, vertexConsumers, seeThrough, backgroundColor, light);
    }

    public void drawEndTexture(InfinityDoorBlockEntity endPortalBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, Direction direction)
    {
        matrixStack.push();
        matrixStack.translate(direction.getVector().getX() * 0.1, 0, direction.getVector().getZ() * 0.1);
        RANDOM.setSeed(31100L);
        
        Matrix4f matrix4f = matrixStack.peek().getModel();
        this.drawEndLayer(endPortalBlockEntity, 0.15F, matrix4f, vertexConsumerProvider.getBuffer((RenderLayer)field_21732.get(0)));

        for(int l = 1; l < 16; ++l) {
            this.drawEndLayer(endPortalBlockEntity, 2.0F / (float)(18 - l), matrix4f, vertexConsumerProvider.getBuffer((RenderLayer)field_21732.get(l)));
        }
        matrixStack.pop();
    }

    private void drawEndLayer(InfinityDoorBlockEntity endPortalBlockEntity, float g, Matrix4f matrix4f, VertexConsumer vertexConsumer) {
        float h = (RANDOM.nextFloat() * 0.5F + 0.1F) * g;
        float i = (RANDOM.nextFloat() * 0.5F + 0.4F) * g;
        float j = (RANDOM.nextFloat() * 0.5F + 0.5F) * g;
        this.drawEndLayerFace(endPortalBlockEntity, matrix4f, vertexConsumer, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, h, i, j, Direction.SOUTH);
        this.drawEndLayerFace(endPortalBlockEntity, matrix4f, vertexConsumer, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, h, i, j, Direction.NORTH);
        this.drawEndLayerFace(endPortalBlockEntity, matrix4f, vertexConsumer, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, h, i, j, Direction.EAST);
        this.drawEndLayerFace(endPortalBlockEntity, matrix4f, vertexConsumer, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, h, i, j, Direction.WEST);
        this.drawEndLayerFace(endPortalBlockEntity, matrix4f, vertexConsumer, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, h, i, j, Direction.DOWN);
    }

    private void drawEndLayerFace(InfinityDoorBlockEntity endPortalBlockEntity, Matrix4f matrix4f, VertexConsumer vertexConsumer, float f, float g, float h, float i, float j, float k, float l, float m, float n, float o, float p, Direction direction) {

        if(direction == endPortalBlockEntity.getCachedState().get(InfinityDoorBlock.FACING).getOpposite())
        {
            vertexConsumer.vertex(matrix4f, f, h, j).color(n, o, p, 1.0F).next();
            vertexConsumer.vertex(matrix4f, g, h, k).color(n, o, p, 1.0F).next();
            vertexConsumer.vertex(matrix4f, g, i, l).color(n, o, p, 1.0F).next();
            vertexConsumer.vertex(matrix4f, f, i, m).color(n, o, p, 1.0F).next();
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
