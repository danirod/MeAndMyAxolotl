package es.danirod.gdx25jam.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

public class ScrollAction extends Action {

	public static ScrollAction scroll(float speed) {
		ScrollAction action = Actions.action(ScrollAction.class);
		action.speed = speed;
		return action;
	}
	
	public static SequenceAction scrollAndDelete(float speed) {
		return Actions.sequence(scroll(speed), Actions.removeActor());
	}
	
	float speed;
	
	public ScrollAction() {
		this.speed = 0;
	}
	
	public ScrollAction(float speed) {
		this.speed = speed;
	}
	
	@Override
	public boolean act(float delta) {
		actor.moveBy(speed * delta, 0);
		if (speed > 0) {
			return actor.getX() >= Gdx.graphics.getWidth();
		} else {
			return actor.getRight() < 0;
		}
	}

}
