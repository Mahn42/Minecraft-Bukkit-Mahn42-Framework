/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author andre
 */
public class BuildingHandlerBase implements BuildingHandler {

    @Override
    public boolean breakBlock(BlockBreakEvent aEvent, Building aBuilding) {
        return remove(aBuilding);
    }

    @Override
    public boolean redstoneChanged(BlockRedstoneEvent aEvent, Building aBuilding) {
        return false;
    }

    @Override
    public boolean playerInteract(PlayerInteractEvent aEvent, Building aBuilding) {
        Player lPlayer = aEvent.getPlayer();
        Building lBuilding = insert(aBuilding);
        if (lBuilding != null) {
            lPlayer.sendMessage(Framework.plugin.getText(lPlayer, "&aBuilding %s found.", lBuilding.getName()));
        } else {
            lPlayer.sendMessage(Framework.plugin.getText(lPlayer, "&cBuilding %s found but not accepted!", aBuilding.getName()));
        }
        return lBuilding != null;
    }

    @Override
    public BuildingDB getDB(World aWorld) {
        return null;
    }

    @Override
    public boolean nameChanged(SignChangeEvent aEvent, Building aBuilding) {
        aBuilding.name = aEvent.getLine(0);
        return true;
    }

    @Override
    public boolean signChanged(SignChangeEvent aEvent, Building aBuilding) {
        return false;
    }

    @Override
    public void nextConfiguration(Building aBuilding, BlockPosition position, Player aPlayer) {
    }

    @Override
    public Building insert(Building aBuilding) {
        return null;
    }

    @Override
    public boolean remove(Building aBuilding) {
        BuildingDB lDB = getDB(aBuilding.world);
        if (lDB != null) {
            lDB.remove(aBuilding);
            return true;
        } else {
            return false;
        }
    }
    
}
