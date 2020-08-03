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

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import woodyx.basicapi.physics.BoxUtility;
import woodyx.basicapi.physics.ObjectModel;
import woodyx.basicapi.screen.XScreen;
import woodyx.basicapi.sound.SoundManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Json;
import com.tengames.jerrystealcheese.interfaces.GlobalVariables;
import com.tengames.jerrystealcheese.main.Assets;
import com.tengames.jerrystealcheese.main.JerryStealCheese;
import com.tengames.jerrystealcheese.objects.Cloud;
import com.tengames.jerrystealcheese.objects.CommonModelList;
import com.tengames.jerrystealcheese.objects.DynamicButton;
import com.tengames.jerrystealcheese.objects.FinalPresent;
import com.tengames.jerrystealcheese.objects.Heart;
import com.tengames.jerrystealcheese.objects.IconModel;
import com.tengames.jerrystealcheese.objects.Jerry;
import com.tengames.jerrystealcheese.objects.Terran;
import com.tengames.jerrystealcheese.objects.Tom;

public class ScreenGame extends XScreen implements Screen, InputProcessor {
	private static int countObjects = 0;

	public static final byte STATE_NULL = 0;
	public static final byte STATE_WIN = 1;
	public static final byte STATE_LOOSE = 2;

	private Texture arrMaps[] = { Assets.txBg, Assets.txBg1, Assets.txBg2 };
	private Heart hearts[];
	private Music arrMusic[] = { Assets.muBgGame1, Assets.muBgGame2 };
	private Music tempMusic;
	private Texture tempMap;

	private JerryStealCheese coreGame;
	private World world;
	private ObjectModel ground;
	private Stage stage;
	private ArrayList<Tom> toms;
	private ArrayList<Terran> terrans;
	private Jerry jerry;
	private FinalPresent present;
	private DynamicButton[] buttons;
	private ParticleEffect peSnow;
	private BufferedReader reader;
	private Cloud[] clouds;
	private String strJson;
	private int number, numButter;
	private byte state, step;
//	private Skin skin;

	public ScreenGame(JerryStealCheese coreGame, String strJson, int number) {
		super(800, 480);
		this.coreGame = coreGame;
		this.strJson = strJson;
		this.number = number;
//		startDebugBox();
		initialize();
	}

	@Override
	public void initialize() {
		// create world
		world = new World(GlobalVariables.GRAVITY, true);

		// create stage
		stage = new Stage(800, 480, true);
		((OrthographicCamera) stage.getCamera()).setToOrtho(false, 800, 480);
		InputMultiplexer input = new InputMultiplexer(stage, this);
		// set input
		Gdx.input.setInputProcessor(input);
		Gdx.input.setCatchBackKey(true);

		// create smt
		initializeParams();
		createUI();
		createArrays();
		createModels();
		// check contact listener
		checkCollision();
	}

	private void createArrays() {
		// create array of toms
		toms = new ArrayList<Tom>();
		// create array of terrans
		terrans = new ArrayList<Terran>();
		// create array of heart
		hearts = new Heart[3];
		for (int i = 0; i < hearts.length; i++) {
			hearts[i] = new Heart(Assets.taObjects.findRegion("heart-1"), 650 + 50 * i, 440, 0.2f);
		}
	}

