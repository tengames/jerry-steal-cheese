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
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Bounce;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tengames.jerrystealcheese.main.Assets;
import com.tengames.jerrystealcheese.screens.ScreenGame;

public class FinalPresent {
	private TweenManager tween;
	private DynamicButton[] buttons;
	private ObjectSprite spTv, spPresent, spText;
	private Animation aniTom, aniJerry;
	private float stateTime;
	private byte state;
	private Image imgDark;

	public FinalPresent() {
		this.state = ScreenGame.STATE_NULL;
		this.stateTime = 0;

		// create tween
		tween = new TweenManager();

		// create sp tv, text, animation
		spTv = new ObjectSprite(Assets.taObjects.findRegion("tv"), 200, 480 * 2);
		spText = new ObjectSprite(Assets.taObjects.findRegion("text-win"), (800 - 321) / 2, 480 * 2);
		spPresent = new ObjectSprite(Assets.taObjects.findRegion("tom-win-1"), (800 - 340) / 2, 480 * 2);

		// create animation
		aniTom = new Animation(0.1f, Assets.taObjects.findRegion("tom-win-1"), Assets.taObjects.findRegion("tom-win-2"),
				Assets.taObjects.findRegion("tom-win-3"), Assets.taObjects.findRegion("tom-win-4"),
				Assets.taObjects.findRegion("tom-win-5"), Assets.taObjects.findRegion("tom-win-6"),
				Assets.taObjects.findRegion("tom-win-7"), Assets.taObjects.findRegion("tom-win-8"));
		aniJerry = new Animation(0.1f, Assets.taObjects.findRegion("jerry-win-1"),
				Assets.taObjects.findRegion("jerry-win-2"), Assets.taObjects.findRegion("jerry-win-3"),
				Assets.taObjects.findRegion("jerry-win-4"), Assets.taObjects.findRegion("jerry-win-5"),
				Assets.taObjects.findRegion("jerry-win-6"), Assets.taObjects.findRegion("jerry-win-7"),
				Assets.taObjects.findRegion("jerry-win-8"), Assets.taObjects.findRegion("jerry-win-9"),
				Assets.taObjects.findRegion("jerry-win-10"), Assets.taObjects.findRegion("jerry-win-11"),
				Assets.taObjects.findRegion("jerry-win-12"), Assets.taObjects.findRegion("jerry-win-13"),
				Assets.taObjects.findRegion("jerry-win-14"), Assets.taObjects.findRegion("jerry-win-15"),
				Assets.taObjects.findRegion("jerry-win-16"));

		spPresent.setOriginCenter(spPresent);
		spTv.setOriginCenter(spTv);
		spText.setOriginCenter(spText);

		// apply tween
		applyTween(spText, new Vector2(400 + 10, 420));
		applyTween(spTv, new Vector2(400, 270));
		applyTween(spPresent, new Vector2(400 - 20, 270));

		// create buttons
		buttons = new DynamicButton[4];
		buttons[0] = new DynamicButton(new TextureRegionDrawable(Assets.taObjects.findRegion("bt-menu")),
				new Vector2(-183, 30), new Vector2(200 - 83, 30));
		// button replay
		buttons[1] = new DynamicButton(new TextureRegionDrawable(Assets.taObjects.findRegion("bt-replay")),
				new Vector2(400 - 83, -100), new Vector2(400 - 83, 30));
		// button next
		buttons[2] = new DynamicButton(new TextureRegionDrawable(Assets.taObjects.findRegion("bt-next")),
				new Vector2(900, 30), new Vector2(600 - 83, 30));
		// button replay 2
		buttons[3] = new DynamicButton(new TextureRegionDrawable(Assets.taObjects.findRegion("bt-replay")),
				new Vector2(900, 30), new Vector2(600 - 83, 30));

		// dark
		imgDark = new Image(Assets.taObjects.findRegion("dark"));
		imgDark.setPosition(0, 0);
		imgDark.setSize(800, 480);
	}

	private void applyTween(ObjectSprite sp, Vector2 target) {
		Tween.to(sp, SpriteAccessor.CPOS_XY, 1f).ease(Bounce.OUT).target(target.x, target.y).start(tween);
	}

	public void update(float deltaTime) {
		if (state != ScreenGame.STATE_NULL) {
			// update state time
			stateTime += deltaTime;
			// update tween
			tween.update(deltaTime);
			// update buttons
			for (DynamicButton button : buttons) {
				if (button != null)
					button.update(deltaTime);
			}
		}
	}

	public void render(SpriteBatch batch) {
		if (state != ScreenGame.STATE_NULL) {
			// draw animation
			TextureRegion keyFrame = null;
			switch (state) {
			case ScreenGame.STATE_WIN:
				keyFrame = aniJerry.getKeyFrame(stateTime, true);
				break;
			case ScreenGame.STATE_LOOSE:
				keyFrame = aniTom.getKeyFrame(stateTime, true);
				break;
			default:
				break;
			}
			if (keyFrame != null)
				spPresent.renderSprite(keyFrame).draw(batch);
			// draw tv
			spTv.draw(batch);
			// draw text
			switch (state) {
			case ScreenGame.STATE_WIN:
				keyFrame = Assets.taObjects.findRegion("text-win");
				break;
			case ScreenGame.STATE_LOOSE:
				keyFrame = Assets.taObjects.findRegion("text-fail");
				break;
			default:
				break;
			}
			if (keyFrame != null)
				spText.renderSprite(keyFrame).draw(batch);
		}
	}

	public DynamicButton[] getButtons() {
		return buttons;
	}

	public Image getImg() {
		return imgDark;
	}

	public void setState(byte state) {
		if (this.state != state)
			this.state = state;
	}

	public byte getState() {
		return state;
	}
}
