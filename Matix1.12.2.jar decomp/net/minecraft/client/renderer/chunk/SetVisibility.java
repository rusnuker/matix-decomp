// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.chunk;

import java.util.Iterator;
import net.minecraft.util.EnumFacing;
import java.util.Set;

public class SetVisibility
{
    private static final int COUNT_FACES;
    private long bits;
    
    public void setManyVisible(final Set<EnumFacing> facing) {
        for (final EnumFacing enumfacing : facing) {
            for (final EnumFacing enumfacing2 : facing) {
                this.setVisible(enumfacing, enumfacing2, true);
            }
        }
    }
    
    public void setVisible(final EnumFacing facing, final EnumFacing facing2, final boolean p_178619_3_) {
        this.setBit(facing.ordinal() + facing2.ordinal() * SetVisibility.COUNT_FACES, p_178619_3_);
        this.setBit(facing2.ordinal() + facing.ordinal() * SetVisibility.COUNT_FACES, p_178619_3_);
    }
    
    public void setAllVisible(final boolean visible) {
        if (visible) {
            this.bits = -1L;
        }
        else {
            this.bits = 0L;
        }
    }
    
    public boolean isVisible(final EnumFacing facing, final EnumFacing facing2) {
        return this.getBit(facing.ordinal() + facing2.ordinal() * SetVisibility.COUNT_FACES);
    }
    
    @Override
    public String toString() {
        final StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append(' ');
        for (final EnumFacing enumfacing : EnumFacing.values()) {
            stringbuilder.append(' ').append(enumfacing.toString().toUpperCase().charAt(0));
        }
        stringbuilder.append('\n');
        for (final EnumFacing enumfacing2 : EnumFacing.values()) {
            stringbuilder.append(enumfacing2.toString().toUpperCase().charAt(0));
            for (final EnumFacing enumfacing3 : EnumFacing.values()) {
                if (enumfacing2 == enumfacing3) {
                    stringbuilder.append("  ");
                }
                else {
                    final boolean flag = this.isVisible(enumfacing2, enumfacing3);
                    stringbuilder.append(' ').append(flag ? 'Y' : 'n');
                }
            }
            stringbuilder.append('\n');
        }
        return stringbuilder.toString();
    }
    
    private boolean getBit(final int p_getBit_1_) {
        return (this.bits & (long)(1 << p_getBit_1_)) != 0x0L;
    }
    
    private void setBit(final int p_setBit_1_, final boolean p_setBit_2_) {
        if (p_setBit_2_) {
            this.setBit(p_setBit_1_);
        }
        else {
            this.clearBit(p_setBit_1_);
        }
    }
    
    private void setBit(final int p_setBit_1_) {
        this.bits |= 1 << p_setBit_1_;
    }
    
    private void clearBit(final int p_clearBit_1_) {
        this.bits &= ~(1 << p_clearBit_1_);
    }
    
    static {
        COUNT_FACES = EnumFacing.values().length;
    }
}
