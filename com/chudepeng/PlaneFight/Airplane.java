package com.chudepeng.PlaneFight;

import java.awt.image.BufferedImage;
import java.util.Random;
/**
* �л���
*
* @author ������
* @version 1.00
*/
public class Airplane extends FlyingObject implements Enemy {
	private int speed = 3; // �ƶ�����

	/** ��ʼ������ */
	public Airplane() {
		this.image = loadImage("airplane1.png");
		width = image.getWidth();
		height = image.getHeight();
		y = -height;
		Random rand = new Random();
		x = rand.nextInt(480 - width);
	}
	private static BufferedImage[] images;
	//��̬�����
	static {
		images = new BufferedImage[5];
		for (int i = 1; i <= images.length; i++) {
			images[i-1] = loadImage("airplane" + i + ".png");
		}
	}
	
	//�õ�ͼƬ
	int index = 1;
	@Override
	public BufferedImage getImage() {//10M
		if (isLife()) {
			return images[0];
		}else if(isDead()){//ͼƬ���л�
			image = images[index++];
			if (index == images.length) {
				state = REMOVE;
				index = 1;
			}
			return image;
		}
		/*images[index++];
		 *   index = 1;
		 *   10M  images[1] index = 2   ����images[1]
		 *   20M  images[2] index = 3   ����images[2]
		 *   30M  images[3] index = 4   ����images[3]
		 * 	 40M  images[4] index = 5   ����images[4]
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
