package tetris;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Color;

// �L�[�{�[�h�p
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
	 * ����������
	 */
	public void init() {
		addKeyListener(this);
	}
	/**
	 * �A�v���b�g�J�n���̏���������
	 */
	public void start() {
		// �A�v���b�g�Ƀt�H�[�J�X��ݒ肷��i�L�[�{�[�h�C�x���g���o�̂��߁j
		pointX = (WIDTH_NUM-1) / 2;
		pointY = 0;
		// �X�^�[�g�n�_
		field[pointX][pointY] = true;
	}
	/**
	 * �`�揈��
	 */
	public void paint(Graphics g) {
		// �g�Œ�
		resize(WIDTH, HEIGHT);
		requestFocusInWindow();
		System.out.println("paint");
		painting(g);
	}
	/**
	 * �A�v���b�g�ɘg�ƃu���b�N��`�ʂ���
	 * @param g 	Graphics�����̂܂�
	 */
	private void painting(Graphics g) {
		// �`��@���u���b�N�`��O�ɂ��Ƙg��������
		for(int w = 0; w < WIDTH; w +=BLOCK_SIZE) {
			for(int h = 0; h < HEIGHT; h +=BLOCK_SIZE) {
				int x = w / BLOCK_SIZE;
				int y = h / BLOCK_SIZE;
				// �ǂ̕`��
				if ( (x==0)  || (x==WIDTH_NUM-1)  || (y==HEIGHT_NUM-1)) {
					g.setColor(Color.PINK);
					g.fillRect(w, h, w+BLOCK_SIZE,  h+BLOCK_SIZE);
				} else {
					// �ړ��u���b�N�̕`��
					if (field[x][y] == true) {
						g.setColor(Color.RED);
						g.fillRect(w, h, w+BLOCK_SIZE,  h+BLOCK_SIZE);
					// �����Ȃ��u���b�N�̕`��
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
		System.out.print("pointX:");
		System.out.println(pointX);
		System.out.print("pointY:");
		System.out.println(pointY);
		switch (keycode) {
			case KeyEvent.VK_RIGHT:
				System.out.println("right");
				// �ǂɂ߂荞�܂Ȃ���
				if ( (pointX+1) < (WIDTH_NUM - 1) ) {
					field[pointX][pointY] = false;
					pointX = pointX + 1;
				}
				break;
			case KeyEvent.VK_LEFT:
				System.out.println("left");
				// �ǂɂ߂荞�܂Ȃ���
				if ( (pointX-1) > 0) {
					field[pointX][pointY] = false;
					pointX = pointX - 1;
				}
				break;
			case KeyEvent.VK_DOWN:
				System.out.println("press down");
				// �ǂɂ߂荞�܂Ȃ���
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
