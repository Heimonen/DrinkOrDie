package com.sanelith.mod;

import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.gui.GenericScreen;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.sanelith.datatypes.GuiThirstBar;

/**
 * 
 * @author Christofer Heimonen
 *
 */
public class SurvivalGUI {
	
	private GuiThirstBar thirstBar;
	
	
	public SurvivalGUI(SpoutPlayer  player, JavaPlugin plugin) {
		GenericScreen screen =(GenericScreen) player.getMainScreen();
        thirstBar = new GuiThirstBar(screen, plugin);
	}
	
	public void updateDamage(float damage) {
		thirstBar.updateDamage(damage);
	}

}
