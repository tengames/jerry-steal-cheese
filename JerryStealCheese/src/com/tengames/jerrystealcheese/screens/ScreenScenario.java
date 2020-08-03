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

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import woodyx.basicapi.screen.XScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Json;
import com.tengames.jerrystealcheese.main.Assets;
import com.tengames.jerrystealcheese.main.JerryStealCheese;
import com.tengames.jerrystealcheese.objects.CommonModel;
import com.tengames.jerrystealcheese.objects.CommonModelList;
import com.tengames.jerrystealcheese.objects.IconModel;

public class ScreenScenario extends XScreen implements Screen, InputProcessor {
	private static ArrayList<IconModel> realArrs;
	private static byte indexMap = 0;

	private JerryStealCheese coreGame;
	private Stage stage;
	private Dialog dialog, dgTarget;
	private TextField tfModel, tfX, tfY;
	private Skin skin;
	private ShapeRenderer shapeRender;
	private ArrayList<IconModel> icoArrs;
	private Texture arrMaps[] = { Assets.txBg, Assets.txBg1, Assets.txBg2 };
	private Texture tempMap;
	private IconModel icoTom, icoJerry, icoTemp, icoGround1, icoGround2, icoGround3, icoHome, icoBridge1, icoBridge2,
			icoBridge3, icoBridge4, icoTerran1, icoTerran2, icoTerran3, icoTerran4, icoTerran5, icoTerran6, icoTerran7,
			icoTerran8, icoTerran9, icoTerran10, icoTerran11, icoBook1, icoBook2, icoBook3, icoBook4, icoBook5,
			icoWood1, icoWood2, icoWood3, icoWood4, icoWood5, icoWood6, icoWood7, icoRuler, icoTrap1, icoTrap2,
			icoButter;
	private CommonModelList cmExport;
	private Json json;
	private String strExport;
	private boolean isHide, isHideNet;

	public ScreenScenario(JerryStealCheese coreGame) {
		super(800, 480);
		this.coreGame = coreGame;
		initialize();
	}

	@Override
	public void initialize() {
		initializeParams();
		createUI();
		createDialog();
		createList();
	}

