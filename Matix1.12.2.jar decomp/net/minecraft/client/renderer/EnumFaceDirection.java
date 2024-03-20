// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer;

import net.minecraft.util.EnumFacing;

public enum EnumFaceDirection
{
    DOWN(new VertexInformation[] { new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX), new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX) }), 
    UP(new VertexInformation[] { new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX), new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX), new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX), new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX) }), 
    NORTH(new VertexInformation[] { new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX), new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX), new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX) }), 
    SOUTH(new VertexInformation[] { new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX), new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX), new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX) }), 
    WEST(new VertexInformation[] { new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX), new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX), new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX), new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX) }), 
    EAST(new VertexInformation[] { new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX), new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX) });
    
    private static final EnumFaceDirection[] FACINGS;
    private final VertexInformation[] vertexInfos;
    
    public static EnumFaceDirection getFacing(final EnumFacing facing) {
        return EnumFaceDirection.FACINGS[facing.getIndex()];
    }
    
    private EnumFaceDirection(final VertexInformation[] vertexInfosIn) {
        this.vertexInfos = vertexInfosIn;
    }
    
    public VertexInformation getVertexInformation(final int index) {
        return this.vertexInfos[index];
    }
    
    static {
        (FACINGS = new EnumFaceDirection[6])[Constants.DOWN_INDEX] = EnumFaceDirection.DOWN;
        EnumFaceDirection.FACINGS[Constants.UP_INDEX] = EnumFaceDirection.UP;
        EnumFaceDirection.FACINGS[Constants.NORTH_INDEX] = EnumFaceDirection.NORTH;
        EnumFaceDirection.FACINGS[Constants.SOUTH_INDEX] = EnumFaceDirection.SOUTH;
        EnumFaceDirection.FACINGS[Constants.WEST_INDEX] = EnumFaceDirection.WEST;
        EnumFaceDirection.FACINGS[Constants.EAST_INDEX] = EnumFaceDirection.EAST;
    }
    
    public static final class Constants
    {
        public static final int SOUTH_INDEX;
        public static final int UP_INDEX;
        public static final int EAST_INDEX;
        public static final int NORTH_INDEX;
        public static final int DOWN_INDEX;
        public static final int WEST_INDEX;
        
        static {
            SOUTH_INDEX = EnumFacing.SOUTH.getIndex();
            UP_INDEX = EnumFacing.UP.getIndex();
            EAST_INDEX = EnumFacing.EAST.getIndex();
            NORTH_INDEX = EnumFacing.NORTH.getIndex();
            DOWN_INDEX = EnumFacing.DOWN.getIndex();
            WEST_INDEX = EnumFacing.WEST.getIndex();
        }
    }
    
    public static class VertexInformation
    {
        public final int xIndex;
        public final int yIndex;
        public final int zIndex;
        
        private VertexInformation(final int xIndexIn, final int yIndexIn, final int zIndexIn) {
            this.xIndex = xIndexIn;
            this.yIndex = yIndexIn;
            this.zIndex = zIndexIn;
        }
    }
}