	private void initializeParams() {

		/* loading data */
//		try {
//			reader = new BufferedReader(new FileReader("/home/woodyx/workspace-gdx/JerryStealCheeseAndroid/assets/data/jerrystealcheese.txt"));
//			reader = new BufferedReader(new FileReader("jerrystealcheese.txt"));
//		} catch (FileNotFoundException e) {}
		reader = coreGame.androidListener.getData();

		// get music
		tempMusic = arrMusic[MathUtils.random(1)];

		// random effects
		if (MathUtils.randomBoolean()) {
			peSnow = new ParticleEffect();
			peSnow.load(Gdx.files.internal("effects/snow.p"), Gdx.files.internal("effects"));
			peSnow.setPosition(0, 0);
			peSnow.start();
		} else {
			// create clouds
			clouds = new Cloud[5];
			for (int i = 0; i < clouds.length; i++) {
				clouds[i] = new Cloud(-200 + MathUtils.random(100), 300 + MathUtils.random(180),
						new Vector2(150 / 2, 82 / 2));
			}
		}
		// play music
		SoundManager.playMusic(tempMusic, 0.5f, true);

		// initialize params
		state = STATE_NULL;
		numButter = 0;
		step = 0;

		// trace
		if (number < 10) {
			coreGame.androidListener.traceScene("0" + number);
		} else {
			coreGame.androidListener.traceScene(number + "");
		}

		// show admob
		if (number % 3 == 0)
			coreGame.androidListener.showIntertitial();
	}

	private void createModels() {
		// create ground
		ground = new ObjectModel(world, ObjectModel.STATIC, ObjectModel.POLYGON, new Vector2(800, 10), new Vector2(), 0,
				new Vector2(), 0, 100, 0.5f, 0.1f, GlobalVariables.CATEGORY_SCENERY, GlobalVariables.MASK_SCENERY,
				"ground");

		// create wall-left
		@SuppressWarnings("unused")
		ObjectModel wallLeft = new ObjectModel(world, ObjectModel.STATIC, ObjectModel.POLYGON, new Vector2(10, 480),
				new Vector2(), 0, new Vector2(-10, 0), 0, 100, 0.5f, 0.1f, GlobalVariables.CATEGORY_SCENERY,
				GlobalVariables.MASK_SCENERY, "wallleft");

		// create wall-right
		@SuppressWarnings("unused")
		ObjectModel wallRight = new ObjectModel(world, ObjectModel.STATIC, ObjectModel.POLYGON, new Vector2(10, 480),
				new Vector2(), 0, new Vector2(800, 0), 0, 100, 0.5f, 0.1f, GlobalVariables.CATEGORY_SCENERY,
				GlobalVariables.MASK_SCENERY, "wallright");

		// create models
		if (strJson != null)
			createJsonModels();
	}

	private void createUI() {
		// load skin
//		skin = new Skin(Gdx.files.internal("drawable/objects/uiskin.json"), Assets.taSkin);

		// creat buttons
		buttons = new DynamicButton[6];

		// label stage
		buttons[0] = new DynamicButton(("Level: " + number), new Vector2(10, 440), 0);

		// button menu
		buttons[1] = new DynamicButton(Assets.taObjects.findRegion("ic-menu"), new Vector2(180, 430), 0.5f, 0.1f);
		buttons[1].getButton().addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
//				 play sound
				SoundManager.playSound(Assets.soClick);
				// back to screen stage
				coreGame.setScreen(new ScreenStage(coreGame));
			}
		});

		// button replay
		buttons[2] = new DynamicButton(Assets.taObjects.findRegion("ic-replay"), new Vector2(260, 430), 0.5f, 0.2f);
		buttons[2].getButton().addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// play sound
				SoundManager.playSound(Assets.soClick);
				// replay
				coreGame.setScreen(new ScreenGame(coreGame, strJson, number));
			}
		});

		// button left and right
		buttons[3] = new DynamicButton(Assets.taObjects.findRegion("arrow-left"), new Vector2(-30, -30), 1f, 0.3f);

		buttons[4] = new DynamicButton(Assets.taObjects.findRegion("arrow-right"), new Vector2(140, -30), 1f, 0.4f);

		// button sound
		buttons[5] = new DynamicButton(Assets.taObjects.findRegion("bt-soundon"),
				Assets.taObjects.findRegion("bt-soundoff"), new Vector2(740, 20), 0.6f, 0.5f);
		if (!SoundManager.SOUND_ENABLE)
			buttons[5].getButton().setChecked(true);
		buttons[5].getButton().addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// play sound
				SoundManager.playSound(Assets.soClick);
				// turn off sound and music
				SoundManager.MUSIC_ENABLE = !SoundManager.MUSIC_ENABLE;
				if (!SoundManager.MUSIC_ENABLE) {
					SoundManager.pauseMusic(tempMusic);
				} else {
					SoundManager.playMusic(tempMusic, 0.5f, true);
				}
				SoundManager.SOUND_ENABLE = !SoundManager.SOUND_ENABLE;
			}
		});

		// back to screen scenario
		/*
		 * TextButton btBack = new TextButton("Back", skin); btBack.setPosition(20,
		 * 400); btBack.addListener(new ChangeListener() {
		 * 
		 * @Override public void changed(ChangeEvent event, Actor actor) {
		 * coreGame.setScreen(new ScreenScenario(coreGame)); } });
		 */

		// add to stage
