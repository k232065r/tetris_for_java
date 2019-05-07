package tetris;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Color;

// マウス用
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class Step03 extends Applet implements MouseListener{
	final private int BLOCK_SIZE = 30;
	final private int HEIGHT_NUM = 21;
	final private int WIDTH_NUM = 12;
	final private int WIDTH = BLOCK_SIZE * WIDTH_NUM; //360
	final private int HEIGHT = BLOCK_SIZE * HEIGHT_NUM; //630
	private boolean[][] field = new boolean[WIDTH_NUM][HEIGHT_NUM];
	
	/**
	 * 初期化処理
	 */
	public void init() {
		addMouseListener(this);
	}
	/**
	 * アプレット開始時の初期化処理
	 */
	public void start() {
		System.out.println("start");
	}
	/**
	 * 描画処理
	 */
	public void paint(Graphics g) {
		// 枠固定
		resize(WIDTH, HEIGHT);
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
				if (field[x][y] == true) {
					g.setColor(Color.GREEN);
					g.fillRect(w, h, w+BLOCK_SIZE,  h+BLOCK_SIZE);
				} else {
					g.setColor(Color.WHITE);
					g.fillRect(w, h, w+BLOCK_SIZE,  h+BLOCK_SIZE);
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
	 * 再描画処理
	 */
	public void repaint(Graphics g) {
		painting(g);
	}
	/**
	 * 終了処理
	 */
	public void stop() {
		System.out.println("stop");
	}

	// マウスイベント用
	/**
	 * マウスがクリックされた
	 * @param event
	 */
	public void mouseClicked(MouseEvent event) {
		// ブロック分修正した座標を取得する
		int clickedX = event.getX() / BLOCK_SIZE;
		int clickedY = event.getY() / BLOCK_SIZE;
		if (field[clickedX][clickedY]) {
			field[clickedX][clickedY] = false;
		} else {
			field = new boolean[WIDTH_NUM][HEIGHT_NUM];
			field[clickedX][clickedY] = true;
		}
		repaint();
	}
	// マウスイベント用　以降未使用
	/**
	 * マウスがアプレットに乗った(未使用)
	 * @param event
	 */
	public void mouseEntered(MouseEvent e) {
		
	}
	/**
	 * マウスがアプレットの外に出た(未使用)
	 * @param event
	 */
	public void mouseExited(MouseEvent event) {
		
	}
	/**
	 * マウスが押された(未使用)
	 * @param event
	 */
	public void mousePressed(MouseEvent event) {
			
	}
	/**
	 * マウスが押された状態から離れた(未使用)
	 * @param event
	 */
	public void mouseReleased(MouseEvent event) {
		
	}
}
