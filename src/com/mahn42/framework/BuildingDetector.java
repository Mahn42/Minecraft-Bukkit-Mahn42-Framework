/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.World;
import org.bukkit.entity.Entity;

/**
 *
 * @author andre
 */
public class BuildingDetector {
    protected ArrayList<BuildingDescription> fDescriptions;
    
    public BuildingDetector() {
        fDescriptions = new ArrayList<BuildingDescription>();
    }
    
    public class BuildingDescriptionComparator implements Comparator<BuildingDescription> {

        @Override
        public int compare(BuildingDescription t, BuildingDescription t1) {
            return t1.detectPriority - t.detectPriority;
        }
        
    }
    
    public List<BuildingDescription> getDescriptions() {
        ArrayList<BuildingDescription> lRes = new ArrayList<BuildingDescription>();
        lRes.addAll(fDescriptions);
        return lRes;
    }
    
    public ArrayList<Building> detect(World aWorld, BlockPosition aPos1, BlockPosition aPos2) {
        HashMap<BlockPosition, Entity> lEntities = new HashMap<BlockPosition, Entity>();
        ArrayList<Building> lResult = new ArrayList<Building>();
        int dx = aPos1.x > aPos2.x ? -1 : 1;
        int dy = aPos1.y > aPos2.y ? -1 : 1;
        int dz = aPos1.z > aPos2.z ? -1 : 1;
        getHandlers();
        List<Entity> lents = aWorld.getEntities();
        for(Entity lent : lents) {
            BlockPosition lPos = new BlockPosition(lent.getLocation());
            lEntities.put(lPos, lent);
        }
        for(BuildingDescription lDesc : fDescriptions) {
            if (lDesc.active) {
                for(int lX = aPos1.x; lX <= aPos2.x; lX+=dx) {
                    for(int lY = aPos1.y; lY <= aPos2.y; lY+=dy) {
                        for(int lZ = aPos1.z; lZ <= aPos2.z; lZ+=dz) {
                            //Logger.getLogger("detect").info("teste " + new Integer(lX) + "," + new Integer(lY) + "," + new Integer(lZ));
                            if (getBuildingsWithNoneShareableBlock(new BlockPosition(lX, lY, lZ)).isEmpty()) {
                                Building aBuilding = matchDescription(lDesc, aWorld, lEntities, lX, lY, lZ);
                                if (aBuilding != null) {
                                    lResult.add(aBuilding);
                                    Framework.plugin.getLogger().info("detected " + aBuilding.getName());
                                }
                            } else {
                                // there are always buildings
                            }
                        }
                    }
                }
            }
        }
        return lResult;
    }

    private Building matchDescription(BuildingDescription lDesc, World aWorld, Map<BlockPosition, Entity> aEntities, int lX, int lY, int lZ) {
        return lDesc.matchDescription(aWorld, aEntities, lX, lY, lZ);
    }

    protected boolean fShouldUpdateHandlers = false;
    
    public BuildingDescription newDescription(String aName) {
        BuildingDescription lDesc = new BuildingDescription(aName);
        addDescription(lDesc);
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
            synchronized(fDescriptions) {
                java.util.Collections.sort(fDescriptions, new BuildingDescriptionComparator());
                fHandlers.clear();
                for(BuildingDescription lDesc : fDescriptions) {
                    if (lDesc.handler != null) {
                        if (!fHandlers.contains(lDesc.handler)) {
                            fHandlers.add(lDesc.handler);
                        }
                    }
                }
                fDBs.clear();
                List<World> lWorlds = Framework.plugin.getServer().getWorlds();
                for(BuildingHandler lHandler : fHandlers) {
                    for(World lWorld : lWorlds) {
                        BuildingDB lDB = lHandler.getDB(lWorld);
                        if (lDB != null) {
                            if (!fDBs.contains(lDB)) {
                                fDBs.add(lDB);
                            }
                        }
                    }
                }
            }
        }
        return new ArrayList<BuildingHandler>(fHandlers);
    }
    
    public ArrayList<BuildingDB> getDBs() {
        getHandlers();
        return new ArrayList<BuildingDB>(fDBs);
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

    public ArrayList<Building> getBuildingsWithNoneShareableBlock(BlockPosition aPos) {
        ArrayList<Building> lResult = new ArrayList<Building>();
        ArrayList<BuildingDB> lDBs = getDBs();
        for(BuildingDB lDB : lDBs) {
            ArrayList<Building> lBuildings = lDB.getBuildingsWithNoneShareableBlock(aPos);
            if (!lBuildings.isEmpty()) {
                lResult.addAll(lBuildings);
            }
        }
        return lResult;
    }

    public ArrayList<Building> getBuildingsWithDetectBlock(BlockPosition aEdge1, BlockPosition aEdge2) {
        BlockPosition lEdge1, lEdge2;
        lEdge1 = aEdge1.getMinPos(aEdge2);
        lEdge2 = aEdge1.getMaxPos(aEdge2);
        ArrayList<Building> lResult = new ArrayList<Building>();
        ArrayList<BuildingDB> lDBs = getDBs();
        for(BuildingDB lDB : lDBs) {
            ArrayList<Building> lBuildings = lDB.getBuildingsWithDetectBlock(lEdge1, lEdge2);
            if (!lBuildings.isEmpty()) {
                lResult.addAll(lBuildings);
            }
        }
        return lResult;
    }
}
