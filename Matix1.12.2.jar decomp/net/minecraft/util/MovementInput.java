// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.util;

import net.minecraft.util.math.Vec2f;

public class MovementInput
{
    public float moveStrafe;
    public float moveForward;
    public boolean forwardKeyDown;
    public boolean backKeyDown;
    public boolean leftKeyDown;
    public boolean rightKeyDown;
    public boolean jump;
    public boolean sneak;
    
    public void updatePlayerMoveState() {
    }
    
    public Vec2f getMoveVector() {
        return new Vec2f(this.moveStrafe, this.moveForward);
    }
}
