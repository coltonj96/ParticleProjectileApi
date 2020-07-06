package com.aim.coltonjgriswold.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import com.aim.coltonjgriswold.ParticleProjectileApi;
import com.aim.coltonjgriswold.api.events.ParticleProjectileHitBlockEvent;
import com.aim.coltonjgriswold.api.events.ParticleProjectileHitEntityEvent;
import com.aim.coltonjgriswold.api.events.ParticleProjectileHitEvent;
import com.aim.coltonjgriswold.api.events.ParticleProjectileLaunchEvent;
import com.aim.coltonjgriswold.api.events.ParticleProjectilePenetrateBlockEvent;
import com.aim.coltonjgriswold.api.events.ParticleProjectilePenetrateEntityEvent;

public abstract class ParticleProjectile {

    private List<EntityType> entityTypes;
    private List<Entity> entities;
    private List<Material> materials;

    private Color particle_color;
    private Material particle_data;

    private ParticleProjectile projectile;

    private Particle particle_type;

    private double time;
    private double particle_velocity;
    private double hitbox;

    private Vector dir;

    public static Map<UUID, BukkitTask> projectiles;

    {
	projectiles = new HashMap<UUID, BukkitTask>();
    }

    /**
     * Super class for creating custom particle projectiles
     * 
     * @param particle
     *            Particle type
     * @param size
     *            Size of the hitbox (normal: 0.1)
     * @param velocity
     *            Velocity of the projectile in meters/second
     * @param lifespan
     *            The amount of time the projectile has to live
     */
    public ParticleProjectile(final Particle particle, final double size, final double velocity, final double lifespan) {
	particle_type = particle;
	particle_velocity = velocity != 0 ? velocity : 10.0;
	time = lifespan > 0 ? lifespan : 3.0;
	hitbox = size > 0 ? size : 0.1;
	particle_color = Color.fromRGB(0, 0, 0);
	particle_data = Material.STONE;
	projectile = this;
	entityTypes = new ArrayList<EntityType>();
	entities = new ArrayList<Entity>();
	materials = new ArrayList<Material>();
    }

    /**
     * Launches the particle with physics affecting trajectory
     * 
     * @param who
     *            The source entity launching the projectile
     */
    public final void launch(final LivingEntity who) {
	launch(who, who.getWorld(), new Vector(0, 0, 0), true);
    }

    /**
     * Launches the particle with optional physics affecting trajectory
     * 
     * @param who
     *            The source entity launching the projectile
     * @param physics
     *            Set to True for the projectile to be affected by physics
     *            (gravity and drag)
     */
    public final void launch(final LivingEntity who, final boolean physics) {
	launch(who, who.getWorld(), new Vector(0, 0, 0), physics);
    }

    /**
     * Launches the particle from a LivingEntity with optional physics affecting
     * trajectory
     * 
     * @param who
     *            The source entity launching the projectile
     * @param where
     *            The world the projectile is located
     * @param physics
     *            Set to True for the projectile to be affected by physics
     *            (gravity and drag)
     */
    public final void launch(final LivingEntity who, final World where, final boolean physics) {
	launch(who, where, new Vector(0, 0, 0), physics);
    }

    /**
     * Launches the particle with offset and optional physics affecting
     * trajectory
     * 
     * @param who
     *            The source entity launching the projectile
     * @param where
     *            The world the projectile is located
     * @param end_offset
     *            Affects end location of particle
     * @param physics
     *            Set to True for the projectile to be affected by physics
     */
    public final void launch(final LivingEntity who, final World where, final Vector end_offset, final boolean physics) {
	launch(who, where, new Vector(0, 0, 0), end_offset, physics);
    }

