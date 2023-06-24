package es.danirod.gdx25jam.spawner;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import es.danirod.gdx25jam.actor.Bubble;

public class BubbleSpawner extends Actor {
	/** The bubbles group. (Mostly for z-ordering). */
	Group bubbles;
	
	/** The actor that will own the bubbles (they will float on top of the head). */
	Actor owner;
	
	/** The coordinates of the mouth, where the bubbles will start. */
	float mouthX, mouthY;
	
	public BubbleSpawner(Group bubbles, Actor owner, float mouthX, float mouthY) {
		this.bubbles = bubbles;
		this.owner = owner;
		this.mouthX = mouthX;
		this.mouthY = mouthY;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		maybeSpawnBubble();
	}
	
	/** Try to spawn a bubble. */
	void maybeSpawnBubble() {
    	if (spawnBubble()) {
    		Bubble b = new Bubble();
    		b.setPosition(owner.getX() + mouthX, owner.getY() + mouthY);
    		bubbles.addActor(b);
    	}
    }

	/** Can I spawn a bubble? */
	boolean spawnBubble() {
		return owner.isVisible() && MathUtils.randomBoolean(0.03f);
	}
}
