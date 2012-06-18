/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import org.bukkit.World;
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
        return false;
    }

    @Override
    public boolean redstoneChanged(BlockRedstoneEvent aEvent, Building aBuilding) {
        return false;
    }

    @Override
    public boolean playerInteract(PlayerInteractEvent aEvent, Building aBuilding) {
        return false;
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
    
}
