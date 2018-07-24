package me.jet315.blockvalues.listeners;

import me.jet315.blockvalues.Core;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class InventoryClose implements Listener {

    @EventHandler
    public void onClose(InventoryCloseEvent e){
        for(Inventory inventory : Core.getInstance().getInventoryManager().getValidInventories().values()){
            if(inventory.equals(e.getInventory())) {
                Core.getInstance().getInventoryManager().getPlayerInventories().remove(e.getPlayer());
            }
            }
    }
}
