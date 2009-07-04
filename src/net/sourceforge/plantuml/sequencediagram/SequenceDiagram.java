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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.sequencediagram.graphic.SequenceDiagramPngMaker;
import net.sourceforge.plantuml.skin.ProtectedSkin;
import net.sourceforge.plantuml.skin.Skin;
import net.sourceforge.plantuml.skin.SkinUtils;
import net.sourceforge.plantuml.skin.rose.Rose;

public class SequenceDiagram extends UmlDiagram {

	private final Map<String, Participant> participants = new LinkedHashMap<String, Participant>();

	private final List<Event> events = new ArrayList<Event>();

	private Skin skin = new ProtectedSkin(new Rose());

	public Participant getOrCreateParticipant(String code) {
		Participant result = participants.get(code);
		if (result == null) {
			result = new Participant(ParticipantType.PARTICIPANT, code, Arrays.asList(code));
			participants.put(code, result);
		}
		return result;
	}

	private Message lastMessage;

	public Message getLastMessage() {
		return lastMessage;
	}

	public Participant createNewParticipant(ParticipantType type, String code, List<String> display) {
		if (participants.containsKey(code)) {
			throw new IllegalArgumentException();
		}
		if (display == null) {
			display = Arrays.asList(code);
		}
		final Participant result = new Participant(type, code, display);
		participants.put(code, result);
		return result;
	}

	public Map<String, Participant> participants() {
		return Collections.unmodifiableMap(participants);
	}

	public void addMessage(Message m) {
		lastMessage = m;
		events.add(m);
	}

	public void addNote(Note n) {
		events.add(n);
	}

	public void newpage() {
		events.add(new Newpage());
	}

	public List<Event> events() {
		return Collections.unmodifiableList(events);
	}

	public List<File> createPng(File pngFile) throws IOException {
		final SequenceDiagramPngMaker maker = new SequenceDiagramPngMaker(this, skin);
		return maker.createPng(pngFile);
	}

	public void createPng(OutputStream os) throws IOException {
		final SequenceDiagramPngMaker maker = new SequenceDiagramPngMaker(this, skin);
		maker.createPng(os);
	}

	public void activate(Participant p, LifeEventType lifeEventType) {
		if (lifeEventType == LifeEventType.CREATE) {
			events.add(new LifeEvent(p, lifeEventType));
			return;
		}
		if (lastMessage == null) {
			return;
		}
		lastMessage.addLifeEvent(new LifeEvent(p, lifeEventType));
	}

	private final List<Grouping> openGroupings = new ArrayList<Grouping>();

	public void grouping(String title, String comment, GroupingType type) {
		if (type != GroupingType.START && openGroupings.size() == 0) {
			throw new IllegalArgumentException();
		}

		final Grouping g = new Grouping(title, comment, type);
		events.add(g);
		if (openGroupings.size() > 0) {
			g.setFather(openGroupings.get(0));
		}
		if (type == GroupingType.START) {
			openGroupings.add(0, g);
		} else if (type == GroupingType.END) {
			openGroupings.remove(0);
		}
	}

	public String getDescription() {
		return "(" + participants.size() + " participants)";
	}

	public boolean changeSkin(String className) {
		final Skin s = SkinUtils.loadSkin(className);
		final Integer expected = new Integer(1);
		if (s != null && expected.equals(s.getProtocolVersion())) {
			this.skin = new ProtectedSkin(s);
			return true;
		}
		return false;
	}

	public Skin getSkin() {
		return skin;
	}

	private Integer messageNumber = null;
	private int incrementMessageNumber;

	private DecimalFormat decimalFormat;

	public final void goAutonumber(int startingNumber, int increment, DecimalFormat decimalFormat) {
		this.messageNumber = startingNumber;
		this.incrementMessageNumber = increment;
		this.decimalFormat = decimalFormat;
	}

	public String getNextMessageNumber() {
		if (messageNumber == null) {
			return null;
		}
		final Integer result = messageNumber;
		messageNumber += incrementMessageNumber;
		return decimalFormat.format(result);
	}

	public boolean isShowFootbox() {
		return showFootbox;
	}

	private boolean showFootbox = true;

	public void setShowFootbox(boolean footbox) {
		this.showFootbox = footbox;

	}
}
