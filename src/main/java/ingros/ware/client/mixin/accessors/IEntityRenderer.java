package ingros.ware.client.mixin.accessors;

public interface IEntityRenderer {

    void cameraOrientation(float partialTicks);

    void setupCameraTransform(float partialTicks, int pass);
}