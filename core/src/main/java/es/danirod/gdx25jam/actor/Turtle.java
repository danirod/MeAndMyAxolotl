package es.danirod.gdx25jam.actor;

import com.badlogic.gdx.Gdx;
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

		// Pick a random position for the turtle in the background.
		int y = MathUtils.random(120, Gdx.graphics.getHeight() - 120);
		turtle.setPosition(Gdx.graphics.getWidth() / 4, y);

		// Hide the turtle so that we can show it animated.
		turtle.setColor(1f, 1f, 1f, 0f);
		
		// Start a swimming sequence.
		turtle.addAction(Actions.sequence(
				CommonActions.farSwim(Gdx.graphics.getWidth() / 1.5f, 8f),
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
		
		// Randomly place the turtle outside the screen.
		int vertical = MathUtils.random(80, 290);
		turtle.setPosition(Gdx.graphics.getWidth() + turtle.getWidth(), vertical);
		
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
		turtle.addAction(
				Actions.sequence(
						Actions.moveTo(Gdx.graphics.getWidth() + 100, Gdx.graphics.getWidth() / 2, 1f),
						Actions.run(() -> switchState())
				)
		);
	}
	
	public void switchToReposition() {
		// Pick a random position on the screen where the turtle can be moved to.
		float horizontal = MathUtils.random(0.5f * Gdx.graphics.getWidth(), 0.75f * Gdx.graphics.getWidth());
		int vertical = MathUtils.random(80, 290);
		turtle.addAction(
				Actions.sequence(
						Actions.parallel(
								Actions.moveTo(horizontal, vertical, 2f, Interpolation.circle),
								Actions.repeat(3, CommonActions.verticalWave(50, 2f))
						),
						Actions.run(() -> switchState())
				)
		);
	}

	public void switchToAttack() {
		consecutiveAttacks++;
		float x = player.getX() + player.getOriginX() / 2, y = player.getY() - player.getOriginY();
		float currentX = turtle.getX(), currentY = turtle.getY();
		
		turtle.addAction(
				Actions.sequence(
						Actions.moveTo(Gdx.graphics.getWidth() / 2, y, 0.25f, Interpolation.circleOut),
						Actions.delay(0.25f),
						Actions.moveTo(x, y, 0.25f, Interpolation.circleOut),
						Actions.run(() -> tryHit()),
						Actions.moveTo(currentX, currentY, 0.25f, Interpolation.circleIn),
						Actions.run(() -> switchState())
				)
		);
	}
	
	private void tryHit() {
		System.out.println("AYUDA");
		if (player.isColliding(turtle)) {
			System.out.println("AYUDA DE NUEVO");
			player.stun();
			player.hit();
		}
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
