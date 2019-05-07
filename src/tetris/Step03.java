package tetris;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Color;

// �}�E�X�p
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
	 * ����������
	 */
	public void init() {
		addMouseListener(this);
	}
	/**
	 * �A�v���b�g�J�n���̏���������
	 */
	public void start() {
		System.out.println("start");
	}
	/**
	 * �`�揈��
	 */
	public void paint(Graphics g) {
		// �g�Œ�
		resize(WIDTH, HEIGHT);
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
	 * �ĕ`��O����
	 */
	public void update() {
		System.out.println("update");
	}
	/**
	 * �ĕ`�揈��
	 */
	public void repaint(Graphics g) {
		painting(g);
	}
	/**
	 * �I������
	 */
	public void stop() {
		System.out.println("stop");
	}

	// �}�E�X�C�x���g�p
	/**
	 * �}�E�X���N���b�N���ꂽ
	 * @param event
	 */
	public void mouseClicked(MouseEvent event) {
		// �u���b�N���C���������W���擾����
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
	// �}�E�X�C�x���g�p�@�ȍ~���g�p
	/**
	 * �}�E�X���A�v���b�g�ɏ����(���g�p)
	 * @param event
	 */
	public void mouseEntered(MouseEvent e) {
		
	}
	/**
	 * �}�E�X���A�v���b�g�̊O�ɏo��(���g�p)
	 * @param event
	 */
	public void mouseExited(MouseEvent event) {
		
	}
	/**
	 * �}�E�X�������ꂽ(���g�p)
	 * @param event
	 */
	public void mousePressed(MouseEvent event) {
			
	}
	/**
	 * �}�E�X�������ꂽ��Ԃ��痣�ꂽ(���g�p)
	 * @param event
	 */
	public void mouseReleased(MouseEvent event) {
		
	}
}
