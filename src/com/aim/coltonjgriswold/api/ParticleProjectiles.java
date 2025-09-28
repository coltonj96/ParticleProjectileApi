package com.aim.coltonjgriswold.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;

import com.aim.coltonjgriswold.ParticleProjectileApi;

/**
 * Class containing functions and methods that control ParticleProjectiles task
 */
public class ParticleProjectiles extends BukkitRunnable {

    private static ParticleProjectiles instance;

    private static final List<LaunchedProjectileData> projectiles = new ArrayList<>();

    private ParticleProjectiles() {
        start();
    }

    /**
     * Utility method for internal use
     *
     * @return ParticleProjectiles
     */
    public static ParticleProjectiles getInstance() {
        if (instance == null)
            instance = new ParticleProjectiles();
        return instance;
    }

    /**
     * Adds and launches  multiple particle projectiles
     *
     * @param data An array containing data
     */
    public synchronized static void launchMultiple(LaunchedProjectileData[] data) {
        projectiles.addAll(Arrays.asList(data));
    }

    /**
     * Adds and launches multiple particle projectiles
     *
     * @param data A list containing data
     */
    public synchronized static void launchMultiple(List<LaunchedProjectileData> data) {
        projectiles.addAll(data);
    }

    /**
     * Adds and launches a single particle projectile
     *
     * @param data The data
     */
    public synchronized static void launchSingle(LaunchedProjectileData data) {
        projectiles.add(data);
    }

    /**
     * Gets whether ParticleProjectiles is running or not
     *
     * @return True if ParticleProjectiles is running
     */
    public boolean isRunning() {
        return instance != null && !isCancelled();
    }

    /**
     * Starts ParticleProjectiles task
     */
    public void start() {
        if (!isRunning())
            runTaskTimer(ParticleProjectileApi.instance(), 0, 0L);
    }

    /**
     * Stops ParticleProjectiles task
     */
    public void stop() {
        if (isRunning())
            cancel();
    }

    /**
     * Utility method for internal use
     */
    @Override
    public void run() {
        synchronized (this) {
            if (!projectiles.isEmpty())
                projectiles.removeIf(data -> !data.update());
        }
    }
}
