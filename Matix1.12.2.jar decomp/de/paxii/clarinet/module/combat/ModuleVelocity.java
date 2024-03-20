// 
// Decompiled by Procyon v0.6.0
// 

package de.paxii.clarinet.module.combat;

import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.event.events.entity.EntityVelocityEvent;
import de.paxii.clarinet.util.module.settings.ValueBase;
import de.paxii.clarinet.module.ModuleCategory;
import de.paxii.clarinet.module.Module;

public class ModuleVelocity extends Module
{
    public ModuleVelocity() {
        super("Velocity", ModuleCategory.COMBAT, -1);
        this.setRegistered(true);
        this.setDescription("Changes the amount of knockback you take.");
        this.getModuleValues().put("velocityValue", new ValueBase("Velocity (%)", 50.0f, 0.0f, 100.0f, true, "Knockback (%)"));
    }
    
    @EventHandler
    public void onVelocity(final EntityVelocityEvent event) {
        final int playerID = Wrapper.getPlayer().getEntityId();
        if (event.getEntityID() == playerID) {
            final float reduction = this.calculateReduction();
            event.getVelocityPacket().setMotionX((int)(event.getVelocityPacket().getMotionX() * reduction));
            event.getVelocityPacket().setMotionY((int)(event.getVelocityPacket().getMotionY() * reduction));
            event.getVelocityPacket().setMotionZ((int)(event.getVelocityPacket().getMotionZ() * reduction));
        }
    }
    
    private float calculateReduction() {
        return this.getModuleValues().get("velocityValue").getValue() / 100.0f;
    }
}
