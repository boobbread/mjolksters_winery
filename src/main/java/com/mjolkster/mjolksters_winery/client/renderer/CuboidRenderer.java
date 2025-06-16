package com.mjolkster.mjolksters_winery.client.renderer;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.joml.Matrix4f;

public class CuboidRenderer {

    public static void renderCuboid(VertexConsumer consumer, Matrix4f matrix, float x1, float x2, float y1, float y2, float z1, float z2,
                              float r, float g, float b, float a, TextureAtlasSprite sprite, int packedLight, int packedOverlay){

        // Top Face
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

        // North Face
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

        // West Face
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

        // South Face
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

        // East Face
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

        // Bottom Face
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
