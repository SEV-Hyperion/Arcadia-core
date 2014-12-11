package com.hyperion.arcadia.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hyperion.arcadia.MainGame;
import com.hyperion.arcadia.camera.OrthoCamera;
import com.hyperion.arcadia.entity.EntityManager;

/**
 * This was planned to be a generic screen and ended being nothing. AbstractLevel took over.
 * @author Daniel
 *
 */
public class GameScreen extends Screen {

	private OrthoCamera camera;
	private OrthographicCamera cameraGUI;
	// private OrthographicCamera cameraFPS;

	public Sound gameLoopSound;

	private static EntityManager entityManager = null;

	@Override
	public void create() {
		 System.out.println("Created");
		camera = new OrthoCamera();
//		entityManager = new EntityManager(MainGame.NUMBER_OF_ENEMIES, camera);
		gameLoopSound = Gdx.audio.newSound(Gdx.files
		// .internal("sounds/gameloopsound.mp3"));
				.internal("sounds/l2outro.ogg"));// changed to suit my taste
													// while coding. ~Dani
		gameLoopSound.loop();

		cameraGUI = new OrthographicCamera(MainGame.VIEWPORT_GUI_WIDTH,
				MainGame.VIEWPORT_GUI_HEIGHT);
		cameraGUI.position.set(0, 0, 0);
		cameraGUI.setToOrtho(true);
		cameraGUI.update();

	}

	@Override
	public void update() {

		camera.update();
		cameraGUI.update();
		entityManager.update();
	}

	@Override
	public void render(SpriteBatch sb) {
		// System.out.println("Render");
		sb.setProjectionMatrix(camera.combined);
		sb.enableBlending(); // Enable blending in the game screen
		sb.begin();
		entityManager.render(sb);
		sb.end();
	}

	@Override
	public void resize(int width, int height) {
		// System.out.println("Resized");
		camera.resize();

		cameraGUI.viewportHeight = MainGame.VIEWPORT_GUI_HEIGHT;
		cameraGUI.viewportWidth = MainGame.VIEWPORT_GUI_HEIGHT / (float) height
				* (float) width;
		cameraGUI.position.set(cameraGUI.viewportWidth / 2,
				cameraGUI.viewportHeight / 2, 0);
		cameraGUI.update();

	}

	@Override
	public void dispose() {
		System.out.println("Disposed");
		gameLoopSound.dispose();

	}

	@Override
	public void pause() {
		System.out.println("Paused");
		gameLoopSound.stop();

	}

	@Override
	public void resume() {
		System.out.println("Resumed");

		gameLoopSound.loop();
	}

	//

	public static EntityManager getEntityManager() {
		return entityManager;
	}

}
