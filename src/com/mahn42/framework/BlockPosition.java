/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

/**
 *
 * @author andre
 */
public class BlockPosition {
    
    public int x;
    public int y;
    public int z;
    
    public BlockPosition() {
    }
    
    public BlockPosition(int aX, int aY, int aZ) {
        x = aX;
        y = aY;
        z = aZ;
    }
    
    public BlockPosition(Location aLocation) {
        x = aLocation.getBlockX();
        y = aLocation.getBlockY();
        z = aLocation.getBlockZ();
    }
    
    public BlockPosition(BlockPosition aPos) {
        x = aPos.x;
        y = aPos.y;
        z = aPos.z;
    }
    
    public Location getLocation(World aWorld) {
        return new Location(aWorld, x, y, z);
    }
    
    public int getBlockTypeId(World aWorld) {
        return aWorld.getBlockTypeIdAt(x, y, z);
    }

    public BlockPosition getWHD(BlockPosition aPos) {
        BlockPosition lWHD = new BlockPosition();
        lWHD.x = Math.abs(x - aPos.x) + 1;
        lWHD.y = Math.abs(y - aPos.y) + 1;
        lWHD.z = Math.abs(z - aPos.z) + 1;
        return lWHD;
    }
    
    public BlockPosition getMinPos(BlockPosition aPos) {
        return new BlockPosition(Math.min(x, aPos.x), Math.min(y, aPos.y), Math.min(z, aPos.z));
    }
    
    public BlockPosition getMaxPos(BlockPosition aPos) {
        return new BlockPosition(Math.max(x, aPos.x), Math.max(y, aPos.y), Math.max(z, aPos.z));
    }
    
    @Override
    public boolean equals(Object aObject) {
        if (aObject instanceof BlockPosition) {
            BlockPosition lPos = (BlockPosition) aObject;
            return (lPos.x == x) && (lPos.y == y) && (lPos.z == z);
        } else if (aObject instanceof Location) {
            Location lLoc = (Location) aObject;
            return (lLoc.getBlockX() == x) && (lLoc.getBlockY() == y) && (lLoc.getBlockZ() == z);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.x;
        hash = 29 * hash + this.y;
        hash = 29 * hash + this.z;
        return hash;
    }
    
    @Override
    public String toString() {
        return "(" + new Integer(x).toString() + "," + new Integer(y).toString() + "," + new Integer(z).toString() + ")";
    }
    
    public String toCSV() {
        return new Integer(x).toString() + ";" + new Integer(y).toString() + ";" + new Integer(z).toString();
    }
    
    public String toCSV(String aSep) {
        return new Integer(x).toString() + aSep + new Integer(y).toString() + aSep + new Integer(z).toString();
    }

    public void toCSV(StringBuilder aBuilder, String aSep) {
        aBuilder.append(Integer.toString(x));
        aBuilder.append(aSep);
        aBuilder.append(Integer.toString(y));
        aBuilder.append(aSep);
        aBuilder.append(Integer.toString(z));
    }

    public void fromCSV(String aValue, String aSep) {
        String[] lVals = aValue.split(aSep);
        x = Integer.parseInt(lVals[0]);
        y = Integer.parseInt(lVals[1]);
        z = Integer.parseInt(lVals[2]);
    }

    public boolean isAt(int aX, int aY, int aZ) {
        return (x == aX) && (y == aY) && (z == aZ);
    }
    
    public Vector getVector() {
        return new Vector(x,y,z);
    }

    public Block getBlock(World aWorld) {
        return aWorld.getBlockAt(x, y, z);
    }

    public Block getBlockAt(World aWorld, int aDx, int aDy, int aDz) {
        return aWorld.getBlockAt(x + aDx, y + aDy, z + aDz);
    }

    public Material getBlockType(World aWorld) {
        return getBlock(aWorld).getType();
    }

    public void add(int aX, int aY, int aZ) {
        x += aX;
        y += aY;
        z += aZ;
    }

    public void add(BlockPosition aPos) {
        x += aPos.x;
        y += aPos.y;
        z += aPos.z;
    }

    public void subtract(BlockPosition aPos) {
        x -= aPos.x;
        y -= aPos.y;
        z -= aPos.z;
    }

    public void add(BlockPositionDelta aDelta) {
        x += aDelta.dx;
        y += aDelta.dy;
        z += aDelta.dz;
    }

    public void add(Vector aVector) {
        x += aVector.getBlockX();
        y += aVector.getBlockY();
        z += aVector.getBlockZ();
    }

    public double distance(BlockPosition aPos) {
        return getVector().distance(aPos.getVector());
    }

    public double distance(Location aLoc) {
        return getVector().add(new Vector(0.5d, 0.5d, 0.5d)).distance(aLoc.toVector());
    }

    public void cloneFrom(BlockPosition aPos) {
        x = aPos.x;
        y = aPos.y;
        z = aPos.z;
    }
    
    @Override
    public BlockPosition clone() {
        return new BlockPosition(this);
    }

    public boolean nearly(BlockPosition aPos) {
        return nearly(aPos,1);
    }
    
    public boolean nearly(BlockPosition aPos, int aDist) {
        return (Math.abs(x - aPos.x) + Math.abs(y - aPos.y) + Math.abs(z - aPos.z)) <= aDist;
    }
    
    public boolean isBetween(BlockPosition aEdge1, BlockPosition aEdge2) {
        BlockPosition lEdge1 = aEdge1.getMinPos(aEdge2);
        BlockPosition lEdge2 = aEdge1.getMaxPos(aEdge2);
        return x >= lEdge1.x && x <= lEdge2.x
                && y >= lEdge1.y && y <= lEdge2.y
                && z >= lEdge1.z && z <= lEdge2.z;
    }

    public void multiply(Vector aVector) {
        x *= aVector.getBlockX();
        y *= aVector.getBlockY();
        z *= aVector.getBlockZ();
    }
}
