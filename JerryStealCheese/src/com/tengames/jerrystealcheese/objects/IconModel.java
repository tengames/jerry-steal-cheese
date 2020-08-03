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

import woodyx.basicapi.sprite.ObjectSprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class IconModel extends ObjectSprite {
	public static final byte TOM = 0;
	public static final byte JERRY = 1;
	public static final byte BRIDGE_1 = 2;
	public static final byte BRIDGE_2 = 3;
	public static final byte BRIDGE_3 = 4;
	public static final byte BRIDGE_4 = 5;
	public static final byte GROUND_1 = 6;
	public static final byte HOME = 7;
	public static final byte MAP = 8;
	public static final byte TERRAN_1 = 9;
	public static final byte TERRAN_2 = 10;
	public static final byte TERRAN_3 = 11;
	public static final byte TERRAN_4 = 12;
	public static final byte TERRAN_5 = 13;
	public static final byte TERRAN_6 = 14;
	public static final byte TERRAN_7 = 15;
	public static final byte TERRAN_8 = 16;
	public static final byte TERRAN_9 = 17;
	public static final byte TERRAN_10 = 18;
	public static final byte TERRAN_11 = 19;
	public static final byte WOOD_1 = 20;
	public static final byte WOOD_2 = 21;
	public static final byte WOOD_3 = 22;
	public static final byte WOOD_4 = 23;
	public static final byte WOOD_5 = 24;
	public static final byte WOOD_6 = 25;
	public static final byte WOOD_7 = 26;
	public static final byte TRAP_1 = 27;
	public static final byte TRAP_2 = 28;
	public static final byte RULER = 29;
	public static final byte BOOK_1 = 30;
	public static final byte BOOK_2 = 31;
	public static final byte BOOK_3 = 32;
	public static final byte BOOK_4 = 33;
	public static final byte BOOK_5 = 34;
	public static final byte GROUND_2 = 35;
	public static final byte GROUND_3 = 36;
	public static final byte BUTTER = 37;

	private Vector2 target;
	private boolean isTouch, canTouch;
	private byte type;

	public IconModel(TextureRegion texture, float x, float y, byte type, float scaleX, float scaleY) {
		super(texture, x, y);
		this.type = type;
		this.setScale(scaleX, scaleY);
		setTouch(false);
		setCanTouch(false);
	}

	public byte getType() {
		return type;
	}

	public boolean getTouch() {
		return isTouch;
	}

	public void setTouch(boolean isTouch) {
		this.isTouch = isTouch;
	}

	public boolean getCanTouch() {
		return canTouch;
	}

	public void setCanTouch(boolean canTouch) {
		this.canTouch = canTouch;
	}

	public Vector2 getTarget() {
		return target;
	}

	public void setTarget(Vector2 target) {
		this.target = target;
	}

	public boolean checkTouch(float x, float y) {
		if (x >= this.getX() && x <= (this.getX() + this.getWidth() * this.getScaleX()) && y >= this.getY()
				&& (y <= (this.getY() + this.getHeight() * this.getScaleY()))) {
			setTouch(true);
			return true;
		}
		return false;
	}
}