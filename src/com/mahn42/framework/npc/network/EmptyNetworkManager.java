/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework.npc.network;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.Socket;
import java.security.PrivateKey;
import net.minecraft.server.v1_5_R2.Connection;
import net.minecraft.server.v1_5_R2.IConsoleLogManager;
import net.minecraft.server.v1_5_R2.NetworkManager;
import net.minecraft.server.v1_5_R2.Packet;

/**
 *
 * @author andre
 */
public class EmptyNetworkManager extends NetworkManager {

    public EmptyNetworkManager(IConsoleLogManager logManager, Socket socket, String string, Connection connection, PrivateKey key)
            throws IOException {
        super(logManager, socket, string, connection, key);
        //NMS.stopNetworkThreads(this);
        try {
            Field f = null;
            f = NetworkManager.class.getDeclaredField("n");
            f.setAccessible(true);
            f.set(this, false);
        } catch (Exception e) {
        }
    }

    @Override
    public void a() {
    }

    @Override
    public void a(Connection connection) {
    }

    @Override
    public void a(String s, Object... objects) {
    }

    @Override
    public void b() {
    }

    @Override
    public void d() {
    }

    @Override
    public int e() {
        return 0;
    }

    @Override
    public void queue(Packet packet) {
    }
    
}
