package es.danirod.gdx25jam.spawner;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import es.danirod.gdx25jam.GameScreen;
import es.danirod.gdx25jam.JamGame;
import es.danirod.gdx25jam.actor.Axolotl;
import es.danirod.gdx25jam.actor.TrashCan;

public class TrashSpawner extends Actor {

	Group trash;
	
	Axolotl player;
	
	Random rand = new Random();
	
	GameScreen screen;
	
	public TrashSpawner(Group trash, Axolotl player, GameScreen screen) {
		this.trash = trash;
		this.player = player;
		this.screen = screen;
	}
	
	@Override
	public void act(float delta) {
		int score = screen.getScore();
		if (trash.getChildren().size < score && Math.random() < 0.02f) {
			Texture t;
			if (Math.random() < 0.5f) {
				t = JamGame.assets.get("trash1.png", Texture.class);
			} else {
				t = JamGame.assets.get("trash2.png", Texture.class);
			}
			TrashCan can = new TrashCan(t, player);
			can.setPosition(Gdx.graphics.getWidth() + can.getWidth(), rand.nextInt(100, Gdx.graphics.getHeight() - 100));
			trash.addActor(can);
		}
	}
	
}
