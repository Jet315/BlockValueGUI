package me.jet315.blockvalues.inventory;

import org.bukkit.inventory.ItemStack;

public class InventoryItem {

    private ItemStack itemStack;
    private String commandToExecute;
    private boolean showAlways;
    private InventoryAction action;
    private int slotID;

    public InventoryItem(ItemStack getItem, String commandToExecute, boolean showAlways, InventoryAction action, int slotID) {
        this.itemStack = getItem;
        this.commandToExecute = commandToExecute;
        this.showAlways = showAlways;
        this.action = action;
        this.slotID = slotID;
    }

    public ItemStack getGetItem() {
        return itemStack;
    }

    public String getCommandToExecute() {
        return commandToExecute;
    }

    public boolean isShowAlways() {
        return showAlways;
    }

    public InventoryAction getAction() {
        return action;
    }

    public boolean isInventoryItem(ItemStack theSubject){
        return itemStack.isSimilar(theSubject);
    }

    public int getSlotID() {
        return slotID;
    }
}
