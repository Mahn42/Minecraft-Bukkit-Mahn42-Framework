/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;

/**
 *
 * @author andre
 */
public class Profiler {

    public static class ProfileItem {

        protected long fStart;
        public long min = Long.MAX_VALUE;
        public long max = 0;
        public long count = 0;
        public long sum = 0;

        public long start() {
            fStart = System.currentTimeMillis();
            return fStart;
        }

        public void end() {
            end(fStart);
        }

        public void end(long aStart) {
            long lEnd = System.currentTimeMillis();
            long d = lEnd - fStart;
            if (d < min) {
                min = d;
            }
            if (d > max) {
                max = d;
            }
            sum += d;
            count++;
        }
        
        @Override
        public String toString() {
            return "" + min + " " + ((long)sum/count) + " " + max + " " + sum + " " + count;
        }
    }
    protected HashMap<String, ProfileItem> fItems = new HashMap<String, Profiler.ProfileItem>();

    public long beginProfile(String aName) {
        long lstart;
        synchronized (fItems) {
            ProfileItem item = fItems.get(aName);
            if (item == null) {
                item = new ProfileItem();
                fItems.put(aName, item);
            }
            lstart = item.start();
        }
        return lstart;
    }

    public void endProfile(String aName, long aEnd) {
        synchronized (fItems) {
            ProfileItem item = fItems.get(aName);
            if (item != null) {
                item.end(aEnd);
            }
        }
    }
    
    public void endProfile(String aName) {
        synchronized (fItems) {
            ProfileItem item = fItems.get(aName);
            if (item != null) {
                item.end();
            }
        }
    }
    
    public void dump(Logger aLogger) {
        synchronized(fItems) {
            Set<String> keySet = fItems.keySet();
            for(String lName : keySet) {
                aLogger.info(lName + ": " + fItems.get(lName));
            }
        }
    }
}
