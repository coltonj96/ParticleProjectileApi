package com.aim.coltonjgriswold;

import org.bukkit.plugin.java.JavaPlugin;

public class ParticleProjectileApi extends JavaPlugin {
    
    private static ParticleProjectileApi plugin;

    public void onEnable() {
	plugin = this;
    }
    
    public static ParticleProjectileApi instance() {
	return plugin;
    }
}
