package com.jsblock;

import com.jsblock.screen.IDrawingJoban;
import com.jsblock.screen.JobanPIDSConfigScreen;
import com.jsblock.vermappings.render.RenderHelper;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.client.Config;
import mtr.data.IGui;
import mtr.mappings.EntityModelMapper;
import mtr.mappings.RenderBufferSource;
import mtr.mappings.RenderSnapshot;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.feature.TextFeatureRenderer;
import net.minecraft.client.renderer.state.gui.ColoredRectangleRenderState;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3x2fStack;
import org.joml.Matrix4f;
import sun.misc.Unsafe;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/** Exercises actual 26.2 GUI nodes, JCM text capture and baked APG geometry without a window. */
public final class JcmClientCompatibilityCheck {
    private static final Unsafe UNSAFE;
    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            UNSAFE = (Unsafe) field.get(null);
        } catch (ReflectiveOperationException exception) {
            throw new ExceptionInInitializerError(exception);
        }
    }

    public static void main(String[] args) throws Exception {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        Minecraft minecraft = allocate(Minecraft.class);
        set(Minecraft.class, null, "instance", minecraft);
        set(Minecraft.class, minecraft, "gameDirectory", new File("."));
        Font font = allocate(Font.class);
        set(Font.class, font, "splitter", new StringSplitter((codePoint, style) -> 6));
        set(Minecraft.class, minecraft, "font", font);
        checkPidsWidgetRegistration();
        checkCheckboxDraw();
        checkGuiHighlight();
        checkPidsText(font);
        checkApgModels();
        System.out.println("PASS: JCM 26.2 checkbox single-label/toggle and unique PIDS widgets, real GUI rectangle/opacity, PIDS Unicode/custom-font text submit nodes, APG baked model vertices and matrix preservation");
    }

    private static void checkCheckboxDraw() throws Exception {
        Class<?> checkboxType = JobanPIDSConfigScreen.class.getDeclaredField("selectAllCheckbox").getType();
        int[] clicks = {0};
        mtr.screen.WidgetBetterCheckbox checkbox = (mtr.screen.WidgetBetterCheckbox) checkboxType.getConstructor(
            int.class, int.class, int.class, int.class, Component.class, mtr.screen.WidgetBetterCheckbox.OnClick.class
        ).newInstance(20, 20, 144, 20, Component.literal("到站資料"),
            (mtr.screen.WidgetBetterCheckbox.OnClick) selected -> clicks[0]++);
        CountingGraphics graphics = allocate(CountingGraphics.class);
        graphics.labels = new ArrayList<>();
        checkbox.extractRenderState(graphics, -100, -100, 0);
        require(graphics.sprites == 1 && graphics.labels.equals(List.of("[ ] 到站資料")),
            "Checkbox must draw one background and one label, not default plus custom labels: " + graphics.labels);
        checkbox.onPress();
        graphics.sprites = 0;
        graphics.labels.clear();
        checkbox.extractRenderState(graphics, -100, -100, 0);
        require(clicks[0] == 1 && checkbox.selected(), "Checkbox toggle/callback was changed");
        require(graphics.sprites == 1 && graphics.labels.equals(List.of("[x] 到站資料")),
            "Selected checkbox must keep a single labelled check state");
    }

    private static void checkPidsWidgetRegistration() throws Exception {
        JobanPIDSConfigScreen screen = new JobanPIDSConfigScreen(BlockPos.ZERO, BlockPos.ZERO, 4, "None");
        screen.width = 550;
        screen.height = 350;
        Method init = JobanPIDSConfigScreen.class.getDeclaredMethod("init");
        init.setAccessible(true);
        init.invoke(screen);
        Set<Object> unique = Collections.newSetFromMap(new IdentityHashMap<>());
        unique.addAll(screen.children());
        require(screen.children().size() == 11 && unique.size() == 11,
            "PIDS must register 8 row widgets, 2 shared controls and 1 preset exactly once: " + screen.children().size());
    }

    private static void checkGuiHighlight() throws Exception {
        GuiRenderState state = new GuiRenderState();
        GuiGraphicsExtractor graphics = allocate(GuiGraphicsExtractor.class);
        set(GuiGraphicsExtractor.class, graphics, "guiRenderState", state);
        set(GuiGraphicsExtractor.class, graphics, "pose", new Matrix3x2fStack(16));
        Constructor<?> scissorConstructor = Class.forName("net.minecraft.client.gui.GuiGraphicsExtractor$ScissorStack")
            .getDeclaredConstructor(ScreenRectangle.class);
        scissorConstructor.setAccessible(true);
        set(GuiGraphicsExtractor.class, graphics, "scissorStack", scissorConstructor.newInstance(new ScreenRectangle(0, 0, 800, 600)));
        RenderHelper.fill(graphics, 100, 40, 240, 20, 0x4DFFFFFF);
        List<GuiElementRenderState> elements = new ArrayList<>();
        state.forEachElement(elements::add, GuiRenderState.TraverseRange.ALL);
        require(elements.size() == 1 && elements.getFirst() instanceof ColoredRectangleRenderState,
            "Highlight must queue one actual rectangle");
        ColoredRectangleRenderState rectangle = (ColoredRectangleRenderState) elements.getFirst();
        // GuiGraphicsExtractor.fill orders both axes descending before queuing the rectangle.
        require(rectangle.x0() == 340 && rectangle.y0() == 60 && rectangle.x1() == 100 && rectangle.y1() == 40,
            "Highlight extents must survive the vanilla fill winding normalization");
        require(rectangle.bounds().left() == 100 && rectangle.bounds().top() == 40 &&
            rectangle.bounds().width() == 240 && rectangle.bounds().height() == 20,
            "Highlight must cover the full requested 240 x 20 row at (100, 40)");
        require(rectangle.col1() == 0x4DFFFFFF && rectangle.col2() == 0x4DFFFFFF, "Highlight alpha was lost");
        require(RenderHelper.legacyTextColor(0) == 0xFF000000, "Black RGB text became transparent");
        require(RenderHelper.legacyTextColor(0x00EEBB33) == 0xFFEEBB33, "PIDS RGB alpha fallback changed");
        require(RenderHelper.legacyTextColor(0x80112233) == 0x80112233, "Explicit text alpha must survive");
    }

    private static void checkPidsText(Font font) throws Exception {
        // Enable the existing MTR option without invoking its disk-writing setter.
        set(Config.class, null, "useMTRFont", true);
        RenderSnapshot snapshot;
        PoseStack matrices = new PoseStack();
        matrices.translate(1, 2, 3);
        Matrix4f before = new Matrix4f(matrices.last().pose());
        try (RenderBufferSource buffers = RenderBufferSource.begin(Vec3.ZERO)) {
            IDrawingJoban.drawStringWithFont(matrices, font, buffers.immediate(), "港A🚇|Test",
                IGui.HorizontalAlignment.CENTER, IGui.VerticalAlignment.TOP,
                0, 0, 80, 40, 1, false, 0x00112233, false, 15728880, "jsblock:test_font");
            snapshot = buffers.snapshot();
        }
        require(before.equals(matrices.last().pose()), "PIDS drawing leaked its transform");
        SubmitNodeStorage storage = new SubmitNodeStorage();
        snapshot.submit(new PoseStack(), storage, new CameraRenderState());
        List<TextFeatureRenderer.Submit> texts = new ArrayList<>();
        storage.drainPhases(phase -> phase.sortInto((node, blending) -> {
            require(node instanceof TextFeatureRenderer.Submit, "PIDS text used the wrong submission type");
            texts.add((TextFeatureRenderer.Submit) node);
        }));
        require(texts.size() == 2, "Bilingual PIDS lines disappeared or duplicated");
        List<String> recovered = new ArrayList<>();
        for (TextFeatureRenderer.Submit text : texts) {
            require(text.color() == 0xFF112233 && text.lightCoords() == 15728880, "PIDS text opacity/light changed");
            StringBuilder builder = new StringBuilder();
            text.string().accept((index, style, codePoint) -> {
                require(style.getFont().equals(new FontDescription.Resource(Identifier.parse("jsblock:test_font"))),
                    "PIDS custom font was dropped");
                builder.appendCodePoint(codePoint);
                return true;
            });
            recovered.add(builder.toString());
        }
        require(recovered.contains("港A🚇") && recovered.contains("Test"), "PIDS Unicode/bilingual content changed");
    }

    private static void checkApgModels() throws Exception {
        String prefix = "com.jsblock.render.RenderJobanPSDAPG$";
        for (String modelName : List.of("ModelAPGDoorLight", "ModelAPGDoorBottom", "ModelSingleCube")) {
            Class<?> type = Class.forName(prefix + modelName);
            Constructor<?> constructor;
            EntityModelMapper<?> model;
            if (modelName.equals("ModelSingleCube")) {
                constructor = type.getDeclaredConstructor(int.class, int.class, int.class, int.class, int.class, int.class, int.class, int.class);
                constructor.setAccessible(true);
                model = (EntityModelMapper<?>) constructor.newInstance(34, 9, 0, 15, 1, 16, 1, 1);
            } else {
                constructor = type.getDeclaredConstructor();
                constructor.setAccessible(true);
                model = (EntityModelMapper<?>) constructor.newInstance();
            }
            InspectVertices vertices = new InspectVertices();
            PoseStack pose = new PoseStack();
            Matrix4f before = new Matrix4f(pose.last().pose());
            model.renderToBuffer(pose, vertices, 15728880, 0, -1);
            int expected = modelName.equals("ModelSingleCube") ? 24 : modelName.equals("ModelAPGDoorLight") ? 48 : 72;
            require(vertices.count == expected, modelName + " lost baked cube geometry: " + vertices.count);
            require(before.equals(pose.last().pose()), modelName + " leaked its transform");
        }
    }

    private static <T> T allocate(Class<T> type) throws InstantiationException {
        return type.cast(UNSAFE.allocateInstance(type));
    }

    private static void set(Class<?> owner, Object instance, String name, Object value) throws ReflectiveOperationException {
        Field field = owner.getDeclaredField(name);
        field.setAccessible(true);
        field.set(instance, value);
    }

    private static void require(boolean value, String message) {
        if (!value) throw new AssertionError(message);
    }

    private static final class CountingGraphics extends GuiGraphicsExtractor {
        private int sprites;
        private List<String> labels;

        private CountingGraphics() { super(null, null, 0, 0); }

        @Override
        public boolean containsPointInScissor(int x, int y) { return true; }

        @Override
        public void blitSprite(RenderPipeline pipeline, Identifier sprite, int x, int y, int width, int height, int color) {
            sprites++;
        }

        @Override
        public void text(Font font, String text, int x, int y, int color) { labels.add(text); }

        @Override
        public ActiveTextCollector textRendererForWidget(AbstractWidget widget, HoveredTextEffects effects) {
            return (ActiveTextCollector) Proxy.newProxyInstance(ActiveTextCollector.class.getClassLoader(),
                new Class<?>[]{ActiveTextCollector.class}, (proxy, method, arguments) -> {
                    if (method.getName().equals("acceptScrollingWithDefaultCenter")) {
                        labels.add(((Component) arguments[0]).getString());
                        return null;
                    }
                    throw new AssertionError("Unexpected default label path: " + method.getName());
                });
        }
    }

    private static final class InspectVertices implements VertexConsumer {
        private int count;
        public VertexConsumer addVertex(float x, float y, float z) {
            require(Float.isFinite(x) && Float.isFinite(y) && Float.isFinite(z), "Invalid model coordinate");
            count++;
            return this;
        }
        public VertexConsumer setColor(int red, int green, int blue, int alpha) { return this; }
        public VertexConsumer setColor(int color) { return this; }
        public VertexConsumer setUv(float u, float v) { return this; }
        public VertexConsumer setUv1(int u, int v) { return this; }
        public VertexConsumer setUv2(int u, int v) { return this; }
        public VertexConsumer setNormal(float x, float y, float z) { return this; }
        public VertexConsumer setLineWidth(float width) { return this; }
    }
}
