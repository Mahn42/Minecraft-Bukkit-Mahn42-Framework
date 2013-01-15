/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.framework;

import com.mahn42.framework.npc.entity.EntityPlayerNPC;
import net.minecraft.server.v1_4_6.EntityCreature;
import net.minecraft.server.v1_4_6.EntityPlayer;
import net.minecraft.server.v1_4_6.Navigation;
import net.minecraft.server.v1_4_6.PathEntity;
import net.minecraft.server.v1_4_6.PathPoint;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_4_6.CraftWorld;
import org.bukkit.craftbukkit.v1_4_6.entity.CraftEntity;
import org.bukkit.entity.Entity;

/**
 *
 * @author andre
 */
public class EntityControl {

    public int id;
    public Entity entity;
    public boolean active = true;
    public boolean remove = false;
    public EntityControlPath path = new EntityControlPath();
    protected EntityControlPathItem pathItem;

    public EntityControl(Entity aEntity) {
        entity = aEntity;
        id = entity.getEntityId();
    }
    protected int ljumpcount = 0;

    public void run() {
        if (entity.isDead()) {
            remove = true;
            return;
        }
        if (pathItem == null) {
            getNextPathItem();
        }
        if (pathItem != null) {
            BlockPosition lDest = pathItem.getDestination(entity);
            BlockPosition lEntityPos = new BlockPosition(entity.getLocation());
            while (pathItem != null && lDest != null && lDest.distance(entity.getLocation()) < 1.5d) {
                EntityReachedPathItemEvent lEvent = new EntityReachedPathItemEvent(entity, this, pathItem);
                lEvent.raise();
                if (pathItem.shouldStay(entity)) {
                    break;
                }
                getNextPathItem();
                if (pathItem != null) {
                    lDest = pathItem.getDestination(entity);
                }
            }
            if (pathItem != null && lDest != null) {
                net.minecraft.server.v1_4_6.Entity lMCEntitiy = ((CraftEntity) entity).getHandle();
                if (lMCEntitiy instanceof EntityCreature) {
                    EntityCreature lCreature = (EntityCreature) lMCEntitiy;
                    PathEntity lPE = ((CraftWorld) entity.getWorld()).getHandle().a(lCreature, lDest.x, lDest.y, lDest.z, 100.0f, true, true, true, true);
                    lCreature.setPathEntity(lPE);
                    Navigation lNavigation = lCreature.getNavigation();
                    lNavigation.a(lPE, pathItem.getSpeed(entity));
                } else if (lMCEntitiy instanceof EntityPlayerNPC) {
                    //EntityPlayerNPC lNPC = (EntityPlayerNPC) lMCEntitiy;
                    /*
                     PathEntity lPE = ((CraftWorld)entity.getWorld()).getHandle().a(lNPC, lDest.x, lDest.y, lDest.z, 100.0f, true, false, true, true);
                     for(int i = 0; i<lPE.d(); i++) {
                     PathPoint lpp = lPE.a(i);
                     Framework.plugin.getLogger().info("PP " + i + ":" + lpp);
                     }
                     */
                    /*
                     Vector lV = lDest.getVector();
                     Location lLoc = lNPC.getBukkitEntity().getLocation();
                     Vector lF = new Vector(lLoc.getX(), lLoc.getY(), lLoc.getZ());
                     lV = lV.subtract(lF);
                     lV.normalize();
                     float lSpeed = pathItem.getSpeed(entity);
                     lNPC.motX = lV.getX() * lSpeed;// * 0.9f;
                     lNPC.motY = lV.getY() * 2.0f * lSpeed; // * 0.4f;
                     lNPC.motZ = lV.getZ() * lSpeed;// * 0.4f;
                     if (lNPC.motX > 1.2f) {
                     lNPC.motX = 1.2f;
                     }
                     if (lNPC.motY > 1.2f) {
                     lNPC.motY = 1.2f;
                     }
                     if (lNPC.motZ > 1.2f) {
                     lNPC.motZ = 1.2f;
                     }
                     //Random lRnd = new Random();
                     /*
                     ljumpcount++;
                     if (ljumpcount > 10) {
                     ljumpcount = 0;
                     lNPC.motY = 0.6f;
                     } else {
                     lNPC.motY = 0.0f;
                     }
                     */

                    EntityPlayer lPlayer = (EntityPlayer) lMCEntitiy;
                    PathEntity lPE = ((CraftWorld) entity.getWorld()).getHandle().a(lPlayer, lDest.x, lDest.y, lDest.z, pathentityarg, pathentityf0, pathentityf1, pathentityf2, pathentityf3);
                    //lCreature.setPathEntity(lPE);
                    Navigation lNavigation = lPlayer.getNavigation();
                    lNavigation.a(lPE, pathItem.getSpeed(entity));
                    //lNavigation.a(lDest.x, lDest.y, lDest.z, pathItem.getSpeed(entity));

                } else {
                    //todo
                }
            } else {
                net.minecraft.server.v1_4_6.Entity lMCEntitiy = ((CraftEntity) entity).getHandle();
                if (lMCEntitiy instanceof EntityPlayerNPC) {
                    EntityPlayerNPC lNPC = (EntityPlayerNPC) lMCEntitiy;
                    lNPC.motX = lNPC.motY = lNPC.motZ = 0;
                }
            }
        }
    }

