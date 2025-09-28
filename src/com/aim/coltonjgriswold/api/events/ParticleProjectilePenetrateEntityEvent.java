package com.aim.coltonjgriswold.api.events;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.aim.coltonjgriswold.api.ParticleProjectile;

public class ParticleProjectilePenetrateEntityEvent extends ParticleProjectileHitEntityEvent {

    /**
     * Called when a projectile goes through an entity
     *
     * @param who        The entity responsible
     * @param start      Where it started
     * @param end        Where it went through
     * @param what       The entity
     * @param projectile The actual Particle Projectile
     */
    public ParticleProjectilePenetrateEntityEvent(final LivingEntity who, final Location start, final Location end, final Entity what, final ParticleProjectile projectile) {
        super(who, start, end, what, projectile);
    }
}
