// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.realms;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;

public class RealmsSliderButton extends RealmsButton
{
    public float value;
    public boolean sliding;
    private final float minValue;
    private final float maxValue;
    private int steps;
    
    public RealmsSliderButton(final int buttonId, final int x, final int y, final int width, final int maxValueIn, final int p_i1056_6_) {
        this(buttonId, x, y, width, p_i1056_6_, 0, 1.0f, (float)maxValueIn);
    }
    
    public RealmsSliderButton(final int buttonId, final int x, final int y, final int width, final int p_i1057_5_, final int valueIn, final float minValueIn, final float maxValueIn) {
        super(buttonId, x, y, width, 20, "");
        this.value = 1.0f;
        this.minValue = minValueIn;
        this.maxValue = maxValueIn;
        this.value = this.toPct((float)valueIn);
        this.getProxy().displayString = this.getMessage();
    }
    
    public String getMessage() {
        return "";
    }
    
    public float toPct(final float p_toPct_1_) {
        return MathHelper.clamp((this.clamp(p_toPct_1_) - this.minValue) / (this.maxValue - this.minValue), 0.0f, 1.0f);
    }
    
    public float toValue(final float p_toValue_1_) {
        return this.clamp(this.minValue + (this.maxValue - this.minValue) * MathHelper.clamp(p_toValue_1_, 0.0f, 1.0f));
    }
    
    public float clamp(float p_clamp_1_) {
        p_clamp_1_ = this.clampSteps(p_clamp_1_);
        return MathHelper.clamp(p_clamp_1_, this.minValue, this.maxValue);
    }
    
    protected float clampSteps(float p_clampSteps_1_) {
        if (this.steps > 0) {
            p_clampSteps_1_ = (float)(this.steps * Math.round(p_clampSteps_1_ / this.steps));
        }
        return p_clampSteps_1_;
    }
    
    @Override
    public int getYImage(final boolean p_getYImage_1_) {
        return 0;
    }
    
    @Override
    public void renderBg(final int p_renderBg_1_, final int p_renderBg_2_) {
        if (this.getProxy().visible) {
            if (this.sliding) {
                this.value = (p_renderBg_1_ - (this.getProxy().x + 4)) / (float)(this.getProxy().getButtonWidth() - 8);
                this.value = MathHelper.clamp(this.value, 0.0f, 1.0f);
                final float f = this.toValue(this.value);
                this.clicked(f);
                this.value = this.toPct(f);
                this.getProxy().displayString = this.getMessage();
            }
            Minecraft.getMinecraft().getTextureManager().bindTexture(RealmsSliderButton.WIDGETS_LOCATION);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.blit(this.getProxy().x + (int)(this.value * (this.getProxy().getButtonWidth() - 8)), this.getProxy().y, 0, 66, 4, 20);
            this.blit(this.getProxy().x + (int)(this.value * (this.getProxy().getButtonWidth() - 8)) + 4, this.getProxy().y, 196, 66, 4, 20);
        }
    }
    
    @Override
    public void clicked(final int p_clicked_1_, final int p_clicked_2_) {
        this.value = (p_clicked_1_ - (this.getProxy().x + 4)) / (float)(this.getProxy().getButtonWidth() - 8);
        this.value = MathHelper.clamp(this.value, 0.0f, 1.0f);
        this.clicked(this.toValue(this.value));
        this.getProxy().displayString = this.getMessage();
        this.sliding = true;
    }
    
    public void clicked(final float p_clicked_1_) {
    }
    
    @Override
    public void released(final int p_released_1_, final int p_released_2_) {
        this.sliding = false;
    }
}
