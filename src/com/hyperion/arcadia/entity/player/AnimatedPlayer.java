package com.hyperion.arcadia.entity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.Array;
import com.hyperion.arcadia.TextureManager;
import com.hyperion.arcadia.camera.OrthoCamera;
import com.hyperion.arcadia.entity.Entity;
import com.hyperion.arcadia.entity.EntityManager;
import com.hyperion.arcadia.entity.gui.CustomTouchpad;
import com.hyperion.arcadia.entity.projectiles.FireBall;
import com.hyperion.arcadia.entity.projectiles.Missile;

/**
 * Tengo ganas de ponerle soporte a mando de Xbox 360.
 * 
 * @author Daniel
 * 
 */
public class AnimatedPlayer extends Entity {

	private final int CHARACTER_SPEED = 250;
	private long lastFire;
	private static final Vector2 DIRECCION_DERECHA = new Vector2(0, 5);
	private static final Vector2 DIRECCION_IZQUIERDA = new Vector2(0, -5);
	private Vector2 direccionProyectil = DIRECCION_DERECHA;

	/**
	 * Conjunto de frames que forman la secuencia animada del personaje
	 * moviendose hacia "arriba"/"derecha".
	 */
	private Animation playerUpAnimation;

	private Animation playerDownAnimation;

	/**
	 * Tamaño del caragador
	 */
	private static int MAGAZINE_SIZE = 5;
	/**
	 * Municion actual
	 */
	private int currentMagazine = 5;

	/**
	 * Tiempo de recarga, en milisegundos
	 */
	private static int RELOAD_TIME = 2000;

	// animation frames data
	float frameDuration = 0.500f;// original 0,025f

	// touchpad items
	private Touchpad touchpad;
	private Button bt;
	private Sound hit;

	/**
	 * Constructor de AnimatedPlayer (personajes animado).
	 * 
	 * @param pos
	 *            Posicion original del personaje
	 * @param direction
	 *            Direccion en que se esta moviendo el personaje. (0, 0) ->
	 *            quieto
	 * @param entityManager
	 *            EntityManager que gestionará al personaje.
	 * @param camera
	 *            Cámara asociada al personaje
	 */
	public AnimatedPlayer(Vector2 pos, Vector2 direction,
			EntityManager entityManager, OrthoCamera camera) {
		super(pos, direction);

		Array<AtlasRegion> findRegions = TextureManager.instance.atlas
				.findRegions("playerOneUp");

		playerUpAnimation = new Animation(frameDuration, findRegions);

		Array<AtlasRegion> findRegions2 = TextureManager.instance.atlas
				.findRegions("playerOneDown");

		playerDownAnimation = new Animation(frameDuration, findRegions2);

		stateTime = 0f;

		currentFrame = playerUpAnimation.getKeyFrame(stateTime, true);

		hit = Gdx.audio.newSound(Gdx.files.internal("sounds/hit.mp3"));

		createTouchpad(camera);

	}

	/**
	 * @param camera
	 */
	private void createTouchpad(OrthoCamera camera) {
		touchpad = new CustomTouchpad();

		// TODO extract the button ala touchpad
		// bt = new CustomButton();
		bt = new Button();
		bt.setColor(Color.OLIVE);
		Skin btSkin = new Skin();
		String up = "up";
		String down = "down";
		btSkin.add(up, TextureManager.instance.atlas.findRegion("buttonUp")
				.getTexture());
		btSkin.add(down, TextureManager.instance.atlas.findRegion("buttonDown")
				.getTexture());
		ButtonStyle bs = new ButtonStyle(btSkin.getDrawable(up),
				btSkin.getDrawable(down), btSkin.getDrawable(up));
		bt.setPosition(250, 50);
		// bt.setSkin(btSkin);
		bt.setStyle(bs);
		bt.setSize(50f, 50f);

		EntityManager.em.addActor(touchpad);
		EntityManager.em.addActor(bt);
		Gdx.input.setInputProcessor(EntityManager.em);
	}

	// reload support
	boolean reloading = false;
	int timeToReload = 0;
	long initialTime = 0;

