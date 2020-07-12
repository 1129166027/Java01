package com.chudepeng.PlaneFight;

import java.awt.image.BufferedImage;
import java.util.Random;

public class Bee extends FlyingObject implements Award{
	private int xSpeed = 1;   //x�����ƶ��ٶ�
	private int ySpeed = 2;   //y�����ƶ��ٶ�
	private int awardType;    //��������
	private static BufferedImage[] images;

	// ��̬�����
	static {
		images = new BufferedImage[5];
		for (int i = 1; i <= images.length; i++) {
			images[i-1] = loadImage("bee" + i + ".png");
		}
	}

	/** ��ʼ������ */
	public Bee(){
		this.image = loadImage("bee1.png");
		width = image.getWidth();
		height = image.getHeight();
		y = -height;
		Random rand = new Random();
		x = rand.nextInt(480 - width);
		awardType = rand.nextInt(2);   //��ʼ��ʱ������
	}
	// �õ�ͼƬ
		int index = 1;

		@Override
		public BufferedImage getImage() {// 10M
			if (isLife()) {
				return images[0];
			} else if (isDead()) {// ͼƬ���л�
				BufferedImage img = images[index++];
				if (index == images.length) {
					state = REMOVE;
					index = 1;
				}
				return img;
			}

			return null;
		}

	
	/** ��ý������� */
	public int getType(){
		return awardType;
	}

	/** Խ�紦�� */
	@Override
	public boolean outOfBounds() {
		return y>650;
	}

	/** �ƶ�����б�ŷ� */
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
