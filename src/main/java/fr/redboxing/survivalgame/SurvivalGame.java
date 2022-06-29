package fr.redboxing.survivalgame;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import fr.redboxing.survivalgame.commands.SurvivalCommand;
import fr.redboxing.survivalgame.game.GameListener;
import fr.redboxing.survivalgame.game.GameManager;
import lombok.Getter;
import me.lucko.commodore.Commodore;
import me.lucko.commodore.CommodoreProvider;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class SurvivalGame extends JavaPlugin {
    @Getter
    private GameManager gameManager;

    @Override
    public void onEnable() {
        this.gameManager = new GameManager(this, getServer().getWorld("world"));
        getServer().getPluginManager().registerEvents(new GameListener(this), this);

        registerCommand("survivalgame", new SurvivalCommand(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerCommand(String cmd, CommandExecutor executor, JavaPlugin plugin) {
        PluginCommand command = plugin.getCommand(cmd);
        command.setExecutor(executor);

        if(CommodoreProvider.isSupported()) {
            Commodore commodore = CommodoreProvider.getCommodore(plugin);

            commodore.register(command, LiteralArgumentBuilder.literal("survivalgame")
                    .then(LiteralArgumentBuilder.literal("hunted")
                            .then(RequiredArgumentBuilder.argument("player", StringArgumentType.string())))
                    .then(LiteralArgumentBuilder.literal("start"))
                    .then(LiteralArgumentBuilder.literal("stop"))
            );
        }
    }
}
