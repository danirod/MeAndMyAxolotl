package es.danirod.gdx25jam.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Pools;

import es.danirod.gdx25jam.JamGame;
import es.danirod.gdx25jam.Utils;
import es.danirod.gdx25jam.actions.ScrollAction;

public class Rubbish extends Image {

	Axolotl player;
	
	/** Choose some random rubbish texture. */
	static Texture randomRubbish() {
		String[] assets = {"trash1.png", "trash2.png", "trash3.png"};
		String texture = Utils.pick(assets);
		return JamGame.assets.get(texture);
	}
	
	public Rubbish(Axolotl player) {
		super(randomRubbish());
		this.player = player;
		addAction(ScrollAction.scrollAndDelete(-120f));
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		checkCollision();
	}
	
	/** Checks if the player has hit some trash. */
	void checkCollision() {
		if (player.isColliding(this)) {
			player.stun();
			remove();
		}
	}
}
