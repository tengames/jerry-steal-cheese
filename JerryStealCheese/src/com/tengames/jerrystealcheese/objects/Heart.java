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
import woodyx.basicapi.sprite.ObjectSprite;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.TweenPaths;
import aurelienribon.tweenengine.equations.Elastic;
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.equations.Quart;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.tengames.jerrystealcheese.main.Assets;

public class Heart extends ObjectSprite {
	private TweenManager tween;
	private Vector2 oldPos;
	private boolean isDie, canStop;

	public Heart(TextureRegion textureRegion, float x, float y, float delay) {
		super(textureRegion, x, y);
		// init params
		oldPos = getPosition();
		isDie = false;
		canStop = false;
		this.setOriginCenter(this);
		// create tween
		tween = new TweenManager();
		Timeline.createSequence().push(Tween.set(this, SpriteAccessor.SCALE_XY).target(0, 0)).pushPause(delay)
				.push(Tween.to(this, SpriteAccessor.SCALE_XY, 1f).ease(Elastic.OUT).target(1f, 1f)).push(Tween
						.to(this, SpriteAccessor.SCALE_XY, 0.8f).ease(Elastic.OUT).target(1.2f, 1.2f).repeat(1000, 0))
				.start(tween);

	}

	public void update(float deltaTime) {
		// update tween
		if (!canStop)
			tween.update(deltaTime);

	}

	public void render(SpriteBatch batch) {
		// render heart
		if (isDie)
			batch.draw(Assets.taObjects.findRegion("heart-2"), oldPos.x, oldPos.y);
		this.draw(batch);
	}

	public void setDie() {
		// set die
		if (!isDie) {
			Timeline.createParallel()
					.push(Tween.to(this, SpriteAccessor.CPOS_XY, 1f).ease(Linear.INOUT).path(TweenPaths.catmullRom)
							.target(oldPos.x, oldPos.y - 80))
					.push(Tween.to(this, SpriteAccessor.ROTATION, 1f).ease(Quart.OUT).target(135))
					.push(Tween.to(this, SpriteAccessor.OPACITY, 1f).ease(Linear.INOUT).target(0)
							.setCallback(new TweenCallback() {
								@Override
								public void onEvent(int arg0, BaseTween<?> arg1) {
									canStop = true;
								}
							}))
					.start(tween);
			isDie = true;
		}
	}
}
