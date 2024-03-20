// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.util.text;

public enum ChatType
{
    CHAT((byte)0), 
    SYSTEM((byte)1), 
    GAME_INFO((byte)2);
    
    private final byte id;
    
    private ChatType(final byte id) {
        this.id = id;
    }
    
    public byte getId() {
        return this.id;
    }
    
    public static ChatType byId(final byte p_192582_0_) {
        for (final ChatType chattype : values()) {
            if (p_192582_0_ == chattype.id) {
                return chattype;
            }
        }
        return ChatType.CHAT;
    }
}