	private void createUI() {
		// create stage
		stage = new Stage(800, 480, true);
		((OrthographicCamera) stage.getCamera()).setToOrtho(false, 800, 480);

		// set input
		InputMultiplexer input = new InputMultiplexer(stage, this);
		Gdx.input.setInputProcessor(input);

		// load skin
		skin = new Skin(Gdx.files.internal("drawable/objects/uiskin.json"), Assets.taSkin);

		// create shape renderer
		shapeRender = new ShapeRenderer();

		/* create buttons */
		// button hide dialog
		TextButton btHide = new TextButton("HideDialog", skin);
		btHide.setPosition(10, 450);
		btHide.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				isHide = !isHide;
			}
		});

		// button target
		TextButton btTarget = new TextButton("Target", skin);
		btTarget.setPosition(10, 420);
		btTarget.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// set target
				createDgTarget();
			}
		});

		// button export data
		TextButton btExport = new TextButton("RunDemo", skin);
		btExport.setPosition(110, 450);
		btExport.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				saveMap();
			}
		});

		// button size up
		TextButton btSizeUp = new TextButton("SizeUp", skin);
		btSizeUp.setPosition(210, 450);
		btSizeUp.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (!realArrs.isEmpty()) {
					// resize models
					Vector2 oldSize = new Vector2(realArrs.get(index).getWidth(), realArrs.get(index).getHeight());
					realArrs.get(index).setSize(oldSize.x * 1.1f, oldSize.y * 1.1f);
				}
			}
		});

		// button size down
		TextButton btSizeDown = new TextButton("SizeDown", skin);
		btSizeDown.setPosition(310 - 20, 450);
		btSizeDown.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// resize models
				Vector2 oldSize = new Vector2(realArrs.get(index).getWidth(), realArrs.get(index).getHeight());
				realArrs.get(index).setSize(oldSize.x / 1.1f, oldSize.y / 1.1f);
			}
		});

		// button rotate left
		TextButton btRotateLeft = new TextButton("RotRight", skin);
		btRotateLeft.setPosition(410 - 20, 450);
		btRotateLeft.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (!realArrs.isEmpty()) {
					realArrs.get(index).rotate(rotate - 5);
				}
			}
		});

		// button rotate right
		TextButton btRotateRight = new TextButton("RotLeft", skin);
		btRotateRight.setPosition(510 - 30, 450);
		btRotateRight.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (!realArrs.isEmpty()) {
					realArrs.get(index).rotate(rotate + 5);
				}
			}
		});

		// button hide net
		TextButton btHideNet = new TextButton("HideNet", skin);
		btHideNet.setPosition(610 - 60, 450);
		btHideNet.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				isHideNet = !isHideNet;
			}
		});

		// button delete object
		TextButton btDelete = new TextButton("Delete", skin);
		btDelete.setPosition(710 - 80, 450);
		btDelete.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (!realArrs.isEmpty()) {
					// remove touch model
					if (index <= realArrs.size() - 1 && realArrs.get(index) != null) {
						realArrs.remove(index);
					}
				}
			}
		});

		// button map
		TextButton btMap = new TextButton("Map", skin);
		btMap.setPosition(690, 450);
		btMap.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				indexMap++;
				if (indexMap > arrMaps.length - 1)
					indexMap = 0;
				tempMap = arrMaps[indexMap];
			}
		});

		// button reborn
		TextButton btReborn = new TextButton("Rebuilt", skin);
		btReborn.setPosition(735, 450);
		btReborn.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				showDialog();
			}
		});

		// add to stage
		stage.addActor(btExport);
		stage.addActor(btHide);
		stage.addActor(btRotateLeft);
		stage.addActor(btRotateRight);
		stage.addActor(btSizeDown);
		stage.addActor(btSizeUp);
		stage.addActor(btHideNet);
		stage.addActor(btDelete);
		stage.addActor(btMap);
		stage.addActor(btReborn);
		stage.addActor(btTarget);
	}

	private void initializeParams() {
		isHide = false;
		isHideNet = false;
		// create background
		tempMap = arrMaps[indexMap];
		// create jsons
		cmExport = new CommonModelList();
	}

	private void createList() {
		/* create models */
		if (realArrs == null)
			realArrs = new ArrayList<IconModel>();
		// create array icon models
		icoArrs = new ArrayList<IconModel>();

		// create icon models
		icoTom = new IconModel(Assets.taObjects.findRegion("tom-1"), 0, 350, IconModel.TOM, 0.5f, 0.5f);
		icoJerry = new IconModel(Assets.taObjects.findRegion("jerry-stand-1"), 50, 350, IconModel.JERRY, 0.5f, 0.5f);
		icoBook1 = new IconModel(Assets.taObjects.findRegion("book-1"), 100, 350, IconModel.BOOK_1, 0.2f, 0.2f);
		icoBook2 = new IconModel(Assets.taObjects.findRegion("book-2"), 150, 350, IconModel.BOOK_2, 0.5f, 0.5f);
		icoBook3 = new IconModel(Assets.taObjects.findRegion("book-3"), 200, 350, IconModel.BOOK_3, 0.5f, 0.5f);
		icoBook4 = new IconModel(Assets.taObjects.findRegion("book-4"), 250, 350, IconModel.BOOK_4, 0.2f, 0.2f);
		icoBook5 = new IconModel(Assets.taObjects.findRegion("book-5"), 50, 100, IconModel.BOOK_5, 0.2f, 0.2f);
		icoBridge1 = new IconModel(Assets.taObjects.findRegion("bridge-1"), 0, 300, IconModel.BRIDGE_1, 0.3f, 0.3f);
		icoBridge2 = new IconModel(Assets.taObjects.findRegion("bridge-2"), 50, 300, IconModel.BRIDGE_2, 0.3f, 0.3f);
		icoBridge3 = new IconModel(Assets.taObjects.findRegion("bridge-3"), 100, 300, IconModel.BRIDGE_3, 0.3f, 0.3f);
		icoBridge4 = new IconModel(Assets.taObjects.findRegion("bridge-4"), 150, 300, IconModel.BRIDGE_4, 0.3f, 0.3f);
		icoGround1 = new IconModel(Assets.taObjects.findRegion("ground-1"), 200, 300, IconModel.GROUND_1, 0.05f, 0.2f);
		icoGround2 = new IconModel(Assets.taObjects.findRegion("ground-2"), 250, 300, IconModel.GROUND_2, 0.05f, 0.2f);
		icoGround3 = new IconModel(Assets.taObjects.findRegion("ground-3"), 100, 100, IconModel.GROUND_3, 0.05f, 0.2f);
		icoHome = new IconModel(Assets.taObjects.findRegion("home"), 0, 250, IconModel.HOME, 0.5f, 0.5f);
		icoRuler = new IconModel(Assets.taObjects.findRegion("ruler"), 50, 250, IconModel.RULER, 0.3f, 0.5f);
		icoTerran1 = new IconModel(Assets.taObjects.findRegion("terran-1"), 100, 250, IconModel.TERRAN_1, 0.3f, 0.3f);
		icoTerran2 = new IconModel(Assets.taObjects.findRegion("terran-2"), 150, 250, IconModel.TERRAN_2, 0.3f, 0.3f);
		icoTerran3 = new IconModel(Assets.taObjects.findRegion("terran-3"), 200, 250, IconModel.TERRAN_3, 0.2f, 0.3f);
		icoTerran4 = new IconModel(Assets.taObjects.findRegion("terran-4"), 250, 250, IconModel.TERRAN_4, 0.15f, 0.15f);
		icoTerran5 = new IconModel(Assets.taObjects.findRegion("terran-5"), 150, 100, IconModel.TERRAN_5, 0.3f, 0.3f);
		icoTerran6 = new IconModel(Assets.taObjects.findRegion("terran-6"), 0, 200, IconModel.TERRAN_6, 0.3f, 0.2f);
		icoTerran7 = new IconModel(Assets.taObjects.findRegion("terran-7"), 50, 200, IconModel.TERRAN_7, 0.3f, 0.2f);
		icoTerran8 = new IconModel(Assets.taObjects.findRegion("terran-8"), 100, 200, IconModel.TERRAN_8, 0.3f, 0.3f);
		icoTerran9 = new IconModel(Assets.taObjects.findRegion("terran-9"), 150, 200, IconModel.TERRAN_9, 0.3f, 0.3f);
		icoTerran10 = new IconModel(Assets.taObjects.findRegion("terran-10"), 200, 200, IconModel.TERRAN_10, 0.3f,
				0.3f);
		icoTerran11 = new IconModel(Assets.taObjects.findRegion("terran-11"), 250, 200, IconModel.TERRAN_11, 0.15f,
				0.5f);
		icoTrap1 = new IconModel(Assets.taObjects.findRegion("trap-1"), 200, 100, IconModel.TRAP_1, 0.5f, 0.5f);
		icoTrap2 = new IconModel(Assets.taObjects.findRegion("trap-2"), 0, 150, IconModel.TRAP_2, 0.5f, 0.5f);
		icoWood1 = new IconModel(Assets.taObjects.findRegion("wood-1"), 50, 150, IconModel.WOOD_1, 0.3f, 0.3f);
		icoWood2 = new IconModel(Assets.taObjects.findRegion("wood-2"), 100, 150, IconModel.WOOD_2, 0.2f, 0.5f);
		icoWood3 = new IconModel(Assets.taObjects.findRegion("wood-3"), 150, 150, IconModel.WOOD_3, 0.2f, 0.5f);
		icoWood4 = new IconModel(Assets.taObjects.findRegion("wood-4"), 200, 150, IconModel.WOOD_4, 0.2f, 0.5f);
		icoWood5 = new IconModel(Assets.taObjects.findRegion("wood-5"), 250, 150, IconModel.WOOD_5, 0.3f, 0.3f);
		icoWood6 = new IconModel(Assets.taObjects.findRegion("wood-6"), 250, 100, IconModel.WOOD_6, 0.1f, 0.5f);
		icoWood7 = new IconModel(Assets.taObjects.findRegion("wood-7"), 0, 100, IconModel.WOOD_7, 0.5f, 0.5f);
		icoButter = new IconModel(Assets.taObjects.findRegion("butter"), 0, 50, IconModel.BUTTER, 0.5f, 0.5f);

		addIcon(icoJerry, icoTom, icoBook1, icoBook2, icoBook3, icoBook4, icoBook5, icoWood1, icoWood2, icoWood3,
				icoWood4, icoWood5, icoWood6, icoWood7, icoTerran1, icoTerran2, icoTerran3, icoTerran4, icoTerran5,
				icoTerran6, icoTerran7, icoTerran8, icoTerran9, icoTerran10, icoTerran11, icoBridge1, icoBridge2,
				icoBridge3, icoBridge4, icoGround1, icoGround2, icoGround3, icoHome, icoRuler, icoTrap1, icoTrap2,
				icoButter);
	}

	private void addIcon(IconModel... iconModels) {
		for (IconModel icon : iconModels) {
			icoArrs.add(icon);
		}
	}

	private void saveMap() {
		// save background
		IconModel icoBg = new IconModel(Assets.taObjects.findRegion("dark"), indexMap, 0, IconModel.MAP, 0, 0);
		realArrs.add(icoBg);
		// parsing real array
		for (IconModel icModel : realArrs) {
			CommonModel com = new CommonModel(icModel.getType(), icModel.getPosition(), icModel.getTarget(),
					new Vector2(icModel.getWidth(), icModel.getHeight()), icModel.getRotation());
			cmExport.addModel(com);
		}

		/* export json string */
		// for models
		json = new Json();
		strExport = json.toJson(cmExport);
		// print line
		System.out.println(strExport);
		// export file
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("TomAndJerry-Export.txt", "UTF-8");
		} catch (FileNotFoundException e) {
		} catch (UnsupportedEncodingException e) {
		}
		writer.println("=========================== STAGE ============================");
		writer.println(strExport);
		writer.close();
		// set screen
		coreGame.setScreen(new ScreenGame(coreGame, strExport, 0));
	}

	private void createDgTarget() {
		// show dgTarget
		if (dgTarget == null) {
			dgTarget = new Dialog("TARGET", skin);
			dgTarget.setSize(400, 250);
			dgTarget.setPosition(800 / 2 - 200, 480 / 2 - 120);
			dgTarget.setVisible(false);
			stage.addActor(dgTarget);

			// create labels
			Label lbX = new Label("Target X: ", skin);
			lbX.setPosition(20, 200);
			dgTarget.addActor(lbX);
			// create text fields
			tfX = new TextField("", skin);
			if (!realArrs.isEmpty())
				tfX.setText("" + realArrs.get(index).getTarget().x);
			tfX.setPosition(20, 160);
			tfX.setSize(360, 32);
			dgTarget.addActor(tfX);

			// create labels
			Label lbY = new Label("Target Y: ", skin);
			lbY.setPosition(20, 120);
			dgTarget.addActor(lbY);
			// create text fields
			tfY = new TextField("", skin);
			if (!realArrs.isEmpty())
				tfY.setText("" + realArrs.get(index).getTarget().y);
			tfY.setPosition(20, 80);
			tfY.setSize(360, 32);
			dgTarget.addActor(tfY);

			// create buttons
			TextButton btCancel = new TextButton("Cancel", skin);
			btCancel.setSize(100, 32);
			btCancel.setPosition(250, 10);
			btCancel.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					// hide dgTarget
					dgTarget.setVisible(false);
				}
			});
			dgTarget.addActor(btCancel);

			TextButton btOk = new TextButton("OK", skin);
			btOk.setSize(100, 32);
			btOk.setPosition(50, 10);
			btOk.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					// add target
					if (!realArrs.isEmpty()) {
						float x = 0, y = 0;
						if (tfX.getText() != null || !tfX.getText().equals(""))
							x = Float.parseFloat(tfX.getText());
						if (tfY.getText() != null || !tfY.getText().equals(""))
							y = Float.parseFloat(tfY.getText());
						realArrs.get(index).setTarget(new Vector2(x, y));
						dgTarget.setVisible(false);
					}
				}
			});
			dgTarget.addActor(btOk);
		} else {
			dgTarget.setVisible(true);
		}
	}

	private void createDialog() {
		// show dialog
		if (dialog == null) {
			dialog = new Dialog("REBUILT", skin);
			dialog.setSize(400, 250);
			dialog.setPosition(800 / 2 - 200, 480 / 2 - 120);
			dialog.setVisible(false);
			stage.addActor(dialog);
			// create labels
			Label lbModel = new Label("Models", skin);
			lbModel.setPosition(20, 200);
			dialog.addActor(lbModel);
			// create text fields
			tfModel = new TextField("", skin);
			tfModel.setPosition(20, 140);
			tfModel.setSize(360, 32);
			dialog.addActor(tfModel);

			// create buttons
			TextButton btCancel = new TextButton("Cancel", skin);
			btCancel.setSize(100, 32);
			btCancel.setPosition(250, 10);
			btCancel.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					// hide dialog
					dialog.setVisible(false);
				}
			});
			dialog.addActor(btCancel);
			TextButton btOk = new TextButton("OK", skin);
			btOk.setSize(100, 32);
			btOk.setPosition(50, 10);
			btOk.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					// remove all old objects
					realArrs.clear();
					// remove old temp
					if (icoTemp != null)
						icoTemp = null;
					// reborn
					if (tfModel.equals(""))
						tfModel.setText("{}");
					reBorn(tfModel.getText());
					// hide dialog
					dialog.setVisible(false);
				}
			});
			dialog.addActor(btOk);
		} else {
			dialog.setVisible(true);
		}
	}

	private void showDialog() {
		// reset field
		tfModel.setText("");
		// show dialog
		dialog.setVisible(true);
	}

	private void reBorn(String strModel) {
		/* reborn models */
		Json json = new Json();
		CommonModelList jsList = new CommonModelList();
		jsList = json.fromJson(CommonModelList.class, strModel);
		TextureRegion trModel = null;
		if (jsList != null) {
			for (int i = 0; i < jsList.getSize(); i++) {
				switch (jsList.getModel(i).getType()) {
				/* create tom */
				case IconModel.TOM:
					trModel = Assets.taObjects.findRegion("tom-1");
					break;
				/* create jerry */
				case IconModel.JERRY:
					trModel = Assets.taObjects.findRegion("jerry-stand-1");
					break;
				/* create wood */
				case IconModel.WOOD_1:
					trModel = Assets.taObjects.findRegion("wood-1");
					break;
				case IconModel.WOOD_2:
					trModel = Assets.taObjects.findRegion("wood-2");
					break;
				case IconModel.WOOD_3:
					trModel = Assets.taObjects.findRegion("wood-3");
					break;
				case IconModel.WOOD_4:
					trModel = Assets.taObjects.findRegion("wood-4");
					break;
				case IconModel.WOOD_5:
					trModel = Assets.taObjects.findRegion("wood-5");
					break;
				case IconModel.WOOD_6:
					trModel = Assets.taObjects.findRegion("wood-6");
					break;
				case IconModel.WOOD_7:
					trModel = Assets.taObjects.findRegion("wood-7");
					break;
				/* create terran */
				case IconModel.TERRAN_1:
					trModel = Assets.taObjects.findRegion("terran-1");
					break;
				case IconModel.TERRAN_2:
					trModel = Assets.taObjects.findRegion("terran-2");
					break;
				case IconModel.TERRAN_3:
					trModel = Assets.taObjects.findRegion("terran-3");
					break;
				case IconModel.TERRAN_4:
					trModel = Assets.taObjects.findRegion("terran-4");
					break;
				case IconModel.TERRAN_5:
					trModel = Assets.taObjects.findRegion("terran-5");
					break;
				case IconModel.TERRAN_6:
					trModel = Assets.taObjects.findRegion("terran-6");
					break;
				case IconModel.TERRAN_7:
					trModel = Assets.taObjects.findRegion("terran-7");
					break;
				case IconModel.TERRAN_8:
					trModel = Assets.taObjects.findRegion("terran-8");
					break;
				case IconModel.TERRAN_9:
					trModel = Assets.taObjects.findRegion("terran-9");
					break;
				case IconModel.TERRAN_10:
					trModel = Assets.taObjects.findRegion("terran-10");
					break;
				case IconModel.TERRAN_11:
					trModel = Assets.taObjects.findRegion("terran-11");
					break;
				/* create book */
				case IconModel.BOOK_1:
					trModel = Assets.taObjects.findRegion("book-1");
					break;
				case IconModel.BOOK_2:
					trModel = Assets.taObjects.findRegion("book-2");
					break;
				case IconModel.BOOK_3:
					trModel = Assets.taObjects.findRegion("book-3");
					break;
				case IconModel.BOOK_4:
					trModel = Assets.taObjects.findRegion("book-4");
					break;
				case IconModel.BOOK_5:
					trModel = Assets.taObjects.findRegion("book-5");
					break;
				/* create trap */
				case IconModel.TRAP_1:
					trModel = Assets.taObjects.findRegion("trap-1");
					break;
				case IconModel.TRAP_2:
					trModel = Assets.taObjects.findRegion("trap-2");
					break;
				/* create bridge */
				case IconModel.BRIDGE_1:
					trModel = Assets.taObjects.findRegion("bridge-1");
					break;
				case IconModel.BRIDGE_2:
					trModel = Assets.taObjects.findRegion("bridge-2");
					break;
				case IconModel.BRIDGE_3:
					trModel = Assets.taObjects.findRegion("bridge-3");
					break;
				case IconModel.BRIDGE_4:
					trModel = Assets.taObjects.findRegion("bridge-4");
					break;
				/* create ground */
				case IconModel.GROUND_1:
					trModel = Assets.taObjects.findRegion("ground-1");
					break;
				case IconModel.GROUND_2:
					trModel = Assets.taObjects.findRegion("ground-3");
					break;
				case IconModel.GROUND_3:
					trModel = Assets.taObjects.findRegion("ground-3");
					break;
				/* create home */
				case IconModel.HOME:
					trModel = Assets.taObjects.findRegion("home");
					break;
				/* create ruler */
				case IconModel.RULER:
					trModel = Assets.taObjects.findRegion("ruler");
					break;
				/* create butter */
				case IconModel.BUTTER:
					trModel = Assets.taObjects.findRegion("butter");
					break;
				/* create map */
				case IconModel.MAP:
					break;
				default:
					trModel = Assets.taObjects.findRegion("dark");
					break;
				}
				// reborn
				switch (jsList.getModel(i).getType()) {
				case IconModel.MAP:
					// reborn map
					IconModel icoBg = new IconModel(Assets.taObjects.findRegion("dark"),
							jsList.getModel(i).getPosition().x, 0, IconModel.MAP, 0, 0);
					realArrs.add(icoBg);
					// get map
					indexMap = (byte) jsList.getModel(i).getPosition().x;
					tempMap = arrMaps[indexMap];
					break;

				// reborn models
				default:
					IconModel icon = new IconModel(trModel, jsList.getModel(i).getPosition().x,
							jsList.getModel(i).getPosition().y, jsList.getModel(i).getType(), 1, 1);
					icon.setSize(jsList.getModel(i).getSize().x, jsList.getModel(i).getSize().y);
					icon.setOriginCenter(icon);
					icon.setRotation(jsList.getModel(i).getRotation());
					icon.setTarget(jsList.getModel(i).getTarget());
					realArrs.add(icon);
					break;
				}
			}
			// free models
			jsList.dispose();
			jsList = null;
		}
	}

	@Override
	public void update(float deltaTime) {

	}

	@Override
	public void draw() {
		renderBackgrounds();
		renderObjects();
		if (!isHideNet)
			renderShape();
		renderStage();
	}

	private void renderBackgrounds() {
		bgDrawable(true);
		batch.draw(tempMap, -50, -10, 900, 500);
		bgDrawable(false);
	}

	private void renderObjects() {
		objDrawable(true);
		// render real models
		if (realArrs != null && !realArrs.isEmpty()) {
			for (IconModel icModel : realArrs) {
				icModel.draw(batch);
			}
		}
		// render choose dialog
		if (!isHide)
			renderCanHideObjs();
		objDrawable(false);
	}

	private void renderCanHideObjs() {
		batch.draw(Assets.taObjects.findRegion("dark"), 0, 50, 300, 350);
		// render icon models
		if (!icoArrs.isEmpty()) {
			for (IconModel icModel : icoArrs) {
				icModel.draw(batch);
			}
		}
		// render temp model
		if (icoTemp != null)
			icoTemp.draw(batch);
	}

	private void renderShape() {
		shapeRender.setProjectionMatrix(camera.combined);
		shapeRender.begin(ShapeType.Line);
		shapeRender.setColor(Color.RED);
		for (int i = 1; i < 10; i++) {
			shapeRender.line(0, 50 * i, 800, 50 * i);
		}
		for (int i = 1; i < 16; i++) {
			shapeRender.line(50 * i, 0, 50 * i, 480);
		}
		shapeRender.end();
	}

	private void renderStage() {
		stage.draw();
	}

	@Override
	public void render(float deltaTime) {
		clearScreen(deltaTime);
		update(deltaTime);
		draw();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {

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
		stage.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
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
	int rotate = 0, index = 0;

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		camera.unproject(touchPoint.set(screenX, screenY, 0));
		/* touch models */
		// parsing icon array models
		if (!isHide) {
			for (IconModel icModel : icoArrs) {
				if (icModel.checkTouch(touchPoint.x, touchPoint.y)) {
					icoTemp = new IconModel(icModel.getRegion(), icModel.getX(), icModel.getY(), icModel.getType(), 1f,
							1f);
				}
			}
			// parsing realarrays, check condition
			if (touchPoint.x >= 0 && touchPoint.x <= 300 && touchPoint.y >= 50 && touchPoint.y <= 400) {
			} else {
				for (IconModel icModel : realArrs) {
					icModel.checkTouch(touchPoint.x, touchPoint.y);
				}
			}
		} else {
			for (IconModel icModel : realArrs) {
				icModel.checkTouch(touchPoint.x, touchPoint.y);
			}
		}
		// find last model
		for (int i = 0; i < realArrs.size(); i++) {
			if (realArrs.get(i).getTouch()) {
				// set origin center
				realArrs.get(i).setOriginCenter(realArrs.get(i));
				// get last model
				index = i;
				// reset rotation
				rotate = 0;
			}
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		camera.unproject(touchPoint.set(screenX, screenY, 0));
		// set icon temp
		if (icoTemp != null) {
			icoTemp.setTouch(false);
			// set default value
			icoTemp.setTarget(new Vector2());
			// add to realArrs
			realArrs.add(icoTemp);
			// get index
			index = realArrs.size() - 1;
			// set origin center
			realArrs.get(index).setOriginCenter(realArrs.get(index));
			// remove icoTemp
			icoTemp = null;
		}
		// parsing icon array
		for (IconModel icModel : icoArrs) {
			icModel.setTouch(false);
		}
		// parsing real array
		for (IconModel icModel : realArrs) {
			icModel.setTouch(false);
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		camera.unproject(touchPoint.set(screenX, screenY, 0));
		/* touch models */
		// move icon temp
		if (icoTemp != null) {
			icoTemp.setPosition(touchPoint.x - icoTemp.getWidth() / 2, touchPoint.y - icoTemp.getHeight() / 2);
		}
		// parsing real array
		for (IconModel icModel : realArrs) {
			if (icModel.getTouch()) {
				icModel.setPosition(touchPoint.x - icModel.getWidth() / 2, touchPoint.y - icModel.getHeight() / 2);
			}
		}
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
