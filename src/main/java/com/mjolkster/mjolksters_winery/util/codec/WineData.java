package com.mjolkster.mjolksters_winery.util.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record WineData(int colour, String name, int barrelAge, String barrelType, float alcoholPercentage, float wineSweetness) {
    public static final Codec<WineData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.INT.fieldOf("colour").forGetter(WineData::colour),
            Codec.STRING.fieldOf("type").forGetter(WineData::name),
            Codec.INT.fieldOf("barrel_age").forGetter(WineData::barrelAge),
            Codec.STRING.fieldOf("barrel_type").forGetter(WineData::barrelType),
            Codec.FLOAT.fieldOf("alcohol_perc").forGetter(WineData::alcoholPercentage),
            Codec.FLOAT.fieldOf("sweetness").forGetter(WineData::wineSweetness)
    ).apply(inst, WineData::new));
}