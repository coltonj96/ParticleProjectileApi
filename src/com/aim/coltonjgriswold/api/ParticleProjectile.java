package com.aim.coltonjgriswold.api;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public abstract class ParticleProjectile implements IParticleProjectile {

    //protected static List<LaunchedProjectileData> projectiles = new ArrayList<>();

    protected Set<EntityType> entityTypes = new HashSet<>();
    protected Set<Entity> entities = new HashSet<>();
    protected Set<Material> materials = new HashSet<>();

    protected Color particle_color;
    protected Color particle_color_to;
    protected Material particle_data;

    protected Particle particle_type;

    protected double time;
    protected double particle_velocity;
    protected double hitbox;

    protected long ticks = 1;

    protected Vector dir = new Vector();

    /**
     * Super class for creating custom particle projectiles
     *
     * @param particle Particle type
     * @param size     Size of the hitbox (normal: 0.1)
     * @param velocity Velocity of the projectile in meters/second
     * @param lifespan The amount of time the projectile has to live
     */
    public ParticleProjectile(final Particle particle, final double size, final double velocity, final double lifespan) {
        particle_type = particle;
        particle_velocity = velocity != 0 ? velocity : 10.0;
        time = lifespan > 0 ? lifespan : 3.0;
        hitbox = size > 0 ? size : 0.1;
        particle_color = Color.fromRGB(0, 0, 0);
        particle_color_to = Color.fromRGB(0, 0, 0);
        particle_data = Material.STONE;
    }

    public void setTicks(long t) {
        ticks = t;
    }

    /**
     * Launches the particle with physics affecting trajectory
     *
     * @param who The source entity launching the projectile
     */
    public final void launchSingle(LivingEntity who) {
        launchSingle(who, who.getWorld(), new Vector(0, 0, 0), true);
    }

    /**
     * Launches the particle with optional physics affecting trajectory
     *
     * @param who     The source entity launching the projectile
     * @param physics Set to True for the projectile to be affected by physics
     *                (gravity and drag)
     */
    public final void launchSingle(LivingEntity who, boolean physics) {
        launchSingle(who, who.getWorld(), new Vector(0, 0, 0), physics);
    }

    /**
     * Launches the particle from a LivingEntity with optional physics affecting
     * trajectory
     *
     * @param who     The source entity launching the projectile
     * @param where   The world the projectile is located
     * @param physics Set to True for the projectile to be affected by physics
     *                (gravity and drag)
     */
    public final void launchSingle(LivingEntity who, World where, boolean physics) {
        launchSingle(who, where, new Vector(0, 0, 0), physics);
    }

    /**
     * Launches the particle with offset and optional physics affecting
     * trajectory
     *
     * @param who        The source entity launching the projectile
     * @param where      The world the projectile is located
     * @param end_offset Affects end location of particle
     * @param physics    Set to True for the projectile to be affected by physics
     */
    public final void launchSingle(LivingEntity who, World where, Vector end_offset, boolean physics) {
        launchSingle(who, where, new Vector(0, 0, 0), end_offset, physics);
    }

    /**
     * Launches the particle with offsets and optional physics affecting
     * trajectory
     *
     * @param who          The source entity launching the projectile
     * @param where        The world the projectile is located
     * @param start_offset Affects start location of particle
     * @param end_offset   Affects end location of particle
     * @param physics      Set to True for the projectile to be affected by physics
     */
    public final void launchSingle(LivingEntity who, World where, Vector start_offset, Vector end_offset, boolean physics) {
        launchSingle(who, where, who.getEyeLocation(), start_offset, end_offset, physics);
    }

    /**
     * Launches the particle from start with offsets and optional physics
     * affecting trajectory
     *
     * @param who          The source entity launching the projectile (can be null)
     * @param where        The world the projectile is located
     * @param start        Location that the particle starts at
     * @param start_offset Affects start location of particle
     * @param end_offset   Affects end location of particle
     * @param physics      Set to True for the projectile to be affected by physics
     */
    public final void launchSingle(LivingEntity who, World where, Location start, Vector start_offset, Vector end_offset, boolean physics) {
        launchSingle(who, where, start.toVector(), start.getDirection(), start_offset, end_offset, physics);
    }

    /**
     * Launches the particle from start in direction with offsets and optional
     * physics affecting trajectory
     *
     * @param who          The source entity launching the projectile (can be null)
     * @param where        The world the projectile is located
     * @param start        Location that the particle starts at
     * @param direction    Direction of travel
     * @param start_offset Affects start location of particle
     * @param end_offset   Affects end location of particle
     * @param physics      Set to True for the projectile to be affected by physics
     */
    public final void launchSingle(LivingEntity who, World where, Vector start, Vector direction, Vector start_offset, Vector end_offset, boolean physics) {
        launchSingle(new LaunchedProjectileData(this, who, where, start, direction, start_offset, end_offset, physics));
    }

    public final void launchSingle(LaunchedProjectileData data) {
        ParticleProjectiles.launchSingle(data);
    }

    public final void launchMultiple(List<LaunchedProjectileData> data) {
        ParticleProjectiles.launchMultiple(data);
    }

    /**
     * Sets the projectile to ignore specific types of entities
     *
     * @param type Type of entity to ignore
     */
    public final void ignoreEntityType(EntityType type) {
        entityTypes.add(type);
    }

    /**
     * Sets the projectile to ignore specific types of entities
     *
     * @param types Types of entities to ignore
     */
    public final void ignoreEntityTypes(Collection<? extends EntityType> types) {
        entityTypes.addAll(types);
    }

    /**
     * Sets the projectile to ignore specific entities
     *
     * @param entity Entity to ignore
     */
    public final void ignoreEntity(Entity entity) {
        entities.add(entity);
    }

    /**
     * Sets the projectile to ignore specific entities
     *
     * @param entities Entity to ignore
     */
    public final void ignoreEntities(Collection<? extends Entity> entities) {
        this.entities.addAll(entities);
    }

    /**
     * Sets the projectile to ignore specific types of meaterials
     *
     * @param material Type of material to ignore
     */
    public final void ignoreMaterial(Material material) {
        materials.add(material);
    }

    /**
     * Sets the projectile to ignore specific types of meaterials
     *
     * @param materials Type of material to ignore
     */
    public final void ignoreMaterials(Collection<? extends Material> materials) {
        this.materials.addAll(materials);
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
     * Sets the projectiles color (If applicable)
     *
     * @param color The color to apply
     */
    public final void setColor(Color color) {
        particle_color = color;
    }

    /**
     * Gets the projectiles second color
     *
     * @return A Color
     */
    public final Color getSecondaryColor() {
        return particle_color_to;
    }

    /**
     * Sets the projectiles secondary color (If applicable)
     *
     * @param color The color to apply
     */
    public final void setSecondaryColor(Color color) {
        particle_color_to = color;
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
     * Sets the projectiles particle data (If applicable)
     *
     * @param material The material to use as data
     */
    public final void setData(Material material) {
        switch (particle_type.toString()) {
            case "ITEM":
                if (material.isItem())
                    particle_data = material;
            case "BLOCK", "FALLING_DUST", "BLOCK_MARKER", "DUST_PILLAR":
                if (material.isBlock())
                    particle_data = material;
            default:
                break;
        }
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
     * @param velocity The new velocity
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
     * @param lifespan The new lifespan
     */
    public final void setLifespan(double lifespan) {
        if (lifespan <= 0)
            return;
        time = lifespan;
    }

    /**
     * Gets the particle type
     *
     * @return The particle type
     */
    public final Particle getParticleType() {
        return particle_type;
    }

    /**
     * Sets the particle type
     *
     * @param particle A particle
     */
    public final void setParticleType(Particle particle) {
        particle_type = particle;
    }

    /**
     * Gets the direction
     *
     * @return The direction
     */
    public final Vector getDirection() {
        return dir;
    }
}