//		 stage.addActor(btBack);
		for (DynamicButton button : buttons) {
			if (button != null)
				stage.addActor(button);
		}

		// create presentation
		present = new FinalPresent();
	}

	private void addUIPresent() {
		// set input
		Gdx.input.setInputProcessor(stage);

		// invisible all buttons
		for (DynamicButton button : buttons) {
			if (button != null)
				button.setVisible(false);
		}

		// add to stage
		stage.addActor(present.getImg());
		if (present.getState() == STATE_WIN) {
			for (int i = 0; i < present.getButtons().length - 1; i++) {
				stage.addActor(present.getButtons()[i]);
			}
		} else if (present.getState() == STATE_LOOSE) {
			for (int i = 0; i < present.getButtons().length; i++) {
				if (i == 0 || i == 3)
					stage.addActor(present.getButtons()[i]);
			}
		}

		// button menu
		present.getButtons()[0].getButton().addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// play sound
				SoundManager.playSound(Assets.soClick);
				// return screen stage
				coreGame.setScreen(new ScreenStage(coreGame));
			}
		});
		// button replay
		if (present.getState() == STATE_WIN) {
			present.getButtons()[1].getButton().addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					// play sound
					SoundManager.playSound(Assets.soClick);
					// replay game
					coreGame.setScreen(new ScreenGame(coreGame, strJson, number));
				}
			});
		}
		if (present.getState() == STATE_LOOSE) {
			present.getButtons()[3].getButton().addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					// play sound
					SoundManager.playSound(Assets.soClick);
					// replay game
					coreGame.setScreen(new ScreenGame(coreGame, strJson, number));
				}
			});
		}
		// button next
		if (present.getState() == STATE_WIN) {
			present.getButtons()[2].getButton().addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					// play sound
					SoundManager.playSound(Assets.soClick);
					if (number == 15) {
						// return screen stage
						coreGame.setScreen(new ScreenStage(coreGame));
					} else {
						// save data
						coreGame.androidListener.setValue(number - 1, 1);

						// next level
						coreGame.androidListener.setValue(number, 1);
						export(number);
					}
				}
			});
		}
	}

	/* generate map */
	private void createJsonModels() {
		Json json = new Json();
		CommonModelList jsList = new CommonModelList();
		jsList = json.fromJson(CommonModelList.class, strJson);
		// generate map
		for (int i = 0; i < jsList.getSize(); i++) {
			switch (jsList.getModel(i).getType()) {
			/* create background */
			case IconModel.MAP:
				tempMap = arrMaps[(byte) jsList.getModel(i).getPosition().x];
				break;
			/* create jerry */
			case IconModel.JERRY:
				if (jerry == null)
					jerry = new Jerry(world, jsList.getModel(i).getPosition(), jsList.getModel(i).getSize(), 3);
				break;
			/* create tom */
			case IconModel.TOM:
				Tom tom = new Tom(world, jsList.getModel(i).getPosition(), jsList.getModel(i).getTarget(),
						jsList.getModel(i).getSize(), ("tom" + countObjects));
				toms.add(tom);
				countObjects++;
				break;
			/* create objects */
			// butter
			case IconModel.BUTTER:
				numButter++;
				Terran butter = new Terran(world, ground, jsList.getModel(i).getType(),
						jsList.getModel(i).getPosition(), jsList.getModel(i).getTarget(), jsList.getModel(i).getSize(),
						jsList.getModel(i).getRotation(), ("butter" + countObjects));
				terrans.add(butter);
				countObjects++;
				break;
			// terran
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
			case IconModel.BOOK_4:
			case IconModel.GROUND_1:
			case IconModel.GROUND_2:
			case IconModel.GROUND_3:
			case IconModel.RULER:
			case IconModel.TERRAN_8:
			case IconModel.TERRAN_9:
			case IconModel.TERRAN_10:
			case IconModel.TERRAN_11:
			case IconModel.TRAP_1:
			case IconModel.TRAP_2:
			case IconModel.WOOD_1:
			case IconModel.WOOD_2:
			case IconModel.WOOD_3:
			case IconModel.WOOD_4:
			case IconModel.WOOD_5:
			case IconModel.WOOD_6:
			case IconModel.WOOD_7:
				Terran terran = new Terran(world, ground, jsList.getModel(i).getType(),
						jsList.getModel(i).getPosition(), jsList.getModel(i).getTarget(), jsList.getModel(i).getSize(),
						jsList.getModel(i).getRotation(), "terran");
				terrans.add(terran);
				break;
			case IconModel.HOME:
				Terran home = new Terran(world, ground, jsList.getModel(i).getType(), jsList.getModel(i).getPosition(),
						jsList.getModel(i).getTarget(), jsList.getModel(i).getSize(), jsList.getModel(i).getRotation(),
						("home" + countObjects));
				terrans.add(home);
				countObjects++;
				break;
			default:
				break;
			}
		}

		// free models
		jsList.dispose();
		jsList = null;
	}

	private void export(int number) {
		String line = null, strExport = "MAP: " + (number + 1);
		try {
			while ((line = reader.readLine()) != null) {
				if (line.equals(strExport)) {
					// export strJson
					strJson = reader.readLine();
					break;
				}
			}
			// close reader
			reader.close();
			// set new Screen
			coreGame.setScreen(new ScreenGame(coreGame, strJson, (number + 1)));
		} catch (IOException e) {
		}
	}

	private void checkFinish(float deltaTime) {
		// check lost heart
		checkLostHearts();
		// show fail
		if (jerry.getHp() <= 0) {
			if (state == STATE_NULL) {
				// change state help
				if (number == 1 && step != 5)
					step = 5;
				// show presentation
				present.setState(STATE_LOOSE);
				addUIPresent();
				// play sound
				SoundManager.playSound(Assets.soFail);
				// turn on flag
				state = STATE_LOOSE;
			}
		}
		// update presentation
		present.update(deltaTime);
	}

	// show hp lost
	private void checkLostHearts() {
		switch (jerry.getHp()) {
		case 2:
			hearts[0].setDie();
			break;
		case 1:
			hearts[1].setDie();
			break;
		case 0:
			hearts[2].setDie();
			break;
		default:
			break;
		}
	}

	@Override
	public void update(float deltaTime) {
		updateWorld(deltaTime);
		updateStage(deltaTime);
		checkFinish(deltaTime);
		updateObjects(deltaTime);
//		control();
	}

	private void updateWorld(float deltaTime) {
		world.step(deltaTime, 8, 3);
	}

	private void updateStage(float deltaTime) {
		if (buttons[3].getButton().isPressed()) {
			// change help
			if (number == 1 && step == 0)
				step = 1;
			jerry.runLeft();
		} else if (buttons[4].getButton().isPressed()) {
			// change help
			if (number == 1 && step == 0)
				step = 1;
			jerry.runRight();
		} else {
			jerry.stopRun();
		}

	}

	private void updateObjects(float deltaTime) {
		// update jerry
		if (jerry != null)
			jerry.update(deltaTime);
		// change state help
		if (number == 1 && jerry.getX() >= 400 && step == 2)
			step = 3;
		// update toms
		if (!toms.isEmpty()) {
			for (Tom tom : toms) {
				tom.update(deltaTime);
			}
		}
		// update terran
		if (!terrans.isEmpty()) {
			for (int i = 0; i < terrans.size(); i++) {
				if (terrans.get(i) != null) {
					terrans.get(i).update(deltaTime);
					// remove dead cheese
					if (terrans.get(i).getCanRemove()) {
						// decrease butter
						numButter--;
						terrans.remove(i);
					}
				}
			}
		}
		// update hearts
		for (Heart heart : hearts) {
			if (heart != null) {
				heart.update(deltaTime);
			}
		}
		// update clouds or cloud
		if (peSnow != null) {
			peSnow.update(deltaTime);
			if (peSnow.isComplete())
				peSnow.start();
		} else {
			for (Cloud cloud : clouds) {
				if (cloud != null)
					cloud.update(deltaTime);
			}
		}
	}

	/*
	 * private void control() { if (Gdx.input.isKeyPressed(Keys.LEFT)) {
	 * jerry.runLeft(); } else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
	 * jerry.runRight(); } else if (Gdx.input.isKeyPressed(Keys.UP)) { } else {
	 * jerry.stopRun(); } }
	 */

	@Override
	public void draw() {
		renderBackGround();
		renderObjects();
		renderStage();
		renderPresent();
	}

	private void renderBackGround() {
		bgDrawable(true);
		batch.draw(tempMap, 0, 0, 800, 480);
		bgDrawable(false);
	}

	private void renderObjects() {
		objDrawable(true);
		renderClouds();
		renderJerry();
		renderToms();
		renderTerrans();
		renderHearts();
		renderHelp();
		objDrawable(false);
	}

	private void renderPresent() {
		objDrawable(true);
		present.render(batch);
		objDrawable(false);
	}

	private void renderHearts() {
		for (Heart heart : hearts) {
			if (heart != null) {
				heart.render(batch);
			}
		}
	}

	private void renderTerrans() {
		if (!terrans.isEmpty()) {
			for (Terran terran : terrans) {
				terran.draw(batch);
			}
		}
	}

	private void renderToms() {
		if (!toms.isEmpty()) {
			for (Tom tom : toms) {
				tom.render(batch);
			}
		}
	}

	private void renderJerry() {
		if (jerry != null)
			jerry.render(batch);
	}

	private void renderHelp() {
		// render help
		if (number == 1) {
			switch (step) {
			case 0:
				batch.draw(Assets.taObjects.findRegion("help"), 100, 120, 182 * 1.8f, 103);
				Assets.fHelp.draw(batch, "Use arrow keys to move Jerry", 120, 200);
				break;
			case 1:
				batch.draw(Assets.taObjects.findRegion("dark"), 400, 0, 400, 480);
				batch.draw(Assets.taObjects.findRegion("help"), 500, 240);
				Assets.fHelp.draw(batch, "Tap here to jump", 520, 315);
				break;
			case 2:
				batch.draw(Assets.taObjects.findRegion("help"), 250, 130, 180 * 1.8f, 103 * 1.5f);
				Assets.fHelp.draw(batch, "Collect cheese!", 340, 260);
				Assets.fHelp.draw(batch, "Be careful with traps and Tom!", 270, 220);
				break;
			case 3:
				batch.draw(Assets.taObjects.findRegion("help"), 790, 300, -180 * 1.3f, 103 * 1.5f);
				Assets.fHelp.draw(batch, "Collect all cheese", 600, 430);
				Assets.fHelp.draw(batch, "and take Jerry to home!", 570, 400);
				break;
			default:
				break;
			}
		}

		// render snow
		if (peSnow != null)
			peSnow.draw(batch);
	}

	private void renderClouds() {
		if (peSnow == null) {
			for (Cloud cloud : clouds) {
				if (cloud != null)
					cloud.render(batch);
			}
		}
	}

	private void renderStage() {
		stage.draw();
	}

	@Override
	public void render(float deltaTime) {
		clearScreen(deltaTime);
		clearWorld();
		update(deltaTime);
		draw();
//		renderDebug(world);
	}

	private void clearWorld() {
		world.clearForces();
	}

	private void checkCollision() {
		world.setContactListener(new ContactListener() {

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
			}

			@Override
			public void endContact(Contact contact) {
			}

			@Override
			public void beginContact(Contact contact) {
				// jerry contact ground
				if (BoxUtility.detectCollision(contact, "jerry", "ground")) {
					jerry.setState(Jerry.STATE_STAND);
					jerry.resetSleepJump();
				}
				// jerry contact to tom
				for (Tom tom : toms) {
					if (BoxUtility.detectCollision(contact, tom.getModel(), "jerry")) {
						// jerry hurt
						jerry.setHurt();
					}
				}
				// jerry contact to terran
				for (Terran terran : terrans) {
					if (terran != null) {
						if (BoxUtility.detectCollision(contact, terran.getModel(), "jerry")) {
							switch (terran.getType()) {
							case IconModel.HOME:
								// show win
								if (numButter <= 0) {
									if (state == STATE_NULL) {
										// change state help
										if (number == 1 && step != 5)
											step = 5;
										// set jerry invisible
										jerry.setVisible();
										// show presentation
										present.setState(STATE_WIN);
										addUIPresent();

										// pause music
										SoundManager.pauseMusic(tempMusic);

										// play sound
										SoundManager.playSound(Assets.soWin);

										switch (jerry.getHp()) {
										case 2:
											// save hscore
											coreGame.androidListener.saveHscore(100);
											break;

										case 1:
											// save hscore
											coreGame.androidListener.saveHscore(10);
											break;

										}

										// turn on flag
										state = STATE_WIN;
									}
								}
								break;
							case IconModel.BUTTER:
								// eat butter
								terran.setDie();
								break;
							case IconModel.TRAP_1:
							case IconModel.TRAP_2:
								// jerry hurt
								jerry.setHurt();
								break;
							default:
								break;
							}
						}
					}
				}
			}
		});
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		// play music
		SoundManager.playMusic(tempMusic, 0.5f, true);
	}

	@Override
	public void hide() {
		// pause music, sound
		SoundManager.pauseMusic(tempMusic);
		switch (state) {
		case STATE_LOOSE:
			SoundManager.stopSound(Assets.soFail);
			break;
		case STATE_WIN:
			SoundManager.stopSound(Assets.soWin);
			break;
		default:
			break;
		}
	}

	@Override
	public void pause() {
		// pause music, sound
		SoundManager.pauseMusic(tempMusic);
		switch (state) {
		case STATE_LOOSE:
			SoundManager.stopSound(Assets.soFail);
			break;
		case STATE_WIN:
			SoundManager.stopSound(Assets.soWin);
			break;
		default:
			break;
		}
	}

	@Override
	public void resume() {
		// play music
		SoundManager.playMusic(tempMusic, 0.5f, true);
	}

	@Override
	public void dispose() {
		world.dispose();
		stage.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
//		if (keycode == Keys.UP) {
//			jerry.jump();
//		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	Vector3 touchPoint = new Vector3();
	float lastTouch;

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		camera.unproject(touchPoint.set(screenX, screenY, 0));
		// control jerry: jerry jump
		if (jerry != null && touchPoint.y <= 420 && touchPoint.x >= 400) {
			// change help
			if (number == 1 && step == 1)
				step = 2;
			jerry.jump();
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
