package com.sanelith.mod;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.sanelith.datatypes.SurvivalPlayer;

/**
 * Makes the player take damage when dehydrated
 * 
 * @author Christofer Heimonen
 * 
 */
public class DamageThread implements Runnable {

	private Player player;

	public DamageThread(SurvivalPlayer player) {
		this.player = Bukkit.getPlayer(player.getName());
	}

	@Override
	public void run() {
		player.damage(2);
	}

}
