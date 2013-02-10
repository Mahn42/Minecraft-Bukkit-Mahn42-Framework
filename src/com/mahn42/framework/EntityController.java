/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

/**
 *
 * @author andre
 */
public class EntityController implements Listener, Runnable  {
    
    protected final HashMap<Integer, EntityControl> controlledEntities = new HashMap<Integer, EntityControl>();
    
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
        Framework.plugin.getProfiler().beginProfile("FW.EntityController.run");
        Collection<EntityControl> lControls = new ArrayList<EntityControl>();
        synchronized(controlledEntities) {
            lControls.addAll(controlledEntities.values());
        }
        ArrayList<EntityControl> lRemoves = new ArrayList<EntityControl>();
        for(EntityControl lControl : lControls) {
            lControl.run();
            if (lControl.remove) {
                lRemoves.add(lControl);
            }
        }
        synchronized(controlledEntities) {
            for(EntityControl lControl : lRemoves) {
                Framework.plugin.log("fw", "entity removed from EC: " + lControl.entity.getEntityId());
                controlledEntities.remove(lControl.id);
            }
        }
        Framework.plugin.getProfiler().endProfile("FW.EntityController.run");
    }
    
    public void add(EntityControl aControl) {
        synchronized(controlledEntities) {
            controlledEntities.put(aControl.id, aControl);
        }
    }

    public void remove(int aEntityId) {
        synchronized(controlledEntities) {
            controlledEntities.remove(aEntityId);
        }
    }
}
