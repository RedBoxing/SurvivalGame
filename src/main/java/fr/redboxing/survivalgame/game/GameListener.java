package fr.redboxing.survivalgame.game;

import fr.redboxing.survivalgame.SurvivalGame;
import fr.redboxing.survivalgame.utils.LocationUtils;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

@RequiredArgsConstructor
public class GameListener implements Listener {
    private final SurvivalGame plugin;

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {
        if(!this.plugin.getGameManager().hasStarted()) return;
        if(event.getEntity().getUniqueId() == this.plugin.getGameManager().getHunted()) {
            this.plugin.getGameManager().stop(true);
        }
    }
}
