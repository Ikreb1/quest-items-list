package com.github.questitems;

import com.github.questitems.ui.MyElement;
import com.github.questitems.ui.MyPanel;
import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.bank.BankSearch;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

import java.awt.image.BufferedImage;

@Slf4j
@PluginDescriptor(
	name = "quest items list"
)
public class QuestItemsListPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private QuestItemsListConfig config;

	@Inject
	private BankSearch bankSearch;

	private MyPanel panel;

	private MyElement element;
	private NavigationButton navButton;

	private BufferedImage icon;

	@Override
	protected void startUp()
	{
		element = new MyElement("Hello lads");
		panel = new MyPanel(element);
		icon = new BufferedImage(48, 48, BufferedImage.TYPE_INT_ARGB);
		// icon = ImageUtil.loadImageResource(getClass(), "panel_icon.png");

		navButton = NavigationButton.builder()
				.tooltip("Quest items list")
				.icon(icon)
				.priority(1)
				.panel(panel)
				.build();

		clientToolbar.addNavigation(navButton);
	}

	@Override
	protected void shutDown() throws Exception
	{
		clientToolbar.removeNavigation(navButton);
		log.info("quest items list stopped!");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "quest items list says " + config.greeting(), null);
		}
	}

	@Provides
	QuestItemsListConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(QuestItemsListConfig.class);
	}
}
