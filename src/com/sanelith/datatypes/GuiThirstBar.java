package com.sanelith.datatypes;

import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.gui.ContainerType;
import org.getspout.spoutapi.gui.GenericContainer;
import org.getspout.spoutapi.gui.GenericScreen;
import org.getspout.spoutapi.gui.WidgetAnchor;

/**
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
			waterList[i] = new GuiWaterDrop(container, GuiWaterDrop.WATER_FILLED);
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
