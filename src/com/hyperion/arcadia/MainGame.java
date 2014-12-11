package com.hyperion.arcadia;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hyperion.arcadia.screen.MainMenuScreen;
import com.hyperion.arcadia.screen.ScreenManager;

public class MainGame implements ApplicationListener {

	private static SpriteBatch batch;

	// public static int WIDTH = 480, HEIGHT = 800;
	public static int WIDTH = 800, HEIGHT = 480;

	public static boolean debugMode = true;
	// =========================================================================
	public static boolean drawDebugOutline = true; // Texture atlas Management
	public static boolean rebuildAtlas = true; // Texture atlas Management
	// public static final String TEXTURE_ATLAS_OBJECTS =
	// "/images/aracadia_alpha.pack";
	// =========================================================================
	public static boolean isAndroid = false;

	// =========================================================================
	// GUI Management
	// public static final float VIEWPORT_GUI_WIDTH = 800.0f;
	// public static final float VIEWPORT_GUI_HEIGHT = 480.0f;

	public static final float VIEWPORT_GUI_WIDTH = 480.0f;
	public static final float VIEWPORT_GUI_HEIGHT = 800.0f;

	// =========================================================================
	// Lives Management
	// public static final int LIVES_START = 5;// TODO level dependent too
	// Enemies management
	// public static final int NUMBER_OF_ENEMIES = 10; // TODO remove later , it
	// is
	// level dependent

	// public int lives = 0; // level dependent

	// =========================================================================

	@Override
	public void create() {

		// =================================================
		// Texture Atlas Management
		TextureManager.instance.init(new AssetManager());
		// =================================================

		batch = new SpriteBatch();
		// ScreenManager.setScreen(new GameScreen());
//		ScreenManager.setScreen(new AbstractLevel("level_1_1"));// comenzamos
																// por la
																// primera
		 ScreenManager.setScreen(new MainMenuScreen());// comenzamos por el
		// menu principal

	}

	@Override
	public void dispose() {
		if (ScreenManager.getCurrentscreen() != null)
			ScreenManager.currentScreen.dispose();
		batch.dispose();

	}

	@Override
	public void render() {
		// Gdx.gl.glClearColor(0, 1, 0, 1);
		// Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// TODO double check != null ??
		if (ScreenManager.getCurrentscreen() != null)
			ScreenManager.getCurrentscreen().update();

		if (ScreenManager.getCurrentscreen() != null)
			ScreenManager.getCurrentscreen().render(batch);
		// batch.setProjectionMatrix(camera.combined);
		// batch.begin();
		// Nothing right now....
		// batch.end();
	}

	@Override
	public void resize(int width, int height) {
		if (ScreenManager.getCurrentscreen() != null)
			ScreenManager.getCurrentscreen().resize(width, height);
	}

	@Override
	public void pause() {

		if (ScreenManager.getCurrentscreen() != null)
			ScreenManager.getCurrentscreen().pause();
	}

	@Override
	public void resume() {

		if (ScreenManager.getCurrentscreen() != null)
			ScreenManager.getCurrentscreen().resume();
	}

	public static SpriteBatch getBatch() {
		return batch;
	}

	public static void setBatch(SpriteBatch batch) {
		MainGame.batch = batch;
	}
}
