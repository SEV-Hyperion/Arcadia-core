package com.hyperion.arcadia.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Clase abstracta que recoge las características básicas de una entidad
 * (jugador, enemigo, obstáculo, etc.).
 * 
 * @author Daniel
 *
 */
public abstract class Entity extends Actor{

	/**
	 * Posición absoluta de la entidad en el mapa
	 */
	protected Vector2 pos;
	/**
	 * Dirección en que se mueve la entidad
	 */
	protected Vector2 direction;

	/**
	 * Frame actual del personaje
	 */
	public TextureRegion currentFrame;
	/**
	 * Indica el tiempo subjetivo del estado del personaje. Sirve para saber que
	 * frame pintar en ese momento,
	 */
	protected float stateTime;

	/**
	 * Constructor de entidades con tipo predeterminado = -1.
	 * 
	 * @param pos
	 *            Posición inicial de la entidad
	 * @param direction
	 *            Direccion en que se está moviendo la entidad
	 */
	public Entity(Vector2 pos, Vector2 direction) {
		this.pos = pos;
		this.direction = direction;
	}

	/**
	 * Actualiza el estado de la entidad
	 */
	public abstract void update();

	
	@Override
	public void act(float delta) {
		super.act(delta);
		update();
	}
	
	
	/**
	 * Dibuja la entidad
	 * 
	 * @param sb
	 *            SpriteBatch que se encarga de dibujar la entidad.
	 */
	public void render(SpriteBatch sb) {
		sb.draw(currentFrame, pos.x, pos.y);
	}

	/**
	 * Devuelve el vector de posicion de la entidad. Es bidimensional (x, y).
	 * 
	 * @return Vector que indica la posicion de la entidad
	 */
	public Vector2 getPosition() {
		return pos;
	}

	/**
	 * Cambia la dirección de la entidad, multiplicada por los segundos pasados
	 * con el frame anterior
	 * 
	 * @param x
	 *            Coordenada x
	 * @param y
	 *            Coordenada y
	 */
	public void setDirection(float x, float y) {
		direction.set(x, y);
		direction.scl(Gdx.graphics.getDeltaTime());

	}

	/**
	 * Devuelve un rectángulo cuyas dimensiones se ajustan al tamaño de la
	 * entidad, en la posicion de dicha entidad.
	 * 
	 * @return Rectangulo con las dimensiones de la entidad y su misma posición.
	 */
	public Rectangle getBounds() {
		if (currentFrame == null) {
			System.out.println("NULL FRAME");
		}
		return new Rectangle(pos.x, pos.y, currentFrame.getRegionWidth(),
				currentFrame.getRegionHeight());
	}

	/**
	 * Devuelve el frame actual
	 * 
	 * @return El frame actual
	 */
	public TextureRegion getCurrentFrame() {
		return currentFrame;
	}

	/**
	 * Carga una animación dados los parámetros. No tiene control de errores así
	 * que sed buenos!
	 * 
	 * @param path
	 *            Nombre del archivo que contiene la animacion, interno a la
	 *            aplicacion
	 * @param width
	 *            Ancho de la animacion
	 * @param height
	 *            Alto de la anumacion
	 * @param frameDuration
	 *            Duracion de cada frame de la animacion
	 * @return Una Animacion cargada segun los parametros de entrada.
	 */
	public Animation loadAnimation(String path, int width, int height,
			float frameDuration) {
		TextureRegion[] frames = new TextureRegion[2];
		Texture sheet = new Texture(Gdx.files.internal(path));
		TextureRegion[][] tmp = TextureRegion.split(sheet, width, height);
		int index = 0;
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < 1; j++) {
				frames[index++] = tmp[i][j];
			}
		}
		return new Animation(frameDuration, frames);
	}

	/**
	 * Devuelve una animacion compuesta por varios png.
	 * 
	 * @param paths
	 *            Array con los nombres de los archivos que se usaran como
	 *            animacion
	 * @param width
	 *            Ancho de la animacion
	 * @param height
	 *            Alto de la anumacion
	 * @param frameDuration
	 *            Duracion de cada frame de la animacion
	 * @return Una Animacion cargada segun los parametros de entrada.
	 */
	public Animation loadComplexAnimation(String[] paths, int width,
			int height, float frameDuration) {
		TextureRegion[] frames = new TextureRegion[paths.length];
		int index = 0;
		for (String path : paths) {
			Texture sheet = new Texture(Gdx.files.internal(path));
			TextureRegion[][] tmp = TextureRegion.split(sheet, width, height);
			for (int i = 0; i < 1; i++) {
				for (int j = 0; j < 1; j++) {
					frames[index++] = tmp[i][j];
				}
			}
		}

		return new Animation(frameDuration, frames);
	}

@Override
public void draw(Batch batch, float parentAlpha) {
	super.draw(batch, parentAlpha);
	if(batch instanceof SpriteBatch){
		render((SpriteBatch) batch);
	}
}
}
