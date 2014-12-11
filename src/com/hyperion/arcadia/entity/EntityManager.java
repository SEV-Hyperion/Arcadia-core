package com.hyperion.arcadia.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hyperion.arcadia.MainGame;
import com.hyperion.arcadia.camera.OrthoCamera;
import com.hyperion.arcadia.entity.enemy.Enemy;
import com.hyperion.arcadia.entity.gui.LivesDisplay;
import com.hyperion.arcadia.entity.player.AnimatedPlayer;
import com.hyperion.arcadia.entity.projectiles.Projectile;
import com.hyperion.arcadia.screen.GameOverScreen;
import com.hyperion.arcadia.screen.ScreenManager;

/**
 * Clase encargada de gestionar las entidades usadas por el programa. Las crea,
 * actualiza, pinta y destruye. <br>
 * Almacena a las Entity, que son Actores con nuestras reglas.<br>
 * ¿Deberia ser Singleton? <strong>SI. CADA VEZ QUE ME PONGO A USAR ESTA CLASE
 * DESDE FUERA ME QUEDA MÁS CLARO.</strong>
 * 
 * @author Daniel
 * 
 */

public class EntityManager extends Stage {

	AnimatedPlayer animatedPlayer;
	LivesDisplay livesDisplay;

	public static EntityManager em;

	private String nextLevel;
	private OrthoCamera camera;

	public EntityManager(Viewport vp, Batch batch) {
		super(vp, batch);
		em = this;
	}

	public EntityManager(Entity[] levelEntities, OrthoCamera camera) {
		em = this;
		this.camera = camera;

		for (Entity ent : levelEntities) {
			addActor(ent);
		}
	}

	/**
	 * Actualiza todas la entidades gestionadas por ésta. <br>
	 * Llama a checkCollisions() para manejar las colisiones entre entidades.
	 */
	public void update() {
		checkCollisions();
		checkOutofBoundsProjectiles();
	}

	private void checkOutofBoundsProjectiles() {
		for (Projectile p : getProjectiles())
			if (p.checkEnd()) {
				getActors().removeValue(p, false);
				System.out.println("Proyectil destruido");
			}
	}

	/**
	 * Dibuja todas las entidades.<br>
	 * También elimina los misiles si se han salido de la pantalla.
	 * 
	 * @param sb
	 */
	public void render(SpriteBatch sb) {
		for (Actor actor : getActors()) {
			// if(!(actor instanceof LivesDisplay)){
			actor.draw(sb, 0);
			sb.setColor(1.0f, 1.0f, 1.0f, 1.0f);
			camera.update();
			// }
		}
//		 draw();
	}
	@Override
	public void draw() {
		
		super.draw();
		render(MainGame.getBatch());
	}

	/**
	 * Comprueba las colisiones entre entidades gestionadas por la
	 * EntityManager.<br>
	 * Destruye los misiles al impactar junto con los enemigos. Quita vidas al
	 * chocar y llama al GameOver si es necesario.
	 */
	private void checkCollisions() {
		// TODO this should go to some game-logic controlling class.
		// Why? Because there aren't going to be always Enemy and Projectile on Screen. Example: the MainMenuScreen.
		// Anyway, it works as is now.
		for (Enemy e : getEnemies()) {
			for (Projectile m : getProjectiles()) {
				if (e.getBounds().overlaps(m.getBounds())) {

					/*
					 * TODO the name of this method shows the need for events
					 * and listeners
					 */
					e.onHit(m);

					/*
					 * TODO Currently, this doesn't allow the enemies to have
					 * "life".
					 * TODO Currently, this doesn't allow the
					 * projectiles to have "life" nor "pierce".
					 */
					getActors().removeValue(e, false);
					getActors().removeValue(m, false); // destroy missile on hit

					/*
					 * TODO replace the static score with another thing :S maybe
					 * not even replace, just delete
					 */
					// MainGame.score += 1;

					if (getEnemies().size <= 0) {
						System.out.println("Won with "
								+ getLivesDisplay().getLives() + " lives");
						System.out.println("Enemies remaining: "
								+ getEnemies().size);
						// end, Game Won
						ScreenManager.setScreen(new GameOverScreen(true,
								getNextLevel()));
						/*
						 * if won, show winning screen and send to NEXT_LEVEL
						 */
						return;
					}
				}
			}

			if (e.getBounds().overlaps(getAnimatedPlayer().getBounds())) {
				// Collision with enemy. Decrement Live Counter
				System.out.println("Hit by " + e);

				// TODO change the fixed distance to some random one
				e.pos = new Vector2(800, 800);

				System.out.println("You have " + getLivesDisplay().getLives()
						+ " lives");
				getAnimatedPlayer().onHit();
				reduceLives();// TODO lives should depend on the player, not the
								// EntityManager.

				if (getLivesDisplay().getLives() == 0) {
					System.out.println("Lost with "
							+ getLivesDisplay().getLives() + " lives");
					System.out.println("Enemies remaining: "
							+ getEnemies().size);
					// If we ran out of lives, game is over, lost
					ScreenManager.setScreen(new GameOverScreen(false,
							getNextLevel()));
					return;
				}
			}
		}
	}

