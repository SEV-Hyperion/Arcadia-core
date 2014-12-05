package com.hyperion.arcadia.entity.enemy;

import com.badlogic.gdx.math.Vector2;

public class Enemy2 extends Enemy {
	private static String path = "enemy2.png";

	public Enemy2(Vector2 pos, Vector2 direction) {

		super(pos, direction, path);
		speed = 2;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Enemy 2");
		return sb.toString();
	}

}
