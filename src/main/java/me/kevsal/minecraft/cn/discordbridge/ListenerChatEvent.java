package me.kevsal.minecraft.cn.discordbridge;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ListenerChatEvent implements Listener {

    private final DiscordBridge plugin;

    ListenerChatEvent(DiscordBridge plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChatEvent(ChatEvent e) {
        if(!e.isCancelled()) {
            //player chat not cancelled
            if(e.isCommand()) {
                return;
                //TODO: Add something here?
            } else {
                String name;
                String server;
                if(e.getSender() instanceof ProxiedPlayer) {
                    name = ((ProxiedPlayer) e.getSender()).getDisplayName();
                    server = ((ProxiedPlayer) e.getSender()).getServer().getInfo().getName();
                } else {
                    name = "CONSOLE";
                    server = "LOG";
                }

                String message = e.getMessage();

                String formattedMessage = "`[" + server + "] " + name + ": " + message + "`";

                TextChannel channel = null;
                try {
                    channel = DiscordBridge.getJDA().getTextChannelById(DiscordBridge.getConfig().getString("chat-channel"));
                    channel.sendMessage(formattedMessage).queue();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    plugin.getLogger().warning("Failed to send message");
                }


            }
        }
    }

}
