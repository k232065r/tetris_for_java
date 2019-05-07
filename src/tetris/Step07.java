package tetris;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Color;

// キーボード用
import java.awt.event.KeyListener;
import java.util.Random;
import java.awt.event.KeyEvent;

public class Step07 extends Applet implements KeyListener, Runnable {
	// アプレットの外見
	final private int BLOCK_SIZE = 30;
	final private int HEIGHT_NUM = 21;
	final private int WIDTH_NUM = 12;
	final private int WIDTH = BLOCK_SIZE * WIDTH_NUM; //360
	final private int HEIGHT = BLOCK_SIZE * HEIGHT_NUM; //630
	
	// アプレットの中身 
	private int[][] field = new int[WIDTH_NUM][HEIGHT_NUM];
	private Color[][] fieldColor = new Color[WIDTH_NUM][HEIGHT_NUM];
	final int WALL_BLOCK = -1;
	final int NO_BLOCK = 0;
	final int MOVE_BLOCK = 1;
	final int HOLD_BLOCK = 2;
	private Thread GameTetris;

	// 移動ポインタ用
	private int pointX;
	private int pointY;
	private Random random = new Random();
	private int randomValue;
	private Color blockColor;
	
	/**
	 * 初期化処理
	 */
	public void init() {
		addKeyListener(this);
		// fiedの初期化
		for(int w = 0; w < WIDTH; w +=BLOCK_SIZE) {
			for(int h = 0; h < HEIGHT; h +=BLOCK_SIZE) {
				int x = w / BLOCK_SIZE;
				int y = h / BLOCK_SIZE;
				// 壁の描画
				if ( (x==0)  || (x==WIDTH_NUM-1)  || (y==HEIGHT_NUM-1)) {
					field[x][y] = WALL_BLOCK;
					fieldColor[x][y] = Color.GRAY;
				} else {
					field[x][y] = NO_BLOCK;
					fieldColor[x][y] = Color.WHITE;
				}
			}
		}
		
		// スタート地点（アプレット開始時）
		pointX = (WIDTH_NUM-1) / 2;
		pointY = 0;
		GameTetris = new Thread(this);
		GameTetris.start();
	}
	/**
	 * アプレット開始時の初期化処理
	 */
	public void start() {
		// ランダムで色を決定する
		randomValue = random.nextInt(777);
		switch (randomValue % 7) {
		case 0:
			blockColor = Color.PINK;
			break;
		case 1:
			blockColor = Color.BLUE;
			break;
		case 2:
			blockColor = Color.GREEN;
			break;
		case 3:
			blockColor = Color.ORANGE;
			break;
		case 4:
			blockColor = Color.MAGENTA;
			break;
		case 5:
			blockColor = Color.RED;
			break;
		case 6:
			blockColor = Color.YELLOW;
			break;
		}
		
		// スタート地点（リスタート含む）
		pointX = (WIDTH_NUM-1) / 2;
		pointY = 0;
		field[pointX][pointY] = MOVE_BLOCK;
		fieldColor[pointX][pointY] = blockColor;
	}
	/**
	 * 描画処理
	 */
	public void paint(Graphics g) {
		System.out.println("paint");
		// 枠固定
		resize(WIDTH, HEIGHT);
		// アプレットにフォーカスを設定する（キーボードイベント検出のため）
		requestFocusInWindow();
		repeat_paint(g);
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
					paintBlock(g, w, h, fieldColor[x][y]);
					break;
				// 固定ブロックの描画
				case HOLD_BLOCK:
					paintBlock(g, w, h, fieldColor[x][y]);
					break;
				// 何もないブロックの描画
				case NO_BLOCK:
					paintBlock(g, w, h, Color.WHITE);
					break;
				// 壁ブロックの描画
				case WALL_BLOCK:
					paintBlock(g, w, h, Color.GRAY);
					break;
				}
				if(y== 1) {
					System.out.print(x);
					System.out.println(fieldColor[x][y]);
				}
				// 格子描画
				g.setColor(Color.BLACK);
				g.drawLine(w, h, w, h+BLOCK_SIZE);
				g.drawLine(w, h, w +BLOCK_SIZE, h);
			}
		}
	}
	/**
	 * ブロックの着色関数
	 * @param g Graphics
	 * @param w width
	 * @param h height
	 * @param color
	 */
	public void paintBlock(Graphics g, int w, int h, Color color) {
		g.setColor(color);
		g.fillRect(w, h, w+BLOCK_SIZE,  h+BLOCK_SIZE);
	}
	@Override
	public void run() {
		// 無限ループは仕様　アプレットを終了することで問題はない
		while(true) {
			downBlocks();
			repaint();
			try {
				Thread.sleep(800); //正しくは1000ms
			} catch(InterruptedException e) {
				return;
			}
		}
	}
	/**
	 * 再描画前処理
	 */
	public void update(Graphics g) {
		paint(g);
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
			System.out.println("press right");
			// 壁にめり込まないか
			if ( (pointX+1) < (WIDTH_NUM - 1)  &&
					(field[pointX+1][pointY] != HOLD_BLOCK) ) {
				clearNowPosition();
				pointX = pointX + 1;
				updateNowPosition();
			}
			break;
		case KeyEvent.VK_LEFT:
			System.out.println("press left");
			// 壁にめり込まないか
			if ( ((pointX-1) > 0)  &&
					(field[pointX-1][pointY] != HOLD_BLOCK) ) {
				clearNowPosition();
				pointX = pointX - 1;
				updateNowPosition();
			}
			break;
		case KeyEvent.VK_DOWN:
			System.out.println("press down");
			downBlocks();
			break;
		case KeyEvent.VK_SPACE:
			System.out.println("space");
			break;
		}
		repaint();
	}
	public void downBlocks() {
		// 壁にめり込まないか
		if ( ( (pointY+1) < (HEIGHT_NUM - 1)) &&
			(field[pointX][pointY + 1] != HOLD_BLOCK) ) {
			clearNowPosition();
			pointY = pointY + 1;
			updateNowPosition();
		}
		System.out.println(pointY);
		if ( (field[pointX][pointY + 1] == WALL_BLOCK) || (field[pointX][pointY + 1] == HOLD_BLOCK) ) {
			field[pointX][pointY] = HOLD_BLOCK;
			fieldColor[pointX][pointY] = blockColor;
			repaint();
			start();
		}
	}
	public void clearNowPosition() {
		System.out.print("clearNowPosition:");
		System.out.println(pointX);
		field[pointX][pointY] = NO_BLOCK;
		fieldColor[pointX][pointY] = Color.white;
	}
	public void updateNowPosition() {
		field[pointX][pointY] = MOVE_BLOCK;
		fieldColor[pointX][pointY] = blockColor;
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
