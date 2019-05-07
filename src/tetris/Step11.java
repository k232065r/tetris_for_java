package tetris;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Color;

// キーボード用
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.awt.event.KeyEvent;

public class Step11 extends Applet implements KeyListener, Runnable {
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
	private Image background; 
	private Graphics bufferGraphics;

	// 移動ポインタ用
	// Point型を使う利点はないので内部変数で保持する
	private int pointX;
	private int pointY;
	private Random random = new Random();
	private int randomValue;
	private Color blockColor;
	private Point[] movingBlocks;
	
	// テトリスブロック
	int randomAns;
	private int rotationIndex = 0;
	private Point data[][][] =  { 
			// 1
			{ 	{ new Point(0, 0), new Point(-1, 0), new Point(1, 0), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, -1), new Point(0, 1), new Point(-1, 1) },
				{ new Point(0, 0), new Point(-1, 0), new Point(-1, -1), new Point(1, 0) } ,
				{ new Point(0, -1), new Point(1, -1), new Point(0, 0), new Point(0, 1) }},
			// 2
			{ { new Point(0, 0), new Point(0, -1), new Point(0, 1), new Point(1, 1) },
				{ new Point(0, 0), new Point(1, 0), new Point(-1, 0), new Point(-1, 1) },
				{ new Point(0, 0), new Point(0, -1), new Point(0, 1), new Point(-1, -1) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 0) } },
			// 3
			{ { new Point(0, 0), new Point(1, 0), new Point(0, 1), new Point(1, 1) },
				{ new Point(0, 0), new Point(1, 0), new Point(0, 1), new Point(1, 1) },
				{ new Point(0, 0), new Point(1, 0), new Point(0, 1), new Point(1, 1) },
				{ new Point(0, 0), new Point(1, 0), new Point(0, 1), new Point(1, 1) } },
			// 4
			{ { new Point(-1, 0), new Point(0, 0), new Point(1, 0), new Point(2, 0) },
				{ new Point(0, -1), new Point(0, 0), new Point(0, 1), new Point(0, 2) },
				{ new Point(-1, 0), new Point(0, 0), new Point(1, 0), new Point(2, 0) },
				{ new Point(0, -1), new Point(0, 0), new Point(0, 1), new Point(0, 2) } },
			// 5
			{ { new Point(0, -1), new Point(0, 0), new Point(-1, 0), new Point(1, -1) },
				{ new Point(0, 0), new Point(0, -1), new Point(1, 0), new Point(1, 1) },
				{ new Point(0, -1), new Point(0, 0), new Point(-1, 0), new Point(1, -1) },
				{ new Point(0, 0), new Point(0, -1), new Point(1, 0), new Point(1, 1) } },
			// 6
			{ { new Point(0, 0), new Point(1, 1), new Point(0, 1), new Point(-1, 1) },
				{ new Point(0, 0), new Point(-1, 1), new Point(-1, 0), new Point(-1, -1) },
				{ new Point(0, 0), new Point(1, -1), new Point(0, -1), new Point(-1, -1)},
				{ new Point(0, 0), new Point(1, 1), new Point(1, 0), new Point(1, -1) },
				 },
			// 7
			{ { new Point(0, 0), new Point(0, -1), new Point(-1, -1), new Point(1, 0) },
				{ new Point(0, 0), new Point(1, 0), new Point(1, -1), new Point(0, 1) },
				{ new Point(0, 0), new Point(0, -1), new Point(-1, -1), new Point(1, 0) },
				{ new Point(0, 0), new Point(1, 0), new Point(1, -1), new Point(0, 1) } } 
				};
	/**
	 * 初期化処理
	 */
	public void init() {
		addKeyListener(this);
		// スタート地点（アプレット開始時）
		pointX = (WIDTH_NUM-1) / 2;
		pointY = 0;
	}
	/**
	 *  fiedの初期化関数
	 */
	public void fieldInit(int[][] initField) {
		for(int w = 0; w < WIDTH; w +=BLOCK_SIZE) {
			for(int h = 0; h < HEIGHT; h +=BLOCK_SIZE) {
				int x = w / BLOCK_SIZE;
				int y = h / BLOCK_SIZE;
				// 壁の描画
				if ( (x==0)  || (x==WIDTH_NUM-1)  || (y==HEIGHT_NUM-1)) {
					initField[x][y] = WALL_BLOCK;
				} else {
					initField[x][y] = NO_BLOCK;
				}
			}
		}
	}
	/**
	 * アプレット開始時の初期化処理
	 */
	public void start() {
		if (GameTetris == null) {
			GameTetris = new Thread(this);
			GameTetris.start();
			fieldInit(field);
		}
		// ランダムで色を決定する
		randomValue = random.nextInt(777);
		randomAns = randomValue % 7;
		switch (randomAns) {
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
		movingBlocks = data[randomAns][rotationIndex];
		pointX = (WIDTH_NUM-1) / 2;
		pointY = 0;
		for (int index=0; index<movingBlocks.length; index++) {
			int tmpY = pointY+movingBlocks[index].y;
			int tmpX = pointX+movingBlocks[index].x;
			// スタートときのエラー対策
			if (tmpY < 0) {
				pointY = Math.abs(tmpY);
			}
		}
		updateNowPosition();
	}
	/**
	 * ブロックが今までいた場所を綺麗にする関数
	 */
	public void clearNowPosition() {
		for (int index=0; index<movingBlocks.length; index++) {
			int tmpY = pointY+movingBlocks[index].y;
			int tmpX = pointX+movingBlocks[index].x;
			field[tmpX][tmpY] = NO_BLOCK;
			fieldColor[tmpX][tmpY] = Color.white;
		}
	}
	/**
	 * ブロックが新しい場所に移動するときの関数
	 */
	public void updateNowPosition() {
		for (int index=0; index<movingBlocks.length; index++) {
			int tmpY = pointY+movingBlocks[index].y;
			int tmpX = pointX+movingBlocks[index].x;
			field[tmpX][tmpY] = MOVE_BLOCK;
			fieldColor[tmpX][tmpY] = blockColor;
		}
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
		repeat_paint();
		g.drawImage(background, 0, 0, this);
	}

	/* アプレットに枠とブロックを描写する
	 * @param g 	Graphicsをそのまま
	 */
	private void repeat_paint() {
		background = createImage(WIDTH, HEIGHT); 
		bufferGraphics = background.getGraphics();
		
		// 描画　※ブロック描画前にやると枠が消える
		for(int w = 0; w < WIDTH; w +=BLOCK_SIZE) {
			for(int h = 0; h < HEIGHT; h +=BLOCK_SIZE) {
				int x = w / BLOCK_SIZE;
				int y = h / BLOCK_SIZE;
				switch(field[x][y]) {
				// 移動ブロック(MOVE_BLOCK)と固定ブロック(HOLD_BLOCK)の場合
				// 特に何もしない
				// 何もないブロックの描画
				case NO_BLOCK:
					fieldColor[x][y] = Color.WHITE;
					break;
				// 壁ブロックの描画
				case WALL_BLOCK:
					fieldColor[x][y] = Color.GRAY;
					break;
				}
				paintBlock(bufferGraphics, w, h, fieldColor[x][y]);
				// 格子描画
				bufferGraphics.setColor(Color.BLACK);
				bufferGraphics.drawLine(w, h, w, h+BLOCK_SIZE);
				bufferGraphics.drawLine(w, h, w +BLOCK_SIZE, h);
				System.out.print(field[x][y]);
			}
			System.out.println("");
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
	/**
	 * スレッドを1秒ごとに動かすための必須関数
	 */
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
		//キー連打はバグの元
		int keycode = event.getKeyCode();
		switch (keycode) {
		case KeyEvent.VK_RIGHT:
			horizonMoveBlocks(true);
			break;
		case KeyEvent.VK_LEFT:
			horizonMoveBlocks(false);
			break;
		case KeyEvent.VK_DOWN:
			downBlocks();
			break;
		case KeyEvent.VK_SPACE:
			clearNowPosition();
			if (rotationIndex < movingBlocks.length-1) {
				rotationIndex = rotationIndex + 1;
			} else {
				rotationIndex = 0;
			}
			movingBlocks = data[randomAns][rotationIndex];
			updateNowPosition();
			break;
		}
		repaint();
	}
	/**
	 * ブロックが左右に動くときの関数
	 * @param isRight 右か左かのフラグ
	 */
	public void horizonMoveBlocks(boolean isRight) {
		int diff = 0;
		if (isRight) {
			diff = 1;
		} else { //left
			diff = -1;
		}
		boolean doChange = true;
		for (int index=0; index<movingBlocks.length; index++) {
			int tmpX = pointX + movingBlocks[index].x;
			int tmpY = pointY + movingBlocks[index].y;
			if ( (field[tmpX+diff][tmpY] == WALL_BLOCK) ||
					(field[tmpX+diff][tmpY] == HOLD_BLOCK) ){
				doChange = false;
			}
		}
		if (doChange) {
			clearNowPosition();
			pointX = pointX + diff;
			updateNowPosition();
		}
	}
	/**
	 * ブロックが下に行くときの関数
	 * （下ボタンと時間で落ちるとき）
	 */
	public void downBlocks() {
		boolean isHold = false;
		boolean isDown = false;
		// 壁にめり込まないか
		for (int index=0; index<movingBlocks.length; index++) {
			int tmpY = pointY + movingBlocks[index].y;
			int tmpX = pointX + movingBlocks[index].x;
			if ( (field[tmpX][tmpY + 1] == WALL_BLOCK) ||
					(field[tmpX][tmpY + 1] == HOLD_BLOCK) ){
				isHold = true;
				break;
			}
			if ( (field[tmpX][tmpY + 1] != WALL_BLOCK) &&
					(field[tmpX][tmpY + 1] != HOLD_BLOCK) ) {
				isDown = true;
			} 
		}
		if (isHold) {
			holdBlocks();
			// ラインを消すか確認する（削除の関数の中）
			deleteLinesCheck();
			repaint();
			start();
		} else if (isDown) {
			clearNowPosition();
			pointY = pointY + 1;
			updateNowPosition();
		}
	}
	/**
	 * ブロックの固定関数
	 */
	public void holdBlocks() {
		for (int index=0; index<movingBlocks.length; index++) {
			int tmpY = pointY+movingBlocks[index].y;
			int tmpX = pointX+movingBlocks[index].x;
			field[tmpX][tmpY] = HOLD_BLOCK;
			fieldColor[tmpX][tmpY] = blockColor;
		}
	}
	public void deleteLinesCheck() {
		ArrayList<Integer> lineNumbers = new ArrayList<>();
		boolean isDelete;
		// 壁の内部の話なので少し補正を加える
		for(int y = HEIGHT_NUM-1; y > 0 ; y--) {
			isDelete = true;
			for(int x = 1; x < WIDTH_NUM-1; x++) {
				if (field[x][y] != 2) {
					isDelete = false;
				}
			}
			if (isDelete) {
				lineNumbers.add(y);
			}
		}
		if ( lineNumbers.size() > 0) {
			// 小さい値から削除していきたいのでソートする
			Collections.sort(lineNumbers);
			for(int y :lineNumbers) {
				deleteLines(y);
			}
		}
	}
	public void deleteLines(int deleteLineNum) {
		int[][] tmpField = new int[WIDTH_NUM][HEIGHT_NUM];
		fieldInit(tmpField);
		// 条件の1より大きいは削除した1行分を補正した値
		for(int y = HEIGHT_NUM-1; y > 1 ; y--) {
			for(int x = 1; x < WIDTH_NUM-1; x++) {
				if (y <= deleteLineNum) {
					tmpField[x][y] = field[x][y-1];
				} else {
					tmpField[x][y] = field[x][y];
				}
			}
		}
		field = tmpField;
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
