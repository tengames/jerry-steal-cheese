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

import woodyx.basicapi.accessor.ActorAccessor;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Elastic;
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.equations.Quart;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tengames.jerrystealcheese.main.Assets;

public class DynamicButton extends Group {
	private TweenManager tween = new TweenManager();
	private Button button;
	private Image imgBt;
	private Label lbText;
	private int value;
	private boolean isComplete = false;

	// for logo
	public DynamicButton(TextureRegion region, Vector2 position, Vector2 target) {
		Image img = new Image(region);
		img.setPosition(0, 0);
		this.setPosition(position.x, position.y);
		this.addActor(img);
		this.setOrigin(region.getRegionWidth() / 2, region.getRegionHeight() / 2);

		Timeline.createSequence()
				.push(Tween.to(this, ActorAccessor.CPOS_XY, 2).ease(Bounce.OUT).target(target.x, target.y))
				.push(Tween.to(this, ActorAccessor.CPOS_XY, 1).ease(Linear.INOUT).target(target.x - 20, target.y)
						.repeatYoyo(1000, 0))
				.start(tween);
	}

	// for label
	public DynamicButton(String text, Vector2 positon, float delay) {
		lbText = new Label(text, Assets.lbSSmall);
		lbText.setPosition(0, 0);
		this.setPosition(positon.x, positon.y);
		this.addActor(lbText);
		this.setOrigin(lbText.getWidth() / 2, lbText.getHeight() / 2);

		Timeline.createSequence().push(Tween.set(this, ActorAccessor.SCALE_XY).target(0, 0)).pushPause(delay)
				.push(Tween.to(this, ActorAccessor.SCALE_XY, 1f).target(1, 1).ease(Elastic.OUT)).start(tween);
	}

	// for button menu
	public DynamicButton(TextureRegion region, Vector2 position, Vector2 target, float delay) {
		button = new Button(new TextureRegionDrawable(region));
		button.setPosition(0, 0);
		this.setPosition(position.x, position.y);
		this.addActor(button);
		this.setOrigin(region.getRegionWidth() / 2, region.getRegionHeight() / 2);

		Timeline.createSequence()
				.push(Tween.to(this, ActorAccessor.CPOS_XY, 1f).target(target.x, target.y).ease(Bounce.OUT))
				.push(Tween.to(this, ActorAccessor.SCALE_XY, 1f).target(0.9f, 0.9f).repeatYoyo(100, 0)).start(tween);
	}

	// for button present
	public DynamicButton(TextureRegionDrawable region, Vector2 position, Vector2 target) {
		button = new Button(region);
		button.setPosition(0, 0);
		this.setPosition(position.x, position.y);
		this.addActor(button);

		Tween.to(this, ActorAccessor.CPOS_XY, 1f).target(target.x, target.y).ease(Quart.OUT).start(tween);
	}

	// for button game
	public DynamicButton(TextureRegion region, Vector2 position, float scale, float delay) {
		button = new Button(new TextureRegionDrawable(region));
		button.setPosition(0, 0);
		button.setSize(region.getRegionWidth() * scale, region.getRegionHeight() * scale);
		this.setPosition(position.x, position.y);
		this.addActor(button);
		this.setOrigin(region.getRegionWidth() / 2, region.getRegionHeight() / 2);

		Timeline.createSequence().push(Tween.set(this, ActorAccessor.SCALE_XY).target(0, 0)).pushPause(delay).push(Tween
				.to(this, ActorAccessor.SCALE_XY, 1f).target(1, 1).ease(Elastic.OUT).setCallback(new TweenCallback() {
					@Override
					public void onEvent(int arg0, BaseTween<?> arg1) {
						isComplete = true;
					}
				})).start(tween);
	}

