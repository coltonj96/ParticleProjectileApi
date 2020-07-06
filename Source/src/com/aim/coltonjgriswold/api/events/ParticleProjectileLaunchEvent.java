package com.aim.coltonjgriswold.api.events;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import com.aim.coltonjgriswold.api.ParticleProjectile;

public class ParticleProjectileLaunchEvent extends ParticleProjectileEvent {

    private Location start_location;
    
    /**
     * Called when a projectile is launched
     *
     * @param who
     *            The entity responsible
     * @param start
     *            Where it started
     * @param projectile
     *            The actual Particle Projectile
     */
    public ParticleProjectileLaunchEvent(final LivingEntity who, final Location start, final ParticleProjectile projectile) {
	super(who, projectile);
	start_location = start;
    }
    
    /**
     * Gets the start location of the projectile
     * 
     * @return Start location of the projectile
     */
    public Location getStart() {
	if (!cancelled)
	    return start_location;
	return null;
    }
    
    /**
     * Gets the projectile color that hit
     * 
     * @return The projectile color
     */
    public Color getColor() {
	if (!cancelled)
	    return particle_projectile.getColor();
	return null;	   
    }
}
