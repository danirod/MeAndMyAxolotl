package es.danirod.gdx25jam.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

public class RegrowTimer extends Actor implements Disposable {
	
	private Axolotl player;
	
	private ShapeRenderer renderer = new ShapeRenderer();
	
	public RegrowTimer(Axolotl player) {
		this.player = player;
	}
	
	/** The center of the circle. */
	float getCenterX() {
		return getX() + getWidth() * 0.5f;
	}
	
	/** The center of the circle. */
	float getCenterY() {
		return getY() + getHeight() * 0.5f;
	}
	
	/** The radius of the circle. */
	float getRadius() {
		float diameter = Math.min(getWidth(), getHeight());
		return diameter / 2;
	}
	
	/** The arc of the circle. */
	float getArc() {
		return (360f / Axolotl.TIME_TO_HEAL) * player.timeToHeal;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.end();
		renderer.setProjectionMatrix(batch.getProjectionMatrix());
		renderer.setTransformMatrix(batch.getTransformMatrix());
		renderer.begin(ShapeType.Filled);
		renderer.arc(getCenterX(), getCenterY(), getRadius(), 0, 360 - getArc());
		renderer.end();
		batch.begin();
	}
	
	public void dispose() {
		renderer.dispose();
	}
}
