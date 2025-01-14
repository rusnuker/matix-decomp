// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.util;

import com.google.common.collect.Maps;
import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import com.mojang.util.UUIDTypeAdapter;
import com.mojang.authlib.GameProfile;

public class Session
{
    private final String username;
    private final String playerID;
    private final String token;
    private final Type sessionType;
    
    public Session(final String usernameIn, final String playerIDIn, final String tokenIn, final String sessionTypeIn) {
        this.username = usernameIn;
        this.playerID = playerIDIn;
        this.token = tokenIn;
        this.sessionType = Type.setSessionType(sessionTypeIn);
    }
    
    public String getSessionID() {
        return "token:" + this.token + ":" + this.playerID;
    }
    
    public String getPlayerID() {
        return this.playerID;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public String getToken() {
        return this.token;
    }
    
    public GameProfile getProfile() {
        try {
            final UUID uuid = UUIDTypeAdapter.fromString(this.getPlayerID());
            return new GameProfile(uuid, this.getUsername());
        }
        catch (final IllegalArgumentException var2) {
            return new GameProfile((UUID)null, this.getUsername());
        }
    }
    
    public enum Type
    {
        LEGACY("legacy"), 
        MOJANG("mojang");
        
        private static final Map<String, Type> SESSION_TYPES;
        private final String sessionType;
        
        private Type(final String sessionTypeIn) {
            this.sessionType = sessionTypeIn;
        }
        
        @Nullable
        public static Type setSessionType(final String sessionTypeIn) {
            return Type.SESSION_TYPES.get(sessionTypeIn.toLowerCase(Locale.ROOT));
        }
        
        static {
            SESSION_TYPES = Maps.newHashMap();
            for (final Type session$type : values()) {
                Type.SESSION_TYPES.put(session$type.sessionType, session$type);
            }
        }
    }
}
