package com.aim.coltonjgriswold;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.aim.coltonjgriswold.api.utilities.ParticleColor;
import com.aim.coltonjgriswold.projectiles.ExplosiveProjectile;
import com.aim.coltonjgriswold.projectiles.NormalProjectile;

public class ExamplePlugin extends JavaPlugin implements Listener {

    NormalProjectile normal;
    ExplosiveProjectile explosive;
    
    public void onEnable() {
	getServer().getPluginManager().registerEvents(this, this);
	normal = new NormalProjectile();
	explosive = new ExplosiveProjectile();
	normal.ignoreMaterial(Material.STATIONARY_WATER);
	explosive.ignoreMaterial(Material.STATIONARY_WATER);
    }

    public void onDisable() {

    }
    
    @EventHandler
    public void oninteract(PlayerInteractEvent event) {
	Material item = event.getPlayer().getInventory().getItemInMainHand().getType();
	if (item == Material.STICK) {
	    for (int i = 0; i < 8; i++) {
		normal.launch(event.getPlayer(), Vector.getRandom().subtract(new Vector(0.5, 0.5, 0.5)).multiply(16), 1.0, 32.0, ParticleColor.fromRGB(255, 255, 255));
	    }
	}
	if (item == Material.BLAZE_ROD) {
	    explosive.launch(event.getPlayer(), 0.5, 128.0, ParticleColor.fromRGB(255, 127, 0));
	}
    }
}
