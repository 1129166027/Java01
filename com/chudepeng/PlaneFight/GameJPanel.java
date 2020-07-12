package com.chudepeng.PlaneFight;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


/**
 * ��Ϸ������������Ϸ���ķ���
 *
 * @author ������
 */

public class GameJPanel extends JPanel {
	private int score = 0; // �÷�
	private Timer timer; // ��ʱ��
	private int intervel = 1000 / 100; // ʱ����(����)

	private FlyingObject[] flyings = {}; // �л�����
	private Bullet[] bullets = {}; // �ӵ�����
	private Hero hero = new Hero(); // Ӣ�ۻ�

	/** ��Ϸ�ĵ�ǰ״̬: START RUNNING PAUSE GAME_OVER */
	private int state = 0;
	private static final int START = 0;
	private static final int RUNNING = 1;
	private static final int PAUSE = 2;
	private static final int GAME_OVER = 3;

	public static BufferedImage pause;
	public static BufferedImage gameover;
	public static BufferedImage start;
	public static BufferedImage background;
	static { // ��̬����飬��ʼ��ͼƬ��Դ
		try {

			start = ImageIO.read(new FileInputStream("src/images/title.png"));
			background=ImageIO.read(new FileInputStream("src/images/background.png"));
			pause = ImageIO.read(new FileInputStream("src/images/pause.png"));
			gameover = ImageIO.read(new FileInputStream("src/images/gameover.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(background, 0, 0, null);
		paintHero(g); // ��Ӣ�ۻ�
		paintBullets(g); // ���ӵ�
		paintFlyingObjects(g); // ��������
		paintScore(g); // ������
		paintState(g); // ����Ϸ״̬

	}

	/** ��Ӣ�ۻ� */
	public void paintHero(Graphics g) {
		g.drawImage(hero.image, hero.getX(), hero.getY(), null);
	}

	/** ���ӵ� */
	public void paintBullets(Graphics g) {
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			g.drawImage(b.image, b.getX() - b.getWidth() / 2, b.getY(), null);
		}
	}

	/** �������� */
	public void paintFlyingObjects(Graphics g) {
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			g.drawImage(f.getImage(), f.getX(), f.getY(), null);
		}
	}

	/** ������ */
	public void paintScore(Graphics g) {
		Font font = new Font(Font.SANS_SERIF, Font.BOLD, 14); // ����
		g.setColor(new Color(0x3A3B3B));
		g.setFont(font); // ��������
		g.drawString("����:" + score, 10, 25); // ������
		g.drawString("����:" + hero.getLife(), 10, 45); // ����
	}

	/** ����Ϸ״̬ */
	public void paintState(Graphics g) {
		switch (state) {
		case START: // ����״̬
			g.drawImage(start, 30, -50, null);
			break;
		case PAUSE: // ��ͣ״̬
			g.drawImage(pause, 0, -50, null);
			break;
		case GAME_OVER: // ��Ϸ��ֹ״̬
			g.drawImage(gameover, 0, -50, null);
			break;
		}
	}

	/** ����ִ�д��� */
	public void action() {
		// �������¼�
		System.out.println(GameJPanel.WIDTH);
		MouseAdapter l = new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) { // ����ƶ�
				if (state == RUNNING) { // ����״̬���ƶ�Ӣ�ۻ�--�����λ��
					int x = e.getX();
					int y = e.getY();
					hero.moveTo(x, y);
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) { // ������
				if (state == PAUSE) { // ��ͣ״̬������
					state = RUNNING;
				}
			}

			@Override
			public void mouseExited(MouseEvent e) { // ����˳�
				if (state != GAME_OVER && state != START) { // ��Ϸδ��������������Ϊ��ͣ
					state = PAUSE;
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) { // �����
				switch (state) {
				case START:
					state = RUNNING; // ����״̬������
					break;
				case GAME_OVER: // ��Ϸ�����������ֳ�
					flyings = new FlyingObject[0]; // ��շ�����
					bullets = new Bullet[0]; // ����ӵ�
					hero = new Hero(); // ���´���Ӣ�ۻ�
					score = 0; // ��ճɼ�
					state = START; // ״̬����Ϊ����
					break;
				}
			}
		};
		this.addMouseListener(l); // �������������
		this.addMouseMotionListener(l); // ������껬������

		timer = new Timer(); // �����̿���
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (state == RUNNING) { // ����״̬
					enterAction(); // �������볡
					stepAction(); // ��һ��
					shootAction(); // Ӣ�ۻ����
					bangAction(); // �ӵ��������
					outOfBoundsAction(); // ɾ��Խ������Ｐ�ӵ�
					checkGameOverAction(); // �����Ϸ����
				}
				repaint(); // �ػ棬����paint()����
			}

		}, intervel, intervel);
	}

	int flyEnteredIndex = 0; // �������볡����

	/** �������볡 */
	public void enterAction() {
		flyEnteredIndex++;
		if (flyEnteredIndex % 40 == 0) { // 400��������һ��������--10*40
			FlyingObject obj = nextOne(); // �������һ��������
			flyings = Arrays.copyOf(flyings, flyings.length + 1);
			flyings[flyings.length - 1] = obj;
		}
	}

	/**
	 * ������ɷ�����
	 * 
	 * @return ���������
	 */
	public static FlyingObject nextOne() {
		Random random = new Random();
		int type = random.nextInt(20); // [0,20)
		if (type == 0) {
			return new Bee();
		} else {
			return new Airplane();
		}
	}

	/** ��һ�� */
	public void stepAction() {
		for (int i = 0; i < flyings.length; i++) { // ��������һ��
			FlyingObject f = flyings[i];
			f.move();
		}

		for (int i = 0; i < bullets.length; i++) { // �ӵ���һ��
			Bullet b = bullets[i];
			b.move();
		}
		hero.move(); // Ӣ�ۻ���һ��
	}

	int shootIndex = 0; // �������

	/** ��� */
	public void shootAction() {
		shootIndex++;
		if (shootIndex % 30 == 0) { // 300���뷢һ��
			Bullet[] bs = hero.shoot(); // Ӣ�۴���ӵ�
			bullets = Arrays.copyOf(bullets, bullets.length + bs.length); // ����
			System.arraycopy(bs, 0, bullets, bullets.length - bs.length, bs.length); // ׷������
		}
	}

	/** �ӵ����������ײ��� */
	public void bangAction() {
		for (int i = 0; i < bullets.length; i++) { // ���������ӵ�
			Bullet b = bullets[i];
			if (bang(b)) // �ӵ��ͷ�����֮�����ײ���
			{
				Bullet temp = b;
				bullets[i] = bullets[bullets.length - 1];// ���е��ӵ������һ���ӵ��滻
				bullets = Arrays.copyOf(bullets, bullets.length - 1); // ɾ�����һ���ӵ�
			}
		}
	}

	/** �ӵ��ͷ�����֮�����ײ��� */
	public boolean bang(Bullet bullet) {
		int index = -1; // ���еķ���������
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject obj = flyings[i];
			if (obj.shootBy(bullet) && obj.getClass() != Bee.class) { // �ж��Ƿ����
				index = i; // ��¼�����еķ����������
				flyings[i].goDead();
				break;
			}
		}
		if (index != -1) { // �л��еķ�����
			FlyingObject one = flyings[index]; // ��¼�����еķ�����
			Enemy e = (Enemy) one; // ǿ������ת��
			score += e.getScore(); // �ӷ�
			Thread thread = new Thread(new Runnable() {//����һ���߳����ڲ��ŵл���ը������
				File f;
				URI uri;
				URL url;
				@Override
				public void run() {
					try {
						f = new File("src/music/��ը.wav");
						uri = f.toURI();
						url = uri.toURL();
						AudioClip aau;
						aau = Applet.newAudioClip(url);
						aau.play();// ����
						Thread.sleep(1000);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // ������ַ

				}
			});
			thread.start();// �����߳�
			return true;
		}
		return false;
	}

	/** ɾ��Խ������ײ��ķ����ＰԽ����ӵ� */
	public void outOfBoundsAction() {
		int index = 0; // ����
		FlyingObject[] flyingLives = new FlyingObject[flyings.length]; // ���ŵķ�����
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			if (!f.outOfBounds() && f.state != -1) {
				flyingLives[index++] = f; // ��Խ����û�з�����ײ������
			}
		}
		flyings = Arrays.copyOf(flyingLives, index); // ����Խ��ķ����ﶼ����

		index = 0; // ��������Ϊ0
		Bullet[] bulletLives = new Bullet[bullets.length];
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			if (!b.outOfBounds()) {
				bulletLives[index++] = b;
			}
		}
		bullets = Arrays.copyOf(bulletLives, index); // ����Խ����ӵ�����
	}

	/** �����Ϸ���� */
	public void checkGameOverAction() {
		if (isGameOver()) {
			state = GAME_OVER; // �ı�״̬
		}
	}

	/** �����Ϸ�Ƿ���� */
	public boolean isGameOver() {

		for (int i = 0; i < flyings.length; i++) {
			int index = -1;
			FlyingObject obj = flyings[i];
			if (hero.hit(obj)) { // ���Ӣ�ۻ���������Ƿ���ײ
				if (obj.getClass() == Bee.class) {

					Award a = (Award) obj;
					int type = a.getType(); // ��ȡ��������
					switch (type) {
					case Award.DOUBLE_FIRE:
						hero.setDoubleFire(1); // ����˫������
						break;
					case Award.LIFE:
						hero.addLife(); // ���ü���
						break;
					}
				} else {

					hero.subtractLife(); // ����
					hero.setDoubleFire(0); // ˫���������

				}
				index = i; // ��¼���ϵķ���������

			}
			if (index != -1) {
				FlyingObject t = flyings[index];
				flyings[index] = flyings[flyings.length - 1];
				flyings[flyings.length - 1] = t; // ���ϵ������һ�������ｻ��

				flyings = Arrays.copyOf(flyings, flyings.length - 1); // ɾ�����ϵķ�����
			}
		}

		return hero.getLife() <= 0;
	}

}
