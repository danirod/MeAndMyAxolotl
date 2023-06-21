package es.danirod.gdx25jam.spawner;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import es.danirod.gdx25jam.actor.Bubble;

public class BubbleSpawner extends Actor {
	
	Group bubbles;
	
	Actor axolotl;
	
	public BubbleSpawner(Group bubbles, Actor axolotl) {
		this.bubbles = bubbles;
		this.axolotl = axolotl;
	}

	@Override
	public void act(float delta) {
		maybeSpawnBubble();
	}
	
	void maybeSpawnBubble() {
    	if (Math.random() < 0.02) {
    		Bubble b = new Bubble();
    		b.setPosition(axolotl.getX() + axolotl.getOriginX(), axolotl.getY() + axolotl.getOriginY());
    		bubbles.addActor(b);
    	}
    }

}
