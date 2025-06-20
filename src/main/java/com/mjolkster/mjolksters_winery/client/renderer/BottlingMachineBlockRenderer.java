package com.mjolkster.mjolksters_winery.client.renderer;

import com.mjolkster.mjolksters_winery.common.block.entity.BottlingMachineBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Matrix4f;

import java.util.EnumMap;
import java.util.Map;

import static com.mjolkster.mjolksters_winery.common.block.BottlingMachineBlock.FACING;

public class BottlingMachineBlockRenderer implements BlockEntityRenderer<BottlingMachineBlockEntity> {
    public BottlingMachineBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    public static final Map<Direction, float[]> VERTICES = new EnumMap<>(Direction.class);
    static {
        VERTICES.put(Direction.NORTH, new float[]{1.75f, 6.75f, 9.25f, 14.75f});
        VERTICES.put(Direction.EAST, new float[]{1.25f, 6.75f, 1.75f, 6.75f});
        VERTICES.put(Direction.SOUTH, new float[]{9.25f, 14.25f, 1.25f, 6.75f});
        VERTICES.put(Direction.WEST, new float[]{9.25f, 14.75f, 9.25f, 14.25f});
    }

    @Override
    public void render(BottlingMachineBlockEntity be, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight, int packedOverlay) {

        FluidStack fluid = be.fluidTank.getFluid();

        BlockState state = be.getBlockState();
        float[] array = VERTICES.get(state.getValue(FACING));

        float x1 = array[0] / 16f; // Left inner wall
        float x2 = array[1] / 16f; // Right inner wall
        float y1 = (1f / 16f)+ 0.009f; // Bottom inner floor
        float z1 = array[2] / 16f; // Front inner wall
        float z2 = array[3] / 16f; // Back inner wall

        TextureAtlas atlas = Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS);
        TextureAtlasSprite sprite = atlas.getSprite(ResourceLocation.fromNamespaceAndPath("minecraft", "block/water_still"));

        if (sprite == null) {
            return;
        }

        int argb = be.inputWineColour;

        float a = 1.0f;
        float r = ((argb >> 16) & 0xFF) / 255.0f;
        float g = ((argb >> 8) & 0xFF) / 255.0f;
        float b = (argb & 0xFF) / 255.0f;

        float progress = be.fillProgress;

        float fillRatio = Math.min(1f, (fluid.getAmount() - 250 * progress) / (float) be.fluidTank.getCapacity());
        float y2 = y1 + ((8f/16f - y1) * fillRatio);

        if (fillRatio != 0) {

            VertexConsumer consumer = buffer.getBuffer(RenderType.translucent());
            Matrix4f matrix = poseStack.last().pose();

            CuboidRenderer.renderCuboid(consumer, matrix, x1, x2, y1, y2, z1, z2, r, g, b, a, sprite, packedLight, packedOverlay);
        }
    }
}