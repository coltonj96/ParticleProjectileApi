package com.aim.coltonjgriswold.projectiles;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.aim.coltonjgriswold.api.ParticleProjectile;

public class ExplosiveProjectile extends ParticleProjectile {

    @Override
    public void Hit(Player who, Location where) {
	where.getWorld().createExplosion(where, 4f);
    }

    @Override
    public void HitBlock(Player who, Location where, Block what) {
	where.getWorld().createExplosion(where, 4f);
    }

    @Override
    public void HitEntity(Player who, Location where, Entity what) {
	where.getWorld().createExplosion(where, 4f);
	who.sendMessage(what.getType().name());
    }

}
