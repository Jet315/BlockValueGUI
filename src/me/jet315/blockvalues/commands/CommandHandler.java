package me.jet315.blockvalues.commands;

import me.jet315.blockvalues.Core;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (cmd.getName().equalsIgnoreCase("blockvalue")) {
            if (args.length == 0) {
                if(sender instanceof Player) {
                    //perm check
                    Player p = (Player) sender;
                    if(p.hasPermission("blockvaluegui.menu")) {
                        //Open the GUI
                        p.openInventory(Core.getInstance().getInventoryManager().getValidInventories().get(1));
                        Core.getInstance().getInventoryManager().setPlayerInInventory(p, 1);
                        return true;
                    }else{
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + "&cNo Permissions."));
                    }

                }else{
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + "&cMust be a Player!"));
                    return true;
                }
            }
            if(args.length == 1 && args[0].equalsIgnoreCase("reload")){
                if(sender.hasPermission("blockvaluegui.menu")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + "&aStarting Reload"));
                long startTime = System.currentTimeMillis();
                Core.getInstance().reloadProperties();
                long endtime = System.currentTimeMillis() - startTime;
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + "&aReload Complete: &6" + endtime + "ms"));
                }else{
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + "&cNo Permissions."));
                }
            }

        }
        return true;
    }
}
