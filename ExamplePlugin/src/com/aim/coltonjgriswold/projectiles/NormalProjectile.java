package com.aim.coltonjgriswold.projectiles;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.aim.coltonjgriswold.api.ParticleProjectile;

public class NormalProjectile extends ParticleProjectile {

    @Override
    public void Hit(Player who, Location where) {
	
    }

    @Override
    public void HitBlock(Player who, Location where, Block what) {
	
    }

    @Override
    public void HitEntity(Player who, Location where, Entity what) {
	if (what instanceof Damageable)
	    ((Damageable) what).damage(1.0, who);
    }
}
