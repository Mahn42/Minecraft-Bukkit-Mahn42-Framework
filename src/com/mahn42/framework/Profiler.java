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
        public long ocount = 0;
        public long sum = 0;
        public long memory_start = 0;
        public long memory_used = 0;

        public long start() {
            fStart = System.nanoTime(); // currentTimeMillis();
            Runtime lRuntime = Runtime.getRuntime();
            memory_start = lRuntime.totalMemory() - lRuntime.freeMemory();
            return fStart;
        }

        public void end() {
            end(fStart);
        }

        public void end(long aStart) {
            long lEnd = System.nanoTime(); // currentTimeMillis();
            long d = lEnd - fStart;
            if (d < min) {
                min = d;
            }
            if (d > max) {
                max = d;
            }
            sum += d;
            if (d > 50000000) {
                ocount++;
            }
            Runtime lRuntime = Runtime.getRuntime();
            memory_used = (lRuntime.totalMemory() - lRuntime.freeMemory()) - memory_start;
            count++;
        }
        
        @Override
        public String toString() {
            float lmin = min; lmin /= 1000000.0;
            float lavg = sum; lavg /= count; lavg /= 1000000.0;
            float lmax = max; lmax /= 1000000.0;
            float lsum = sum; lsum /= 1000000000.0;
            return String.format("%5d %5d %8.2f %8.2f %10.2f %10.2f %6d %10d", count, ocount, lmin, lavg, lmax, lsum, memory_used, memory_start);
            //return "" + min + " " + ((long)sum/count) + " " + max + " " + sum + " " + count;
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
            aLogger.info("count  over   min      avg        max        sum     : name");
            for(String lName : keySet) {
                aLogger.info(fItems.get(lName).toString() + "  : " + lName);
            }
        }
    }
}
