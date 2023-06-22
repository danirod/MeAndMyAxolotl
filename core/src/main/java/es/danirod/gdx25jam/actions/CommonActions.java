package es.danirod.gdx25jam.actions;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class CommonActions {

	public static Action verticalWave(float radius, float duration) {
		var a1 = Actions.moveBy(0, -radius, duration / 4, Interpolation.sineOut);
		var a2 = Actions.moveBy(0, 2 * radius, duration / 2, Interpolation.sine);
		var a3 = Actions.moveBy(0, -radius, duration / 4, Interpolation.sineIn);
		return Actions.sequence(a1, a2, a3);
	}

	// The action used by the axolotl arms.
	public static Action gravitySwimArm() {
		float angle = MathUtils.random(15, 25);
		float angle2 = MathUtils.random(-25, -15);
		float duration = MathUtils.random(0.3f, 0.6f);
		float duration2 = MathUtils.random(0.3f, 0.6f);
		return Actions.forever(
				Actions.sequence(
						Actions.rotateTo(angle, duration, Interpolation.sine),
						Actions.rotateTo(angle2, duration2, Interpolation.sine)
				)
		);
	}
}
