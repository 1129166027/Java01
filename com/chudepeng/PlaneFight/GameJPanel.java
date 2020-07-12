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
 * 游戏画布，包含游戏核心方法
 *
 * @author 储德鹏
 */

public class GameJPanel extends JPanel {
	private int score = 0; // 得分
	private Timer timer; // 定时器
	private int intervel = 1000 / 100; // 时间间隔(毫秒)

	private FlyingObject[] flyings = {}; // 敌机数组
	private Bullet[] bullets = {}; // 子弹数组
	private Hero hero = new Hero(); // 英雄机

	/** 游戏的当前状态: START RUNNING PAUSE GAME_OVER */
	private int state = 0;
	private static final int START = 0;
	private static final int RUNNING = 1;
	private static final int PAUSE = 2;
	private static final int GAME_OVER = 3;

	public static BufferedImage pause;
	public static BufferedImage gameover;
	public static BufferedImage start;
	public static BufferedImage background;
	static { // 静态代码块，初始化图片资源
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
		paintHero(g); // 画英雄机
		paintBullets(g); // 画子弹
		paintFlyingObjects(g); // 画飞行物
		paintScore(g); // 画分数
		paintState(g); // 画游戏状态

	}

	/** 画英雄机 */
	public void paintHero(Graphics g) {
		g.drawImage(hero.image, hero.getX(), hero.getY(), null);
	}

