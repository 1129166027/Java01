package com.chudepeng.PlaneFight;

import java.awt.image.BufferedImage;
import java.util.Random;
/**
* 敌机类
*
* @author 储德鹏
* @version 1.00
*/
public class Airplane extends FlyingObject implements Enemy {
	private int speed = 3; // 移动步骤

	/** 初始化数据 */
	public Airplane() {
		this.image = loadImage("airplane1.png");
		width = image.getWidth();
		height = image.getHeight();
		y = -height;
		Random rand = new Random();
		x = rand.nextInt(480 - width);
	}
	private static BufferedImage[] images;
	//静态代码块
	static {
		images = new BufferedImage[5];
		for (int i = 1; i <= images.length; i++) {
			images[i-1] = loadImage("airplane" + i + ".png");
		}
	}
	
	//得到图片
	int index = 1;
	@Override
	public BufferedImage getImage() {//10M
		if (isLife()) {
			return images[0];
		}else if(isDead()){//图片的切换
			image = images[index++];
			if (index == images.length) {
				state = REMOVE;
				index = 1;
			}
			return image;
		}
		/*images[index++];
		 *   index = 1;
		 *   10M  images[1] index = 2   返回images[1]
		 *   20M  images[2] index = 3   返回images[2]
		 *   30M  images[3] index = 4   返回images[3]
		 * 	 40M  images[4] index = 5   返回images[4]
		 * 
		 */
		return null;
	}



	@Override
	public boolean outOfBounds() {
		// TODO Auto-generated method stub
		return y>650;
	}

	@Override
	public int getScore() {
		// TODO Auto-generated method stub
		return 1;
	}
	@Override
	public void move() {
		y += speed;
	}
}
