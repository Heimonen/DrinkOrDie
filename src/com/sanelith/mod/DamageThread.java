package com.sanelith.mod;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
