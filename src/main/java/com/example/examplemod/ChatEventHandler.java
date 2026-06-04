package com.example.examplemod;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ChatEventHandler {

    @SubscribeEvent
    public void onPlayerChat(ServerChatEvent event) {
        event.setCanceled(true);

        EntityPlayerMP sender = event.getPlayer();
        TextComponentString message = new TextComponentString(
                "<" + sender.getName() + "> " + event.getMessage()
        );

        for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
            if (player.dimension == sender.dimension && player.getDistanceSq(sender) <= 2500.0D) {
                player.sendMessage(message);
            }
        }
    }
}
