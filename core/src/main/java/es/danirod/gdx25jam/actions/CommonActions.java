package es.danirod.gdx25jam.actions;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;

public class CommonActions {
	
	/** An action to use for things like turtles or u-boot when they are far. */
	public static Action farSwim(float delta, float duration) {
		Action initialWait = Actions.delay(MathUtils.random(2f, 4f));
		Action swing1 = Actions.moveBy(delta / 2, 0, duration / 2f);
		Action appear = Actions.alpha(0.75f, duration / 2f);
		Action swing2 = Actions.moveBy(delta / 2, 0, duration / 2f);
		Action dissapear = Actions.fadeOut(duration / 2f);
		
		Action swingAndAppear = Actions.parallel(swing1, appear);
		Action swingAndDissapear = Actions.parallel(swing2, dissapear);
		return Actions.sequence(initialWait, swingAndAppear, swingAndDissapear);
	}

	public static Action horizontalWave(float radius, float duration) {
		var a1 = Actions.moveBy(-radius, 0, duration / 4, Interpolation.sineOut);
		var a2 = Actions.moveBy(2 * radius, 0, duration / 2, Interpolation.sine);
		var a3 = Actions.moveBy(-radius, 0, duration / 4, Interpolation.sineIn);
		return Actions.sequence(a1, a2, a3);
	}
	
	public static Action verticalWave(float radius, float duration) {
		var a1 = Actions.moveBy(0, -radius, duration / 4, Interpolation.sineOut);
		var a2 = Actions.moveBy(0, 2 * radius, duration / 2, Interpolation.sine);
		var a3 = Actions.moveBy(0, -radius, duration / 4, Interpolation.sineIn);
		return Actions.sequence(a1, a2, a3);
	}

	// The action used by the axolotl arms.
	public static Action gravitySwimArm() {
		// Initially they will get set to zero because we will tweak them every time.
		RotateToAction firstSwing = Actions.rotateTo(0, 0, Interpolation.sine);
		RotateToAction secondSwing = Actions.rotateTo(0, 0, Interpolation.sine);
		
		Action swim = Actions.sequence(
				Actions.run(() -> {
					// Set a new pair of rotations and durations for this arm.
					firstSwing.setRotation(MathUtils.random(10, 30));
					secondSwing.setRotation(MathUtils.random(-30, -10));
					firstSwing.setDuration(MathUtils.random(0.2f, 0.8f));
					secondSwing.setDuration(MathUtils.random(0.2f, 0.8f));
				}),
				
				// Then swing the arm forward and backward.
				firstSwing, secondSwing
		);
		
		// And keep doing this forever.
		return Actions.forever(swim);
	}
}
