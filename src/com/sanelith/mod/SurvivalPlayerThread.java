package com.sanelith.mod;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import com.sanelith.datatypes.SurvivalPlayer;

/**
 * 
 * @author Christofer Heimonen
 *
 */
public class SurvivalPlayerThread implements Runnable {
	private SurvivalPlayer player;
	private SurvivalGUI gui;

	public SurvivalPlayerThread(SurvivalPlayer player, SurvivalGUI gui) {
		this.player = player;
		this.gui = gui;
	}

	@Override
	public void run() {
		this.player.decreaseThirst();
		this.player.decreaseStamina();
		Player player = Bukkit.getPlayer(this.player.getName());
		if(this.player.getThirst() == 0) {
			if(this.player.getTakingDamage() == false) {
				BukkitTask task = DrinkOrDie.instance
						.getServer().getScheduler().runTaskTimerAsynchronously(DrinkOrDie.instance, new DamageThread(this.player), 60L, 50L);
				SurvivalPlayer toupdate = PlayerListener.playerMap.get(this.player.getName());
				toupdate.setDamageTaskId(task.getTaskId());
				toupdate.setTakingDamage(true);
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20000, 1));
			player.playSound(player.getLocation(), Sound.ZOMBIE_IDLE, 10, 1);
		} else {
			if(this.player.getThirst() < 4) {
				player.playSound(player.getLocation(), Sound.ZOMBIE_IDLE, 10, 1);
			}
		}
		gui.updateDamage(this.player.getThirst());
	}
}
