package tetris;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Color;
import java.awt.Font;
// キーボード用
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.awt.event.KeyEvent;

public class Step12 extends Applet implements KeyListener, Runnable {
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
	private int randomAns;
	private int nextRandomAns;
	private Color blockColor = Color.WHITE;
	private Color nextBlockColor = Color.WHITE;
	private Point[] movingBlocks;
	private Point[] nextMovingBlocks;
	
	// テトリスブロック
	private boolean playGame = true;
	private int score = 0;
	private int deleteLineNum = 0;
	private int rotationIndex = 0;
	private Point data[][][] =  { 
					// 逆L
					{ {  new Point(0, 0),new Point(0, -1), new Point(1, -1), new Point(0, 1) },
							{ new Point(0, 0), new Point(-1, 0), new Point(1, 0), new Point(1, 1) },
							{ new Point(0, 0), new Point(0, -1), new Point(0, 1), new Point(-1, 1) },
							{ new Point(0, 0), new Point(-1, 0), new Point(-1, -1), new Point(1, 0) } },
					// L
					{ { new Point(0, 0), new Point(0, -1), new Point(0, 1), new Point(1, 1) },
							{ new Point(0, 0), new Point(1, 0), new Point(-1, 0), new Point(-1, 1) },
							{ new Point(0, 0), new Point(0, -1), new Point(0, 1), new Point(-1, -1) },
							{ new Point(0, 0), new Point(1, 0), new Point(-1, 0), new Point(1, -1) } },
					// □
					{ { new Point(0, 0), new Point(1, 0), new Point(0, 1), new Point(1, 1) },
							{ new Point(0, 0), new Point(1, 0), new Point(0, 1), new Point(1, 1) },
							{ new Point(0, 0), new Point(1, 0), new Point(0, 1), new Point(1, 1) },
							{ new Point(0, 0), new Point(1, 0), new Point(0, 1), new Point(1, 1) } },
					// I
					{ { new Point(0, 0), new Point(-1, 0), new Point(1, 0), new Point(2, 0) },
							{ new Point(0, 0), new Point(0, -1), new Point(0, 1), new Point(0, 2) },
							{ new Point(0, 0), new Point(-1, 0), new Point(1, 0), new Point(2, 0) },
							{ new Point(0, 0), new Point(0, -1), new Point(0, 1), new Point(0, 2) } },
					// 逆Z
					{ { new Point(0, 0),new Point(0, -1),  new Point(-1, 0), new Point(1, -1) },
							{ new Point(0, 0), new Point(0, -1), new Point(1, 0), new Point(1, 1) },
							{ new Point(0, 0),new Point(0, -1),  new Point(-1, 0), new Point(1, -1) },
							{ new Point(0, 0), new Point(0, -1), new Point(1, 0), new Point(1, 1) } },
					// ┣
					{ { new Point(0, 0), new Point(0, -1), new Point(-1, 0), new Point(1, 0) },
							{ new Point(0, 0), new Point(0, -1), new Point(0, 1), new Point(1, 0) },
							{ new Point(0, 0), new Point(-1, 0), new Point(1, 0), new Point(0, 1) },
							{ new Point(0, 0), new Point(-1, 0), new Point(0, -1), new Point(0, 1) } },
					// Z
					{ { new Point(0, 0), new Point(0, -1), new Point(-1, -1), new Point(1, 0) },
							{ new Point(0, 0), new Point(1, 0), new Point(1, -1), new Point(0, 1) },
							{ new Point(0, 0), new Point(0, -1), new Point(-1, -1), new Point(1, 0) },
							{ new Point(0, 0), new Point(1, 0), new Point(1, -1), new Point(0, 1) } } };
	/**
	 * 初期化処理
	 */
	public void init() {
		addKeyListener(this);
		// スタート地点（アプレット開始時）
		pointX = (WIDTH_NUM-1) / 2;
		pointY = 0;
		randomValue = random.nextInt(777);
		nextRandomAns = randomValue % 7;
		nextMovingBlocks = choiseBlocks(nextRandomAns);
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
		// スタート地点（リスタート含む）
		rotationIndex = 0;
		randomAns = nextRandomAns;
		movingBlocks = nextMovingBlocks;
		blockColor = nextBlockColor;
		// 次のブロック更新
		randomValue = random.nextInt(777);
		nextRandomAns = randomValue % 7;
		nextMovingBlocks = choiseBlocks(nextRandomAns);
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
		int[][] checkField = checkHoldBlock(0);
		playGame = checkCrashBlocks(movingBlocks, checkField);
		if (playGame) {
			updateNowPosition();
		} else {
			repaint();
		}
	}
	public Point[] choiseBlocks(int randamNum) {
		// ランダムで色を決定する
		switch (randamNum) {
		case 0:
			nextBlockColor = Color.PINK;
			break;
		case 1:
			nextBlockColor = Color.BLUE;
			break;
		case 2:
			nextBlockColor = Color.GREEN;
			break;
		case 3:
			nextBlockColor = Color.ORANGE;
			break;
		case 4:
			nextBlockColor = Color.MAGENTA;
			break;
		case 5:
			nextBlockColor = Color.RED;
			break;
		case 6:
			nextBlockColor = Color.YELLOW;
			break;
		}
		return data[randamNum][rotationIndex];
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
		// 枠固定 得点もろもろ用に拡張
		resize(WIDTH+(BLOCK_SIZE*6), HEIGHT);
		// アプレットにフォーカスを設定する（キーボードイベント検出のため）
		requestFocusInWindow();
		repeat_paint(playGame);
		g.drawImage(background, 0, 0, this);
	}

