package es.danirod.gdx25jam.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Disposable;

import es.danirod.gdx25jam.JamGame;

public class PendingEggs extends Group implements Disposable {

	private static final int TOTAL = 1;
	
	Image icon;
	
	Label display;
	
	int pending = TOTAL;
	
	ShapeRenderer renderer;
	
	public PendingEggs() {
		this.renderer = new ShapeRenderer();
		
		Texture egg = JamGame.assets.get("egg.png", Texture.class);
		TextureRegion[][] eggs = TextureRegion.split(egg, egg.getWidth() / 2, egg.getHeight() / 2);
		this.icon = new Image(eggs[0][0]);
		addActor(this.icon);
		
		this.display = new Label("", JamGame.labelStyle);
		this.display.setPosition(egg.getWidth() * 0.5f, 4);
		addActor(this.display);
		updateDisplay();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.end();

		Gdx.gl.glEnable(GL30.GL_BLEND);
		Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		this.renderer.setProjectionMatrix(batch.getProjectionMatrix());
		this.renderer.setTransformMatrix(batch.getTransformMatrix());
		this.renderer.begin(ShapeType.Filled);
		this.renderer.setColor(0, 0, 0, 0.5f * parentAlpha);
		this.renderer.rect(getX(), getY(), getWidth(), getHeight());
		this.renderer.end();
		Gdx.gl.glDisable(GL30.GL_BLEND);
		
		batch.begin();
		super.draw(batch, parentAlpha);
	}
	
	public boolean update(int picked) {
		this.pending = TOTAL - picked;
		updateDisplay();
		return this.pending == 0;
	}
	
	void updateDisplay() {
		CharSequence text = " PENDING: " + pending + "  ";
		var layout = new GlyphLayout(display.getStyle().font, text);
		display.setText(text);
		display.setHeight(layout.height);
		display.setWidth(layout.width);
		setWidth(icon.getWidth() + display.getWidth());
		setHeight(Math.max(icon.getHeight(), display.getHeight()));
	}

	@Override
	public void dispose() {
		this.renderer.dispose();
	}
}
