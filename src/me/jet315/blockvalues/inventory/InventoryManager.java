package me.jet315.blockvalues.inventory;

import me.jet315.blockvalues.Core;
import me.jet315.blockvalues.utils.BlockOrder;
import me.jet315.blockvalues.utils.ItemNames;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class InventoryManager {

    //Stores players that are currently in the inventory and the page they are in
    private HashMap<Player,Integer> playerInventories = new HashMap<>();

    //stores the Inventory and the number of the inventory
    private HashMap<Integer,Inventory> validInventories = new HashMap<>();

    //stores the total number of inventories
    private int inventories = 0;

    public InventoryManager(){
        setupInventories();
    }


    private void setupInventories(){
        Core instance = Core.getInstance();

        int amountOfInventories = (int) Math.ceil(instance.getBlockValuesData().getAmountOfBlocks() / 45.0);
        Map<ItemStack, Integer> sortedMap;

        if(instance.getProperties().getBlockOrder() == BlockOrder.ASCENDING) {
            sortedMap = sortHashMap(instance.getBlockValuesData().getBlockList(), false);
        }else if(instance.getProperties().getBlockOrder() == BlockOrder.DESCENDING) {
            sortedMap = sortHashMap(instance.getBlockValuesData().getBlockList(), true);
        }else if(instance.getProperties().getBlockOrder() == BlockOrder.NAMES){
            //treemap will not work for ItemStack :(
            sortedMap = instance.getBlockValuesData().getBlockList().entrySet().stream()
                    .sorted(Comparator.comparing(e -> e.getKey().getType().toString()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        }else{
            sortedMap = instance.getBlockValuesData().getBlockList();
        }
        //sort block value in order
        HashMap<InventoryAction,InventoryItem> inventoryItems = instance.getProperties().getInventoryItems();
        //To format values
        DecimalFormat formatter = new DecimalFormat(Core.getInstance().getProperties().getValueFormat());
        for(int i = 1; i <= amountOfInventories ; i++){
            //create the inventory
            Inventory inventory = Bukkit.createInventory(null,54,instance.getProperties().getGuiName().replaceAll("%PAGE%",String.valueOf(i)).replaceAll("%PAGES%",String.valueOf(amountOfInventories)));
            //put the predefined items in the inventory

            //Go back item
            InventoryItem backItem = inventoryItems.get(InventoryAction.BACKWARD);
            if(backItem != null) {
                if (i != 1 || (i == 1 && backItem.isShowAlways())) {
                    inventory.setItem(backItem.getSlotID()-1,backItem.getGetItem());
                }
            }

            //Go forward item
            InventoryItem forwardItem = inventoryItems.get(InventoryAction.FORWARD);
            if(forwardItem != null){
                if(i != amountOfInventories || (i == amountOfInventories && forwardItem.isShowAlways())){
                    inventory.setItem(forwardItem.getSlotID()-1,forwardItem.getGetItem());
                }
            }
            //Exit item
            InventoryItem exitItem = inventoryItems.get(InventoryAction.EXIT);
            if(exitItem != null && exitItem.isShowAlways()){
                inventory.setItem(exitItem.getSlotID()-1,exitItem.getGetItem());

            }

            //Filler item
            InventoryItem fillerItem = inventoryItems.get(InventoryAction.FILLER);
            if(fillerItem != null && fillerItem.isShowAlways()){
                for(int j = 44; j <= 53 ; j++){
                    if(inventory.getItem(j) == null || inventory.getItem(j).getType() == Material.AIR) {
                        inventory.setItem(j, fillerItem.getGetItem());
                    }
                }
            }
            //cant go above 44
            int amountAdded = 0;
            //iterator?
            int counter = 0;
            for (Map.Entry<ItemStack, Integer> entry : sortedMap.entrySet()) {
                if(i > 1){
                    if(counter < 45 * (i-1)){
                        counter++;
                        continue;
                    }
                }
                if(amountAdded == 45){
                    break;
                }
                ItemStack item = entry.getKey().clone();
                if(item.getType() == Material.AIR) continue;


                ItemMeta itemMeta = item.getItemMeta();
                String value = "0";
                if(entry.getValue() > 0){
                    value = formatter.format(((double) entry.getValue() / Core.getInstance().getBlockValuesData().getLevelCost()));
                }

                itemMeta.setDisplayName(instance.getProperties().getBlockDisplayName().replaceAll("%NAME%",ItemNames.lookup(item)).replaceAll("%VALUE%",value));
                ArrayList<String> lore = new ArrayList<>();
                for(String s : instance.getProperties().getLore()){
                    lore.add(s.replaceAll("%NAME%",itemMeta.getDisplayName().replaceAll("_", " ")).replaceAll("%VALUE%",value));
                }
                itemMeta.setLore(lore);
                item.setItemMeta(itemMeta);
                item.setType(alternativeType(item.getType()));
                inventory.setItem(amountAdded,item);
                //sortedMap.remove(entry.getKey(),entry.getValue());
                amountAdded++;
            }
            validInventories.put(i,inventory);
        }
    }


    private Material alternativeType(Material material){
        switch (material){
            case BURNING_FURNACE:
                return Material.FURNACE;
            case SKULL:
                return Material.SKULL_ITEM;
            case DAYLIGHT_DETECTOR_INVERTED:
                return Material.DAYLIGHT_DETECTOR;
            case CAULDRON:
                return Material.CAULDRON_ITEM;
            case DIODE_BLOCK_ON:
                return Material.DIODE;
            case FLOWER_POT:
                return Material.FLOWER_POT_ITEM;
            case DIODE_BLOCK_OFF:
                return Material.DIODE;
            case BREWING_STAND:
                return Material.BREWING_STAND_ITEM;
            case PURPUR_DOUBLE_SLAB:
                return Material.PURPUR_BLOCK;
            case REDSTONE_COMPARATOR_OFF:
                return Material.REDSTONE_COMPARATOR;
            case REDSTONE_COMPARATOR_ON:
                return Material.REDSTONE_COMPARATOR;
            case TRIPWIRE:
                return Material.TRIPWIRE_HOOK;
            case IRON_DOOR_BLOCK:
                return Material.IRON_DOOR;
            case MELON_STEM:
                return Material.MELON_SEEDS;
            case DOUBLE_STONE_SLAB2:
                return Material.REDSTONE;
            case COCOA:
                return Material.INK_SACK;
            case DARK_OAK_DOOR:
                return Material.DARK_OAK_DOOR_ITEM;
            case SUGAR_CANE_BLOCK:
                return Material.SUGAR_CANE;
            case WALL_SIGN:
                return Material.SIGN;
            case BEETROOT_BLOCK:
                return Material.BEETROOT;
            case SPRUCE_DOOR:
                return Material.SPRUCE_DOOR_ITEM;
            case SIGN_POST:
                return Material.SIGN;
            case BED_BLOCK:
                return Material.BED;
            case DOUBLE_STEP:
                return Material.STONE;
            case BIRCH_DOOR:
                return Material.BIRCH_DOOR_ITEM;
            case REDSTONE_TORCH_OFF:
                return Material.REDSTONE_TORCH_ON;
            case WOODEN_DOOR:
                return Material.WOOD_DOOR;
            case CAKE_BLOCK:
                return Material.CAKE;
            case FROSTED_ICE:
                return Material.ICE;
            case WOOD_DOUBLE_STEP:
                return Material.WOOD_STEP;
            case PUMPKIN_STEM:
                return Material.PUMPKIN_SEEDS;
            case JUNGLE_DOOR:
                return Material.JUNGLE_DOOR_ITEM;
            case FIRE:
                return Material.FIREBALL;
            case WATER_BUCKET:
                return Material.WATER;
            case PORTAL:
                return Material.ENDER_PORTAL_FRAME;
            case LAVA:
                return Material.LAVA_BUCKET;
            case ENDER_PORTAL:
                return Material.ENDER_PORTAL_FRAME;
            case STATIONARY_LAVA:
                return Material.LAVA_BUCKET;
            case STATIONARY_WATER:
                return Material.WATER_BUCKET;
            case REDSTONE_WIRE:
                return Material.REDSTONE;
            case REDSTONE_LAMP_ON:
                return Material.REDSTONE_LAMP_OFF;
            case ACACIA_DOOR:
                return Material.ACACIA_DOOR_ITEM;

        }
        return material;
    }




    private HashMap<ItemStack,Integer> sortHashMap(HashMap<ItemStack,Integer> unsortMap, final boolean order){

        List<Map.Entry<ItemStack, Integer>> list = new LinkedList<Map.Entry<ItemStack, Integer>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Map.Entry<ItemStack, Integer>>() {
            public int compare(Map.Entry<ItemStack, Integer> o1,
                               Map.Entry<ItemStack, Integer> o2) {
                if (order) {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        HashMap<ItemStack, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<ItemStack, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), Integer.valueOf(entry.getValue()));
        }

        return sortedMap;

    }

    public void openNextPage(Player p){
        if(!this.playerInventories.containsKey(p)){
            p.openInventory(validInventories.get(1));
            setPlayerInInventory(p,1);
            return;
        }
        int currentInventory = this.playerInventories.get(p);
        if(currentInventory == inventories) return;

        p.openInventory(validInventories.get(currentInventory+1));
        setPlayerInInventory(p,currentInventory+1);
    }

    public void goBackPage(Player p){
        if(!this.playerInventories.containsKey(p)){
            p.openInventory(validInventories.get(1));
            setPlayerInInventory(p,1);
            return;
        }
        int currentInventory = this.playerInventories.get(p);
        if(currentInventory == 1) return;

        p.openInventory(validInventories.get(currentInventory-1));
        setPlayerInInventory(p,currentInventory-1);
    }

    public void setPlayerInInventory(Player p, int page){
        if(this.playerInventories.containsKey(p)){
            this.playerInventories.remove(p);
        }
        this.playerInventories.put(p,page);
    }

    public HashMap<Player, Integer> getPlayerInventories() {
        return playerInventories;
    }

    public HashMap<Integer, Inventory> getValidInventories() {
        return validInventories;
    }
}
