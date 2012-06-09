/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import org.bukkit.World;

/**
 *
 * @author andre
 */
public class DBRecordWorld extends DBRecord {
    public World world;
    
    public void setWorld(World aWorld) {
        world = aWorld;
    }

    @Override
    protected void added(DBSet aSet) {
        super.added(aSet);
        if (aSet instanceof DBSetWorld) {
            world = ((DBSetWorld)aSet).world;
        }
    }

    @Override
    public void cloneFrom(DBRecord aRecord) {
        super.cloneFrom(aRecord);
        if (aRecord instanceof DBRecordWorld) {
            world = ((DBRecordWorld)aRecord).world;
        }
    }
}
