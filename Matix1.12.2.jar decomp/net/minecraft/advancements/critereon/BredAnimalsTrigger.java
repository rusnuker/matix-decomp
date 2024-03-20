// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.advancements.critereon;

import java.util.Iterator;
import java.util.List;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class BredAnimalsTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation ID;
    private final Map<PlayerAdvancements, Listeners> listeners;
    
    public BredAnimalsTrigger() {
        this.listeners = Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation getId() {
        return BredAnimalsTrigger.ID;
    }
    
    @Override
    public void addListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        Listeners bredanimalstrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (bredanimalstrigger$listeners == null) {
            bredanimalstrigger$listeners = new Listeners(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, bredanimalstrigger$listeners);
        }
        bredanimalstrigger$listeners.add(listener);
    }
    
    @Override
    public void removeListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        final Listeners bredanimalstrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (bredanimalstrigger$listeners != null) {
            bredanimalstrigger$listeners.remove(listener);
            if (bredanimalstrigger$listeners.isEmpty()) {
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
        final EntityPredicate entitypredicate = EntityPredicate.deserialize(json.get("parent"));
        final EntityPredicate entitypredicate2 = EntityPredicate.deserialize(json.get("partner"));
        final EntityPredicate entitypredicate3 = EntityPredicate.deserialize(json.get("child"));
        return new Instance(entitypredicate, entitypredicate2, entitypredicate3);
    }
    
    public void trigger(final EntityPlayerMP player, final EntityAnimal parent1, final EntityAnimal parent2, final EntityAgeable child) {
        final Listeners bredanimalstrigger$listeners = this.listeners.get(player.getAdvancements());
        if (bredanimalstrigger$listeners != null) {
            bredanimalstrigger$listeners.trigger(player, parent1, parent2, child);
        }
    }
    
    static {
        ID = new ResourceLocation("bred_animals");
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final EntityPredicate parent;
        private final EntityPredicate partner;
        private final EntityPredicate child;
        
        public Instance(final EntityPredicate parent, final EntityPredicate partner, final EntityPredicate child) {
            super(BredAnimalsTrigger.ID);
            this.parent = parent;
            this.partner = partner;
            this.child = child;
        }
        
        public boolean test(final EntityPlayerMP player, final EntityAnimal parent1In, final EntityAnimal parent2In, final EntityAgeable childIn) {
            return this.child.test(player, childIn) && ((this.parent.test(player, parent1In) && this.partner.test(player, parent2In)) || (this.parent.test(player, parent2In) && this.partner.test(player, parent1In)));
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
        
        public void trigger(final EntityPlayerMP player, final EntityAnimal parent1, final EntityAnimal parent2, final EntityAgeable child) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.listeners) {
                if (listener.getCriterionInstance().test(player, parent1, parent2, child)) {
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
