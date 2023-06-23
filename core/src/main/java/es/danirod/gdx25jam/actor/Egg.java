package es.danirod.gdx25jam.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.Scaling;

import es.danirod.gdx25jam.GameScreen;
import es.danirod.gdx25jam.JamGame;
import es.danirod.gdx25jam.actions.ScrollAction;

public class Egg extends Image {
	
	private TextureRegion region;
	
	Axolotl player;
	
	public Egg(float speed, Axolotl player) {
		this.player = player;
		region = getEggTexture();
		setDrawable(new TextureRegionDrawable(region));
		setAlign(Align.center);
		setScaling(Scaling.fit);
		setSize(region.getRegionWidth(), region.getRegionHeight());
		addAction(ScrollAction.scrollAndDelete(speed));
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		checkCollision();
	}
	
	void checkCollision() {
		Rectangle axo = null, bounds = null;
		try {
			axo = Pools.obtain(Rectangle.class);
			bounds = Pools.obtain(Rectangle.class);
			player.getHeadBox(axo);
			bounds.set(getX(), getY(), getWidth(), getHeight());
			if (bounds.overlaps(axo) || axo.overlaps(bounds)) {
				GameScreen.INSTANCE.pickEgg();
				remove();
			}
		} finally {
			Pools.free(axo);
			Pools.free(bounds);
		}
	}
		
	TextureRegion getEggTexture() {
		Texture tex = JamGame.assets.get("egg.png");
		TextureRegion[][] regions = TextureRegion.split(tex, tex.getWidth() / 2, tex.getHeight() / 2);
		int chosenX = Math.random() >= 0.5 ? 0 : 1;
		int chosenY = Math.random() >= 0.5 ? 0 : 1;
		return regions[chosenX][chosenY];
	}
	
}
