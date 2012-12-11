/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework.npc.network;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.Socket;
import java.security.PrivateKey;
import net.minecraft.server.v1_4_5.NetHandler;
import net.minecraft.server.v1_4_5.NetworkManager;
import net.minecraft.server.v1_4_5.Packet;

/**
 *
 * @author andre
 */
public class EmptyNetworkManager extends NetworkManager {

    public EmptyNetworkManager(Socket socket, String string, NetHandler netHandler, PrivateKey key)
            throws IOException {
        super(socket, string, netHandler, key);
        //NMS.stopNetworkThreads(this);
        Field f = null;
        try {
            f = NetworkManager.class.getDeclaredField("m");
            f.setAccessible(true);
            f.set(this, false);
        } catch (Exception e) {
        }
    }

    @Override
    public void a() {
    }

    @Override
    public void a(NetHandler netHandler) {
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
