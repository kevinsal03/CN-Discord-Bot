package me.kevsal.minecraft.cn.discordbridge;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.Objects;

public class DListenerMessageReceived extends ListenerAdapter {

    private DiscordBridge plugin;

    DListenerMessageReceived(DiscordBridge plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        TextChannel channel = DiscordBridge.getJDA().getTextChannelById(DiscordBridge.getConfig().getString("chat-channel"));
        if(!e.getAuthor().getId().equals(DiscordBridge.getJDA().getSelfUser().getId())) {
            if(e.getChannel().equals(channel)){
                BaseComponent message = new TextComponent(ChatColor.DARK_PURPLE + "[DISCORD] " + ChatColor.RESET + Objects.requireNonNull(e.getMember()).getEffectiveName() + ": " + e.getMessage().getContentDisplay());
                plugin.getProxy().broadcast(message);
            }
        }
    }
}
