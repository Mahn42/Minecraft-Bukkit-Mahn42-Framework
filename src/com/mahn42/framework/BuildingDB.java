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
public class BuildingDB<T extends Building> extends DBSetWorld {

    public BuildingDB() {
        super(Building.class);
    }
    
    public BuildingDB(Class<T> aRecordClass) {
        super(aRecordClass);
    }    

    public BuildingDB(World aWorld, File aFile) {
        super(Building.class, aFile, aWorld);
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
}
