/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
 
/**
 *
 * @author andre
 */
public class BuildingEvent extends Event {

    public enum BuildingAction {
        PlayerEnter,
        PlayerLeave,
        Create,
        Destroy,
    }
    
    private static final HandlerList handlers = new HandlerList();
    private Building building;
    private BuildingAction action;
    
    public BuildingEvent(Building aBuilding, BuildingAction aAction) {
        building = aBuilding;
        action = aAction;
    }
 
    public Building getBuilding() {
        return building;
    }
 
    public BuildingAction getAction() {
        return action;
    }
 
    public HandlerList getHandlers() {
        return handlers;
    }
 
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
}
