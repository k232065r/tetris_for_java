package tetris;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Color;

// �L�[�{�[�h�p
import java.awt.event.KeyListener;
import java.util.Random;
import java.awt.event.KeyEvent;

public class Step07 extends Applet implements KeyListener, Runnable {
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

	// �ړ��|�C���^�p
	private int pointX;
	private int pointY;
	private Random random = new Random();
	private int randomValue;
	private Color blockColor;
	
	/**
	 * ����������
	 */
	public void init() {
		addKeyListener(this);
		// fied�̏�����
		for(int w = 0; w < WIDTH; w +=BLOCK_SIZE) {
			for(int h = 0; h < HEIGHT; h +=BLOCK_SIZE) {
				int x = w / BLOCK_SIZE;
				int y = h / BLOCK_SIZE;
				// �ǂ̕`��
				if ( (x==0)  || (x==WIDTH_NUM-1)  || (y==HEIGHT_NUM-1)) {
					field[x][y] = WALL_BLOCK;
					fieldColor[x][y] = Color.GRAY;
				} else {
					field[x][y] = NO_BLOCK;
					fieldColor[x][y] = Color.WHITE;
				}
			}
		}
		
		// �X�^�[�g�n�_�i�A�v���b�g�J�n���j
		pointX = (WIDTH_NUM-1) / 2;
		pointY = 0;
		GameTetris = new Thread(this);
		GameTetris.start();
	}
	/**
	 * �A�v���b�g�J�n���̏���������
	 */
	public void start() {
		// �����_���ŐF�����肷��
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
		
		// �X�^�[�g�n�_�i���X�^�[�g�܂ށj
		pointX = (WIDTH_NUM-1) / 2;
		pointY = 0;
		field[pointX][pointY] = MOVE_BLOCK;
		fieldColor[pointX][pointY] = blockColor;
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
		repeat_paint(g);
	}

	/* �A�v���b�g�ɘg�ƃu���b�N��`�ʂ���
	 * @param g 	Graphics�����̂܂�
	 */
	private void repeat_paint(Graphics g) {
		// �`��@���u���b�N�`��O�ɂ��Ƙg��������
		for(int w = 0; w < WIDTH; w +=BLOCK_SIZE) {
			for(int h = 0; h < HEIGHT; h +=BLOCK_SIZE) {
				int x = w / BLOCK_SIZE;
				int y = h / BLOCK_SIZE;
				switch(field[x][y] ) {
				// �ړ��u���b�N�̕`��
				case MOVE_BLOCK:
					paintBlock(g, w, h, fieldColor[x][y]);
					break;
				// �Œ�u���b�N�̕`��
				case HOLD_BLOCK:
					paintBlock(g, w, h, fieldColor[x][y]);
					break;
				// �����Ȃ��u���b�N�̕`��
				case NO_BLOCK:
					paintBlock(g, w, h, Color.WHITE);
					break;
				// �ǃu���b�N�̕`��
				case WALL_BLOCK:
					paintBlock(g, w, h, Color.GRAY);
					break;
				}
				if(y== 1) {
					System.out.print(x);
					System.out.println(fieldColor[x][y]);
				}
				// �i�q�`��
				g.setColor(Color.BLACK);
				g.drawLine(w, h, w, h+BLOCK_SIZE);
				g.drawLine(w, h, w +BLOCK_SIZE, h);
			}
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
		int keycode = event.getKeyCode();
		switch (keycode) {
		case KeyEvent.VK_RIGHT:
			System.out.println("press right");
			// �ǂɂ߂荞�܂Ȃ���
			if ( (pointX+1) < (WIDTH_NUM - 1)  &&
					(field[pointX+1][pointY] != HOLD_BLOCK) ) {
				clearNowPosition();
				pointX = pointX + 1;
				updateNowPosition();
			}
			break;
		case KeyEvent.VK_LEFT:
			System.out.println("press left");
			// �ǂɂ߂荞�܂Ȃ���
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
		// �ǂɂ߂荞�܂Ȃ���
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
