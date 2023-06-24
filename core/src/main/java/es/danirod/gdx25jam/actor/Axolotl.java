package es.danirod.gdx25jam.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Pools;

import es.danirod.gdx25jam.GameScreen;
import es.danirod.gdx25jam.GameState;
import es.danirod.gdx25jam.JamGame;
import es.danirod.gdx25jam.Utils;
import es.danirod.gdx25jam.actions.CommonActions;
import es.danirod.gdx25jam.actions.ShakeAction;
import es.danirod.gdx25jam.actor.AnimatedImage.Orientation;
import es.danirod.gdx25jam.spawner.BubbleSpawner;

public class Axolotl extends Group {
	public static final float TIME_TO_HEAL = 40f;
	
	/** Current health level. */
	int health = 5;

	/** Current amount of stun combo. (If you hit multiple trash, you add stuns.) */
	int stunnedCombo = 0;

	/** Seconds until a body part grows again. */
	float timeToHeal = TIME_TO_HEAL;

	/** The star that is rendered if the axolotl is currently in stun. */
	Actor stunnedStar;

	/** Each body part is animated separately. */
	Actor body, patLB, patRB, patLF, patRF;

	/** An empty actor just for the shake of showing it when debugging. */
	Actor collisionBox;

	/** The actor used to display a countdown until the arm is regenerated. */
	RegrowTimer timer = new RegrowTimer(this);

	public Axolotl(Group bubblesGroup) {
		addActor(new BubbleSpawner(bubblesGroup, this, 82, 7));
		
		patLF = bodyPart("axolotl/lf.png", 49, 27, 24, 1);
		patLB = bodyPart("axolotl/lb.png", 22, 24, 17, 1);
		body = bodyTrunk();
		patRF = bodyPart("axolotl/rf.png", 55, 2, 23, 11);
		patRB = bodyPart("axolotl/rb.png", 22, 2, 17, 13);

		timer = new RegrowTimer(this);
		timer.setSize(20, 20);
		timer.setPosition(getX(), getY());
		timer.setColor(Color.YELLOW);
		timer.setVisible(false);
		addActor(timer);

		collisionBox = new Actor();
		collisionBox.setBounds(80, 14, 20, 20);
		addActor(collisionBox);

		stunnedStar = new Image(JamGame.assets.get("star.png", Texture.class));
		stunnedStar.setPosition(90, 35);
		stunnedStar.addAction(Actions.forever(CommonActions.horizontalWave(20f, 0.5f)));
		stunnedStar.setVisible(false);
		addActor(stunnedStar);

		setOrigin(83, 11);
	}

	private Actor bodyTrunk() {
		Actor body = new AnimatedImage(Orientation.VERTICAL, JamGame.assets.get("axolotl/body.png", Texture.class),
				0.5f) {
		};
		body.setPosition(6, 11);
		addActor(body);
		return body;
	}

	private Actor bodyPart(String texture, float x, float y, float originX, float originY) {
		Actor act = new Image(JamGame.assets.get(texture, Texture.class));
		act.setPosition(x, y);
		act.setOrigin(originX, originY);
		act.addAction(CommonActions.gravitySwimArm());
		addActor(act);
		return act;
	}

	/**
	 * Triggers a stun: the axolotl shakes, sees the starts and is unable to move
	 * for a couple of seconds.
	 */
	public void stun() {
		var startStun = Actions.run(() -> {
			stunnedCombo++;
			stunnedStar.setVisible(true);
			addAction(ShakeAction.shake(0.3f));
		});
		var holdUp = Actions.delay(3f);
		var stopStun = Actions.run(() -> {
			stunnedCombo--;
			stunnedStar.setVisible(stunnedCombo > 0);
		});
		addAction(Actions.sequence(startStun, holdUp, stopStun));
	}

	/** Whether the axolotl is currently under a stun effect. */
	boolean isStunned() {
		return stunnedCombo > 0;
	}

	/** Current health level. */
	public int getHealth() {
		return health;
	}