    /**
     * Launches the particle with offsets and optional physics affecting
     * trajectory
     * 
     * @param who
     *            The source entity launching the projectile
     * @param where
     *            The world the projectile is located
     * @param start_offset
     *            Affects start location of particle
     * @param end_offset
     *            Affects end location of particle
     * @param physics
     *            Set to True for the projectile to be affected by physics
     */
    public final void launch(final LivingEntity who, final World where, final Vector start_offset, final Vector end_offset, final boolean physics) {
	launch(who, where, who.getEyeLocation(), start_offset, end_offset, physics);
    }

    /**
     * Launches the particle from start with offsets and optional physics
     * affecting trajectory
     * 
     * @param who
     *            The source entity launching the projectile (can be null)
     * @param where
     *            The world the projectile is located
     * @param start
     *            Location that the particle starts at
     * @param start_offset
     *            Affects start location of particle
     * @param end_offset
     *            Affects end location of particle
     * @param physics
     *            Set to True for the projectile to be affected by physics
     */
    public final void launch(final LivingEntity who, final World where, final Location start, final Vector start_offset, final Vector end_offset, final boolean physics) {
	launch(who, where, start.toVector(), start.getDirection(), start_offset, end_offset, physics);
    }

    /**
     * Launches the particle from start in direction with offsets and optional
     * physics affecting trajectory
     * 
     * @param who
     *            The source entity launching the projectile (can be null)
     * @param where
     *            The world the projectile is located
     * @param start
     *            Location that the particle starts at
     * @param direction
     *            Direction of travel
     * @param start_offset
     *            Affects start location of particle
     * @param end_offset
     *            Affects end location of particle
     * @param physics
     *            Set to True for the projectile to be affected by physics
     */
    public final void launch(final LivingEntity who, final World where, final Vector start, final Vector direction, final Vector start_offset, final Vector end_offset, final boolean physics) {

	final Particle type = particle_type;

	projectile = this;

	start.add(direction.clone().normalize().add(start_offset));
	final double lifespan = time;
	final Location start_location = start.toLocation(where).setDirection(direction);
	final Vector end = start.clone().add(direction.clone().normalize().multiply(particle_velocity * lifespan / 20.0).add(end_offset));

	ParticleProjectileLaunchEvent event = new ParticleProjectileLaunchEvent(who, start_location, projectile);
	Bukkit.getServer().getPluginManager().callEvent(event);
	if (event.isCancelled()) {
	    return;
	}

	OnLaunch(who, where, start.clone());

	new BukkitRunnable() {
	    private double t = 0.0;
	    private double step = 1.0 / (lifespan * 20.0);
	    private Vector v = new Vector(0.0, 0.49, 0.0);
	    private Location previous = start_location;

	    @Override
	    public void run() {
		Location lerp = start.clone().add(end.clone().subtract(start).multiply(t)).toLocation(where).setDirection(direction);

		if (physics)
		    end.subtract(v);
		switch (type) {
		case REDSTONE:
		    where.spawnParticle(type, lerp, 0, 0, 0, 0, 0, new Particle.DustOptions(particle_color, 0.5f), true);
		    break;
		case ITEM_CRACK:
		    where.spawnParticle(type, lerp, 0, 0, 0, 0, 0, new ItemStack(particle_data), true);
		    break;
		case BLOCK_CRACK:
		case BLOCK_DUST:
		case FALLING_DUST:
		    where.spawnParticle(type, lerp, 0, 0, 0, 0, 0, particle_data.createBlockData(), true);
		    break;
		default:
		    where.spawnParticle(type, lerp, 0, 0, 0, 0, 0, null, true);
		    break;
		}
		RayTraceResult result = null;
		if (t > 0.0) {
		    Vector temp = lerp.toVector().subtract(previous.toVector());
		    double length = previous.distance(lerp);
		    result = where.rayTrace(previous, temp.normalize(), length, FluidCollisionMode.ALWAYS, false, hitbox, null);
		}
		if (t >= 1.0) {
		    ParticleProjectileHitEvent event = new ParticleProjectileHitEvent(who, start_location, lerp, projectile);
		    Bukkit.getServer().getPluginManager().callEvent(event);
		    if (!event.isCancelled()) {
			OnHit(who, where, start, lerp.toVector(), t);
			cancel();
			return;
		    }
		} else if (result != null) {
		    if (result.getHitBlock() != null) {
			Block hit = result.getHitBlock();
			Vector pos = result.getHitPosition();
			Location loc = pos.toLocation(where);
			dir = pos.clone().subtract(start);
			if (!materials.contains(hit.getType())) {
			    ParticleProjectileHitBlockEvent event = new ParticleProjectileHitBlockEvent(who, start_location, loc, hit, projectile);
			    Bukkit.getServer().getPluginManager().callEvent(event);
			    if (!event.isCancelled()) {
				OnHitBlock(who, where, start, pos, hit, t);
				cancel();
				return;
			    }
			} else if (materials.contains(hit.getType())) {
			    ParticleProjectilePenetrateBlockEvent event = new ParticleProjectilePenetrateBlockEvent(who, start_location, loc, hit, projectile);
			    Bukkit.getServer().getPluginManager().callEvent(event);
			    if (!event.isCancelled()) {
				OnPenetrateBlock(who, where, pos, hit, t);
			    }
			}
		    } else if (result.getHitEntity() != null) {
			Entity hit = result.getHitEntity();
			Vector pos = result.getHitPosition();
			Location loc = pos.toLocation(where);
			if (!entities.contains(hit) && !entityTypes.contains(hit.getType())) {
			    ParticleProjectileHitEntityEvent event = new ParticleProjectileHitEntityEvent(who, start_location, loc, hit, projectile);
			    Bukkit.getServer().getPluginManager().callEvent(event);
			    if (!event.isCancelled()) {
				OnHitEntity(who, where, start, pos, hit, t);
				cancel();
				return;
			    }
			} else if (entities.contains(hit) || entityTypes.contains(hit.getType())) {
			    ParticleProjectilePenetrateEntityEvent event = new ParticleProjectilePenetrateEntityEvent(who, start_location, loc, hit, projectile);
			    Bukkit.getServer().getPluginManager().callEvent(event);
			    if (!event.isCancelled()) {
				OnPenetrateEntity(who, where, pos, hit, t);
			    }
			}
		    }
		}
		OnMove(where, previous.toVector(), lerp.toVector(), t);
		previous = lerp;
		t += step;
	    }
	}.runTaskTimer(ParticleProjectileApi.instance(), 0, 1);
    }

