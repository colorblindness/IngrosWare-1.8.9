package ingros.ware.client.module.impl.visuals;

import ingros.ware.client.events.Render2DEvent;
import ingros.ware.client.events.RenderNameEvent;
import ingros.ware.client.mixin.accessors.IEntityRenderer;
import ingros.ware.client.module.Module;
import ingros.ware.client.utils.RenderUtil;
import ingros.ware.client.utils.value.impl.BooleanValue;
import net.b0at.api.event.Subscribe;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class ESP extends Module {
    private BooleanValue players = new BooleanValue("Players",true);
    private BooleanValue mobs = new BooleanValue("Mobs",false);
    private BooleanValue animals = new BooleanValue("Animals",false);
    private BooleanValue passives = new BooleanValue("Passives",false);
    private BooleanValue invisibles = new BooleanValue("Invisibles",true,players,"true");

    public ESP() {
        super("ESP", Category.VISUALS, 0xff33ff33);
        addValues(players,mobs,animals,passives,invisibles);
    }

    @Subscribe
    public void onRenderNameTag(RenderNameEvent event) {
        if (event.getEntity() instanceof  EntityLivingBase && isValid((EntityLivingBase) event.getEntity())) event.setCancelled(true);
    }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        getMc().theWorld.loadedEntityList.forEach(entity -> {
            if (entity instanceof EntityLivingBase) {
                final EntityLivingBase ent = (EntityLivingBase) entity;
                if (isValid(ent) && entity.getUniqueID() != getMc().thePlayer.getUniqueID() && RenderUtil.isInViewFrustrum(ent)) {
                    double posX = RenderUtil.interpolate(entity.posX, entity.lastTickPosX, event.getPartialTicks());
                    double posY = RenderUtil.interpolate(entity.posY, entity.lastTickPosY, event.getPartialTicks());
                    double posZ = RenderUtil.interpolate(entity.posZ, entity.lastTickPosZ, event.getPartialTicks());
                    double width = entity.width / 1.5;
                    double height = entity.height + (entity.isSneaking() ? -0.3 : 0.2);
                    final AxisAlignedBB aabb = new AxisAlignedBB(posX - width, posY, posZ - width, posX + width, posY + height + 0.05, posZ + width);
                    final List<Vector3d> vectors = Arrays.asList(new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.minX, aabb.maxY, aabb.minZ), new Vector3d(aabb.maxX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vector3d(aabb.minX, aabb.minY, aabb.maxZ), new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ));
                    ((IEntityRenderer) getMc().entityRenderer).setupCameraTransform(event.getPartialTicks(), 0);
                    Vector4d position = null;
                    for (Vector3d vector : vectors) {
                        vector = RenderUtil.project(vector.x - getMc().getRenderManager().viewerPosX, vector.y - getMc().getRenderManager().viewerPosY, vector.z - getMc().getRenderManager().viewerPosZ);
                        if (vector != null && vector.z >= 0.0 && vector.z < 1.0) {
                            if (position == null) {
                                position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                            }
                            position.x = Math.min(vector.x, position.x);
                            position.y = Math.min(vector.y, position.y);
                            position.z = Math.max(vector.x, position.z);
                            position.w = Math.max(vector.y, position.w);
                        }
                    }
                    getMc().entityRenderer.setupOverlayRendering();
                    if (position != null) {
                        GL11.glPushMatrix();
                        final float x = (float) position.x;
                        final float y = (float) position.y;
                        final float w = (float) ((float) position.z - position.x);
                        final float h = (float) ((float) position.w - position.y);
                        RenderUtil.drawBorderedRect(x + 0.5f, y + 0.5f, w - 1, h - 1, 0.5f, 0x00000000, 0xff000000);
                        RenderUtil.drawBorderedRect(x, y, w, h, 0.5f, 0x00000000, 0xffffffff);
                        RenderUtil.drawBorderedRect(x - 0.5f, y - 0.5f, w + 1, h + 1, 0.5f, 0x00000000, 0xff000000);
                        RenderUtil.drawBorderedRect(x - 2.5f, y + h, 1.5f, -((h / ent.getMaxHealth()) * ent.getHealth()), 0.5f, getHealthColor(ent), 0xff000000);
                        GL11.glScalef(0.5f, 0.5f, 0.5f);
                        if (ent.getHealth() <ent.getMaxHealth())
                            getMc().fontRendererObj.drawStringWithShadow((int)ent.getHealth()+"hp", (x - 3f - getMc().fontRendererObj.getStringWidth((int)ent.getHealth()+"hp") / 2)* 2, (y + h -((h / ent.getMaxHealth()) * ent.getHealth()))* 2, -1);
                        getMc().fontRendererObj.drawStringWithShadow(ent.getName(), (x + (w / 2) - (getMc().fontRendererObj.getStringWidth(ent.getName()) / 4)) * 2, (y * 2) - 1 - getMc().fontRendererObj.FONT_HEIGHT, -1);
                        GL11.glScalef(1.0f, 1.0f, 1.0f);
                        GL11.glPopMatrix();
                    }
                }
            }
        });
    }

    private boolean isValid(EntityLivingBase entity) {
        return getMc().thePlayer != entity && entity.getEntityId() != -1488 && isValidType(entity) && entity.isEntityAlive() && (!entity.isInvisible() || invisibles.isEnabled());
    }

    private boolean isValidType(EntityLivingBase entity) {
        return (players.isEnabled() && entity instanceof EntityPlayer) || ((mobs.isEnabled() && (entity instanceof EntityMob || entity instanceof EntitySlime)) || (passives.isEnabled() && (entity instanceof EntityVillager || entity instanceof EntityGolem)) || (animals.isEnabled() && entity instanceof IAnimals));
    }

    private int getHealthColor(EntityLivingBase player) {
        return Color.HSBtoRGB(Math.max(0.0F, Math.min(player.getHealth(), player.getMaxHealth()) / player.getMaxHealth()) / 3.0F, 1.0F, 0.8f) | 0xFF000000;
    }
}