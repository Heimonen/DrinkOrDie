package com.sanelith.datatypes;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.block.SpoutBlock;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.material.item.GenericCustomItem;
import org.getspout.spoutapi.player.SpoutPlayer;

/**
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
 * 
 * @author Christofer Heimonen
 * 
 */
public class LeatherPouch extends GenericCustomItem {

	private static String name = "Leather Pouch";
	private Plugin plugin;

	public LeatherPouch(Plugin plugin, String name, String texture) {
		super(plugin, LeatherPouch.name,
				"https://dl.dropbox.com/u/44954718/mywaterpouch2.png");
		this.setStackable(false);
		this.plugin = plugin;
	}

	// Even though the method is deprecated there is currently no other way to
	// update the inventory
	@SuppressWarnings("deprecation")
	@Override
	public boolean onItemInteract(SpoutPlayer player, SpoutBlock block,
			BlockFace face) {
		String tempEnabled = plugin.getConfig().getString("modEnabled");
		boolean modEnabled = Boolean.valueOf(tempEnabled);
		if (modEnabled) {
			Material clickedMaterial = player.getTargetBlock(null, 100)
					.getType();
			if (clickedMaterial == Material.STATIONARY_WATER
					|| clickedMaterial == Material.WATER) {
				org.bukkit.block.Block targetBlock = player.getTargetBlock(
						null, 100);
				targetBlock.breakNaturally();
				player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 10, 1);

				ItemStack hand = player.getItemInHand();
				hand.setAmount(hand.getAmount() - 1);
				player.getInventory().addItem(
						new SpoutItemStack(new LeatherWaterFilledPouch(plugin,
								"", "")));
				player.updateInventory();
				if (hand.getAmount() <= 0) {
					player.getInventory().setItemInHand(null);
				}
			}
		}
		return true;
	}
}
