package com.aim.coltonjgriswold.api.events;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import com.aim.coltonjgriswold.api.ParticleProjectile;

public class ParticleProjectileHitEvent extends ParticleProjectileLaunchEvent {

    private final Location end_location;

    /**
     * Called when a projectile hits something
     *
     * @param who        The entity responsible
     * @param start      Where it started
     * @param end        Where it stopped
     * @param projectile The actual Particle Projectile
     */
    public ParticleProjectileHitEvent(final LivingEntity who, final Location start, final Location end, final ParticleProjectile projectile) {
        super(who, start, projectile);
        end_location = end;
    }

    /**
     * Gets the end location of the projectile
     *
     * @return End location of the projectile
     */
    public Location getEnd() {
        return !cancelled ? end_location : null;
    }
}
