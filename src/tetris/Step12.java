package tetris;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Color;
import java.awt.Font;
// �L�[�{�[�h�p
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.awt.event.KeyEvent;

public class Step12 extends Applet implements KeyListener, Runnable {
	// �A�v���b�g�̊O��
	final private int BLOCK_SIZE = 30;
	final private int HEIGHT_NUM = 21;
	final private int WIDTH_NUM = 12;
	final private int WIDTH = BLOCK_SIZE * WIDTH_NUM; //360
	final private int HEIGHT = BLOCK_SIZE * HEIGHT_NUM; //630
	
	// �A�v���b�g�̒��g 
	private int[][] field = new int[WIDTH_NUM][HEIGHT_NUM];
	private Color[][] fieldColor = new Color[WIDTH_NUM][HEIGHT_NUM];
	final int WALL_BLOCK = -1;
	final int NO_BLOCK = 0;
	final int MOVE_BLOCK = 1;
	final int HOLD_BLOCK = 2;
	private Thread GameTetris;
	private Image background; 
	private Graphics bufferGraphics;

	// �ړ��|�C���^�p
	// Point�^���g�����_�͂Ȃ��̂œ����ϐ��ŕێ�����
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
	
	// �e�g���X�u���b�N
	private boolean playGame = true;
	private int score = 0;
	private int deleteLineNum = 0;
	private int rotationIndex = 0;
	private Point data[][][] =  { 
					// �tL
					{ {  new Point(0, 0),new Point(0, -1), new Point(1, -1), new Point(0, 1) },
							{ new Point(0, 0), new Point(-1, 0), new Point(1, 0), new Point(1, 1) },
							{ new Point(0, 0), new Point(0, -1), new Point(0, 1), new Point(-1, 1) },
							{ new Point(0, 0), new Point(-1, 0), new Point(-1, -1), new Point(1, 0) } },
					// L
					{ { new Point(0, 0), new Point(0, -1), new Point(0, 1), new Point(1, 1) },
							{ new Point(0, 0), new Point(1, 0), new Point(-1, 0), new Point(-1, 1) },
							{ new Point(0, 0), new Point(0, -1), new Point(0, 1), new Point(-1, -1) },
							{ new Point(0, 0), new Point(1, 0), new Point(-1, 0), new Point(1, -1) } },
					// ��
					{ { new Point(0, 0), new Point(1, 0), new Point(0, 1), new Point(1, 1) },
							{ new Point(0, 0), new Point(1, 0), new Point(0, 1), new Point(1, 1) },
							{ new Point(0, 0), new Point(1, 0), new Point(0, 1), new Point(1, 1) },
							{ new Point(0, 0), new Point(1, 0), new Point(0, 1), new Point(1, 1) } },
					// I
					{ { new Point(0, 0), new Point(-1, 0), new Point(1, 0), new Point(2, 0) },
							{ new Point(0, 0), new Point(0, -1), new Point(0, 1), new Point(0, 2) },
							{ new Point(0, 0), new Point(-1, 0), new Point(1, 0), new Point(2, 0) },
							{ new Point(0, 0), new Point(0, -1), new Point(0, 1), new Point(0, 2) } },
					// �tZ
					{ { new Point(0, 0),new Point(0, -1),  new Point(-1, 0), new Point(1, -1) },
							{ new Point(0, 0), new Point(0, -1), new Point(1, 0), new Point(1, 1) },
							{ new Point(0, 0),new Point(0, -1),  new Point(-1, 0), new Point(1, -1) },
							{ new Point(0, 0), new Point(0, -1), new Point(1, 0), new Point(1, 1) } },
					// ��
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
	 * ����������
	 */
	public void init() {
		addKeyListener(this);
		// �X�^�[�g�n�_�i�A�v���b�g�J�n���j
		pointX = (WIDTH_NUM-1) / 2;
		pointY = 0;
		randomValue = random.nextInt(777);
		nextRandomAns = randomValue % 7;
		nextMovingBlocks = choiseBlocks(nextRandomAns);
	}
	/**
	 *  fied�̏������֐�
	 */
	public void fieldInit(int[][] initField) {
		for(int w = 0; w < WIDTH; w +=BLOCK_SIZE) {
			for(int h = 0; h < HEIGHT; h +=BLOCK_SIZE) {
				int x = w / BLOCK_SIZE;
				int y = h / BLOCK_SIZE;
				// �ǂ̕`��
				if ( (x==0)  || (x==WIDTH_NUM-1)  || (y==HEIGHT_NUM-1)) {
					initField[x][y] = WALL_BLOCK;
				} else {
					initField[x][y] = NO_BLOCK;
				}
			}
		}
	}
	/**
	 * �A�v���b�g�J�n���̏���������
	 */
	public void start() {
		if (GameTetris == null) {
			GameTetris = new Thread(this);
			GameTetris.start();
			fieldInit(field);
		}
		// �X�^�[�g�n�_�i���X�^�[�g�܂ށj
		rotationIndex = 0;
		randomAns = nextRandomAns;
		movingBlocks = nextMovingBlocks;
		blockColor = nextBlockColor;
		// ���̃u���b�N�X�V
		randomValue = random.nextInt(777);
		nextRandomAns = randomValue % 7;
		nextMovingBlocks = choiseBlocks(nextRandomAns);
		pointX = (WIDTH_NUM-1) / 2;
		pointY = 0;
		for (int index=0; index<movingBlocks.length; index++) {
			int tmpY = pointY+movingBlocks[index].y;
			int tmpX = pointX+movingBlocks[index].x;
			// �X�^�[�g�Ƃ��̃G���[�΍�
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
		// �����_���ŐF�����肷��
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
	 * �u���b�N�����܂ł����ꏊ���Y��ɂ���֐�
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
	 * �u���b�N���V�����ꏊ�Ɉړ�����Ƃ��̊֐�
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
	 * �`�揈��
	 */
	public void paint(Graphics g) {
		// �g�Œ� ���_�������p�Ɋg��
		resize(WIDTH+(BLOCK_SIZE*6), HEIGHT);
		// �A�v���b�g�Ƀt�H�[�J�X��ݒ肷��i�L�[�{�[�h�C�x���g���o�̂��߁j
		requestFocusInWindow();
		repeat_paint(playGame);
		g.drawImage(background, 0, 0, this);
	}

	/* �A�v���b�g�ɘg�ƃu���b�N��`�ʂ���
	 * @param g 	Graphics�����̂܂�
	 */
	private void repeat_paint(boolean playGame) {
		background = createImage(WIDTH+(BLOCK_SIZE*6), HEIGHT); 
		bufferGraphics = background.getGraphics();
		
		// �`��@���u���b�N�`��O�ɂ��Ƙg��������
		for(int x = 0; x < WIDTH_NUM; x++) {
			for(int y = 0; y < HEIGHT_NUM; y ++) {
				int w = x * BLOCK_SIZE;
				int h = y * BLOCK_SIZE;
				switch(field[x][y]) {
				// �ړ��u���b�N(MOVE_BLOCK)�ƌŒ�u���b�N(HOLD_BLOCK)�̏ꍇ
				// ���ɉ������Ȃ�
				// �����Ȃ��u���b�N�̕`��
				case NO_BLOCK:
					fieldColor[x][y] = Color.WHITE;
					break;
				// �ǃu���b�N�̕`��
				case WALL_BLOCK:
					fieldColor[x][y] = Color.GRAY;
					break;
				}
				paintBlock(bufferGraphics, w, h, fieldColor[x][y]);
				// �i�q�`��
				bufferGraphics.setColor(Color.BLACK);
				bufferGraphics.drawLine(w, h, w, h+BLOCK_SIZE);
				bufferGraphics.drawLine(w, h, w +BLOCK_SIZE, h);
				bufferGraphics.drawLine(WIDTH, 0, WIDTH , HEIGHT);
				bufferGraphics.drawLine(0, HEIGHT, WIDTH , HEIGHT);
				System.out.print(field[x][y]);
			}
			System.out.println("");
		}
		// �Q�[���g�O�`��
		// ���u���b�N��ԕ`��
		// ��
		bufferGraphics.setColor(Color.WHITE);
		bufferGraphics.fillRect(WIDTH + BLOCK_SIZE, BLOCK_SIZE, (BLOCK_SIZE*4),  BLOCK_SIZE*4);
		// ���u���b�N
		for(int index=0; index<nextMovingBlocks.length;index++) {
			int tmpX = (nextMovingBlocks[index].x+1)+WIDTH_NUM+1;
			int tmpY = (nextMovingBlocks[index].y+1)+1;
			if ( (nextRandomAns != 2) && (nextRandomAns != 3) ) {
				tmpY = tmpY + 1;
			}
			// �`�ʈʒu��␳���ău���b�N��`�ʂ���
			paintBlock(bufferGraphics, tmpX*BLOCK_SIZE, tmpY*BLOCK_SIZE, nextBlockColor);
		}
		// �g��
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
		
		//�X�R�A�`��
		bufferGraphics.setColor(Color.GREEN);
		Font font = new Font("SensSerif", Font.BOLD, 30);
		bufferGraphics.setFont(font);
		bufferGraphics.drawString("SCORE:", BLOCK_SIZE*WIDTH_NUM+10, BLOCK_SIZE*13);
		bufferGraphics.drawString(String.valueOf(score), BLOCK_SIZE*WIDTH_NUM+10, BLOCK_SIZE*15);
		font = new Font("SensSerif", Font.BOLD, 23);
		bufferGraphics.setFont(font);
		bufferGraphics.drawString("CLEAR LINES:", BLOCK_SIZE*WIDTH_NUM+10, BLOCK_SIZE*17);
		bufferGraphics.drawString(String.valueOf(deleteLineNum), BLOCK_SIZE*WIDTH_NUM+10, BLOCK_SIZE*18);
		// �Q�[���I�[�o�[���Ă��邩�ǂ���
		if(playGame== false) {
			bufferGraphics.setColor(Color.BLACK);
			font = new Font("SensSerif", Font.BOLD, 45);
			bufferGraphics.setFont(font);
			bufferGraphics.drawString("GAME OVER!!", BLOCK_SIZE*1, BLOCK_SIZE*5);
		}
	}
	/**
	 * �u���b�N�̒��F�֐�
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
	 * �X���b�h��1�b���Ƃɓ��������߂̕K�{�֐�
	 */
	@Override
	public void run() {
		// �������[�v�͎d�l�@�A�v���b�g���I�����邱�ƂŖ��͂Ȃ�
		while(playGame) {
			downBlocks();
			repaint();
			try {
				Thread.sleep(800); //��������1000ms
			} catch(InterruptedException e) {
				return;
			}
		}
	}
	/**
	 * �ĕ`��O����
	 */
	public void update(Graphics g) {
		paint(g);
	}
	/**
	 * �L�[�������ꂽ�ꍇ
	 * @param event
	 */
	public void keyPressed(KeyEvent event) {
		//�L�[�A�ł̓o�O�̌�
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
	 * ��]�̍�Ɗ֐�
	 */
	public void changeMoveBlocks() {
		// ��]�ғ��͈͂Ƀu���b�N�����邩�m�F
		int[][] checkField;
		// �ړ���
		int diff=0;
		boolean doChange = true;
		int tmpRotationIndex  = rotationIndex + 1;
		// ��]�C���f�b�N�X�̕␳
		if (tmpRotationIndex > data[randomAns].length -1) {
			tmpRotationIndex = tmpRotationIndex - data[randomAns].length;
		}
		// ��]�������Ƃ̃u���b�N�擾
		Point[] tmpBlocks = data[randomAns][tmpRotationIndex];
		// ��]�ł��邩�`�F�b�N
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
	 * ��]�ɂ��ǁE�u���b�N�̂����蔻��
	 * @param tmpBlocks ��]�\��̃u���b�N
	 * @param checkField 3*3�܂���4*4�͈̔͂�int[][]
	 * @return �ړ��\���ǂ����̃t���O
	 */
	public boolean checkCrashBlocks(Point[] tmpBlocks, int[][] checkField) {
		for (int index=0; index<tmpBlocks.length; index++) {
			// �e�g���X�u���b�N�̍��W�̎d�l��̕␳�Ł{�P
			int tmpX = tmpBlocks[index].x + 1;
			int tmpY = tmpBlocks[index].y + 1;
			if  (checkField[tmpX][tmpY] == HOLD_BLOCK ) {
				return false;
			}
		}
		return true;
	}
	/**
	 * �͈͓��̃t�B�[���h�ɕǂ��ǂ������m�F����
	 * @param diff X���̈ړ��␳�l
	 * @return 3*3��������4*4��int[][]
	 */
	public int[][] checkHoldBlock(int diff) {
		int minusNum;
		int absNum;
		// �u���b�N�̌`�ɂ���Ċm�F�͈͂�ς���
		if (randomAns == 3) {
			minusNum = -1;
			absNum = 2;
		} else {
			minusNum = -1;
			absNum = 1;
		}
		// �m�F�͈͂̃t�B�[���h�����
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
					// �ǂ̐�Ȃ̂Ńu���b�N�����ɂ���
					checkField[i+1][j+1] = HOLD_BLOCK;
				}else {
					if ( (field[tmpX][tmpY] == WALL_BLOCK) ||
							(field[tmpX][tmpY] == HOLD_BLOCK) ){
						// �e�g���X�u���b�N�̍��W�̎d�l��̕␳�Ł{�P
						checkField[i+1][j+1] = HOLD_BLOCK;
					}
				}
			}
		}
		return checkField;
	}
	/**
	 * �u���b�N�����E�ɓ����Ƃ��̊֐�
	 * @param isRight �E�������̃t���O
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
	 * �u���b�N�����ɍs���Ƃ��̊֐�
	 * �i���{�^���Ǝ��Ԃŗ�����Ƃ��j
	 */
	public void downBlocks() {
		boolean isHold = false;
		boolean isDown = false;
		// �ǂɂ߂荞�܂Ȃ���
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
			// ���C�����������m�F����i�폜�̊֐��̒��j
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
	 * �u���b�N�̌Œ�֐�
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
	 * �Q�[����ʓ��ō폜�ł��邩�m�F����֐�
	 */
	public void deleteLinesCheck() {
		ArrayList<Integer> lineNumbers = new ArrayList<>();
		boolean isDelete;
		// �ǂ̓����̘b�Ȃ̂ŏ����␳��������
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
			// �����񐔂ɉ����ē��_GET
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
			// �������l����폜���Ă��������̂Ń\�[�g����
			Collections.sort(lineNumbers);
			for(int y :lineNumbers) {
				deleteLines(y);
			}
		}
	}
	/**
	 * ���폜����
	 * @param deleteLineNum �폜����s��
	 */
	public void deleteLines(int deleteLineNum) {
		int[][] tmpField = new int[WIDTH_NUM][HEIGHT_NUM];
		fieldInit(tmpField);
		// ������1���傫���͍폜����1�s����␳�����l
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
	 * �L�[�������ꂽ�ꍇ(���g�p)
	 * @param event
	 */
	public void keyReleased(KeyEvent event) {
	}
	/**
	 * �L�[�������ꂽ�ꍇ�i��ɕ����L�[�j(���g�p)
	 */
	public void keyTyped(KeyEvent event) {
	}}
