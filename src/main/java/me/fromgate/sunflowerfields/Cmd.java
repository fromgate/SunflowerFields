package me.fromgate.sunflowerfields;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Cmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            Cfg.load();
            Farms.load();
            sender.sendMessage(ChatColor.GREEN + "Plugin settings reloaded");
            sender.sendMessage(ChatColor.GREEN + "Loaded " + Farms.getAmount() + " sunflowers");
        } else return false;
        return true;
    }

}
