// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import com.google.gson.JsonParseException;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.StringUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class BookPagesStrictJSON implements IFixableData
{
    @Override
    public int getFixVersion() {
        return 165;
    }
    
    @Override
    public NBTTagCompound fixTagCompound(final NBTTagCompound compound) {
        if ("minecraft:written_book".equals(compound.getString("id"))) {
            final NBTTagCompound nbttagcompound = compound.getCompoundTag("tag");
            if (nbttagcompound.hasKey("pages", 9)) {
                final NBTTagList nbttaglist = nbttagcompound.getTagList("pages", 8);
                for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                    final String s = nbttaglist.getStringTagAt(i);
                    ITextComponent itextcomponent = null;
                    Label_0253: {
                        if (!"null".equals(s) && !StringUtils.isNullOrEmpty(s)) {
                            if (s.charAt(0) != '\"' || s.charAt(s.length() - 1) != '\"') {
                                if (s.charAt(0) != '{' || s.charAt(s.length() - 1) != '}') {
                                    itextcomponent = new TextComponentString(s);
                                    break Label_0253;
                                }
                            }
                            try {
                                itextcomponent = JsonUtils.gsonDeserialize(SignStrictJSON.GSON_INSTANCE, s, ITextComponent.class, true);
                                if (itextcomponent == null) {
                                    itextcomponent = new TextComponentString("");
                                }
                            }
                            catch (final JsonParseException ex) {}
                            if (itextcomponent == null) {
                                try {
                                    itextcomponent = ITextComponent.Serializer.jsonToComponent(s);
                                }
                                catch (final JsonParseException ex2) {}
                            }
                            if (itextcomponent == null) {
                                try {
                                    itextcomponent = ITextComponent.Serializer.fromJsonLenient(s);
                                }
                                catch (final JsonParseException ex3) {}
                            }
                            if (itextcomponent == null) {
                                itextcomponent = new TextComponentString(s);
                            }
                        }
                        else {
                            itextcomponent = new TextComponentString("");
                        }
                    }
                    nbttaglist.set(i, new NBTTagString(ITextComponent.Serializer.componentToJson(itextcomponent)));
                }
                nbttagcompound.setTag("pages", nbttaglist);
            }
        }
        return compound;
    }
}
