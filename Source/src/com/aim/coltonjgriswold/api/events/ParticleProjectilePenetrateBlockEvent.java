package com.aim.coltonjgriswold.api.events;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;

import com.aim.coltonjgriswold.api.ParticleProjectile;

public class ParticleProjectilePenetrateBlockEvent extends ParticleProjectileHitBlockEvent {

    /**
     * Called when a projectile goes through a block
     *
     * @param who
     *            The entity responsible
     * @param start
     *            Where it started
     * @param end
     *            Where it went through
     * @param what
     *            The Block
     * @param projectile
     *            The actual Particle Projectile
     */
    public ParticleProjectilePenetrateBlockEvent(final LivingEntity who, final Location start, final Location end, final Block what, final ParticleProjectile projectile) {
	super(who, start, end, what, projectile);
    }
}
