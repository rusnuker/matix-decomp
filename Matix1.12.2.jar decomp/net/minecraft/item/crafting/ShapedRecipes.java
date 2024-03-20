// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item.crafting;

import com.google.gson.JsonParseException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.Item;
import javax.annotation.Nullable;
import java.util.Iterator;
import com.google.gson.JsonElement;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.common.annotations.VisibleForTesting;
import java.util.Set;
import com.google.gson.JsonSyntaxException;
import com.google.common.collect.Sets;
import java.util.Map;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonObject;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ShapedRecipes implements IRecipe
{
    private final int recipeWidth;
    private final int recipeHeight;
    private final NonNullList<Ingredient> recipeItems;
    private final ItemStack recipeOutput;
    private final String group;
    
    public ShapedRecipes(final String group, final int width, final int height, final NonNullList<Ingredient> ingredients, final ItemStack result) {
        this.group = group;
        this.recipeWidth = width;
        this.recipeHeight = height;
        this.recipeItems = ingredients;
        this.recipeOutput = result;
    }
    
    @Override
    public String getGroup() {
        return this.group;
    }
    
    @Override
    public ItemStack getRecipeOutput() {
        return this.recipeOutput;
    }
    
    @Override
    public NonNullList<ItemStack> getRemainingItems(final InventoryCrafting inv) {
        final NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
        for (int i = 0; i < nonnulllist.size(); ++i) {
            final ItemStack itemstack = inv.getStackInSlot(i);
            if (itemstack.getItem().hasContainerItem()) {
                nonnulllist.set(i, new ItemStack(itemstack.getItem().getContainerItem()));
            }
        }
        return nonnulllist;
    }
    
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.recipeItems;
    }
    
    @Override
    public boolean canFit(final int width, final int height) {
        return width >= this.recipeWidth && height >= this.recipeHeight;
    }
    
    @Override
    public boolean matches(final InventoryCrafting inv, final World worldIn) {
        for (int i = 0; i <= 3 - this.recipeWidth; ++i) {
            for (int j = 0; j <= 3 - this.recipeHeight; ++j) {
                if (this.checkMatch(inv, i, j, true)) {
                    return true;
                }
                if (this.checkMatch(inv, i, j, false)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean checkMatch(final InventoryCrafting p_77573_1_, final int p_77573_2_, final int p_77573_3_, final boolean p_77573_4_) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                final int k = i - p_77573_2_;
                final int l = j - p_77573_3_;
                Ingredient ingredient = Ingredient.EMPTY;
                if (k >= 0 && l >= 0 && k < this.recipeWidth && l < this.recipeHeight) {
                    if (p_77573_4_) {
                        ingredient = this.recipeItems.get(this.recipeWidth - k - 1 + l * this.recipeWidth);
                    }
                    else {
                        ingredient = this.recipeItems.get(k + l * this.recipeWidth);
                    }
                }
                if (!ingredient.apply(p_77573_1_.getStackInRowAndColumn(i, j))) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public ItemStack getCraftingResult(final InventoryCrafting inv) {
        return this.getRecipeOutput().copy();
    }
    
    public int getWidth() {
        return this.recipeWidth;
    }
    
    public int getHeight() {
        return this.recipeHeight;
    }
    
    public static ShapedRecipes deserialize(final JsonObject p_193362_0_) {
        final String s = JsonUtils.getString(p_193362_0_, "group", "");
        final Map<String, Ingredient> map = deserializeKey(JsonUtils.getJsonObject(p_193362_0_, "key"));
        final String[] astring = shrink(patternFromJson(JsonUtils.getJsonArray(p_193362_0_, "pattern")));
        final int i = astring[0].length();
        final int j = astring.length;
        final NonNullList<Ingredient> nonnulllist = deserializeIngredients(astring, map, i, j);
        final ItemStack itemstack = deserializeItem(JsonUtils.getJsonObject(p_193362_0_, "result"), true);
        return new ShapedRecipes(s, i, j, nonnulllist, itemstack);
    }
    
    private static NonNullList<Ingredient> deserializeIngredients(final String[] p_192402_0_, final Map<String, Ingredient> p_192402_1_, final int p_192402_2_, final int p_192402_3_) {
        final NonNullList<Ingredient> nonnulllist = NonNullList.withSize(p_192402_2_ * p_192402_3_, Ingredient.EMPTY);
        final Set<String> set = Sets.newHashSet((Iterable)p_192402_1_.keySet());
        set.remove(" ");
        for (int i = 0; i < p_192402_0_.length; ++i) {
            for (int j = 0; j < p_192402_0_[i].length(); ++j) {
                final String s = p_192402_0_[i].substring(j, j + 1);
                final Ingredient ingredient = p_192402_1_.get(s);
                if (ingredient == null) {
                    throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
                }
                set.remove(s);
                nonnulllist.set(j + p_192402_2_ * i, ingredient);
            }
        }
        if (!set.isEmpty()) {
            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
        }
        return nonnulllist;
    }
    
    @VisibleForTesting
    static String[] shrink(final String... p_194134_0_) {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;
        for (int i2 = 0; i2 < p_194134_0_.length; ++i2) {
            final String s = p_194134_0_[i2];
            i = Math.min(i, firstNonSpace(s));
            final int j2 = lastNonSpace(s);
            j = Math.max(j, j2);
            if (j2 < 0) {
                if (k == i2) {
                    ++k;
                }
                ++l;
            }
            else {
                l = 0;
            }
        }
        if (p_194134_0_.length == l) {
            return new String[0];
        }
        final String[] astring = new String[p_194134_0_.length - l - k];
        for (int k2 = 0; k2 < astring.length; ++k2) {
            astring[k2] = p_194134_0_[k2 + k].substring(i, j + 1);
        }
        return astring;
    }
    
    private static int firstNonSpace(final String str) {
        int i;
        for (i = 0; i < str.length() && str.charAt(i) == ' '; ++i) {}
        return i;
    }
    
    private static int lastNonSpace(final String str) {
        int i;
        for (i = str.length() - 1; i >= 0 && str.charAt(i) == ' '; --i) {}
        return i;
    }
    
    private static String[] patternFromJson(final JsonArray p_192407_0_) {
        final String[] astring = new String[p_192407_0_.size()];
        if (astring.length > 3) {
            throw new JsonSyntaxException("Invalid pattern: too many rows, 3 is maximum");
        }
        if (astring.length == 0) {
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        }
        for (int i = 0; i < astring.length; ++i) {
            final String s = JsonUtils.getString(p_192407_0_.get(i), "pattern[" + i + "]");
            if (s.length() > 3) {
                throw new JsonSyntaxException("Invalid pattern: too many columns, 3 is maximum");
            }
            if (i > 0 && astring[0].length() != s.length()) {
                throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
            }
            astring[i] = s;
        }
        return astring;
    }
    
    private static Map<String, Ingredient> deserializeKey(final JsonObject p_192408_0_) {
        final Map<String, Ingredient> map = Maps.newHashMap();
        for (final Map.Entry<String, JsonElement> entry : p_192408_0_.entrySet()) {
            if (entry.getKey().length() != 1) {
                throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            }
            if (" ".equals(entry.getKey())) {
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            }
            map.put(entry.getKey(), deserializeIngredient(entry.getValue()));
        }
        map.put(" ", Ingredient.EMPTY);
        return map;
    }
    
    public static Ingredient deserializeIngredient(@Nullable final JsonElement p_193361_0_) {
        if (p_193361_0_ == null || p_193361_0_.isJsonNull()) {
            throw new JsonSyntaxException("Item cannot be null");
        }
        if (p_193361_0_.isJsonObject()) {
            return Ingredient.fromStacks(deserializeItem(p_193361_0_.getAsJsonObject(), false));
        }
        if (!p_193361_0_.isJsonArray()) {
            throw new JsonSyntaxException("Expected item to be object or array of objects");
        }
        final JsonArray jsonarray = p_193361_0_.getAsJsonArray();
        if (jsonarray.size() == 0) {
            throw new JsonSyntaxException("Item array cannot be empty, at least one item must be defined");
        }
        final ItemStack[] aitemstack = new ItemStack[jsonarray.size()];
        for (int i = 0; i < jsonarray.size(); ++i) {
            aitemstack[i] = deserializeItem(JsonUtils.getJsonObject(jsonarray.get(i), "item"), false);
        }
        return Ingredient.fromStacks(aitemstack);
    }
    
    public static ItemStack deserializeItem(final JsonObject p_192405_0_, final boolean useCount) {
        final String s = JsonUtils.getString(p_192405_0_, "item");
        final Item item = Item.REGISTRY.getObject(new ResourceLocation(s));
        if (item == null) {
            throw new JsonSyntaxException("Unknown item '" + s + "'");
        }
        if (item.getHasSubtypes() && !p_192405_0_.has("data")) {
            throw new JsonParseException("Missing data for item '" + s + "'");
        }
        final int i = JsonUtils.getInt(p_192405_0_, "data", 0);
        final int j = useCount ? JsonUtils.getInt(p_192405_0_, "count", 1) : 1;
        return new ItemStack(item, j, i);
    }
}
