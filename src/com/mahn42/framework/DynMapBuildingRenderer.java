/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.CircleMarker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerIcon;
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
        double[] lXs = new double[9];
        double[] lYs = new double[9];
        double[] lZs = new double[9];
        int lCount = 0;
        for(BuildingDB<Building> lDB : lDBs) {
            for(Building lBuilding : lDB) {
                if (lBuilding.description.visibleOnMap) {
                    String lIconName = lBuilding.getIconName();
                    if (lIconName != null && !lIconName.isEmpty()) {
                        MarkerIcon markerIcon = lMarkerAPI.getMarkerIcon(lIconName);
                        if (markerIcon == null) {
                            Framework.plugin.log("fw", "Icon '" + lIconName + "' not found!");
                            markerIcon = lMarkerAPI.getMarkerIcon("default");
                        }
                        BlockPosition lPos = lBuilding.edge1.getMidPoint(lBuilding.edge2);
                        lMarkerSet.createMarker(
                                lBuilding.key,
                                lBuilding.getName(),
                                lDB.world.getName(),
                                lPos.x,
                                lPos.y,
                                lPos.z,
                                markerIcon,
                                false);
                        lCount++;
                    } else if (lBuilding.description.circleRadius > 0) {
                        CircleMarker lCircleMarker = lMarkerSet.createCircleMarker(
                                lBuilding.key,
                                lBuilding.getName(),
                                false,
                                lDB.world.getName(), 
                                lBuilding.edge1.x,
                                lBuilding.edge1.y,
                                lBuilding.edge1.z,
                                lBuilding.description.circleRadius,
                                lBuilding.description.circleRadius,
                                false);
                        lCircleMarker.setLineStyle(1, 1.0, lBuilding.description.color);
                        lCircleMarker.setFillStyle(0.5, lBuilding.description.color);
                    } else {
                        lXs[0] = lBuilding.edge1.x;
                        lYs[0] = lBuilding.edge2.y;
                        lZs[0] = lBuilding.edge1.z;
                        lXs[1] = lBuilding.edge1.x;
                        lYs[1] = lBuilding.edge1.y;
                        lZs[1] = lBuilding.edge1.z;
                        lXs[2] = lBuilding.edge2.x;
                        lYs[2] = lBuilding.edge1.y;
                        lZs[2] = lBuilding.edge1.z;
                        lXs[3] = lBuilding.edge2.x;
                        lYs[3] = lBuilding.edge2.y;
                        lZs[3] = lBuilding.edge1.z;
                        lXs[4] = lBuilding.edge2.x;
                        lYs[4] = lBuilding.edge2.y;
                        lZs[4] = lBuilding.edge2.z;
                        lXs[5] = lBuilding.edge2.x;
                        lYs[5] = lBuilding.edge1.y;
                        lZs[5] = lBuilding.edge2.z;
                        lXs[6] = lBuilding.edge1.x;
                        lYs[6] = lBuilding.edge1.y;
                        lZs[6] = lBuilding.edge2.z;
                        lXs[7] = lBuilding.edge1.x;
                        lYs[7] = lBuilding.edge2.y;
                        lZs[7] = lBuilding.edge2.z;
                        lXs[8] = lBuilding.edge1.x;
                        lYs[8] = lBuilding.edge2.y;
                        lZs[8] = lBuilding.edge1.z;
                        PolyLineMarker lRect = lMarkerSet.createPolyLineMarker(
                                lBuilding.key,
                                lBuilding.getName(),
                                true,
                                lDB.world.getName(),
                                lXs,
                                lYs,
                                lZs,
                                false);
                        if (lRect != null) {
                            lRect.setLineStyle(3, 0.75, 0xF0A0A0);
                            //Framework.plugin.getLogger().info(":" + lBuilding.edge1 + " to " + lBuilding.edge2);
                            lCount++;
                        }
                    }
                }
            }
        }
        //Framework.plugin.getLogger().info("DynMap Buildings count = " + lCount);
    }
    
}
