package com.DrynessTracker.PopupTrackers;

import net.runelite.api.Client;
import net.runelite.api.Item;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.InventoryID;

import java.util.ArrayList;

public class FishingTrawler extends PopupRewardTrackerBase{

    private ArrayList<Integer> _items;

    public FishingTrawler(){
        _items = new ArrayList<Integer>(0);

        _items.add(13258); // Angler hat
        _items.add(13259); // Angler top
        _items.add(13260); // Angler waders
        _items.add(13261); // Angler boots
    }

    @Override
    public int GetInterfaceId() {
        return InterfaceID.TRAWLER_REWARD;
    }

    @Override
    public ArrayList<Integer> GetUniqueItemIds() {
        return _items;
    }

    @Override
    public String GetCongratualtionsPhrase() {
        return "Angler outfit";
    }

    @Override
    public String GetConfigName() {
        return "fishingtrawler";
    }

    @Override
    public ArrayList<Integer> GetRewardItemIds(Client client) {
        var itemContainer = client.getItemContainer(InventoryID.TRAWLER_REWARDINV);
        var items = itemContainer.getItems();
        var rewardIds = new ArrayList<Integer>(0);

        for (Item item : items){
            rewardIds.add(item.getId());
        }

        return rewardIds;
    }
}
