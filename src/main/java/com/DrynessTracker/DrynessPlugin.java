package com.DrynessTracker;

import com.DrynessTracker.ItemSpawnTrackers.IItemSpawnTracker;
import com.DrynessTracker.PopupTrackers.Barrows;
import com.DrynessTracker.PopupTrackers.FishingTrawler;
import com.DrynessTracker.PopupTrackers.IPopupRewardTracker;
import com.DrynessTracker.PopupTrackers.PerilousMoons;
import com.google.gson.Gson;
import com.google.inject.Provides;

import javax.inject.Inject;
import javax.swing.text.html.Option;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.ItemSpawned;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@PluginDescriptor(
        name = "Barrows Dry Tracker"
)
public class DrynessPlugin extends Plugin {
    private static final String PLUGIN_NAME = "Dryness Tracker - ";
    private static final boolean DEBUG = false;

    @Inject
    private Client _client;

    @Inject
    private ChatMessageManager _chatMessageManager;

    @Inject
    private Gson _gson;

    @Inject
    ConfigManager _configManager;

    @Inject
    DrynessConfig _config;

    private ArrayList<IPopupRewardTracker> _popupTrackers;
    private ArrayList<IItemSpawnTracker> _itemSpawnTrackers;

    @Override
    protected void startUp() throws Exception {
        _popupTrackers = new ArrayList<>(0);
        _itemSpawnTrackers = new ArrayList<>(0);

        if (_config.BarrowsTrack()) {
            _popupTrackers.add(new Barrows());
        }

        if (_config.MoonsTrack()) {
            _popupTrackers.add(new PerilousMoons());
        }

        if (_config.FishingTrawlerTrack()) {
            _popupTrackers.add(new FishingTrawler());
        }

        for (IPopupRewardTracker currentTracker : _popupTrackers) {

            var savedData = _configManager.getConfiguration(DrynessConfig.GROUP, currentTracker.GetConfigName());
            var dryCount = _gson.fromJson(savedData, Integer.class);

            if (dryCount == null) {
                dryCount = 0;
            }

            currentTracker.SetDryCount(dryCount);
        }
    }

    @Override
    protected void shutDown() throws Exception {
        _popupTrackers.clear();
        _itemSpawnTrackers.clear();
    }

//    @Subscribe
//    public void onItemSpawned(ItemSpawned itemSpawned){
//        for (IItemSpawnTracker curr : _itemSpawnTrackers) {
//
//        }
//    }

    @Subscribe
    public void onWidgetLoaded(WidgetLoaded widgetLoaded) {

        if (_config.ShowWidgetId()) {
            SendMessage("Widget ID: " + widgetLoaded.getGroupId());
        }

        for (IPopupRewardTracker curr : _popupTrackers) {
            if (widgetLoaded.getGroupId() == curr.GetInterfaceId()) {
                var uniqueCount = 0;
                var noLongerDry = false;

                DebugShowItemSpawnType(curr.GetInterfaceId(), curr.GetItemChestId());

                var rewards = curr.GetRewardItemIds(_client);

                if (rewards == null) {
                    continue;
                }

                for (Integer item : rewards) {
                    if (_config.ShowItemIds()) {
                        SendMessage("ItemId - " + item);
                    }

                    if (curr.GetUniqueItemIds().contains(item)) {
                        noLongerDry = true;
                        uniqueCount++;
                    }
                }

                if (noLongerDry) {
                    if (uniqueCount > 1) {
                        SendMessage(PLUGIN_NAME + curr.GetGZMessage());
                    } else {
                        SendMessage(PLUGIN_NAME + curr.GetCongratualtionsPhrase());
                    }

                    curr.ResetDryCount();
                } else {
                    // Increment dry counter
                    curr.IncrementDryCount();

                    var plural = curr.GetDryCount() == 1 ? "" : "s";
                    SendMessage(PLUGIN_NAME + "You've gone dry " + curr.GetDryCount() + " time" + plural);
                }

                var data = _gson.toJson(curr.GetDryCount());

                _configManager.setConfiguration(DrynessConfig.GROUP, curr.GetConfigName(), data);
            }
        }
    }

    private void DebugShowItemSpawnType(int widgetId, int itemChestId) {
        if (!_config.ShowItemSpawnType()) {
            return;
        }

        if (widgetId != DrynessConfig.NO_ID_PROVIDED) {
            // Check to see if like Barrows and the items are part of the reward widget
            var widgetChildren = _client.getWidget(widgetId).getChildren();

            var widgetChildrenCount = -1;

            if (widgetChildren != null) {
                widgetChildrenCount = widgetChildren.length;
            }
            SendMessage("Widget Children Count: " + widgetChildrenCount);
        } else {
            SendMessage("Did not check widget children");
        }

        if (itemChestId != DrynessConfig.NO_ID_PROVIDED) {

            // Check to see if like Moons and part of the item chest
            var itemChest = _client.getItemContainer(itemChestId);


            var itemChestCount = -1;

            if (itemChest != null) {
                itemChestCount = itemChest.getItems().length;
            }

            SendMessage("Item Chest Count: " + itemChestCount);
        } else {
            SendMessage("Did not check item chest");
        }
    }

    private void SendMessage(String message) {
        _chatMessageManager.queue(
                QueuedMessage.builder()
                        .type(ChatMessageType.CONSOLE)
                        .runeLiteFormattedMessage(new ChatMessageBuilder()
                                .append(ChatColorType.HIGHLIGHT)
                                .append(message)
                                .build())
                        .build()
        );
    }

    @Provides
    DrynessConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(DrynessConfig.class);
    }
}
