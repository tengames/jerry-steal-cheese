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
import woodyx.basicapi.physics.ObjectsJoint;
import woodyx.basicapi.sound.SoundManager;
import woodyx.basicapi.sprite.ObjectSprite;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.TweenPaths;
import aurelienribon.tweenengine.equations.Elastic;
import aurelienribon.tweenengine.equations.Linear;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.tengames.jerrystealcheese.interfaces.GlobalVariables;
import com.tengames.jerrystealcheese.main.Assets;
import com.badlogic.gdx.physics.box2d.World;

public class Terran extends ObjectSprite {
	private static final byte VELOCITY = 18;

	private ObjectModel mdObject;
	private ObjectsJoint jRevol;
	private TweenManager tween;
	private String name;
	private Vector2 low, high, target;
	private int type;
	private boolean isDie, isProcDie, canRemove;

	public Terran(World world, ObjectModel ground, int type, Vector2 position, Vector2 target, Vector2 size,
			float angle, String user) {
		super(position.x, position.y);
		this.type = type;
		this.target = target;
		this.isDie = false;
		this.isProcDie = false;
		this.canRemove = false;

		// set texture region for sprites
		switch (type) {
		case IconModel.BOOK_1:
			name = "book-1";
			break;
		case IconModel.BOOK_2:
			name = "book-2";
			break;
		case IconModel.BOOK_3:
			name = "book-3";
			break;
		case IconModel.BOOK_4:
			name = "book-4";
			break;
		case IconModel.BOOK_5:
			name = "book-5";
			break;
		case IconModel.BRIDGE_1:
			name = "bridge-1";
			break;
		case IconModel.BRIDGE_2:
			name = "bridge-2";
			break;
		case IconModel.BRIDGE_3:
			name = "bridge-3";
			break;
		case IconModel.BRIDGE_4:
			name = "bridge-4";
			break;
		case IconModel.BUTTER:
			name = "butter";
			// create tween
			tween = new TweenManager();
			Tween.to(this, SpriteAccessor.SCALE_XY, 1f).ease(Linear.INOUT).target(0.9f, 0.9f)
					.path(TweenPaths.catmullRom).repeatYoyo(-1, 0).start(tween);
			break;
		case IconModel.GROUND_1:
			name = "ground-1";
			break;
		case IconModel.GROUND_2:
			name = "ground-2";
			break;
		case IconModel.GROUND_3:
			name = "ground-3";
			break;
		case IconModel.HOME:
			name = "home";
			break;
		case IconModel.RULER:
			name = "ruler";
			break;
		case IconModel.TERRAN_1:
			name = "terran-1";
			break;
		case IconModel.TERRAN_2:
			name = "terran-2";
			break;
		case IconModel.TERRAN_3:
			name = "terran-3";
			break;
		case IconModel.TERRAN_4:
			name = "terran-4";
			break;
		case IconModel.TERRAN_5:
			name = "terran-5";
			break;
		case IconModel.TERRAN_6:
			name = "terran-6";
			break;
		case IconModel.TERRAN_7:
			name = "terran-7";
			break;
		case IconModel.TERRAN_8:
			name = "terran-8";
			break;
		case IconModel.TERRAN_9:
			name = "terran-9";
			break;
		case IconModel.TERRAN_10:
			name = "terran-10";
			break;
		case IconModel.TERRAN_11:
			name = "terran-11";
			break;
		case IconModel.TRAP_1:
			name = "trap-1";
			break;
		case IconModel.TRAP_2:
			name = "trap-2";
			break;
		case IconModel.WOOD_1:
			name = "wood-1";
			break;
		case IconModel.WOOD_2:
			name = "wood-2";
			break;
		case IconModel.WOOD_3:
			name = "wood-3";
			break;
		case IconModel.WOOD_4:
			name = "wood-4";
			break;
		case IconModel.WOOD_5:
			name = "wood-5";
			break;
		case IconModel.WOOD_6:
			name = "wood-6";
			break;
		case IconModel.WOOD_7:
			name = "wood-7";
			break;
		default:
			break;
		}

		this.setRegion(Assets.taObjects.findRegion(name));
		this.setSize(size.x, size.y);
		this.setOriginCenter(this);

		byte active = ObjectModel.KINEMATIC;

		// create models
		switch (type) {
		// special shape
		case IconModel.BRIDGE_1:
		case IconModel.BRIDGE_2:
		case IconModel.BRIDGE_3:
		case IconModel.BRIDGE_4:

		case IconModel.TERRAN_1:
		case IconModel.TERRAN_2:
		case IconModel.TERRAN_3:
		case IconModel.TERRAN_4:
		case IconModel.TERRAN_5:
		case IconModel.TERRAN_6:
		case IconModel.TERRAN_7:

		case IconModel.BOOK_1:
		case IconModel.BOOK_2:
		case IconModel.BOOK_3:
		case IconModel.BOOK_5:
			if (target.x == 0 && target.y == 0)
				active = ObjectModel.STATIC;
			mdObject = new ObjectModel(world, active, Assets.bdLoader, name, position, angle, 5, 0.3f, 0.1f,
					this.getWidth(), GlobalVariables.CATEGORY_SCENERY, GlobalVariables.MASK_SCENERY, "ground");
			break;

		// basic shape
		case IconModel.HOME:
		case IconModel.BUTTER:
			// create butter
			mdObject = new ObjectModel(world, ObjectModel.STATIC, ObjectModel.POLYGON,
					new Vector2(this.getWidth(), this.getHeight()), new Vector2(), 0, position, angle, 1, 0.3f, 0.1f,
					GlobalVariables.CATEGORY_SCENERY, GlobalVariables.MASK_SCENERY, user);
			mdObject.getBody().getFixtureList().get(0).setSensor(true);
			break;

		case IconModel.TRAP_1:
		case IconModel.TRAP_2:
			mdObject = new ObjectModel(world, ObjectModel.STATIC, ObjectModel.POLYGON,
					new Vector2(this.getWidth(), this.getHeight()), new Vector2(), 0, position, angle, 3, 0.1f, 0.1f,
					GlobalVariables.CATEGORY_SCENERY, GlobalVariables.MASK_SCENERY, user);
			mdObject.getBody().getFixtureList().get(0).setSensor(true);
			break;

		case IconModel.BOOK_4:
		case IconModel.TERRAN_8:
		case IconModel.TERRAN_9:
		case IconModel.TERRAN_10:
		case IconModel.TERRAN_11:
		case IconModel.WOOD_1:
		case IconModel.WOOD_2:
		case IconModel.WOOD_3:
		case IconModel.WOOD_4:
		case IconModel.WOOD_5:
		case IconModel.WOOD_6:
		case IconModel.WOOD_7:
			if (target.x == 0 && target.y == 0)
				active = ObjectModel.STATIC;
			mdObject = new ObjectModel(world, active, ObjectModel.POLYGON,
					new Vector2(this.getWidth(), this.getHeight()), new Vector2(), 0, position, angle, 5, 0.3f, 0.1f,
					GlobalVariables.CATEGORY_SCENERY, GlobalVariables.MASK_SCENERY, "ground");
			break;

		case IconModel.GROUND_1:
		case IconModel.GROUND_2:
		case IconModel.GROUND_3:
			mdObject = new ObjectModel(world, ObjectModel.STATIC, ObjectModel.POLYGON,
					new Vector2(this.getWidth(), this.getHeight()), new Vector2(), 0, position, angle, 10, 0.3f, 0.1f,
					GlobalVariables.CATEGORY_SCENERY, GlobalVariables.MASK_SCENERY, "ground");
			break;

		case IconModel.RULER:
			mdObject = new ObjectModel(world, ObjectModel.DYNAMIC, ObjectModel.POLYGON,
					new Vector2(this.getWidth(), this.getHeight()), new Vector2(), 0, position, angle, 5, 0.3f, 0.1f,
					GlobalVariables.CATEGORY_SCENERY, GlobalVariables.MASK_SCENERY, "ground");
			ObjectModel mdAnchor = new ObjectModel(world, ObjectModel.STATIC, ObjectModel.POLYGON, new Vector2(5, 5),
					new Vector2(), 0, new Vector2(position.x + (size.x + 5) / 2, position.y + (size.y + 5) / 2), 0, 5,
					0.1f, 0.1f, GlobalVariables.CATEGORY_SCENERY, GlobalVariables.MASK_SCENERY, "ground");
			jRevol = new ObjectsJoint(mdAnchor, mdObject, ObjectsJoint.REVOLUTE, false);
			jRevol.setAnchorA(new Vector2());
			jRevol.setAnchorB(new Vector2());
			jRevol.setAngleLimit(-45, 45);
			jRevol.createJoint(world);
			break;

		default:
			break;
		}

		if (target.x == 0) {
			// verticle move
			if (this.getY() <= target.y) {
				low = new Vector2(this.getX(), this.getY() + 1);
				high = new Vector2(this.getX(), target.y - 1);
			} else {
				low = new Vector2(this.getX(), target.y + 1);
				high = new Vector2(this.getX(), this.getY() - 1);
			}
		} else if (target.y == 0) {
			// hoziron move
			if (this.getX() <= target.x) {
				low = new Vector2(this.getX() + 1, this.getY());
				high = new Vector2(target.x - 1, this.getY());
			} else {
				low = new Vector2(target.x + 1, this.getY());
				high = new Vector2(this.getX() - 1, this.getY());
			}
		} else {

		}
	}