	/** 画子弹 */
	public void paintBullets(Graphics g) {
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			g.drawImage(b.image, b.getX() - b.getWidth() / 2, b.getY(), null);
		}
	}

	/** 画飞行物 */
	public void paintFlyingObjects(Graphics g) {
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			g.drawImage(f.getImage(), f.getX(), f.getY(), null);
		}
	}

	/** 画分数 */
	public void paintScore(Graphics g) {
		Font font = new Font(Font.SANS_SERIF, Font.BOLD, 14); // 字体
		g.setColor(new Color(0x3A3B3B));
		g.setFont(font); // 设置字体
		g.drawString("分数:" + score, 10, 25); // 画分数
		g.drawString("生命:" + hero.getLife(), 10, 45); // 画命
	}

	/** 画游戏状态 */
	public void paintState(Graphics g) {
		switch (state) {
		case START: // 启动状态
			g.drawImage(start, 30, -50, null);
			break;
		case PAUSE: // 暂停状态
			g.drawImage(pause, 0, -50, null);
			break;
		case GAME_OVER: // 游戏终止状态
			g.drawImage(gameover, 0, -50, null);
			break;
		}
	}

	/** 启动执行代码 */
	public void action() {
		// 鼠标监听事件
		System.out.println(GameJPanel.WIDTH);
		MouseAdapter l = new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) { // 鼠标移动
				if (state == RUNNING) { // 运行状态下移动英雄机--随鼠标位置
					int x = e.getX();
					int y = e.getY();
					hero.moveTo(x, y);
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) { // 鼠标进入
				if (state == PAUSE) { // 暂停状态下运行
					state = RUNNING;
				}
			}

			@Override
			public void mouseExited(MouseEvent e) { // 鼠标退出
				if (state != GAME_OVER && state != START) { // 游戏未结束，则设置其为暂停
					state = PAUSE;
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) { // 鼠标点击
				switch (state) {
				case START:
					state = RUNNING; // 启动状态下运行
					break;
				case GAME_OVER: // 游戏结束，清理现场
					flyings = new FlyingObject[0]; // 清空飞行物
					bullets = new Bullet[0]; // 清空子弹
					hero = new Hero(); // 重新创建英雄机
					score = 0; // 清空成绩
					state = START; // 状态设置为启动
					break;
				}
			}
		};
		this.addMouseListener(l); // 处理鼠标点击操作
		this.addMouseMotionListener(l); // 处理鼠标滑动操作

		timer = new Timer(); // 主流程控制
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (state == RUNNING) { // 运行状态
					enterAction(); // 飞行物入场
					stepAction(); // 走一步
					shootAction(); // 英雄机射击
					bangAction(); // 子弹打飞行物
					outOfBoundsAction(); // 删除越界飞行物及子弹
					checkGameOverAction(); // 检查游戏结束
				}
				repaint(); // 重绘，调用paint()方法
			}

		}, intervel, intervel);
	}

	int flyEnteredIndex = 0; // 飞行物入场计数

	/** 飞行物入场 */
	public void enterAction() {
		flyEnteredIndex++;
		if (flyEnteredIndex % 40 == 0) { // 400毫秒生成一个飞行物--10*40
			FlyingObject obj = nextOne(); // 随机生成一个飞行物
			flyings = Arrays.copyOf(flyings, flyings.length + 1);
			flyings[flyings.length - 1] = obj;
		}
	}

	/**
	 * 随机生成飞行物
	 * 
	 * @return 飞行物对象
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

	/** 走一步 */
	public void stepAction() {
		for (int i = 0; i < flyings.length; i++) { // 飞行物走一步
			FlyingObject f = flyings[i];
			f.move();
		}

		for (int i = 0; i < bullets.length; i++) { // 子弹走一步
			Bullet b = bullets[i];
			b.move();
		}
		hero.move(); // 英雄机走一步
	}

	int shootIndex = 0; // 射击计数

	/** 射击 */
	public void shootAction() {
		shootIndex++;
		if (shootIndex % 30 == 0) { // 300毫秒发一颗
			Bullet[] bs = hero.shoot(); // 英雄打出子弹
			bullets = Arrays.copyOf(bullets, bullets.length + bs.length); // 扩容
			System.arraycopy(bs, 0, bullets, bullets.length - bs.length, bs.length); // 追加数组
		}
	}

	/** 子弹与飞行物碰撞检测 */
	public void bangAction() {
		for (int i = 0; i < bullets.length; i++) { // 遍历所有子弹
			Bullet b = bullets[i];
			if (bang(b)) // 子弹和飞行物之间的碰撞检查
			{
				Bullet temp = b;
				bullets[i] = bullets[bullets.length - 1];// 命中的子弹和最后一个子弹替换
				bullets = Arrays.copyOf(bullets, bullets.length - 1); // 删除最后一个子弹
			}
		}
	}

	/** 子弹和飞行物之间的碰撞检查 */
	public boolean bang(Bullet bullet) {
		int index = -1; // 击中的飞行物索引
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject obj = flyings[i];
			if (obj.shootBy(bullet) && obj.getClass() != Bee.class) { // 判断是否击中
				index = i; // 记录被击中的飞行物的索引
				flyings[i].goDead();
				break;
			}
		}
		if (index != -1) { // 有击中的飞行物
			FlyingObject one = flyings[index]; // 记录被击中的飞行物
			Enemy e = (Enemy) one; // 强制类型转换
			score += e.getScore(); // 加分
			Thread thread = new Thread(new Runnable() {//创建一个线程用于播放敌机爆炸的声音
				File f;
				URI uri;
				URL url;
				@Override
				public void run() {
					try {
						f = new File("src/music/爆炸.wav");
						uri = f.toURI();
						url = uri.toURL();
						AudioClip aau;
						aau = Applet.newAudioClip(url);
						aau.play();// 播放
						Thread.sleep(1000);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // 解析地址

				}
			});
			thread.start();// 开启线程
			return true;
		}
		return false;
	}

	/** 删除越界与碰撞后的飞行物及越界的子弹 */
	public void outOfBoundsAction() {
		int index = 0; // 索引
		FlyingObject[] flyingLives = new FlyingObject[flyings.length]; // 活着的飞行物
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			if (!f.outOfBounds() && f.state != -1) {
				flyingLives[index++] = f; // 不越界且没有发生碰撞的留着
			}
		}
		flyings = Arrays.copyOf(flyingLives, index); // 将不越界的飞行物都留着

		index = 0; // 索引重置为0
		Bullet[] bulletLives = new Bullet[bullets.length];
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			if (!b.outOfBounds()) {
				bulletLives[index++] = b;
			}
		}
		bullets = Arrays.copyOf(bulletLives, index); // 将不越界的子弹留着
	}

	/** 检查游戏结束 */
	public void checkGameOverAction() {
		if (isGameOver()) {
			state = GAME_OVER; // 改变状态
		}
	}

	/** 检查游戏是否结束 */
	public boolean isGameOver() {

		for (int i = 0; i < flyings.length; i++) {
			int index = -1;
			FlyingObject obj = flyings[i];
			if (hero.hit(obj)) { // 检查英雄机与飞行物是否碰撞
				if (obj.getClass() == Bee.class) {

					Award a = (Award) obj;
					int type = a.getType(); // 获取奖励类型
					switch (type) {
					case Award.DOUBLE_FIRE:
						hero.setDoubleFire(1); // 设置双倍火力
						break;
					case Award.LIFE:
						hero.addLife(); // 设置加命
						break;
					}
				} else {

					hero.subtractLife(); // 减命
					hero.setDoubleFire(0); // 双倍火力解除

				}
				index = i; // 记录碰上的飞行物索引

			}
			if (index != -1) {
				FlyingObject t = flyings[index];
				flyings[index] = flyings[flyings.length - 1];
				flyings[flyings.length - 1] = t; // 碰上的与最后一个飞行物交换

				flyings = Arrays.copyOf(flyings, flyings.length - 1); // 删除碰上的飞行物
			}
		}

		return hero.getLife() <= 0;
	}

}
