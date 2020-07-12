package com.chudepeng.PlaneFight;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;

import javax.imageio.ImageIO;


/**
* 所有飞行物的父类
* 蜜蜂、子弹、敌机、英雄机
*
* @author 储德鹏
* @version 1.00
*/
public abstract class FlyingObject {
	protected BufferedImage image;
	
	//成员变量
	protected int width;//宽
	protected int height;//高
	protected int x;//x坐标
	protected int y;//y坐标
	
	//设计三种状态
	public static final int LIFE=1;//存活
	public static final int DEAD=0;//over
	public static final int REMOVE=-1;//删除
	public int state = LIFE;//初始状态为存活
	
	/** 移动的方法 */
	public void move() {
	}
	
	/** 读取图片 */
	public static BufferedImage loadImage(String fileName) {
		try {
			// 同包之内的图片读取
			BufferedImage img =ImageIO.read(new FileInputStream("src/images/"+fileName));
			return img;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/** 画图片 g:画笔 */
	public void paintObject(Graphics g) {
		g.drawImage(this.image, this.x,this.y,null);
	}
	/**获取图片*/
	public BufferedImage getImage() {
		// TODO Auto-generated method stub
		return null;
	}

	
	/** 判断当前状态是不是存活的 */
	public boolean isLife() {
		return state == LIFE;
	}

	/** 判断当前状态是不是over的 */
	public boolean isDead() {
		return state == DEAD;
	}
	/** 飞行物over */
	public void goDead() {
		state = DEAD;// 将对象状态修改为DAED

	}

	/** 判断当前状态是不是删除的 */
	public boolean isRemove() {
		return state == REMOVE;
	}
	
	/**
	 * 检查是否出界
	 * @return true 出界与否
	 */
	public abstract boolean outOfBounds();
	
	/**
	 * 检查当前飞行物体是否被子弹(x,y)击(shoot)中
	 * @param Bullet 子弹对象
	 * @return true表示被击中了
	 */
	public boolean shootBy(Bullet bullet){
		int x = bullet.x;  //子弹横坐标
		int y = bullet.y;  //子弹纵坐标
		return this.x<x && x<this.x+width && this.y<y && y<this.y+height;
	}
	
	/** 在Source中选Generate，自动写set、get方法*/

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	


}
