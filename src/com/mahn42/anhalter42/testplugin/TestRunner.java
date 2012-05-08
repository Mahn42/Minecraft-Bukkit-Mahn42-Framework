/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.testplugin;

import org.bukkit.Location;
import org.bukkit.block.Block;


/**
 *
 * @author andre
 */
public class TestRunner implements Runnable {
    
    public Testplugin plugin;
    public Location location = null;
    
    protected int fOldBlockID = 0;
    protected Location fOldLocation = null;
    
    public TestRunner(Testplugin aPlugin) {
        plugin = aPlugin;
    }
    
    @Override
    public void run() {
        if (location != null) {
            plugin.log.info("run");
            Block lBlock = location.getBlock();
            if (fOldLocation != null) {
                fOldLocation.getBlock().setTypeId(fOldBlockID);
            }
            fOldBlockID = lBlock.getTypeId();
            lBlock.setTypeId(18); // Blatt
            //location.getWorld().sp
            fOldLocation = location;
            location = null;
        }
    }
}