	/* アプレットに枠とブロックを描写する
	 * @param g 	Graphicsをそのまま
	 */
	private void repeat_paint(boolean playGame) {
		background = createImage(WIDTH+(BLOCK_SIZE*6), HEIGHT); 
		bufferGraphics = background.getGraphics();
		
		// 描画　※ブロック描画前にやると枠が消える
		for(int x = 0; x < WIDTH_NUM; x++) {
			for(int y = 0; y < HEIGHT_NUM; y ++) {
				int w = x * BLOCK_SIZE;
				int h = y * BLOCK_SIZE;
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
				bufferGraphics.drawLine(WIDTH, 0, WIDTH , HEIGHT);
				bufferGraphics.drawLine(0, HEIGHT, WIDTH , HEIGHT);
				System.out.print(field[x][y]);
			}
			System.out.println("");
		}
		// ゲーム枠外描写
		// 次ブロック空間描写
		// 白
		bufferGraphics.setColor(Color.WHITE);
		bufferGraphics.fillRect(WIDTH + BLOCK_SIZE, BLOCK_SIZE, (BLOCK_SIZE*4),  BLOCK_SIZE*4);
		// 次ブロック
		for(int index=0; index<nextMovingBlocks.length;index++) {
			int tmpX = (nextMovingBlocks[index].x+1)+WIDTH_NUM+1;
			int tmpY = (nextMovingBlocks[index].y+1)+1;
			if ( (nextRandomAns != 2) && (nextRandomAns != 3) ) {
				tmpY = tmpY + 1;
			}
			// 描写位置を補正してブロックを描写する
			paintBlock(bufferGraphics, tmpX*BLOCK_SIZE, tmpY*BLOCK_SIZE, nextBlockColor);
		}
		// 枠線
		for(int x = 1; x <=4; x++) {
			for(int y = 1; y <=4; y ++) {
				int w = x * BLOCK_SIZE + WIDTH;
				int h = y * BLOCK_SIZE;
				bufferGraphics.setColor(Color.BLACK);
				bufferGraphics.drawLine(w, h, w, h+BLOCK_SIZE);
				bufferGraphics.drawLine(w, h, w +BLOCK_SIZE, h);
			}
		}
		bufferGraphics.drawLine(WIDTH+(BLOCK_SIZE*5), BLOCK_SIZE, WIDTH+(BLOCK_SIZE*5) , BLOCK_SIZE*5);
		bufferGraphics.drawLine(WIDTH+BLOCK_SIZE, BLOCK_SIZE*5, WIDTH+(BLOCK_SIZE*5), BLOCK_SIZE*5);
		
		//スコア描写
		bufferGraphics.setColor(Color.GREEN);
		Font font = new Font("SensSerif", Font.BOLD, 30);
		bufferGraphics.setFont(font);
		bufferGraphics.drawString("SCORE:", BLOCK_SIZE*WIDTH_NUM+10, BLOCK_SIZE*13);
		bufferGraphics.drawString(String.valueOf(score), BLOCK_SIZE*WIDTH_NUM+10, BLOCK_SIZE*15);
		font = new Font("SensSerif", Font.BOLD, 23);
		bufferGraphics.setFont(font);
		bufferGraphics.drawString("CLEAR LINES:", BLOCK_SIZE*WIDTH_NUM+10, BLOCK_SIZE*17);
		bufferGraphics.drawString(String.valueOf(deleteLineNum), BLOCK_SIZE*WIDTH_NUM+10, BLOCK_SIZE*18);
		// ゲームオーバーしているかどうか
		if(playGame== false) {
			bufferGraphics.setColor(Color.BLACK);
			font = new Font("SensSerif", Font.BOLD, 45);
			bufferGraphics.setFont(font);
			bufferGraphics.drawString("GAME OVER!!", BLOCK_SIZE*1, BLOCK_SIZE*5);
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
		g.fillRect(w, h, BLOCK_SIZE, BLOCK_SIZE);
	}
	/**
	 * スレッドを1秒ごとに動かすための必須関数
	 */
	@Override
	public void run() {
		// 無限ループは仕様　アプレットを終了することで問題はない
		while(playGame) {
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
			score = score + 1;
			if(playGame) {
				downBlocks();	
			}
			break;
		case KeyEvent.VK_SPACE:
			if(playGame) {
				changeMoveBlocks();
			}
			break;
		case KeyEvent.VK_ENTER:
			if(playGame == false) {
				playGame = true;
				score = 0;
				deleteLineNum = 0;
				fieldInit(field);
				GameTetris = new Thread(this);
				GameTetris.start();
				fieldInit(field);
			}
			break;
		}
		repaint();
	}
	/**
	 * 回転の作業関数
	 */
	public void changeMoveBlocks() {
		// 回転稼働範囲にブロックがあるか確認
		int[][] checkField;
		// 移動幅
		int diff=0;
		boolean doChange = true;
		int tmpRotationIndex  = rotationIndex + 1;
		// 回転インデックスの補正
		if (tmpRotationIndex > data[randomAns].length -1) {
			tmpRotationIndex = tmpRotationIndex - data[randomAns].length;
		}
		// 回転したあとのブロック取得
		Point[] tmpBlocks = data[randomAns][tmpRotationIndex];
		// 回転できるかチェック
		for (int num= 0; num<5; num ++) {
			switch (num) {
			case 0:
				diff = 0;
				break;
			case 1:
				diff = 1;
				break;
			case 2:
				diff = -1;
				break;
			case 3:
				diff = 2;
				break;
			case 4:
				diff = -2;
				break;
			}
			checkField = checkHoldBlock(diff);
			doChange = checkCrashBlocks(tmpBlocks, checkField);
			if (doChange) {
				break;
			}
		}
		
		if (doChange) {
			rotationIndex = tmpRotationIndex;
			clearNowPosition();
			movingBlocks = data[randomAns][rotationIndex];
			pointX = pointX + diff;
			updateNowPosition();
		}
	}
	/**
	 * 回転による壁・ブロックのあたり判定
	 * @param tmpBlocks 回転予定のブロック
	 * @param checkField 3*3または4*4の範囲のint[][]
	 * @return 移動可能かどうかのフラグ
	 */
	public boolean checkCrashBlocks(Point[] tmpBlocks, int[][] checkField) {
		for (int index=0; index<tmpBlocks.length; index++) {
			// テトリスブロックの座標の仕様上の補正で＋１
			int tmpX = tmpBlocks[index].x + 1;
			int tmpY = tmpBlocks[index].y + 1;
			if  (checkField[tmpX][tmpY] == HOLD_BLOCK ) {
				return false;
			}
		}
		return true;
	}
	/**
	 * 範囲内のフィールドに壁かどうかを確認する
	 * @param diff X軸の移動補正値
	 * @return 3*3もしくは4*4のint[][]
	 */
	public int[][] checkHoldBlock(int diff) {
		int minusNum;
		int absNum;
		// ブロックの形によって確認範囲を変える
		if (randomAns == 3) {
			minusNum = -1;
			absNum = 2;
		} else {
			minusNum = -1;
			absNum = 1;
		}
		// 確認範囲のフィールドを作る
		int[][] checkField = new int[absNum+2][absNum+2];
		for (int x =0; x<absNum+2;x++) {
			for (int y =0; y<absNum+2;y++) {
				checkField[x][y] = NO_BLOCK;
			}
		}
		for(int i= minusNum; i<=absNum; i++) {
			for(int j= minusNum; j<=absNum; j++) {
				int tmpX = pointX + diff + i;
				int tmpY = pointY + j;
				if(tmpX<0 || tmpX>=WIDTH_NUM || tmpY<0 || tmpY>=HEIGHT_NUM) {
					// 壁の先なのでブロック扱いにする
					checkField[i+1][j+1] = HOLD_BLOCK;
				}else {
					if ( (field[tmpX][tmpY] == WALL_BLOCK) ||
							(field[tmpX][tmpY] == HOLD_BLOCK) ){
						// テトリスブロックの座標の仕様上の補正で＋１
						checkField[i+1][j+1] = HOLD_BLOCK;
					}
				}
			}
		}
		return checkField;
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
	/**
	 * ゲーム画面内で削除できるか確認する関数
	 */
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
		int deleteNum = lineNumbers.size();
		if ( deleteNum > 0) {
			// 消す列数に応じて得点GET
			deleteLineNum = deleteLineNum + deleteNum;
			switch(deleteNum) {
			case 1:
				score = score + 40;
				break;
			case 2:
				score = score + 100;
				break;
			case 3:
				score = score + 300;
				break;
			case 4:
				score = score + 1200;
				break;
			}
			// 小さい値から削除していきたいのでソートする
			Collections.sort(lineNumbers);
			for(int y :lineNumbers) {
				deleteLines(y);
			}
		}
	}
	/**
	 * 一列削除する
	 * @param deleteLineNum 削除する行数
	 */
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
