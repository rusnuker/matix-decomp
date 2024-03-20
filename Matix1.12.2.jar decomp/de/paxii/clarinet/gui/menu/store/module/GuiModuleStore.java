// 
// Decompiled by Procyon v0.6.0
// 

package de.paxii.clarinet.gui.menu.store.module;

import de.paxii.clarinet.Client;
import de.paxii.clarinet.event.EventHandler;
import de.paxii.clarinet.event.events.client.PostReloadClientEvent;
import de.paxii.clarinet.util.notifications.NotificationPriority;
import java.io.IOException;
import de.paxii.clarinet.util.module.store.ModuleStore;
import de.paxii.clarinet.util.threads.ThreadChain;
import de.paxii.clarinet.Wrapper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiModuleStore extends GuiScreen
{
    private final GuiScreen parentScreen;
    private GuiExternalModuleList externalModuleList;
    private GuiListEntryExternalModule selectedButton;
    private GuiListEntryExternalModule pressedButton;
    private GuiButton doneButton;
    private GuiButton installButton;
    private GuiButton uninstallButton;
    
    public GuiModuleStore(final GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
        Wrapper.getEventManager().register(this);
        final ThreadChain threadChain = new ThreadChain();
        threadChain.chainThread(new Thread(() -> {
            ModuleStore.fetchModules();
            threadChain.next();
        })).chainThread(new Thread(() -> this.externalModuleList = new GuiExternalModuleList(this))).kickOff();
    }
    
    @Override
    public void initGui() {
        this.externalModuleList = new GuiExternalModuleList(this);
        this.doneButton = new GuiButton(0, this.width / 2 - 50, this.height - 25, 100, 20, "Done");
        this.installButton = new GuiButton(1, this.width / 2 - 160, this.height - 25, 100, 20, "Install");
        this.uninstallButton = new GuiButton(2, this.width / 2 + 60, this.height - 25, 100, 20, "Uninstall");
        this.installButton.enabled = false;
        this.uninstallButton.enabled = false;
        this.buttonList.add(this.doneButton);
        this.buttonList.add(this.installButton);
        this.buttonList.add(this.uninstallButton);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1) {
            Wrapper.getMinecraft().displayGuiScreen(this.parentScreen);
        }
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.externalModuleList.handleMouseInput();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == this.doneButton.id) {
            Wrapper.getMinecraft().displayGuiScreen(this.parentScreen);
        }
        if (button.id == this.installButton.id && this.getPressedButton() != null) {
            final String moduleName = this.getPressedButton().getModuleEntry().getModule();
            if (ModuleStore.isModuleInstalled(moduleName) && !ModuleStore.isModuleUptoDate(moduleName)) {
                Wrapper.getClient().getNotificationManager().addNotification("Please restart Minecraft after updating Plugins!", NotificationPriority.WARNING, 5000L);
            }
            ModuleStore.downloadModule(moduleName);
        }
        if (button.id == this.uninstallButton.id && this.getPressedButton() != null) {
            ModuleStore.removeModule(this.getPressedButton().getModuleEntry().getModule());
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.externalModuleList.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.getPressedButton() != null) {
            if (this.getPressedButton().getModuleEntry().getModule().equals("Error")) {
                this.installButton.enabled = false;
                this.uninstallButton.enabled = false;
            }
            else {
                final String moduleName = this.getPressedButton().getModuleEntry().getModule();
                final boolean installed = ModuleStore.isModuleInstalled(moduleName);
                final boolean latest = ModuleStore.isModuleUptoDate(moduleName);
                if (installed && !latest) {
                    this.installButton.displayString = "Update";
                }
                else {
                    this.installButton.displayString = "Install";
                }
                this.installButton.enabled = !latest;
                this.uninstallButton.enabled = installed;
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @EventHandler
    public void onPostReload(final PostReloadClientEvent event) {
        if (this.getPressedButton() != null) {
            final boolean installed = ModuleStore.isModuleInstalled(this.getPressedButton().getModuleEntry().getModule());
            this.installButton.enabled = !installed;
            this.uninstallButton.enabled = installed;
        }
        Wrapper.getClient().getNotificationManager().addNotification("Client was reloaded!", NotificationPriority.GOOD);
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.externalModuleList.mouseReleased(mouseX, mouseY, state);
        super.mouseReleased(mouseX, mouseY, state);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        if (this.externalModuleList != null) {
            this.externalModuleList.drawScreen(mouseX, mouseY, partialTicks);
        }
        this.drawCenteredString(this.fontRenderer, Client.getClientName() + " Plugins", this.width / 2, 8, 16777215);
        this.drawString(this.fontRenderer, "Plugin may be compatible", 5, this.height - 55, 1726506775);
        this.drawString(this.fontRenderer, "Plugin is incompatible", 5, this.height - 45, 1725632271);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public void onGuiClosed() {
        Wrapper.getEventManager().unregister(this);
    }
    
    public GuiListEntryExternalModule getSelectedButton() {
        return this.selectedButton;
    }
    
    public void setSelectedButton(final GuiListEntryExternalModule selectedButton) {
        this.selectedButton = selectedButton;
    }
    
    public GuiListEntryExternalModule getPressedButton() {
        return this.pressedButton;
    }
    
    public void setPressedButton(final GuiListEntryExternalModule pressedButton) {
        this.pressedButton = pressedButton;
    }
}
