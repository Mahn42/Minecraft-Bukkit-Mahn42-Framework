/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.List;
import org.bukkit.World;

/**
 *
 * @author andre
 */
public class ProjectionAreasRunner implements Runnable {

    @Override
    public void run() {
        List<World> lWorlds = Framework.plugin.getServer().getWorlds();
        for(World lWorld : lWorlds) {
            ProjectionAreas lAreas = Framework.plugin.getProjectionAreas(lWorld, false);
            if (lAreas != null) {
                lAreas.run();
            }
        }
    }
    
}
