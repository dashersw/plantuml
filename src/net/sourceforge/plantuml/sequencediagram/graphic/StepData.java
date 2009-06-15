/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques (for Atos Origin).
 *
 * Project Info:  http://plantuml.sourceforge.net
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques (for Atos Origin).
 *
 */
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.awt.Graphics2D;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.skin.Skin;

class StepData {

	private final Map<Participant, LivingParticipantBox> participants = new LinkedHashMap<Participant, LivingParticipantBox>();
	private final Map<Event, GraphicalElement> events = new LinkedHashMap<Event, GraphicalElement>();
	private final Skin skin;

	StepData(Skin skin) {
		this.skin = skin;
	}

	public final Skin getSkin() {
		return skin;
	}

	public final Map<Participant, LivingParticipantBox> getParticipants() {
		return participants;
	}

	public final Map<Event, GraphicalElement> getEvents() {
		return events;
	}

	public double getHeadHeight(Graphics2D g2d) {
		double r = 0;
		for (LivingParticipantBox livingParticipantBox : participants.values()) {
			final double y = livingParticipantBox.getParticipantBox().getHeadHeight(g2d);
			r = Math.max(r, y);
		}
		return r;
	}

	public double getTailHeight(Graphics2D g2d) {
		double r = 0;
		for (LivingParticipantBox livingParticipantBox : participants.values()) {
			final double y = livingParticipantBox.getParticipantBox().getTailHeight(g2d);
			r = Math.max(r, y);
		}
		return r;
	}

}
