package es.danirod.gdx25jam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

import es.danirod.gdx25jam.actions.CommonActions;
import es.danirod.gdx25jam.actor.Axolotl;
import es.danirod.gdx25jam.actor.Egg;
import es.danirod.gdx25jam.actor.PendingEggs;
import es.danirod.gdx25jam.actor.RepeatingSolid;
import es.danirod.gdx25jam.actor.Turtle;
import es.danirod.gdx25jam.spawner.CoralSpawner;
import es.danirod.gdx25jam.spawner.EggSpawner;
import es.danirod.gdx25jam.spawner.TrashSpawner;
import es.danirod.gdx25jam.spawner.UbootSpawner;

public class GameStage extends Stage {

	/** Sky, water, floor. */
	Group background = new Group();

	/** Far turtles and submarines. */
	Group swimmingFar = new Group();

	/** Corals that render behind the player. */
	Group coralFar = new Group();

	/** Group associated with the axolotl bubbles. */
	Group bubbles = new Group();
	
	/** The star. */
	Axolotl axolotl = new Axolotl(bubbles);

	/** Corals that render in front of the player. */
	Group coralNear = new Group();
	
	/** The group where we will add the near turtles. */
	Group swimmingNear = new Group();

	/** A group that only exists to count easier the eggs. */
	Group eggs = new Group();

	/** A group that only exists to count easier the trash. */
	Group trash = new Group();
	
	/** The turtle that will attack the player. */
	Turtle turtle = new Turtle(axolotl, swimmingFar, swimmingNear, bubbles);

	/** The counter that indicates how many eggs to pick. */
	PendingEggs counter = new PendingEggs();
	
	float floorHeight;
	
	public GameStage() {
		initBackground();
		initSpawners();
		addGroups();
	}
	
	void initBackground() {
		var water = new Image(JamGame.assets.get("water.png", Texture.class));
		water.setY(40);
		water.setHeight(Gdx.graphics.getHeight() - 120);
		water.getColor().a = 0.5f;
		background.addActor(water);
		
		var sky = new RepeatingSolid(JamGame.assets.get("sky.png"), 200);
		sky.setAlign(Align.bottom);
		sky.setY(Gdx.graphics.getHeight() - 80);
		sky.getColor().a = 0.5f;
		background.addActor(sky);
		
		var floor = new RepeatingSolid(JamGame.assets.get("floor.png"), 200);
		floor.setY(0);
		floorHeight = floor.getHeight();
		background.addActor(floor);
	}
	
	void initSpawners() {
		addActor(new CoralSpawner(coralFar, coralNear, floorHeight));
		addActor(new EggSpawner(eggs, axolotl));
		addActor(new TrashSpawner(trash, axolotl));
		addActor(new UbootSpawner(swimmingFar));
		addActor(turtle);
		turtle.switchToCalm();
	}
	
	void addGroups() {
		addActor(background);
		addActor(swimmingFar);
		addActor(coralFar);
		addActor(axolotl);
		axolotl.setPosition(20, Gdx.graphics.getHeight() / 2);
		addActor(bubbles);
		addActor(coralNear);
		addActor(eggs);
		addActor(trash);
		addActor(swimmingNear);
		addActor(counter);
		counter.setPosition(10, Gdx.graphics.getHeight() - counter.getHeight() - 10);
	}
	
	public void pickEgg(Egg egg) {
		egg.clearActions();
		Action updateTimers = Actions.run(() -> {
			if (counter.update(++GameState.instance.score)) {
				triggerFinalSequence();
			}
			egg.remove();
		});
		egg.addAction(Actions.sequence(CommonActions.pickEgg(counter), updateTimers));
	}
	
	void triggerFinalSequence() {
		Action forward = Actions.parallel(
			Actions.moveBy(Gdx.graphics.getWidth() / 3, 0, 1f, Interpolation.sineOut),
			CommonActions.verticalWave(10f, 1f)
		);
		Action moveBack = Actions.scaleTo(-1f, 1f);
		Action grow = Actions.scaleTo(-3f, 3f, 1f, Interpolation.sineIn);
		Action goBack = Actions.moveBy(-Gdx.graphics.getWidth(), 0, 1f, Interpolation.sineIn);
		Action wave = CommonActions.verticalWave(20f, 1f);
		Action growAndGoBack = Actions.parallel(grow, goBack, wave);
		Action animation = Actions.sequence(forward, moveBack, growAndGoBack);
		axolotl.addAction(animation);
	}
}
