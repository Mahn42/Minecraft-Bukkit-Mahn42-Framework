/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

/**
 *
 * @author andre
 */
public class EntityController implements Listener, Runnable {

    protected final HashMap<Integer, EntityControl> controlledEntities = new HashMap<Integer, EntityControl>();
    protected ArrayList<EntityControl> currentControlledEntities = null;

    @EventHandler
    public void entityTargetEvent(EntityTargetEvent aEvent) {
        EntityControl lControl;
        synchronized (controlledEntities) {
            lControl = controlledEntities.get(aEvent.getEntity().getEntityId());
        }
        if (lControl != null && lControl.active) {
            aEvent.setCancelled(true);
        }
    }

    @Override
    public void run() {
        Framework.plugin.getProfiler().beginProfile("FW.EntityController.run");
        if (currentControlledEntities == null || currentControlledEntities.isEmpty()) {
            if (currentControlledEntities == null) {
                currentControlledEntities = new ArrayList<EntityControl>();
            }
            synchronized (controlledEntities) {
                currentControlledEntities.addAll(controlledEntities.values());
            }
        }
        if (!currentControlledEntities.isEmpty()) {
            int lCount = 0;
            ArrayList<EntityControl> lRemoves = new ArrayList<EntityControl>();
            while (!currentControlledEntities.isEmpty() && lCount < Framework.plugin.configEntityControllerCallsPerRun) {
                lCount++;
                EntityControl lControl = currentControlledEntities.get(0);
                currentControlledEntities.remove(0);
                lControl.run();
                if (lControl.remove) {
                    lRemoves.add(lControl);
                }
            }
            synchronized (controlledEntities) {
                for (EntityControl lControl : lRemoves) {
                    EntityControl remove = controlledEntities.remove(lControl.id);
                    Framework.plugin.log("fw", "entity removed from EC: " + remove);
                }
            }
        }
        Framework.plugin.getProfiler().endProfile("FW.EntityController.run");
    }

    public void add(EntityControl aControl) {
        synchronized (controlledEntities) {
            controlledEntities.put(aControl.id, aControl);
        }
    }

    public void remove(int aEntityId) {
        synchronized (controlledEntities) {
            controlledEntities.remove(aEntityId);
        }
    }
}
