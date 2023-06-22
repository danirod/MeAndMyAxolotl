package es.danirod.gdx25jam.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;

import es.danirod.gdx25jam.GameScreen;
import es.danirod.gdx25jam.JamGame;
import es.danirod.gdx25jam.actions.CommonActions;

public class Turtle extends Group {

	public static class FarTurtle extends AnimatedImage {
		public FarTurtle() {
			super(Orientation.VERTICAL, JamGame.assets.get("farturtle.png"), 0.5f);
		}
	}

	public static class NearTurtle extends AnimatedImage {
		public NearTurtle() {
			super(Orientation.HORIZONTAL, JamGame.assets.get("turtle.png"), 0.5f);
		}
	}
	
	public Turtle(Axolotl player) {
		this.player = player;
	}
	
	Axolotl player;

	public static enum TurtleState {
		Calm, Alert, Reposition, Attack, Calming,
	}

	public TurtleState currentState = TurtleState.Calm;

	float nextEval = 10f;

	public void switchToCalm() {
		Gdx.app.log("Turtle", "Switching to CALM");
		
		// Switch to the FarTurtle sprite.
		clearChildren();
		var turtle = new FarTurtle();

		// Pick a random position for the turtle in the background.
		int y = MathUtils.random(120, Gdx.graphics.getHeight() - 120);
		setPosition(Gdx.graphics.getWidth() / 4, y);

		// Hide the turtle so that we can show it animated.
		setColor(1f, 1f, 1f, 0f);
		
		// Start a swimming sequence.
		addAction(Actions.sequence(
				// Wait a little.
				Actions.delay(MathUtils.random(2f, 4f)),
				
				// Fade in and swim.
				Actions.parallel(
						Actions.alpha(0.75f, 4f),
						Actions.moveBy(Gdx.graphics.getWidth() / 3, 0, 4f)
				),
				
				// Continue swimming and fade out.
				Actions.parallel(
						Actions.fadeOut(4f),
						Actions.moveBy(Gdx.graphics.getWidth() / 3, 0, 4f)
				),
				
				// Switch to the next state.
				Actions.delay(MathUtils.random(2f, 4f)), Actions.run(() -> {
					switchState();
				})
		));

		addActor(turtle);
	}

	public void switchToAlert() {
		consecutiveAttacks = 0;
		// Switch to the near turtle sprite.
		clearChildren();
		var turtle = new NearTurtle();
		addActor(turtle);
		
		// Reset the alpha, in case it was kept from the old state.
		getColor().a = 1;
		Gdx.app.log("Turtle", "Switching to ALERT");

		// Randomly place the turtle outside the screen.
		int vertical = MathUtils.random(80, 290);
		setPosition(Gdx.graphics.getWidth() + turtle.getWidth(), vertical);
		
		addAction(
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
		Gdx.app.log("Turtle", "Switching to CALMING");
		getColor().a = 1;
		addAction(
				Actions.sequence(
						Actions.moveTo(Gdx.graphics.getWidth() + 100, Gdx.graphics.getWidth() / 2, 1f),
						Actions.run(() -> switchState())
				)
		);
	}
	
	public void switchToReposition() {
		// Reset the alpha (in case it comes from a previous animation).
		getColor().a = 1;
		
		Gdx.app.log("Turtle", "Switching to REPOSITION");
		
		// Pick a random position on the screen where the turtle can be moved to.
		float horizontal = MathUtils.random(0.5f * Gdx.graphics.getWidth(), 0.75f * Gdx.graphics.getWidth());
		int vertical = MathUtils.random(80, 290);
		addAction(
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
		Gdx.app.log("Turtle", "Switching to ATTACK");
		consecutiveAttacks++;
		getColor().a = 1;
		float x = player.getX() + player.getOriginX() / 2, y = player.getY() - player.getOriginY();
		float currentX = getX(), currentY = getY();
		
		addAction(
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
		Rectangle axo = Pools.obtain(Rectangle.class);
		Rectangle turtle = Pools.obtain(Rectangle.class);
		
		player.getHeadBox(axo);
		turtle.set(getX(), getY(), getChild(0).getWidth(), getChild(0).getHeight());
		
		Gdx.app.log("Turtle", "Axo: " + axo.toString());
		Gdx.app.log("Turtle", "Turt: " + turtle.toString());
		if (axo.overlaps(turtle) || turtle.overlaps(axo)) {
			player.stun();
			player.hit();
		}
		
		Pools.freeAll(Array.with(axo, turtle));
	}
	
	int consecutiveAttacks = 0;

	/**
	 * This function will choose whether the turtle can attack the axolotl
	 * based on whether the axolotl is vertically closer to the atxolotl.
	 * If the axolotl is far from the turtle, it will reposition instead.
	 */
	TurtleState prepareToAttack() {
		if (Math.abs(getY() - player.getY()) < 100) {
			return TurtleState.Attack;
		} else {
			return TurtleState.Reposition;
		}
	}
	
	TurtleState nextState() {
		double i = Math.random();
		switch (currentState) {
		case Calm:
			if (i < 0.5f + GameScreen.INSTANCE.getScore() * 0.02f) {
				return TurtleState.Alert;
			}
			return TurtleState.Calm;
		case Alert:
		case Reposition:
			if (i < 0.6f - consecutiveAttacks * 0.2f) {
				return prepareToAttack();
			} else {
				return TurtleState.Calming;
			}
		case Calming:
			return TurtleState.Calm;
		case Attack:
			return TurtleState.Reposition;
		default:
			return currentState;
		}
	}

	void switchState() {
		TurtleState next = nextState();
		this.currentState = next;
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
