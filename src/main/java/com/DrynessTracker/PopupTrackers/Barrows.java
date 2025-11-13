package com.DrynessTracker.PopupTrackers;

import com.DrynessTracker.DrynessConfig;
import net.runelite.api.Client;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;

import java.util.ArrayList;

public class Barrows extends PopupRewardTrackerBase {

    private final ArrayList<Integer> _items;

    public Barrows(){
        _items = new ArrayList<>(24);

        _items.add(4708); // Ahrim's Hood Undamaged
        _items.add(4714); // Ahrim's Robeskirt Undamaged
        _items.add(4712); // Ahrim's Robotop Undamaged
        _items.add(4710); // Ahrim's Staff Undamaged

        _items.add(4716); // Dharok's Helm Undamaged
        _items.add(4720); // Dharok's Platebody Undamaged
        _items.add(4722); // Dharok's Platelegs Undamaged
        _items.add(4718); // Dharok's Greataxe Undamaged

        _items.add(4724); // Guthan's Helm Undamaged
        _items.add(4728); // Guthan's Platebody Undamaged
        _items.add(4730); // Guthan's Chainskirt Undamaged
        _items.add(4726); // Guthan's Warspear Undamaged

        _items.add(4732); // Karil's Coif Undamaged
        _items.add(4736); // Karil's Leathertop Undamaged
        _items.add(4738); // Karil's Leatherskirt Undamaged
        _items.add(4734); // Karil's Crossbow Undamaged

        _items.add(4745); // Torag's Helm Undamaged
        _items.add(4749); // Torag's Platebody Undamaged
        _items.add(4751); // Torag's Platelegs Undamaged
        _items.add(4747); // Torag's Hammers Undamaged

        _items.add(4753); // Verac's Helm Undamaged
        _items.add(4757); // Verac's Brassard Undamaged
        _items.add(4759); // Verac's Plateskirt Undamaged
        _items.add(4755); // Verac's Flail Undamaged
    }

    @Override
    public int GetInterfaceId() {
        return InterfaceID.BARROWS_REWARD;
    }

    @Override
    public int GetItemChestId() {
        return DrynessConfig.NO_ID_PROVIDED;
    }

    @Override
    public ArrayList<Integer> GetUniqueItemIds() {
        return _items;
    }

    @Override
    public String GetCongratualtionsPhrase() {
        return "Congrats on your new Barrows item.  Resetting count";
    }

    @Override
    public String GetConfigName() {
        return "barrowsinfo";
    }

    @Override
    public ArrayList<Integer> GetRewardItemIds(Client client) {
        var rewardPopup = client.getWidget(InterfaceID.BarrowsReward.ITEMS);

        // Barrows populates the interface items as children
        var items = rewardPopup.getChildren();
        var rewardIds = new ArrayList<Integer>(0);

        for (Widget item: items){
            rewardIds.add(item.getItemId());
        }

        return rewardIds;
    }
}
