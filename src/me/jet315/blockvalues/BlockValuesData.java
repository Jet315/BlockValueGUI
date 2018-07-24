package me.jet315.blockvalues;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class BlockValuesData {

    private int levelCost;

    private HashMap<ItemStack,Integer> blockList = new HashMap<>();

    private int amountOfBlocks = 0;

    public BlockValuesData(){
        loadData();
    }

    private void loadData(){
        File skyBlockYML = new File(Core.getInstance().getDataFolder()+"/../ASkyBlock/blockvalues.yml");
        FileConfiguration skyblockConfig = YamlConfiguration.loadConfiguration(skyBlockYML);

        //load Level Cost
        levelCost = skyblockConfig.getInt("levelcost");

        ArrayList<String> list = new ArrayList<>();
        for(String item : skyblockConfig.getConfigurationSection("blocks").getKeys(false)){
            list.add(item + ":" + skyblockConfig.getString("blocks."+item));
        }

        for(String line : list){
            try {
                String lineSplit[] = line.split(":");
                if (lineSplit.length >= 3) {
                    blockList.put(new ItemStack(Material.valueOf(lineSplit[0]), 1, Short.valueOf(lineSplit[1])), (int) Math.round(Double.valueOf(lineSplit[1])));
                } else if (lineSplit.length >= 2) {
                    blockList.put(new ItemStack(Material.valueOf(lineSplit[0])), (int) Math.round(Double.valueOf(lineSplit[1])));
                } else {
                    System.out.println("[BlockValuesGUI] error with value " + line);
                    continue;
                }
                amountOfBlocks++;

            }catch (Exception e){
                System.out.println("[BlockValuesGUI] error with value " + line);

            }
            }

    }

    public int getLevelCost() {
        return levelCost;
    }

    public HashMap<ItemStack, Integer> getBlockList() {
        return blockList;
    }

    public int getAmountOfBlocks() {
        return amountOfBlocks;
    }
}
