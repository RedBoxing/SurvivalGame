package fr.redboxing.survivalgame.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class LocationUtils {
    public static Location findRandomSafeLocation(World world, double maxDistance) {
        // 35 is the range findSafeLocationAround() will look for a spawn block
        maxDistance-=10;
        Location randomLoc;
        Location location = null;

        int i = 0;
        while (location == null){
            i++;
            randomLoc = RandomUtils.newRandomLocation(world, maxDistance);
            location = findSafeLocationAround(randomLoc, 10);
            if (i > 20){
                return randomLoc;
            }
        }

        return location;
    }

    private static Location findSafeLocationAround(Location loc, int searchRadius) {
        boolean nether = loc.getWorld().getEnvironment() == World.Environment.NETHER;
        Material material;
        Location betterLocation;

        for(int i = -searchRadius ; i <= searchRadius ; i +=3){
            for(int j = -searchRadius ; j <= searchRadius ; j+=3){
                betterLocation = getGroundLocation(loc.clone().add(new Vector(i, 0, j)), nether);

                // Check if location is on the nether roof.
                if (nether && betterLocation.getBlockY() > 120){
                    continue;
                }

                // Check if the block below is lava / water
                material = betterLocation.clone().add(0, -1, 0).getBlock().getType();
                if(material.equals(Material.LAVA) || material.equals(Material.WATER)){
                    continue;
                }

                // Stop players from spawning on top of the lobby.
                if (betterLocation.getBlockY() > 160) {
                    continue;
                }

                return betterLocation;
            }
        }

        return null;
    }

    private static Location getGroundLocation(Location loc, boolean allowCaves){
        World w = loc.getWorld();

        loc.setY(0);

        if (allowCaves){
            while (loc.getBlock().getType() != Material.AIR){
                loc = loc.add(0, 1, 0);
            }
        }else {
            loc = w.getHighestBlockAt(loc).getLocation();
        }

        loc = loc.add(.5, 0, .5);
        return loc;
    }
}
