package com.hyperion.arcadia.entity.enemy;

import com.badlogic.gdx.math.Vector2;

public class EnemyWarrior extends Enemy {

	private static int width = 32;
	private static int height = 32;
	private static float frameDuration = 0.5f;
	private String[] paths = { "enemyOnedown_1.png", "enemyOnedown_2.png" };

	public EnemyWarrior(Vector2 pos, Vector2 direction) {
		super(pos, direction);

		enemyAnimation = loadComplexAnimation(paths, width, height,
				frameDuration);
		currentFrame = enemyAnimation.getKeyFrame(stateTime, true);
		speed = 4;
	}

	@Override
	public void update() {
		super.update();
		currentFrame = enemyAnimation.getKeyFrame(stateTime, true);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Enemy Warrior");
		return sb.toString();
	}

//	@Override
//	protected void finalize() throws Throwable {
//		System.out.println("Enemy Warrior");
////		super.finalize();
//	}
}
