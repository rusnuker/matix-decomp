// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world.gen.structure;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;
import javax.annotation.Nullable;
import java.util.Iterator;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.BlockPos;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Random;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.World;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.world.gen.MapGenBase;

public abstract class MapGenStructure extends MapGenBase
{
    private MapGenStructureData structureData;
    protected Long2ObjectMap<StructureStart> structureMap;
    
    public MapGenStructure() {
        this.structureMap = (Long2ObjectMap<StructureStart>)new Long2ObjectOpenHashMap(1024);
    }
    
    public abstract String getStructureName();
    
    @Override
    protected final synchronized void recursiveGenerate(final World worldIn, final int chunkX, final int chunkZ, final int originalX, final int originalZ, final ChunkPrimer chunkPrimerIn) {
        this.initializeStructureData(worldIn);
        if (!this.structureMap.containsKey(ChunkPos.asLong(chunkX, chunkZ))) {
            this.rand.nextInt();
            try {
                if (this.canSpawnStructureAtCoords(chunkX, chunkZ)) {
                    final StructureStart structurestart = this.getStructureStart(chunkX, chunkZ);
                    this.structureMap.put(ChunkPos.asLong(chunkX, chunkZ), (Object)structurestart);
                    if (structurestart.isSizeableStructure()) {
                        this.setStructureStart(chunkX, chunkZ, structurestart);
                    }
                }
            }
            catch (final Throwable throwable) {
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception preparing structure feature");
                final CrashReportCategory crashreportcategory = crashreport.makeCategory("Feature being prepared");
                crashreportcategory.addDetail("Is feature chunk", new ICrashReportDetail<String>() {
                    @Override
                    public String call() throws Exception {
                        return MapGenStructure.this.canSpawnStructureAtCoords(chunkX, chunkZ) ? "True" : "False";
                    }
                });
                crashreportcategory.addCrashSection("Chunk location", String.format("%d,%d", chunkX, chunkZ));
                crashreportcategory.addDetail("Chunk pos hash", new ICrashReportDetail<String>() {
                    @Override
                    public String call() throws Exception {
                        return String.valueOf(ChunkPos.asLong(chunkX, chunkZ));
                    }
                });
                crashreportcategory.addDetail("Structure type", new ICrashReportDetail<String>() {
                    @Override
                    public String call() throws Exception {
                        return MapGenStructure.this.getClass().getCanonicalName();
                    }
                });
                throw new ReportedException(crashreport);
            }
        }
    }
    
    public synchronized boolean generateStructure(final World worldIn, final Random randomIn, final ChunkPos chunkCoord) {
        this.initializeStructureData(worldIn);
        final int i = (chunkCoord.x << 4) + 8;
        final int j = (chunkCoord.z << 4) + 8;
        boolean flag = false;
        for (final StructureStart structurestart : this.structureMap.values()) {
            if (structurestart.isSizeableStructure() && structurestart.isValidForPostProcess(chunkCoord) && structurestart.getBoundingBox().intersectsWith(i, j, i + 15, j + 15)) {
                structurestart.generateStructure(worldIn, randomIn, new StructureBoundingBox(i, j, i + 15, j + 15));
                structurestart.notifyPostProcessAt(chunkCoord);
                flag = true;
                this.setStructureStart(structurestart.getChunkPosX(), structurestart.getChunkPosZ(), structurestart);
            }
        }
        return flag;
    }
    
    public boolean isInsideStructure(final BlockPos pos) {
        if (this.world == null) {
            return false;
        }
        this.initializeStructureData(this.world);
        return this.getStructureAt(pos) != null;
    }
    
    @Nullable
    protected StructureStart getStructureAt(final BlockPos pos) {
        for (final StructureStart structurestart : this.structureMap.values()) {
            if (structurestart.isSizeableStructure() && structurestart.getBoundingBox().isVecInside(pos)) {
                for (final StructureComponent structurecomponent : structurestart.getComponents()) {
                    if (structurecomponent.getBoundingBox().isVecInside(pos)) {
                        return structurestart;
                    }
                }
            }
        }
        return null;
    }
    
    public boolean isPositionInStructure(final World worldIn, final BlockPos pos) {
        this.initializeStructureData(worldIn);
        for (final StructureStart structurestart : this.structureMap.values()) {
            if (structurestart.isSizeableStructure() && structurestart.getBoundingBox().isVecInside(pos)) {
                return true;
            }
        }
        return false;
    }
    
