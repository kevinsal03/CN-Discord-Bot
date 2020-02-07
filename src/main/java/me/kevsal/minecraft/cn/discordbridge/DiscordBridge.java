package me.kevsal.minecraft.cn.discordbridge;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class DiscordBridge extends Plugin {

    private static Configuration configuration;
    private static JDA jda;

    @Override
    public void onEnable() {
        getProxy().getLogger().info("ClubNetwork Discord Bridge Enabled");

        //create data folder if not exist
        if (!getDataFolder().exists())
            getDataFolder().mkdir();
        //create file if not exist
        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //load config
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        /* Config code copied from https://www.spigotmc.org/wiki/using-the-bungee-configuration-system/ */

        /* REGISTER EVENTS */
        getProxy().getPluginManager().registerListener(this, new ListenerChatEvent(this));

        try {
            createBot();
        } catch(Exception e) {
            e.printStackTrace();
            getLogger().warning("Failed to create bot!");
        }

        //send startup message
        TextChannel channel;
        try {
            channel = DiscordBridge.getJDA().getTextChannelById(DiscordBridge.getConfig().getString("chat-channel"));
            channel.sendMessage("Proxy started successfully!").queue();
        } catch (Exception ex) {
            ex.printStackTrace();
            getLogger().warning("Failed to send message");
        }
    }

    @Override
    public void onDisable() {
        //send shutdown message
        TextChannel channel;
        try {
            channel = DiscordBridge.getJDA().getTextChannelById(DiscordBridge.getConfig().getString("chat-channel"));
            channel.sendMessage("Proxy shutting down!").queue();
        } catch (Exception ex) {
            ex.printStackTrace();
            getLogger().warning("Failed to send message");
        }

        getProxy().getLogger().info("ClubNetwork Discord Bridge Disabled");
    }

    private void createBot() throws LoginException, InterruptedException {
        String token = getConfig().getString("token");

        jda = new JDABuilder(token).addEventListeners(new DListenerMessageReceived(this)).build().awaitReady();
    }

    // pass config
    public static Configuration getConfig() {
        return configuration;
    }
    // get JDA
    public static JDA getJDA() {
        return jda;
    }
}
