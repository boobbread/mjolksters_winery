package com.mjolkster.mjolksters_winery.util;

import net.minecraft.util.StringRepresentable;

public enum TrellisConnections implements StringRepresentable {
    NONE("none"), EAST("east"), WEST("west"), EAST_WEST("east_west");

    private final String name;
    TrellisConnections(String name) { this.name = name; }

    @Override public String getSerializedName() { return name; }
    @Override public String toString() { return name; }
}
