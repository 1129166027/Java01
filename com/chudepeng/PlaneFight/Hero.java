package com.chudepeng.PlaneFight;

import java.awt.image.BufferedImage;
/**
* Ӣ�ۻ���
*
* @author ������
*/
public class Hero extends FlyingObject {
	
	private BufferedImage[] images = {};  //Ӣ�ۻ�ͼƬ
	private int index = 0;                //Ӣ�ۻ�ͼƬ�л�����
	
	private int doubleFire;   //˫������
	private int life;   //��
	
	/** ��ʼ������ */
	public Hero(){
		life = 3;   //��ʼ3����
		doubleFire = 0;   //��ʼ����Ϊ1
		images = new BufferedImage[]{loadImage("hero0.png"), loadImage("hero1.png")}; //Ӣ�ۻ�ͼƬ����
		image = images[0];   //��ʼΪhero0ͼƬ
		setHeight(image.getHeight()); 
		setWidth(image.getWidth());
		setX(190);
		setY(400);
	}
	
	/** ���� */
	public void addLife(){  //����
		life++;
	}
	
	/** ���� */
	public void subtractLife(){   //����
		life--;
	}
	
	/** ��ȡ�� */
	public int getLife(){
		return life;
	}
	
	/** ��ǰ�����ƶ���x,y��������λ�õľ���  */
	public void moveTo(int x,int y){   
		this.x = x - width/2;
		this.y = y - height/2;
	}

	
	@Override
	public boolean outOfBounds() {
		return false;
	}
	
	/**Ӣ�ۻ������ӵ�*/
	public Bullet[] shoot(){
		int xStep = this.width/4;
		int yStep = 15;
		if (doubleFire > 0) {//˫������
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
	/** ��Ӣ�ۻ��ƶ���ʱ�������̬Ч��*/
	@Override
	public void move() {
		if(images.length>0){
			if(index==0)
			image = images[++index];  //�л�ͼƬhero0��hero1
			else 
			image=images[--index];	
			
		}
	}
	/** ��ײ�㷨 */
	public boolean hit(FlyingObject other){
		
		int x1 = other.x - this.width/2;                 //x������С����
		int x2 = other.x + this.width/2 + other.width;   //x����������
		int y1 = other.y - this.height/2;                //y������С����
		int y2 = other.y + this.height/2 + other.height; //y����������
	
		int herox = this.x + this.width/2;               //Ӣ�ۻ�x�������ĵ����
		int heroy = this.y + this.height/2;              //Ӣ�ۻ�y�������ĵ����
		
		return herox>x1 && herox<x2 && heroy>y1 && heroy<y2;   //���䷶Χ��Ϊײ����
	}
	
	/** ��˫������*/
	public void setDoubleFire(int i) {
		doubleFire=i;
		
	}


}
