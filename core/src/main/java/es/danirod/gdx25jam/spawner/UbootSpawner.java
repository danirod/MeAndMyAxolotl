package es.danirod.gdx25jam.spawner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import es.danirod.gdx25jam.JamGame;
import es.danirod.gdx25jam.actions.CommonActions;

public class UbootSpawner extends Actor {

	/** The group where the u-boot will be placed. */
	Group background;
	
	/** The submarine itself. */
	Actor uboot;
	
	public UbootSpawner(Group background) {
		this.background = background;
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		if (!ubootIsSpawned()) {
			trySpawnUboot();
		}
	}
	
	public boolean ubootIsSpawned() {
		return uboot != null && uboot.getParent() != null && uboot.getParent().equals(background);
	}
	
	public void trySpawnUboot() {
		if (!MathUtils.randomBoolean(0.003f)) {
			return;
		}
		uboot = new Image(JamGame.assets.get("uboot.png", Texture.class));
		uboot.setX(MathUtils.random(0, Gdx.graphics.getWidth() / 2));
		uboot.setY(MathUtils.random(100, Gdx.graphics.getHeight() - 100));
		uboot.getColor().a = 0;
		Action animation = Actions.sequence(
				Actions.delay(MathUtils.random(1f, 2f)),
				CommonActions.farSwim(Gdx.graphics.getWidth() / 2, 6f),
				Actions.removeActor()
		);
		uboot.addAction(animation);
		background.addActor(uboot);
	}
}
