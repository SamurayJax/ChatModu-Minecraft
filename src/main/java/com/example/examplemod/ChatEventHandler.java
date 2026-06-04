package com.example.examplemod;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatEventHandler {

    @SubscribeEvent
    public void onPlayerChat(ServerChatEvent event) {
        event.setCanceled(true);

        EntityPlayerMP sender = event.getPlayer();
        String chatMessage = event.getMessage();

        if (chatMessage.startsWith("#K# ")) {
            ChatDeliveryHelper.sendClanMessage(
                    FMLCommonHandler.instance().getMinecraftServerInstance(),
                    sender,
                    chatMessage.substring(4)
            );
            return;
        }

        if (chatMessage.toLowerCase().startsWith("/s ")) {
            ChatDeliveryHelper.sendWhisper(
                    FMLCommonHandler.instance().getMinecraftServerInstance(),
                    sender,
                    chatMessage.substring(3)
            );
            return;
        }

        TextComponentString message = new TextComponentString(
                "<" + sender.getName() + "> " + chatMessage
        );

        for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
            if (player.dimension == sender.dimension && player.getDistanceSq(sender) <= 2500.0D) {
                player.sendMessage(message);
            }
        }
    }
}
