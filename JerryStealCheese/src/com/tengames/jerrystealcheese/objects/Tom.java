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

import woodyx.basicapi.physics.ObjectModel;
import woodyx.basicapi.sprite.ObjectSprite;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.tengames.jerrystealcheese.interfaces.GlobalVariables;
import com.tengames.jerrystealcheese.main.Assets;

public class Tom extends ObjectSprite {
	private static final byte FORCE = 35;
	private ObjectModel model;
	private Vector2 oldPos, target;
	private float stateTime;
	private boolean isLeft, canTurnRight, initLeft;

	public Tom(World world, Vector2 position, Vector2 target, Vector2 size, String user) {
		super(position.x, position.y, size.x, size.y);
		this.target = target;
		stateTime = 0;
		oldPos = position;

		this.setOriginCenter(this);

		// detect position
		if (target.x <= position.x) {
			initLeft = true;
			isLeft = true;
			canTurnRight = false;
		} else {
			initLeft = false;
			isLeft = false;
			canTurnRight = true;
		}

		// create model
		model = new ObjectModel(world, ObjectModel.KINEMATIC, ObjectModel.CIRCLE,
				new Vector2(this.getWidth(), this.getHeight()), new Vector2(), this.getWidth() / 3, this.getPosition(),
				0, 5, 0.2f, 0.1f, GlobalVariables.CATEGORY_SCENERY, GlobalVariables.MASK_SCENERY, user);
		model.getBody().getFixtureList().get(0).setSensor(true);
		// fix rotation
		model.getBody().setFixedRotation(true);

	}

	public ObjectModel getModel() {
		return model;
	}

	public void update(float deltaTime) {
		// update stateTime
		stateTime += deltaTime;
		// update follow model
		this.updateFollowModel(model);
		// move
		if (initLeft) {
			// turn left
			if (this.getX() >= target.x && !canTurnRight) {
				if (!isLeft)
					isLeft = true;
				model.getBody().setLinearVelocity(new Vector2(-FORCE * deltaTime, 0));
			} else {
				if (!canTurnRight)
					canTurnRight = true;
				if (isLeft)
					isLeft = false;
				if (this.getX() < oldPos.x) {
					model.getBody().setLinearVelocity(new Vector2(FORCE * deltaTime, 0));
				} else {
					if (canTurnRight)
						canTurnRight = false;
				}
			}
		} else {
			// turn right
			if (this.getX() <= target.x && canTurnRight) {
				if (isLeft)
					isLeft = false;
				model.getBody().setLinearVelocity(new Vector2(FORCE * deltaTime, 0));
			} else {
				if (canTurnRight)
					canTurnRight = false;
				if (!isLeft)
					isLeft = true;
				if (this.getX() >= oldPos.x) {
					model.getBody().setLinearVelocity(new Vector2(-FORCE * deltaTime, 0));
				} else {
					if (!canTurnRight)
						canTurnRight = true;
				}
			}
		}
		model.getBody().setLinearDamping(100 * deltaTime);
	}

	public void render(SpriteBatch batch) {
		TextureRegion keyFrame = null;
		if (isLeft) {
			keyFrame = Assets.aniTomF.getKeyFrame(stateTime, true);
		} else {
			keyFrame = Assets.aniTom.getKeyFrame(stateTime, true);
		}
		if (keyFrame != null)
			this.renderSprite(keyFrame).draw(batch);
	}

}
