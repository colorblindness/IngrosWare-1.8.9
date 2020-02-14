package ingros.ware.client.mixin.accessors;

public interface IC03PacketPlayer {

    void setOnGround(boolean onGround);

    void setY(double y);

    double getY();

    void setX(double x);

    double getX();

    void setZ(double z);

    double getZ();

    void setYaw(float yaw);

    float getYaw();

    void setPitch(float pitch);

    float getPitch();

}