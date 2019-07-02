package ua.shadowdan.buildbattle.cuboid;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashSet;
import java.util.Set;

@Getter
public class Cuboid {
    private World world;
    private long xMin;
    private long xMax;
    private long yMin;
    private long yMax;
    private long zMin;
    private long zMax;

    public Cuboid(Location loc1, Location loc2) {
        this.world = loc1.getWorld();
        normalize(loc1.getBlockX(), loc1.getBlockY(), loc1.getBlockZ(), loc2.getBlockX(), loc2.getBlockY(),
                loc2.getBlockZ());
    }

    @JsonCreator
    public Cuboid(World world, long x1, long y1, long z1, long x2, long y2, long z2) {
        this.world = world;
        normalize(x1, y1, z1, x2, y2, z2);
    }

    private void normalize(long x1, long y1, long z1, long x2, long y2, long z2) {
        this.xMin = Math.min(x1, x2);
        this.xMax = Math.max(x1, x2);
        this.yMin = Math.min(y1, y2);
        this.yMax = Math.max(y1, y2);
        this.zMin = Math.min(z1, z2);
        this.zMax = Math.max(z1, z2);
    }

    public boolean intersects(Cuboid cuboid) {
        return cuboid.xMin <= xMax && cuboid.xMax >= xMin && cuboid.yMin <= yMax && cuboid.yMax >= yMin
                && cuboid.zMin <= zMax && cuboid.zMax >= zMin;
    }

    public boolean contains(Location loc) {
        return contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    public boolean contains(long x, long y, long z) {
        return x >= xMin && x <= xMax && y >= yMin && y <= yMax && z >= zMin && z <= zMax;
    }

    public long getVolume() {
        return getWidth() * getHeight() * getDepth();
    }

    public long getWidth() {
        return xMax - xMin + 1;
    }

    public long getHeight() {
        return yMax - yMin + 1;
    }

    public long getDepth() {
        return zMax - zMin + 1;
    }

    public Set<Chunk> getChunks() {
        Set<Chunk> chunks = new HashSet<>();

        int x1 = (int) xMin & ~0xf;
        int x2 = (int) xMax & ~0xf;
        int z1 = (int) zMin & ~0xf;
        int z2 = (int) zMax & ~0xf;
        for (int x = x1; x <= x2; x += 16) {
            for (int z = z1; z <= z2; z += 16) {
                chunks.add(world.getChunkAt(x >> 4, z >> 4));
            }
        }
        return chunks;
    }

    public Cuboid expand(CuboidDirection dir, int amount) {
        switch (dir) {
            case North:
                return new Cuboid(world, xMin - amount, yMin, zMin, xMax, yMax, zMax);
            case South:
                return new Cuboid(world, xMin, yMin, zMin, xMax + amount, yMax, zMax);
            case East:
                return new Cuboid(world, xMin, yMin, zMin - amount, xMax, yMax, zMax);
            case West:
                return new Cuboid(world, xMin, yMin, zMin, xMax, yMax, zMax + amount);
            case Down:
                return new Cuboid(world, xMin, yMin - amount, zMin, xMax, yMax, zMax);
            case Up:
                return new Cuboid(world, xMin, yMin, zMin, xMax, yMax + amount, zMax);
            default:
                throw new IllegalArgumentException("invalid direction " + dir);
        }
    }

    public Cuboid shift(CuboidDirection dir, int amount) {
        return expand(dir, amount).expand(dir.opposite(), -amount);
    }

    public Cuboid outset(CuboidDirection dir, int amount) {
        return expand(CuboidDirection.North, amount).expand(CuboidDirection.South, amount)
                .expand(CuboidDirection.East, amount).expand(CuboidDirection.West, amount)
                .expand(CuboidDirection.Down, amount).expand(CuboidDirection.Up, amount);
    }

    public Cuboid inset(CuboidDirection dir, int amount) {
        return outset(dir, -amount);
    }

    @Override
    public String toString() {
        return "Cuboid: " + xMin + "," + yMin + "," + zMin + "=>" + xMax + "," + yMax + "," + zMax;
    }
}