	@Override
	public void update() {

		pos.add(direction);

		setBoundedDirection();

		stateTime += Gdx.graphics.getDeltaTime(); // #15

		if (currentFrame == null) {
			currentFrame = playerUpAnimation.getKeyFrame(stateTime, true); // #16
		}

		if (touchpad.getKnobPercentY() < 0) {
			// face down
			currentFrame = playerDownAnimation.getKeyFrame(stateTime, true);
		}
		if (touchpad.getKnobPercentY() > 0) {
			// face up
			currentFrame = playerUpAnimation.getKeyFrame(stateTime, true);
		}

		if (reloading) {
			if (System.currentTimeMillis() < (initialTime + timeToReload)) {
				return;
			} else {
				reloading = false;
				currentMagazine = MAGAZINE_SIZE;
				return;
			}
		}

		setProyectileDireccion();

		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			// disparo principal.
			// lose ammo on fire.
			disparoPrincipal();

		} else if (Gdx.input.isKeyPressed(Keys.A) || bt.isPressed()) {
			// TODO rework touch to "disparo secundario".
			// lose ammo on fire.
			disparoSecundario();
		} else if (Gdx.input.isKeyPressed(Keys.R)) {
			// manual reload
			timeToReload = (RELOAD_TIME / MAGAZINE_SIZE) * currentMagazine;
			initialTime = System.currentTimeMillis();

			reloading = true;
		}
	}

	/**
	 * The player won't leave the map.
	 */
	private void setBoundedDirection() {

		// TODO we desperately need a way to lock the player inside the map.

		// float x = getPosition().x;
		float newX = touchpad.getKnobPercentX() * CHARACTER_SPEED;
		// if (x <= 0) {
		// newX = 1;
		// } else if (x >= MainGame.VIEWPORT_GUI_WIDTH) {
		// newX = - 1;
		// }

		// float y = getPosition().y;
		float newY = touchpad.getKnobPercentY() * CHARACTER_SPEED;
		// if (y <= 0){
		// newY = 1;
		// }else if (y >= MainGame.VIEWPORT_GUI_HEIGHT) {
		// newY = -1;
		// }
		setDirection(newX, newY);
	}

	private void disparoPrincipal() {
		if (System.currentTimeMillis() - lastFire >= 500 && currentMagazine > 0) {
			EntityManager.em.addEntity(new Missile(new Vector2(pos.x
					+ getWidth() / 4, pos.y), direccionProyectil));
			lastFire = System.currentTimeMillis();
			currentMagazine--;
		} else {
			// auto reload
			if (System.currentTimeMillis() - lastFire >= RELOAD_TIME
					&& currentMagazine == 0) {
				currentMagazine = MAGAZINE_SIZE;
			}
		}
	}

	private void disparoSecundario() {

		if (System.currentTimeMillis() - lastFire >= 500 && currentMagazine > 0) {
			System.out.println("Bola fuego lanzada");// TODO remove println
			EntityManager.em.addEntity(new FireBall(new Vector2(pos.x
					+ getWidth() / 4, pos.y), direccionProyectil));
			lastFire = System.currentTimeMillis();
			currentMagazine--;
		} else {
			// auto reload
			if (System.currentTimeMillis() - lastFire >= RELOAD_TIME
					&& currentMagazine == 0) {
				currentMagazine = MAGAZINE_SIZE;
			}
		}
	}

	private void setProyectileDireccion() {
		if (touchpad.getKnobPercentY() < 0) {
			direccionProyectil = DIRECCION_IZQUIERDA;
		}

		if (touchpad.getKnobPercentY() > 0) {
			direccionProyectil = DIRECCION_DERECHA;
		}
	}

	int counter = 0;

	@Override
	public void render(SpriteBatch sb) {
		sb.draw(currentFrame, pos.x, pos.y);

		touchpad.draw(sb, 1);
		bt.draw(sb, 1);
	}

	/**
	 * What to do when the player gets hit?
	 */
	public void onHit() {
		hit.play();
	}

}