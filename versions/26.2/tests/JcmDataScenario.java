package com.jsblock.compatibility;

import com.google.gson.JsonParser;
import com.jsblock.data.*;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Kept Java intentionally: also exercises external Java/Mixin-facing descriptors. */
public final class JcmDataScenario {
    public static String run() {
        List<String> values = new ArrayList<>();
        int[] sizes = {0, 1, 3, -1, -99, 100, Integer.MAX_VALUE, Integer.MIN_VALUE};
        long hash = 1;
        for (ScreenAlignment alignment : ScreenAlignment.values()) {
            require(ScreenAlignment.valueOf(alignment.name()) == alignment, "Enum name drift");
            for (int total : sizes) for (int part : sizes) {
                hash = 31 * hash + ScreenAlignment.getX(alignment, total, part);
                hash = 31 * hash + ScreenAlignment.getY(alignment, total, part);
            }
        }
        values.add("alignment=" + hash);
        values.add(failure(() -> ScreenAlignment.getX(null, 10, 2)));
        values.add(failure(() -> ScreenAlignment.getY(null, 10, 2)));
        for (float factor : new float[]{1, 2, -2, 0, Float.NaN, Float.POSITIVE_INFINITY}) {
            ScreenRoot root = new ScreenRoot(null, factor, factor);
            root.init(101.5F, -99.5F);
            values.add(root.width + "/" + root.height + "/" + root.startX);
            require(root.screenAlignment == null, "Nullable root alignment changed");
        }
        require(new OverriddenRoot().width == 77, "Class/method no longer overridable");
        InlineComponentEntry entry = new InlineComponentEntry(null, null, new net.minecraft.client.gui.components.AbstractWidget[]{null, null});
        entry.setAvailableWidth(105);
        require(entry.calculateWidth() == 42 && entry.widgetList.size() == 2 && entry.widgetList.get(0) == null, "Widget layout changed");
        entry.widgetList.clear();
        values.add(failure(entry::calculateWidth));
        entry.widgetList = null;
        values.add(failure(entry::calculateWidth));
        ConfigGuiEntry config = new ConfigGuiEntry(null, null, 15, 30);
        require(config.text == null && config.widget == null && config.y == 0 && config.widgetWidth == 15 && config.widgetHeight == 30, "Config constructor changed");
        TextLabel label = new TextLabel(null, -0F, null, Integer.MIN_VALUE);
        require(label.text == null && label.horizontalAlignment == null && label.y == Integer.MIN_VALUE && Float.floatToRawIntBits(label.scale) == Float.floatToRawIntBits(-0F), "Text label changed");
        PIDSPreset defaults = parse("{\"background\":\"jsblock:test\"}");
        require(defaults.showWeather && defaults.showClock && !defaults.customTextPushArrival && defaults.visibility == null && defaults.color == null && defaults.font == null && defaults.getCarColor(1) == null, "Preset defaults changed");
        PIDSPreset full = parse("{\"background\":\"jsblock:test\",\"showWeather\":false,\"showClock\":false,\"customTextPushArrival\":true,\"fonts\":\"中文\",\"color\":\"FFFFFF\",\"hideRow\":[true,false,true,false,true],\"carLengthColor\":[\"0\",null,\"abcdef\"]}");
        require(!full.showWeather && !full.showClock && full.customTextPushArrival && full.color == 0xFFFFFF && full.font.equals("中文"), "Preset explicit fields changed");
        require(Arrays.equals(full.visibility, new boolean[]{true, false, true, false}), "Visibility truncation changed");
        require(full.getCarColor(0) == null && full.getCarColor(1) == 0 && full.getCarColor(2) == null && full.getCarColor(3) == 0xabcdef && full.getCarColor(4) == null, "One-based sparse car colors changed");
        require(Arrays.equals(parse("{\"background\":\"jsblock:test\",\"hideRow\":[true]}").visibility, new boolean[]{true, false, false, false}), "Visibility padding changed");
        boolean[] visibility = {true};
        Int2IntArrayMap colors = new Int2IntArrayMap();
        PIDSPreset alias = new PIDSPreset(null, false, false, false, visibility, null, null, colors);
        colors.put(0, 123);
        visibility[0] = false;
        require(alias.image == null && alias.visibility == visibility && !alias.visibility[0] && alias.getCarColor(1) == 123, "Constructor aliasing changed");
        for (String json : new String[]{"null", "[]", "{}", "{\"background\":\"jsblock:test\",\"color\":\"bad color\"}", "{\"background\":\"jsblock:test\",\"hideRow\":[null]}", "{\"background\":\"UPPER:case\"}"}) values.add(failure(() -> parse(json)));
        values.add(failure(() -> PIDSPreset.fromJson(null)));
        return String.join(";", values);
    }
    private static PIDSPreset parse(String json) { return PIDSPreset.fromJson(JsonParser.parseString(json)); }
    private static String failure(Runnable action) { try { action.run(); return "NO_ERROR"; } catch (Throwable failure) { return failure.getClass().getName(); } }
    private static void require(boolean value, String message) { if (!value) throw new AssertionError(message); }
    private static final class OverriddenRoot extends ScreenRoot {
        OverriddenRoot() { super(null, 1, 1); init(0, 0); }
        @Override public void init(float width, float height) { this.width = 77; }
    }
}
