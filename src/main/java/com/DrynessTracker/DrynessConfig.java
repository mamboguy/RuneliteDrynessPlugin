package com.DrynessTracker;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup(DrynessConfig.GROUP)
public interface DrynessConfig extends Config
{
    String GROUP = "drynesscount";
    int NO_ID_PROVIDED = -1;

    @ConfigItem(
            keyName = "BarrowsTrack",
            name = "Track Barrows?",
            description = "Toggle tracking Barrows dryness"
    )

    default boolean BarrowsTrack()
    {
        return true;
    }

    @ConfigItem(
            keyName = "MoonsTrack",
            name = "Track Perilous Moons?",
            description = "Toggle tracking Perilous Moons dryness"
    )

    default boolean MoonsTrack()
    {
        return true;
    }

    @ConfigItem(
            keyName = "TrawlerTrack",
            name = "Track Fishing Trawler?",
            description = "Toggle tracking Fishing Trawler dryness"
    )

    default boolean FishingTrawlerTrack()
    {
        return true;
    }

    @ConfigSection(
            name = "Debug",
            description = "Various debugging options",
            position = 1000
    )

    String Debug = "Debug";

    @ConfigItem(
            keyName = "ShowWidgetId",
            name = "Show Widget Ids",
            description = "Show the ids of any widget that appears on screen",
            position = 1010,
            section = Debug
    )

    default boolean ShowWidgetId()
    {
        return false;
    }

    @ConfigItem(
            keyName = "ShowItemSpawnType",
            name = "Show item spawn type?",
            description = "Shows whether items are stored in widget or a chest",
            position = 1030,
            section = Debug
    )

    default boolean ShowItemSpawnType()
    {
        return false;
    }

    @ConfigItem(
            keyName = "ShowItemIds",
            name = "Show item ids?",
            description = "Shows any item ids found in chest or widget",
            position = 1040,
            section = Debug
    )

    default boolean ShowItemIds()
    {
        return false;
    }
}
