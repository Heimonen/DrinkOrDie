package com.sanelith.mod;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
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
 * 
 * Listens for player actions
 * 
 * @author Christofer Heimonen
 * 
 */
public class PlayerListener implements Listener {
	private JavaPlugin plugin;
	private long thirstPeriod;
	private SurvivalPlayerThread survivalPlayerThread;
	private HashMap<String, Integer> playerTaskMap = new HashMap<String, Integer>();

	public static HashMap<String, SurvivalPlayer> playerMap = null;

	public PlayerListener(JavaPlugin plugin, long thirstPeriod) {
		this.plugin = plugin;
		this.thirstPeriod = thirstPeriod;
	}

	/**
	 * When a player logs in thirst and stamina are initiated
	 * 
	 * @param event
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player loggedInPlayer = event.getPlayer();

		// Attempt to deserialize
		SurvivalPlayer tempSurvivalPlayer = (SurvivalPlayer) this.plugin
				.getConfig().get(loggedInPlayer.getName());
		SurvivalPlayer survivalPlayer = new SurvivalPlayer(this.plugin,
				loggedInPlayer);
		if (tempSurvivalPlayer != null) {
			survivalPlayer.setThirst(tempSurvivalPlayer.getThirst());
			survivalPlayer.setStamina(tempSurvivalPlayer.getStamina());
		}
		playerMap.put(survivalPlayer.getName(), survivalPlayer);
		this.survivalPlayerThread = new SurvivalPlayerThread(survivalPlayer,
				survivalPlayer.gui);
		BukkitTask task = plugin
				.getServer()
				.getScheduler()
				.runTaskTimerAsynchronously(plugin, this.survivalPlayerThread,
						60L, this.thirstPeriod);
		playerTaskMap.put(survivalPlayer.getName(), task.getTaskId());
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			SurvivalPlayer survivalPlayer = PlayerListener.playerMap.get(player
					.getName());
			survivalPlayer.setThirst(20);
			survivalPlayer.setStamina(20);
		}
	}

	/**
	 * Drink water when clicking on water
	 * 
	 * @param event
	 */
	@EventHandler
	public void OnPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = player.getTargetBlock(null, 100);
		if (block.getType() == Material.WATER
				|| block.getType() == Material.STATIONARY_WATER) {
			SurvivalPlayer survivalPlayer = PlayerListener.playerMap.get(player
					.getName());
			if (survivalPlayer.getThirst() < 20) {
				block.breakNaturally();
				survivalPlayer.setThirst(survivalPlayer.getThirst() + 3);
				player.playSound(player.getLocation(), Sound.DRINK, 10, 1);
			}
		}
	}

	/**
	 * The player leaves the server: the AsyncTaskThread has to be stopped.
	 * 
	 * @param event
	 */
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player leavingPlayer = event.getPlayer();
		Bukkit.getServer().getScheduler()
				.cancelTask(playerTaskMap.get(leavingPlayer.getName()));
		SurvivalPlayer sPlayer = PlayerListener.playerMap.get(leavingPlayer
				.getName());
		DrinkOrDie.instance.getConfig().set(leavingPlayer.getName(), sPlayer);
	}

	// Not supported yet
	public void setThirstPeriod(long thirstPeriod) {
	}
}
