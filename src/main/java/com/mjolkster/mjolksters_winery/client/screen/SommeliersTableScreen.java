package com.mjolkster.mjolksters_winery.client.screen;

import com.mjolkster.mjolksters_winery.MjolkstersWinery;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SommeliersTableScreen extends AbstractContainerScreen<SommeliersTableMenu> {
    private static final ResourceLocation SOMMELIERS_GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(MjolkstersWinery.MODID,"textures/gui/sommeliers_table_gui.png");

    public SommeliersTableScreen(SommeliersTableMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, Component.literal("Sommelier's Table"));
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, SOMMELIERS_GUI_TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(SOMMELIERS_GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
        pGuiGraphics.drawString(font, title, leftPos + 8, topPos + 6, 0xFFD3D3D3, false);
        pGuiGraphics.drawString(font, playerInventoryTitle, leftPos + 8, topPos + 72, 0xFFD3D3D3, false);
    }
}
