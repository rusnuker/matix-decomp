// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;

public class ScreenChatOptions extends GuiScreen
{
    private static final GameSettings.Options[] CHAT_OPTIONS;
    private final GuiScreen parentScreen;
    private final GameSettings game_settings;
    private String chatTitle;
    private GuiOptionButton narratorButton;
    
    public ScreenChatOptions(final GuiScreen parentScreenIn, final GameSettings gameSettingsIn) {
        this.parentScreen = parentScreenIn;
        this.game_settings = gameSettingsIn;
    }
    
    @Override
    public void initGui() {
        this.chatTitle = I18n.format("options.chat.title", new Object[0]);
        int i = 0;
        for (final GameSettings.Options gamesettings$options : ScreenChatOptions.CHAT_OPTIONS) {
            if (gamesettings$options.getEnumFloat()) {
                this.buttonList.add(new GuiOptionSlider(gamesettings$options.returnEnumOrdinal(), this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), gamesettings$options));
            }
            else {
                final GuiOptionButton guioptionbutton = new GuiOptionButton(gamesettings$options.returnEnumOrdinal(), this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), gamesettings$options, this.game_settings.getKeyBinding(gamesettings$options));
                this.buttonList.add(guioptionbutton);
                if (gamesettings$options == GameSettings.Options.NARRATOR) {
                    this.narratorButton = guioptionbutton;
                    guioptionbutton.enabled = NarratorChatListener.INSTANCE.isActive();
                }
            }
            ++i;
        }
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 144, I18n.format("gui.done", new Object[0])));
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1) {
            this.mc.gameSettings.saveOptions();
        }
        super.keyTyped(typedChar, keyCode);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id < 100 && button instanceof GuiOptionButton) {
                this.game_settings.setOptionValue(((GuiOptionButton)button).returnEnumOptions(), 1);
                button.displayString = this.game_settings.getKeyBinding(GameSettings.Options.getEnumOptions(button.id));
            }
            if (button.id == 200) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(this.parentScreen);
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.chatTitle, this.width / 2, 20, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public void updateNarratorButton() {
        this.narratorButton.displayString = this.game_settings.getKeyBinding(GameSettings.Options.getEnumOptions(this.narratorButton.id));
    }
    
    static {
        CHAT_OPTIONS = new GameSettings.Options[] { GameSettings.Options.CHAT_VISIBILITY, GameSettings.Options.CHAT_COLOR, GameSettings.Options.CHAT_LINKS, GameSettings.Options.CHAT_OPACITY, GameSettings.Options.CHAT_LINKS_PROMPT, GameSettings.Options.CHAT_SCALE, GameSettings.Options.CHAT_HEIGHT_FOCUSED, GameSettings.Options.CHAT_HEIGHT_UNFOCUSED, GameSettings.Options.CHAT_WIDTH, GameSettings.Options.REDUCED_DEBUG_INFO, GameSettings.Options.NARRATOR };
    }
}
