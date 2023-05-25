package com.github.questitems;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("questitemslist")
public interface QuestItemsListConfig extends Config
{
	@ConfigItem(
		keyName = "my string",
		name = "Some string config",
		description = "A config that has a string"
	)
	default String greeting()
	{
		return "Default value";
	}
}
