package me.jet315.blockvalues;

import jdk.nashorn.internal.ir.Block;
import me.jet315.blockvalues.commands.CommandHandler;
import me.jet315.blockvalues.inventory.InventoryManager;
import me.jet315.blockvalues.listeners.InventoryClick;
import me.jet315.blockvalues.listeners.InventoryClose;
import me.jet315.blockvalues.utils.Properties;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin {

    /**
     * The plugin
     */
    private static Core instance;
    //Only needed while starting the plugin
    private BlockValuesData blockValuesData;
    //properties
    private Properties properties;
    //inventory manager
    private InventoryManager inventoryManager;

    public void onEnable() {
        instance = this;
        blockValuesData = new BlockValuesData();
        properties = new Properties();
        inventoryManager = new InventoryManager();

        //listeners
        Bukkit.getServer().getPluginManager().registerEvents(new InventoryClick(),this);
        Bukkit.getServer().getPluginManager().registerEvents(new InventoryClose(),this);
        //comands
        getCommand("blockvalue").setExecutor(new CommandHandler());

        blockValuesData = null;
    }

    public void onDisable() {

    }

    public void reloadProperties(){
        for(Player p : inventoryManager.getPlayerInventories().keySet()){
            if(p != null && p.isOnline()){
                p.closeInventory();
                p.sendMessage(properties.getPluginPrefix() + "&cThe plugin was reloaded.");
            }
        }
        properties = null;
        inventoryManager = null;

        blockValuesData = new BlockValuesData();
        properties = new Properties();
        inventoryManager = new InventoryManager();

        //Not needed
        blockValuesData = null;
    }
    public static Core getInstance() {
        return instance;
    }

    public BlockValuesData getBlockValuesData() {
        return blockValuesData;
    }

    public Properties getProperties() {
        return properties;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }
}
