package com.byteoverlay.config;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import java.io.IOException;

public class ByteGui extends GuiScreen {
    private final String[] colors = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    private GuiTextField sniperInput;

    @Override
    public void initGui() {
        int centerX = this.width / 2;
        int y = 30;

        this.buttonList.clear();

        // --- SECTION: OVERLAY STATS ---
        y += 15;
        this.buttonList.add(new GuiButton(1, centerX - 105, y, 100, 20, "Overlay: " + (ByteConfig.enabled ? "§aON" : "§cOFF")));
        this.buttonList.add(new GuiButton(6, centerX + 5, y, 100, 20, "FKDR: " + (ByteConfig.showFKDR ? "§aON" : "§cOFF")));
        y += 22;
        this.buttonList.add(new GuiButton(7, centerX - 105, y, 100, 20, "WS: " + (ByteConfig.showWS ? "§aON" : "§cOFF")));
        this.buttonList.add(new GuiButton(8, centerX + 5, y, 100, 20, "WR: " + (ByteConfig.showWR ? "§aON" : "§cOFF")));
        y += 22;
        this.buttonList.add(new GuiButton(16, centerX - 105, y, 100, 20, "Wins: " + (ByteConfig.showWins ? "§aON" : "§cOFF")));
        this.buttonList.add(new GuiButton(17, centerX + 5, y, 100, 20, "Losses: " + (ByteConfig.showLosses ? "§aON" : "§cOFF")));
        
        y += 30;
        
        // --- SECTION: NAMETAGS ---
        y += 15;
        this.buttonList.add(new GuiButton(11, centerX - 105, y, 100, 20, "Nametags: " + (ByteConfig.nametagsEnabled ? "§aON" : "§cOFF")));
        this.buttonList.add(new GuiButton(12, centerX + 5, y, 100, 20, "NT FKDR: " + (ByteConfig.ntShowFKDR ? "§aON" : "§cOFF")));
        y += 22;
        this.buttonList.add(new GuiButton(13, centerX - 105, y, 100, 20, "NT WS: " + (ByteConfig.ntShowWS ? "§aON" : "§cOFF")));
        this.buttonList.add(new GuiButton(14, centerX + 5, y, 100, 20, "NT Level: " + (ByteConfig.ntShowLevel ? "§aON" : "§cOFF")));
        y += 22;
        this.buttonList.add(new GuiButton(18, centerX - 105, y, 210, 20, "NT HP Bar: " + (ByteConfig.ntShowHealthBar ? "§aON" : "§cOFF")));

        y += 30;

        // --- SECTION: VISUALS & POSITION ---
        y += 15;
        this.buttonList.add(new GuiButton(2, centerX - 105, y, 210, 20, "Transparência: " + (ByteConfig.backgroundAlpha * 100 / 255) + "%"));
        y += 22;
        this.buttonList.add(new GuiButton(3, centerX - 105, y, 100, 20, "Cor Hdr: §" + colors[ByteConfig.headerColorIndex] + "Ex"));
        this.buttonList.add(new GuiButton(15, centerX + 5, y, 100, 20, "Escala: " + (int)(ByteConfig.scale * 100) + "%"));
        y += 22;
        this.buttonList.add(new GuiButton(4, centerX - 105, y, 100, 20, "X: " + ByteConfig.posX));
        this.buttonList.add(new GuiButton(5, centerX + 5, y, 100, 20, "Y: " + ByteConfig.posY));

        y += 30;

        // --- SECTION: SNIPERS ---
        y += 15;
        this.sniperInput = new GuiTextField(100, this.fontRendererObj, centerX - 105, y, 180, 20);
        this.buttonList.add(new GuiButton(101, centerX + 80, y, 25, 20, "+"));
        y += 25;
        
        int sniperY = y;
        for (int i = 0; i < ByteConfig.snipers.size(); i++) {
            String sniper = ByteConfig.snipers.get(i);
            int btnWidth = this.fontRendererObj.getStringWidth(sniper) + 15;
            if (centerX - 105 + btnWidth > centerX + 105) {
                centerX = this.width / 2; // dummy reset
                y += 22;
            }
            // Use 2000+ IDs for removal buttons
            this.buttonList.add(new GuiButton(2000 + i, centerX - 105, y, btnWidth, 20, "§c[x] §r" + sniper));
            centerX += btnWidth + 5;
        }

        this.buttonList.add(new GuiButton(0, (this.width / 2) - 100, this.height - 25, "Salvar e Fechar"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            ByteConfig.save();
            mc.displayGuiScreen(null);
        } else if (button.id == 1) {
            ByteConfig.enabled = !ByteConfig.enabled;
            button.displayString = "Overlay: " + (ByteConfig.enabled ? "§aON" : "§cOFF");
        } else if (button.id == 2) {
            ByteConfig.backgroundAlpha = (ByteConfig.backgroundAlpha + 15) % 256;
            button.displayString = "Transparência: " + (ByteConfig.backgroundAlpha * 100 / 255) + "%";
        } else if (button.id == 3) {
            ByteConfig.headerColorIndex = (ByteConfig.headerColorIndex + 1) % colors.length;
            button.displayString = "Cor Hdr: §" + colors[ByteConfig.headerColorIndex] + "Ex";
        } else if (button.id == 4) {
            ByteConfig.posX = (ByteConfig.posX + 10) % width;
            button.displayString = "X: " + ByteConfig.posX;
        } else if (button.id == 5) {
            ByteConfig.posY = (ByteConfig.posY + 10) % height;
            button.displayString = "Y: " + ByteConfig.posY;
        } else if (button.id == 6) {
            ByteConfig.showFKDR = !ByteConfig.showFKDR;
            button.displayString = "FKDR: " + (ByteConfig.showFKDR ? "§aON" : "§cOFF");
        } else if (button.id == 7) {
            ByteConfig.showWS = !ByteConfig.showWS;
            button.displayString = "WS: " + (ByteConfig.showWS ? "§aON" : "§cOFF");
        } else if (button.id == 8) {
            ByteConfig.showWR = !ByteConfig.showWR;
            button.displayString = "WR: " + (ByteConfig.showWR ? "§aON" : "§cOFF");
        } else if (button.id == 11) {
            ByteConfig.nametagsEnabled = !ByteConfig.nametagsEnabled;
            button.displayString = "Nametags: " + (ByteConfig.nametagsEnabled ? "§aON" : "§cOFF");
        } else if (button.id == 12) {
            ByteConfig.ntShowFKDR = !ByteConfig.ntShowFKDR;
            button.displayString = "NT FKDR: " + (ByteConfig.ntShowFKDR ? "§aON" : "§cOFF");
        } else if (button.id == 13) {
            ByteConfig.ntShowWS = !ByteConfig.ntShowWS;
            button.displayString = "NT WS: " + (ByteConfig.ntShowWS ? "§aON" : "§cOFF");
        } else if (button.id == 14) {
            ByteConfig.ntShowLevel = !ByteConfig.ntShowLevel;
            button.displayString = "NT Level: " + (ByteConfig.ntShowLevel ? "§aON" : "§cOFF");
        } else if (button.id == 18) {
            ByteConfig.ntShowHealthBar = !ByteConfig.ntShowHealthBar;
            button.displayString = "NT HP Bar: " + (ByteConfig.ntShowHealthBar ? "§aON" : "§cOFF");
        } else if (button.id == 15) {
            ByteConfig.scale += 0.1f;
            if (ByteConfig.scale > 1.55f) ByteConfig.scale = 0.1f;
            button.displayString = "Escala: " + (int)(ByteConfig.scale * 100) + "%";
        } else if (button.id == 16) {
            ByteConfig.showWins = !ByteConfig.showWins;
            button.displayString = "Wins: " + (ByteConfig.showWins ? "§aON" : "§cOFF");
        } else if (button.id == 17) {
            ByteConfig.showLosses = !ByteConfig.showLosses;
            button.displayString = "Losses: " + (ByteConfig.showLosses ? "§aON" : "§cOFF");
        } else if (button.id == 101) {
            addSniper();
        } else if (button.id >= 2000) {
            int index = button.id - 2000;
            if (index < ByteConfig.snipers.size()) {
                ByteConfig.snipers.remove(index);
                mc.displayGuiScreen(new ByteGui());
            }
        }
    }

    private void addSniper() {
        String nick = sniperInput.getText().trim();
        if (!nick.isEmpty()) {
            if (!ByteConfig.snipers.contains(nick)) {
                ByteConfig.snipers.add(nick);
            }
            sniperInput.setText("");
            mc.displayGuiScreen(new ByteGui());
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.sniperInput.textboxKeyTyped(typedChar, keyCode);
        if (keyCode == 28) addSniper();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.sniperInput.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.sniperInput.updateCursorCounter();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        
        int centerX = this.width / 2;
        this.drawCenteredString(this.fontRendererObj, "§b§lByte Overlay - Configurações", centerX, 5, 16777215);
        
        this.drawCenteredString(this.fontRendererObj, "§7§nOverlay Stats", centerX, 25, 16777215);
        this.drawCenteredString(this.fontRendererObj, "§7§nNametags", centerX, 115, 16777215);
        this.drawCenteredString(this.fontRendererObj, "§7§nVisuals & Position", centerX, 222, 16777215);
        this.drawCenteredString(this.fontRendererObj, "§c§nSnipers", centerX, 312, 16777215);
        
        this.sniperInput.drawTextBox();
    }

    @Override
    public boolean doesGuiPauseGame() { return false; }
}
