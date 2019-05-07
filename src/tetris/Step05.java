package tetris;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Color;

// �L�[�{�[�h�p
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class Step05 extends Applet implements KeyListener{
	// �A�v���b�g�̊O��
	final private int BLOCK_SIZE = 30;
	final private int HEIGHT_NUM = 21;
	final private int WIDTH_NUM = 12;
	final private int WIDTH = BLOCK_SIZE * WIDTH_NUM; //360
	final private int HEIGHT = BLOCK_SIZE * HEIGHT_NUM; //630
	
	// �A�v���b�g�̒��g 
	private int[][] field = new int[WIDTH_NUM][HEIGHT_NUM];
	final int WALL_BLOCK = -1;
	final int NO_BLOCK = 0;
	final int MOVE_BLOCK = 1;
	final int HOLD_BLOCK = 2;
	
	// �ړ��|�C���^�p
	private int pointX;
	private int pointY;
	
	/**
	 * ����������
	 */
	public void init() {
		addKeyListener(this);
		init_field();
	}
	/**
	 * �A�v���b�g�J�n���̏���������
	 */
	public void start() {
		// �A�v���b�g�Ƀt�H�[�J�X��ݒ肷��i�L�[�{�[�h�C�x���g���o�̂��߁j
		pointX = (WIDTH_NUM-1) / 2;
		pointY = 0;
		// �X�^�[�g�n�_
		field[pointX][pointY] = MOVE_BLOCK;
	}
	/**
	 * �`�揈��
	 */
	public void paint(Graphics g) {
		// �g�Œ�
		resize(WIDTH, HEIGHT);
		requestFocusInWindow();
		System.out.println("paint");
		repeat_paint(g);
	}
	/**
	 * fied�̏�����
	 */
	private void init_field() {
		for(int w = 0; w < WIDTH; w +=BLOCK_SIZE) {
			for(int h = 0; h < HEIGHT; h +=BLOCK_SIZE) {
				int x = w / BLOCK_SIZE;
				int y = h / BLOCK_SIZE;
				// �ǂ̕`��
				if ( (x==0)  || (x==WIDTH_NUM-1)  || (y==HEIGHT_NUM-1)) {
					field[x][y] = WALL_BLOCK;
				} else {
					field[x][y] = NO_BLOCK;
				}
			}
		}
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
					g.setColor(Color.RED);
					g.fillRect(w, h, w+BLOCK_SIZE,  h+BLOCK_SIZE);
					break;
				// �Œ�u���b�N�̕`��
				case HOLD_BLOCK:
					g.setColor(Color.PINK);
					g.fillRect(w, h, w+BLOCK_SIZE,  h+BLOCK_SIZE);
					break;
				// �����Ȃ��u���b�N�̕`��
				case NO_BLOCK:
					g.setColor(Color.WHITE);
					g.fillRect(w, h, w+BLOCK_SIZE,  h+BLOCK_SIZE);
					break;
				// �ǃu���b�N�̕`��
				case WALL_BLOCK:
					g.setColor(Color.PINK);
					g.fillRect(w, h, w+BLOCK_SIZE,  h+BLOCK_SIZE);
					break;
				}
				// �i�q�`��
				g.setColor(Color.BLACK);
				g.drawLine(w, h, w, h+BLOCK_SIZE);
				g.drawLine(w, h, w +BLOCK_SIZE, h);
			}
		}
	}
	/**
	 * �ĕ`��O����
	 */
	public void update() {
		System.out.println("update");
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
			// �ǂɂ߂荞�܂Ȃ���
			if ( (pointX+1) < (WIDTH_NUM - 1) ) {
				field[pointX][pointY] = NO_BLOCK;
				pointX = pointX + 1;
			}
			break;
		case KeyEvent.VK_LEFT:
			// �ǂɂ߂荞�܂Ȃ���
			if ( (pointX-1) > 0) {
				field[pointX][pointY] = NO_BLOCK;
				pointX = pointX - 1;
			}
			break;
		case KeyEvent.VK_DOWN:
			// �ǂɂ߂荞�܂Ȃ���
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
	 * �L�[�������ꂽ�ꍇ
	 * @param event
	 */
	public void keyReleased(KeyEvent event) {
		System.out.println("release");
	}
	/**
	 * �L�[�������ꂽ�ꍇ�i��ɕ����L�[�j
	 */
	public void keyTyped(KeyEvent event) {
		System.out.println("tyoe");
	}}
