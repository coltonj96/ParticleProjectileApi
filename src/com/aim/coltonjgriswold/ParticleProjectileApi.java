package com.aim.coltonjgriswold;

import org.bukkit.plugin.java.JavaPlugin;

import com.aim.coltonjgriswold.api.ParticleProjectiles;

public class ParticleProjectileApi extends JavaPlugin {

    private static ParticleProjectileApi plugin;

    public void onEnable() {
        plugin = this;
        Debugging.enable();
        ParticleProjectiles.getInstance();
    }

    public static ParticleProjectileApi instance() {
        return plugin;
    }
}
