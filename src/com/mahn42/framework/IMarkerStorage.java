/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.List;
import org.bukkit.World;

/**
 *
 * @author andre
 */
public interface IMarkerStorage {
    public List<IMarker> findMarkers(World aWorld, String aName);
    public List<IMarker> findMarkers(World aWorld, BlockRect aArea);
}
