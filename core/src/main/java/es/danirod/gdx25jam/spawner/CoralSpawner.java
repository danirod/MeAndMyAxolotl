package es.danirod.gdx25jam.spawner;

import java.util.stream.Stream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;

import es.danirod.gdx25jam.actor.Coral;

public class CoralSpawner extends Actor {

	/** There are two groups, mainly for z-ordering against the entities. */
	Group backCoral, frontCoral;
	
	/** Height of floor. (Prevents spawning corals in the air.) */
	float floorHeight;
	
	public CoralSpawner(Group backCoral, Group frontCoral, float floorHeight) {
		this.backCoral = backCoral;
		this.frontCoral = frontCoral;
		this.floorHeight = floorHeight;
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		if (shouldSpawnNewCoral()) {
			spawnCoral();
		}
	}
	
	void spawnCoral() {
		Coral coral = new Coral();
		coral.setAlign(Align.bottom);
		coral.setScale(MathUtils.random(0.5f, 1.5f));
		coral.setX(Gdx.graphics.getWidth());
		coral.setY(MathUtils.random(floorHeight));
		
		if (coral.getScaleY() < 1) {
			backCoral.addActor(coral);
		} else {
			frontCoral.addActor(coral);
		}
	}
	
	boolean shouldSpawnNewCoral() {
		// Don't spawn a coral if there is already a coral in the right half of the screen.
		if (getRightestCoral() > Gdx.graphics.getWidth() / 2) {
			return false;
		}
		// Maybe?
		return MathUtils.randomBoolean(0.02f);
	}
	
	/** Get the most right position of all the visible corals. */
	float getRightestCoral() {
		return getCorals().map(Coral::getRight).reduce(Float::max).orElse(0f);
	}
	
	/** Get all the corals in both coral groups. */
	Stream<Coral> getCorals() {
		return Stream.concat(groupToStream(backCoral), groupToStream(frontCoral));
	}
	
	/** Stream the corals in a group. */
	Stream<Coral> groupToStream(Group g) {
		var children = g.getChildren();
		var iterator = children.iterator();
		return Stream.generate(iterator::next)
				.limit(children.size)
				.map(act -> (Coral) act);
	}
}
