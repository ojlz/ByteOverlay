package com.byteoverlay.input;

import com.byteoverlay.config.ByteGui;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.client.Minecraft;

import java.util.Arrays;
import java.util.List;

public class SettingsCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "byteoverlay";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("byte", "bo");
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/byteoverlay";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            Minecraft.getMinecraft().displayGuiScreen(new ByteGui());
        });
    }
}
