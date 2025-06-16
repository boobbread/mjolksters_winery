package com.mjolkster.mjolksters_winery.client.renderer;

import com.mjolkster.mjolksters_winery.common.block.entity.TrellisBlockEntity;
import com.mjolkster.mjolksters_winery.util.TrellisConnections;
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
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;

import static com.mjolkster.mjolksters_winery.common.block.TrellisBlock.CONNECTION;
import static com.mjolkster.mjolksters_winery.common.block.TrellisBlock.TOP;
import static java.lang.Math.clamp;

public class TrellisBlockRenderer implements BlockEntityRenderer<TrellisBlockEntity> {
    public TrellisBlockRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(TrellisBlockEntity be, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight, int packedOverlay) {

        int bushAge = be.bushAge;
        boolean hasBush = be.hasBush;

        TextureAtlas atlas = Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS);
        TextureAtlasSprite leavesSprite = atlas.getSprite(ResourceLocation.fromNamespaceAndPath("mjolksters_winery", "block/grape_trellis_leaves"));
        TextureAtlasSprite berrySprite = atlas.getSprite(ResourceLocation.fromNamespaceAndPath("mjolksters_winery", "block/grape_trellis_berries"));


        BlockState state = be.getBlockState();
        boolean isTop = state.getValue(TOP);

        TrellisConnections tCon = state.getValue(CONNECTION);
        boolean isWest = false;
        boolean isEast = false;

        if (tCon == TrellisConnections.EAST) {
            isEast = true;
        } else if (tCon == TrellisConnections.WEST) {
            isWest = true;
        } else if (tCon == TrellisConnections.EAST_WEST) {
            isEast = true;
            isWest = true;
        }

        float y1 = 0;
        float y2 = 0.99999f;

        if (!isTop) {
            y1 = 6f / 16f;
        }

        int age = clamp(bushAge, 0, 2);
        float halfWidth = 3.0f / 16f + (age * (2.5f / 16f));
        float center = 0.5f;
        float x1 = center - halfWidth;
        float x2 = center + halfWidth;

        if (!isEast) {
            x2 = Math.min(x2, 11f / 16f);
        }
        if (!isWest) {
            x1 = Math.max(x1, 5f / 16f);
        }

        float z1 = 5f / 16f;
        float z2 = 11f / 16f;

        int argb = be.grapeColour;

        float a = 1.0f;
        float r = ((argb >> 16) & 0xFF) / 255.0f;
        float g = ((argb >> 8) & 0xFF) / 255.0f;
        float b = (argb & 0xFF) / 255.0f;


        VertexConsumer consumer = buffer.getBuffer(RenderType.translucent());
        Matrix4f matrix = poseStack.last().pose();

        if (hasBush){
            CuboidRenderer.renderCuboid(consumer, matrix, x1, x2, y1, y2, z1, z2, 1.0f, 1.0f, 1.0f, a, leavesSprite, packedLight, packedOverlay);
        }

        if (bushAge == 3) {
            CuboidRenderer.renderCuboid(consumer, matrix, x1, x2, y1, y2, z1, z2, r, g , b, a, berrySprite, packedLight, packedOverlay);
        }
    }
}
