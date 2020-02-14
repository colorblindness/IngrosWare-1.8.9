package ingros.ware.client.module;

import com.google.gson.JsonObject;
import ingros.ware.client.Client;
import ingros.ware.client.utils.font.Fonts;
import ingros.ware.client.utils.value.Value;
import ingros.ware.client.utils.value.impl.BooleanValue;
import ingros.ware.client.utils.value.impl.EnumValue;
import ingros.ware.client.utils.value.impl.NumberValue;
import ingros.ware.client.utils.value.impl.RangedValue;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Module {
    private String label, renderLabel, suffix, description;
    private boolean enabled, hidden;
    private int color, keybind;
    private Category category;
    private Minecraft mc = Minecraft.getMinecraft();
    private List<Value> values = new ArrayList<>();

    public Module(String label, Category category, int color) {
        this.label = label;
        this.category = category;
        this.color = color;
    }

    protected void addValues(Value... value) {
        values.addAll(Arrays.asList(value));
    }

    public Value findValue(String term) {
        for (Value value : values) {
            if (value.getLabel().equalsIgnoreCase(term)) {
                return value;
            }
        }
        return null;
    }

    public List<Value> getValues() {
        return this.values;
    }

    public float getLongestValueInModule() {
        float width = Fonts.font.getStringWidth(getValues().get(0).getLabel() + ": " + getValues().get(0).getValue());
        for (Value value : getValues()) {
            if (value instanceof NumberValue) {
                if (Fonts.font.getStringWidth(value.getLabel() + ": " + ((NumberValue) value).getMaximum()) > width) {
                    width = Fonts.font.getStringWidth(value.getLabel() + ": " + ((NumberValue) value).getMaximum());
                } else if (Fonts.font.getStringWidth(value.getLabel() + ": " + ((NumberValue) value).getMinimum()) > width) {
                    width = Fonts.font.getStringWidth(value.getLabel() + ": " + ((NumberValue) value).getMinimum());
                }
            }
            if (value instanceof RangedValue) {
                if (Fonts.font.getStringWidth(value.getLabel() + ": " + ((RangedValue) value).getMaximum() + " - " + ((RangedValue) value).getMaximum()) > width) {
                    width = Fonts.font.getStringWidth(value.getLabel() + ": " + ((RangedValue) value).getMaximum() + " - " + ((RangedValue) value).getMaximum());
                } else if (Fonts.font.getStringWidth(value.getLabel() + ": " + ((RangedValue) value).getMaximum() + " - " + ((RangedValue) value).getMaximum()) > width) {
                    width = Fonts.font.getStringWidth(value.getLabel() + ": " + ((RangedValue) value).getMaximum() + " - " + ((RangedValue) value).getMaximum());
                }
            } else if (value instanceof EnumValue) {
                for (Enum enoom : ((EnumValue) value).getConstants()) {
                    if (Fonts.font.getStringWidth(value.getLabel() + ": " + enoom.name()) > width) {
                        width = Fonts.font.getStringWidth(value.getLabel() + ": " + enoom.name());
                    }
                }
            } else if (value instanceof BooleanValue) {
                if (Fonts.font.getStringWidth(value.getLabel() + ": " + value.getValue()) > width) {
                    width = Fonts.font.getStringWidth(value.getLabel() + ": " + value.getValue());
                }
            }
        }
        return width;
    }

    public void onEnable() {

    }

    public void onDisable() {

    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setRenderLabel(String renderLabel) {
        this.renderLabel = renderLabel;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setKeybind(int keybind) {
        this.keybind = keybind;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            Client.INSTANCE.getBus().registerListener(this);
            onEnable();
        } else {
            Client.INSTANCE.getBus().deregisterListener(this);
            onDisable();
        }
    }

    public void toggle() {
        setEnabled(!isEnabled());
    }

    public String getLabel() {
        return label;
    }

    public String getRenderLabel() {
        return renderLabel;
    }

    public String getDescription() {
        return description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isHidden() {
        return hidden;
    }

    public int getColor() {
        return color;
    }

    public int getKeybind() {
        return keybind;
    }

    public Category getCategory() {
        return category;
    }

    public Minecraft getMc() {
        return mc;
    }

    public void save(JsonObject directory, boolean key) {
        if (key) directory.addProperty("key", getKeybind());
        directory.addProperty("enabled", isEnabled());
        directory.addProperty("hidden", isHidden());
        values.forEach(val -> {
            if (val instanceof RangedValue)
                directory.addProperty(val.getLabel(), ((RangedValue) val).getLeftVal() + "-" + ((RangedValue) val).getRightVal());
            else directory.addProperty(val.getLabel(), val.getValue().toString());
        });
    }

    public void load(JsonObject directory) {
        directory.entrySet().forEach(data -> {
            switch (data.getKey()) {
                case "name":
                    return;
                case "key":
                    setKeybind(data.getValue().getAsInt());
                    return;
                case "enabled":
                    if (!(isEnabled() && data.getValue().getAsBoolean()) && !(!isEnabled() && !data.getValue().getAsBoolean()))
                        setEnabled(data.getValue().getAsBoolean());
                    return;
                case "hidden":
                    setHidden(data.getValue().getAsBoolean());
                    return;
            }
            final Value val = findValue(data.getKey());
            if (val != null) {
                if (val instanceof RangedValue) {
                    String[] strings = data.getValue().getAsString().split("-");
                    if (((RangedValue) val).getInc() instanceof Float) {
                        ((RangedValue) val).setLeftVal(Float.parseFloat(strings[0]));
                        ((RangedValue) val).setRightVal(Float.parseFloat(strings[1]));
                    }
                    if (((RangedValue) val).getInc() instanceof Integer) {
                        ((RangedValue) val).setLeftVal(Integer.parseInt(strings[0]));
                        ((RangedValue) val).setRightVal(Integer.parseInt(strings[1]));
                    }
                    if (((RangedValue) val).getInc() instanceof Double) {
                        ((RangedValue) val).setLeftVal(Double.parseDouble(strings[0]));
                        ((RangedValue) val).setRightVal(Double.parseDouble(strings[1]));
                    }
                    if (((RangedValue) val).getInc() instanceof Byte) {
                        ((RangedValue) val).setLeftVal(Byte.parseByte(strings[0]));
                        ((RangedValue) val).setRightVal(Byte.parseByte(strings[1]));
                    }
                    if (((RangedValue) val).getInc() instanceof Short) {
                        ((RangedValue) val).setLeftVal(Short.parseShort(strings[0]));
                        ((RangedValue) val).setRightVal(Short.parseShort(strings[1]));
                    }
                    if (((RangedValue) val).getInc() instanceof Long) {
                        ((RangedValue) val).setLeftVal(Long.parseLong(strings[0]));
                        ((RangedValue) val).setRightVal(Long.parseLong(strings[1]));
                    }
                } else val.setValue(data.getValue().getAsString());
            }
        });
    }

    public enum Category {
        COMBAT, EXPLOITS, GHOST, MOVEMENT, OTHER, PLAYER, VISUALS
    }
}
