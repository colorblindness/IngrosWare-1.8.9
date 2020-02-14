package ingros.ware.client.module.impl.combat;

import ingros.ware.client.events.UpdateEvent;
import ingros.ware.client.events.PacketEvent;
import ingros.ware.client.mixin.accessors.IS12PacketEntityVelocity;
import ingros.ware.client.mixin.accessors.IS27PacketExplosion;
import ingros.ware.client.module.Module;
import ingros.ware.client.utils.value.impl.EnumValue;
import ingros.ware.client.utils.value.impl.NumberValue;
import net.b0at.api.event.Subscribe;
import net.b0at.api.event.types.EventType;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public class NoVelocity extends Module {
    private NumberValue<Integer> horizontalModifier = new NumberValue<>("Horizontal", 0, 0, 100, 1);
    private NumberValue<Integer> verticalModifier = new NumberValue<>("Vertical", 0, 0, 100, 1);
    private EnumValue<Mode> mode = new EnumValue<>("Mode", Mode.NORMAL);

    public NoVelocity() {
        super("NoVelocity", Category.COMBAT, 0xff505050);
        addValues(horizontalModifier, verticalModifier, mode);

    }
    private enum Mode {
        NORMAL, AAC, DEV
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        final int vertical = verticalModifier.getValue();
        final int horizontal = horizontalModifier.getValue();
        if (getMc().thePlayer == null || getMc().theWorld == null) return;
        switch (mode.getValue()) {
            case NORMAL:
                if (event.getType() == EventType.POST) {
                    if (event.getPacket() instanceof S12PacketEntityVelocity) {
                        final IS12PacketEntityVelocity ipacket = (IS12PacketEntityVelocity) event.getPacket();
                        final S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.getPacket();
                        if (packet.getEntityID() != getMc().thePlayer.getEntityId()) return;
                        if (vertical != 0 || horizontal != 0) {
                            ipacket.setMotionX(horizontal * packet.getMotionX() / 100);
                            ipacket.setMotionY(vertical * packet.getMotionY() / 100);
                            ipacket.setMotionZ(horizontal * packet.getMotionZ() / 100);
                        } else event.setCancelled(true);
                    }
                    if (event.getPacket() instanceof S27PacketExplosion) {
                        final IS27PacketExplosion ipacket = (IS27PacketExplosion) event.getPacket();
                        final S27PacketExplosion packet = (S27PacketExplosion) event.getPacket();
                        if (vertical != 0 || horizontal != 0) {
                            ipacket.setPosX(horizontal * packet.getX() / 100);
                            ipacket.setPosY(vertical * packet.getY() / 100);
                            ipacket.setPosZ(horizontal * packet.getZ() / 100);
                        } else event.setCancelled(true);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (getMc().thePlayer == null) return;
        switch (mode.getValue()) {
            case AAC:
                if (event.getEventType() == EventType.PRE) {
                    if (getMc().thePlayer.hurtResistantTime > 13 && getMc().thePlayer.hurtResistantTime < 20 && !getMc().thePlayer.onGround) {
                        if (getMc().thePlayer.hurtResistantTime == 19) {
                            getMc().thePlayer.motionX *= 0.85;
                            getMc().thePlayer.motionZ *= 0.85;
                        }
                        else {
                            getMc().thePlayer.onGround = true;
                            double tick = Math.max(0, 16 - getMc().thePlayer.hurtResistantTime);
                        }
                    }
                }
                break;
            case DEV:
                if (getMc().thePlayer.hurtResistantTime == 15) {
                    getMc().thePlayer.motionY *= 0.999;
                    getMc().thePlayer.onGround = true;
                }
                break;
            default:break;
        }
    }
}
