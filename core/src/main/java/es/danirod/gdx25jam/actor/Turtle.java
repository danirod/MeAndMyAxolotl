package es.danirod.gdx25jam.actor;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import es.danirod.gdx25jam.GameState;
import es.danirod.gdx25jam.JamGame;
import es.danirod.gdx25jam.actions.CommonActions;
import es.danirod.gdx25jam.spawner.BubbleSpawner;

public class Turtle extends Group {

	/** The sprite to use when the turtle is far. */
	public static class FarTurtle extends AnimatedImage {
		public FarTurtle() {
			super(Orientation.VERTICAL, JamGame.assets.get("farturtle.png"), 0.5f);
		}
	}

	/** The sprite to use when the turtle is near. */
	public static class NearTurtle extends AnimatedImage {
		public NearTurtle() {
			super(Orientation.HORIZONTAL, JamGame.assets.get("turtle.png"), 0.5f);
		}
	}
	
	/** The state machine for the turtle. */
	public static enum TurtleState {
		Calm, Alert, Reposition, Attack, Calming;
	}
	
	Axolotl player;
	
	Group far, near;
	
	public TurtleState state = TurtleState.Calm;

	Sound[] bites = {
		JamGame.assets.get("sounds/bite1.ogg", Sound.class),
		JamGame.assets.get("sounds/bite2.ogg", Sound.class),
		JamGame.assets.get("sounds/bite3.ogg", Sound.class),
		JamGame.assets.get("sounds/bite4.ogg", Sound.class)
	};
	
	Sound flow = JamGame.assets.get("sounds/flow.ogg");

	Actor turtle;
	
	int consecutiveAttacks = 0;
	
	BubbleSpawner bubbleSpawner;
	Group bubblesGroup;
	
	public Turtle(Axolotl player, Group far, Group near, Group bubbles) {
		this.player = player;
		this.far = far;
		this.near = near;
		this.bubblesGroup = bubbles;
	}

	public void switchToCalm() {	
		// Switch to the FarTurtle sprite.
		if (turtle != null)
			turtle.remove();
		if (bubbleSpawner != null)
			bubbleSpawner.remove();
		turtle = new FarTurtle();
		far.addActor(turtle);
		playFlowSound(0.25f);

		// Pick a random position for the turtle in the background.
		int y = MathUtils.random(120, (int) getStage().getViewport().getWorldHeight() - 120);
		turtle.setPosition((int) getStage().getViewport().getWorldWidth() / 4, y);

		// Hide the turtle so that we can show it animated.
		turtle.setColor(1f, 1f, 1f, 0f);
		
		// Start a swimming sequence.
		turtle.addAction(Actions.sequence(
				CommonActions.farSwim(getStage().getViewport().getWorldWidth() / 1.5f, 8f),
				Actions.run(() -> switchState())
		));
	}

	public void switchToAlert() {
		consecutiveAttacks = 0;
		// Switch to the near turtle sprite.
		if (turtle != null)
			turtle.remove();
		turtle = new NearTurtle();
		near.addActor(turtle);
		
		bubbleSpawner = new BubbleSpawner(bubblesGroup, turtle, 4, 50);
		addActor(bubbleSpawner);
		playFlowSound(1.0f);
		
		// Randomly place the turtle outside the screen.
		int vertical = MathUtils.random(80, 270);
		turtle.setPosition(getStage().getViewport().getWorldWidth() + turtle.getWidth(), vertical);
		
		turtle.addAction(
			Actions.sequence(
				Actions.parallel(
					// Wave the turtle up and down. 
					Actions.repeat(3, CommonActions.verticalWave(50, 2f)),
					
					// Move the turtle into the viewport.
					Actions.moveBy(-200, 0, 2f, Interpolation.circleOut)
				),
				Actions.run(() -> switchState())
			)
		);
	}
	
	public void switchToCalming() {
		playFlowSound(1.0f);
		turtle.addAction(
				Actions.sequence(
						Actions.moveTo(getStage().getViewport().getWorldWidth() + 100, getStage().getViewport().getWorldWidth() / 2, 1f),
						Actions.run(() -> switchState())
				)
		);
	}
	
	public void switchToReposition() {
		// Pick a random position on the screen where the turtle can be moved to.
		float horizontal = MathUtils.random(0.5f * getStage().getViewport().getWorldWidth(), 0.75f * getStage().getViewport().getWorldWidth());
		int vertical = MathUtils.random(80, 270);
		turtle.addAction(
				Actions.sequence(
						Actions.parallel(
								Actions.moveTo(horizontal, vertical, 2f, Interpolation.circle),
								Actions.repeat(3, CommonActions.verticalWave(50, 2f))
						),
						Actions.run(() -> switchState())
				)
		);
		playFlowSound(1.0f);
	}

	public void switchToAttack() {
		consecutiveAttacks++;
		float x = player.getX() + player.getOriginX() / 2, y = player.getY() - player.getOriginY();
		float currentX = turtle.getX(), currentY = turtle.getY();
		
		turtle.addAction(
				Actions.sequence(
						Actions.moveTo(getStage().getViewport().getWorldWidth() / 2, y, 0.25f, Interpolation.circleOut),
						Actions.delay(0.25f),
						Actions.moveTo(x, y, 0.25f, Interpolation.circleOut),
						Actions.run(() -> tryHit()),
						Actions.moveTo(currentX, currentY, 0.25f, Interpolation.circleIn),
						Actions.run(() -> switchState())
				)
		);
	}
	
	private void tryHit() {
		if (player.isColliding(turtle)) {
			playBiteSound();
			player.stun();
			player.hit();
		}
	}
	
	void playFlowSound(float volume) {
		float pitch = MathUtils.random(0.9f, 1.1f) * volume;
		flow.play(0.8f * volume, pitch, 0.0f);
	}
	
	void playBiteSound() {
		int sound = MathUtils.random(bites.length - 1);
		float pitch = MathUtils.random(0.75f, 1.25f);
		bites[sound].play(0.5f, pitch, 0.0f);
	}

	/**
	 * This function will choose whether the turtle can attack the axolotl
	 * based on whether the axolotl is vertically closer to the atxolotl.
	 * If the axolotl is far from the turtle, it will reposition instead.
	 */
	TurtleState prepareToAttack() {
		if (Math.abs(turtle.getY() - player.getY()) < 100) {
			return TurtleState.Attack;
		} else {
			return TurtleState.Reposition;
		}
	}
	
	TurtleState nextState() {
		double i = Math.random();
		switch (state) {
		case Calm:
			if (i < 0.7f + GameState.instance.score * 0.02f) {
				return TurtleState.Alert;
			}
			return TurtleState.Calm;
		case Alert:
		case Reposition:
			if (i < 0.8f - consecutiveAttacks * 0.2f) {
				return prepareToAttack();
			} else {
				return TurtleState.Calming;
			}
		case Calming:
			return TurtleState.Calm;
		case Attack:
			return TurtleState.Reposition;
		default:
			return state;
		}
	}

	void switchState() {
		TurtleState next = nextState();
		this.state = next;
		switch (next) {
		case Calm:
			switchToCalm();
			break;
		case Alert:
			switchToAlert();
			break;
		case Calming:
			switchToCalming();
			break;
		case Reposition:
			switchToReposition();
			break;
		case Attack:
			switchToAttack();
			break;
		default:
			switchToCalm();
			break;
		}
	}
}