    /**
     * Sets the projectile to ignore specific types of entities
     * 
     * @param type
     *            Type of entity to ignore
     */
    public final void ignoreEntityType(EntityType type) {
	if (!entityTypes.contains(type))
	    entityTypes.add(type);
    }

    /**
     * Sets the projectile to ignore specific entities
     * 
     * @param entity
     *            Entity to ignore
     */
    public final void ignoreEntity(Entity entity) {
	if (!entities.contains(entity))
	    entities.add(entity);
    }

    /**
     * Sets the projectile to ignore specific types of meaterials
     * 
     * @param type
     *            Type of material to ignore
     */
    public final void ignoreMaterial(Material type) {
	if (!materials.contains(type))
	    materials.add(type);
    }

    /**
     * Sets the projectiles color (If applicable)
     * 
     * @param color
     *            The color to apply
     */
    public final void setColor(Color color) {
	particle_color = color;
    }

    /**
     * Sets the projectiles particle data (If applicable)
     * 
     * @param material
     *            The material to use as data
     */
    public final void setData(Material material) {
	switch (particle_type) {
	case ITEM_CRACK:
	    if (material.isItem())
		particle_data = material;
	    break;
	case BLOCK_CRACK:
	case BLOCK_DUST:
	case FALLING_DUST:
	    if (material.isBlock())
		particle_data = material;
	    break;
	default:
	    break;
	}
    }

