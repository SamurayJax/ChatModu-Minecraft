package com.example.examplemod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = ExampleMod.MODID, value = Side.CLIENT)
public class ChatGuiHandler {
    public static String currentChannel = "GLOBAL";

    private static final int GLOBAL_CHAT_BUTTON_ID = 901;
    private static final int CLAN_CHAT_BUTTON_ID = 902;

    @SubscribeEvent
    public static void onChatGuiInit(GuiScreenEvent.InitGuiEvent.Post event) {
        if (!(event.getGui() instanceof GuiChat)) {
            return;
        }

        int x = 4;
        int y = event.getGui().height - 36;

        event.getButtonList().add(new GuiButton(GLOBAL_CHAT_BUTTON_ID, x, y, 70, 20, "Chat 1"));
        event.getButtonList().add(new GuiButton(CLAN_CHAT_BUTTON_ID, x + 74, y, 90, 20, "Klan Chati"));
    }

    @SubscribeEvent
    public static void onChatButtonPressed(GuiScreenEvent.ActionPerformedEvent.Post event) {
        if (!(event.getGui() instanceof GuiChat)) {
            return;
        }

        if (event.getButton().id == GLOBAL_CHAT_BUTTON_ID) {
            currentChannel = "GLOBAL";
            sendClientMessage("Global chat kanalına geçildi.");
        } else if (event.getButton().id == CLAN_CHAT_BUTTON_ID) {
            currentChannel = "CLAN";
            sendClientMessage("Klan chat kanalına geçildi.");
        }
    }

    @SubscribeEvent
    public static void onClientChat(ClientChatEvent event) {
        if ("CLAN".equals(currentChannel) && !event.getMessage().startsWith("/")) {
            event.setMessage("#K# " + event.getMessage());
        }
    }

    private static void sendClientMessage(String message) {
        EntityPlayer player = Minecraft.getMinecraft().player;

        if (player != null) {
            player.sendMessage(new TextComponentString(message));
        }
    }
}
