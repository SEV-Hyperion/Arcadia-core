package com.hyperion.arcadia.entity.projectiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.hyperion.arcadia.MainGame;
import com.hyperion.arcadia.entity.Entity;

public class Projectile extends Entity {

	protected Animation animation;
	private Sound hit;

	public Projectile(Vector2 pos, Vector2 direction) {
		super(pos, direction);
		hit = Gdx.audio.newSound(Gdx.files.internal("sounds/hit.mp3"));
	}

	@Override
	public void update() {
		pos.add(direction);
	}

	/**
	 * Comprueba si el proyectil a alcanzado el final de la pantalla. Deberia
	 * comprobar los 4 limites, pero aun no sabemos disparar bien :S
	 * 
	 * @return true si está fuera del mapa, false si esta dentro
	 */
	public boolean checkEnd() {
		if (pos.y > MainGame.HEIGHT
				+ animation.getKeyFrame(stateTime).getRegionHeight()
				|| pos.y < -animation.getKeyFrame(stateTime).getRegionHeight()) {
			System.out.println("Proyectil fuera");
			return true;
		}
		return false;

	}
	
	public void onHit(){
		hit.play();
	}
}
