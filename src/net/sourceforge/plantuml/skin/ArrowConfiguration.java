/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 5191 $
 *
 */
package net.sourceforge.plantuml.skin;

public class ArrowConfiguration {

	private final ArrowDirection direction;
	private final ArrowBody body;
	private final ArrowHead head;
	private final ArrowPart part;
	private final ArrowDecoration decorationStart;
	private final ArrowDecoration decorationEnd;

	private ArrowConfiguration(ArrowDirection direction, ArrowBody body, ArrowHead head, ArrowPart part,
			ArrowDecoration decorationStart, ArrowDecoration decorationEnd) {
		if (direction == null || body == null || head == null || part == null) {
			throw new IllegalArgumentException();
		}
		this.part = part;
		this.direction = direction;
		this.body = body;
		this.head = head;
		this.decorationStart = decorationStart;
		this.decorationEnd = decorationEnd;
	}

	@Override
	public String toString() {
		return name();
	}

	public String name() {
		return direction.name().substring(0, 4) + "-" + body.name() + "-" + head.name() + "-"
				+ part.name().substring(0, 3) + "-" + decorationEnd.name().substring(0, 4);
	}

	public static ArrowConfiguration withDirection(ArrowDirection direction) {
		return new ArrowConfiguration(direction, ArrowBody.NORMAL, ArrowHead.NORMAL, ArrowPart.FULL,
				ArrowDecoration.NONE, ArrowDecoration.NONE);
	}

	public ArrowConfiguration reverse() {
		if (direction == ArrowDirection.SELF) {
			throw new UnsupportedOperationException();
		}
		return new ArrowConfiguration(direction.reverse(), body, head, part, decorationStart, decorationEnd);
	}

	public ArrowConfiguration withHead(ArrowHead head) {
		return new ArrowConfiguration(direction, body, head, part, decorationStart, decorationEnd);
	}

	public ArrowConfiguration withDotted() {
		return new ArrowConfiguration(direction, ArrowBody.DOTTED, head, part, decorationStart, decorationEnd);
	}

	public ArrowConfiguration withPart(ArrowPart part) {
		return new ArrowConfiguration(direction, body, head, part, decorationStart, decorationEnd);
	}

	public ArrowConfiguration withDecorationStart(ArrowDecoration decorationStart) {
		return new ArrowConfiguration(direction, body, head, part, decorationStart, decorationEnd);
	}

	public ArrowConfiguration withDecorationEnd(ArrowDecoration decorationEnd) {
		return new ArrowConfiguration(direction, body, head, part, decorationStart, decorationEnd);
	}

	public final ArrowDecoration getDecorationEnd() {
		return this.decorationEnd;
	}

	public final ArrowDecoration getDecorationStart() {
		return this.decorationStart;
	}

	public boolean isLeftToRightNormal() {
		return this.direction == ArrowDirection.LEFT_TO_RIGHT_NORMAL;
	}

	public boolean isRightToLeftReverse() {
		return this.direction == ArrowDirection.RIGHT_TO_LEFT_REVERSE;
	}

	public boolean isSelfArrow() {
		return this.direction == ArrowDirection.SELF;
	}

	public boolean isDotted() {
		return body == ArrowBody.DOTTED;
	}

	public ArrowHead getHead() {
		return this.head;
	}

	public final ArrowPart getPart() {
		return part;
	}

}
