package me.benfah.doorsofinfinity.block.entity.renderer;

import com.google.common.collect.ImmutableList;
import me.benfah.doorsofinfinity.block.InfinityDoorBlock;
import me.benfah.doorsofinfinity.block.entity.InfinityDoorBlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class InfinityDoorBlockEntityRenderer extends BlockEntityRenderer<InfinityDoorBlockEntity>
{
    private static Random RANDOM = new Random();

    private static final List<RenderLayer> field_21732 = (List<RenderLayer>) IntStream.range(0, 16).mapToObj((i) -> {
        return RenderLayer.getEndPortal(i + 1);
    }).collect(ImmutableList.toImmutableList());

    public InfinityDoorBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher)
    {
        super(dispatcher);
    }

    public void render(InfinityDoorBlockEntity endPortalBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        matrixStack.push();
        Direction direction = endPortalBlockEntity.getCachedState().get(InfinityDoorBlock.FACING);
        matrixStack.translate(direction.getVector().getX() * 0.1, 0, direction.getVector().getZ() * 0.1);
        RANDOM.setSeed(31100L);
        double d = endPortalBlockEntity.getPos().getSquaredDistance(this.dispatcher.camera.getPos(), true);
        int k = this.getLayersToRender(d);
        float g = this.getHeight();
        Matrix4f matrix4f = matrixStack.peek().getModel();
        this.drawEndLayer(endPortalBlockEntity, g, 0.15F, matrix4f, vertexConsumerProvider.getBuffer((RenderLayer)field_21732.get(0)));

        for(int l = 1; l < 15; ++l) {
            this.drawEndLayer(endPortalBlockEntity, g, 2.0F / (float)(18 - l), matrix4f, vertexConsumerProvider.getBuffer((RenderLayer)field_21732.get(l)));
        }
        matrixStack.pop();
    }

    private void drawEndLayer(InfinityDoorBlockEntity endPortalBlockEntity, float f, float g, Matrix4f matrix4f, VertexConsumer vertexConsumer) {
        float h = (RANDOM.nextFloat() * 0.5F + 0.1F) * g;
        float i = (RANDOM.nextFloat() * 0.5F + 0.4F) * g;
        float j = (RANDOM.nextFloat() * 0.5F + 0.5F) * g;
        this.drawEndLayerFace(endPortalBlockEntity, matrix4f, vertexConsumer, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, h, i, j, Direction.SOUTH);
        this.drawEndLayerFace(endPortalBlockEntity, matrix4f, vertexConsumer, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, h, i, j, Direction.NORTH);
        this.drawEndLayerFace(endPortalBlockEntity, matrix4f, vertexConsumer, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, h, i, j, Direction.EAST);
        this.drawEndLayerFace(endPortalBlockEntity, matrix4f, vertexConsumer, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, h, i, j, Direction.WEST);
        this.drawEndLayerFace(endPortalBlockEntity, matrix4f, vertexConsumer, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, h, i, j, Direction.DOWN);
        this.drawEndLayerFace(endPortalBlockEntity, matrix4f, vertexConsumer, 0.0F, 1.0F, f, f, 1.0F, 1.0F, 0.0F, 0.0F, h, i, j, Direction.UP);
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

    protected float getHeight() {
        return 0.75F;
    }

}