    protected void getNextPathItem() {
        if (!path.isEmpty()) {
            pathItem = path.get(0);
            path.remove(0);
        } else {
            pathItem = null;
            remove = true;
        }
    }
    protected final static float pathentityarg = 300.0f;
    protected final static boolean pathentityf0 = true;
    protected final static boolean pathentityf1 = true;
    protected final static boolean pathentityf2 = true; //false; // with water path -> false
    protected final static boolean pathentityf3 = true;

    public void createPath(BlockPosition aDest) {
        net.minecraft.server.v1_4_6.Entity lMCEntitiy = ((CraftEntity) entity).getHandle();
        if (lMCEntitiy instanceof EntityPlayerNPC) {
            EntityPlayerNPC lNPC = (EntityPlayerNPC) lMCEntitiy;
            PathEntity lPE = ((CraftWorld) entity.getWorld()).getHandle().a(lNPC, aDest.x, aDest.y, aDest.z, pathentityarg, pathentityf0, pathentityf1, pathentityf2, pathentityf3);
            for (int i = 0; i < lPE.d(); i++) {
                PathPoint lpp = lPE.a(i);
                EntityControlPathItem lItem = new EntityControlPathItemDestination(new BlockPosition(lpp.a, lpp.b, lpp.c));
                path.add(lItem);
            }
        }
    }

    public void showPath(BlockPosition aDest) {
        showPath(aDest, pathentityf0, pathentityf1, pathentityf2, pathentityf3);
    }

    public void showPath(BlockPosition aDest, boolean f0, boolean f1, boolean f2, boolean f3) {
        net.minecraft.server.v1_4_6.Entity lMCEntitiy = ((CraftEntity) entity).getHandle();
        if (lMCEntitiy instanceof EntityPlayer) {
            EntityPlayer lNPC = (EntityPlayer) lMCEntitiy;
            PathEntity lPE = ((CraftWorld) entity.getWorld()).getHandle().a(lNPC, aDest.x, aDest.y, aDest.z, pathentityarg, f0, f1, f2, f3);
            if (lPE != null) {
                for (int i = 0; i < lPE.d(); i++) {
                    PathPoint lpp = lPE.a(i);
                    Block blockAt = entity.getWorld().getBlockAt(lpp.a, lpp.b, lpp.c);
                    if (blockAt.getType().equals(Material.AIR)
                            || blockAt.getType().equals(Material.LONG_GRASS)
                            || blockAt.getType().equals(Material.RED_ROSE)
                            || blockAt.getType().equals(Material.YELLOW_FLOWER)
                            || blockAt.getType().equals(Material.DEAD_BUSH)) {
                        blockAt.setTypeId(Material.REDSTONE_WIRE.getId(), true);
                    }
                    //EntityControlPathItem lItem = new EntityControlPathItemDestination(new BlockPosition(lpp.a, lpp.b, lpp.c));
                    //path.add(lItem);
                }
            }
        }
    }

    public static boolean existsPath(Entity aEntity, BlockPosition aDest) {
        return existsPath(aEntity, aDest, pathentityf0, pathentityf1, pathentityf2, pathentityf3);
    }

    public static boolean existsPath(Entity aEntity, BlockPosition aDest, boolean f0, boolean f1, boolean f2, boolean f3) {
        net.minecraft.server.v1_4_6.Entity lMCEntitiy = ((CraftEntity) aEntity).getHandle();
        PathEntity lPE = ((CraftWorld) aEntity.getWorld()).getHandle().a(lMCEntitiy, aDest.x, aDest.y, aDest.z, pathentityarg, f0, f1, f2, f3);
        if (lPE != null && lPE.d() > 0) {
            PathPoint pp = lPE.a(lPE.d() - 1);
            boolean lR = pp.a == aDest.x && pp.b == aDest.y && pp.c == aDest.z;
            if (!lR) {
                Framework.plugin.log("fw", "EP: " + aDest + " != " + pp.a + "," + pp.b + "," + pp.c);
            }
            return lR;
        } else {
            return false;
        }
    }
}
