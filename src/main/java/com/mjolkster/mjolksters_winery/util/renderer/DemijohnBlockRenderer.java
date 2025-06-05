package com.mjolkster.mjolksters_winery.util.renderer;

import com.mjolkster.mjolksters_winery.block.entity.DemijohnBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import org.joml.Matrix4f;

public class DemijohnBlockRenderer implements BlockEntityRenderer<DemijohnBlockEntity> {
    public DemijohnBlockRenderer(BlockEntityRendererProvider.Context context) {
        // Constructor can remain empty unless you need to initialize something
    }
    public int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);

    }
    @Override
    public void render(DemijohnBlockEntity be, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight, int packedOverlay) {

        FluidStack fluid = be.fluidTank.getFluid();
        float x1 = (5f / 16f);  // Left inner wall
        float x2 = (11f / 16f); // Right inner wall
        float y1 = (1f / 16f)+ 0.009f;    // Bottom inner floor
        float z1 = (5f / 16f);  // Front inner wall
        float z2 = (11f / 16f); // Back inner wall

        TextureAtlas atlas = Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS);
        TextureAtlasSprite sprite = atlas.getSprite(ResourceLocation.fromNamespaceAndPath("minecraft", "block/water_still"));


        if (sprite == null) {
            System.out.print(sprite.toString());
            return;
        }

        int argb = be.inputJuiceColour;

        float a = 1.0f;
        float r = ((argb >> 16) & 0xFF) / 255.0f;
        float g = ((argb >> 8) & 0xFF) / 255.0f;
        float b = (argb & 0xFF) / 255.0f;

        float fillRatio = Math.min(1f, fluid.getAmount() / (float) be.fluidTank.getCapacity()); // Ensure you have getCapacity
        float y2 = y1 + ((8f/16f - y1) * fillRatio);

        if (fillRatio != 0) {

            VertexConsumer consumer = buffer.getBuffer(RenderType.translucent());
            Matrix4f matrix = poseStack.last().pose();

            consumer.addVertex(matrix, x1, y2, z1)
                    .setColor(r, g, b, a)
                    .setUv(sprite.getU(x1), sprite.getV(z1))
                    .setUv1(packedLight, packedOverlay)
                    .setLight(packedLight)
                    .setNormal(0, 1, 0);

            consumer.addVertex(matrix, x1, y2, z2)
                    .setColor(r, g, b, a)
                    .setUv(sprite.getU(x1), sprite.getV(z2))
                    .setUv1(packedLight, packedOverlay)
                    .setLight(packedLight)
                    .setNormal(0, 1, 0);

            consumer.addVertex(matrix, x2, y2, z2)
                    .setColor(r, g, b, a)
                    .setUv(sprite.getU(x2), sprite.getV(z2))
                    .setUv1(packedLight, packedOverlay)
                    .setLight(packedLight)
                    .setNormal(0, 1, 0);

            consumer.addVertex(matrix, x2, y2, z1)
                    .setColor(r, g, b, a)
                    .setUv(sprite.getU(x2), sprite.getV(z1))
                    .setUv1(packedLight, packedOverlay)
                    .setLight(packedLight)
                    .setNormal(0, 1, 0);

            // North Face (corrected UV mapping)
            consumer.addVertex(matrix, x1, y1, z1)
                    .setColor(r, g, b, a)
                    .setUv(sprite.getU(x1), sprite.getV(y1))
                    .setUv1(packedLight, packedOverlay)
                    .setLight(packedLight)
                    .setNormal(0, 0, -1);

            consumer.addVertex(matrix, x1, y2, z1)
                    .setColor(r, g, b, a)
                    .setUv(sprite.getU(x1), sprite.getV(y2))
                    .setUv1(packedLight, packedOverlay)
                    .setLight(packedLight)
                    .setNormal(0, 0, -1);

            consumer.addVertex(matrix, x2, y2, z1)
                    .setColor(r, g, b, a)
                    .setUv(sprite.getU(x2), sprite.getV(y2))
                    .setUv1(packedLight, packedOverlay)
                    .setLight(packedLight)
                    .setNormal(0, 0, -1);

            consumer.addVertex(matrix, x2, y1, z1)
                    .setColor(r, g, b, a)
                    .setUv(sprite.getU(x2), sprite.getV(y1))
                    .setUv1(packedLight, packedOverlay)
                    .setLight(packedLight)
                    .setNormal(0, 0, -1);

// West Face (x = x1)
            consumer.addVertex(matrix, x1, y1, z1)
                    .setColor(r, g, b, a)
                    .setUv(sprite.getU(z1), sprite.getV(y1))
                    .setUv1(packedLight, packedOverlay)
                    .setLight(packedLight)
                    .setNormal(-1, 0, 0);

            consumer.addVertex(matrix, x1, y1, z2)
                    .setColor(r, g, b, a)
                    .setUv(sprite.getU(z2), sprite.getV(y1))
                    .setUv1(packedLight, packedOverlay)
                    .setLight(packedLight)
                    .setNormal(-1, 0, 0);

            consumer.addVertex(matrix, x1, y2, z2)
                    .setColor(r, g, b, a)
                    .setUv(sprite.getU(z2), sprite.getV(y2))
                    .setUv1(packedLight, packedOverlay)
                    .setLight(packedLight)
                    .setNormal(-1, 0, 0);

            consumer.addVertex(matrix, x1, y2, z1)
                    .setColor(r, g, b, a)
                    .setUv(sprite.getU(z1), sprite.getV(y2))
                    .setUv1(packedLight, packedOverlay)
                    .setLight(packedLight)
                    .setNormal(-1, 0, 0);

// South Face (z = z2)
            consumer.addVertex(matrix, x1, y1, z2)
                    .setColor(r, g, b, a)
                    .setUv(sprite.getU(x1), sprite.getV(y1))
                    .setUv1(packedLight, packedOverlay)
                    .setLight(packedLight)
                    .setNormal(0, 0, 1);

            consumer.addVertex(matrix, x2, y1, z2)
                    .setColor(r, g, b, a)
                    .setUv(sprite.getU(x2), sprite.getV(y1))
                    .setUv1(packedLight, packedOverlay)
                    .setLight(packedLight)
                    .setNormal(0, 0, 1);

            consumer.addVertex(matrix, x2, y2, z2)
                    .setColor(r, g, b, a)
                    .setUv(sprite.getU(x2), sprite.getV(y2))
                    .setUv1(packedLight, packedOverlay)
                    .setLight(packedLight)
                    .setNormal(0, 0, 1);

            consumer.addVertex(matrix, x1, y2, z2)
                    .setColor(r, g, b, a)
                    .setUv(sprite.getU(x1), sprite.getV(y2))
                    .setUv1(packedLight, packedOverlay)
                    .setLight(packedLight)
                    .setNormal(0, 0, 1);

// East Face (x = x2)
            consumer.addVertex(matrix, x2, y1, z1)
                    .setColor(r, g, b, a)
                    .setUv(sprite.getU(z1), sprite.getV(y1))
                    .setUv1(packedLight, packedOverlay)
                    .setLight(packedLight)
                    .setNormal(1, 0, 0);

            consumer.addVertex(matrix, x2, y2, z1)
                    .setColor(r, g, b, a)
                    .setUv(sprite.getU(z1), sprite.getV(y2))
                    .setUv1(packedLight, packedOverlay)
                    .setLight(packedLight)
                    .setNormal(1, 0, 0);

            consumer.addVertex(matrix, x2, y2, z2)
                    .setColor(r, g, b, a)
                    .setUv(sprite.getU(z2), sprite.getV(y2))
                    .setUv1(packedLight, packedOverlay)
                    .setLight(packedLight)
                    .setNormal(1, 0, 0);

            consumer.addVertex(matrix, x2, y1, z2)
                    .setColor(r, g, b, a)
                    .setUv(sprite.getU(z2), sprite.getV(y1))
                    .setUv1(packedLight, packedOverlay)
                    .setLight(packedLight)
                    .setNormal(1, 0, 0);

// Bottom Face (y = y1)
            consumer.addVertex(matrix, x1, y1, z1)
                    .setColor(r, g, b, a)
                    .setUv(sprite.getU(x1), sprite.getV(z1))
                    .setUv1(packedLight, packedOverlay)
                    .setLight(packedLight)
                    .setNormal(0, -1, 0);

            consumer.addVertex(matrix, x2, y1, z1)
                    .setColor(r, g, b, a)
                    .setUv(sprite.getU(x2), sprite.getV(z1))
                    .setUv1(packedLight, packedOverlay)
                    .setLight(packedLight)
                    .setNormal(0, -1, 0);

            consumer.addVertex(matrix, x2, y1, z2)
                    .setColor(r, g, b, a)
                    .setUv(sprite.getU(x2), sprite.getV(z2))
                    .setUv1(packedLight, packedOverlay)
                    .setLight(packedLight)
                    .setNormal(0, -1, 0);

            consumer.addVertex(matrix, x1, y1, z2)
                    .setColor(r, g, b, a)
                    .setUv(sprite.getU(x1), sprite.getV(z2))
                    .setUv1(packedLight, packedOverlay)
                    .setLight(packedLight)
                    .setNormal(0, -1, 0);
        }
    }
}