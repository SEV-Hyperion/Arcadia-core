package com.hyperion.arcadia.entity.enemy;

import com.badlogic.gdx.math.Vector2;

public class Enemy3 extends Enemy {
	private static String path = "enemy3.png";

	public Enemy3(Vector2 pos, Vector2 direction) {

		super(pos, direction, path);
		speed = 3;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Enemy 3");
		return sb.toString();}

}
