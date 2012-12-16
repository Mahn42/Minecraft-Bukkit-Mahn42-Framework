/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework.npc.entity;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_4_5.CraftServer;
import org.bukkit.craftbukkit.v1_4_5.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author andre
 */
public class NPCEntity extends CraftPlayer {

        protected Object fDataObject =null;
        
        public Object getDataObject() {
            return fDataObject;
        }
        
        public void setDataObject(Object aObject) {
            fDataObject = aObject;
        }
        
        public Player getAsPlayer() {
            return this;
        }
        
        public NPCEntity(EntityHumanNPC entity) {
            super((CraftServer) Bukkit.getServer(), entity);
        }

        @Override
        public List<MetadataValue> getMetadata(String metadataKey) {
            return server.getEntityMetadata().getMetadata(this, metadataKey);
        }

        @Override
        public boolean hasMetadata(String metadataKey) {
            return server.getEntityMetadata().hasMetadata(this, metadataKey);
        }

        @Override
        public void removeMetadata(String metadataKey, Plugin owningPlugin) {
            server.getEntityMetadata().removeMetadata(this, metadataKey, owningPlugin);
        }

        @Override
        public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
            server.getEntityMetadata().setMetadata(this, metadataKey, newMetadataValue);
        }
    
}
