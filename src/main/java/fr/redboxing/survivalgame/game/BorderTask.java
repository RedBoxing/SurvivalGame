package fr.redboxing.survivalgame.game;

import fr.redboxing.survivalgame.SurvivalGame;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.WorldBorder;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class BorderTask extends BukkitRunnable {
    private final SurvivalGame plugin;

    @Override
    public void run() {
        GameManager gameManager = this.plugin.getGameManager();

        if(gameManager.hasStarted()) {
            WorldBorder worldBorder = gameManager.getWorld().getWorldBorder();
            double toReduce = worldBorder.getSize() - (gameManager.getMapSize() / 10);
            if(worldBorder.getSize() > toReduce) {
                worldBorder.setSize(toReduce, (long) (toReduce / 2));
            }
        }

        this.plugin.getServer().broadcastMessage(ChatColor.RED + "The border is shrinking !");
    }
}
