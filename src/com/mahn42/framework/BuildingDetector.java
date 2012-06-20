/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import java.util.List;
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
                        if (getBuildings(new BlockPosition(lX, lY, lZ)).isEmpty()) {
                            Building aBuilding = matchDescription(lDesc, aWorld, lX, lY, lZ);
                            if (aBuilding != null) {
                                lResult.add(aBuilding);
                                Logger.getLogger("detect").info("add " + aBuilding.getName());
                            }
                        } else {
                            // there are always buildings
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
    
    public void addDescription(BuildingDescription aDesc) {
        fDescriptions.add(aDesc);
        fShouldUpdateHandlers = true;
    }
    
    protected ArrayList<BuildingHandler> fHandlers = new ArrayList<BuildingHandler>();
    protected ArrayList<BuildingDB> fDBs = new ArrayList<BuildingDB>();
    
    public ArrayList<BuildingHandler> getHandlers() {
        if (fShouldUpdateHandlers) {
            fHandlers.clear();
            for(BuildingDescription lDesc : fDescriptions) {
                if (!fHandlers.contains(lDesc.handler)) {
                    fHandlers.add(lDesc.handler);
                }
            }
            fDBs.clear();
            List<World> lWorlds = Framework.plugin.getServer().getWorlds();
            for(BuildingHandler lHandler : fHandlers) {
                for(World lWorld : lWorlds) {
                    BuildingDB lDB = lHandler.getDB(lWorld);
                    if (!fDBs.contains(lDB)) {
                        fDBs.add(lDB);
                    }
                }
            }
        }
        return fHandlers;
    }
    
    public ArrayList<BuildingDB> getDBs() {
        getHandlers();
        return fDBs;
    }
    
    public ArrayList<Building> getBuildings(BlockPosition aPos) {
        ArrayList<Building> lResult = new ArrayList<Building>();
        ArrayList<BuildingDB> lDBs = getDBs();
        for(BuildingDB lDB : lDBs) {
            ArrayList<Building> lBuildings = lDB.getBuildings(aPos);
            if (!lBuildings.isEmpty()) {
                lResult.addAll(lBuildings);
            }
        }
        return lResult;
    }
}
