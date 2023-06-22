package es.danirod.gdx25jam.actions;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class CommonActions {

	public static Action verticalWave(float radius, float duration) {
		var a1 = Actions.moveBy(0, -radius, duration / 4, Interpolation.sineOut);
		var a2 = Actions.moveBy(0, 2 * radius, duration / 2, Interpolation.sine);
		var a3 = Actions.moveBy(0, -radius, duration / 4, Interpolation.sineIn);
		return Actions.sequence(a1, a2, a3);
	}
	
}
