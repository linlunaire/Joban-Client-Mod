package com.jsblock.compatibility;

import java.lang.reflect.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Arrays;

/** Runs one Java-call-site contract suite against the original Java and the Kotlin replacement. */
public final class JcmDataCompatibilityCheck {
    public static void main(String[] args) throws Exception {
        try (var original = loader(args[0], args[2]); var migrated = loader(args[1], args[2])) {
            for (String name : new String[]{"ScreenAlignment", "ScreenRoot", "InlineComponentEntry", "ConfigGuiEntry", "TextLabel", "PIDSPreset"}) {
                Class<?> before = original.loadClass("com.jsblock.data." + name);
                Class<?> after = migrated.loadClass(before.getName());
                require(Modifier.isFinal(before.getModifiers()) == Modifier.isFinal(after.getModifiers()), name + " changed extensibility");
                for (Field field : before.getDeclaredFields()) {
                    if (!visible(field.getModifiers()) || field.isSynthetic()) continue;
                    Field replacement = after.getDeclaredField(field.getName());
                    require(field.getType().getName().equals(replacement.getType().getName()) && field.getModifiers() == replacement.getModifiers(), "Field ABI changed: " + field);
                }
                for (Method method : before.getDeclaredMethods()) {
                    if (!visible(method.getModifiers()) || method.isSynthetic()) continue;
                    Method replacement = Arrays.stream(after.getDeclaredMethods()).filter(candidate -> signature(candidate).equals(signature(method))).findFirst().orElseThrow(() -> new AssertionError("Missing method: " + method));
                    require(Modifier.isStatic(method.getModifiers()) == Modifier.isStatic(replacement.getModifiers()), "Static ABI changed: " + method);
                    if (!Modifier.isStatic(method.getModifiers())) require(Modifier.isFinal(method.getModifiers()) == Modifier.isFinal(replacement.getModifiers()), "Method extensibility changed: " + method);
                }
                for (Constructor<?> constructor : before.getDeclaredConstructors()) {
                    if (!visible(constructor.getModifiers())) continue;
                    require(Arrays.stream(after.getDeclaredConstructors()).anyMatch(candidate -> parameterNames(candidate).equals(parameterNames(constructor))), "Missing constructor: " + constructor);
                }
            }
            String baseline = execute(original);
            String actual = execute(migrated);
            require(baseline.equals(actual), "Java/Kotlin scenario mismatch:\nJava: " + baseline + "\nKotlin: " + actual);
            System.out.println("PASS: six JCM Kotlin data types preserve Java fields, signatures, extensibility, layout arithmetic, null/alias semantics and PIDS JSON behavior; " + actual);
        }
    }

    private static boolean visible(int modifiers) { return Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers); }
    private static String parameterNames(Executable method) { return Arrays.toString(Arrays.stream(method.getParameterTypes()).map(Class::getName).toArray()); }
    private static String signature(Method method) { return method.getName() + parameterNames(method) + method.getReturnType().getName(); }
    private static String execute(ClassLoader loader) throws Exception {
        try { return (String) loader.loadClass("com.jsblock.compatibility.JcmDataScenario").getMethod("run").invoke(null); }
        catch (InvocationTargetException failure) { throw new AssertionError("Scenario failed", failure.getCause()); }
    }
    private static URLClassLoader loader(String classes, String tests) throws Exception {
        return new URLClassLoader(new URL[]{Path.of(classes).toUri().toURL(), Path.of(tests).toUri().toURL()}, JcmDataCompatibilityCheck.class.getClassLoader()) {
            @Override protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
                if (!name.startsWith("com.jsblock.data.") && !name.startsWith("com.jsblock.compatibility.JcmDataScenario")) return super.loadClass(name, resolve);
                synchronized (getClassLoadingLock(name)) {
                    Class<?> type = findLoadedClass(name);
                    if (type == null) type = findClass(name);
                    if (resolve) resolveClass(type);
                    return type;
                }
            }
        };
    }
    private static void require(boolean value, String message) { if (!value) throw new AssertionError(message); }
}
