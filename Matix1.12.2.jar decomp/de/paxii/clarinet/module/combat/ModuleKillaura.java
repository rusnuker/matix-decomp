// 
// Decompiled by Procyon v0.6.0
// 

package de.paxii.clarinet.module.combat;

import de.paxii.clarinet.util.settings.ClientSettings;
import de.paxii.clarinet.util.chat.Chat;
import de.paxii.clarinet.event.events.player.PostMotionUpdateEvent;
import de.paxii.clarinet.event.EventHandler;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import de.paxii.clarinet.event.events.player.PreMotionUpdateEvent;
import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.module.ModuleCategory;
import net.minecraft.entity.EntityLivingBase;
import de.paxii.clarinet.util.module.settings.ValueBase;
import de.paxii.clarinet.util.module.killaura.TimeManager;
import de.paxii.clarinet.util.module.killaura.EntityManager;
import de.paxii.clarinet.util.module.killaura.AuraManager;
import de.paxii.clarinet.module.Module;

public class ModuleKillaura extends Module
{
    private final AuraManager auraManager;
    private final EntityManager entityManager;
    private final TimeManager timeManager;
    private final ValueBase valueRandomness;
    private EntityLivingBase target;
    
    public ModuleKillaura() {
        super("KillAura", ModuleCategory.COMBAT, 33);
        this.setCommand(true);
        this.setRegistered(true);
        this.setSyntax("killaura set <speed/range/animals/mobs/players/silent/legit/autospeed> <digit/true/false>");
        this.setDescription("Automatically hits entities around you.");
        this.auraManager = new AuraManager(this);
        (this.timeManager = new TimeManager()).setRandom(true);
        this.entityManager = new EntityManager(this.auraManager, Wrapper.getFriendManager());
        this.valueRandomness = new ValueBase(String.format("%s Random", this.getName()), 50.0f, 1.0f, 250.0f, true, "Randomness");
        this.getModuleValues().put("randomness", this.valueRandomness);
    }
    
    @EventHandler
    public void preMotion(final PreMotionUpdateEvent event) {
        if (this.auraManager.isSilent()) {
            this.auraManager.saveCamera(Wrapper.getPlayer());
        }
        this.timeManager.updateTimer((int)this.valueRandomness.getValue());
        this.target = this.entityManager.getEntity(this.auraManager.getAngle(), this.auraManager.getRange());
        if (this.target == null) {
            return;
        }
        this.auraManager.setAngles(this.target, this.entityManager);
        if (this.auraManager.isLegit() && !this.auraManager.isSilent() && (Wrapper.getMinecraft().pointedEntity == null || Wrapper.getMinecraft().pointedEntity.getEntityId() != this.target.getEntityId())) {
            return;
        }
        if (this.auraManager.isDelayComplete(this.timeManager)) {
            Wrapper.getMinecraft().playerController.attackEntity(Wrapper.getPlayer(), this.target);
            Wrapper.getPlayer().swingArm(EnumHand.MAIN_HAND);
            this.auraManager.addToAttackMap(this.target.getEntityId(), this.timeManager.getLast());
            this.timeManager.updateLast();
        }
    }
    
    @EventHandler
    public void postMotion(final PostMotionUpdateEvent event) {
        if (this.auraManager.isSilent()) {
            this.auraManager.restoreCamera(Wrapper.getPlayer());
        }
    }
    
    @Override
    public void onCommand(final String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("set")) {
                if (args.length >= 3) {
                    final String identifier = args[1];
                    final String value = args[2];
                    if (identifier.equalsIgnoreCase("speed")) {
                        try {
                            final float speed = Float.parseFloat(value);
                            this.auraManager.setDelay(speed);
                            Chat.printClientMessage("KillAura speed has been set to " + speed + ".");
                        }
                        catch (final Exception e) {
                            Chat.printClientMessage("Invalid argument!");
                        }
                    }
                    else if (identifier.equalsIgnoreCase("range")) {
                        try {
                            final float range = Float.parseFloat(value);
                            this.auraManager.setDelay(range);
                            Chat.printClientMessage("KillAura range has been set to " + range + ".");
                        }
                        catch (final Exception e) {
                            Chat.printClientMessage("Invalid argument!");
                        }
                    }
                    else if (identifier.equalsIgnoreCase("animals")) {
                        try {
                            final boolean animal = Boolean.parseBoolean(value);
                            this.auraManager.setAnimal(animal);
                            Chat.printClientMessage("KillAura animal mode has been set to " + animal + ".");
                        }
                        catch (final Exception e) {
                            Chat.printClientMessage("Invalid argument!");
                        }
                    }
                    else if (identifier.equalsIgnoreCase("mobs")) {
                        try {
                            final boolean mob = Boolean.parseBoolean(value);
                            this.auraManager.setMob(mob);
                            Chat.printClientMessage("KillAura mob mode has been set to " + mob + ".");
                        }
                        catch (final Exception e) {
                            Chat.printClientMessage("Invalid argument!");
                        }
                    }
                    else if (identifier.equalsIgnoreCase("players")) {
                        try {
                            final boolean player = Boolean.parseBoolean(value);
                            this.auraManager.setPlayer(player);
                            Chat.printClientMessage("KillAura player mode has been set to " + player + ".");
                        }
                        catch (final Exception e) {
                            Chat.printClientMessage("Invalid argument!");
                        }
                    }
                    else if (identifier.equalsIgnoreCase("silent")) {
                        try {
                            final boolean silent = Boolean.parseBoolean(value);
                            this.auraManager.setSilent(silent);
                            Chat.printClientMessage("KillAura silent mode has been set to " + silent + ".");
                        }
                        catch (final Exception e) {
                            Chat.printClientMessage("Invalid argument!");
                        }
                    }
                    else if (identifier.equalsIgnoreCase("legit")) {
                        try {
                            final boolean legit = Boolean.parseBoolean(value);
                            this.auraManager.setLegit(legit);
                            Chat.printClientMessage("KillAura legit mode has been set to " + legit + ".");
                        }
                        catch (final Exception e) {
                            Chat.printClientMessage("Invalid argument!");
                        }
                    }
                    else if (identifier.equalsIgnoreCase("invisible")) {
                        try {
                            final boolean invisible = Boolean.parseBoolean(value);
                            this.auraManager.setInvisible(invisible);
                            Chat.printClientMessage("KillAura invisibility mode has been set to " + invisible + ".");
                        }
                        catch (final Exception e) {
                            Chat.printClientMessage("Invalid argument!");
                        }
                    }
                    else if (identifier.equalsIgnoreCase("autospeed")) {
                        try {
                            final boolean autoSpeed = Boolean.parseBoolean(value);
                            this.auraManager.setAutoSpeed(autoSpeed);
                            Chat.printClientMessage("KillAura auto speed mode has been set to " + autoSpeed + ".");
                        }
                        catch (final Exception e) {
                            Chat.printClientMessage("Invalid argument!");
                        }
                    }
                    else {
                        Chat.printClientMessage("Invalid subcommand! Use \"" + ClientSettings.getValue("client.prefix", String.class) + "help killaura\"");
                    }
                }
                else {
                    Chat.printClientMessage("Too few arguments!");
                }
            }
            else {
                Chat.printClientMessage("Invalid subcommand! Use \"" + ClientSettings.getValue("client.prefix", String.class) + "help killaura\"");
            }
        }
        else {
            Chat.printClientMessage("Too few arguments!");
        }
    }
}