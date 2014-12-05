package com.hyperion.arcadia.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hyperion.arcadia.MainGame;
import com.hyperion.arcadia.camera.OrthoCamera;
import com.hyperion.arcadia.entity.EntityManager;

public class GameOverScreen extends Screen {
	private OrthoCamera camera;
	// private Texture texture;
	private TextureRegion texture;
	private boolean won;
	private String nextLevel;

	public GameOverScreen(boolean won, String nextLevel) {
		this.won = won;
		this.nextLevel=nextLevel;
		if (won) {
			texture = EntityManager.em.getAnimatedPlayer().getCurrentFrame();

		} else {
			texture = EntityManager.em.getAnimatedPlayer().getCurrentFrame();
			// TODO use texture from the killing enemy or the round boss or
			// anything?

		}
		// MainGame.score = 0;
	}

	@Override
	public void create() {
		camera = new OrthoCamera();
		camera.resize();

	}

	@Override
	public void update() {

		camera.update();

		if (Gdx.input.isKeyPressed(Keys.R)) {
			ScreenManager.setScreen(new AbstractLevel("level_1_1"));
		}
		else if(Gdx.input.isKeyPressed(Keys.N)){
			if(nextLevel!="" && !nextLevel.isEmpty())
				ScreenManager.setScreen(new AbstractLevel(nextLevel));
			else{
				//de momento nada
			}
		}
	}

	@Override
	public void render(SpriteBatch sb) {

		if (won) {
			Gdx.gl.glClearColor(0, 1, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		} else {
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		}

		sb.setProjectionMatrix(camera.combined);
		sb.begin();
		sb.draw(texture, MainGame.WIDTH / 2, MainGame.HEIGHT / 2);
		sb.end();
	}

	@Override
	public void resize(int width, int height) {

		camera.resize();
	}

	@Override
	public void dispose() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

}
