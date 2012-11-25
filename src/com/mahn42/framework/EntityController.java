/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.Collection;
import java.util.HashMap;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

/**
 *
 * @author andre
 */
public class EntityController implements Listener, Runnable  {
    
    protected HashMap<Integer, EntityControl> controlledEntities = new HashMap<Integer, EntityControl>();
    
    @EventHandler
    public void entityTargetEvent(EntityTargetEvent aEvent) {
        EntityControl lControl;
        synchronized(controlledEntities) {
            lControl = controlledEntities.get(aEvent.getEntity().getEntityId());
        }
        if (lControl != null && lControl.active) {
            aEvent.setCancelled(true);
        }
    }

    @Override
    public void run() {
        Collection<EntityControl> lControls;
        synchronized(controlledEntities) {
            lControls = controlledEntities.values();
        }
        for(EntityControl lControl : lControls) {
            lControl.run();
        }
    }
    
    public void add(EntityControl aControl) {
        synchronized(controlledEntities) {
            controlledEntities.put(aControl.id, aControl);
        }
    }
}
