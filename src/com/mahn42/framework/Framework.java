/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import com.mahn42.framework.commands.CommandWorldCreate;
import com.mahn42.framework.commands.CommandMarkerList;
import com.mahn42.framework.commands.CommandSave;
import com.mahn42.framework.commands.CommandWorldRegenerate;
import com.mahn42.framework.commands.CommandBD_Create;
import com.mahn42.framework.commands.CommandTeleport;
import com.mahn42.framework.commands.CommandWorldPlayerInventory;
import com.mahn42.framework.commands.CommandWorldSet;
import com.mahn42.framework.commands.CommandWorldRemove;
import com.mahn42.framework.commands.CommandDebugSet;
import com.mahn42.framework.commands.CommandWorldClassificationList;
import com.mahn42.framework.commands.CommandBD_CreateFromArea;
import com.mahn42.framework.commands.CommandBD_Detect;
import com.mahn42.framework.commands.CommandSetSpawn;
import com.mahn42.framework.commands.CommandWorldList;
import com.mahn42.framework.commands.CommandBD_Dump;
import com.mahn42.framework.commands.CommandChunkRegenerate;
import com.mahn42.framework.commands.CommandTest;
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
import net.minecraft.server.v1_4_5.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_4_5.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author andre
 */
public class Framework extends JavaPlugin {
    private EntityController fEntityController;

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
    public int configDynMapTicks = 40;
    public int configDBSaverTicks = 18000;
    public String configLanguage = "DE_de";
    public int configProjectionTicks = 10;
    public int configEntityControllerTicks = 10;
    
