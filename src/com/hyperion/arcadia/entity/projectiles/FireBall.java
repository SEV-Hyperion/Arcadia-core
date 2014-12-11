package com.hyperion.arcadia.entity.projectiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public class FireBall extends Projectile {
	private static String path = "fireball.png";
	private static int width = 12;
	private static int height = 14;
	private static int frameDuration = 1;

	/**
	 * 
	 * @param pos la direccion de movimiento del proyectil
	 */
	public FireBall(Vector2 pos, Vector2 direccion) {
		super(pos, direccion);
		animation = loadAnimation(path, width, height, frameDuration);
		currentFrame = animation.getKeyFrame(0, true);
		throwProjectile = Gdx.audio.newSound(Gdx.files.internal("sounds/gun_fire.ogg"));
	}

}
