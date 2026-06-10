package com.byteoverlay.config;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import java.io.IOException;

public class ByteGui extends GuiScreen {
    private final String[] colors = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    private GuiTextField sniperInput;

    private int cx;
    private int y;

    private void lbl(String text) {
        drawCenteredString(fontRendererObj, text, cx, y, 0x55FFFF);
        y += 14;
    }

    private void sep() {
        y += 10;
    }

    private void btn(int id, String text) {
        buttonList.add(new GuiButton(id, cx + offset, y, 100, 20, text));
        offset += 110;
    }

    private int offset;

    @Override
    public void initGui() {
        cx = width / 2;
        y = 25;

        buttonList.clear();

        // --- OVERLAY STATS ---
        lbl("§b§lOverlay Stats");
        offset = -105;
        btn(1, "Overlay: " + toggle(ByteConfig.enabled));
        btn(6, "FKDR: " + toggle(ByteConfig.showFKDR));
        y += 22; offset = -105;
        btn(7, "WS: " + toggle(ByteConfig.showWS));
        btn(8, "WR: " + toggle(ByteConfig.showWR));
        y += 22; offset = -105;
        btn(16, "Wins: " + toggle(ByteConfig.showWins));
        btn(17, "Losses: " + toggle(ByteConfig.showLosses));
        y += 22; offset = -105;
        btn(19, "FPS: " + toggle(ByteConfig.showFps));
        btn(20, "Ping: " + toggle(ByteConfig.showPing));
        sep();

        // --- NAMETAGS ---
        lbl("§b§lNametags");
        offset = -105;
        btn(11, "Nametags: " + toggle(ByteConfig.nametagsEnabled));
        btn(12, "NT FKDR: " + toggle(ByteConfig.ntShowFKDR));
        y += 22; offset = -105;
        btn(13, "NT WS: " + toggle(ByteConfig.ntShowWS));
        btn(14, "NT Level: " + toggle(ByteConfig.ntShowLevel));
        y += 22;
        buttonList.add(new GuiButton(18, cx - 105, y, 210, 20, "NT HP Bar: " + toggle(ByteConfig.ntShowHealthBar)));
        sep();

        // --- VISUALS & POSITION ---
        lbl("§b§lVisuals & Position");
        buttonList.add(new GuiButton(2, cx - 105, y, 210, 20, "Transparencia: " + (ByteConfig.backgroundAlpha * 100 / 255) + "%"));
        y += 22; offset = -105;
        btn(3, "Cor Hdr: §" + colors[ByteConfig.headerColorIndex] + "Ex");
        btn(15, "Escala: " + (int)(ByteConfig.scale * 100) + "%");
        y += 22; offset = -105;
        btn(4, "X: " + ByteConfig.posX);
        btn(5, "Y: " + ByteConfig.posY);
        sep();

        // --- SNIPERS ---
        lbl("§c§lSnipers");
        offset = -105;
        sniperInput = new GuiTextField(100, fontRendererObj, cx - 105, y, 180, 20);
        buttonList.add(new GuiButton(101, cx + 80, y, 25, 20, "+"));
        y += 25;

        for (int i = 0; i < ByteConfig.snipers.size(); i++) {
            String sniper = ByteConfig.snipers.get(i);
            int btnWidth = fontRendererObj.getStringWidth(sniper) + 20;
            if (cx - 105 + btnWidth > cx + 105) {
                y += 22;
                offset = -105;
            }
            buttonList.add(new GuiButton(2000 + i, cx + offset, y, btnWidth, 20, "§c[x] §r" + sniper));
            offset += btnWidth + 5;
        }

        buttonList.add(new GuiButton(0, cx - 100, height - 28, 200, 20, "§aSalvar e Fechar"));
    }

    private String toggle(boolean val) {
        return val ? "§aON" : "§cOFF";
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            ByteConfig.save();
            mc.displayGuiScreen(null);
        } else if (button.id == 1) {
            ByteConfig.enabled = !ByteConfig.enabled;
            button.displayString = "Overlay: " + toggle(ByteConfig.enabled);
        } else if (button.id == 2) {
            ByteConfig.backgroundAlpha = (ByteConfig.backgroundAlpha + 15) % 256;
            button.displayString = "Transparencia: " + (ByteConfig.backgroundAlpha * 100 / 255) + "%";
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
            button.displayString = "FKDR: " + toggle(ByteConfig.showFKDR);
        } else if (button.id == 7) {
            ByteConfig.showWS = !ByteConfig.showWS;
            button.displayString = "WS: " + toggle(ByteConfig.showWS);
        } else if (button.id == 8) {
            ByteConfig.showWR = !ByteConfig.showWR;
            button.displayString = "WR: " + toggle(ByteConfig.showWR);
        } else if (button.id == 11) {
            ByteConfig.nametagsEnabled = !ByteConfig.nametagsEnabled;
            button.displayString = "Nametags: " + toggle(ByteConfig.nametagsEnabled);
        } else if (button.id == 12) {
            ByteConfig.ntShowFKDR = !ByteConfig.ntShowFKDR;
            button.displayString = "NT FKDR: " + toggle(ByteConfig.ntShowFKDR);
        } else if (button.id == 13) {
            ByteConfig.ntShowWS = !ByteConfig.ntShowWS;
            button.displayString = "NT WS: " + toggle(ByteConfig.ntShowWS);
        } else if (button.id == 14) {
            ByteConfig.ntShowLevel = !ByteConfig.ntShowLevel;
            button.displayString = "NT Level: " + toggle(ByteConfig.ntShowLevel);
        } else if (button.id == 15) {
            ByteConfig.scale += 0.1f;
            if (ByteConfig.scale > 1.55f) ByteConfig.scale = 0.1f;
            button.displayString = "Escala: " + (int)(ByteConfig.scale * 100) + "%";
        } else if (button.id == 16) {
            ByteConfig.showWins = !ByteConfig.showWins;
            button.displayString = "Wins: " + toggle(ByteConfig.showWins);
        } else if (button.id == 17) {
            ByteConfig.showLosses = !ByteConfig.showLosses;
            button.displayString = "Losses: " + toggle(ByteConfig.showLosses);
        } else if (button.id == 18) {
            ByteConfig.ntShowHealthBar = !ByteConfig.ntShowHealthBar;
            button.displayString = "NT HP Bar: " + toggle(ByteConfig.ntShowHealthBar);
        } else if (button.id == 19) {
            ByteConfig.showFps = !ByteConfig.showFps;
            button.displayString = "FPS: " + toggle(ByteConfig.showFps);
        } else if (button.id == 20) {
            ByteConfig.showPing = !ByteConfig.showPing;
            button.displayString = "Ping: " + toggle(ByteConfig.showPing);
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
        sniperInput.textboxKeyTyped(typedChar, keyCode);
        if (keyCode == 28) addSniper();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        sniperInput.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        sniperInput.updateCursorCounter();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        sniperInput.drawTextBox();
    }

    @Override
    public boolean doesGuiPauseGame() { return false; }
}
