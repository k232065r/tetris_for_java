package tetris;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Color;

// �L�[�{�[�h�p
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.awt.event.KeyEvent;

public class Step11 extends Applet implements KeyListener, Runnable {
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
	private Color blockColor;
	private Point[] movingBlocks;
	
	// �e�g���X�u���b�N
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
	 * ����������
	 */
	public void init() {
		addKeyListener(this);
		// �X�^�[�g�n�_�i�A�v���b�g�J�n���j
		pointX = (WIDTH_NUM-1) / 2;
		pointY = 0;
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
		// �����_���ŐF�����肷��
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
		
		// �X�^�[�g�n�_�i���X�^�[�g�܂ށj
		movingBlocks = data[randomAns][rotationIndex];
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
		updateNowPosition();
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
		System.out.println("paint");
		// �g�Œ�
		resize(WIDTH, HEIGHT);
		// �A�v���b�g�Ƀt�H�[�J�X��ݒ肷��i�L�[�{�[�h�C�x���g���o�̂��߁j
		requestFocusInWindow();
		repeat_paint();
		g.drawImage(background, 0, 0, this);
	}

	/* �A�v���b�g�ɘg�ƃu���b�N��`�ʂ���
	 * @param g 	Graphics�����̂܂�
	 */
	private void repeat_paint() {
		background = createImage(WIDTH, HEIGHT); 
		bufferGraphics = background.getGraphics();
		
		// �`��@���u���b�N�`��O�ɂ��Ƙg��������
		for(int w = 0; w < WIDTH; w +=BLOCK_SIZE) {
			for(int h = 0; h < HEIGHT; h +=BLOCK_SIZE) {
				int x = w / BLOCK_SIZE;
				int y = h / BLOCK_SIZE;
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
				System.out.print(field[x][y]);
			}
			System.out.println("");
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
		g.fillRect(w, h, w+BLOCK_SIZE,  h+BLOCK_SIZE);
	}
	/**
	 * �X���b�h��1�b���Ƃɓ��������߂̕K�{�֐�
	 */
	@Override
	public void run() {
		// �������[�v�͎d�l�@�A�v���b�g���I�����邱�ƂŖ��͂Ȃ�
		while(true) {
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
	 * �I������
	 */
	public void stop() {
		System.out.println("stop");
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
		if ( lineNumbers.size() > 0) {
			// �������l����폜���Ă��������̂Ń\�[�g����
			Collections.sort(lineNumbers);
			for(int y :lineNumbers) {
				deleteLines(y);
			}
		}
	}
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
