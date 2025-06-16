package com.mjolkster.mjolksters_winery.client.screen;

import com.mjolkster.mjolksters_winery.Winery;
import com.mjolkster.mjolksters_winery.common.block.entity.DistillerBlockEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class DistillerScreen extends AbstractContainerScreen<DistillerMenu> {
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Winery.MODID, "textures/gui/distiller_gui.png");
    private static final ResourceLocation INPUT_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Winery.MODID, "textures/gui/distiller_input_fullness.png");
    private static final ResourceLocation OUTPUT_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Winery.MODID, "textures/gui/distiller_output_fullness.png");

    public DistillerScreen(DistillerMenu menu, Inventory playerInventory, Component title) {
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
        renderInputFullness(guiGraphics, x, y);
        renderOutputFullness(guiGraphics, x, y);
    }

    private void renderInputFullness(GuiGraphics guiGraphics, int x, int y) {

        int bottleFullness = menu.getInputVolume();
        int v = 31 - bottleFullness;
        int yPos = y + 50 + v;

        int argb = menu.data.get(4);
        float r = ((argb >> 16) & 0xFF) / 255.0f;
        float g = ((argb >> 8) & 0xFF) / 255.0f;
        float b = (argb & 0xFF) / 255.0f;
        float a = 0.8f;

        guiGraphics.setColor(r,g,b,a);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        guiGraphics.blit(INPUT_TEXTURE, x + 83, yPos, 0,v, 36, bottleFullness, 36, 31);
        guiGraphics.setColor(1,1,1,1);
        RenderSystem.disableBlend();
    }

    private void renderOutputFullness(GuiGraphics guiGraphics, int x, int y) {

        int bottleFullness = menu.getOutputVolume();
        int v = 56 - bottleFullness;
        int yPos = y + 25 + v;

        int argb = menu.data.get(4);
        float r = ((argb >> 16) & 0xFF) / 255.0f;
        float g = ((argb >> 8) & 0xFF) / 255.0f;
        float b = (argb & 0xFF) / 255.0f;
        float a = 0.8f;

        guiGraphics.setColor(r,g,b,a);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        guiGraphics.blit(OUTPUT_TEXTURE, x + 137, yPos, 0,v, 14, bottleFullness, 14, 56);
        guiGraphics.setColor(1,1,1,1);
        RenderSystem.disableBlend();
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
        pGuiGraphics.drawString(font, title, leftPos + 8, topPos + 6, 0xFFD3D3D3, false);
        pGuiGraphics.drawString(font, playerInventoryTitle, leftPos + 8, topPos + 72, 0xFFD3D3D3, false);
    }
}
