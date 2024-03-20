// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.tileentity.TileEntity;
import java.util.List;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.block.Block;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.src.CustomColors;
import net.minecraft.src.Config;
import net.minecraft.init.Blocks;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.model.ModelSign;
import net.minecraft.util.ResourceLocation;
import net.minecraft.tileentity.TileEntitySign;

public class TileEntitySignRenderer extends TileEntitySpecialRenderer<TileEntitySign>
{
    private static final ResourceLocation SIGN_TEXTURE;
    private final ModelSign model;
    
    public TileEntitySignRenderer() {
        this.model = new ModelSign();
    }
    
    @Override
    public void render(final TileEntitySign te, final double x, final double y, final double z, final float partialTicks, final int destroyStage, final float alpha) {
        final Block block = te.getBlockType();
        GlStateManager.pushMatrix();
        final float f = 0.6666667f;
        if (block == Blocks.STANDING_SIGN) {
            GlStateManager.translate((float)x + 0.5f, (float)y + 0.5f, (float)z + 0.5f);
            final float f2 = te.getBlockMetadata() * 360 / 16.0f;
            GlStateManager.rotate(-f2, 0.0f, 1.0f, 0.0f);
            this.model.signStick.showModel = true;
        }
        else {
            final int k = te.getBlockMetadata();
            float f3 = 0.0f;
            if (k == 2) {
                f3 = 180.0f;
            }
            if (k == 4) {
                f3 = 90.0f;
            }
            if (k == 5) {
                f3 = -90.0f;
            }
            GlStateManager.translate((float)x + 0.5f, (float)y + 0.5f, (float)z + 0.5f);
            GlStateManager.rotate(-f3, 0.0f, 1.0f, 0.0f);
            GlStateManager.translate(0.0f, -0.3125f, -0.4375f);
            this.model.signStick.showModel = false;
        }
        if (destroyStage >= 0) {
            this.bindTexture(TileEntitySignRenderer.DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0f, 2.0f, 1.0f);
            GlStateManager.translate(0.0625f, 0.0625f, 0.0625f);
            GlStateManager.matrixMode(5888);
        }
        else {
            this.bindTexture(TileEntitySignRenderer.SIGN_TEXTURE);
        }
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.6666667f, -0.6666667f, -0.6666667f);
        this.model.renderSign();
        GlStateManager.popMatrix();
        final FontRenderer fontrenderer = this.getFontRenderer();
        final float f4 = 0.010416667f;
        GlStateManager.translate(0.0f, 0.33333334f, 0.046666667f);
        GlStateManager.scale(0.010416667f, -0.010416667f, 0.010416667f);
        GlStateManager.glNormal3f(0.0f, 0.0f, -0.010416667f);
        GlStateManager.depthMask(false);
        int i = 0;
        if (Config.isCustomColors()) {
            i = CustomColors.getSignTextColor(i);
        }
        if (destroyStage < 0) {
            for (int j = 0; j < te.signText.length; ++j) {
                if (te.signText[j] != null) {
                    final ITextComponent itextcomponent = te.signText[j];
                    final List<ITextComponent> list = GuiUtilRenderComponents.splitText(itextcomponent, 90, fontrenderer, false, true);
                    String s = (list != null && !list.isEmpty()) ? list.get(0).getFormattedText() : "";
                    if (j == te.lineBeingEdited) {
                        s = "> " + s + " <";
                        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, j * 10 - te.signText.length * 5, i);
                    }
                    else {
                        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, j * 10 - te.signText.length * 5, i);
                    }
                }
            }
        }
        GlStateManager.depthMask(true);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }
    
    static {
        SIGN_TEXTURE = new ResourceLocation("textures/entity/sign.png");
    }
}
