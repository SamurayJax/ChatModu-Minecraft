package com.example.examplemod;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class ChatDeliveryHelper {
    private ChatDeliveryHelper() {
    }

    public static void sendWhisper(MinecraftServer server, EntityPlayerMP sender, String whisperMessage) {
        TextComponentString message = new TextComponentString(
                "[Sessiz] <" + sender.getName() + ">: " + whisperMessage
        );
        message.getStyle().setColor(TextFormatting.DARK_GRAY);

        for (EntityPlayerMP player : server.getPlayerList().getPlayers()) {
            if (player.dimension == sender.dimension && player.getDistanceSq(sender) <= 100.0D) {
                player.sendMessage(message);
            }
        }
    }
}
