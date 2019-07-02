package ua.shadowdan.buildbattle;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.module.paranamer.ParanamerModule;
import com.jasonclawson.jackson.dataformat.hocon.HoconFactory;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import ua.shadowdan.buildbattle.command.BuildBattleCommand;
import ua.shadowdan.buildbattle.command.VoteCommand;
import ua.shadowdan.buildbattle.config.Configuration;
import ua.shadowdan.buildbattle.config.LocationMixin;
import ua.shadowdan.buildbattle.config.WorldDeserializer;

import java.io.*;
import java.util.logging.Level;

/**
 * Created by SHADOWDAN on 30.06.2019.
 */
@Author("SAHDOWDAN")
@Commands(value = {@Command(name ="buildbattle", aliases = {"bb"}), @Command(name = "vote")})
@Plugin(name = "BuildBattle", version = "1.0")
public class BuildBattle extends JavaPlugin {

    @Getter
    private GameManager gameManager;
    private Configuration config;
    private ObjectMapper objectMapper;

    @Override
    public void onEnable() {
        String configFileName = "config.conf";
        this.saveDefaultConfig(configFileName);
        objectMapper = new ObjectMapper(new HoconFactory());

        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        objectMapper.addMixIn(Location.class, LocationMixin.class);

        SimpleModule module = new SimpleModule();
        module.addDeserializer(World.class, WorldDeserializer.INSTANCE);

        objectMapper.registerModule(new ParanamerModule());
        objectMapper.registerModule(module);

        try {
            config = objectMapper.readValue(new File(getDataFolder(), configFileName), Configuration.class);
        } catch (IOException e) {
            Bukkit.getPluginManager().disablePlugin(this);
            throw new RuntimeException("Failed to load config! Plugin will not be enabled", e);
        }

        if (config.getSpawnPoints().size() < config.getMaxPlayers()) {
            this.getLogger().log(Level.WARNING, "Max player's less then spawn point's. This may cause unexpected errors!");
        }

        this.gameManager = new GameManager(this);
        this.gameManager.setup();

        new BuildBattleCommand(this).setup();
        new VoteCommand(this).setup();
    }

    public void saveDefaultConfig(String configFileName) {
        File configFile = new File(getDataFolder(), configFileName);
        if (!configFile.exists()) {
            saveResource(configFileName, false);
        }
    }

    public Configuration getPluginConfig() {
        if (config == null) {
            throw new RuntimeException("Configuration not initialized!");
        }
        return config;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
