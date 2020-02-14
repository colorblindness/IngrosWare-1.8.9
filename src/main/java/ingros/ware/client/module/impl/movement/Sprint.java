package ingros.ware.client.module.impl.movement;

import ingros.ware.client.events.UpdateEvent;
import ingros.ware.client.module.Module;
import net.b0at.api.event.Subscribe;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", Category.MOVEMENT, 0xff00ff00);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (getMc().thePlayer == null) return;
        getMc().thePlayer.setSprinting(canSprint());
    }

    private boolean canSprint() {
        return getMc().thePlayer.getFoodStats().getFoodLevel() > 7 && getMc().gameSettings.keyBindForward.isKeyDown();
    }
}
