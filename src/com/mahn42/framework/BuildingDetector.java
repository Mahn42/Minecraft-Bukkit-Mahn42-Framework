/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import java.util.logging.Logger;
import org.bukkit.World;

/**
 *
 * @author andre
 */
public class BuildingDetector {
    protected ArrayList<BuildingDescription> fDescriptions;
    
    public BuildingDetector() {
        fDescriptions = new ArrayList<BuildingDescription>();
    }
    
    public ArrayList<Building> detect(World aWorld, BlockPosition aPos1, BlockPosition aPos2) {
        //TODO sort desc with most related to first scan
        ArrayList<Building> lResult = new ArrayList<Building>();
        int dx = aPos1.x > aPos2.x ? -1 : 1;
        int dy = aPos1.y > aPos2.y ? -1 : 1;
        int dz = aPos1.z > aPos2.z ? -1 : 1;
        //Logger.getLogger("detect").info("count " + new Integer(fDescriptions.size()));
        //Logger.getLogger("detect").info(aPos1.toString() + " - " + aPos2.toString());
        for(BuildingDescription lDesc : fDescriptions) {
            //Logger.getLogger("detect").info("teste " + lDesc.name);
            for(int lX = aPos1.x; lX <= aPos2.x; lX+=dx) {
                for(int lY = aPos1.y; lY <= aPos2.y; lY+=dy) {
                    for(int lZ = aPos1.z; lZ <= aPos2.z; lZ+=dz) {
                        //TODO check if pos in building of any DB
                        //Logger.getLogger("detect").info("teste " + new Integer(lX) + "," + new Integer(lY) + "," + new Integer(lZ));
                        Building aBuilding = matchDescription(lDesc, aWorld, lX, lY, lZ);
                        if (aBuilding != null) {
                            lResult.add(aBuilding);
                        }
                    }
                }
            }
        }
        return lResult;
    }

    private Building matchDescription(BuildingDescription lDesc, World aWorld, int lX, int lY, int lZ) {
        return lDesc.matchDescription(aWorld, lX, lY, lZ);
    }

    protected boolean fShouldUpdateHandlers = false;
    
    public BuildingDescription newDescription(String aName) {
        BuildingDescription lDesc = new BuildingDescription(aName);
        fDescriptions.add(lDesc);
        fShouldUpdateHandlers = true;
        return lDesc;
    }
    
    protected ArrayList<BuildingHandler> fHandlers = new ArrayList<BuildingHandler>();
    
    public ArrayList<BuildingHandler> getHandlers() {
        if (fShouldUpdateHandlers) {
            fHandlers.clear();
            for(BuildingDescription lDesc : fDescriptions) {
                if (!fHandlers.contains(lDesc.handler)) {
                    fHandlers.add(lDesc.handler);
                }
            }
        }
        return fHandlers;
    }
}
