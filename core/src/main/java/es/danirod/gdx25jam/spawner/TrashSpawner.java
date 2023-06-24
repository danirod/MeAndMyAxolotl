package es.danirod.gdx25jam.spawner;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import es.danirod.gdx25jam.GameScreen;
import es.danirod.gdx25jam.GameState;
import es.danirod.gdx25jam.JamGame;
import es.danirod.gdx25jam.actor.Axolotl;
import es.danirod.gdx25jam.actor.Rubbish;

public class TrashSpawner extends Actor {

	Group trash;
	
	Axolotl player;
		
	public TrashSpawner(Group trash, Axolotl player) {
		this.trash = trash;
		this.player = player;
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		if (shouldSpawnTrash()) {
			Rubbish can = new Rubbish(player);
			float vertical = MathUtils.random(100, getStage().getViewport().getWorldHeight() - 100);
			can.setPosition(getStage().getViewport().getWorldWidth() + can.getWidth(), vertical);
			trash.addActor(can);
		}
	}
	
	boolean shouldSpawnTrash() {
		// The amount of trash is limited by the current score.
		if (trash.getChildren().size >= GameState.instance.score) {
			return false;
		}
		// Low chance since this is evaluated each frame.
		return MathUtils.randomBoolean(0.03f);
	}
	
}
