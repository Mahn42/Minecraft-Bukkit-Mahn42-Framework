/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework.npc.network;

import net.minecraft.server.v1_4_6.EntityPlayer;
import net.minecraft.server.v1_4_6.INetworkManager;
import net.minecraft.server.v1_4_6.MinecraftServer;
import net.minecraft.server.v1_4_6.PlayerConnection;

/**
 *
 * @author andre
 */
public class EmptyConnection extends PlayerConnection {
    public EmptyConnection(MinecraftServer minecraftserver, INetworkManager inetworkmanager, EntityPlayer entityplayer) {
        super(minecraftserver, inetworkmanager, entityplayer);
    }
}
