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
package com.tengames.jerrystealcheese.main;

import woodyx.basicapi.screen.Asset;
import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class Assets extends Asset {
	public static Texture txBg, txBg1, txBg2, txBgMenu;
	public static TextureAtlas taSkin, taObjects;
	public static TextureRegion trSnow;
	public static Animation aniTom, aniTomF;
	public static BitmapFont fNumber, fSmall, fHelp;
	public static LabelStyle lbSNumber, lbSSmall;
	public static BodyEditorLoader bdLoader;
	public static Music muBgMeu, muBgStage, muBgGame1, muBgGame2;
	public static Sound soClick, soEat, soJump, soWin, soFail, soHurt;

	public static void loadResLoading() {
		loading("drawable/", "atlas", "loading");
		assetManager.finishLoading();
	}

	public static void load() {
		// loading background
		loading("drawable/backgrounds/", "jpg", "bggame", "bggame1", "bggame2", "bgmenu");
		// loading objects
		loading("drawable/objects/", "atlas", "uiskin", "objects");
		// loading effects
		loading("effects/", "png", "snow");
		// loading fonts
		loading("fonts/", "png", "numfont", "fonts");
		// loading sound
		loading("raw/", "ogg", "buttonclick", "eat", "fail", "win", "jump", "yow");
		// loading music
		loading("raw/", "mp3", "bgmenu", "bgstage", "bggame1", "bggame2");
	}

	public static void loadDone() {
		// loaded backgrounds
		txBg = assetManager.get("drawable/backgrounds/bggame.jpg");
		txBg.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		txBg1 = assetManager.get("drawable/backgrounds/bggame1.jpg");
		txBg1.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		txBg2 = assetManager.get("drawable/backgrounds/bggame2.jpg");
		txBg2.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		txBgMenu = assetManager.get("drawable/backgrounds/bgmenu.jpg");
		txBgMenu.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		// loaded objects
		taSkin = assetManager.get("drawable/objects/uiskin.atlas");
		taObjects = assetManager.get("drawable/objects/objects.atlas");
		// loaded animation
		aniTom = new Animation(0.1f, taObjects.findRegion("tom-1"), taObjects.findRegion("tom-2"),
				taObjects.findRegion("tom-3"), taObjects.findRegion("tom-4"), taObjects.findRegion("tom-5"),
				taObjects.findRegion("tom-6"), taObjects.findRegion("tom-7"), taObjects.findRegion("tom-8"));
		TextureRegion tom1 = new TextureRegion(taObjects.findRegion("tom-1"));
		TextureRegion tom2 = new TextureRegion(taObjects.findRegion("tom-2"));
		TextureRegion tom3 = new TextureRegion(taObjects.findRegion("tom-3"));
		TextureRegion tom4 = new TextureRegion(taObjects.findRegion("tom-4"));
		TextureRegion tom5 = new TextureRegion(taObjects.findRegion("tom-5"));
		TextureRegion tom6 = new TextureRegion(taObjects.findRegion("tom-6"));
		TextureRegion tom7 = new TextureRegion(taObjects.findRegion("tom-7"));
		TextureRegion tom8 = new TextureRegion(taObjects.findRegion("tom-8"));
		tom1.flip(true, false);
		tom2.flip(true, false);
		tom3.flip(true, false);
		tom4.flip(true, false);
		tom5.flip(true, false);
		tom6.flip(true, false);
		tom7.flip(true, false);
		tom8.flip(true, false);
		aniTomF = new Animation(0.1f, tom1, tom2, tom3, tom4, tom5, tom6, tom7, tom8);
		// loaded effect
		Texture txSnow = assetManager.get("effects/snow.png");
		txSnow.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		trSnow = new TextureRegion(txSnow);
		// loaded fonts
		Texture txNumFont = assetManager.get("fonts/numfont.png");
		txNumFont.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		fNumber = new BitmapFont(Gdx.files.internal("fonts/numfont.fnt"), new TextureRegion(txNumFont), false);
		fSmall = new BitmapFont(Gdx.files.internal("fonts/numfont.fnt"), new TextureRegion(txNumFont), false);
		fSmall.setScale(0.6f);
		Texture txFont = assetManager.get("fonts/fonts.png");
		txFont.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		fHelp = new BitmapFont(Gdx.files.internal("fonts/fonts.fnt"), new TextureRegion(txFont), false);
		fHelp.setColor(Color.BLACK);
		lbSNumber = new LabelStyle();
		lbSNumber.font = fNumber;
		lbSSmall = new LabelStyle();
		lbSSmall.font = fSmall;
		// loaded models
		bdLoader = new BodyEditorLoader(Gdx.files.internal("drawable/objects/objects.json"));
		// loaded sound
		soClick = assetManager.get("raw/buttonclick.ogg");
		soEat = assetManager.get("raw/eat.ogg");
		soFail = assetManager.get("raw/fail.ogg");
		soHurt = assetManager.get("raw/yow.ogg");
		soJump = assetManager.get("raw/jump.ogg");
		soWin = assetManager.get("raw/win.ogg");
		// loaded music
		muBgGame1 = assetManager.get("raw/bggame1.mp3");
		muBgGame2 = assetManager.get("raw/bggame2.mp3");
		muBgMeu = assetManager.get("raw/bgmenu.mp3");
		muBgStage = assetManager.get("raw/bgstage.mp3");
	}

}
