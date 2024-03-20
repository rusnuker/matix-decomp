// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.advancements.critereon;

import java.util.List;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.world.WorldServer;
import javax.annotation.Nullable;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.common.base.Optional;
import java.util.Iterator;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.properties.IProperty;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import net.minecraft.block.Block;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class PlacedBlockTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation ID;
    private final Map<PlayerAdvancements, Listeners> listeners;
    
    public PlacedBlockTrigger() {
        this.listeners = Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation getId() {
        return PlacedBlockTrigger.ID;
    }
    
    @Override
    public void addListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        Listeners placedblocktrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (placedblocktrigger$listeners == null) {
            placedblocktrigger$listeners = new Listeners(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, placedblocktrigger$listeners);
        }
        placedblocktrigger$listeners.add(listener);
    }
    
    @Override
    public void removeListener(final PlayerAdvancements playerAdvancementsIn, final Listener<Instance> listener) {
        final Listeners placedblocktrigger$listeners = this.listeners.get(playerAdvancementsIn);
        if (placedblocktrigger$listeners != null) {
            placedblocktrigger$listeners.remove(listener);
            if (placedblocktrigger$listeners.isEmpty()) {
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
        Block block = null;
        if (json.has("block")) {
            final ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(json, "block"));
            if (!Block.REGISTRY.containsKey(resourcelocation)) {
                throw new JsonSyntaxException("Unknown block type '" + resourcelocation + "'");
            }
            block = Block.REGISTRY.getObject(resourcelocation);
        }
        Map<IProperty<?>, Object> map = null;
        if (json.has("state")) {
            if (block == null) {
                throw new JsonSyntaxException("Can't define block state without a specific block type");
            }
            final BlockStateContainer blockstatecontainer = block.getBlockState();
            for (final Map.Entry<String, JsonElement> entry : JsonUtils.getJsonObject(json, "state").entrySet()) {
                final IProperty<?> iproperty = blockstatecontainer.getProperty(entry.getKey());
                if (iproperty == null) {
                    throw new JsonSyntaxException("Unknown block state property '" + entry.getKey() + "' for block '" + Block.REGISTRY.getNameForObject(block) + "'");
                }
                final String s = JsonUtils.getString(entry.getValue(), entry.getKey());
                final Optional<?> optional = iproperty.parseValue(s);
                if (!optional.isPresent()) {
                    throw new JsonSyntaxException("Invalid block state value '" + s + "' for property '" + entry.getKey() + "' on block '" + Block.REGISTRY.getNameForObject(block) + "'");
                }
                if (map == null) {
                    map = Maps.newHashMap();
                }
                map.put(iproperty, optional.get());
            }
        }
        final LocationPredicate locationpredicate = LocationPredicate.deserialize(json.get("location"));
        final ItemPredicate itempredicate = ItemPredicate.deserialize(json.get("item"));
        return new Instance(block, map, locationpredicate, itempredicate);
    }
    
    public void trigger(final EntityPlayerMP player, final BlockPos pos, final ItemStack item) {
        final IBlockState iblockstate = player.world.getBlockState(pos);
        final Listeners placedblocktrigger$listeners = this.listeners.get(player.getAdvancements());
        if (placedblocktrigger$listeners != null) {
            placedblocktrigger$listeners.trigger(iblockstate, pos, player.getServerWorld(), item);
        }
    }
    
    static {
        ID = new ResourceLocation("placed_block");
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final Block block;
        private final Map<IProperty<?>, Object> properties;
        private final LocationPredicate location;
        private final ItemPredicate item;
        
        public Instance(@Nullable final Block block, @Nullable final Map<IProperty<?>, Object> propertiesIn, final LocationPredicate locationIn, final ItemPredicate itemIn) {
            super(PlacedBlockTrigger.ID);
            this.block = block;
            this.properties = propertiesIn;
            this.location = locationIn;
            this.item = itemIn;
        }
        
        public boolean test(final IBlockState state, final BlockPos pos, final WorldServer world, final ItemStack item) {
            if (this.block != null && state.getBlock() != this.block) {
                return false;
            }
            if (this.properties != null) {
                for (final Map.Entry<IProperty<?>, Object> entry : this.properties.entrySet()) {
                    if (state.getValue(entry.getKey()) != entry.getValue()) {
                        return false;
                    }
                }
            }
            return this.location.test(world, (float)pos.getX(), (float)pos.getY(), (float)pos.getZ()) && this.item.test(item);
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
        
        public void trigger(final IBlockState state, final BlockPos pos, final WorldServer world, final ItemStack item) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.listeners) {
                if (listener.getCriterionInstance().test(state, pos, world, item)) {
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
