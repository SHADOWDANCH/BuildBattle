package ua.shadowdan.buildbattle.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.IOException;

/**
 * Created by SHADOWDAN on 01.07.2019.
 */
public class WorldDeserializer extends StdDeserializer<World> {

    public final static WorldDeserializer INSTANCE = new WorldDeserializer();

    private WorldDeserializer() {
        super(World.class);
    }

    @Override
    public World deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        return Bukkit.getWorld(context.readValue(parser, String.class));
    }
}
