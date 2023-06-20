package es.danirod.gdx25jam;

public class Alga extends AnimatedImage {

	public Alga() {
		super(
			Orientation.HORIZONTAL,
			JamGame.assets.get("algae.png"),
			1f
		);
	}
	
	@Override
	public void act(float delta) {
		moveBy(-250 * getScaleY() * delta, 0);
		if (getRight() < 0) {
			remove();
		}
	}
}
