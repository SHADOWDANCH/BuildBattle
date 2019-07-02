package ua.shadowdan.buildbattle.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Location;
import ua.shadowdan.buildbattle.cuboid.Cuboid;

import java.util.List;

/**
 * Created by SHADOWDAN on 30.06.2019.
 */
@Data
@AllArgsConstructor(onConstructor = @__({@JsonCreator}))
public class Configuration {

    @JsonProperty("max-players")
    private final int maxPlayers;
    @JsonProperty("game-duration")
    private final long gameDuration;
    private final List<Cuboid> plots;
    @JsonProperty("spawn-points")
    private final List<Location> spawnPoints;
}
