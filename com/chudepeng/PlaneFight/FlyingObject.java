package com.chudepeng.PlaneFight;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;

import javax.imageio.ImageIO;


/**
* ���з�����ĸ���
* �۷䡢�ӵ����л���Ӣ�ۻ�
*
* @author ������
* @version 1.00
*/
public abstract class FlyingObject {
	protected BufferedImage image;
	
	//��Ա����
	protected int width;//��
	protected int height;//��
	protected int x;//x����
	protected int y;//y����
	
	//�������״̬
	public static final int LIFE=1;//���
	public static final int DEAD=0;//over
	public static final int REMOVE=-1;//ɾ��
	public int state = LIFE;//��ʼ״̬Ϊ���
	
	/** �ƶ��ķ��� */
	public void move() {
	}
	
	/** ��ȡͼƬ */
	public static BufferedImage loadImage(String fileName) {
		try {
			// ͬ��֮�ڵ�ͼƬ��ȡ
			BufferedImage img =ImageIO.read(new FileInputStream("src/images/"+fileName));
			return img;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/** ��ͼƬ g:���� */
	public void paintObject(Graphics g) {
		g.drawImage(this.image, this.x,this.y,null);
	}
	/**��ȡͼƬ*/
	public BufferedImage getImage() {
		// TODO Auto-generated method stub
		return null;
	}

	
	/** �жϵ�ǰ״̬�ǲ��Ǵ��� */
	public boolean isLife() {
		return state == LIFE;
	}

	/** �жϵ�ǰ״̬�ǲ���over�� */
	public boolean isDead() {
		return state == DEAD;
	}
	/** ������over */
	public void goDead() {
		state = DEAD;// ������״̬�޸�ΪDAED

	}

	/** �жϵ�ǰ״̬�ǲ���ɾ���� */
	public boolean isRemove() {
		return state == REMOVE;
	}
	
	/**
	 * ����Ƿ����
	 * @return true �������
	 */
	public abstract boolean outOfBounds();
	
	/**
	 * ��鵱ǰ���������Ƿ��ӵ�(x,y)��(shoot)��
	 * @param Bullet �ӵ�����
	 * @return true��ʾ��������
	 */
	public boolean shootBy(Bullet bullet){
		int x = bullet.x;  //�ӵ�������
		int y = bullet.y;  //�ӵ�������
		return this.x<x && x<this.x+width && this.y<y && y<this.y+height;
	}
	
	/** ��Source��ѡGenerate���Զ�дset��get����*/

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
