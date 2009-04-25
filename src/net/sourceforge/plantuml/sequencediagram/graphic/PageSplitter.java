/* ========================================================================
 * Plantuml : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques (for Atos Origin).
 *
 * Project Info:  http://plantuml.sourceforge.net
 * 
 * This file is part of Plantuml.
 *
 * Plantuml is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Plantuml distributed in the hope that it will be useful, but
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.sequencediagram.Newpage;

public class PageSplitter {

	private final double fullHeight;
	private final List<Double> positions;
	private final double titleHeight;
	private final double headerHeight;
	private final double tailHeight;
	private final double signatureHeight;
	private final double newpageHeight;

	public PageSplitter(double fullHeight, double titleHeight, double headerHeight, Map<Newpage, Double> newpages,
			double tailHeight, double signatureHeight, double newpageHeight) {
		this.fullHeight = fullHeight;
		this.positions = new ArrayList<Double>(newpages.values());
		this.titleHeight = titleHeight;
		this.headerHeight = headerHeight;
		this.tailHeight = tailHeight;
		this.signatureHeight = signatureHeight;
		this.newpageHeight = newpageHeight;
	}

	public List<Page> getPages() {

		if (positions.size() == 0) {
			return Arrays.asList(onePage());
		}

		final List<Page> result = new ArrayList<Page>();

		result.add(firstPage());
		for (int i = 0; i < positions.size() - 1; i++) {
			result.add(createPage(i));
		}
		result.add(lastPage());

		return result;
	}

	private Page lastPage() {
		final double newpage1 = positions.get(positions.size() - 1) - this.newpageHeight;
		final double newpage2 = this.fullHeight - this.tailHeight - this.signatureHeight;
		return new Page(0, headerHeight, newpage1, newpage2, tailHeight, signatureHeight);
	}

	private Page firstPage() {
		final double newpage1 = this.titleHeight + this.headerHeight;
		final double newpage2 = positions.get(0) + this.newpageHeight;
		return new Page(titleHeight, headerHeight, newpage1, newpage2, tailHeight, 0);
	}

	private Page onePage() {
		final double newpage1 = this.titleHeight + this.headerHeight;
		final double newpage2 = this.fullHeight - this.tailHeight - this.signatureHeight;
		return new Page(titleHeight, headerHeight, newpage1, newpage2, tailHeight, signatureHeight);
	}

	private Page createPage(int i) {
		final double newpage1 = positions.get(i) - this.newpageHeight;
		final double newpage2 = positions.get(i + 1) + this.newpageHeight;
		return new Page(0, headerHeight, newpage1, newpage2, tailHeight, 0);
	}

}
