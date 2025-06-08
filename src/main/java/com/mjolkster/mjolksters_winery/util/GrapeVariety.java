package com.mjolkster.mjolksters_winery.util;

import net.minecraft.util.StringRepresentable;

public enum GrapeVariety implements StringRepresentable {
    PINOT_NOIR("pinot_noir_grapes"),
    SANGIOVESE("sangiovese_grapes"),
    AUTUMN_ROYAL("autumn_royal_grapes"),
    CABERNET_SAUVIGNON("cabernet_sauvignon_grapes"),
    TEMPRANILLO("tempranillo_grapes"),
    MOONDROP("moondrop_grapes"),
    RUBY_ROMAN("ruby_roman_grapes"),

    RIESLING("riesling_grapes"),
    CHARDONNAY("chardonnay_grapes"),
    SAUVIGNON_BLANC("sauvignon_blanc_grapes"),
    PINOT_GRIGIO("pinot_grigio_grapes"),
    COTTON_CANDY("cotton_candy_grapes"),
    GRENACHE_BLANC("grenache_blanc_grapes"),
    WATERFALL("waterfall_grapes"),

    KOSHU("koshu_grapes"),
    PINOT_DE_LENFER("pinot_de_lenfer_grapes");

    private final String name;

    GrapeVariety(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}