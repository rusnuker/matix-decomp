// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.advancements.critereon;

import java.util.Iterator;
import java.util.List;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class SummonedEntityTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation ID;
    private final Map<PlayerAdvancements, Listeners> listeners;
    
    public SummonedEntityTrigger() {
        this.listeners = Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation getId() {
        return SummonedEntityTrigger.ID;
    }
    
    @Override
    public void addListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        Listeners summonedentitytrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (summonedentitytrigger$listeners == null) {
            summonedentitytrigger$listeners = new Listeners(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, summonedentitytrigger$listeners);
        }
        summonedentitytrigger$listeners.add(listener);
    }
    
    @Override
    public void removeListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        final Listeners summonedentitytrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (summonedentitytrigger$listeners != null) {
            summonedentitytrigger$listeners.remove(listener);
            if (summonedentitytrigger$listeners.isEmpty()) {
                this.listeners.remove(playerAdvancementsIn);
            }
        }
    }
    
    @Override
    public void removeAllListeners(final PlayerAdvancements playerAdvancementsIn) {
        this.listeners.remove(playerAdvancementsIn);
    }
    
    @Override
    public Instance deserializeInstance(final JsonObject json, final JsonDeserializationContext context) {
        final EntityPredicate entitypredicate = EntityPredicate.deserialize(json.get("entity"));
        return new Instance(entitypredicate);
    }
    
    public void trigger(final EntityPlayerMP player, final Entity entity) {
        final Listeners summonedentitytrigger$listeners = this.listeners.get(player.getAdvancements());
        if (summonedentitytrigger$listeners != null) {
            summonedentitytrigger$listeners.trigger(player, entity);
        }
    }
    
    static {
        ID = new ResourceLocation("summoned_entity");
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final EntityPredicate entity;
        
        public Instance(final EntityPredicate entity) {
            super(SummonedEntityTrigger.ID);
            this.entity = entity;
        }
        
        public boolean test(final EntityPlayerMP player, final Entity entity) {
            return this.entity.test(player, entity);
        }
    }
    
    static class Listeners
    {
        private final PlayerAdvancements playerAdvancements;
        private final Set<Listener<Instance>> listeners;
        
        public Listeners(final PlayerAdvancements playerAdvancementsIn) {
            this.listeners = Sets.newHashSet();
            this.playerAdvancements = playerAdvancementsIn;
        }
        
        public boolean isEmpty() {
            return this.listeners.isEmpty();
        }
        
        public void add(final Listener<Instance> listener) {
            this.listeners.add(listener);
        }
        
        public void remove(final Listener<Instance> listener) {
            this.listeners.remove(listener);
        }
        
        public void trigger(final EntityPlayerMP player, final Entity entity) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.listeners) {
                if (listener.getCriterionInstance().test(player, entity)) {
                    if (list == null) {
                        list = Lists.newArrayList();
                    }
                    list.add(listener);
                }
            }
            if (list != null) {
                for (final Listener<Instance> listener2 : list) {
                    listener2.grantCriterion(this.playerAdvancements);
                }
            }
        }
    }
}
