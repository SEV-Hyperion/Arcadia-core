package com.hyperion.arcadia.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hyperion.arcadia.MainGame;
import com.hyperion.arcadia.TextureManager;
import com.hyperion.arcadia.camera.OrthoCamera;
import com.hyperion.arcadia.entity.Entity;
import com.hyperion.arcadia.entity.EntityManager;
import com.hyperion.arcadia.entity.enemy.Enemy0;
import com.hyperion.arcadia.entity.enemy.Enemy1;
import com.hyperion.arcadia.entity.enemy.Enemy2;
import com.hyperion.arcadia.entity.enemy.Enemy3;
import com.hyperion.arcadia.entity.enemy.EnemyWarrior;
import com.hyperion.arcadia.entity.enemy.EnemyWarriorStanding;
import com.hyperion.arcadia.entity.gui.LivesDisplay;
import com.hyperion.arcadia.entity.player.AnimatedPlayer;

/*
 * Old imports
 */
//import javax.json.Json;
//import javax.json.JsonArray;
//import javax.json.JsonObject;
//import javax.json.JsonReader;
//import com.badlogic.gdx.assets.AssetManager;

public class AbstractLevel extends Screen {
	public int width;
	public int height;

	private OrthoCamera camera;
	public Sound gameLoopSound;
	private static EntityManager entityManager = null;

	private String level = "level_1_1";
	private String map = "map_level1.1";
	private String sound = "sounds/gameloopsound.mp3";
	private String title = "Tutorial";
	private String nextLevel = "level_1_2";
	private Sprite mapSprite;
	private int lives;

	/**
	 * 
	 * @param level
	 */
	public AbstractLevel(String level) {
		this.level = level;
	}

	@Override
	public void create() {
		camera = new OrthoCamera();

		camera.setPosition(0.0f, 0.0f);

		Viewport vp = new FitViewport(MainGame.VIEWPORT_GUI_HEIGHT,
				MainGame.VIEWPORT_GUI_WIDTH);
		entityManager = new EntityManager(vp, MainGame.getBatch());
		entityManager.setCamera(camera);
		loadLevelInfo();
		Gdx.graphics.setTitle(title);// TODO remove if necessary
		// entityManager=new EntityManager(10, camera);

		entityManager.setNextLevel(nextLevel);
		gameLoopSound = Gdx.audio.newSound(Gdx.files.internal(sound));
		gameLoopSound.loop();

		/**
		 * map support testing
		 **/
		TextureRegion region = TextureManager.instance.atlas.findRegion(map);
		mapSprite = new Sprite(region);
		mapSprite.setSize(MainGame.VIEWPORT_GUI_HEIGHT,
				MainGame.VIEWPORT_GUI_WIDTH);
		if (MainGame.isAndroid) {
			System.out.println(Gdx.graphics.getDesktopDisplayMode());
		} else {
			// mapSprite.rotate90(false);
		}

		// mapSprite.setPosition(0, -MainGame.VIEWPORT_GUI_WIDTH / 2);
		// mapSprite.setOrigin(MainGame.VIEWPORT_GUI_HEIGHT / 2,
		// MainGame.VIEWPORT_GUI_WIDTH / 2);
		mapSprite.setPosition(0, 0);
		mapSprite.setOrigin(0, 0);

		height = mapSprite.getRegionHeight();
		width = mapSprite.getRegionWidth();

		camera.updateViewport();
	}

	/**
	 * Create the required entities for current level here
	 * 
	 * @return entities
	 */
	private void loadLevelInfo() {

		JsonReader reader = new JsonReader(); // desktop works right

		JsonValue a = reader.parse(Gdx.files
				.internal("maps/" + level + ".json").read());
		title = a.getString("title");
		map = a.getString("map");
		// sound = a.getString("sound");
		/*
		 * TODO add proper sounds to the game. Currently, the ones i like are
		 * too big for the buffer on android devices.
		 */
		nextLevel = a.getString("next_level");
		JsonValue entities = a.get("entities");
		for (int i = 0; i < entities.size; i++) {
			JsonValue o = entities.get(i);
			int type = o.getInt("type");
			JsonValue pos = o.get("pos");
			int pos_x = pos.getInt("x");
			int pos_y = pos.getInt("y");
			JsonValue dir = o.get("dir");
			int dir_x = dir.getInt("x");
			int dir_y = dir.getInt("y");
			if (type == -1) {
				lives = o.getInt("lives");
			}
			entityManager.addActor(loadEntity(type, new Vector2(pos_x, pos_y),
					new Vector2(dir_x, dir_y), lives));
		}
	}

	/**
	 * 
	 * @param type
	 *            positive: enemies; negative: players and stuff.
	 * @param pos
	 * @param direction
	 * @return
	 */
	private Entity loadEntity(int type, Vector2 pos, Vector2 direction,
			Object extra) {
		Entity en = null;
		System.out.println("Creating enemy type: " + type);
		switch (type) {
		case 0:
			en = new Enemy0(pos, direction);
			break;
		case 1:
			en = new Enemy1(pos, direction);
			break;
		case 2:
			en = new Enemy2(pos, direction);
			break;
		case 3:
			en = new Enemy3(pos, direction);
			break;
		case 4:
			en = new EnemyWarrior(pos, direction);
			break;
		case 5:
			en = new EnemyWarriorStanding(pos, direction);
			break;
		case -1:
			en = new AnimatedPlayer(pos, direction, entityManager, camera);
			break;
		case -100:
			en = new LivesDisplay(new Vector2(MainGame.WIDTH / 2,
					MainGame.HEIGHT
							- (TextureManager.instance.atlas
									.findRegion("playerOneUp")
									.getRegionHeight())), entityManager,
					camera, lives);
			break;
		}
		return en;
	}

	@Override
	public void update() {
		camera.update();
		entityManager.act();

	}

	@Override
	public void render(SpriteBatch sb) {

		// TODO this should be flexible.
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		sb.setProjectionMatrix(camera.combined);
		sb.enableBlending(); // Enable blending in the game screen
		sb.begin();
		mapSprite.draw(sb);
		entityManager.render(sb);
		sb.end();

	}

	@Override
	public void resize(int width, int height) {
		camera.resize();
		// my guesses
		camera.updateViewport();
		camera.update();

	}

	@Override
	public void dispose() {
		gameLoopSound.dispose();
		this.camera = null;
		entityManager.dispose();
	}

	@Override
	public void pause() {
		gameLoopSound.stop();
	}

	@Override
	public void resume() {
		gameLoopSound.loop();
	}

//	public static EntityManager getEntityManager() {
//		return entityManager;
//	}

}
