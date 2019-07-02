package ua.shadowdan.buildbattle.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.bukkit.World;

/**
 * Created by SHADOWDAN on 01.07.2019.
 */
public abstract class LocationMixin {

    @JsonCreator
    public LocationMixin(World world, double x, double y, double z, float yaw, float pitch) { }
}

