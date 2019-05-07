package tetris;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Color;

// キーボード用
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class Step04 extends Applet implements KeyListener{
	final private int BLOCK_SIZE = 30;
	final private int HEIGHT_NUM = 21;
	final private int WIDTH_NUM = 12;
	final private int WIDTH = BLOCK_SIZE * WIDTH_NUM; //360
	final private int HEIGHT = BLOCK_SIZE * HEIGHT_NUM; //630
	private boolean[][] field = new boolean[WIDTH_NUM][HEIGHT_NUM];
	
	private int pointX;
	private int pointY;
	
	/**
	 * 初期化処理
	 */
	public void init() {
		addKeyListener(this);
	}
	/**
	 * アプレット開始時の初期化処理
	 */
	public void start() {
		// アプレットにフォーカスを設定する（キーボードイベント検出のため）
		pointX = (WIDTH_NUM-1) / 2;
		pointY = 0;
		// スタート地点
		field[pointX][pointY] = true;
	}
	/**
	 * 描画処理
	 */
	public void paint(Graphics g) {
		// 枠固定
		resize(WIDTH, HEIGHT);
		requestFocusInWindow();
		System.out.println("paint");
		painting(g);
	}
	/**
	 * アプレットに枠とブロックを描写する
	 * @param g 	Graphicsをそのまま
	 */
	private void painting(Graphics g) {
		// 描画　※ブロック描画前にやると枠が消える
		for(int w = 0; w < WIDTH; w +=BLOCK_SIZE) {
			for(int h = 0; h < HEIGHT; h +=BLOCK_SIZE) {
				int x = w / BLOCK_SIZE;
				int y = h / BLOCK_SIZE;
				// 壁の描画
				if ( (x==0)  || (x==WIDTH_NUM-1)  || (y==HEIGHT_NUM-1)) {
					g.setColor(Color.PINK);
					g.fillRect(w, h, w+BLOCK_SIZE,  h+BLOCK_SIZE);
				} else {
					// 移動ブロックの描画
					if (field[x][y] == true) {
						g.setColor(Color.RED);
						g.fillRect(w, h, w+BLOCK_SIZE,  h+BLOCK_SIZE);
					// 何もないブロックの描画
					} else {
						g.setColor(Color.WHITE);
						g.fillRect(w, h, w+BLOCK_SIZE,  h+BLOCK_SIZE);
					}
				}
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
		System.out.print("pointX:");
		System.out.println(pointX);
		System.out.print("pointY:");
		System.out.println(pointY);
		switch (keycode) {
			case KeyEvent.VK_RIGHT:
				System.out.println("right");
				// 壁にめり込まないか
				if ( (pointX+1) < (WIDTH_NUM - 1) ) {
					field[pointX][pointY] = false;
					pointX = pointX + 1;
				}
				break;
			case KeyEvent.VK_LEFT:
				System.out.println("left");
				// 壁にめり込まないか
				if ( (pointX-1) > 0) {
					field[pointX][pointY] = false;
					pointX = pointX - 1;
				}
				break;
			case KeyEvent.VK_DOWN:
				System.out.println("press down");
				// 壁にめり込まないか
				if ( (pointY+1) < (HEIGHT_NUM - 1) ) {
					field[pointX][pointY] = false;
					pointY = pointY + 1;
				}
				break;
			case KeyEvent.VK_SPACE:
				System.out.println("space");
				break;
		}
		field[pointX][pointY] = true;
		repaint();
	}
	/**
	 * キーが離された場合(未使用)
	 * @param event
	 */
	public void keyReleased(KeyEvent event) {
	}
	/**
	 * キーが押された場合（主に文字キー）(未使用)
	 */
	public void keyTyped(KeyEvent event) {
	}}
