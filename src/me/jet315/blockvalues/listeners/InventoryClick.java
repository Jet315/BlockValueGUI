package me.jet315.blockvalues.listeners;

import me.jet315.blockvalues.Core;
import me.jet315.blockvalues.inventory.InventoryAction;
import me.jet315.blockvalues.inventory.InventoryItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryClick implements Listener {

    @EventHandler
    public void inventoryClick(InventoryClickEvent e){
        for(Inventory inventory : Core.getInstance().getInventoryManager().getValidInventories().values()){
            if(inventory.equals(e.getClickedInventory())){
                e.setCancelled(true);
                e.setResult(Event.Result.DENY);
                Player p = (Player) e.getWhoClicked();
                for(InventoryItem item : Core.getInstance().getProperties().getInventoryItems().values()){
                    if(item.isInventoryItem(e.getCurrentItem())){
                        runCommand(p,item.getCommandToExecute());
                        if(item.getAction() == InventoryAction.FORWARD){
                            Core.getInstance().getInventoryManager().openNextPage(p);
                            return;
                        }else if(item.getAction() == InventoryAction.BACKWARD){
                            Core.getInstance().getInventoryManager().goBackPage(p);
                            return;
                        }else if(item.getAction() == InventoryAction.EXIT){
                            p.closeInventory();
                            return;
                        }
                    }
                }
            }
        }
    }

    private void runCommand(Player p, String command){
        if(command.equalsIgnoreCase("none")) return;
        Bukkit.getServer().getScheduler().runTaskLater(Core.getInstance(), new Runnable() {
            @Override
            public void run() {
                if(p != null && p.isOnline()){
                    p.performCommand(command);
                }
            }
        },1L);
    }
}
