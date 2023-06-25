package es.danirod.gdx25jam.spawner;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import es.danirod.gdx25jam.actor.Boat;

public class BoatSpawner extends Actor {

	Group boats;
	
	public BoatSpawner(Group boats) {
		this.boats = boats;
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		if (canSpawnBoat()) {
			trySpawnBoat();
		}
	}
	
	boolean canSpawnBoat() {
		return boats.getChildren().size == 0;
	}
	
	void trySpawnBoat() {
		if (MathUtils.randomBoolean(0.005f)) {
			spawnBoat();
		}
	}
	
	void spawnBoat() {
		Boat b = new Boat();
		b.setPosition(getStage().getViewport().getWorldWidth(), getStage().getViewport().getWorldHeight() - 100);
		boats.addActor(b);
	}
	
}
