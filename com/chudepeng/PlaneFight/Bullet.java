package com.chudepeng.PlaneFight;

import java.awt.image.BufferedImage;
/**
* �ӵ���
*
* @author ������
*/
public class Bullet extends FlyingObject {
private int speed = 3;  //�ƶ����ٶ�
	
	/** ��ʼ������ */
	public Bullet(int x,int y){
		this.x = x;
		this.y = y;
		this.image = loadImage("bullet.png");
	}

	/** �ƶ� */
	@Override
	public void move(){   
		y-=speed;
	}

	/** Խ�紦�� */
	@Override
	public boolean outOfBounds() {
		return y<-height;
	}

}
