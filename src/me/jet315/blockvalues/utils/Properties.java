package me.jet315.blockvalues.utils;

import me.jet315.blockvalues.Core;
import me.jet315.blockvalues.inventory.InventoryAction;
import me.jet315.blockvalues.inventory.InventoryItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Properties {

    private String pluginPrefix = "BlockValues > ";
    private String guiName = "Blocks";
    private BlockOrder blockOrder = BlockOrder.ASCENDING;

    private HashMap<InventoryAction,InventoryItem> inventoryItems = new HashMap<>();

    private String blockDisplayName = "%NAME%";
    private ArrayList<String> lore = new ArrayList<>();
    private String valueFormat = "#.##";


    public Properties() {
        createPluginConfig();
        loadValues();

    }

    private void loadValues() {
        //config file
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(Core.getInstance().getDataFolder(), "config.yml"));
        /**
         * A few options
         */
        pluginPrefix = ChatColor.translateAlternateColorCodes('&', config.getString("PluginsPrefix"));
        guiName = ChatColor.translateAlternateColorCodes('&', config.getString("GUIName"));
        blockOrder = BlockOrder.valueOf(config.getString("ArrangeBlockOrder"));
       blockDisplayName = ChatColor.translateAlternateColorCodes('&',config.getString("BlockDisplayName"));
        lore = (ArrayList<String>) config.getStringList("BlockLore");
        valueFormat = config.getString("ValueFormat");

        //Apply formatting
        if (lore != null && lore.size() > 0) {
            ArrayList<String> formattedLore = new ArrayList<>();
            for (String lineInLore : lore) {
                formattedLore.add(ChatColor.translateAlternateColorCodes('&', lineInLore));
            }
            lore = formattedLore;
        }

        //Loop through the different GUI items
        for (String guiName : config.getConfigurationSection("GUIItems").getKeys(false)) {
            try {
                //Get the path as a string, so it is easy to get future values from the config
                String path = "GUIItems." + guiName;

                //Gets the material
                Material material = Material.valueOf(config.getString(path + ".material"));

                //If they enter an invalid material, this wil catch it
                if (material == null) {
                    System.out.println("The material " + config.getStringList(path + ".material") + " is invalid");
                    continue;
                }
                //material data
                short data = (short) config.getInt(path + ".data");

                //create the itemstack
                ItemStack guiItem = new ItemStack(material,1,data);
                ItemMeta guiItemMeta = guiItem.getItemMeta();

                //sets the displayname
                guiItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',config.getString(path+".displayName")));

                //Getting the lore, however it will not be formatted correctly, this formats each line
                List<String> lore = config.getStringList(path + ".lore");
                ArrayList<String> formattedLore = new ArrayList<>();
                if (lore != null && lore.size() > 0) {
                    for (String lineInLore : lore) {
                        formattedLore.add(ChatColor.translateAlternateColorCodes('&', lineInLore));
                    }
                    guiItemMeta.setLore(formattedLore);
                }

                if(config.getBoolean(path+".glow")){
                    guiItemMeta.addEnchant(Enchantment.SILK_TOUCH, 1, false);
                    guiItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }
                guiItem.setItemMeta(guiItemMeta);

                boolean alwaysShow = config.getBoolean(path+".alwaysShow");
                String command = config.getString(path+".commandToPerformWhenClicked");
                int id = config.getInt(path+".slotID");
                InventoryAction inventoryAction = InventoryAction.FILLER;

                if(guiName.equalsIgnoreCase("backpageitem")){
                    inventoryAction = InventoryAction.BACKWARD;
                }else if(guiName.equalsIgnoreCase("forwardpageitem")){
                    inventoryAction = InventoryAction.FORWARD;
                }else if(guiName.equalsIgnoreCase("exitinventoryitem")){
                    inventoryAction = InventoryAction.EXIT;
                }

                InventoryItem item = new InventoryItem(guiItem,command,alwaysShow,inventoryAction,id);
                inventoryItems.put(inventoryAction,item);


            } catch (Exception e) {
                System.out.println("[BlockValues] Error loading GUIItem: " + guiName);
            }
        }


    }

    private void createPluginConfig() {
        try {
            Core instance = Core.getInstance();
            if (!instance.getDataFolder().exists()) {
                instance.getDataFolder().mkdirs();
            }
            File file = new File(instance.getDataFolder(), "config.yml");
            if (!file.exists()) {
                instance.getLogger().info("Config.yml not found, creating!");
                instance.saveDefaultConfig();
            } else {
                instance.getLogger().info("Config.yml found, loading!");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public String getPluginPrefix() {
        return pluginPrefix;
    }

    public String getGuiName() {
        return guiName;
    }

    public BlockOrder getBlockOrder() {
        return blockOrder;
    }

    public HashMap<InventoryAction,InventoryItem> getInventoryItems() {
        return inventoryItems;
    }


    public ArrayList<String> getLore() {
        return lore;
    }

    public String getValueFormat() {
        return valueFormat;
    }

    public String getBlockDisplayName() {
        return blockDisplayName;
    }
}
