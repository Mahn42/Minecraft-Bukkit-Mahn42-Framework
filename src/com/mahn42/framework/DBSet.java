/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andre
 */
public class DBSet<T extends DBRecord> implements Iterable<T>, DBSave {
    protected File file;
    protected ArrayList<T> fRecords = new ArrayList<T>();
    protected HashMap<String, T> fKeyIndex = new HashMap();
    protected Class<T> fRecordClass;
    
    public DBSet(Class<T> aRecordClass) {
        fRecordClass = aRecordClass;
    }
    
    public DBSet(Class<T> aRecordClass, File aStore) {
        fRecordClass = aRecordClass;
        file = aStore;
    }
    
    public void clear() {
        fRecords.clear();
        fKeyIndex.clear();
    }
    
    public void load() {
        clear();
        if (file.exists()) {
            try {
                BufferedReader lReader = new BufferedReader(new FileReader(file));
                String lLine;
                int lLineNum = 1;
                String lHeader = lReader.readLine();
                while ((lLine = lReader.readLine()) != null) {
                    T lRecord = null;
                    try {
                        lRecord = fRecordClass.newInstance();
                    } catch (InstantiationException ex) {
                        Logger.getLogger(DBSet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(DBSet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (lRecord != null) {
                        boolean lOK = false;
                        try {
                            lRecord.fromCSV(lHeader, lLine);
                            lOK = true;
                        } catch (Exception ex) {
                            Logger.getLogger(DBSet.class.getName()).log(Level.SEVERE, "line " + lLineNum, ex);
                        }
                        lLineNum++;
                        if (lOK) {
                            addRecordInternal(lRecord);
                        }
                    }
                }
            } catch (IOException ex) {
                getLogger().log(Level.SEVERE, null, ex);
            }            
        }
    }
    
    @Override
    public void save() {
        if (file.exists()) {
            file.delete();
        }
        try {
            BufferedWriter lWriter = new BufferedWriter(new FileWriter(file));
            lWriter.write("CSV:1.0");
            lWriter.newLine();
            for (T lRecord : fRecords) {
                lWriter.write(lRecord.toCSV());
                lWriter.newLine();
            }
            lWriter.close();
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, null, ex);
        }
    }
    
    public T getRecord(int aIndex) {
        return (T)fRecords.get(aIndex);
    }
    
    public T getRecord(String aKey) {
        return (T)fKeyIndex.get(aKey);
    }
    
    public void addRecord(T aRecord) {
        addRecordInternal(aRecord);
    }
    
    protected void addRecordInternal(T aRecord) {
        if (aRecord != null) {
            fRecords.add(aRecord);
            fKeyIndex.put(aRecord.key, aRecord);
            aRecord.added(this);
        }
    }
    
    protected void removedRecord(T aRecord) {
    }
    
    public void remove(T aRecord) {
        if (!fRecords.remove(aRecord))
            getLogger().info("remove: not found");
        if (fKeyIndex.remove(aRecord.key) == null)
            getLogger().info("remove: index not found");
        removedRecord(aRecord);
    }
    
    public void remove(int aIndex) {
        remove(fRecords.get(aIndex));
    }

    public void remove(String aKey) {
        remove(fKeyIndex.get(aKey));
    }

    public int size() {
        return fRecords.size();
    }
    
    protected Logger getLogger() {
        return Logger.getLogger(getClass().getSimpleName());
    }
    
    @Override
    public Iterator<T> iterator() {
        return fRecords.iterator();
    }
}
