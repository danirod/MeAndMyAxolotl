package es.danirod.gdx25jam.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

public abstract class AnimatedImage extends Image {

	public enum Orientation {
		HORIZONTAL, VERTICAL
	};

	Texture texture;

	TextureRegionDrawable[] drawables;

	int frame = 0;

	float timer = 0f;

	float timePerFrame;

	public AnimatedImage(Orientation orientation, Texture base, float timePerFrame) {
		this.texture = base;
		this.timePerFrame = timePerFrame;
		drawables = getRegions(orientation);
		setDrawable(drawables[frame]);
		setAlign(Align.center);
		setScaling(Scaling.fit);
		switch (orientation) {
		case HORIZONTAL:
			setSize(texture.getWidth() / 2, texture.getHeight());
			break;
		case VERTICAL:
			setSize(texture.getWidth(), texture.getHeight() / 2);
			break;
		}
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		updateFrame(delta);
	}

	void updateFrame(float delta) {
		timer += delta;
		while (timer > timePerFrame) {
			frame = (frame + 1) % 2;
			timer -= timePerFrame;
			setDrawable(drawables[frame]);
		}
	}

	TextureRegionDrawable[] getRegions(Orientation orientation) {
		TextureRegion[][] regions;
		switch (orientation) {
		case HORIZONTAL:
			regions = TextureRegion.split(texture, texture.getWidth() / 2, texture.getHeight());
			return new TextureRegionDrawable[] { new TextureRegionDrawable(regions[0][0]),
					new TextureRegionDrawable(regions[0][1]) };
		case VERTICAL:
			regions = TextureRegion.split(texture, texture.getWidth(), texture.getHeight() / 2);
			return new TextureRegionDrawable[] { new TextureRegionDrawable(regions[0][0]),
					new TextureRegionDrawable(regions[1][0]) };
		}
		return null;
	}

}
