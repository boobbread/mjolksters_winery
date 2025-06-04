package com.mjolkster.mjolksters_winery.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record JuiceType(int colour, String name) {
    public static final Codec<JuiceType> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.INT.fieldOf("colour").forGetter(JuiceType::colour),
            Codec.STRING.fieldOf("type").forGetter(JuiceType::name)
    ).apply(inst, JuiceType::new));
}