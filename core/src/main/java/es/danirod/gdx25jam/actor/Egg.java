package es.danirod.gdx25jam.actor;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

import es.danirod.gdx25jam.GameStage;
import es.danirod.gdx25jam.JamGame;
import es.danirod.gdx25jam.actions.ScrollAction;

public class Egg extends Image {

	private TextureRegion region;

	Axolotl player;
	
	boolean picked = false;
	
	Sound pick = JamGame.assets.get("sounds/crunch.mp3");
	
	void playPick() {
		float pitch = MathUtils.random(0.8f, 1.2f);
		pick.play(1.0f, pitch, 0.0f);
	}

	public Egg(float speed, Axolotl player) {
		this.player = player;

		region = getEggTexture();
		setDrawable(new TextureRegionDrawable(region));
		setAlign(Align.center);
		setScaling(Scaling.fit);
		setSize(region.getRegionWidth(), region.getRegionHeight());
		setTouchable(Touchable.enabled);

		addAction(ScrollAction.scrollAndDelete(speed));
	}

	/** Pick a random egg from the texture pack. */
	TextureRegion getEggTexture() {
		Texture tex = JamGame.assets.get("egg.png");
		TextureRegion[][] regions = TextureRegion.split(tex, tex.getWidth() / 2, tex.getHeight() / 2);
		int chosenX = Math.random() >= 0.5 ? 0 : 1;
		int chosenY = Math.random() >= 0.5 ? 0 : 1;
		return regions[chosenX][chosenY];
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		checkCollision();
	}

	/** Checks if the egg is being picked. */
	void checkCollision() {
		if (!picked && player.isColliding(this)) {
			picked = true;
			setTouchable(Touchable.disabled);
			playPick();
			((GameStage) getStage()).pickEgg(this);
		}
	}
}
