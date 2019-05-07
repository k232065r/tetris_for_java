package tetris;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Color;

// キーボード用
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class Step05 extends Applet implements KeyListener{
	// アプレットの外見
	final private int BLOCK_SIZE = 30;
	final private int HEIGHT_NUM = 21;
	final private int WIDTH_NUM = 12;
	final private int WIDTH = BLOCK_SIZE * WIDTH_NUM; //360
	final private int HEIGHT = BLOCK_SIZE * HEIGHT_NUM; //630
	
	// アプレットの中身 
	private int[][] field = new int[WIDTH_NUM][HEIGHT_NUM];
	final int WALL_BLOCK = -1;
	final int NO_BLOCK = 0;
	final int MOVE_BLOCK = 1;
	final int HOLD_BLOCK = 2;
	
	// 移動ポインタ用
	private int pointX;
	private int pointY;
	
	/**
	 * 初期化処理
	 */
	public void init() {
		addKeyListener(this);
		init_field();
	}
	/**
	 * アプレット開始時の初期化処理
	 */
	public void start() {
		// アプレットにフォーカスを設定する（キーボードイベント検出のため）
		pointX = (WIDTH_NUM-1) / 2;
		pointY = 0;
		// スタート地点
		field[pointX][pointY] = MOVE_BLOCK;
	}
	/**
	 * 描画処理
	 */
	public void paint(Graphics g) {
		// 枠固定
		resize(WIDTH, HEIGHT);
		requestFocusInWindow();
		System.out.println("paint");
		repeat_paint(g);
	}
	/**
	 * fiedの初期化
	 */
	private void init_field() {
		for(int w = 0; w < WIDTH; w +=BLOCK_SIZE) {
			for(int h = 0; h < HEIGHT; h +=BLOCK_SIZE) {
				int x = w / BLOCK_SIZE;
				int y = h / BLOCK_SIZE;
				// 壁の描画
				if ( (x==0)  || (x==WIDTH_NUM-1)  || (y==HEIGHT_NUM-1)) {
					field[x][y] = WALL_BLOCK;
				} else {
					field[x][y] = NO_BLOCK;
				}
			}
		}
	}
	/* アプレットに枠とブロックを描写する
	 * @param g 	Graphicsをそのまま
	 */
	private void repeat_paint(Graphics g) {
		// 描画　※ブロック描画前にやると枠が消える
		for(int w = 0; w < WIDTH; w +=BLOCK_SIZE) {
			for(int h = 0; h < HEIGHT; h +=BLOCK_SIZE) {
				int x = w / BLOCK_SIZE;
				int y = h / BLOCK_SIZE;
				switch(field[x][y] ) {
				// 移動ブロックの描画
				case MOVE_BLOCK:
					g.setColor(Color.RED);
					g.fillRect(w, h, w+BLOCK_SIZE,  h+BLOCK_SIZE);
					break;
				// 固定ブロックの描画
				case HOLD_BLOCK:
					g.setColor(Color.PINK);
					g.fillRect(w, h, w+BLOCK_SIZE,  h+BLOCK_SIZE);
					break;
				// 何もないブロックの描画
				case NO_BLOCK:
					g.setColor(Color.WHITE);
					g.fillRect(w, h, w+BLOCK_SIZE,  h+BLOCK_SIZE);
					break;
				// 壁ブロックの描画
				case WALL_BLOCK:
					g.setColor(Color.PINK);
					g.fillRect(w, h, w+BLOCK_SIZE,  h+BLOCK_SIZE);
					break;
				}
				// 格子描画
				g.setColor(Color.BLACK);
				g.drawLine(w, h, w, h+BLOCK_SIZE);
				g.drawLine(w, h, w +BLOCK_SIZE, h);
			}
		}
	}
	/**
	 * 再描画前処理
	 */
	public void update() {
		System.out.println("update");
	}

	/**
	 * 終了処理
	 */
	public void stop() {
		System.out.println("stop");
	}
	/**
	 * キーが押された場合
	 * @param event
	 */
	public void keyPressed(KeyEvent event) {
		int keycode = event.getKeyCode();
		switch (keycode) {
		case KeyEvent.VK_RIGHT:
			// 壁にめり込まないか
			if ( (pointX+1) < (WIDTH_NUM - 1) ) {
				field[pointX][pointY] = NO_BLOCK;
				pointX = pointX + 1;
			}
			break;
		case KeyEvent.VK_LEFT:
			// 壁にめり込まないか
			if ( (pointX-1) > 0) {
				field[pointX][pointY] = NO_BLOCK;
				pointX = pointX - 1;
			}
			break;
		case KeyEvent.VK_DOWN:
			// 壁にめり込まないか
			if ( (pointY+1) < (HEIGHT_NUM - 1) ) {
				field[pointX][pointY] = NO_BLOCK;
				pointY = pointY + 1;
			} 
			if ( (field[pointX][pointY + 1] == WALL_BLOCK) || (field[pointX][pointY + 1] == HOLD_BLOCK) ) {
				field[pointX][pointY] = HOLD_BLOCK;
				repaint();
				start();
			}
			break;
		case KeyEvent.VK_SPACE:
			System.out.println("space");
			break;
		}
		field[pointX][pointY] = MOVE_BLOCK;
		repaint();
	}
	/**
	 * キーが離された場合
	 * @param event
	 */
	public void keyReleased(KeyEvent event) {
		System.out.println("release");
	}
	/**
	 * キーが押された場合（主に文字キー）
	 */
	public void keyTyped(KeyEvent event) {
		System.out.println("tyoe");
	}}
