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
package com.tengames.jerrystealcheese.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.tengames.jerrystealcheese.main.Assets;
import com.tengames.jerrystealcheese.main.JerryStealCheese;

public class ScreenLoading implements Screen {
	protected JerryStealCheese game;
	protected Stage stage;
	protected int type;

	private Image logo;
	private Image loadingFrame;
	private Image loadingBarHidden;
	private Image screenBg;
	private Image loadingBg;

	private float startX, endX;
	private float percent;

	private Actor loadingBar;

	public ScreenLoading(JerryStealCheese game) {
		this.game = game;
		Assets.loadResLoading();
		Assets.load();
	}

	private void create_actor() {
		stage.setViewport(800, 480, false);

		// Make the background fill the screen
		screenBg.setSize(800, 480);

		// Place the logo in the middle of the screen and 100 px up
		logo.setX((800 - logo.getWidth()) / 2);
		logo.setY((480 - logo.getHeight()) / 2 + 100);

		// Place the loading frame in the middle of the screen
		loadingFrame.setX((800 - loadingFrame.getWidth()) / 2);
		loadingFrame.setY(150);

		// Place the loading bar at the same spot as the frame, adjusted a few px
		loadingBar.setX(loadingFrame.getX() + 15);
		loadingBar.setY(loadingFrame.getY() + 5);

		// Place the image that will hide the bar on top of the bar, adjusted a few px
		loadingBarHidden.setX(loadingBar.getX() + 35);
		loadingBarHidden.setY(loadingBar.getY() - 3);
		// The start position and how far to move the hidden loading bar
		startX = loadingBarHidden.getX();
		endX = 440;

		// The rest of the hidden bar
		loadingBg.setSize(450, 50);
		loadingBg.setX(loadingBarHidden.getX() + 30);
		loadingBg.setY(loadingBarHidden.getY() + 3);
	}

	@Override
	public void render(float delta) {
		// Clear the screen
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		if (Assets.assetManager.update() && percent > 0.99f) {
			Assets.loadDone();
//    	  game.setScreen(new ScreenScenario(game));
			game.setScreen(new ScreenMenu(game));
//    	  game.setScreen(new ScreenRegion(game));
//    	  String str = "{commonList:[{size:{x:10.0,y:51.0},position:{x:364.0,y:78.5},target:{},type:28},{size:{x:10.0,y:51.0},position:{x:373.0,y:75.5},target:{},type:28},{size:{x:10.0,y:51.0},position:{x:381.0,y:77.5},target:{},type:28},{size:{x:800.0,y:105.0},position:{y:-2.5},target:{},type:6},{size:{x:37.255276,y:37.255276},position:{x:56.37236,y:103.37236},target:{},type:1},{size:{x:67.618324,y:50.33809},position:{x:719.19086,y:103.830956},target:{x:600.0}},{size:{x:111.0,y:36.0},position:{x:516.5,y:143.0},target:{},type:4},{size:{x:139.0,y:72.0},position:{x:649.5,y:158.0},target:{},type:2},{size:{x:67.618324,y:58.60255},position:{x:701.19086,y:226.69873},target:{},type:7},{size:{x:37.56574,y:26.296017},position:{x:194.21715,y:101.85198},target:{},type:37},{size:{x:37.56574,y:26.296017},position:{x:446.21713,y:101.85198},target:{},type:37},{size:{x:50.0,y:50.0},position:{},type:8},{size:{x:50.0,y:50.0},position:{},type:8},{size:{x:50.0,y:50.0},position:{},type:8},{size:{x:50.0,y:50.0},position:{},type:8}]}";
//    	  game.setScreen(new ScreenGame(game, str, 1));
		}

		percent = Interpolation.linear.apply(percent, Assets.assetManager.getProgress(), 0.02f);

		// Update positions (and size) to match the percentage
		loadingBarHidden.setX(startX + endX * percent);
		loadingBg.setX(loadingBarHidden.getX() + 30);
		loadingBg.setWidth(450 - 450 * percent);
		loadingBg.invalidate();

		// Show the loading screen
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		stage = new Stage();

		// Get our texture atlas from the manager
		TextureAtlas atlas = Assets.assetManager.get("drawable/loading.atlas", TextureAtlas.class);

		// Grab the regions from the atlas and create some images
		logo = new Image(atlas.findRegion("libgdx-logo"));
		loadingFrame = new Image(atlas.findRegion("loading-frame"));
		loadingBarHidden = new Image(atlas.findRegion("loading-bar-hidden"));
		screenBg = new Image(atlas.findRegion("screen-bg"));
		loadingBg = new Image(atlas.findRegion("loading-frame-bg"));

//       Or if you only need a static bar, you can do
		loadingBar = new Image(atlas.findRegion("loading-bar1"));

		// Add all the actors to the stage
		stage.addActor(screenBg);
		stage.addActor(loadingBar);
		stage.addActor(loadingBg);
		stage.addActor(loadingBarHidden);
		stage.addActor(loadingFrame);
		stage.addActor(logo);

		create_actor();
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
	}

}
