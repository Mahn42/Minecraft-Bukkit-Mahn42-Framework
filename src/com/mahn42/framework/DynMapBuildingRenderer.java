/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;
import org.dynmap.markers.PolyLineMarker;

/**
 *
 * @author andre
 */
public class DynMapBuildingRenderer implements Runnable {

    protected boolean fInRun = false;
    protected Plugin fDynmap;
    
    @Override
    public void run() {
        if (!fInRun) {
            fInRun = true;
            try {
                PluginManager lPM = Framework.plugin.getServer().getPluginManager();
                fDynmap = lPM.getPlugin("dynmap");
                if(fDynmap != null && fDynmap.isEnabled()) {
                    execute();
                }
            } finally {
                fInRun = false;
            }
        }
    }

    private void execute() {
        DynmapAPI lDynmapAPI = (DynmapAPI)fDynmap; /* Get API */
        MarkerAPI lMarkerAPI = lDynmapAPI.getMarkerAPI();
        MarkerSet lMarkerSet = lMarkerAPI.getMarkerSet("framework.buildings");
        if (lMarkerSet != null) {
            lMarkerSet.deleteMarkerSet();
        }
        lMarkerSet = lMarkerAPI.createMarkerSet("framework.buildings", "Buildings", null, false);
        if (lMarkerSet == null) {
            return;
        }
        ArrayList<BuildingDB> lDBs = Framework.plugin.getBuildingDetector().getDBs();
        double[] lXs = new double[5];
        double[] lYs = new double[5];
        double[] lZs = new double[5];
        int lCount = 0;
        for(BuildingDB<Building> lDB : lDBs) {
            for(Building lBuilding : lDB) {
                lXs[0] = lBuilding.edge1.x;
                lYs[0] = lBuilding.edge1.y;
                lZs[0] = lBuilding.edge1.z;
                lXs[1] = lBuilding.edge2.x;
                lYs[1] = lBuilding.edge2.y;
                lZs[1] = lBuilding.edge1.z;
                lXs[2] = lBuilding.edge2.x;
                lYs[2] = lBuilding.edge2.y;
                lZs[2] = lBuilding.edge2.z;
                lXs[3] = lBuilding.edge1.x;
                lYs[3] = lBuilding.edge1.y;
                lZs[3] = lBuilding.edge2.z;
                lXs[4] = lBuilding.edge1.x;
                lYs[4] = lBuilding.edge1.y;
                lZs[4] = lBuilding.edge1.z;
                PolyLineMarker lRect = lMarkerSet.createPolyLineMarker(lBuilding.key, lBuilding.getName(), true, lDB.world.getName(), lXs, lYs, lZs, false);
                if (lRect != null) {
                    lRect.setLineStyle(3, 0.75, 0xF0A0A0);
                    //Framework.plugin.getLogger().info(":" + lBuilding.edge1 + " to " + lBuilding.edge2);
                    lCount++;
                }
            }
        }
        //Framework.plugin.getLogger().info("DynMap Buildings count = " + lCount);
    }
    
}
