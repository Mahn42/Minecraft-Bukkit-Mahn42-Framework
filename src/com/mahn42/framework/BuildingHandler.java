/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author andre
 */
public interface BuildingHandler {
    public Building insert(Building aBuilding);
    public boolean remove(Building aBuilding);

    
    public boolean breakBlock(BlockBreakEvent aEvent, Building aBuilding);
    public boolean redstoneChanged(BlockRedstoneEvent aEvent, Building aBuilding);
    public boolean playerInteract(PlayerInteractEvent aEvent, Building aBuilding);
    public boolean nameChanged(SignChangeEvent aEvent, Building aBuilding);
    public boolean signChanged(SignChangeEvent aEvent, Building aBuilding);
    public BuildingDB getDB(World aWorld);
    public void nextConfiguration(Building aBuilding, BlockPosition position, Player aPlayer);
    public void playerInteractWith(Building aBuilding, BlockPosition position, Player aPlayer, Material aInHand);
    public JavaPlugin getPlugin();
}
