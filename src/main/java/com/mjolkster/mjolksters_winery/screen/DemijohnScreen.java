package com.mjolkster.mjolksters_winery.screen;

import com.mjolkster.mjolksters_winery.Winery;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class DemijohnScreen extends AbstractContainerScreen<DemijohnMenu> {
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Winery.MODID,"textures/gui/demijohn_gui.png");
    private static final ResourceLocation ARROW_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Winery.MODID,"textures/gui/demijohn_progress.png");

    public DemijohnScreen(DemijohnMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressArrow(guiGraphics, x, y);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if (menu.isCrafting()) {
            int progress = menu.getScaledArrowProgress(); // 0 → 67
            int totalFrames = 6;
            int frameHeight = 67;
            int frameWidth = 38;

            int currentFrame = menu.getCurrentFrame(totalFrames);
            int vOffset = currentFrame * frameHeight;

            int textureY = vOffset + (frameHeight - progress); // texture V offset
            int drawY = y + 8 + (frameHeight - progress);       // screen Y position

            guiGraphics.blit(
                    ARROW_TEXTURE,
                    x + 114, drawY,        // on-screen position
                    0, textureY,           // source texture (u, v)
                    frameWidth, progress,  // size: full width, partial height
                    frameWidth, frameHeight * totalFrames // total texture dimensions
            );
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
        pGuiGraphics.drawString(font, title, leftPos + 8, topPos + 6, 0xFFD3D3D3, false);
        pGuiGraphics.drawString(font, playerInventoryTitle, leftPos + 8, topPos + 72, 0xFFD3D3D3, false);
    }
}
