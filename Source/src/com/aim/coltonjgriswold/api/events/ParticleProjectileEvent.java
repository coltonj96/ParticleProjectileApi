package com.aim.coltonjgriswold.api.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.aim.coltonjgriswold.api.ParticleProjectile;

public class ParticleProjectileEvent extends Event implements Cancellable {
    
    private static final HandlerList handlers = new HandlerList();
    protected boolean cancelled = false;
    protected ParticleProjectile particle_projectile;
    private LivingEntity source;
    
    /**
     * Super class for ParticlePRojectile events
     *
     * @param who
     *            The entity responsible
     * @param projectile
     *            The actual Particle Projectile
     */
    public ParticleProjectileEvent(final LivingEntity who, final ParticleProjectile projectile) {
	source = who;
	particle_projectile = projectile;
    }
    
    /**
     * Gets the Entity whom the projectile came from (may be null)
     * 
     * @return LivingEntity that the projectile came from (may be null)
     */
    public LivingEntity getSourceEntity() {
	if (!cancelled)
	    return source;
	return null;
    }
    
    public static HandlerList getHandlerList() {
	return handlers;
    }

    @Override
    public HandlerList getHandlers() {
	return handlers;
    }

    @Override
    public boolean isCancelled() {
	return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
	cancelled = cancel;
    }

}
