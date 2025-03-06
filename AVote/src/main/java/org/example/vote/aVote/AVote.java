package org.example.vote.aVote;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;
import com.vexsoftware.votifier.model.VotifierEvent;
import com.vexsoftware.votifier.model.Vote;
import org.bukkit.entity.Player;
import java.util.List;

public final class AVote extends JavaPlugin implements CommandExecutor, Listener {

    private FileConfiguration config;

    @Override
    public void onEnable() {
        this.saveDefaultConfig(); // Varsayılan config dosyasını oluştur
        this.config = this.getConfig();
        this.getCommand("vote").setExecutor(this);
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin kapatıldığında yapılacak işlemler
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("vote")) {
            String prefix = ChatColor.translateAlternateColorCodes('&', config.getString("prefix", "&6[Vote]"));
            sender.sendMessage(prefix + " " + ChatColor.GREEN + "Vote using the links below:");

            for (String key : config.getConfigurationSection("vote-links").getKeys(false)) {
                sender.sendMessage(ChatColor.AQUA + config.getString("vote-links." + key));
            }
            return true;
        }
        return false;
    }

    @EventHandler
    public void onVote(VotifierEvent event) {
        Vote vote = event.getVote();
        Player player = Bukkit.getPlayer(vote.getUsername());
        if (player != null) {
            String prefix = ChatColor.translateAlternateColorCodes('&', config.getString("prefix", "&6[Vote]"));
            String voteMessage = ChatColor.translateAlternateColorCodes('&', config.getString("vote-message", "&aThank you for voting! Here are your rewards:"));

            player.sendMessage(prefix + " " + voteMessage);

            List<String> rewardCommands = config.getStringList("reward-commands");
            for (String command : rewardCommands) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
            }
        }
    }
}