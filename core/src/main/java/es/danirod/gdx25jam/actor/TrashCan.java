package es.danirod.gdx25jam.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Pools;

import es.danirod.gdx25jam.actions.ScrollAction;

public class TrashCan extends Image {

	Axolotl player;
	
	public TrashCan(Texture texture, Axolotl player) {
		super(texture);
		this.player = player;
		addAction(ScrollAction.scrollAndDelete(-120f));
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		if (checkCollision()) {
			player.stun();
			remove();
		}
	}
	
	boolean checkCollision() {
		Rectangle playerBB = null, mineBB = null;
		
		try {
			playerBB = Pools.obtain(Rectangle.class);
			mineBB = Pools.obtain(Rectangle.class);
		
			player.getHeadBox(playerBB);
			mineBB.set(getX(), getY(), getWidth(), getHeight());
			
			return playerBB.overlaps(mineBB) || mineBB.overlaps(playerBB);
		} finally {
			Pools.free(mineBB);
			Pools.free(playerBB);
		}
	}
	
}
