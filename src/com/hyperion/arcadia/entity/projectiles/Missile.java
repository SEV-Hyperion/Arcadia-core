package com.hyperion.arcadia.entity.projectiles;

import com.badlogic.gdx.math.Vector2;

public class Missile extends Projectile {

	private static String path = "missile.png";
	private static int width = 12;
	private static int height = 25;
	private static int frameDuration = 1;

	public Missile(Vector2 pos,  Vector2 direccion) {
		super(pos, direccion);

		animation = loadAnimation(path, width, height, frameDuration);
		currentFrame = animation.getKeyFrame(0, true);
	}

}
