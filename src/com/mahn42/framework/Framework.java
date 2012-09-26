/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author andre
 */
public class Framework extends JavaPlugin {

    protected class __Messenger implements Messenger {
        @Override
        public boolean sendPlayerMessage(String aFromPlayer, String aToPlayerName, String aMessage) {
            return true;
        }

        @Override
        public boolean sendGroupMessage(String aFromPlayer, String aToGroupName, String aMessage) {
            return true;
        }

        @Override
        public boolean recallPlayerMessages(String aFromPlayer, String aToPlayerName) {
            return true;
        }

        @Override
        public boolean recallGroupMessages(String aFromPlayer, String aToGroupName) {
            return true;
        }

        @Override
        public boolean recallPlayerMessages(String aFromPlayer) {
            return true;
        }
    }

    protected class __PlayerManager implements PlayerManager {

        @Override
        public void increaseSocialPoint(String aPlayerName, String aName, int aAmount, String aReason, String aChargePlayerName) {
        }

        @Override
        public List<SocialPoint> getSocialPoints(String aPlayerName) {
            return new ArrayList<SocialPoint>();
        }

        @Override
        public List<SocialPointHistory> getSocialPointHistory(String aPlayerName, String aName) {
            return new ArrayList<SocialPointHistory>();
        }

        @Override
        public SocialPoint getSocialPoint(String aPlayerName, String aName) {
            return null;
        }
        
    }
    
    public static Framework plugin;
    public int configSyncBlockSetterTicks = 2;
    public int configDBSaverTicks = 18000;
    public String configLanguage = "DE_de";
    
