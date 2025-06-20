package com.mjolkster.mjolksters_winery.client.renderer;

import com.mjolkster.mjolksters_winery.common.block.entity.DemijohnBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Matrix4f;

public class DemijohnBlockRenderer implements BlockEntityRenderer<DemijohnBlockEntity> {
    public DemijohnBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(DemijohnBlockEntity be, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight, int packedOverlay) {

        FluidStack fluid = be.fluidTank.getFluid();
        float x1 = (5f / 16f);
        float x2 = (11f / 16f);
        float y1 = (1f / 16f)+ 0.009f;
        float z1 = (5f / 16f);
        float z2 = (11f / 16f);

        TextureAtlas atlas = Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS);
        TextureAtlasSprite sprite = atlas.getSprite(ResourceLocation.fromNamespaceAndPath("minecraft", "block/water_still"));


        if (sprite == null) {
            return;
        }

        int argb = be.inputJuiceColour;

        float a = 1.0f;
        float r = ((argb >> 16) & 0xFF) / 255.0f;
        float g = ((argb >> 8) & 0xFF) / 255.0f;
        float b = (argb & 0xFF) / 255.0f;

        float fillRatio = Math.min(1f, fluid.getAmount() / (float) be.fluidTank.getCapacity());
        float y2 = y1 + ((8f/16f - y1) * fillRatio);

        if (fillRatio != 0) {

            VertexConsumer consumer = buffer.getBuffer(RenderType.translucent());
            Matrix4f matrix = poseStack.last().pose();

            CuboidRenderer.renderCuboid(consumer, matrix, x1, x2, y1, y2, z1, z2, r, g, b, a, sprite, packedLight, packedOverlay);

        }
    }
}