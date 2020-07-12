package com.chudepeng.PlaneFight;

import java.awt.image.BufferedImage;
/**
* 子弹类
*
* @author 储德鹏
*/
public class Bullet extends FlyingObject {
private int speed = 3;  //移动的速度
	
	/** 初始化数据 */
	public Bullet(int x,int y){
		this.x = x;
		this.y = y;
		this.image = loadImage("bullet.png");
	}

	/** 移动 */
	@Override
	public void move(){   
		y-=speed;
	}

	/** 越界处理 */
	@Override
	public boolean outOfBounds() {
		return y<-height;
	}

}
