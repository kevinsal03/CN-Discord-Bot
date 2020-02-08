package me.kevsal.minecraft.cn.discordbridge;

import net.dv8tion.jda.api.EmbedBuilder;
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
            String name;
            String server;
            ProxiedPlayer p = null;
            if(e.getSender() instanceof ProxiedPlayer) {
                name = ((ProxiedPlayer) e.getSender()).getDisplayName();
                server = ((ProxiedPlayer) e.getSender()).getServer().getInfo().getName();
                p = (ProxiedPlayer)e.getSender();
            } else {
                name = "CONSOLE";
                server = "LOG";
            }

            String message = e.getMessage();

            if(e.isCommand()) {
                // Build command Message embed
                EmbedBuilder eb = new EmbedBuilder();
                eb.setAuthor(name, null, "https://mc-heads.net/head/" + name + ".png");
                eb.addField("Command: ", message, true);
                eb.setFooter("Server: " + server);

                TextChannel channel = null;
                try {
                    channel = DiscordBridge.getJDA().getTextChannelById(DiscordBridge.getConfig().getString("command-channel"));
                    assert channel != null;
                    channel.sendMessage(eb.build()).queue();
                    if (message.contains("/lp") | message.contains("/perm") | message.contains("/bperm") ) {
                        channel.sendMessage("<@&" + DiscordBridge.getConfig().getString("pc-role") + ">" + " A LuckPerms command ran by user: " + name).queue();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    plugin.getLogger().warning("Failed to send message");
                }
            } else {
                // Build Embed from message
                EmbedBuilder eb = new EmbedBuilder();
                eb.setAuthor(name, null, "https://mc-heads.net/head/" + name + ".png");
                eb.setTitle(message);
                //eb.addField("", message, true);
                eb.setFooter("Server: " + server);

                TextChannel channel = null;
                try {
                    channel = DiscordBridge.getJDA().getTextChannelById(DiscordBridge.getConfig().getString("chat-channel"));
                    assert channel != null;
                    channel.sendMessage(eb.build()).queue();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    plugin.getLogger().warning("Failed to send message");
                }
            }
        }
    }

}
