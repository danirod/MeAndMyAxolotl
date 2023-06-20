package es.danirod.gdx25jam;

import java.util.stream.Stream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;

public class AlgaSpawner extends Actor {

	Group algaGroupBack, algaGroupFront;
	
	public AlgaSpawner(Group algaGroupBack, Group algaGroupFront) {
		this.algaGroupBack = algaGroupBack;
		this.algaGroupFront = algaGroupFront;
	}
	
	@Override
	public void act(float delta) {
		float maxAlga = algas().map(Alga::getRight).reduce(Float::max).orElse(0f);
		if (maxAlga < Gdx.graphics.getWidth() / 2) {
			if (Math.random() < 0.02) {
				Alga a = new Alga();
				a.setAlign(Align.bottom);
				a.setScale((float) (Math.random() + 0.5));
				a.setX(Gdx.graphics.getWidth());
				a.setY((float) Math.random() * 20);
				if (a.getScaleY() < 1) {
					algaGroupBack.addActor(a);
				} else {
					algaGroupFront.addActor(a);
				}
				
			}
		}
	}
	
	Stream<Alga> algas() {
		return Stream.concat(groupToStream(algaGroupBack), groupToStream(algaGroupFront));
	}
	
	Stream<Alga> groupToStream(Group g) {
		var children = g.getChildren();
		var iterator = children.iterator();
		return Stream.generate(iterator::next)
				.limit(children.size)
				.map(act -> (Alga) act);
	}
}
