package com.aim.coltonjgriswold.api.events;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.aim.coltonjgriswold.api.ParticleProjectile;

public class ParticleProjectileHitEntityEvent extends ParticleProjectileHitEvent {
    private Entity entity;

    /**
     * Called when a projectile hits an entity
     *
     * @param who
     *            The entity responsible
     * @param start
     *            Where it started
     * @param end
     *            Where it stopped
     * @param what
     *            The entity
     * @param projectile
     *            The actual Particle Projectile
     */
    public ParticleProjectileHitEntityEvent(final LivingEntity who, final Location start, final Location end, final Entity what, final ParticleProjectile projectile) {
	super(who, start, end, projectile);
	entity = what;
    }

    /**
     * Gets the entity that was hit
     * 
     * @return Entity that was hit or NULL if event was canceled
     */
    public Entity getEntity() {
	if (!cancelled)
	    return entity;
	return null;
    }
}
