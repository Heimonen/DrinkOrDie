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

import com.sanelith.mod.PlayerListener;

/**
 * 
 * @author Christofer Heimonen
 *
 */
public class LeatherWaterFilledPouch extends GenericCustomItem{

	private final int waterRestored = 6;
	private Plugin plugin;

	public LeatherWaterFilledPouch(Plugin plugin, String name, String texture) {
		super(plugin, "Water Leather Pouch", "https://dl.dropbox.com/u/44954718/myWaterPouchWater5.png");
		this.plugin = plugin;
	}

	//Even though the method is deprecated there is currently no other way to update the inventory
	@SuppressWarnings("deprecation") 
	@Override
	public boolean onItemInteract(SpoutPlayer player, SpoutBlock block, BlockFace face) {
		String tempEnabled = plugin.getConfig().getString("modEnabled");
		boolean modEnabled = Boolean.valueOf(tempEnabled);
		if(modEnabled) {
			int playerThirst = (int)PlayerListener.playerMap.get(player.getName()).getThirst();
			org.bukkit.block.Block targetBlock = player.getTargetBlock(null, 1000);
			if(targetBlock.getType() != Material.WATER || targetBlock.getType() != Material.STATIONARY_WATER) {
				if(playerThirst < 20) {
					playerThirst += waterRestored;
					playerThirst = playerThirst > 20 ? 20 : playerThirst;
					PlayerListener.playerMap.get(player.getName()).setThirst(playerThirst);
					player.playSound(player.getLocation(), Sound.DRINK, 10, 1);			

					ItemStack hand = player.getItemInHand();
					hand.setAmount(hand.getAmount() - 1);
					player.getInventory().addItem(new SpoutItemStack(new LeatherPouch(plugin, "", "")));
					player.updateInventory();
					if(hand.getAmount() <= 0) {
						player.getInventory().setItemInHand(null);
					}
				}
			}
		}
		return true;
	}
}
