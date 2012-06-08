/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.io.File;
import org.bukkit.World;

/**
 *
 * @author andre
 */
public class BuildingDB<T extends DBRecord> extends DBSetWorld {

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
}
