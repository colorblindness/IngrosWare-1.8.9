package ingros.ware.client.mixin.launch;

import net.minecraftforge.fml.common.asm.transformers.AccessTransformer;

import java.io.IOException;

public class IngrosAccessTransformer extends AccessTransformer {

    public IngrosAccessTransformer() throws IOException {
        super("ingros_at.cfg");
    }

}
