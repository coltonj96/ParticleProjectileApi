package com.aim.coltonjgriswold.api;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public interface IParticleProjectile {
    
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
    default void OnLaunch(LivingEntity who, World world, Vector start) {}

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
    default void OnHit(LivingEntity who, World world, Vector start, Vector end, double t) {}

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
    default void OnHitBlock(LivingEntity who, World world, Vector start, Vector end, Block block, double t) {}

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
    default void OnHitEntity(LivingEntity who, World world, Vector start, Vector end, Entity entity, double t) {}

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
    default void OnMove(World world, Vector previous, Vector current, double t) {}

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
    default void OnPenetrateBlock(LivingEntity who, World world, Vector where, Block block, double t) {}

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
    default void OnPenetrateEntity(LivingEntity who, World world, Vector where, Entity entity, double t) {}
}
