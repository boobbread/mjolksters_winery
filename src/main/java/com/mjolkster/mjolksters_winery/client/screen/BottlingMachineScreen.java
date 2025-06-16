package com.mjolkster.mjolksters_winery.client.screen;

import com.mjolkster.mjolksters_winery.MjolkstersWinery;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BottlingMachineScreen extends AbstractContainerScreen<BottlingMachineMenu> {
    private static final ResourceLocation BOTTLING_MACHINE_GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(MjolkstersWinery.MODID,"textures/gui/bottling_machine_gui.png");
    private static final ResourceLocation GUI_BOTTLE_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(MjolkstersWinery.MODID,"textures/gui/aging_barrel_fullness.png");


    public BottlingMachineScreen(BottlingMachineMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, Component.literal("Auto Bottler"));
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BOTTLING_MACHINE_GUI_TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(BOTTLING_MACHINE_GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
        renderBottleFullness(guiGraphics, x, y);
    }

    private void renderBottleFullness(GuiGraphics guiGraphics, int x, int y) {

        int bottleFullness = menu.getBottleFullness();
        int v = 67 - bottleFullness;
        int yPos = y + 14 + v;

        int argb = menu.data.get(2);
        float r = ((argb >> 16) & 0xFF) / 255.0f;
        float g = ((argb >> 8) & 0xFF) / 255.0f;
        float b = (argb & 0xFF) / 255.0f;
        float a = 0.8f;

        guiGraphics.setColor(r,g,b,a);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        guiGraphics.blit(GUI_BOTTLE_TEXTURE, x + 69, yPos, 0,v, 38, bottleFullness, 38, 67);
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
