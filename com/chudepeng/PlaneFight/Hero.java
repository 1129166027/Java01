package com.chudepeng.PlaneFight;

import java.awt.image.BufferedImage;
/**
* 英雄机类
*
* @author 储德鹏
*/
public class Hero extends FlyingObject {
	
	private BufferedImage[] images = {};  //英雄机图片
	private int index = 0;                //英雄机图片切换索引
	
	private int doubleFire;   //双倍火力
	private int life;   //命
	
	/** 初始化数据 */
	public Hero(){
		life = 3;   //初始3条命
		doubleFire = 0;   //初始火力为1
		images = new BufferedImage[]{loadImage("hero0.png"), loadImage("hero1.png")}; //英雄机图片数组
		image = images[0];   //初始为hero0图片
		setHeight(image.getHeight()); 
		setWidth(image.getWidth());
		setX(190);
		setY(400);
	}
	
	/** 增命 */
	public void addLife(){  //增命
		life++;
	}
	
	/** 减命 */
	public void subtractLife(){   //减命
		life--;
	}
	
	/** 获取命 */
	public int getLife(){
		return life;
	}
	
	/** 当前物体移动，x,y相对于鼠标位置的距离  */
	public void moveTo(int x,int y){   
		this.x = x - width/2;
		this.y = y - height/2;
	}

	
	@Override
	public boolean outOfBounds() {
		return false;
	}
	
	/**英雄机产生子弹*/
	public Bullet[] shoot(){
		int xStep = this.width/4;
		int yStep = 15;
		if (doubleFire > 0) {//双倍火力
			Bullet[] bs = new Bullet[2];
			bs[0] = new Bullet(this.x + xStep * 1, this.y - yStep);
			bs[1] = new Bullet(this.x + xStep * 3, this.y - yStep);
			return bs;
		}else{
			Bullet[] bs = new Bullet[1];
			bs[0] = new Bullet(this.x + xStep * 2, this.y - yStep);
			return bs;
		}
	}
	/** 让英雄机移动的时候产生动态效果*/
	@Override
	public void move() {
		if(images.length>0){
			if(index==0)
			image = images[++index];  //切换图片hero0，hero1
			else 
			image=images[--index];	
			
		}
	}
	/** 碰撞算法 */
	public boolean hit(FlyingObject other){
		
		int x1 = other.x - this.width/2;                 //x坐标最小距离
		int x2 = other.x + this.width/2 + other.width;   //x坐标最大距离
		int y1 = other.y - this.height/2;                //y坐标最小距离
		int y2 = other.y + this.height/2 + other.height; //y坐标最大距离
	
		int herox = this.x + this.width/2;               //英雄机x坐标中心点距离
		int heroy = this.y + this.height/2;              //英雄机y坐标中心点距离
		
		return herox>x1 && herox<x2 && heroy>y1 && heroy<y2;   //区间范围内为撞上了
	}
	
	/** 打开双倍火力*/
	public void setDoubleFire(int i) {
		doubleFire=i;
		
	}


}
