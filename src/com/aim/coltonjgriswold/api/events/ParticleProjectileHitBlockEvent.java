package com.aim.coltonjgriswold.api.events;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;

import com.aim.coltonjgriswold.api.ParticleProjectile;

public class ParticleProjectileHitBlockEvent extends ParticleProjectileHitEvent {

    private final Block block;

    /**
     * Called when a projectile hits a block
     *
     * @param who        The entity responsible
     * @param start      Where it started
     * @param end        Where it stopped
     * @param what       The Block
     * @param projectile The actual Particle Projectile
     */
    public ParticleProjectileHitBlockEvent(final LivingEntity who, final Location start, final Location end, final Block what, final ParticleProjectile projectile) {
        super(who, start, end, projectile);
        block = what;
    }

    /**
     * Gets the block that was hit
     *
     * @return Block that was hit or NULL if event was canceled
     */
    public Block getBlock() {
        return !cancelled ? block : null;
    }
}
