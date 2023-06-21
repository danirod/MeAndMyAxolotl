package es.danirod.gdx25jam.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class RepeatingSolid extends Image {
	
	private int textureWidth;
	
	public RepeatingSolid(Texture texture) {
		super(texture);
		textureWidth = texture.getWidth();
		setPosition(0, 0);
		setHeight(texture.getHeight());
		setWidth(computeDesiredWidth());
	}
	
	private float computeDesiredWidth() {
		float width = Gdx.graphics.getWidth();
		float desiredWidth = (textureWidth) * (1 + (float) Math.floor((width / textureWidth) + 1));
		System.out.println(desiredWidth);
		return desiredWidth;
	}
	
	@Override
	public void act(float delta) {
		moveBy(-delta * 200, 0);
		while (getX() <= -textureWidth) {
			moveBy(textureWidth, 0);
		}
	}
}
