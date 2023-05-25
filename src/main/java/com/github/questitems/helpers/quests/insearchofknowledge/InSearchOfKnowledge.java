/*
 * Copyright (c) 2021, Zoinkwiz
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.github.questitems.helpers.quests.insearchofknowledge;

import com.github.questitems.ItemCollections;
import com.github.questitems.QuestDescriptor;
import com.github.questitems.github.questitemsQuest;
import com.github.questitems.Zone;
import com.github.questitems.banktab.BankSlotIcons;
import com.github.questitems.panel.PanelDetails;
import com.github.questitems.github.questitemss.Basicgithub.questitems;
import com.github.questitems.requirements.item.ItemRequirement;
import com.github.questitems.requirements.player.PrayerRequirement;
import com.github.questitems.requirements.Requirement;
import com.github.questitems.requirements.var.VarbitRequirement;
import com.github.questitems.requirements.conditional.Conditions;
import com.github.questitems.requirements.util.LogicType;
import com.github.questitems.rewards.ItemReward;
import com.github.questitems.steps.ConditionalStep;
import com.github.questitems.steps.DetailedQuestStep;
import com.github.questitems.steps.NpcStep;
import com.github.questitems.steps.ObjectStep;
import com.github.questitems.steps.QuestStep;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.runelite.api.ItemID;
import net.runelite.api.NpcID;
import net.runelite.api.ObjectID;
import net.runelite.api.Prayer;
import net.runelite.api.coords.WorldPoint;

@QuestDescriptor(
	quest = github.questitemsQuest.IN_SEARCH_OF_KNOWLEDGE
)
public class InSearchOfKnowledge extends Basicgithub.questitems
{
	// Required
	ItemRequirement combatGear, food5, food5Highlighted, knife, moonPage, templePage, sunPage, sunTome, moonTome,
		templeTome;

	Requirement protectFromMagic;

	Requirement inDungeon, fedAimeri, hadTempleTome, hadSunTome, hadMoonTome, repairedMoon, repairedSun,
		repairedTemple, repairedTomes, givenSunTome, givenMoonTome, givenTempleTome;

	QuestStep enterDungeon, useFoodOnAimeri, talkToAimeriAgain, searchBookcasesForTemple, searchBookcasesForMoon,
		searchBookcasesForSun, getPages, enterDungeonAgain, useMoonOnLogosia, useSunOnLogosia, useTempleOnLogosia,
		talkToLogosia;

	Zone dungeon;

	ConditionalStep goTalkToAimeri, repairTomes;

	@Override
	public Map<Integer, QuestStep> loadSteps()
	{
		loadZones();
		setupRequirements();
		setupConditions();
		setupSteps();
		Map<Integer, QuestStep> steps = new HashMap<>();

		ConditionalStep goFeedAimeri = new ConditionalStep(this, enterDungeon);
		goFeedAimeri.addStep(new Conditions(inDungeon, fedAimeri), talkToAimeriAgain);
		goFeedAimeri.addStep(inDungeon, useFoodOnAimeri);
		steps.put(0, goFeedAimeri);

		goTalkToAimeri = new ConditionalStep(this, enterDungeonAgain);
		goTalkToAimeri.addStep(new Conditions(inDungeon, hadTempleTome, hadSunTome), searchBookcasesForMoon);
		goTalkToAimeri.addStep(new Conditions(inDungeon, hadTempleTome), searchBookcasesForSun);
		goTalkToAimeri.addStep(inDungeon, searchBookcasesForTemple);
		goTalkToAimeri.setLockingCondition(new Conditions(hadTempleTome, hadSunTome, hadMoonTome));

		repairTomes = new ConditionalStep(this, goTalkToAimeri);
		repairTomes.addStep(new Conditions(hadTempleTome, hadSunTome, hadMoonTome), getPages);
		repairTomes.setLockingCondition(repairedTomes);

		ConditionalStep bringTomesToFinish = new ConditionalStep(this, repairTomes);
		bringTomesToFinish.addStep(new Conditions(repairedTomes, givenMoonTome, givenSunTome), useTempleOnLogosia);
		bringTomesToFinish.addStep(new Conditions(repairedTomes, givenMoonTome), useSunOnLogosia);
		bringTomesToFinish.addStep(repairedTomes, useMoonOnLogosia);

		steps.put(1, bringTomesToFinish);
		steps.put(2, talkToLogosia);

		return steps;
	}

	@Override
	public void setupRequirements()
	{
		combatGear = new ItemRequirement("Combat gear", -1, -1).isNotConsumed();
		combatGear.setDisplayItemId(BankSlotIcons.getCombatGear());

		food5 = new ItemRequirement("Food", ItemCollections.FISH_FOOD, 5);
		food5Highlighted = new ItemRequirement("Food", ItemCollections.FISH_FOOD, 5);
		food5Highlighted.setHighlightInInventory(true);
		knife = new ItemRequirement("Knife or slash weapon to cut through a web", ItemID.KNIFE).isNotConsumed();
		knife.addAlternates(ItemID.WILDERNESS_SWORD, ItemID.WILDERNESS_SWORD_1, ItemID.WILDERNESS_SWORD_2,
			ItemID.WILDERNESS_SWORD_3, ItemID.WILDERNESS_SWORD_4);

		protectFromMagic = new PrayerRequirement("Protect from Magic", Prayer.PROTECT_FROM_MAGIC);

		sunPage = new ItemRequirement("Tattered sun page", ItemID.TATTERED_SUN_PAGE);
		moonPage = new ItemRequirement("Tattered moon page", ItemID.TATTERED_MOON_PAGE);
		templePage = new ItemRequirement("Tattered temple page", ItemID.TATTERED_TEMPLE_PAGE);

		sunTome = new ItemRequirement("Tome of the sun", ItemID.TOME_OF_THE_SUN);
		sunTome.setTooltip("You can get another from the bookshelves in the Forthos Dungeon");
		sunTome.setHighlightInInventory(true);
		moonTome = new ItemRequirement("Tome of the moon", ItemID.TOME_OF_THE_MOON);
		moonTome.setTooltip("You can get another from the bookshelves in the Forthos Dungeon");
		moonTome.setHighlightInInventory(true);
		templeTome = new ItemRequirement("Tome of the temple", ItemID.TOME_OF_THE_TEMPLE);
		templeTome.setTooltip("You can get another from the bookshelves in the Forthos Dungeon");
		templeTome.setHighlightInInventory(true);
	}

	public void loadZones()
	{
		dungeon = new Zone(new WorldPoint(1792, 9880, 0), new WorldPoint(1856, 9983, 0));
	}

	public void setupConditions()
	{
		inDungeon = new ZoneRequirement(dungeon);
		fedAimeri = new VarbitRequirement(8393, 5);

		givenSunTome = new VarbitRequirement(8405, 1);
		givenMoonTome = new VarbitRequirement(8404, 1);
		givenTempleTome = new VarbitRequirement(8406, 1);

		hadTempleTome = new Conditions(true, LogicType.OR,
			templeTome,
			givenTempleTome);
		hadMoonTome = new Conditions(true, LogicType.OR,
			moonTome,
			givenMoonTome);
		hadSunTome = new Conditions(true, LogicType.OR,
			sunTome,
			givenSunTome);

		repairedSun = new VarbitRequirement(8399, 4);
		repairedMoon = new VarbitRequirement(8400, 4);
		repairedTemple = new VarbitRequirement(8401, 4);

		repairedTomes = new Conditions(repairedSun, repairedMoon, repairedTemple);
	}

	public void setupSteps()
	{
		enterDungeon = new ObjectStep(this, ObjectID.LADDER_34862, new WorldPoint(1702, 3574, 0), "Enter the Forthos " +
			"Dungeon.", knife, food5);

		useFoodOnAimeri = new FeedingAimeri(this);

		talkToAimeriAgain = new NpcStep(this, NpcID.BROTHER_AIMERI_8705, new WorldPoint(1840, 9926, 0), "Talk to Aimeri " +
			"again.");
		talkToAimeriAgain.addDialogStep("Who are you?");

		searchBookcasesForTemple = new ObjectStep(this, ObjectID.MUSTY_BOOKSHELF_34849, new WorldPoint(1796, 9935, 0),
			"Search the musty bookshelves to the west of Aimeri for the tomes of moon, sun, and temple.",
			protectFromMagic);

		searchBookcasesForSun = new ObjectStep(this, ObjectID.MUSTY_BOOKSHELF, new WorldPoint(1805, 9935, 0),
			"Search the musty bookshelves to the west of Aimeri for the tomes of moon, sun, and temple.",
			protectFromMagic);

		searchBookcasesForMoon = new ObjectStep(this, ObjectID.MUSTY_BOOKSHELF_34848, new WorldPoint(1804, 9943, 0),
			"Search the musty bookshelves to the west of Aimeri for the tomes of moon, sun, and temple.",
			protectFromMagic);

		enterDungeonAgain = new ObjectStep(this, ObjectID.LADDER_34862, new WorldPoint(1702, 3574, 0),
			"Kill any monsters in the Forthos Dungeon for pages for the tomes. You'll need 4 page for each tome.",
			combatGear);

		getPages = new DetailedQuestStep(this, "Kill any monsters in the Forthos Dungeon for pages for the tomes. " +
			"You'll need to use 4 pages on each tome.", combatGear);
		getPages.addSubSteps(enterDungeonAgain);

		useMoonOnLogosia = new NpcStep(this, NpcID.LOGOSIA, new WorldPoint(1633, 3808, 0),
			"Give the tomes to Logosia in the Arceuus library.", moonTome);
		useMoonOnLogosia.addIcon(ItemID.TOME_OF_THE_MOON);

		useSunOnLogosia = new NpcStep(this, NpcID.LOGOSIA, new WorldPoint(1633, 3808, 0),
			"Give the tomes to Logosia in the Arceuus library.", sunTome);
		useSunOnLogosia.addIcon(ItemID.TOME_OF_THE_SUN);

		useTempleOnLogosia = new NpcStep(this, NpcID.LOGOSIA, new WorldPoint(1633, 3808, 0),
			"Give the tomes to Logosia in the Arceuus library.", templeTome);
		useTempleOnLogosia.addIcon(ItemID.TOME_OF_THE_TEMPLE);

		talkToLogosia = new NpcStep(this, NpcID.LOGOSIA, new WorldPoint(1633, 3808, 0),
			"Talk to Logosia in the Arceuus library.");

		useMoonOnLogosia.addSubSteps(useSunOnLogosia, useTempleOnLogosia, talkToLogosia);
	}

	@Override
	public List<ItemRequirement> getItemRequirements()
	{
		return Arrays.asList(knife, food5, combatGear);
	}

	@Override
	public List<String> getCombatRequirements()
	{
		return Collections.singletonList("Many monsters in the Forthos Dungeon");
	}

	@Override
	public List<ItemReward> getItemRewards()
	{
		return Collections.singletonList(new ItemReward("10,000 Experience Lamp (any skill over level 40).", ItemID.ANTIQUE_LAMP, 1)); //4447 Is placeholder for filter.
	}

	@Override
	public List<PanelDetails> getPanels()
	{
		List<PanelDetails> allSteps = new ArrayList<>();
		allSteps.add(new PanelDetails("Helping Aimeri",
			Arrays.asList(enterDungeon, useFoodOnAimeri, talkToAimeriAgain),
			knife, food5, combatGear));

		PanelDetails getBookPanel = new PanelDetails("Getting the books",
			Collections.singletonList(searchBookcasesForTemple));
		getBookPanel.setLockingStep(goTalkToAimeri);
		allSteps.add(getBookPanel);

		PanelDetails getPagesPanel = new PanelDetails("Repairing the books",
			Collections.singletonList(getPages), combatGear);
		getPagesPanel.setLockingStep(repairTomes);
		allSteps.add(getPagesPanel);

		allSteps.add(new PanelDetails("Document the Tomes",
			Collections.singletonList(useMoonOnLogosia), moonTome, sunTome,
				templeTome));

		return allSteps;
	}
}
