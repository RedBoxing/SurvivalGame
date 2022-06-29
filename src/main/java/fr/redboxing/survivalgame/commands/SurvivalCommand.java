package fr.redboxing.survivalgame.commands;

import fr.redboxing.survivalgame.SurvivalGame;
import fr.redboxing.survivalgame.exceptions.NoHuntedException;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

@RequiredArgsConstructor
public class SurvivalCommand implements CommandExecutor, TabExecutor {
    private final SurvivalGame plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args[0]) {
            case "start":
                try {
                    plugin.getGameManager().start();
                } catch (NoHuntedException ex) {
                    sender.sendMessage(ChatColor.RED + " No hunted set !");
                }
                break;
            case "stop":
                plugin.getGameManager().stop(false);
                break;
            case "hunted":
                Player player = Bukkit.getPlayer(args[1]);
                if(player == null) {
                    sender.sendMessage(ChatColor.RED + " Player not found !");
                    return true;
                }

                plugin.getGameManager().setHunted(player);
                sender.sendMessage(ChatColor.GREEN + player.getName() + " set to hunted");
                break;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
