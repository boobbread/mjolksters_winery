package com.mjolkster.mjolksters_winery.mixin;

import net.minecraft.world.entity.ai.gossip.GossipContainer;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Villager.class)
public interface VillagerGossipAccessor {
    @Accessor("gossips")
    GossipContainer getGossips();
}