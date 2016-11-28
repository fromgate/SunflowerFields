package me.fromgate.sunflowerfields;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.regex.Pattern;

public class Farm {

    private final static Pattern LOC_PATTERN = Pattern.compile("\\S+, *-?\\d+, *\\d+, *-?\\d+");

    String world = null;
    int x;
    int y;
    int z;

    public Farm(Location loc) {
        this.world = loc.getWorld().getName();
        this.x = loc.getBlockX();
        this.y = loc.getBlockY();
        this.z = loc.getBlockZ();
    }

    public Farm(String str) {
        if (!LOC_PATTERN.matcher(str).matches()) return;
        String[] ln = str.split(",\\s*");

        world = ln[0];
        x = Integer.parseInt(ln[1]);
        y = Integer.parseInt(ln[2]);
        z = Integer.parseInt(ln[3]);
    }

    public boolean isValid() {
        return world != null;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((world == null) ? 0 : world.hashCode());
        result = prime * result + x;
        result = prime * result + y;
        result = prime * result + z;
        return result;
    }

    public boolean compareTo(Block block) {
        return compareTo(block.getLocation());
    }

    public boolean compareTo(Location loc) {
        if (!this.world.equalsIgnoreCase(loc.getWorld().getName())) return false;
        if (this.z != loc.getBlockZ()) return false;
        if (this.x != loc.getBlockX()) return false;
        return this.y == loc.getBlockY();
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Farm other = (Farm) obj;
        if (world == null || other.world == null) return false;
        if (!world.equalsIgnoreCase(other.world)) return false;
        if (z != other.z) return false;
        if (x != other.x) return false;
        if (y != other.y) return false;
        return true;
    }

    @Override
    public String toString() {
        return new StringBuilder(world).append(", ").append(x).append(", ").append(y).append(", ").append(z).toString();
    }


}
