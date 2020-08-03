/*
The MIT License

Copyright (c) 2014 kong <tengames.inc@gmail.com>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package com.tengames.jerrystealcheese.objects;

import woodyx.basicapi.accessor.SpriteAccessor;
import woodyx.basicapi.physics.ObjectModel;
import woodyx.basicapi.sound.SoundManager;
import woodyx.basicapi.sprite.ObjectSprite;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Linear;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.tengames.jerrystealcheese.interfaces.GlobalVariables;
import com.tengames.jerrystealcheese.main.Assets;

public class Jerry extends ObjectSprite {
	public static final byte STATE_STAND = 1;
	public static final byte STATE_RUN = 2;
	public static final byte STATE_JUMP = 3;
	private static final short DAMPING = 90;
	private static final float FORCE = 1.8f;
	private static final byte JUMP = 60;
	private static final byte IMBA = 3;

	private Animation aniStand, aniRun, aniJump, aniStandF, aniRunF, aniJumpF;
	private ObjectModel model;
	private TweenManager tween;
	private float stateTime, deltaTime, sleepJump, countImba, fcr;
	private int hp;
	private byte state;
	private boolean isLeft, isHurt, isVisible, canDes;

	public Jerry(World world, Vector2 position, Vector2 size, int hp) {
		super(position.x, position.y, size.x, size.y);

		// init params
		this.hp = hp;
		state = STATE_STAND;
		fcr = FORCE;
		stateTime = 0;
		sleepJump = 0;
		countImba = -1;
		deltaTime = 0.01f;
		isLeft = true;
		isHurt = false;
		isVisible = true;
		canDes = true;

		this.setOriginCenter(this);

		// create tween
		tween = new TweenManager();

		// create model
		model = new ObjectModel(world, ObjectModel.DYNAMIC, ObjectModel.CIRCLE,
				new Vector2(this.getWidth(), this.getHeight()), new Vector2(), this.getWidth() / 2, this.getPosition(),
				0, 1, 0.5f, 0.1f, GlobalVariables.CATEGORY_SCENERY, GlobalVariables.MASK_SCENERY, "jerry");
		// fix rotation
		model.getBody().setFixedRotation(true);

		// create animations
		aniStand = new Animation(0.2f, Assets.taObjects.findRegion("jerry-stand-1"),
				Assets.taObjects.findRegion("jerry-stand-2"), Assets.taObjects.findRegion("jerry-stand-3"));

		TextureRegion jerryStand1 = new TextureRegion(Assets.taObjects.findRegion("jerry-stand-1"));
		TextureRegion jerryStand2 = new TextureRegion(Assets.taObjects.findRegion("jerry-stand-2"));
		TextureRegion jerryStand3 = new TextureRegion(Assets.taObjects.findRegion("jerry-stand-3"));
		jerryStand1.flip(true, false);
		jerryStand2.flip(true, false);
		jerryStand3.flip(true, false);

		aniStandF = new Animation(0.2f, jerryStand1, jerryStand2, jerryStand3);

		aniRun = new Animation(0.1f, Assets.taObjects.findRegion("jerry-run-1"),
				Assets.taObjects.findRegion("jerry-run-2"), Assets.taObjects.findRegion("jerry-run-3"),
				Assets.taObjects.findRegion("jerry-run-4"), Assets.taObjects.findRegion("jerry-run-5"),
				Assets.taObjects.findRegion("jerry-run-6"), Assets.taObjects.findRegion("jerry-run-7"),
				Assets.taObjects.findRegion("jerry-run-8"), Assets.taObjects.findRegion("jerry-run-9"),
				Assets.taObjects.findRegion("jerry-run-10"), Assets.taObjects.findRegion("jerry-run-11"));

		TextureRegion jerryRun1 = new TextureRegion(Assets.taObjects.findRegion("jerry-run-1"));
		TextureRegion jerryRun2 = new TextureRegion(Assets.taObjects.findRegion("jerry-run-2"));
		TextureRegion jerryRun3 = new TextureRegion(Assets.taObjects.findRegion("jerry-run-3"));
		TextureRegion jerryRun4 = new TextureRegion(Assets.taObjects.findRegion("jerry-run-4"));
		TextureRegion jerryRun5 = new TextureRegion(Assets.taObjects.findRegion("jerry-run-5"));
		TextureRegion jerryRun6 = new TextureRegion(Assets.taObjects.findRegion("jerry-run-6"));
		TextureRegion jerryRun7 = new TextureRegion(Assets.taObjects.findRegion("jerry-run-7"));
		TextureRegion jerryRun8 = new TextureRegion(Assets.taObjects.findRegion("jerry-run-8"));
		TextureRegion jerryRun9 = new TextureRegion(Assets.taObjects.findRegion("jerry-run-9"));
		TextureRegion jerryRun10 = new TextureRegion(Assets.taObjects.findRegion("jerry-run-10"));
		TextureRegion jerryRun11 = new TextureRegion(Assets.taObjects.findRegion("jerry-run-11"));
		jerryRun1.flip(true, false);
		jerryRun2.flip(true, false);
		jerryRun3.flip(true, false);
		jerryRun4.flip(true, false);
		jerryRun5.flip(true, false);
		jerryRun6.flip(true, false);
		jerryRun7.flip(true, false);
		jerryRun8.flip(true, false);
		jerryRun9.flip(true, false);
		jerryRun10.flip(true, false);
		jerryRun11.flip(true, false);

		aniRunF = new Animation(0.1f, jerryRun1, jerryRun2, jerryRun3, jerryRun4, jerryRun5, jerryRun6, jerryRun7,
				jerryRun8, jerryRun9, jerryRun10, jerryRun11);

		aniJump = new Animation(0.2f, Assets.taObjects.findRegion("jerry-jump-1"),
				Assets.taObjects.findRegion("jerry-jump-2"));

		TextureRegion jerryJump1 = new TextureRegion(Assets.taObjects.findRegion("jerry-jump-1"));
		TextureRegion jerryJump2 = new TextureRegion(Assets.taObjects.findRegion("jerry-jump-2"));
		jerryJump1.flip(true, false);
		jerryJump2.flip(true, false);

		aniJumpF = new Animation(0.2f, jerryJump1, jerryJump2);
	}

	public void runLeft() {
		// change flag
		isLeft = true;
		if (state == STATE_STAND) {
			state = STATE_RUN;
		}
		// run left
		model.getBody().applyLinearImpulse(new Vector2(-fcr * deltaTime, 0), model.getBody().getWorldCenter());
		model.getBody().setLinearDamping(DAMPING * deltaTime);
	}

	public void stopRun() {
		if (state == STATE_RUN) {
			state = STATE_STAND;
		}
	}

	public void runRight() {
		// change flag
		isLeft = false;
		if (state == STATE_STAND) {
			state = STATE_RUN;
		}
		// run right
		model.getBody().applyLinearImpulse(new Vector2(fcr * deltaTime, 0), model.getBody().getWorldCenter());
		model.getBody().setLinearDamping(DAMPING * deltaTime);
	}

	public void jump() {
		if ((state == STATE_STAND || state == STATE_RUN) && sleepJump > 0.2f) {
			// play sound
			SoundManager.playSound(Assets.soJump);
			state = STATE_JUMP;
			sleepJump = -1;
			model.getBody().applyLinearImpulse(new Vector2(0, JUMP * deltaTime), model.getBody().getWorldCenter());
		}
	}

	public void update(float deltaTime) {
		// get deltaTime
		this.deltaTime = deltaTime;
		// update tween
		tween.update(deltaTime);
		// update stateTime
		stateTime += deltaTime;
		// update sleep time
		if (sleepJump >= 0)
			sleepJump += deltaTime;
		// update follow model
		this.updateFollowModel(model);
		// set force
		if (state == STATE_JUMP)
			fcr = FORCE * 0.3f;
		else
			fcr = FORCE;
		// change state
		if (model.getBody().getLinearVelocity().y < -1) {
			setState(STATE_JUMP);
		}
		// update hurt
		if (isHurt) {
			if (canDes) {
				// decrease hp
				hp--;
				// reset imba
				countImba = 0;
				// create new tween
				Tween.to(this, SpriteAccessor.OPACITY, 0.5f).target(0.5f).ease(Linear.INOUT).repeatYoyo(20, 0)
						.start(tween);
				// jump
				if (isLeft)
					model.getBody().applyLinearImpulse(new Vector2(JUMP * 0.5f * deltaTime, JUMP * 0.5f * deltaTime),
							model.getBody().getWorldCenter());
				else
					model.getBody().applyLinearImpulse(new Vector2(-JUMP * 0.5f * deltaTime, JUMP * 0.5f * deltaTime),
							model.getBody().getWorldCenter());
				// change stage
				setState(STATE_JUMP);
				// play sound
				SoundManager.playSound(Assets.soHurt);
				// turn off flag
				canDes = false;
			}
		}
		// state imba
		if (countImba >= 0)
			countImba += deltaTime;
		if (countImba >= IMBA) {
			// reset tween
			tween.killAll();
			// change flag
			canDes = true;
			isHurt = false;
			// turn off count imba
			countImba = -1;
		}
	}

	public void render(SpriteBatch batch) {
		TextureRegion keyFrame = null;
		switch (state) {
		case STATE_STAND:
			if (isLeft)
				keyFrame = aniStand.getKeyFrame(stateTime, true);
			else {
				keyFrame = aniStandF.getKeyFrame(stateTime, true);
			}
			break;
		case STATE_RUN:
			if (isLeft)
				keyFrame = aniRun.getKeyFrame(stateTime, true);
			else {
				keyFrame = aniRunF.getKeyFrame(stateTime, true);
			}
			break;
		case STATE_JUMP:
			if (isLeft)
				keyFrame = aniJump.getKeyFrame(stateTime, false);
			else
				keyFrame = aniJumpF.getKeyFrame(stateTime, false);
			break;
		default:
			break;
		}
		// render sprite
		if (keyFrame != null && hp > 0 && isVisible) {
			this.renderSprite(keyFrame).draw(batch);
		}
	}

	public void resetSleepJump() {
		if (sleepJump == -1)
			sleepJump = 0;
	}

	public void setState(byte state) {
		if (this.state != state)
			this.state = state;
	}

	public byte getState() {
		return state;
	}

	public int getHp() {
		return hp;
	}

	public void setHurt() {
		if (!this.isHurt)
			this.isHurt = true;
	}

	public void setVisible() {
		if (isVisible) {
			// deactive body
			this.isVisible = false;
		}
	}

}
