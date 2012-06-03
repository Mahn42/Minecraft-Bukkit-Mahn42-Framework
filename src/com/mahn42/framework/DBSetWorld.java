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
public class DBSetWorld<T extends DBRecord> extends DBSet<T> {
    
    public World world;
    
    public DBSetWorld(Class<T> aRecordClass) {
        super(aRecordClass);
    }
    
    public DBSetWorld(Class<T> aRecordClass, File aStore) {
        super(aRecordClass, aStore);
    }
    
    public DBSetWorld(Class<T> aRecordClass, File aStore, World aWorld) {
        super(aRecordClass, aStore);
        world = aWorld;
    }
    
}
