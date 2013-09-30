/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework.npc.network;

import net.minecraft.server.v1_6_R3.EntityPlayer;
import net.minecraft.server.v1_6_R3.INetworkManager;
import net.minecraft.server.v1_6_R3.MinecraftServer;
import net.minecraft.server.v1_6_R3.Packet;
import net.minecraft.server.v1_6_R3.Packet102WindowClick;
import net.minecraft.server.v1_6_R3.Packet106Transaction;
import net.minecraft.server.v1_6_R3.Packet10Flying;
import net.minecraft.server.v1_6_R3.Packet130UpdateSign;
import net.minecraft.server.v1_6_R3.Packet14BlockDig;
import net.minecraft.server.v1_6_R3.Packet15Place;
import net.minecraft.server.v1_6_R3.Packet16BlockItemSwitch;
import net.minecraft.server.v1_6_R3.Packet255KickDisconnect;
import net.minecraft.server.v1_6_R3.Packet28EntityVelocity;
import net.minecraft.server.v1_6_R3.Packet3Chat;
import net.minecraft.server.v1_6_R3.Packet51MapChunk;
import net.minecraft.server.v1_6_R3.PlayerConnection;

/**
 *
 * @author andre
 */
public class EmptyConnection extends PlayerConnection {
    public EmptyConnection(MinecraftServer minecraftserver, INetworkManager inetworkmanager, EntityPlayer entityplayer) {
        super(minecraftserver, inetworkmanager, entityplayer);
    }

    @Override
    public void a(Packet102WindowClick packet) {
    }

    @Override
    public void a(Packet106Transaction packet) {
    }

    @Override
    public void a(Packet10Flying packet) {
    }

    @Override
    public void a(Packet130UpdateSign packet) {
    }

    @Override
    public void a(Packet14BlockDig packet) {
    }

    @Override
    public void a(Packet15Place packet) {
    }

    @Override
    public void a(Packet16BlockItemSwitch packet) {
    }

    @Override
    public void a(Packet255KickDisconnect packet) {
    }

    @Override
    public void a(Packet28EntityVelocity packet) {
    }

    @Override
    public void a(Packet3Chat packet) {
    }

    @Override
    public void a(Packet51MapChunk packet) {
    }

    @Override
    public void a(String string, Object[] objects) {
    }

    @Override
    public void sendPacket(Packet packet) {
    }    
    
    /*
    public void d() {
        d();
    }

    public void disconnect(String s) {
        disconnect(s);
    }

    public void a(Packet10Flying packet10flying) {
    }

    public void a(double d0, double d1, double d2, float f, float f1) {
    }

    public void teleport(Location dest) {
        teleport(dest);
    }

    public void a(Packet14BlockDig packet14blockdig) {
    }

    public void a(Packet15Place packet15place) {
    }

    public void a(String s, Object[] aobject) {
    }

    public void onUnhandledPacket(Packet packet) {
        onUnhandledPacket(packet);
    }

    public void sendPacket(Packet packet) {
    }

    public void a(Packet16BlockItemSwitch packet16blockitemswitch) {
    }

    public void a(Packet3Chat packet3chat) {
    }

    public void chat(String s, boolean async) {
        chat(s, async);
    }

    private void handleCommand(String s) {
        handleCommand(s);
    }

    public void a(Packet18ArmAnimation packet18armanimation) {
    }

    public void a(Packet19EntityAction packet19entityaction) {
    }

    public void a(Packet255KickDisconnect packet255kickdisconnect) {
    }

    public int lowPriorityCount() {
        return super.lowPriorityCount();
    }

    public void a(Packet7UseEntity packet7useentity) {
    }

    public void a(Packet205ClientCommand packet205clientcommand) {
    }

    public boolean b() {
        return b();
    }

    public void a(Packet9Respawn packet9respawn) {
    }

    public void handleContainerClose(Packet101CloseWindow packet101closewindow) {
        handleContainerClose(packet101closewindow);
    }

    public void a(Packet102WindowClick packet102windowclick) {
    }

    public void a(Packet108ButtonClick packet108buttonclick) {
    }

    public void a(Packet107SetCreativeSlot packet107setcreativeslot) {
    }

    public void a(Packet106Transaction packet106transaction) {
    }

    public void a(Packet130UpdateSign packet130updatesign) {
    }

    public void a(Packet0KeepAlive packet0keepalive) {
    }

    public boolean a() {
        return a();
    }

    public void a(Packet202Abilities packet202abilities) {
    }

    public void a(Packet203TabComplete packet203tabcomplete) {
    }

    public void a(Packet204LocaleAndViewDistance packet204localeandviewdistance) {
    }

    public void a(Packet250CustomPayload packet250custompayload) {
    }
    */
}
