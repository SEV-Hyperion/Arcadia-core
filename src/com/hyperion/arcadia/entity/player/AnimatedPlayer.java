package com.hyperion.arcadia.entity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
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
	private static final Vector2 DIRECCION_DERECHA = new Vector2(5, 0);
	private static final Vector2 DIRECCION_IZQUIERDA = new Vector2(-5, 0);
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
	private static int RELOAD_TIME = 1000;

	// animation frames data
	float frameDuration = 0.500f;// original 0,025f

	// touchpad items
	private Touchpad touchpad;
	private Button bt;
	private Sound hit;
	private Button bt2;

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
		bt = new Button();
		Skin btSkin = new Skin();
		TextureAtlas atlas = new TextureAtlas();
		atlas.addRegion("botonFuego",
				TextureManager.instance.atlas.findRegion("botonFuego"));
		atlas.addRegion("botonRayo",
				TextureManager.instance.atlas.findRegion("botonRayo"));

		btSkin.addRegions(atlas);
		ButtonStyle bs = new ButtonStyle(btSkin.getDrawable("botonFuego"),
				btSkin.getDrawable("botonFuego"),
				btSkin.getDrawable("botonFuego"));
		bt.setPosition(250, 50);
		bt.setStyle(bs);
		bt.setSize(66f, 32f);
		bt.addListener(new EventListener() {
			
			@Override
			public boolean handle(Event arg0) {
				if (bt.isPressed()){
					disparoSecundario();
				}
				return true;
			}
		});

		// second button
		// TODO remove duplicities here
		bt2 = new Button();
		ButtonStyle bs2 = new ButtonStyle(btSkin.getDrawable("botonRayo"),
				btSkin.getDrawable("botonRayo"),
				btSkin.getDrawable("botonRayo"));
		bt2.setPosition(350, 50);
		bt2.setStyle(bs2);
		bt2.setSize(39f, 29f);
		bt2.addListener(new EventListener() {
			
			@Override
			public boolean handle(Event arg0) {
				if (bt2.isPressed()){
					disparoPrincipal();
				}
				return true;
			}
		});

		EntityManager.em.addActor(touchpad);
		EntityManager.em.addActor(bt);
		EntityManager.em.addActor(bt2);
		Gdx.input.setInputProcessor(EntityManager.em);
	}

	// reload support
//	boolean reloading = false;
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
			// face down/¿left?
			currentFrame = playerDownAnimation.getKeyFrame(stateTime, true);
		}
		if (touchpad.getKnobPercentY() > 0) {
			// face up
			currentFrame = playerUpAnimation.getKeyFrame(stateTime, true);
		}

		// TODO replace by events for respecting buttons
		setProyectileDireccion();

		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			disparoPrincipal();

		} else if (Gdx.input.isKeyPressed(Keys.A)) {
			disparoSecundario();
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
		if (System.currentTimeMillis() - lastFire >= 200 && currentMagazine > 0) {
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

		if (System.currentTimeMillis() - lastFire >= 200 && currentMagazine > 0) {
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
		// el unico cambio aqui es Y por X. ahora se dispara a derecha e
		// izquierda.(Más acorde al diseño del juego)
		if (touchpad.getKnobPercentX() < 0) {
			direccionProyectil = DIRECCION_IZQUIERDA;
		}

		if (touchpad.getKnobPercentX() > 0) {
			direccionProyectil = DIRECCION_DERECHA;
		}
	}

	int counter = 0;

	@Override
	public void render(SpriteBatch sb) {
		sb.draw(currentFrame, pos.x, pos.y);

		touchpad.draw(sb, 0.5f);
		bt.draw(sb, 1);
		bt2.draw(sb, 1);
	}

	/**
	 * What to do when the player gets hit?
	 */
	public void onHit() {
		hit.play();
	}

}