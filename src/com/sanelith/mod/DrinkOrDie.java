package com.sanelith.mod;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.inventory.SpoutShapedRecipe;
import org.getspout.spoutapi.material.MaterialData;
import com.sanelith.datatypes.LeatherPouch;
import com.sanelith.datatypes.LeatherWaterFilledPouch;
import com.sanelith.datatypes.SurvivalPlayer;

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
 * his is a server multiplayer mod for a bukkit SpoutPlugin server. Requires
 * Spoutcraft (for clients) in order to function correclty (and for the players
 * to see the custom GUI).
 * 
 * This mod adds another dimension to your Minecraft experience on your survival
 * server. In addition to avoiding death from starvation you will also have to
 * avoid dying from dehydration. So keep that leather pouch with you at all
 * times to combat thirst! If you're not careful and you become dehydrated, you
 * will start hallucinating, and you will continuously lose health until you
 * either drink, or die.
 * 
 * Version History: Version 1.0.0 First stable release. Version 1.0.1 Added
 * ability to drink water directly from a water block Some small bug fixes
 * 
 * @author Christofer Heimonen
 * @version 1.0.1
 * 
 */
public class DrinkOrDie extends JavaPlugin {
	private long defaultThirst = 1000L;
	private PlayerListener playerListener;

	public static DrinkOrDie instance = null;

	@Override
	public void onEnable() {
		super.onEnable();
		DrinkOrDie.instance = this;
		ConfigurationSerialization.registerClass(SurvivalPlayer.class,
				"survivalPlayer");
		PlayerListener.playerMap = new HashMap<String, SurvivalPlayer>();
		// If data folder doesn't exist: create one.
		if (!getDataFolder().isDirectory())
			getDataFolder().mkdirs();

		// Save and create the default config.yml into the folder
		File configFile = new File(this.getDataFolder() + "/config.yml");
		if (!configFile.exists()) {
			this.saveDefaultConfig();
		}

		long thirstPeriod = defaultThirst;
		try {
			thirstPeriod = Long.parseLong(this.getConfig().getString(
					"thirstPeriod"));
		} catch (NumberFormatException e) {
			this.getLogger()
					.log(Level.SEVERE,
							"thirstLevel in config.yml must be set to an integer value, eg. 200. Loading a default value.");
		}

		String tempEnabled = this.getConfig().getString("modEnabled");
		boolean modEnabled = Boolean.valueOf(tempEnabled);

		// Initializing custom items
		SpoutManager.getFileManager().addToPreLoginCache(this,
				"https://dl.dropbox.com/u/44954718/mywaterpouch2.png");
		SpoutManager.getFileManager().addToPreLoginCache(this,
				"https://dl.dropbox.com/u/44954718/myWaterPouchWater5.png");
		SpoutManager.getFileManager().addToPreLoginCache(this,
				"https://dl.dropbox.com/u/44954718/myWaterDrop.png");
		SpoutManager.getFileManager().addToPreLoginCache(this,
				"https://dl.dropbox.com/u/44954718/myWaterDropDepleted.png");

		LeatherWaterFilledPouch waterPouch = new LeatherWaterFilledPouch(this,
				"", "");
		SpoutItemStack leatherWaterStack = new SpoutItemStack(waterPouch, 1);
		SpoutShapedRecipe leatherWaterPouchRecipe = new SpoutShapedRecipe(
				leatherWaterStack);
		leatherWaterPouchRecipe.shape(new String[] { " L ", "LWL", " L " });
		leatherWaterPouchRecipe.setIngredient('W', MaterialData.water);
		leatherWaterPouchRecipe.setIngredient('L', MaterialData.leather);
		SpoutManager.getMaterialManager().registerSpoutRecipe(
				leatherWaterPouchRecipe);

		LeatherPouch leatherPouch = new LeatherPouch(this, "", "");
		SpoutItemStack leatherPouchStack = new SpoutItemStack(leatherPouch, 1);
		SpoutShapedRecipe leatherPouchRecipe = new SpoutShapedRecipe(
				leatherPouchStack);
		leatherPouchRecipe.shape(new String[] { " L ", "L L", " L " });
		leatherPouchRecipe.setIngredient('L', MaterialData.leather);
		SpoutManager.getMaterialManager().registerSpoutRecipe(
				leatherPouchRecipe);
		if (modEnabled) {
			playerListener = new PlayerListener(this, thirstPeriod);
			this.getServer().getPluginManager()
					.registerEvents(playerListener, this);
		}
	}

	@Override
	public void onDisable() {
		super.onDisable();
		DrinkOrDie.instance = null;
		PlayerListener.playerMap = null;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("thirstperiod")) {
			if (args.length == 1) {
				try {
					long thirstPeriod = Long.parseLong(args[0]);
					this.getConfig().set("thirstPeriod", thirstPeriod);
					this.saveConfig();
					this.playerListener.setThirstPeriod(thirstPeriod);
					sender.sendMessage("The thirst period has been set to: "
							+ thirstPeriod
							+ ". \nPlease reload the server to apply changes.");
					return true;
				} catch (NumberFormatException e) {
					sender.sendMessage("The argument has to be an integer value. No action done.");
				}
			} else {
				sender.sendMessage("You need to include an argument, for example: \"/thirstperiod 500\". No action done.");
			}
		} else if (cmd.getName().equalsIgnoreCase("restorehydration")) {
			if (args.length != 0) {
				sender.sendMessage("This command has no support for arguments. No action done");
			} else {
				if (!(sender instanceof Player)) {
					sender.sendMessage("This command can only be run by a player.");
				} else {
					Player player = (Player) sender;
					SurvivalPlayer survivalPlayer = PlayerListener.playerMap
							.get(player.getName());
					survivalPlayer.setThirst(20);
					sender.sendMessage("Your hydration level has been restored to max value.");
				}
			}
			return true;
		} else if (cmd.getName().equalsIgnoreCase("drinkordieenabled")) {
			if (args.length == 1) {
				Boolean modEnabled = Boolean.valueOf(args[0]);
				String toWrite;
				if (modEnabled) {
					toWrite = "true";
					sender.sendMessage("The mod has been enabled. Please reload the server to apply changes.");
				} else {
					toWrite = "false";
					sender.sendMessage("The mod has been disabled. Please reload the server to apply changes.");
				}
				this.getConfig().set("modEnabled", toWrite);
				this.saveConfig();
			} else {
				sender.sendMessage("You need to include an argument, for example: \"/drinkordienabled true\". No action done.");
			}
			return true;
		}
		return false;
	}
}
