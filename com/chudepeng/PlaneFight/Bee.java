package com.chudepeng.PlaneFight;

import java.awt.image.BufferedImage;
import java.util.Random;

public class Bee extends FlyingObject implements Award{
	private int xSpeed = 1;   //x坐标移动速度
	private int ySpeed = 2;   //y坐标移动速度
	private int awardType;    //奖励类型
	private static BufferedImage[] images;

	// 静态代码块
	static {
		images = new BufferedImage[5];
		for (int i = 1; i <= images.length; i++) {
			images[i-1] = loadImage("bee" + i + ".png");
		}
	}

	/** 初始化数据 */
	public Bee(){
		this.image = loadImage("bee1.png");
		width = image.getWidth();
		height = image.getHeight();
		y = -height;
		Random rand = new Random();
		x = rand.nextInt(480 - width);
		awardType = rand.nextInt(2);   //初始化时给奖励
	}
	// 得到图片
		int index = 1;

		@Override
		public BufferedImage getImage() {// 10M
			if (isLife()) {
				return images[0];
			} else if (isDead()) {// 图片的切换
				BufferedImage img = images[index++];
				if (index == images.length) {
					state = REMOVE;
					index = 1;
				}
				return img;
			}

			return null;
		}

	
	/** 获得奖励类型 */
	public int getType(){
		return awardType;
	}

	/** 越界处理 */
	@Override
	public boolean outOfBounds() {
		return y>650;
	}

	/** 移动，可斜着飞 */
	@Override
	public void move() {      
		x += xSpeed;
		y += ySpeed;
		if(x > 480-width){  
			xSpeed = -1;
		}
		if(x < 0){
			xSpeed = 1;
		}
	}


}