    //protected HashMap<String, BlockPosition> fPositionMarker = new HashMap<String, BlockPosition>();
    protected HashMap<String, Boolean> fDebugSet = new HashMap<String, Boolean>();
    protected SyncBlockSetter fSyncBlockSetter;
    protected DBSaverTask fSaverTask;
    protected DynMapBuildingRenderer fDynMapTask;
    protected BuildingDetector fBuildingDetector;
    protected HashMap<String, PlayerBuildings> fPlayerBuildings;
    protected Messenger fMessenger = null;
    protected PlayerManager fPlayerManager = null;
    protected HashMap<World, RestrictedRegions> fRestrictedRegions = new HashMap<World, RestrictedRegions>();
    protected Properties fLang = null;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BlockArea lArea = new BlockArea(10, 10, 10);
        Logger.getAnonymousLogger().info("Area Item 0 = " + lArea.items.get(0));
    }

    public RestrictedRegions getRestrictedRegions(World aWorld, boolean aCreate) {
        RestrictedRegions lResult = fRestrictedRegions.get(aWorld);
        if (lResult == null && aCreate) {
            lResult = new RestrictedRegions();
            fRestrictedRegions.put(aWorld, lResult);
        }
        return lResult;
    }
    
    public boolean isDebugSet(String aName) {
        Boolean lValue = fDebugSet.get(aName);
        return lValue != null && lValue.booleanValue();
    }
    
    public void setDebugSet(String aName, boolean aValue) {
        fDebugSet.put(aName, new Boolean(aValue));
    }
    
    /*
    public void setPositionMarker(String aName, BlockPosition aPos) {
        fPositionMarker.put(aName, aPos.clone());
    }
    
    public BlockPosition getPositionMarker(String aName) {
        BlockPosition lPos = fPositionMarker.get(aName);
        return lPos == null ? null : lPos.clone();
    }
    */
    
    public Messenger getMessenger() {
        if (fMessenger != null) {
            return fMessenger;
        } else {
            return new __Messenger();
        }
    }
    
    public boolean registerMessenger(Messenger aMessenger) {
        if (fMessenger == null) {
            fMessenger = aMessenger;
            return true;
        } else {
            return false;
        }
    }
    
    public PlayerManager getPlayerManager() {
        if (fPlayerManager != null) {
            return fPlayerManager;
        } else {
            return new __PlayerManager();
        }
    }
    
    public boolean registerPlayerManager(PlayerManager aPlayerManager) {
        if (fPlayerManager == null) {
            fPlayerManager = aPlayerManager;
            return true;
        } else {
            return false;
        }
    }
    
    public Properties getLanguage(JavaPlugin aPlugin, String aLanguage) {
        Properties lProps = new Properties();
        String lFileName = String.format("%s.properties", aLanguage != null ? aLanguage : configLanguage);
        try {
            InputStream lStream = null;
            File lExt = new File(aPlugin.getDataFolder().getPath() + File.separatorChar + lFileName);
            if (lExt.exists()) {
                lStream = new BufferedInputStream(new FileInputStream(lExt));
            } else {
                lStream = aPlugin.getResource(lFileName);
            }
            if (lStream != null) {
                try {
                    lProps.load(lStream);
                } finally {
                    lStream.close();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lProps;
    }
    
    @Override
    public void onEnable() { 
        plugin = this;
        fPlayerBuildings = new HashMap<String, PlayerBuildings>();
        fBuildingDetector = new BuildingDetector();
        readFrameworkConfig();
        fLang = getLanguage(this, configLanguage);
        fSyncBlockSetter = new SyncBlockSetter();
        getServer().getScheduler().scheduleSyncRepeatingTask(this, fSyncBlockSetter, 10, configSyncBlockSetterTicks);
        fSaverTask = new DBSaverTask();
        getServer().getScheduler().scheduleSyncRepeatingTask(this, fSaverTask, 100, configDBSaverTicks);
        fDynMapTask = new DynMapBuildingRenderer();
        getServer().getScheduler().scheduleSyncRepeatingTask(this, fDynMapTask, 100, 20);
        
        getCommand("fw_bd_dump").setExecutor(new CommandBD_Dump());
        getCommand("fw_bd_create").setExecutor(new CommandBD_Create());
        getCommand("fw_bd_detect").setExecutor(new CommandBD_Detect());
        getCommand("fw_set_spawn").setExecutor(new CommandSetSpawn());
        getCommand("fw_save").setExecutor(new CommandSave());
        getCommand("fw_debug").setExecutor(new CommandDebugSet());
        getCommand("fw_createworld").setExecutor(new CommandCreateWorld());
        getCommand("fw_tpworld").setExecutor(new CommandTPWorld());

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new BlockListener(), this);
    }

    @Override
    public void onDisable() {
        fSaverTask.run();
        getServer().getScheduler().cancelTasks(this);
    }

    public void setTypeAndData(Location aLocation, Material aMaterial, byte aData, boolean  aPhysics) {
        fSyncBlockSetter.setTypeAndData(aLocation, aMaterial, aData, aPhysics);
    }
    
    public BuildingDescription getBuildingDescription(String aName) {
        for(BuildingDescription lDesc : fBuildingDetector.fDescriptions) {
            if (aName.equalsIgnoreCase(lDesc.name)) {
                return lDesc;
            }
        }
        return null;
    }
    
    public BuildingDetector getBuildingDetector() {
        return fBuildingDetector;
    }
    
    public PlayerBuildings getPlayerBuildings(String aPlayerName) {
        return getPlayerBuildings(getServer().getPlayer(aPlayerName));
    }

    public PlayerBuildings getPlayerBuildings(Player aPlayer) {
        PlayerBuildings lBuildings = fPlayerBuildings.get(aPlayer.getName());
        if (lBuildings == null) {
            lBuildings = new PlayerBuildings(aPlayer.getName());
            lBuildings.playerPos = new BlockPosition(aPlayer.getLocation());
            fPlayerBuildings.put(lBuildings.playerName, lBuildings);
        }
        return lBuildings;
    }

    public void registerSaver(DBSave aSaver) {
        fSaverTask.registerSaver(aSaver);
    }
    
    public void unregisterSaver(DBSave aSaver) {
        fSaverTask.unregisterSaver(aSaver);
    }

    private void readFrameworkConfig() {
        FileConfiguration lConfig = getConfig();
        configSyncBlockSetterTicks = lConfig.getInt("SyncBlockSetter.Ticks");
        configLanguage = lConfig.getString("Language");
    }
    
    public String getText(String aText, Object... aObjects) {
        if (fLang == null) {
            return String.format(aText, aObjects);
        } else {
            String lText = ChatColor.translateAlternateColorCodes('&', String.format(fLang.getProperty(aText, aText), aObjects));
            return lText;
        }
    }
    
    public void log(String aDebugOption, String aText) {
        if (isDebugSet(aDebugOption)) {
            getLogger().info(aText);
        }
    }
    
    public static boolean isSpade(Material aMaterial) {
        return aMaterial != null
                && (aMaterial.equals(Material.STONE_SPADE)
                || aMaterial.equals(Material.WOOD_SPADE)
                || aMaterial.equals(Material.IRON_SPADE)
                || aMaterial.equals(Material.DIAMOND_SPADE)
                || aMaterial.equals(Material.GOLD_SPADE));
    }

    public static boolean isAxe(Material aMaterial) {
        return aMaterial != null
                && (aMaterial.equals(Material.STONE_AXE)
                || aMaterial.equals(Material.WOOD_AXE)
                || aMaterial.equals(Material.IRON_AXE)
                || aMaterial.equals(Material.DIAMOND_AXE)
                || aMaterial.equals(Material.GOLD_AXE));
    }
    
    public static boolean isSign(Material aMaterial) {
        return aMaterial != null
                && (aMaterial.equals(Material.SIGN)
                || aMaterial.equals(Material.SIGN_POST)
                || aMaterial.equals(Material.WALL_SIGN));
    }
    
    public boolean existsPlayer(String aName) {
        OfflinePlayer[] offPlayers = getServer().getOfflinePlayers();
        for (OfflinePlayer lOffPlayer : offPlayers) {
            if (lOffPlayer.getName().equals(aName)) return true;
        }
        return false;
    }
    
}
