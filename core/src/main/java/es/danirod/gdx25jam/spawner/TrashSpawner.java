package es.danirod.gdx25jam.spawner;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import es.danirod.gdx25jam.GameScreen;
import es.danirod.gdx25jam.JamGame;
import es.danirod.gdx25jam.actor.Axolotl;
import es.danirod.gdx25jam.actor.Rubbish;

public class TrashSpawner extends Actor {

	Group trash;
	
	Axolotl player;
	
	GameScreen screen;
	
	public TrashSpawner(Group trash, Axolotl player, GameScreen screen) {
		this.trash = trash;
		this.player = player;
		this.screen = screen;
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		if (shouldSpawnTrash()) {
			Rubbish can = new Rubbish(player);
			float vertical = MathUtils.random(100, Gdx.graphics.getHeight() - 100);
			can.setPosition(Gdx.graphics.getWidth() + can.getWidth(), vertical);
			trash.addActor(can);
		}
	}
	
	boolean shouldSpawnTrash() {
		// The amount of trash is limited by the current score.
		if (trash.getChildren().size >= screen.getScore()) {
			return false;
		}
		// Low chance since this is evaluated each frame.
		return MathUtils.randomBoolean(0.03f);
	}
	
}