	/** Updates the health level, also updates the visibility of arms. */
	public void setHealth(int health) {
		// Sets the health.
		this.health = MathUtils.clamp(health, 0, 5);
		this.health = health;

		// Updates visibilities.
		patLB.setVisible(health >= 2);
		patLF.setVisible(health >= 3);
		patRB.setVisible(health >= 4);
		patRF.setVisible(health >= 5);

		// Which arm will grow next.
		if (health == 1) {
			timer.setPosition(patLB.getX(), patLB.getY() + 20f);
		} else if (health == 2) {
			timer.setPosition(patLF.getX(), patLF.getY() + 20f);
		} else if (health == 3) {
			timer.setPosition(patRB.getX(), patRB.getY() - 20f);
		} else if (health == 4) {
			timer.setPosition(patRF.getX(), patRF.getY() - 20f);
		}
		setVisible(health > 0);
		timer.setVisible(health < 5);
	}

	/** Shortcut to increase the health. */
	public void heal() {
		setHealth(getHealth() + 1);
	}

	/** Shortcut to decrease the health. */
	public void hit() {
		timeToHeal = TIME_TO_HEAL;
		setHealth(getHealth() - 1);
	}

	/** Does the axolotl have a right body? */
	boolean hasRightBody() {
		return health > 3;
	}

	/** Does the axolotl have a left body? */
	boolean hasLeftBody() {
		return health > 1;
	}

	/** Is the axolotl ded? */
	public boolean isAlive() {
		return health > 0;
	}

	/** Will check if the other actor is hitting my axolotl. */
	public boolean isColliding(Actor other) {
		Rectangle myBounds = null, yourBounds = null;
		try {
			myBounds = Pools.obtain(Rectangle.class);
			yourBounds = Pools.obtain(Rectangle.class);
			Utils.transferStageBounds(collisionBox, myBounds);
			Utils.transferStageBounds(other, yourBounds);
			return myBounds.overlaps(yourBounds) || yourBounds.overlaps(myBounds);
		} finally {
			Pools.free(myBounds);
			Pools.free(yourBounds);
		}
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if (!isStunned() && isAlive()) {
			swimUpToMouse(delta);
		}
		if (health < 5) {
			checkHealing(delta);
		}
	}

	/** Arms grow over time. */
	void checkHealing(float delta) {
		timeToHeal -= delta;
		if (timeToHeal < 0) {
			heal();
			timeToHeal = TIME_TO_HEAL;
		}
	}

	/** Make the axolotl swim. */
	void swimUpToMouse(float delta) {
		Vector2 mouse = null;
		Vector2 axolotl = null;
		Vector2 distance = null;
		try {
			mouse = Pools.obtain(Vector2.class);
			axolotl = Pools.obtain(Vector2.class);
			distance = Pools.obtain(Vector2.class);

			// Get the position of the mouse as a Vector.
			mouse.set(Gdx.input.getX(), Gdx.input.getY());
			getStage().screenToStageCoordinates(mouse);
			if (mouse.x < getX() + body.getRight()) {
				mouse.x = getX() + body.getRight();
			}

			// Then start calculating a vector that starts from axolotl to the mouse.
			axolotl.set(getX() + getOriginX(), getY() + getOriginY());
			distance.set(mouse).sub(axolotl);

			// Move the axolotl so that it tracks the mouse.
			float speed = 500f + 30 * GameState.instance.score;
			distance.nor().scl(speed * delta);
			moveBy(0, distance.y);

			// Clamp the vertical position so that it never goes to the floor or above sea
			// level.
			if (getY() < 40 - getOriginY()) {
				setY(40 - getOriginY());
			}
			if (getY() > getStage().getViewport().getWorldHeight() - getHeight() + getOriginY() - 120) {
				setY(getStage().getViewport().getWorldHeight() - getHeight() + getOriginY() - 120);
			}

			// Rotate the axolotl so that it looks to the mouse, more realistic. The thing
			// is that we have to skew the rotations if the arms are not present, so the
			// angle is moved to negative numbers so that it is more symmetric.
			float angle = (int) distance.angleDeg();
			if (angle > 180) {
				angle -= 360;
			}
			if (!hasRightBody() && angle < 0) {
				angle *= 0.1f;
			}
			if (!hasLeftBody() && angle > 0) {
				angle *= 0.1f;
			}
			setRotation(angle);
		} finally {
			Pools.free(mouse);
			Pools.free(axolotl);
			Pools.free(distance);
		}
	}
}