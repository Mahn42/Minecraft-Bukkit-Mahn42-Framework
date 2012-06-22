/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author andre
 */
public class WorldDBList<T extends DBSetWorld> implements DBSave, Iterable<T> {

    public Plugin plugin;
    public String name;
    protected HashMap<String, T> fDBs = new HashMap<String, T>();
    protected Class<T> fDBClass;
    
    public WorldDBList(Class<T> aDBSetClass, String aName, Plugin aPlugin) {
        name = aName;
        plugin = aPlugin;
        fDBClass = aDBSetClass;
    }
    
    public WorldDBList(Class<T> aDBSetClass, Plugin aPlugin) {
        name = aPlugin.getName();
        plugin = aPlugin;
        fDBClass = aDBSetClass;
    }
    
    public T getDB(World aWorld) {
        String lWorldName = aWorld.getName();
        if (!fDBs.containsKey(lWorldName)) {
            File lFolder = aWorld.getWorldFolder();
            if (!lFolder.exists()) {
                lFolder.mkdirs();
            }
            String lPath = lFolder.getPath();
            lPath = lPath + File.separatorChar + lWorldName + "_" + name + ".csv";
            File lFile = new File(lPath);
            T lDB = null;
            try {
                lDB = fDBClass.newInstance();
            } catch (InstantiationException ex) {
                Logger.getLogger(WorldDBList.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(WorldDBList.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (lDB != null) {
                lDB.world = aWorld;
                lDB.file = lFile;
                lDB.load();
                plugin.getLogger().info("Datafile " + lFile.toString() + " loaded. (Records:" + new Integer(lDB.size()).toString() + ")");
                fDBs.put(lWorldName, lDB);
            }
        }
        return fDBs.get(lWorldName);
    }
    
    public T getDB(String aWorldName) {
        World lWorld = plugin.getServer().getWorld(aWorldName);
        return getDB(lWorld);
    }
    
    public ArrayList<World> getWorlds() {
        ArrayList<World> lResult = new ArrayList<World>();
        for (String lName : fDBs.keySet()) {
            World lWorld = Framework.plugin.getServer().getWorld(lName);
            lResult.add(lWorld);
        }
        return lResult;
    }
    
    @Override
    public void save() {
        for(T lDB : fDBs.values()) {
            lDB.save();
        }
    }

    @Override
    public Iterator<T> iterator() {
        return fDBs.values().iterator();
    }
}