    /**
     * Gets the projectiles color
     * 
     * @return A Color
     */
    public final Color getColor() {
	return particle_color;
    }

    /**
     * Gets the projectiles particle data
     * 
     * @return The Material of this data
     */
    public final Material getData() {
	return particle_data;
    }

    /**
     * Gets the projectiles velocity
     * 
     * @return The velocity of the particle projectile
     */
    public final double getVelocity() {
	return particle_velocity;
    }

    /**
     * Sets the particles velocity (Only works before launch or after finished
     * launch)
     * 
     * @param velocity
     *            The new velocity
     */
    public final void setVelocity(double velocity) {
	particle_velocity = velocity;
    }

    /**
     * Gets the projectiles lifespan
     * 
     * @return The lifespan of the particle projectile
     */
    public final double getLifespan() {
	return time;
    }

    /**
     * Sets the particles lifespan (Only works before launch or after finished
     * launch)
     * 
     * @param lifespan
     *            The new lifespan
     */
    public final void setLifespan(double lifespan) {
	if (lifespan <= 0)
	    return;
	time = lifespan;
    }

    public final void setParticleType(Particle particle) {
	particle_type = particle;
    }

    public final Particle getParticleType() {
	return particle_type;
    }

    public final Vector getDirection() {
	return dir;
    }

    /**
     * Called after a particle projectile starts moving
     * 
     * @param who
     *            The source whom shot the projectile
     * @param world
     *            The world this occured in
     * @param start
     *            The start location of the projectile
     */
    protected void OnLaunch(LivingEntity who, World world, Vector start) {
    }

    /**
     * Called after a particle projectile stops moving
     * 
     * @param who
     *            The source whom shot the projectile
     * @param world
     *            The world this occured in
     * @param start
     *            The start location of the projectile
     * @param end
     *            The end location of the projectile
     * @param t
     *            Step
     */
    protected void OnHit(LivingEntity who, World world, Vector start, Vector end, double t) {
    }

    /**
     * Called after a particle projectile hits a block
     * 
     * @param who
     *            The entity whom launched the projectile
     * @param world
     *            The world this occured in
     * @param start
     *            The start location of the projectile
     * @param end
     *            The end location of the projectile
     * @param block
     *            The block that was hit
     * @param t
     *            Step
     */
    protected void OnHitBlock(LivingEntity who, World world, Vector start, Vector end, Block block, double t) {
    }

    /**
     * Called after a particle projectile hits an entity
     * 
     * @param who
     *            The entity whom launched the projectile
     * @param world
     *            The world this occured in
     * @param start
     *            The start location of the projectile
     * @param end
     *            The end location of the projectile
     * @param entity
     *            The entity that was hit
     * @param t
     *            Step
     */
    protected void OnHitEntity(LivingEntity who, World world, Vector start, Vector end, Entity entity, double t) {
    }

    /**
     * Called when a particle moves position
     * 
     * @param world
     *            The world this occured in
     * @param previous
     *            The previous location of the projectile
     * @param current
     *            The current location of the projectile
     * @param t
     *            Step
     */
    protected void OnMove(World world, Vector previous, Vector current, double t) {
    }

    /**
     * Called after a particle projectile penetrates through a block
     * 
     * @param who
     *            The entity whom launched the projectile
     * @param world
     *            The world this occured in
     * @param where
     *            The pass-through location of the projectile
     * @param block
     *            The block involved
     * @param t
     *            Step
     */
    protected void OnPenetrateBlock(LivingEntity who, World world, Vector where, Block block, double t) {
    }

    /**
     * Called after a particle projectile penetrates through an entity
     * 
     * @param who
     *            The entity whom launched the projectile
     * @param world
     *            The world this occured in
     * @param where
     *            The pass-through location of the projectile
     * @param entity
     *            The entity involved
     * @param t
     *            Step
     */
    protected void OnPenetrateEntity(LivingEntity who, World world, Vector where, Entity entity, double t) {
    }
}
