/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.io.File;
import java.util.ArrayList;
import org.bukkit.World;

/**
 *
 * @author andre
 */
public class BuildingDB<T extends Building> extends DBSetWorld<T> {

    public BuildingDB(Class<T> aRecordClass) {
        super(aRecordClass);
    }    

    public BuildingDB(Class<T> aRecordClass, World aWorld, File aFile) {
        super(aRecordClass, aFile, aWorld);
    }
    
    public ArrayList<T> getBuildings(BlockPosition aPos) {
        ArrayList<T> lResult = new ArrayList<T>();
        for(Object lObj : this) {
            T lBuilding = (T)lObj;
            if (lBuilding.isInside(aPos)) {
                lResult.add(lBuilding);
            }
        }
        return lResult;
    }
    
    public ArrayList<T> getBuildingsWithBlock(BlockPosition aPos) {
        ArrayList<T> lResult = new ArrayList<T>();
        for(Object lObj : this) {
            T lBuilding = (T)lObj;
            if (lBuilding.isInside(aPos)) {
                if (lBuilding.getBlock(aPos) != null) {
                    lResult.add(lBuilding);
                }
            }
        }
        return lResult;
    }
    
    public ArrayList<T> getBuildingsWithNoneShareableBlock(BlockPosition aPos) {
        ArrayList<T> lResult = new ArrayList<T>();
        for(Object lObj : this) {
            T lBuilding = (T)lObj;
            if (lBuilding.isInside(aPos)) {
                BuildingBlock lBlock = lBuilding.getBlock(aPos);
                if (lBlock != null && !lBlock.description.shareable) {
                    lResult.add(lBuilding);
                }
            }
        }
        return lResult;
    }
    
    public ArrayList<T> getRedStoneSensibles(BlockPosition aPos) {
        ArrayList<T> lResult = new ArrayList<T>();
        for(Object lObj : this) {
            T lBuilding = (T)lObj;
            if (lBuilding.getRedStoneSensibles(aPos) != null) {
                lResult.add(lBuilding);
            }
        }
        return lResult;
    }

    public ArrayList<T> getNameSensibles(BlockPosition aPos) {
        ArrayList<T> lResult = new ArrayList<T>();
        for(Object lObj : this) {
            T lBuilding = (T)lObj;
            if (lBuilding.getNameSensibles(aPos) != null) {
                lResult.add(lBuilding);
            }
        }
        return lResult;
    }

    public ArrayList<T> getSignSensibles(BlockPosition aPos) {
        ArrayList<T> lResult = new ArrayList<T>();
        for(Object lObj : this) {
            T lBuilding = (T)lObj;
            if (lBuilding.getSignSensibles(aPos) != null) {
                lResult.add(lBuilding);
            }
        }
        return lResult;
    }

    public ArrayList<T> getBuildingsWithDetectBlock(BlockPosition aEdge1, BlockPosition aEdge2) {
        ArrayList<T> lResult = new ArrayList<T>();
        for(Object lObj : this) {
            T lBuilding = (T)lObj;
            BuildingBlock lDetectBlock = lBuilding.getDetectBlock();
            if (lDetectBlock.position.x >= aEdge1.x && lDetectBlock.position.x <= aEdge2.x
                    && lDetectBlock.position.y >= aEdge1.y && lDetectBlock.position.y <= aEdge2.y
                    && lDetectBlock.position.z >= aEdge1.z && lDetectBlock.position.z <= aEdge2.z) {
                lResult.add(lBuilding);
            }
        }
        return lResult;
    }
}
