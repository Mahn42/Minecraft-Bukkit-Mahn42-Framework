/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 *
 * @author andre
 */
public class WorldCircleWalk implements Iterable<BlockPosition> {
    
    public ArrayList<BlockPosition> positions = new ArrayList<BlockPosition>();
    public BlockPosition center;
    public int radius;
    public int startPos = 0;
    
    public WorldCircleWalk(BlockPosition aCenter, int aRadius, int aStartPos) {
        center = aCenter;
        radius = aRadius;
        startPos = aStartPos;
        bresenham_kreis(center, radius);
    }
    
    public WorldCircleWalk(BlockPosition aCenter, int aRadius) {
        center = aCenter;
        radius = aRadius;
        bresenham_kreis(center, radius);
    }

    private void bresenham_kreis (                // zeichnet mit Bresenham-Algorithmus
            BlockPosition aPos,                   // einen Kreis um den Punkt p
            int r)                                // mit Radius r
    {
        //Logger.getLogger("WorldCircleWalk").info("center " + center + " r " + radius + " sp " + startPos);
        positions.clear();
        ArrayList<BlockPosition> lOkt1 = new ArrayList<BlockPosition>();
        ArrayList<BlockPosition> lOkt2 = new ArrayList<BlockPosition>();
        ArrayList<BlockPosition> lOkt3 = new ArrayList<BlockPosition>();
        ArrayList<BlockPosition> lOkt4 = new ArrayList<BlockPosition>();
        ArrayList<BlockPosition> lOkt5 = new ArrayList<BlockPosition>();
        ArrayList<BlockPosition> lOkt6 = new ArrayList<BlockPosition>();
        ArrayList<BlockPosition> lOkt7 = new ArrayList<BlockPosition>();
        ArrayList<BlockPosition> lOkt8 = new ArrayList<BlockPosition>();
        
        int x,y,d,dx,dxy;
        x=0; y=r; d=1-r;
        dx=3; dxy=-2*r+5;
        while (y>=x)
        {
            BlockPosition lPos;
            lPos = aPos.clone(); lPos.add( x, 0, y); lOkt1.add(lPos);
            lPos = aPos.clone(); lPos.add( y, 0, x); lOkt2.add(0, lPos);
            lPos = aPos.clone(); lPos.add( y, 0,-x); lOkt3.add(lPos);
            lPos = aPos.clone(); lPos.add( x, 0,-y); lOkt4.add(0, lPos);
            lPos = aPos.clone(); lPos.add(-x, 0,-y); lOkt5.add(lPos);
            lPos = aPos.clone(); lPos.add(-y, 0,-x); lOkt6.add(0, lPos);
            lPos = aPos.clone(); lPos.add(-y, 0, x); lOkt7.add(lPos);
            lPos = aPos.clone(); lPos.add(-x, 0, y); lOkt8.add(0, lPos);
            if (d<0) {
                d=d+dx;  dx=dx+2; dxy=dxy+2; x++;
            } else {
                d=d+dxy; dx=dx+2; dxy=dxy+4; x++; y--;
            }
        }
        //if (lOkt1.size()>1) lOkt1.remove(lOkt1.size()-1);
        if (lOkt2.size()>1) lOkt2.remove(lOkt2.size()-1);
        //if (lOkt3.size()>1) lOkt3.remove(lOkt3.size()-1);
        if (lOkt4.size()>1) lOkt4.remove(lOkt4.size()-1);
        //if (lOkt5.size()>1) lOkt5.remove(lOkt5.size()-1);
        if (lOkt6.size()>1) lOkt6.remove(lOkt6.size()-1);
        //if (lOkt7.size()>1) lOkt7.remove(lOkt7.size()-1);
        if (lOkt8.size()>1) lOkt8.remove(lOkt8.size()-1);
        for(BlockPosition lPos : lOkt1) { positions.add(lPos); }
        for(BlockPosition lPos : lOkt2) { positions.add(lPos); }
        for(BlockPosition lPos : lOkt3) { positions.add(lPos); }
        for(BlockPosition lPos : lOkt4) { positions.add(lPos); }
        for(BlockPosition lPos : lOkt5) { positions.add(lPos); }
        for(BlockPosition lPos : lOkt6) { positions.add(lPos); }
        for(BlockPosition lPos : lOkt7) { positions.add(lPos); }
        for(BlockPosition lPos : lOkt8) { positions.add(lPos); }
    }

    protected class WorldCircleWalkIter implements Iterator<BlockPosition> {
        protected WorldCircleWalk fCircle;
        protected int fPos = 0;
        
        public WorldCircleWalkIter(WorldCircleWalk aCircle, int aPos) {
            fCircle = aCircle;
            fPos = aPos;
        }

        @Override
        public boolean hasNext() {
            return fPos < fCircle.positions.size();
        }

        @Override
        public BlockPosition next() {
            BlockPosition lPos = fCircle.positions.get(fPos);
            fPos++;
            return lPos;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
    
    @Override
    public Iterator<BlockPosition> iterator() {
        return new WorldCircleWalkIter(this, startPos);
    }
    
    
}