	/**
	 * Busca el LivesDisplay actual y le reduce una vida.
	 */
	private void reduceLives() {
		for (Entity e : getEntities()) {
			if (e instanceof LivesDisplay) {
				((LivesDisplay) e).setLives(((LivesDisplay) e).getLives() - 1);
				return;
			}
		}
	}

	/**
	 * Devuelve un array con todas la entidades que sean Enemigos.
	 * 
	 * @return Array de Enemy
	 */
	public Array<Enemy> getEnemies() {
		Array<Enemy> ret = new Array<Enemy>();
		for (Entity e : getEntities()) {
			if (e instanceof Enemy)
				ret.add((Enemy) e);
		}
		return ret;
	}

	/**
	 * Devuelve un array con todos los proyectiles
	 * 
	 * @return Array de Projectile
	 */
	private Array<Projectile> getProjectiles() {
		Array<Projectile> ret = new Array<Projectile>();
		for (Entity m : getEntities()) {
			if (m instanceof Projectile)
				ret.add((Projectile) m);
		}
		return ret;
	}

	/**
	 * Comprueba las condiciones de fin de partida.
	 * 
	 * @return True si se dan las condiciones de GAMEOVER. FALSE en cualquier
	 *         otro caso.
	 */
	public boolean gameOver() {
		return ((getEnemies().size <= 0) || (getLivesDisplay().getLives() == 0));
	}

	/**
	 * Añade una entidad al EntityManager
	 * 
	 * @param e
	 *            Entidad que va a ser gestionada por el {@link EntityManager}
	 */
	public void addEntity(Entity e) {
		addActor(e);
	}

	/**
	 * Devuelve el jugador gestionado por el {@link EntityManager}
	 * 
	 * @return
	 */
	public AnimatedPlayer getAnimatedPlayer() {
		if (animatedPlayer != null) {
			return animatedPlayer;
		}
		for (Entity en : getEntities()) {
			if (en instanceof AnimatedPlayer) {
				animatedPlayer = (AnimatedPlayer) en;
			}
		}
		return animatedPlayer;
	}

	private LivesDisplay getLivesDisplay() {
		if (livesDisplay != null) {
			return livesDisplay;
		}
		for (Entity en : getEntities()) {
			if (en instanceof LivesDisplay) {
				livesDisplay = (LivesDisplay) en;
			}
		}
		return livesDisplay;
	}

	@Override
	public void act() {
		super.act();
		update();
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		update();
	}

	public Array<Entity> getEntities() {
		Array<Entity> entities = new Array<Entity>();
		for (Actor actor : getActors()) {
			if (actor instanceof Entity) {
				entities.add((Entity) actor);
			}
		}
		return entities;
	}

	public void setCamera(OrthoCamera camera2) {
		this.camera = camera2;

	}

	public String getNextLevel() {
		return nextLevel;
	}

	public void setNextLevel(String nextLevel) {
		this.nextLevel = nextLevel;
	}

	// GUI Management
	/**
	 * This needs to be somewhere else or be deleted. We aren't writing anything
	 * on screen right now
	 * 
	 * @author Daniel
	 * 
	 */
	public class AssetFonts {

		public final BitmapFont font;

		public AssetFonts() {
			font = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"),
					false);

			font.setScale(1.0f);
			font.getRegion().getTexture()
					.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
	}
}
