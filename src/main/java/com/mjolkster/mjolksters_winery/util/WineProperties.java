package com.mjolkster.mjolksters_winery.util;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class WineProperties {

    // Red
    public static final FoodProperties PINOT_NOIR = new FoodProperties.Builder()
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 15, 0), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 20 * 15, 3), 1.0f)
            .build();

    public static final FoodProperties SANGIOVESE = new FoodProperties.Builder()
            .effect(() -> new MobEffectInstance(MobEffects.JUMP, 20 * 15, 2), 1.0f)
            .build();

    public static final FoodProperties CABERNET_SAUVIGNON = new FoodProperties.Builder()
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20 * 60 * 2, 2), 1.0f)
            .build();

    public static final FoodProperties TEMPRANILLO = new FoodProperties.Builder()
            .effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 20 * 15, 2), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.INFESTED, 20 * 60 * 5, 0), 1.0f)
            .build();

    public static final FoodProperties MOONDROP = new FoodProperties.Builder()
            .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 20 * 60 * 5, 0), 1.0f)
            .build();

    public static final FoodProperties RUBY_ROMAN = new FoodProperties.Builder()
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 60 * 2, 0), 1.0f)
            .build();

    public static final FoodProperties AUTUMN_ROYAL = new FoodProperties.Builder()
            .effect(() -> new MobEffectInstance(MobEffects.INVISIBILITY, 20 * 15, 0), 1.0f)
            .build();

    // White
    public static final FoodProperties RIESLING = new FoodProperties.Builder()
            .effect(() -> new MobEffectInstance(MobEffects.SATURATION, 20 * 60 * 2, 0), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, 20 * 60 * 2, 0), 1.0f)
            .build();

    public static final FoodProperties CHARDONNAY = new FoodProperties.Builder()
            .effect(() -> new MobEffectInstance(MobEffects.INFESTED, 20 * 60 * 5, 0), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.WEAVING, 20 * 60 * 5, 3), 1.0f)
            .build();

    public static final FoodProperties SAUVIGNON_BLANC = new FoodProperties.Builder()
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 20 * 60 * 2, 0), 1.0f)
            .build();

    public static final FoodProperties PINOT_GRIGIO = new FoodProperties.Builder()
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 15, 0), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 20 * 15, 3), 1.0f)
            .build();

    public static final FoodProperties COTTON_CANDY = new FoodProperties.Builder()
            .effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 20 * 15, 0), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * 15, 0), 1.0f)
            .build();

    public static final FoodProperties GRENACHE_BLANC = new FoodProperties.Builder()
            .effect(() -> new MobEffectInstance(MobEffects.SLOW_FALLING, 20 * 60 * 2, 0), 1.0f)
            .build();

    public static final FoodProperties WATERFALL = new FoodProperties.Builder()
            .effect(() -> new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 20 * 30, 0), 1.0f)
            .build();

    // Special
    public static final FoodProperties KOSHU = new FoodProperties.Builder()
            .effect(() -> new MobEffectInstance(MobEffects.LUCK, 20 * 60 * 5, 2), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.WATER_BREATHING, 20 * 60 * 5, 2), 1.0f)
            .build();

    public static final FoodProperties PINOT_DE_LENFER = new FoodProperties.Builder()
            .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20 * 30, 0), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 20 * 15, 2), 1.0f)
            .build();

    // Vanilla
    public static final FoodProperties SWEET_BERRY = new FoodProperties.Builder()
            .effect(() -> new MobEffectInstance(MobEffects.LUCK, 20 * 60 * 5, 0), 1.0f)
            .build();

    public static final FoodProperties GLOW_BERRY = new FoodProperties.Builder()
            .effect(() -> new MobEffectInstance(MobEffects.GLOWING, 20 * 15, 0), 1.0f)
            .build();

    public static final FoodProperties CHORUS_FRUIT = new FoodProperties.Builder()
            .effect(() -> new MobEffectInstance(MobEffects.LEVITATION, 20 * 10, 0), 1.0f)
            .build();
}
