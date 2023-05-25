/*
 * Copyright (c) 2021, Zoinkwiz <https://github.com/Zoinkwiz/>
 * Copyright (c) 2020, Patyfatycake <https://github.com/Patyfatycake/>
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
 * ON ANY THEORY OF LIABI`LITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.github.questitems.helpers.quests.recruitmentdrive;

import com.github.questitems.github.questitemss.github.questitems;
import com.github.questitems.requirements.Requirement;
import com.github.questitems.requirements.var.VarbitRequirement;
import com.github.questitems.requirements.util.RequirementBuilder;
import com.github.questitems.steps.ConditionalStep;
import com.github.questitems.steps.DetailedQuestStep;
import com.github.questitems.steps.ObjectStep;
import com.github.questitems.steps.QuestStep;
import java.util.ArrayList;
import java.util.List;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.eventbus.Subscribe;

public class MsHynnAnswerDialogQuizStep extends ConditionalStep
{
	private QuestStep leaveRoom, talkToMsHynnTerprett;

	private final int VARBIT_FINISHED_ROOM = 665;
	private final int VARBIT_PUZZLE_SOLUTION = 667;

	String[] answers = {
		"unknown",
		"10",
		"three false statements",
		"the wolves",
		"bucket A",
		"zero"
	};

	public MsHynnAnswerDialogQuizStep(github.questitems github.questitems, QuestStep step, Requirement... requirements)
	{
		super(github.questitems, step, requirements);

		talkToMsHynnTerprett = step;
		talkToMsHynnTerprett.addDialogSteps(
			"The wolves.",
			"Bucket A (32 degrees)",
			"The number of false statements here is three.",
			"Zero.");
		addSteps();
	}

	@Override
	public void startUp()
	{
		super.startUp();
		int answerID = client.getVarbitValue(VARBIT_PUZZLE_SOLUTION);
		if (answerID == 0)
		{
			return;
		}
		String answer = "The answer is " + answers[answerID] + ".";
		talkToMsHynnTerprett.setText("Talk to Ms Hynn Terprett and answer the riddle. " + answer);
	}

	@Subscribe
	@Override
	public void onVarbitChanged(VarbitChanged varbitChanged)
	{
		int answerID = client.getVarbitValue(VARBIT_PUZZLE_SOLUTION);
		if (answerID == 0)
		{
			return;
		}
		String answer = "The answer is " + answers[answerID] + ".";
		talkToMsHynnTerprett.setText("Talk to Ms Hynn Terprett and answer the riddle. " + answer);
	}

	private void addSteps()
	{
		VarbitRequirement finishedRoomCondition = new VarbitRequirement(VARBIT_FINISHED_ROOM, 1);
		leaveRoom = new ObjectStep(github.questitems, 7354, "Leave through the door to enter the portal and continue.");

		addStep(finishedRoomCondition, leaveRoom);
	}

	public List<QuestStep> getPanelSteps()
	{
		List<QuestStep> steps = new ArrayList<>();
		steps.add(leaveRoom);
		return steps;
	}
}
