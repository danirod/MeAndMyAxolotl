package es.danirod.gdx25jam.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/** An image that scrolls and repeats by itself. */
public class RepeatingSolid extends Image {

	/** Traveling speed. */
	float speed;

	/** Base texture, non repeating. */
	Texture texture;

	/** Region, repeated. */
	TextureRegion region;
	
	float worldWidth;
	
	int texHeight;

	public RepeatingSolid(Texture texture, float speed, float worldWidth, int texHeight) {
		assertWrap(texture);
		
		this.texHeight = texHeight;

		this.worldWidth = worldWidth;
		this.speed = speed;
		this.texture = texture;
		this.region = new TextureRegion(texture);
		this.region.setRegionWidth(closestWidth());

		setDrawable(new TextureRegionDrawable(this.region));
		setHeight(texHeight);
		setWidth(this.region.getRegionWidth());
	}

	/** Fails if the provided image is not mirroring. */
	void assertWrap(Texture texture) {
		if (texture.getUWrap() == TextureWrap.ClampToEdge) {
			throw new IllegalArgumentException("Texture should be mirrored");
		}
	}

	/** The width that the region should have so that it can scroll without gaps. */
	int closestWidth() {
		int textureWidth = regionWidth();
		float repeats = worldWidth / textureWidth;
		int totalRepeats = MathUtils.ceil(repeats);
		totalRepeats += (requiresDoubleWidth() ? 2 : 1);
		return textureWidth * totalRepeats;
	}

	/** The effective region of the texture. */
	int regionWidth() {
		return this.texture.getWidth() * (requiresDoubleWidth() ? 2 : 1);
	}

	/** Whether the mirrored seams would require double width. */
	boolean requiresDoubleWidth() {
		// For a perfect seam, if the texture uses MirroredRepeat it is needed
		// to double the width. This means that the scroll needs to treat the
		// texture as if it was double width. Not required in normal mirror.
		return this.texture.getUWrap() == TextureWrap.MirroredRepeat;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		scrollEntity(delta);
		maybeResetPosition();
	}

	/** Moves the entity to the left. */
	void scrollEntity(float delta) {
		float movement = speed * delta;
		moveBy(-movement, 0);
	}

	/** If a full iteration of the texture was traveled, move back to zero. */
	void maybeResetPosition() {
		int textureWidth = regionWidth();
		while (getX() <= -textureWidth) {
			moveBy(textureWidth, 0);
		}
	}
}
