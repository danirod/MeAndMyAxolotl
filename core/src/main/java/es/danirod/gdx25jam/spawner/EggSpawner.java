package es.danirod.gdx25jam.spawner;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import es.danirod.gdx25jam.actor.Axolotl;
import es.danirod.gdx25jam.actor.Egg;

public class EggSpawner extends Actor {

	private Random random = new Random();
	
	private Axolotl axolotl;
	
	private Group eggGroup;
	
	boolean spawn = false;
	
	float spawnIn;
	
	public EggSpawner(Group eggGroup, Axolotl axolotl) {
		this.axolotl = axolotl;
		this.eggGroup = eggGroup;
	}
	
	@Override
	public void act(float delta) {
		// If there are no eggs to catch, fix it.
		if (!spawn && eggGroup.getChildren().isEmpty()) {
			spawn = true;
			spawnIn = (float) Math.random() * 2f + 2f;
		}
		
		if (spawn) {
			spawnIn -= delta;
			if (spawnIn < 0) {
				spawnEgg();
				spawn = false;
			}
		}
	}
	
	void spawnEgg() {
		float speed = random.nextFloat(-240f, -120f);
		Egg egg = new Egg(speed, axolotl);
		int y = random.nextInt(100, Gdx.graphics.getHeight() - 100);
		egg.setY(y);
		egg.setX(Gdx.graphics.getWidth() + egg.getWidth());
		eggGroup.addActor(egg);
	}
	
}
