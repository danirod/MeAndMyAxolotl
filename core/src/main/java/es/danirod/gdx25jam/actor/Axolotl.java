package es.danirod.gdx25jam.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Pools;

import es.danirod.gdx25jam.GameScreen;
import es.danirod.gdx25jam.JamGame;
import es.danirod.gdx25jam.actions.CommonActions;
import es.danirod.gdx25jam.actions.ShakeAction;
import es.danirod.gdx25jam.actor.AnimatedImage.Orientation;

public class Axolotl extends Group {

    int health = 4;
	
	int stunnedCombo = 0;
	
	/** Seconds until a body part grows again. */
	float timeToHeal = 40f;
	
	Actor stunnedStar;
	
	Actor body, patLB, patRB, patLF, patRF;
	
	RegrowTimer timer = new RegrowTimer(this);
	
	public int getHealth() {
		return health;
	}
	
	public void setHealth(int health) {
		if (health < 0)
			health = 0;
		if (health > 4)
			health = 4;
		this.health = health;
		patLB.setVisible(health >= 1);
		patLF.setVisible(health >= 2);
		patRB.setVisible(health >= 3);
		patRF.setVisible(health >= 4);
		
		if (health == 0) {
			timer.setPosition(patLB.getX(), patLB.getY() + 20f);
		} else if (health == 1) {
			timer.setPosition(patLF.getX(), patLF.getY() + 20f);
		} else if (health == 2) {
			timer.setPosition(patRB.getX(), patRB.getY() - 20f);
		} else if (health == 3) {
			timer.setPosition(patRF.getX(), patRF.getY() - 20f);
		}
		timer.setVisible(health < 4);
	}
	
	public void hit() {
		timeToHeal = 40f;
		setHealth(getHealth() - 1);
	}
	
	public void heal() {
		setHealth(getHealth() + 1);
	}
	
	boolean hasRightBody() {
		return health >= 3;
	}
	
	boolean hasLeftBody() {
		return health >= 1;
	}
	
    public Axolotl() {
    	patLF = new Image(JamGame.assets.get("axolotl/lf.png", Texture.class));
    	patLF.setPosition(49, 27);
    	patLF.addAction(CommonActions.gravitySwimArm());
    	patLF.setOrigin(24, 1);
    	addActor(patLF);
    	
    	patLB = new Image(JamGame.assets.get("axolotl/lb.png", Texture.class));
    	patLB.setPosition(22, 24);
    	patLB.setOrigin(17, 1);
    	patLB.addAction(CommonActions.gravitySwimArm());
    	addActor(patLB);
    	
    	body = new AnimatedImage(Orientation.VERTICAL, JamGame.assets.get("axolotl/body.png", Texture.class), 0.5f) { };
    	body.setPosition(6, 11);
    	addActor(body);
    	
    	patRF = new Image(JamGame.assets.get("axolotl/rf.png", Texture.class));
    	patRF.setPosition(55, 2);
    	patRF.setOrigin(23, 11);
    	patRF.addAction(CommonActions.gravitySwimArm());
    	addActor(patRF);
    	
    	patRB = new Image(JamGame.assets.get("axolotl/rb.png", Texture.class));
    	patRB.setPosition(22, 2);
    	patRB.setOrigin(17, 13);
    	patRB.addAction(CommonActions.gravitySwimArm());
    	addActor(patRB);
    	
    	timer = new RegrowTimer(this);
    	timer.setSize(20, 20);
    	timer.setPosition(getX(), getY());
    	timer.setColor(Color.YELLOW);
    	addActor(timer);
    	
        // setScale(2f);
        // setOrigin(OFFSET_X, OFFSET_Y);
        setOrigin(91, 19);
    }
    
    public void stun() {
    	addAction(Actions.sequence(
    			Actions.run(() -> startStunEffect()),
    			Actions.delay(3f),
    			Actions.run(() -> stopStunEffect())
		));
    }
    
    void startStunEffect() {
    	if (stunnedCombo++ == 0) {
    		stunnedStar = new Image(JamGame.assets.get("star.png", Texture.class));
    		stunnedStar.setPosition(getX() + getOriginX(), getY() + body.getHeight());
    		stunnedStar.addAction(
        			Actions.forever(
    					Actions.sequence(
    						Actions.moveBy(-2 * stunnedStar.getWidth(), 0, 0.1f),
    						Actions.moveBy(4 * stunnedStar.getWidth(), 0, 0.2f),
    						Actions.moveBy(-2 * stunnedStar.getWidth(), 0, 0.1f)
    					)
    				)
    		);
        	getStage().addActor(stunnedStar);
    	}
    	addAction(ShakeAction.shake(0.3f));
    }
    
    void stopStunEffect() {
    	if (--stunnedCombo == 0) {
    		stunnedStar.remove();
    	}
    }
    
    @Override
    public void act(float delta) {
    	super.act(delta);
    	if (stunnedCombo == 0) {
    		swimUpToMouse(delta);
    	}
    	if (health < 4) {
    		checkHealing(delta);
    	}
    }
    
    void checkHealing(float delta) {    	
		timeToHeal -= delta;
		if (timeToHeal < 0) {
			timeToHeal = 40f;
			heal();
		}
    }
    
    void getHeadBox(Rectangle out) {
    	float cx = getX() + getOriginX(), cy = getY() + getOriginY();
    	out.set(cx - 15, cy - 15, 30, 30);
    }
    
    void swimUpToMouse(float delta) {
    	Vector2 mouse = null;
    	Vector2 axolotl = null;
    	Vector2 distance = null;
    	try {
    		mouse = Pools.obtain(Vector2.class);
    		axolotl = Pools.obtain(Vector2.class);
    		distance = Pools.obtain(Vector2.class);
    		mouse.set(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
    		axolotl.set(getX() + getOriginX(), getY() + getOriginY());
    		
    		if (mouse.x < body.getRight() + 100) {
    			mouse.x = body.getRight() + 100;
    		}
    		
    		distance.set(mouse).sub(axolotl);
    		if (distance.len() < 10) {
    			return;
    		}
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
    		if (angle >= 90 || angle <= -90) {
    			setScaleX(-1);
    		} else {
    			setScaleX(1);
    		}
    		float speed = 500f + 30 * GameScreen.INSTANCE.getScore();
    		distance.nor().scl(speed * delta);
    		moveBy(0, distance.y);
    		if (getY() < 40 - getOriginY()) {
    			setY(40 - getOriginY());
    		}
    		if (getY() > Gdx.graphics.getHeight() - getHeight() + getOriginY() - 120) {
    			setY(Gdx.graphics.getHeight() - getHeight() + getOriginY() - 120);
    		}
    	} finally {
    		Pools.free(mouse);
    		Pools.free(axolotl);
    		Pools.free(distance);
    	}
    }
}
