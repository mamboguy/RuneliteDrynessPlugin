package com.DrynessTracker;

import com.DrynessTracker.ItemSpawnTrackers.IItemSpawnTracker;
import com.DrynessTracker.PopupTrackers.Barrows;
import com.DrynessTracker.PopupTrackers.FishingTrawler;
import com.DrynessTracker.PopupTrackers.IPopupRewardTracker;
import com.DrynessTracker.PopupTrackers.PerilousMoons;
import com.google.gson.Gson;
import com.google.inject.Provides;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
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

@Slf4j
@PluginDescriptor(
	name = "Barrows Dry Tracker"
)
public class DrynessPlugin extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(DrynessPlugin.class);
    private static final String PLUGIN_NAME = "Dryness Tracker - ";

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

    @Getter
    @Inject
    @Named("developerMode")
    private boolean developerMode;

    private ArrayList<IPopupRewardTracker> _popupTrackers;
    private ArrayList<IItemSpawnTracker> _itemSpawnTrackers;

    @Override
    protected void startUp() throws Exception {
        _popupTrackers = new ArrayList<>(0);
        _itemSpawnTrackers = new ArrayList<>(0);

        if (_config.BarrowsTrack()) {
            SendDebugMessage("Loaded Barrows");
            _popupTrackers.add(new Barrows());
        }

        if (_config.MoonsTrack()) {
            SendDebugMessage("Loaded Moons");
            _popupTrackers.add(new PerilousMoons());
        }

        if (_config.FishingTrawlerTrack()) {
            SendDebugMessage("Loaded Trawler");
            _popupTrackers.add(new FishingTrawler());
        }

        for (IPopupRewardTracker currentTracker : _popupTrackers) {

            var savedData = _configManager.getConfiguration(DrynessConfig.GROUP, currentTracker.GetConfigName());
            var dryCount = _gson.fromJson(savedData, Integer.class);

            if (dryCount == null) {
                dryCount = 0;
            }

            currentTracker.SetDryCount(dryCount);

            SendDebugMessage("Loaded " + dryCount + " for " + currentTracker.GetConfigName());
        }
    }

    @Override
    protected void shutDown() throws Exception {
        _popupTrackers.clear();
        _itemSpawnTrackers.clear();
    }

    @Subscribe
    public void onItemSpawned(ItemSpawned itemSpawned){
//        for (IItemSpawnTracker curr : _itemSpawnTrackers) {
//
//        }
    }

    @Subscribe
    public void onWidgetLoaded(WidgetLoaded widgetLoaded){

        SendDebugMessage("Widget ID: " + widgetLoaded.getGroupId());

        for (IPopupRewardTracker curr : _popupTrackers){
            if (widgetLoaded.getGroupId() == curr.GetInterfaceId()){
                var uniqueCount = 0;
                var noLongerDry = false;

                SendDebugMessage("Found widget match");

                var rewards = curr.GetRewardItemIds(_client);

                if (rewards == null) {
                    SendDebugMessage("Item rewards were null, exiting");
                    continue;
                }

                for (Integer item : rewards)
                {
                    SendDebugMessage("ItemId - " + item);

                    if (curr.GetUniqueItemIds().contains(item)){
                        noLongerDry = true;
                        uniqueCount++;
                    }
                }

                if (noLongerDry){
                    if (uniqueCount > 1){
                        SendMessage(PLUGIN_NAME + curr.GetGZMessage());
                    }
                    else {
                        SendMessage(PLUGIN_NAME + curr.GetCongratualtionsPhrase());
                    }

                    curr.ResetDryCount();
                }
                else {
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

    private void SendDebugMessage(String message) {
        if (isDeveloperMode()) {
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
    DrynessConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(DrynessConfig.class);
	}
}
