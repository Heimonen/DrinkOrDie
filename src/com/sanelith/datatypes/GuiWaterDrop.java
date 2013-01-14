package com.sanelith.datatypes;

import org.getspout.spoutapi.gui.GenericContainer;
import org.getspout.spoutapi.gui.GenericTexture;

/**
 * 
 * This file is part of DrinkOrDie.
 * 
 * Copyright (c) 2013, Christofer Heimonen DrinkOrDie is licensed under the GNU
 * Lesser General Public License.
 * 
 * DrinkOrDie is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * DrinkOrDie is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 * @author Christofer Heimonen
 * 
 */
public class GuiWaterDrop {

	public static final int WATER_FILLED = 0;
	public static final int WATER_DEPLETED = 1;

	private int type;
	private GenericTexture texture;

	public GuiWaterDrop(GenericContainer container, int type) {
		this.type = type;
		texture = new GenericTexture();
		setType(this.type);
		container.addChild(texture);
	}

	/**
	 * Sets the water drop type. TODO: Change from dropbox links to local files.
	 * 
	 * @param type
	 */
	public void setType(int type) {
		switch (type) {
		case GuiWaterDrop.WATER_FILLED:
			texture.setUrl("https://dl.dropbox.com/u/44954718/myWaterDrop.png");
			break;
		case GuiWaterDrop.WATER_DEPLETED:
			texture.setUrl("https://dl.dropbox.com/u/44954718/myWaterDropDepleted.png");
			break;
		}
	}
}