    @Nullable
    public abstract BlockPos getNearestStructurePos(final World p0, final BlockPos p1, final boolean p2);
    
    protected void initializeStructureData(final World worldIn) {
        if (this.structureData == null && worldIn != null) {
            this.structureData = (MapGenStructureData)worldIn.loadData(MapGenStructureData.class, this.getStructureName());
            if (this.structureData == null) {
                this.structureData = new MapGenStructureData(this.getStructureName());
                worldIn.setData(this.getStructureName(), this.structureData);
            }
            else {
                final NBTTagCompound nbttagcompound = this.structureData.getTagCompound();
                for (final String s : nbttagcompound.getKeySet()) {
                    final NBTBase nbtbase = nbttagcompound.getTag(s);
                    if (nbtbase.getId() == 10) {
                        final NBTTagCompound nbttagcompound2 = (NBTTagCompound)nbtbase;
                        if (!nbttagcompound2.hasKey("ChunkX") || !nbttagcompound2.hasKey("ChunkZ")) {
                            continue;
                        }
                        final int i = nbttagcompound2.getInteger("ChunkX");
                        final int j = nbttagcompound2.getInteger("ChunkZ");
                        final StructureStart structurestart = MapGenStructureIO.getStructureStart(nbttagcompound2, worldIn);
                        if (structurestart == null) {
                            continue;
                        }
                        this.structureMap.put(ChunkPos.asLong(i, j), (Object)structurestart);
                    }
                }
            }
        }
    }
    
    private void setStructureStart(final int chunkX, final int chunkZ, final StructureStart start) {
        this.structureData.writeInstance(start.writeStructureComponentsToNBT(chunkX, chunkZ), chunkX, chunkZ);
        this.structureData.markDirty();
    }
    
    protected abstract boolean canSpawnStructureAtCoords(final int p0, final int p1);
    
    protected abstract StructureStart getStructureStart(final int p0, final int p1);
    
    protected static BlockPos findNearestStructurePosBySpacing(final World worldIn, final MapGenStructure p_191069_1_, final BlockPos p_191069_2_, final int p_191069_3_, final int p_191069_4_, final int p_191069_5_, final boolean p_191069_6_, final int p_191069_7_, final boolean findUnexplored) {
        final int i = p_191069_2_.getX() >> 4;
        final int j = p_191069_2_.getZ() >> 4;
        int k = 0;
        final Random random = new Random();
        while (k <= p_191069_7_) {
            for (int l = -k; l <= k; ++l) {
                final boolean flag = l == -k || l == k;
                for (int i2 = -k; i2 <= k; ++i2) {
                    final boolean flag2 = i2 == -k || i2 == k;
                    if (flag || flag2) {
                        int j2 = i + p_191069_3_ * l;
                        int k2 = j + p_191069_3_ * i2;
                        if (j2 < 0) {
                            j2 -= p_191069_3_ - 1;
                        }
                        if (k2 < 0) {
                            k2 -= p_191069_3_ - 1;
                        }
                        int l2 = j2 / p_191069_3_;
                        int i3 = k2 / p_191069_3_;
                        final Random random2 = worldIn.setRandomSeed(l2, i3, p_191069_5_);
                        l2 *= p_191069_3_;
                        i3 *= p_191069_3_;
                        if (p_191069_6_) {
                            l2 += (random2.nextInt(p_191069_3_ - p_191069_4_) + random2.nextInt(p_191069_3_ - p_191069_4_)) / 2;
                            i3 += (random2.nextInt(p_191069_3_ - p_191069_4_) + random2.nextInt(p_191069_3_ - p_191069_4_)) / 2;
                        }
                        else {
                            l2 += random2.nextInt(p_191069_3_ - p_191069_4_);
                            i3 += random2.nextInt(p_191069_3_ - p_191069_4_);
                        }
                        MapGenBase.setupChunkSeed(worldIn.getSeed(), random, l2, i3);
                        random.nextInt();
                        if (p_191069_1_.canSpawnStructureAtCoords(l2, i3)) {
                            if (!findUnexplored || !worldIn.isChunkGeneratedAt(l2, i3)) {
                                return new BlockPos((l2 << 4) + 8, 64, (i3 << 4) + 8);
                            }
                        }
                        else if (k == 0) {
                            break;
                        }
                    }
                }
                if (k == 0) {
                    break;
                }
            }
            ++k;
        }
        return null;
    }
}
