package com.mjolkster.mjolksters_winery.util;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum BiomeType implements StringRepresentable {
    FREEZING("freezing"),
    COLD("cold"),
    MILD("mild"),
    WARM("warm"),
    HOT("hot");

    private final String name;

    BiomeType(String name) {
        this.name = name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }
}