	public void update(float deltaTime) {
		// update tween
		if (tween != null)
			tween.update(deltaTime);
		// update follow
		if (!isDie)
			this.updateFollowModel(mdObject);
		// move platform
		if (type != IconModel.BUTTER && mdObject.getBody().getType() == BodyType.KinematicBody) {
			if (target.x == 0) {
				// move verticle
				if (this.getY() > high.y) {
					mdObject.getBody().setLinearVelocity(new Vector2(0, -VELOCITY * deltaTime));
				}
				if (this.getY() < low.y) {
					mdObject.getBody().setLinearVelocity(new Vector2(0, VELOCITY * deltaTime));
				}
			} else if (target.y == 0) {
				// move hoziron
				if (this.getX() > high.x) {
					mdObject.getBody().setLinearVelocity(new Vector2(-VELOCITY * deltaTime, 0));
				}
				if (this.getX() < low.x) {
					mdObject.getBody().setLinearVelocity(new Vector2(VELOCITY * deltaTime, 0));
				}
			} else {

			}
		}
		// check die
		if (isDie && !isProcDie) {
			// deactive body
			if (mdObject.getBody().isActive())
				mdObject.getBody().setActive(false);
			// create new tween
			Tween.to(this, SpriteAccessor.SCALE_XY, 0.5f).ease(Elastic.IN).target(0).setCallback(new TweenCallback() {
				@Override
				public void onEvent(int arg0, BaseTween<?> arg1) {
					canRemove = true;
				}
			}).start(tween);
			// turn off flag
			isProcDie = true;
		}
	}

	public Vector2 getPosCenter() {
		return new Vector2(this.getX() + this.getWidth() / 2, this.getY() + this.getHeight() / 2);
	}

	public ObjectModel getModel() {
		return this.mdObject;
	}

	public int getType() {
		return this.type;
	}

	public void setDie() {
		if (!isDie) {
			// play sound
			SoundManager.playSound(Assets.soEat);
			isDie = true;
		}
	}

	public boolean getCanRemove() {
		return this.canRemove;
	}

	public boolean getDie() {
		return this.isDie;
	}
}
