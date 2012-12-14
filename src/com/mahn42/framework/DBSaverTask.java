/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;

/**
 *
 * @author andre
 */
public class DBSaverTask implements Runnable {

    protected ArrayList<DBSave> fSaver = new ArrayList<DBSave>();
    
    public void registerSaver(DBSave aSaver) {
        if (!fSaver.contains(aSaver)) {
            fSaver.add(aSaver);
        }
    }
    
    public void unregisterSaver(DBSave aSaver) {
        if (fSaver.contains(aSaver)) {
            fSaver.remove(aSaver);
        }
    }
    
    @Override
    public void run() {
        Framework.plugin.getLogger().info("Saving DBs...");
        for(DBSave lSaver : fSaver) {
            IBeforeAfterExecute lx = null;
            if (lSaver instanceof IBeforeAfterExecute) {
                lx = (IBeforeAfterExecute)lSaver;
            }
            if (lx != null) {
                lx.beforeExecute(this);
            }
            lSaver.save(); 
            if (lx != null) {
                lx.afterExecute(this);
            }
        }
        Framework.plugin.getLogger().info("Saving DBs done.");
    }
}
