package com.sanelith.datatypes;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.getspout.spoutapi.SpoutManager;
import com.sanelith.mod.SurvivalGUI;
import com.sanelith.mod.DrinkOrDie;

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
 * A player class. Each player on the server should have one initiated when they
 * log in. Holds information about thirst and stamina. Even though stamina is
 * mentioned everywhere it is not supported in this mod yet.
 * 
 * @author Christofer Heimonen
 * 
 */
@SerializableAs("survivalPlayer")
public class SurvivalPlayer implements ConfigurationSerializable {

	private String playerName;
	private int thirst = 20;
	private int stamina = 20;
	private int damageTaskId = 0;
	private boolean takingDamage = false;
	public SurvivalGUI gui;

	public SurvivalPlayer(Player player) {
		this.playerName = player.getName();
		this.gui = new SurvivalGUI(SpoutManager.getPlayer(player));
	}

	public SurvivalPlayer(ConfigurationSection map) {
		this.playerName = map.getString("playerName");
		this.thirst = map.getInt("thirst");
		this.stamina = map.getInt("stamina");
		this.damageTaskId = map.getInt("damageTaskId");
		this.takingDamage = Boolean.parseBoolean(map.getString("takingDamage"));
		// TODO: This seems to cause some bugs. Check it out.
		if (DrinkOrDie.instance != null
				&& DrinkOrDie.instance.getServer().getPlayerExact(
						this.playerName) != null) {
			this.gui = new SurvivalGUI(SpoutManager.getPlayer(Bukkit
					.getPlayer(this.playerName)));
		}
	}

	public String getName() {
		return this.playerName;
	}

	public int getThirst() {
		return this.thirst;
	}

	public void setThirst(int thirst) {
		if (thirst > 20) {
			this.thirst = 20;
		} else if (thirst < 0) {
			this.thirst = 0;
		} else {
			this.thirst = thirst;
		}
		gui.updateDamage(this.thirst);
		if (this.thirst > 0) {
			if (this.getTakingDamage()) {
				Bukkit.getServer().getScheduler()
						.cancelTask(this.getDamageTaskId());
				this.setTakingDamage(false);
			}
			Bukkit.getPlayer(this.playerName).removePotionEffect(
					PotionEffectType.CONFUSION);
		}
	}

	@Deprecated
	public void incrementThirst() {
		if (this.thirst < 20)
			this.thirst++;
	}

	public void decreaseThirst() {
		this.thirst--;
		this.thirst = this.thirst < 0 ? 0 : this.thirst;
	}

	public int getStamina() {
		return this.stamina;
	}

	public void setStamina(int stamina) {
		if (stamina > 20)
			this.stamina = 20;
		else if (stamina < 0)
			this.stamina = 0;
		else
			this.stamina = stamina;
	}

	public void incrementStamina() {
		if (this.stamina < 20)
			this.stamina++;
	}

	public void decreaseStamina() {
		this.stamina--;
		this.stamina = this.stamina < 0 ? 0 : this.stamina;
	}

	public int getDamageTaskId() {
		return this.damageTaskId;
	}

	public void setDamageTaskId(int damageTaskId) {
		this.damageTaskId = damageTaskId;
	}

	public boolean getTakingDamage() {
		return this.takingDamage;
	}

	public void setTakingDamage(boolean takingDamage) {
		this.takingDamage = takingDamage;
	}

	@Override
	public Map<String, Object> serialize() {
		System.out.println("attempt to serialize");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("playerName", playerName);
		map.put("thirst", thirst);
		map.put("stamina", stamina);
		map.put("damageTaskId", this.damageTaskId);
		if (this.takingDamage) {
			map.put("takingDamage", "true");
		} else {
			map.put("takingDamage", "false");
		}
		return map;
	}

	public static SurvivalPlayer deserialize(Map<String, Object> map) {
		System.out.println("attempt to deserialize");
		Configuration conf = new MemoryConfiguration();
		for (Entry<String, Object> e : map.entrySet()) {
			conf.set(e.getKey(), e.getValue());
		}
		return new SurvivalPlayer(conf);
	}
}
