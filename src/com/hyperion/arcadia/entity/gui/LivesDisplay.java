package com.hyperion.arcadia.entity.gui;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.hyperion.arcadia.MainGame;
import com.hyperion.arcadia.camera.OrthoCamera;
import com.hyperion.arcadia.entity.Entity;
import com.hyperion.arcadia.entity.EntityManager;

public class LivesDisplay extends Entity {

	private EntityManager entityManager;

	private int lives;
	private OrthoCamera camera;
	private Animation livesAnimation;
	private static String path = "playerOneUp_1.png";
	private static int width = 12;
	private static int height = 25;
	private static int frameDuration = 1;
	private int MAX_LIVES;

	public LivesDisplay(Vector2 pos,
			EntityManager entityManager, OrthoCamera camera, int lives) {
		super(pos, null);
		this.setEntityManager(entityManager);
		this.camera = camera;

		livesAnimation = loadAnimation(path, width, height, frameDuration);

		currentFrame = livesAnimation.getKeyFrame(0, true);
		MAX_LIVES=lives;
		this.lives = lives;;
	}

	@Override
	public void update() {
//		System.out.println("Current lives: "+lives);
	}

	public int getLives() {
		return lives;
	}

	public LivesDisplay setLives(int li) {
		lives = li;
		return this;
	}

	@Override
	public void render(SpriteBatch sb) {
		for (int i = 0; i < MAX_LIVES; i++) {
			if (this.lives - 1 < i)
				sb.setColor(
						0.5f + (0.15f * (MAX_LIVES - (this.lives - 1))),
						0.0f, 0.0f, 0.5f);
			sb.draw(currentFrame, MainGame.WIDTH - (50 * (i + 1)), pos.y);
			sb.setColor(1.0f, 1.0f, 1.0f, 1.0f);
			camera.update();
		}

	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
}
