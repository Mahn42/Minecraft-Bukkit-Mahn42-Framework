/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

/**
 *
 * @author andre
 */
public class DBRecord {

    protected class DBRecordCSVArray {
        ArrayList<String> fCols = new ArrayList<String>();
        
        public DBRecordCSVArray(String aLine) {
            String[] lArray = aLine.split(";");
            fCols.addAll(Arrays.asList(lArray));
        }
        
        public String pop() {
            String lCol;
            if (fCols.size() > 0) {
                lCol = fCols.get(0);
                fCols.remove(0);
            } else {
                lCol = "";
            }
            return lCol;
        }

        public int popInt() {
            String lCol = pop();
            if (!lCol.equals("")) {
                return Integer.parseInt(lCol);
            }
                return 0;
        }
        
        public void popKeys(ArrayList<String> aArray) {
            DBRecord.stringToKeys(pop(), aArray);
        }
    }
    
    public String key;
    
    public DBRecord() {
        init();
    }
    
    protected void init() {
        key = UUID.randomUUID().toString();
    }
    
    public String toCSV() {
        StringBuilder lBuilder = new StringBuilder();
        toCSV(lBuilder);
        return lBuilder.toString();
    }
    
    public void toCSV(StringBuilder aBuilder) {
        ArrayList lCols = new ArrayList();
        boolean lFirst = true;
        toCSVInternal(lCols);
        for(Object lObject : lCols) {
            String lCol;
            if (lObject != null) {
                lCol = (String) lObject.toString();
            } else {
                lCol = "";
            }
            if (lFirst) {
                lFirst = false;
            } else {
                aBuilder.append(";");
            }
            aBuilder.append(lCol);
        }
    }

    protected void toCSVInternal(ArrayList aCols) {
        aCols.add(key);
    }
    
    public void fromCSV(String aHeader, String aLine) {
        DBRecordCSVArray lCols = new DBRecordCSVArray(aLine);
        fromCSVInternal(lCols);
    }
    
    protected void fromCSVInternal(DBRecordCSVArray aCols) {
        key = (String) aCols.pop();
    }

    public boolean isSameRecord(DBRecord aRecord) {
        return (aRecord.key == null ? key == null : aRecord.key.equals(key));
    }
    
    @Override
    public boolean equals(Object aObject) {
        if (aObject instanceof DBRecord) {
            DBRecord lItem = (DBRecord) aObject;
            return lItem.key.equals(key);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (this.key != null ? this.key.hashCode() : 0);
        return hash;
    }
    
    @Override
    public String toString() {
        return getClass().getName() + ":" + key;
    }
    
    public static String arrayToString(Iterable aArray) {
        String lResult = null;
        for (Object lObject : aArray) {
            if (lResult == null) {
                lResult = lObject.toString();
            } else {
                lResult = lResult + ',' + lObject.toString();
            }
        }
        return lResult;
    }

    public static String arrayToKeys(Iterable aArray) {
        String lResult = null;
        for (Object lObject : aArray) {
            if (lObject != null) {
                String lStr = lObject.toString();
                if (!lStr.equals("") && !lStr.isEmpty()) {
                    if (lResult == null) {
                        lResult = lStr;
                    } else {
                        lResult = lResult + ',' + lStr;
                    }
                }
            }
        }
        return lResult;
    }

    public static void stringToArray(String aString, ArrayList aArray) {
        String lStrings[] = aString.split(",");
        aArray.addAll(Arrays.asList(lStrings));
    }

    public static void stringToKeys(String aString, ArrayList<String> aArray) {
        String lStrings[] = aString.split(",");
        for(String lString : lStrings) {
            if (lString != null && !lString.isEmpty() && !lString.equals("")) {
                aArray.add(lString);
            }
        }
    }
    
    protected void added(DBSet aSet) {
        
    }
    
    public void cloneFrom(DBRecord aRecord) {
        key = aRecord.key;
    }
}
