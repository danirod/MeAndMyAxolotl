package es.danirod.gdx25jam.actor;

import es.danirod.gdx25jam.JamGame;

public class Turtle extends AnimatedImage {
	
	public Turtle() {
		super(
            Orientation.HORIZONTAL,
            JamGame.assets.get("turtle.png"),
            0.5f
        );
	}

}
