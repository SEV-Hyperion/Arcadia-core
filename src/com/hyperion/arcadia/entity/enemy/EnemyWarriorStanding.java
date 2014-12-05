package com.hyperion.arcadia.entity.enemy;

import com.badlogic.gdx.math.Vector2;

public class EnemyWarriorStanding extends EnemyWarrior {

	public EnemyWarriorStanding(Vector2 pos, Vector2 direction) {
		super(pos, direction);
	}

	@Override
	public void update() {
		// stand still
	}
}
