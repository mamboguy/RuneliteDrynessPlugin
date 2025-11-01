package com.DrynessTracker.PopupTrackers;

import net.runelite.api.Client;
import net.runelite.api.Item;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.InventoryID;
import net.runelite.api.widgets.Widget;

import java.util.ArrayList;

public class PerilousMoons extends PopupRewardTrackerBase {

    private final ArrayList<Integer> _items;

    public PerilousMoons(){
        _items = new ArrayList<>(12);

        _items.add(29019); // Blue moon helm
        _items.add(29013); // Blue moon chestplate
        _items.add(29016); // Blue moon tassets
        _items.add(28988); // Blue moon spear

        _items.add(29028); // Blood moon helm
        _items.add(29022); // Blood moon chestplate
        _items.add(29025); // Blood moon tassets
        _items.add(28997); // Dual macuahuitl

        _items.add(29010); // Eclipse moon helm
        _items.add(29004); // Eclipse moon chestplate
        _items.add(29007); // Eclipse moon tassets
        _items.add(29000); // Eclipse atlatl
    }

    @Override
    public int GetInterfaceId() {
        return InterfaceID.PMOON_REWARD;
    }

    @Override
    public ArrayList<Integer> GetUniqueItemIds() {
        return _items;
    }

    @Override
    public String GetCongratualtionsPhrase() {
        return "Congrats on your new Moons item.  Resetting count";
    }

    @Override
    public String GetConfigName() {
        return "perilousmoons";
    }

    @Override
    public ArrayList<Integer> GetRewardItemIds(Client client) {
        var itemContainer = client.getItemContainer(InventoryID.PMOON_REWARDINV);
        var items = itemContainer.getItems();
        var rewardIds = new ArrayList<Integer>(0);

        for (Item item : items){
            rewardIds.add(item.getId());
        }

        return rewardIds;
    }
}
