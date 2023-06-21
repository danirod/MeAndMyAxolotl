package es.danirod.gdx25jam.actions;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

public class ShakeAction extends TemporalAction {

	private static float SHAKE_DISTANCE = 8f;
	
	private static float SHAKE_TICK = 0.05f;
	
	public static ShakeAction shake() {
		return Actions.action(ShakeAction.class);
	}
	
	public static ShakeAction shake(float duration) {
		ShakeAction action = shake();
		action.setDuration(duration);
		return action;
	}
	
	private float originalX, originalY;
	
	private float transitionTime;
	
	@Override
	protected void begin() {
		originalX = actor.getX();
		originalY = actor.getY();
		transitionTime = 0;
	}
	
	@Override
	protected void update(float percent) {
		if (transitionTime <= getTime()) {
			transitionTime += SHAKE_TICK;
			float shuffledX = originalX + (float) Math.random() * SHAKE_DISTANCE - (SHAKE_DISTANCE / 2);
			float shuffledY = originalY + (float) Math.random() * SHAKE_DISTANCE - (SHAKE_DISTANCE / 2);
			actor.setPosition(shuffledX, shuffledY);
		}
	}
	
	
	@Override
	protected void end() {
		actor.setPosition(originalX, originalY);
	}
}
