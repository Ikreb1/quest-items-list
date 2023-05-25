package com.github.questitems;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class QuestItemsListPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(QuestItemsListPlugin.class);
		RuneLite.main(args);
	}
}