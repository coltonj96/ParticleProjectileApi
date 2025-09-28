package com.aim.coltonjgriswold.api;

import org.bukkit.*;
import org.bukkit.Vibration.Destination.BlockDestination;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import com.aim.coltonjgriswold.api.events.ParticleProjectileHitBlockEvent;
import com.aim.coltonjgriswold.api.events.ParticleProjectileHitEntityEvent;
import com.aim.coltonjgriswold.api.events.ParticleProjectileHitEvent;
import com.aim.coltonjgriswold.api.events.ParticleProjectileLaunchEvent;
import com.aim.coltonjgriswold.api.events.ParticleProjectilePenetrateBlockEvent;
import com.aim.coltonjgriswold.api.events.ParticleProjectilePenetrateEntityEvent;

import java.util.Random;

public class LaunchedProjectileData {

    protected ParticleProjectile projectile;
    protected double t = 0.0;
    protected double step;
    protected Vector v = new Vector(0.0, 9.8 / 3.0, 0.0); //0.0, 0.49, 0.0
    protected Location previous;
    protected LivingEntity who;
    protected World where;
    protected Vector start;
    protected Vector direction;
    protected Vector start_offset;
    protected Location start_location;
    protected Vector end_offset;
    protected boolean physics;
    protected Location lerp;
    protected Vector end;

    private boolean running = false;

    public <T extends ParticleProjectile> LaunchedProjectileData(T projectile, LivingEntity who) {
        this(projectile, who, who.getWorld(), new Vector(0, 0, 0), true);
    }

    public <T extends ParticleProjectile> LaunchedProjectileData(T projectile, LivingEntity who, boolean physics) {
        this(projectile, who, who.getWorld(), new Vector(0, 0, 0), physics);
    }

    public <T extends ParticleProjectile> LaunchedProjectileData(T projectile, LivingEntity who, World where, boolean physics) {
        this(projectile, who, where, new Vector(0, 0, 0), physics);
    }

    public <T extends ParticleProjectile> LaunchedProjectileData(T projectile, LivingEntity who, World where, Vector end_offset, boolean physics) {
        this(projectile, who, where, new Vector(0, 0, 0), end_offset, physics);
    }

    public <T extends ParticleProjectile> LaunchedProjectileData(T projectile, LivingEntity who, World where, Vector start_offset, Vector end_offset, boolean physics) {
        this(projectile, who, where, who.getEyeLocation(), start_offset, end_offset, physics);
    }

    public <T extends ParticleProjectile> LaunchedProjectileData(T projectile, LivingEntity who, World where, Location start, Vector start_offset, Vector end_offset, boolean physics) {
        this(projectile, who, where, start.toVector(), start.getDirection(), start_offset, end_offset, physics);
    }

    public <T extends ParticleProjectile> LaunchedProjectileData(T projectile, LivingEntity who, World where, Vector start, Vector direction, Vector start_offset, Vector end_offset, boolean physics) {
        this.projectile = projectile;
        this.who = who;
        this.where = where;
        this.start = start.add(direction.clone().normalize().add(start_offset));
        this.direction = direction;
        this.start_offset = start_offset;
        this.end_offset = end_offset;
        this.physics = physics;

        step = 1.0 / (projectile.time * 20.0);
        start_location = start.toLocation(where).setDirection(direction);
        end = start.clone().add(direction.clone().normalize().multiply(projectile.particle_velocity * projectile.time / 20.0).add(end_offset));
        previous = start_location;
    }

    /**
     * Utility method for internal use
     *
     * @return Boolean
     */
    public final boolean update() {
        if (!running) {
            ParticleProjectileLaunchEvent event = new ParticleProjectileLaunchEvent(who, start_location, projectile);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled())
                return false;
            projectile.OnLaunch(who, where, start.clone());
            running = true;
        }

        lerp = start.clone().add(end.clone().subtract(start).multiply(t)).toLocation(where).setDirection(direction);

