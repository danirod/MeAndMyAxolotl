package es.danirod.gdx25jam.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

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

	TurtleState currentState = TurtleState.Calm;

	float nextEval = 10f;

	public void switchToCalm() {
		clearChildren();
		var turtle = new FarTurtle();

		int y = MathUtils.random(120, Gdx.graphics.getHeight() - 120);
		setPosition(Gdx.graphics.getWidth() / 4, y);
		setRotation(0);
		setColor(1f, 1f, 1f, 0f);

		addAction(Actions.sequence(Actions.delay(MathUtils.random(2f, 4f)),
				Actions.parallel(Actions.alpha(0.75f, 4f), Actions.moveBy(Gdx.graphics.getWidth() / 3, 0, 4f)),
				Actions.parallel(Actions.fadeOut(4f), Actions.moveBy(Gdx.graphics.getWidth() / 3, 0, 4f)),
				Actions.delay(MathUtils.random(2f, 4f)), Actions.run(() -> {
					Gdx.app.log("Turtle", "Animation ended");
					setColor(1f, 1f, 1f, 1f);
					switchState();
				})));

		addActor(turtle);
	}

	public void switchToAlert() {
		clearChildren();
		var turtle = new NearTurtle();
		addActor(turtle);

		int vertical = MathUtils.random(80, 290);
		setPosition(Gdx.graphics.getWidth() + turtle.getWidth(), vertical);
		addAction(
			Actions.sequence(
				Actions.parallel(
					Actions.repeat(3, CommonActions.verticalWave(50, 2f)),
					Actions.moveBy(-200, 0, 2f, Interpolation.circleOut)
				),
				Actions.run(() -> {
					switchState();
				})
			)
		);
	}
	
	void switchToCalming() {
		addAction(
				Actions.sequence(
						Actions.moveTo(Gdx.graphics.getWidth() + 100, Gdx.graphics.getWidth() / 2, 1f),
						Actions.run(() -> switchState())
				)
		);
	}
	
	void switchToReposition() {
		float horizontal = MathUtils.random(0.5f * Gdx.graphics.getWidth(), 0.75f * Gdx.graphics.getWidth());
		int vertical = MathUtils.random(80, 290);
		addAction(
				Actions.sequence(
						Actions.parallel(
								Actions.moveTo(horizontal, vertical, 2f, Interpolation.circle),
								Actions.repeat(3, CommonActions.verticalWave(50, 2f)),
						Actions.run(() -> {
							switchState();
						})
				)
				
		));
	}

	void switchToAttack() {
		float x = player.getX(), y = player.getY() + player.getOriginY();
		float currentX = getX(), currentY = getY();
		
		addAction(
				Actions.sequence(
						Actions.moveTo(Gdx.graphics.getWidth() / 2, y, 0.25f, Interpolation.circleOut),
						Actions.delay(0.25f),
						Actions.moveTo(x, y, 0.25f, Interpolation.circleOut),
						Actions.moveTo(currentX, currentY, 0.25f, Interpolation.circleIn),
						Actions.run(() -> switchState())
				)
		);
	}

	TurtleState nextState() {
		double i = Math.random();
		switch (currentState) {
		case Calm:
			if (i < 0.5f) {
				return TurtleState.Alert;
			}
			return TurtleState.Calm;
		case Alert:
		case Reposition:
			if (i < 0.2f) {
				return TurtleState.Attack;
			} else if (i < 0.4f) {
				return TurtleState.Calming;
			}
			return TurtleState.Reposition;
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
		Gdx.app.log("Turtle", "Switching " + currentState + " -> " + next);
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
