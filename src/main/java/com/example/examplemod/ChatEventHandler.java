package com.example.examplemod;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ChatEventHandler {

    @SubscribeEvent
    public void onPlayerChat(ServerChatEvent event) {
        event.setCanceled(true);

        EntityPlayerMP sender = event.getPlayer();
        String chatMessage = event.getMessage();
        double distanceLimitSq = 2500.0D;
        TextComponentString message;

        if (chatMessage.startsWith("/S ")) {
            String whisperMessage = chatMessage.substring(3);
            distanceLimitSq = 100.0D;
            message = new TextComponentString(
                    "[Sessiz] <" + sender.getName() + ">: " + whisperMessage
            );
            message.getStyle().setColor(TextFormatting.DARK_GRAY);
        } else {
            message = new TextComponentString(
                    "<" + sender.getName() + "> " + chatMessage
            );
        }

        for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
            if (player.dimension == sender.dimension && player.getDistanceSq(sender) <= distanceLimitSq) {
                player.sendMessage(message);
            }
        }
    }
}
