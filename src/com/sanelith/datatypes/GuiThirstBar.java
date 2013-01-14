package com.sanelith.datatypes;

import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.gui.ContainerType;
import org.getspout.spoutapi.gui.GenericContainer;
import org.getspout.spoutapi.gui.GenericScreen;
import org.getspout.spoutapi.gui.WidgetAnchor;

/**
 * 
 * * This file is part of DrinkOrDie.
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
 * @author Christofer Heimonen
 * 
 */
public class GuiThirstBar {

	private GenericContainer container;
	private GenericScreen screen;
	private GuiWaterDrop[] waterList;

	public GuiThirstBar(GenericScreen screen, JavaPlugin plugin) {
		this.waterList = new GuiWaterDrop[10];
		this.container = new GenericContainer();
		this.screen = screen;
		this.container.setAnchor(WidgetAnchor.BOTTOM_CENTER);
		this.container.setLayout(ContainerType.HORIZONTAL);
		this.container.setWidth(89);
		this.container.setHeight(10);
		this.container.setX(95).setY(-10);
		container.setMargin(10);
		for (int i = 0; i < waterList.length; i++) {
			waterList[i] = new GuiWaterDrop(container,
					GuiWaterDrop.WATER_FILLED);
		}
		this.screen.attachWidget(plugin, this.container);
	}

	/**
	 * Updates the ThirstBar
	 * 
	 * @param damage
	 */
	public void updateDamage(float damage) {
		float fl = damage / 2;
		int i = (int) Math.ceil(fl);
		i = waterList.length - i;
		for (int j = waterList.length - 1; j > -1; j--) {
			if (i > j) {
				waterList[j].setType(GuiWaterDrop.WATER_DEPLETED);
			} else {
				waterList[j].setType(GuiWaterDrop.WATER_FILLED);
			}
		}
	}
}
