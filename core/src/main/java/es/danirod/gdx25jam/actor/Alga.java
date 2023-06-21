package es.danirod.gdx25jam.actor;

import es.danirod.gdx25jam.JamGame;
import es.danirod.gdx25jam.actions.ScrollAction;

public class Alga extends AnimatedImage {

	public Alga() {
		super(
			Orientation.HORIZONTAL,
			JamGame.assets.get("algae.png"),
			1f
		);
		addAction(ScrollAction.scrollAndDelete(-250f));
	}
	
}
