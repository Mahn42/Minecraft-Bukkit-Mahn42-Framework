/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.testplugin;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 *
 * @author andre
 */
public class TestASyncRunner implements Runnable {
    
    public Testplugin plugin;
    public TestRunner runner;
    
    public TestASyncRunner(Testplugin aPlugin, TestRunner aRunner) {
        plugin = aPlugin;
        runner = aRunner;
    }
    
    @Override
    public void run() {
        plugin.log.info("async run");
        Player[] lPlayers = plugin.getServer().getOnlinePlayers();
        for (int i = 0; i < lPlayers.length; i++) {
            Player lPlayer = lPlayers[i];
            Block lBlock = lPlayer.getTargetBlock(null, 20);
            Location lLoc = lBlock.getLocation();
            if (!lBlock.isEmpty()) {
                runner.location = lLoc;
            }
            //int lID = lBlock.getTypeId();
            break;
        }
    }
}
