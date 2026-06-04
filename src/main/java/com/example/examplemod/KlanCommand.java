package com.example.examplemod;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class KlanCommand extends CommandBase {

    @Override
    public String getName() {
        return "klan";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/klan olu\u015Ftur [isim] | /klan davet [oyuncu] | /klan da\u011F\u0131t";
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
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);

        if (args.length == 0) {
            throw new WrongUsageException(getUsage(sender));
        }

        if ("help".equalsIgnoreCase(args[0])) {
            sendHelp(player);
        } else if ("olu\u015Ftur".equalsIgnoreCase(args[0])) {
            createClan(player, args);
        } else if ("davet".equalsIgnoreCase(args[0])) {
            invitePlayer(server, sender, player, args);
        } else if ("da\u011F\u0131t".equalsIgnoreCase(args[0])) {
            dissolveClan(player);
        } else {
            throw new WrongUsageException(getUsage(sender));
        }
    }

    private void sendHelp(EntityPlayerMP player) {
        sendColoredMessage(player, "--- Klan Sistemi Komutlar\u0131 ---", TextFormatting.GOLD);
        sendCommandDescription(player, "/klan olu\u015Ftur [isim]", " - Yeni bir klan kurman\u0131z\u0131 sa\u011Flar.");
        sendCommandDescription(player, "/klan davet [oyuncu]", " - Bir oyuncuyu klan\u0131n\u0131za davet eder.");
        sendCommandDescription(player, "/klan da\u011F\u0131t", " - Sahibi oldu\u011Funuz klan\u0131 tamamen kapat\u0131r.");
        sendCommandDescription(player, "/klan help", " - Klan komutlar\u0131n\u0131 g\u00F6sterir.");
    }

    private void sendColoredMessage(EntityPlayerMP player, String message, TextFormatting color) {
        TextComponentString component = new TextComponentString(message);
        component.getStyle().setColor(color);
        player.sendMessage(component);
    }

    private void sendCommandDescription(EntityPlayerMP player, String command, String description) {
        TextComponentString commandComponent = new TextComponentString(command);
        TextComponentString descriptionComponent = new TextComponentString(description);

        commandComponent.getStyle().setColor(TextFormatting.YELLOW);
        descriptionComponent.getStyle().setColor(TextFormatting.GRAY);
        commandComponent.appendSibling(descriptionComponent);
        player.sendMessage(commandComponent);
    }

    private void createClan(EntityPlayerMP player, String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("/klan olu\u015Ftur [isim]");
        }

        ClanManager clanManager = ClanManager.getInstance();
        UUID playerId = player.getUniqueID();

        if (clanManager.getClanName(playerId) != null) {
            player.sendMessage(new TextComponentString("Zaten bir klandas\u0131n."));
            return;
        }

        String clanName = args[1];

        if (clanManager.createClan(clanName, playerId)) {
            player.sendMessage(new TextComponentString("Klan olu\u015Fturuldu: " + clanName));
        } else {
            player.sendMessage(new TextComponentString("Bu isimde bir klan zaten var."));
        }
    }

    private void invitePlayer(MinecraftServer server, ICommandSender sender, EntityPlayerMP player, String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("/klan davet [oyuncu]");
        }

        ClanManager clanManager = ClanManager.getInstance();
        UUID playerId = player.getUniqueID();
        String clanName = clanManager.getClanName(playerId);

        if (clanName == null) {
            player.sendMessage(new TextComponentString("Davet gondermek icin once bir klana katilmalisin."));
            return;
        }

        EntityPlayerMP target = getPlayer(server, sender, args[1]);
        UUID targetId = target.getUniqueID();

        if (playerId.equals(targetId)) {
            player.sendMessage(new TextComponentString("Kendini klana davet edemezsin."));
            return;
        }

        if (clanManager.getClanName(targetId) != null) {
            player.sendMessage(new TextComponentString("Bu oyuncu zaten bir klanda."));
            return;
        }

        clanManager.addPendingInvitation(playerId, targetId);
        player.sendMessage(new TextComponentString(target.getName() + " oyuncusuna klan daveti gonderildi."));
        target.sendMessage(new TextComponentString(player.getName() + " seni " + clanName + " klanina davet etti."));
    }

    private void dissolveClan(EntityPlayerMP player) {
        ClanManager clanManager = ClanManager.getInstance();

        if (clanManager.dissolveClan(player.getUniqueID())) {
            player.sendMessage(new TextComponentString("Klan da\u011F\u0131t\u0131ld\u0131."));
        } else {
            player.sendMessage(new TextComponentString("Da\u011F\u0131tacak bir klan\u0131n yok."));
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, "help", "olu\u015Ftur", "davet", "da\u011F\u0131t");
        }

        if (args.length == 2 && "davet".equalsIgnoreCase(args[0])) {
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }

        return Collections.emptyList();
    }
}
