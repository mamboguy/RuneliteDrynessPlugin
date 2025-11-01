package com.DrynessTracker;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(DrynessConfig.GROUP)
public interface DrynessConfig extends Config
{
    String GROUP = "drynesscount";

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
}
