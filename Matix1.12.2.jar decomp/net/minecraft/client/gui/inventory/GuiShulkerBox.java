// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui.inventory;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerShulkerBox;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiShulkerBox extends GuiContainer
{
    private static final ResourceLocation GUI_TEXTURE;
    private final IInventory inventory;
    private final InventoryPlayer playerInventory;
    
    public GuiShulkerBox(final InventoryPlayer p_i47233_1_, final IInventory p_i47233_2_) {
        super(new ContainerShulkerBox(p_i47233_1_, p_i47233_2_, Minecraft.getMinecraft().player));
        this.playerInventory = p_i47233_1_;
        this.inventory = p_i47233_2_;
        ++this.ySize;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        this.fontRenderer.drawString(this.inventory.getDisplayName().getUnformattedText(), 8, 6, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiShulkerBox.GUI_TEXTURE);
        final int i = (this.width - this.xSize) / 2;
        final int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    }
    
    static {
        GUI_TEXTURE = new ResourceLocation("textures/gui/container/shulker_box.png");
    }
}
