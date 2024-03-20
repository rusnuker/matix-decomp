// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.passive.EntityZombieHorse;
import net.minecraft.entity.passive.EntityMule;
import net.minecraft.entity.passive.EntityDonkey;
import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.util.ResourceLocation;
import java.util.Map;
import net.minecraft.entity.passive.AbstractHorse;

public class RenderAbstractHorse extends RenderLiving<AbstractHorse>
{
    private static final Map<Class<?>, ResourceLocation> MAP;
    private final float scale;
    
    public RenderAbstractHorse(final RenderManager p_i47212_1_) {
        this(p_i47212_1_, 1.0f);
    }
    
    public RenderAbstractHorse(final RenderManager p_i47213_1_, final float p_i47213_2_) {
        super(p_i47213_1_, new ModelHorse(), 0.75f);
        this.scale = p_i47213_2_;
    }
    
    @Override
    protected void preRenderCallback(final AbstractHorse entitylivingbaseIn, final float partialTickTime) {
        GlStateManager.scale(this.scale, this.scale, this.scale);
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final AbstractHorse entity) {
        return RenderAbstractHorse.MAP.get(entity.getClass());
    }
    
    static {
        (MAP = Maps.newHashMap()).put(EntityDonkey.class, new ResourceLocation("textures/entity/horse/donkey.png"));
        RenderAbstractHorse.MAP.put(EntityMule.class, new ResourceLocation("textures/entity/horse/mule.png"));
        RenderAbstractHorse.MAP.put(EntityZombieHorse.class, new ResourceLocation("textures/entity/horse/horse_zombie.png"));
        RenderAbstractHorse.MAP.put(EntitySkeletonHorse.class, new ResourceLocation("textures/entity/horse/horse_skeleton.png"));
    }
}
