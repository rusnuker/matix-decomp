// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.src;

public class CacheLocal
{
    private int maxX;
    private int maxY;
    private int maxZ;
    private int offsetX;
    private int offsetY;
    private int offsetZ;
    private int[][][] cache;
    private int[] lastZs;
    private int lastDz;
    
    public CacheLocal(final int p_i16_1_, final int p_i16_2_, final int p_i16_3_) {
        this.maxX = 18;
        this.maxY = 128;
        this.maxZ = 18;
        this.offsetX = 0;
        this.offsetY = 0;
        this.offsetZ = 0;
        this.cache = null;
        this.lastZs = null;
        this.lastDz = 0;
        this.maxX = p_i16_1_;
        this.maxY = p_i16_2_;
        this.maxZ = p_i16_3_;
        this.cache = new int[p_i16_1_][p_i16_2_][p_i16_3_];
        this.resetCache();
    }
    
    public void resetCache() {
        for (int i = 0; i < this.maxX; ++i) {
            final int[][] aint = this.cache[i];
            for (int j = 0; j < this.maxY; ++j) {
                final int[] aint2 = aint[j];
                for (int k = 0; k < this.maxZ; ++k) {
                    aint2[k] = -1;
                }
            }
        }
    }
    
    public void setOffset(final int p_setOffset_1_, final int p_setOffset_2_, final int p_setOffset_3_) {
        this.offsetX = p_setOffset_1_;
        this.offsetY = p_setOffset_2_;
        this.offsetZ = p_setOffset_3_;
        this.resetCache();
    }
    
    public int get(final int p_get_1_, final int p_get_2_, final int p_get_3_) {
        try {
            this.lastZs = this.cache[p_get_1_ - this.offsetX][p_get_2_ - this.offsetY];
            this.lastDz = p_get_3_ - this.offsetZ;
            return this.lastZs[this.lastDz];
        }
        catch (final ArrayIndexOutOfBoundsException arrayindexoutofboundsexception) {
            arrayindexoutofboundsexception.printStackTrace();
            return -1;
        }
    }
    
    public void setLast(final int p_setLast_1_) {
        try {
            this.lastZs[this.lastDz] = p_setLast_1_;
        }
        catch (final Exception exception) {
            exception.printStackTrace();
        }
    }
}
