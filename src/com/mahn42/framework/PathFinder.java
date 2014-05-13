/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;
import net.minecraft.server.v1_7_R3.PathEntity;
import net.minecraft.server.v1_7_R3.PathPoint;

/**
 *
 * @author andre
 */
public class PathFinder {
    public BlockPosition position;
    
    public PathEntity getPathEntity() {
        PathEntity lRes;
        PathPoint[] lPoints = new PathPoint[10];
        lRes = new PathEntity(lPoints);
        return lRes;
    }
}
