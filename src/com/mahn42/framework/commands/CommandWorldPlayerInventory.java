/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework.commands;

import com.mahn42.framework.Framework;
import com.mahn42.framework.WorldPlayerInventory;
import java.util.ArrayList;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author andre
 */
public class CommandWorldPlayerInventory implements CommandExecutor{

    //fw_world_pinv (<playername> [<worldname>]) | ([<playername> [<worldname>]])
    @Override
    public boolean onCommand(CommandSender aCommandSender, Command aCommand, String aString, String[] aStrings) {
        World lWorld = null;
        Player lPlayer = null;
        String lPlayerName = null;
        String lWorldName = null;
        if (aCommandSender instanceof Player) {
            lPlayer = (Player)aCommandSender;
            lPlayerName = lPlayer.getName();
            lWorld = lPlayer.getWorld();
            lWorldName = lWorld.getName();
        }
        if (aStrings.length > 0) {
            OfflinePlayer lOfflinePlayer = Framework.plugin.getServer().getOfflinePlayer(aStrings[0]);
            if (lOfflinePlayer != null) {
                lPlayerName = lOfflinePlayer.getName();
            } else {
                World lW = Framework.plugin.getServer().getWorld(aStrings[0]);
                if (lW != null) {
                    lWorldName = lW.getName();
                }
            }
            if (aStrings.length > 1) {
                World lW = Framework.plugin.getServer().getWorld(aStrings[1]);
                if (lW != null) {
                    lWorldName = lW.getName();
                }
            }
        }
        WorldPlayerInventory lPInv = Framework.plugin.getWorldPlayerInventoryDB().get(lWorldName, lPlayerName);
        if (lPInv != null) {
            ArrayList<ItemStack> lStacks = lPInv.getAsItemStacks();
            for(ItemStack lStack : lStacks) {
                aCommandSender.sendMessage("" + lStack);
            }
        } else {
            aCommandSender.sendMessage(Framework.plugin.getText(aCommandSender, "no inventory for player %s in world %s found!", lPlayerName, lWorldName));
        }
        return true;
    }
}
