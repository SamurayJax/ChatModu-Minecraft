package com.example.examplemod;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class ChatCommand extends CommandBase {

    @Override
    public String getName() {
        return "chat";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/chat help";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0 || "help".equalsIgnoreCase(args[0])) {
            sendHelp(sender);
            return;
        }

        throw new WrongUsageException(getUsage(sender));
    }

    private void sendHelp(ICommandSender sender) {
        sendColoredMessage(sender, "--- Chat Sistemi Kurallar\u0131 ---", TextFormatting.GOLD);
        sendColoredMessage(sender, "\u2022 Normal Chat: Mesaj\u0131n\u0131z\u0131 sadece 50 blok yak\u0131n\u0131n\u0131zdaki oyuncular duyabilir.", TextFormatting.GRAY);
        sendColoredMessage(sender, "\u2022 F\u0131s\u0131lt\u0131 (/S [mesaj]): 10 blok yak\u0131ndakilere gizlice f\u0131s\u0131ldars\u0131n\u0131z ve sunucu loglar\u0131na kaydedilmez.", TextFormatting.GRAY);
        sendColoredMessage(sender, "\u2022 Klan Chati (GUI): Sol sekmelerden ge\u00E7i\u015F yapabilirsiniz. S\u0131n\u0131rs\u0131z mesafedeki t\u00FCm klan \u00FCyeleri ye\u015Fil renkte g\u00F6r\u00FCr.", TextFormatting.GRAY);
    }

    private void sendColoredMessage(ICommandSender sender, String message, TextFormatting color) {
        TextComponentString component = new TextComponentString(message);
        component.getStyle().setColor(color);
        sender.sendMessage(component);
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, "help");
        }

        return Collections.emptyList();
    }
}