        if (physics) {
            end.subtract(v);
            v = v.add(lerp(v, new Vector(0.0, 0.0, 0.0), 0.9));
            //v = v.add(new Vector(0.0, (9.8 / 3.0), 0.0));
            //v = v.setY(v.getY() + (9.8 / 3.0));
        }
        //Debugging.debug(projectile.particle_type.getDataType().getSimpleName());
        switch (projectile.particle_type.getDataType().getSimpleName()) {
            case "Color":
                where.spawnParticle(projectile.particle_type, lerp, 0, 0, 0, 0, 0, projectile.particle_color, true);
                break;
            case "DustOptions", "DustTransition":
                where.spawnParticle(projectile.particle_type, lerp, 0, 0, 0, 0, 0, new Particle.DustTransition(projectile.particle_color, projectile.particle_color_to, 0.5F), true);
                break;
            case "ItemStack":
                where.spawnParticle(projectile.particle_type, lerp, 0, 0, 0, 0, 0, new ItemStack(projectile.particle_data), true);
                break;
            case "BlockData":
                where.spawnParticle(projectile.particle_type, lerp, 0, 0, 0, 0, 0, projectile.particle_data.createBlockData(), true);
                break;
            case "Vibration":
                where.spawnParticle(projectile.particle_type, lerp, 0, 0, 0, 0, 0, new Vibration(previous, new BlockDestination(lerp), (int) projectile.ticks), true);
                break;
            case "Trail":
                where.spawnParticle(projectile.particle_type, lerp, 0, 0, 0, 0, 0, new Particle.Trail(lerp, projectile.particle_color, projectile.time >= 1 ? (int) projectile.time : 1), true);
                break;
            case "Integer":
                where.spawnParticle(projectile.particle_type, lerp, 0, 0, 0, 0, 0, new Random().nextInt(100), true);
                break;
            case "Float":
                where.spawnParticle(projectile.particle_type, lerp, 0, 0, 0, 0, 0, new Random().nextFloat(10F), true);
                break;
            default:
                where.spawnParticle(projectile.particle_type, lerp, 0, 0, 0, 0, 0, null, true);
        }
        RayTraceResult result = null;
        if (t > 0.0) {
            Vector temp = lerp.toVector().subtract(previous.toVector());
            double length = previous.distance(lerp);
            result = where.rayTrace(previous, temp.normalize(), length, FluidCollisionMode.ALWAYS, false, projectile.hitbox, null);
        }
        if (t >= 1.0) {
            ParticleProjectileHitEvent event = new ParticleProjectileHitEvent(who, start_location, lerp, projectile);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                projectile.OnHit(who, where, start, lerp.toVector(), t);
                return false;
            }
        }
        if (result != null) {
            if (result.getHitBlock() != null) {
                Block hit = result.getHitBlock();
                Vector pos = result.getHitPosition();
                Location loc = pos.toLocation(where);
                projectile.dir = pos.clone().subtract(start);
                if (!projectile.materials.contains(hit.getType())) {
                    ParticleProjectileHitBlockEvent event = new ParticleProjectileHitBlockEvent(who, start_location, loc, hit, projectile);
                    Bukkit.getServer().getPluginManager().callEvent(event);
                    if (!event.isCancelled()) {
                        projectile.OnHitBlock(who, where, start, pos, hit, t);
                        return false;
                    }
                } else if (projectile.materials.contains(hit.getType())) {
                    ParticleProjectilePenetrateBlockEvent event = new ParticleProjectilePenetrateBlockEvent(who, start_location, loc, hit, projectile);
                    Bukkit.getServer().getPluginManager().callEvent(event);
                    if (!event.isCancelled()) {
                        projectile.OnPenetrateBlock(who, where, pos, hit, t);
                    }
                }
            }
            if (result.getHitEntity() != null) {
                Entity hit = result.getHitEntity();
                Vector pos = result.getHitPosition();
                Location loc = pos.toLocation(where);
                if (!projectile.entities.contains(hit) && !projectile.entityTypes.contains(hit.getType())) {
                    ParticleProjectileHitEntityEvent event = new ParticleProjectileHitEntityEvent(who, start_location, loc, hit, projectile);
                    Bukkit.getServer().getPluginManager().callEvent(event);
                    if (!event.isCancelled()) {
                        projectile.OnHitEntity(who, where, start, pos, hit, t);
                        return false;
                    }
                } else if (projectile.entities.contains(hit) || projectile.entityTypes.contains(hit.getType())) {
                    ParticleProjectilePenetrateEntityEvent event = new ParticleProjectilePenetrateEntityEvent(who, start_location, loc, hit, projectile);
                    Bukkit.getServer().getPluginManager().callEvent(event);
                    if (!event.isCancelled())
                        projectile.OnPenetrateEntity(who, where, pos, hit, t);
                }
            }
        }
        projectile.OnMove(where, previous.toVector(), lerp.toVector(), t);
        previous = lerp;
        t += step;
        return true;
    }

    private double lerp(double start, double end, double t) {
        return start + (end - start) * t;
    }

    private Vector lerp(Vector start, Vector end, double t) {
        return start.clone().add(end.clone().subtract(start).multiply(t));
    }
}