    protected HashMap<String, Boolean> fDebugSet = new HashMap<String, Boolean>();
    protected HashMap<String, Properties> fPluginLangs = new HashMap<String, Properties>();
    protected SyncBlockSetter fSyncBlockSetter;
    protected DBSaverTask fSaverTask;
    protected DynMapBuildingRenderer fDynMapTask;
    protected ProjectionAreasRunner fProjectionRunner;
    protected BuildingDetector fBuildingDetector;
    protected HashMap<String, PlayerBuildings> fPlayerBuildings;
    protected Messenger fMessenger = null;
    protected PlayerManager fPlayerManager = null;
    protected HashMap<World, RestrictedRegions> fRestrictedRegions = new HashMap<World, RestrictedRegions>();
    protected HashMap<World, ProjectionAreas> fProjectionAreas = new HashMap<World, ProjectionAreas>();
    protected WorldConfigurationDB fWorldConfigurationDB;
    protected WorldPlayerInventoryDB fWorldPlayerInventoryDB;
    protected WorldDBList<WorldPlayerSettingsDB> fWorldPlayerSettingsDB;
    protected HashMap<String, WorldClassification> fWorldClassifications = new HashMap<String, WorldClassification>();
    protected ArrayList<IMarkerStorage> fMarkerStorages = new ArrayList<IMarkerStorage>();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BlockAreaList lList = new BlockAreaList();
        lList.load(new File("/Users/andre/craftbukkit/test.frm"));
        BlockRect lRect = new BlockRect();
        lRect.fromCSV("1,2,3 - 4,5,6", "\\,");
        Logger.getLogger("xxx").info(lRect.toString());
        lRect.fromCSV("1,2,3", "\\,");
        Logger.getLogger("xxx").info(lRect.toString());
    }
    
    public long getSyncCallCount() {
        return fSyncBlockSetter.calls;
    }
    
    public void registerWorldClassification(WorldClassification aClassification) {
        fWorldClassifications.put(aClassification.name, aClassification);
    }
    
    public void unregisterWorldClassification(String aName) {
        fWorldClassifications.remove(aName);
    }
    
    public WorldClassification getWorldClassification(String aName) {
        return fWorldClassifications.get(aName);
    }
    
    public ArrayList<WorldClassification> getWorldClassifications() {
        ArrayList<WorldClassification> lResult = new ArrayList<WorldClassification>();
        for(WorldClassification lWC : fWorldClassifications.values()) {
            lResult.add(lWC);
        }
        return lResult;
    }

    public WorldConfigurationDB getWorldConfigurationDB() {
        if (fWorldConfigurationDB == null) {
            fWorldConfigurationDB = new WorldConfigurationDB();
            fWorldConfigurationDB.load();
            getLogger().info("Datafile " + fWorldConfigurationDB.file.toString() + " loaded. (Records:" + fWorldConfigurationDB.size() + ")");
            registerSaver(fWorldConfigurationDB);
        }
        return fWorldConfigurationDB;
    }
    
    public void registerMarkerStorage(IMarkerStorage aStorage) {
        fMarkerStorages.add(aStorage);
    }

    public void unregisterMarkerStorage(IMarkerStorage aStorage) {
        fMarkerStorages.remove(aStorage);
    }
    
    public List<IMarker> findMarkers(World aWorld, String aName) {
        ArrayList<IMarker> lResult = new ArrayList<IMarker>();
        for(IMarkerStorage lStore : fMarkerStorages) {
            List<IMarker> lMarkers = lStore.findMarkers(aWorld, aName);
            lResult.addAll(lMarkers);
        }
        return lResult;
    }

    public List<IMarker> findMarkers(World aWorld, BlockRect aArea) {
        ArrayList<IMarker> lResult = new ArrayList<IMarker>();
        for(IMarkerStorage lStore : fMarkerStorages) {
            List<IMarker> lMarkers = lStore.findMarkers(aWorld, aArea);
            lResult.addAll(lMarkers);
        }
        return lResult;
    }

    public WorldPlayerInventoryDB getWorldPlayerInventoryDB() {
        if (fWorldPlayerInventoryDB == null) {
            fWorldPlayerInventoryDB = new WorldPlayerInventoryDB();
            fWorldPlayerInventoryDB.load();
            getLogger().info("Datafile " + fWorldPlayerInventoryDB.file.toString() + " loaded. (Records:" + fWorldPlayerInventoryDB.size() + ")");
            registerSaver(fWorldPlayerInventoryDB);
        }
        return fWorldPlayerInventoryDB;
    }

    public RestrictedRegions getRestrictedRegions(World aWorld, boolean aCreate) {
        RestrictedRegions lResult = fRestrictedRegions.get(aWorld);
        if (lResult == null && aCreate) {
            lResult = new RestrictedRegions();
            fRestrictedRegions.put(aWorld, lResult);
        }
        return lResult;
    }
    
    public ProjectionAreas getProjectionAreas(World aWorld, boolean aCreate) {
        ProjectionAreas lResult = fProjectionAreas.get(aWorld);
        if (lResult == null && aCreate) {
            lResult = new ProjectionAreas(aWorld);
            fProjectionAreas.put(aWorld, lResult);
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
        String lName;
        if (aLanguage != null && !aLanguage.isEmpty()) {
            lName = aPlugin.getName()+"."+aLanguage;
        } else {
            lName = aPlugin.getName()+"."+configLanguage;
        }
        Properties lProps;
        lProps = fPluginLangs.get(lName);
        if (lProps == null) {
            lProps = new Properties();
            String lFileName = String.format("%s.properties", aLanguage != null ? aLanguage : configLanguage);
            try {
                InputStream lStream;
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
            fPluginLangs.put(lName, lProps);
        }
        return lProps;
    }
    
    public WorldPlayerSettingsDB getWorldPlayerSettingsDB(String aWorldName) {
        return fWorldPlayerSettingsDB.getDB(aWorldName);
    }
    
    public EntityController getEntityController() {
        return fEntityController;
    }
    
    /**********************************************************
     * 
     *  STARTUP 
     * 
     **********************************************************/
    @Override
    public void onEnable() { 
        plugin = this;
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        fWorldPlayerSettingsDB = new WorldDBList<WorldPlayerSettingsDB>(WorldPlayerSettingsDB.class, "PlayerSets", this);
        fPlayerBuildings = new HashMap<String, PlayerBuildings>();
        fBuildingDetector = new BuildingDetector();
        readFrameworkConfig();
        fSyncBlockSetter = new SyncBlockSetter();
        fSaverTask = new DBSaverTask();
        fDynMapTask = new DynMapBuildingRenderer();
        fProjectionRunner = new ProjectionAreasRunner();
        fEntityController = new EntityController();
        
        getCommand("fw_bd_dump").setExecutor(new CommandBD_Dump());
        getCommand("fw_bd_create").setExecutor(new CommandBD_Create());
        getCommand("fw_bd_createfromarea").setExecutor(new CommandBD_CreateFromArea());
        getCommand("fw_bd_detect").setExecutor(new CommandBD_Detect());
        getCommand("fw_set_spawn").setExecutor(new CommandSetSpawn());
        getCommand("fw_save").setExecutor(new CommandSave());
        getCommand("fw_debug").setExecutor(new CommandDebugSet());
        getCommand("fw_world_create").setExecutor(new CommandWorldCreate());
        getCommand("fw_world_remove").setExecutor(new CommandWorldRemove());
        getCommand("fw_world_regenerate").setExecutor(new CommandWorldRegenerate());
        getCommand("fw_world_list").setExecutor(new CommandWorldList());
        getCommand("fw_world_clist").setExecutor(new CommandWorldClassificationList());
        getCommand("fw_world_set").setExecutor(new CommandWorldSet());
        getCommand("fw_world_pinv").setExecutor(new CommandWorldPlayerInventory());
        getCommand("fw_marker_list").setExecutor(new CommandMarkerList());
        getCommand("fw_tp").setExecutor(new CommandTeleport());
        getCommand("fw_chunk_regenerate").setExecutor(new CommandChunkRegenerate());
        getCommand("fw_test").setExecutor(new CommandTest());

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new BlockListener(), this);
        getServer().getPluginManager().registerEvents(new WorldListener(), this);
        getServer().getPluginManager().registerEvents(fEntityController, this);
        List<World> lWorlds = getServer().getWorlds();
        for(World lWorld :  lWorlds) {
            WorldConfiguration lConf = getWorldConfigurationDB().getByName(lWorld.getName());
            if (lConf == null) {
                lConf = new WorldConfiguration();
                lConf.name = lWorld.getName();
                lConf.gameMode = GameMode.SURVIVAL; // for unknown worlds is SURVIVAL default 
                lConf.updateFromWorld();
                lConf.inventoryName = lConf.name;
                if (lConf.name.equalsIgnoreCase("world_nether")
                        ||lConf.name.equalsIgnoreCase("world_the_end")) {
                    lConf.inventoryName = "world";
                } 
                getWorldConfigurationDB().addRecord(lConf);
            }
        }
        for(WorldConfiguration lConf : getWorldConfigurationDB()) {
            World lWorld = getServer().getWorld(lConf.name);
            if (lWorld == null) {
                getLogger().info("reload world " + lConf.name);
                lWorld = lConf.getCreator().createWorld();
            }
        }
        registerSaver(fWorldPlayerSettingsDB);
        /*
        getServer().getScheduler().scheduleSyncRepeatingTask(this, fSyncBlockSetter, 10, configSyncBlockSetterTicks);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, fSaverTask, 100, configDBSaverTicks);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, fDynMapTask, 100, configDynMapTicks);
        getServer().getScheduler().scheduleAsyncRepeatingTask(this, fProjectionRunner, 10, configProjectionTicks);
        getServer().getScheduler().scheduleAsyncRepeatingTask(this, fEntityController, 10, 10); // TODO config
        */
        //getServer().getScheduler().runTaskTimer(this, new Runnable() {  }, 1, 1);
        getServer().getScheduler().runTaskTimer(this, fSyncBlockSetter, 10, configSyncBlockSetterTicks);
        getServer().getScheduler().runTaskTimer(this, fSaverTask, 100, configDBSaverTicks);
        getServer().getScheduler().runTaskTimer(this, fDynMapTask, 100, configDynMapTicks);
        getServer().getScheduler().runTaskTimerAsynchronously(this, fProjectionRunner, 10, configProjectionTicks);
        getServer().getScheduler().runTaskTimerAsynchronously(this, fEntityController, 10, configEntityControllerTicks);
    }

    @Override
    public void onDisable() {
        fSaverTask.run();
        getServer().getScheduler().cancelTasks(this);
    }

    public World requireWorld(String aName, WorldClassification aClass) {
        World lWorld = getServer().getWorld(aName);
        if (lWorld == null) {
            WorldConfiguration lConf = new WorldConfiguration();
            lConf.name = aName;
            lConf.updateFromClassification(aClass);
            Framework.plugin.getWorldConfigurationDB().addRecord(lConf);
            lWorld = lConf.getCreator().createWorld();
            lWorld.save();
            getLogger().info("new world " + lWorld.getName() + " is created.");
        }
        return lWorld;
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
    
    public void runSave() {
        fSaverTask.run();
    }

    private void readFrameworkConfig() {
        FileConfiguration lConfig = getConfig();
        configSyncBlockSetterTicks = lConfig.getInt("SyncBlockSetter.Ticks", configSyncBlockSetterTicks);
        configLanguage = lConfig.getString("Language");
        configProjectionTicks = lConfig.getInt("ProjectionAreas.Ticks", configProjectionTicks);
        configEntityControllerTicks = lConfig.getInt("EnityController.Ticks", configEntityControllerTicks);
        configDBSaverTicks = lConfig.getInt("DBSaver.Ticks", configDBSaverTicks);
        configDynMapTicks = lConfig.getInt("DynMap.Ticks", configDynMapTicks);
        List lWorldClasses = lConfig.getList("WorldClassifications");
        for(Object lItem : lWorldClasses) {
            WorldClassification lWC = new WorldClassification();
            lWC.fromSectionValue(lItem);
            registerWorldClassification(lWC);
        }
    }
    
    public String getText(String aText, Object... aObjects) {
        return getText(configLanguage, aText, aObjects);
    }
    
    public String getText(CommandSender aPlayer, String aText, Object... aObjects) {
        return getText(getPlayerLanguage(aPlayer.getName()), aText, aObjects);
    }
    
    public String getText(String aLanguage, String aText, Object... aObjects) {
        return getText(this, aLanguage, aText, aObjects);
    }
    
    public String getText(JavaPlugin aPlugin, String aLanguage, String aText, Object... aObjects) {
        Properties lProps = getLanguage(aPlugin, aLanguage);
        String lText = ChatColor.translateAlternateColorCodes('&', String.format(lProps.getProperty(aText, aText), aObjects));
        return lText;
    }
    
    public String getPlayerLanguage(String aPlayerName) {
        //TODO
        return configLanguage;
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
    
    public static EntityType[] animals;
    {
        animals = new EntityType[8];
        animals[0] = EntityType.CHICKEN;
        animals[1] = EntityType.COW;
        animals[2] = EntityType.MUSHROOM_COW;
        animals[3] = EntityType.OCELOT;
        animals[4] = EntityType.PIG;
        animals[5] = EntityType.SHEEP;
        animals[6] = EntityType.WOLF;
        animals[7] = EntityType.BAT;
    }

    public static boolean isAnimal(EntityType aEntityType) {
        for(EntityType lT : animals) {
            if (lT.equals(aEntityType)) {
                return true;
            }
        }
        return false;
    }
    
    public void teleportPlayerToWorld(Player aPlayer, World aWorld, String aMarkname) {
        List<IMarker> lMarkers = findMarkers(aWorld, aMarkname);
        BlockPosition lPos = null;
        if (lMarkers.size() == 1) {
            lPos = lMarkers.get(0).getPosition();
        }
        teleportPlayerToWorld(aPlayer, aWorld, lPos);
    }
    
    public void teleportPlayerToWorld(Player aPlayer, World aWorld) {
        teleportPlayerToWorld(aPlayer, aWorld, (BlockPosition)null);
    }
    
    public void teleportPlayerToWorld(Player aPlayer, World aWorld, BlockPosition aPos) {
        Location lLocation;
        if (aPos != null) {
            lLocation = aPos.getLocation(aWorld);
        } else {
            WorldPlayerSettingsDB lDB = Framework.plugin.getWorldPlayerSettingsDB(aWorld.getName());
            if (lDB != null) {
                WorldPlayerSettings lSet = lDB.getByName(aPlayer.getName());
                if (lSet != null) {
                    lLocation = lSet.position.getLocation(aWorld);
                } else {
                    lLocation = aWorld.getSpawnLocation();
                }
            } else {
                lLocation = aWorld.getSpawnLocation();
            }
        }
        aPlayer.teleport(lLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }
    
    public ItemStack setItemStackColor(ItemStack aItem, int aColor) {
        CraftItemStack craftStack = null;
        net.minecraft.server.v1_4_5.ItemStack itemStack = null;
        if (aItem instanceof CraftItemStack) {
            craftStack = (CraftItemStack) aItem;
            itemStack = craftStack.getHandle();
        }
        else if (aItem instanceof ItemStack) {
            craftStack = new CraftItemStack(aItem);
            itemStack = craftStack.getHandle();
        }
        NBTTagCompound tag = itemStack.tag;
        if (tag == null) {
            tag = new NBTTagCompound();
            tag.setCompound("display", new NBTTagCompound());
            itemStack.tag = tag;
        }
 
        tag = itemStack.tag.getCompound("display");
        tag.setInt("color", aColor);
        itemStack.tag.setCompound("display", tag);
        return craftStack;        
    }

    public static ArrayList<Material> dependsOnOtherBlock;
    {
        dependsOnOtherBlock = new ArrayList<Material>();
        dependsOnOtherBlock.add(Material.PAINTING);
        dependsOnOtherBlock.add(Material.REDSTONE_WIRE);
        dependsOnOtherBlock.add(Material.REDSTONE_TORCH_ON);
        dependsOnOtherBlock.add(Material.REDSTONE_TORCH_OFF);
        dependsOnOtherBlock.add(Material.TORCH);
        dependsOnOtherBlock.add(Material.LEVER);
        dependsOnOtherBlock.add(Material.CACTUS);
        dependsOnOtherBlock.add(Material.DEAD_BUSH);
        dependsOnOtherBlock.add(Material.DETECTOR_RAIL);
        dependsOnOtherBlock.add(Material.LONG_GRASS);
        dependsOnOtherBlock.add(Material.TRIPWIRE);
        dependsOnOtherBlock.add(Material.TRIPWIRE_HOOK);
        dependsOnOtherBlock.add(Material.RAILS);
        dependsOnOtherBlock.add(Material.POWERED_RAIL);
        dependsOnOtherBlock.add(Material.SAPLING);
        dependsOnOtherBlock.add(Material.VINE);
        dependsOnOtherBlock.add(Material.WHEAT);
        dependsOnOtherBlock.add(Material.SEEDS);
        dependsOnOtherBlock.add(Material.SUGAR_CANE_BLOCK);
        dependsOnOtherBlock.add(Material.WEB);
        dependsOnOtherBlock.add(Material.WATER_LILY);
        dependsOnOtherBlock.add(Material.LADDER);
        dependsOnOtherBlock.add(Material.STONE_BUTTON);
        dependsOnOtherBlock.add(Material.YELLOW_FLOWER);
        dependsOnOtherBlock.add(Material.RED_ROSE);
        dependsOnOtherBlock.add(Material.BROWN_MUSHROOM);
        dependsOnOtherBlock.add(Material.RED_MUSHROOM);
    }
}
