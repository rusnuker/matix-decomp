// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.block;

import java.util.Collection;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import net.minecraft.util.IStringSerializable;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.BlockStateContainer;
import javax.annotation.Nullable;
import com.google.common.base.Predicate;
import net.minecraft.block.properties.IProperty;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.PropertyEnum;

public abstract class BlockFlower extends BlockBush
{
    protected PropertyEnum<EnumFlowerType> type;
    
    protected BlockFlower() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(this.getTypeProperty(), (this.getBlockType() == EnumFlowerColor.RED) ? EnumFlowerType.POPPY : EnumFlowerType.DANDELION));
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        return super.getBoundingBox(state, source, pos).offset(state.getOffset(source, pos));
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return state.getValue(this.getTypeProperty()).getMeta();
    }
    
    @Override
    public void getSubBlocks(final CreativeTabs itemIn, final NonNullList<ItemStack> items) {
        for (final EnumFlowerType blockflower$enumflowertype : EnumFlowerType.getTypes(this.getBlockType())) {
            items.add(new ItemStack(this, 1, blockflower$enumflowertype.getMeta()));
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(this.getTypeProperty(), EnumFlowerType.getType(this.getBlockType(), meta));
    }
    
    public abstract EnumFlowerColor getBlockType();
    
    public IProperty<EnumFlowerType> getTypeProperty() {
        if (this.type == null) {
            this.type = PropertyEnum.create("type", EnumFlowerType.class, (com.google.common.base.Predicate<EnumFlowerType>)new Predicate<EnumFlowerType>() {
                public boolean apply(@Nullable final EnumFlowerType p_apply_1_) {
                    return p_apply_1_.getBlockType() == BlockFlower.this.getBlockType();
                }
            });
        }
        return this.type;
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(this.getTypeProperty()).getMeta();
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { this.getTypeProperty() });
    }
    
    @Override
    public EnumOffsetType getOffsetType() {
        return EnumOffsetType.XZ;
    }
    
    public enum EnumFlowerColor
    {
        YELLOW, 
        RED;
        
        public BlockFlower getBlock() {
            return (this == EnumFlowerColor.YELLOW) ? Blocks.YELLOW_FLOWER : Blocks.RED_FLOWER;
        }
    }
    
    public enum EnumFlowerType implements IStringSerializable
    {
        DANDELION(EnumFlowerColor.YELLOW, 0, "dandelion"), 
        POPPY(EnumFlowerColor.RED, 0, "poppy"), 
        BLUE_ORCHID(EnumFlowerColor.RED, 1, "blue_orchid", "blueOrchid"), 
        ALLIUM(EnumFlowerColor.RED, 2, "allium"), 
        HOUSTONIA(EnumFlowerColor.RED, 3, "houstonia"), 
        RED_TULIP(EnumFlowerColor.RED, 4, "red_tulip", "tulipRed"), 
        ORANGE_TULIP(EnumFlowerColor.RED, 5, "orange_tulip", "tulipOrange"), 
        WHITE_TULIP(EnumFlowerColor.RED, 6, "white_tulip", "tulipWhite"), 
        PINK_TULIP(EnumFlowerColor.RED, 7, "pink_tulip", "tulipPink"), 
        OXEYE_DAISY(EnumFlowerColor.RED, 8, "oxeye_daisy", "oxeyeDaisy");
        
        private static final EnumFlowerType[][] TYPES_FOR_BLOCK;
        private final EnumFlowerColor blockType;
        private final int meta;
        private final String name;
        private final String unlocalizedName;
        
        private EnumFlowerType(final EnumFlowerColor blockType, final int meta, final String name) {
            this(blockType, meta, name, name);
        }
        
        private EnumFlowerType(final EnumFlowerColor blockType, final int meta, final String name, final String unlocalizedName) {
            this.blockType = blockType;
            this.meta = meta;
            this.name = name;
            this.unlocalizedName = unlocalizedName;
        }
        
        public EnumFlowerColor getBlockType() {
            return this.blockType;
        }
        
        public int getMeta() {
            return this.meta;
        }
        
        public static EnumFlowerType getType(final EnumFlowerColor blockType, int meta) {
            final EnumFlowerType[] ablockflower$enumflowertype = EnumFlowerType.TYPES_FOR_BLOCK[blockType.ordinal()];
            if (meta < 0 || meta >= ablockflower$enumflowertype.length) {
                meta = 0;
            }
            return ablockflower$enumflowertype[meta];
        }
        
        public static EnumFlowerType[] getTypes(final EnumFlowerColor flowerColor) {
            return EnumFlowerType.TYPES_FOR_BLOCK[flowerColor.ordinal()];
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        @Override
        public String getName() {
            return this.name;
        }
        
        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }
        
        static {
            TYPES_FOR_BLOCK = new EnumFlowerType[EnumFlowerColor.values().length][];
            for (final EnumFlowerColor blockflower$enumflowercolor : EnumFlowerColor.values()) {
                final Collection<EnumFlowerType> collection = Collections2.filter((Collection)Lists.newArrayList((Object[])values()), (Predicate)new Predicate<EnumFlowerType>() {
                    public boolean apply(@Nullable final EnumFlowerType p_apply_1_) {
                        return p_apply_1_.getBlockType() == blockflower$enumflowercolor;
                    }
                });
                EnumFlowerType.TYPES_FOR_BLOCK[blockflower$enumflowercolor.ordinal()] = collection.toArray(new EnumFlowerType[collection.size()]);
            }
        }
    }
}
