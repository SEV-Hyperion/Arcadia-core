package com.hyperion.arcadia.entity.enemy;

import com.badlogic.gdx.math.Vector2;

public class Enemy0 extends Enemy {

	private static String path = "enemy0.png";

	public Enemy0(Vector2 pos, Vector2 direction) {

		super(pos, direction, path);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Enemy 0");
		return sb.toString();
	}

}
