package fr.redboxing.survivalgame.game;

import fr.redboxing.survivalgame.SurvivalGame;
import fr.redboxing.survivalgame.exceptions.NoHuntedException;
import fr.redboxing.survivalgame.utils.LocationUtils;
import fr.redboxing.survivalgame.utils.MathHelper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Random;
import java.util.UUID;

public class GameManager {
    @Getter
    private UUID hunted;

    @Getter
    @Setter
    private int mapSize = 1000;

    private final SurvivalGame plugin;
    @Getter
    private final World world;
    private final Random random = new Random();

    private boolean started = false;

    private BorderTask borderTask;
    private BukkitTask task;
    private BukkitTask endTask;

    @Getter
    private boolean startingGodMode = false;

    public GameManager(SurvivalGame plugin, World world) {
        this.plugin = plugin;
        this.world = world;
        this.borderTask = new BorderTask(this.plugin);
    }

    public void setHunted(Player player) {
        this.hunted = player.getUniqueId();
    }

    public void start() throws NoHuntedException {
        if(this.hunted == null) {
            throw new NoHuntedException();
        }

        started = true;
        startingGodMode = true;

        WorldBorder worldBorder = this.world.getWorldBorder();
        worldBorder.setCenter(0, 0);
        worldBorder.setSize(this.mapSize);
        worldBorder.setDamageAmount(2);
        worldBorder.setDamageBuffer(1);
        worldBorder.setWarningDistance(10);
        worldBorder.setWarningTime(10);

        for(Player player : this.world.getPlayers()) {
            double maxDistance = 0.9 * (this.mapSize / 2);
            player.teleport(LocationUtils.findRandomSafeLocation(this.world, maxDistance));
        }

        this.task = this.borderTask.runTaskTimer(this.plugin,  600 * 20, 600 * 20); // run task every 10 minutes
        this.endTask = new BukkitRunnable() {
            @Override
            public void run() {
                GameManager.this.stop(false);
            }
        }.runTaskLater(this.plugin, 3600 * 20);

       /* new BukkitRunnable() {

            @Override
            public void run() {
                GameManager.this.startingGodMode = false;
            }
        }.runTaskLater(this.plugin, 10 * 20);*/

        plugin.getServer().broadcastMessage(ChatColor.GOLD + "The survival game has now stated !");
    }

    public void stop(boolean huntedDied) {
        started = false;
        this.task.cancel();
        this.endTask.cancel();
        this.world.getWorldBorder().reset();

        plugin.getServer().broadcastMessage(ChatColor.GOLD + "The survival game have ended !");

        if(huntedDied) {
            for(Player player : this.world.getPlayers()) {
                if(player.getUniqueId() == this.hunted) {
                    player.sendTitle(ChatColor.RED + "YOU LOSE", ChatColor.GOLD + "Hunters won !");
                } else {
                    player.sendTitle(ChatColor.GRAY + "YOU WON", ChatColor.GOLD + "Hunters won !");
                }
            }
        }
    }

    public boolean hasStarted() {
        return this.started;
    }
}
