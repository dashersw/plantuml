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
package net.sourceforge.plantuml.sequencediagram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Message implements Event {

	final private Participant p1;
	final private Participant p2;
	final private List<String> label;
	final private boolean dotted;
	final private List<LifeEvent> lifeEvents = new ArrayList<LifeEvent>();

	private List<String> notes;
	private NotePosition notePosition;

	public Message(Participant p1, Participant p2, List<String> label, boolean dotted) {
		this.p1 = p1;
		this.p2 = p2;
		this.label = label;
		this.dotted = dotted;
	}

	public void addLifeEvent(LifeEvent lifeEvent) {
		this.lifeEvents.add(lifeEvent);
	}

	public List<LifeEvent> getLiveEvents() {
		return Collections.unmodifiableList(lifeEvents);
	}

	public Participant getParticipant1() {
		return p1;
	}

	public Participant getParticipant2() {
		return p2;
	}

	public List<String> getLabel() {
		return label;
	}

	public boolean isDotted() {
		return dotted;
	}

	public List<String> getNote() {
		return notes == null ? notes : Collections.unmodifiableList(notes);
	}

	public void setNote(List<String> strings, NotePosition notePosition) {
		if (notePosition != NotePosition.LEFT && notePosition != NotePosition.RIGHT) {
			throw new IllegalArgumentException();
		}
		this.notes = strings;
		this.notePosition = notePosition;
	}

	public NotePosition getNotePosition() {
		return notePosition;
	}

}