	// for button game
	public DynamicButton(TextureRegion region, Vector2 position, float scale) {
		imgBt = new Image(region);
		imgBt.setPosition(0, 0);
		imgBt.setSize(region.getRegionWidth() * scale, region.getRegionHeight() * scale);
		this.setPosition(position.x, position.y);
		this.addActor(imgBt);
		this.setOrigin(region.getRegionWidth() / 2, region.getRegionHeight() / 2);

		Timeline.createSequence().push(Tween.set(this, ActorAccessor.SCALE_XY).target(0, 0)).push(Tween
				.to(this, ActorAccessor.SCALE_XY, 1f).target(1, 1).ease(Elastic.OUT).setCallback(new TweenCallback() {
					@Override
					public void onEvent(int arg0, BaseTween<?> arg1) {
						isComplete = true;
					}
				})).start(tween);
	}

	// for button sound
	public DynamicButton(TextureRegion up, TextureRegion down, Vector2 position, float scale, float delay) {
		button = new Button(new TextureRegionDrawable(up), new TextureRegionDrawable(down),
				new TextureRegionDrawable(down));
		button.setPosition(0, 0);
		button.setSize(scale * down.getRegionWidth(), scale * down.getRegionHeight());
		this.setPosition(position.x, position.y);
		this.addActor(button);
		this.setOrigin(scale * down.getRegionWidth() / 2, scale * down.getRegionHeight() / 2);

		Timeline.createSequence().push(Tween.set(this, ActorAccessor.SCALE_XY).target(0, 0)).pushPause(delay).push(Tween
				.to(this, ActorAccessor.SCALE_XY, 1f).target(1, 1).ease(Elastic.OUT).setCallback(new TweenCallback() {
					@Override
					public void onEvent(int arg0, BaseTween<?> arg1) {
						isComplete = true;
					}
				})).start(tween);
	}

	// for buttons stage
	public DynamicButton(TextureRegion unlock, TextureRegion lock, int value, int number, float delay) {
		this.value = value;
		this.setBounds(0, 0, unlock.getRegionWidth(), unlock.getRegionHeight());
		this.setOrigin(unlock.getRegionWidth() / 2, unlock.getRegionHeight() / 2);
		// create button, label, image
		if (value == 0) {
			Image imgBg = new Image(lock);
			// add icon lock
			Image imgLock = new Image(new TextureRegionDrawable(Assets.taObjects.findRegion("lock")));
			imgLock.setPosition((imgBg.getWidth() - imgLock.getWidth()) / 2,
					(imgBg.getHeight() - imgLock.getHeight()) / 2);
			this.addActor(imgBg);
			this.addActor(imgLock);
		} else {
			Image imgBg = new Image(unlock);
			Label lbNumber = new Label("" + number, Assets.lbSNumber);
			lbNumber.setPosition((unlock.getRegionWidth() - lbNumber.getWidth()) / 2,
					(unlock.getRegionHeight() - lbNumber.getHeight()) / 2);
			this.addActor(imgBg);
			this.addActor(lbNumber);
		}
		button = new Button(
				new TextureRegionDrawable(new TextureRegion(Assets.taObjects.findRegion("logo"), 0, 0, 5, 5)));
		button.setSize(unlock.getRegionWidth(), unlock.getRegionWidth());
		button.setPosition(0, 0);
		this.addActor(button);

		// create tween
		Timeline.createSequence().push(Tween.set(this, ActorAccessor.SCALE_XY).target(0, 0)).pushPause(delay).push(Tween
				.to(this, ActorAccessor.SCALE_XY, 1f).target(1, 1).ease(Elastic.OUT).setCallback(new TweenCallback() {
					@Override
					public void onEvent(int arg0, BaseTween<?> arg1) {
						isComplete = true;
					}
				})).start(tween);
	}

	public void setText(String text) {
		lbText.setText(text);
	}

	public Button getButton() {
		return button;
	}

	public int getValue() {
		return value;
	}

	public void update(float deltaTime) {
		if (!isComplete)
			tween.update(deltaTime);
	}

	public Image getImgBt() {
		return imgBt;
	}

}