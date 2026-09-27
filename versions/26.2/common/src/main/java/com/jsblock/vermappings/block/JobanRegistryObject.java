package com.jsblock.vermappings.block;

import mtr.RegistryObject;
import mtr.mappings.RegistrationContext;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

/** Gives MTR's block/item constructors the actual jsblock registry identifier. */
public final class JobanRegistryObject<T> extends RegistryObject<T> {

    public JobanRegistryObject(String path, Supplier<T> supplier) {
        super(() -> RegistrationContext.construct(Identifier.fromNamespaceAndPath("jsblock", path), supplier));
    }
}
