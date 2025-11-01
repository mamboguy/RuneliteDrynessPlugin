package com.DrynessTracker.PopupTrackers;

import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;

import java.util.ArrayList;

public interface IPopupRewardTracker {

    /**
     * Interface id for the overall reward window
     */
    public int GetInterfaceId();

    /**
     * The list of items to search for in the inventory
     * Full list at:  https://oldschool.runescape.wiki/w/Item_IDs
     */
    public ArrayList<Integer> GetUniqueItemIds();

    /**
     * Unique message congratulating player on their drop
     */
    public String GetCongratualtionsPhrase();

    /**
     * Unique message for double or better drops
     */
    public String GetGZMessage();

    public void IncrementDryCount();

    public void ResetDryCount();

    public String GetConfigName();

    public void SetDryCount(int value);

    public int GetDryCount();

    public ArrayList<Integer> GetRewardItemIds(Client client);
}
