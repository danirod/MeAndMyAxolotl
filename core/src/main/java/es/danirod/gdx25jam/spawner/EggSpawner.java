package es.danirod.gdx25jam.spawner;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import es.danirod.gdx25jam.actor.Axolotl;
import es.danirod.gdx25jam.actor.Egg;

public class EggSpawner extends Actor {

	/** The player that has to pick the egg. */
	private Axolotl axolotl;
	
	/** The group where the axolotls will be placed. */
	private Group eggGroup;
	
	/** An egg is set to spawn. */
	boolean spawn = false;
	
	public EggSpawner(Group eggGroup, Axolotl axolotl) {
		this.axolotl = axolotl;
		this.eggGroup = eggGroup;
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		if (shouldSpawnAnEgg()) {
			scheduleNewEgg();
		}
	}
	
	/** Is it time to spawn a new egg? */
	boolean shouldSpawnAnEgg() {
		return !spawn && eggGroup.getChildren().isEmpty();
	}
	
	/** Defer the initialization of a new egg. */
	void scheduleNewEgg() {
		spawn = true;
		float timer = MathUtils.random(2f, 5f);
		Action action = Actions.sequence(Actions.delay(timer), Actions.run(() -> {
			float speed = MathUtils.random(-240f, -120f);
			Egg egg = new Egg(speed, axolotl);
			int y = MathUtils.random(100, (int) getStage().getViewport().getWorldHeight() - 100);
			egg.setY(y);
			egg.setX(getStage().getViewport().getWorldWidth() + egg.getWidth());
			eggGroup.addActor(egg);
			spawn = false;
		})); 
		addAction(action);
	}
}
