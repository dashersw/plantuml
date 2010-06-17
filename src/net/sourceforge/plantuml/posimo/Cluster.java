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
 * Revision $Revision: 4236 $
 * 
 */
package net.sourceforge.plantuml.posimo;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Cluster implements Clusterable {

	private static int CPT = 1;

	private final Cluster parent;
	private final Collection<Block> blocs = new ArrayList<Block>();
	private final Collection<Cluster> children = new ArrayList<Cluster>();
	private final int uid = CPT++;

	public Cluster(Cluster parent) {
		this.parent = parent;
		if (parent != null) {
			parent.children.add(this);
		}
	}

	public Collection<Cluster> getSubClusters() {
		return Collections.unmodifiableCollection(children);
	}

	public int getUid() {
		return uid;
	}

	public void addBloc(Block b) {
		this.blocs.add(b);
	}

	public Cluster getParent() {
		return parent;
	}

	public Point2D getPosition() {
		throw new UnsupportedOperationException();
	}

	public Dimension2D getSize() {
		throw new UnsupportedOperationException();
	}

	public Collection<Block> getContents() {
		return Collections.unmodifiableCollection(blocs);
	}

	public Block getBlock(int uid) {
		for (Block b : blocs) {
			if (b.getUid() == uid) {
				return b;
			}
		}
		for (Cluster sub : children) {
			final Block result = sub.getBlock(uid);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

}
