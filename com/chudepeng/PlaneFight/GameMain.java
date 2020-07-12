package com.chudepeng.PlaneFight;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.URI;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * ����������
 *
 * @author ������
 * @version 1.00
 */
public class GameMain extends JFrame {

	public static void main(String[] args) {
		GameMain j = new GameMain(); // ��������
		GameJPanel g = new GameJPanel(); // ��������
		j.add(g); // ��ӻ���
		j.setSize(480, 650);
		j.setTitle("PlaneFight"); // ���ñ���
        j.setResizable(false);//���ô�С�Ƿ�ɵ�
		j.setLocationRelativeTo(null); // ���þ���
		j.setDefaultCloseOperation(3); // Ĭ�Ϲر�
		j.setVisible(true); // ���ڿɼ���
		j.setIconImage(new ImageIcon("src/images/��ͨ�ɻ�.png").getImage()); // ���ô����ͼ��
		j.setVisible(true); // �������paint
		g.action();//���йؼ�����
		Thread thread = new Thread(new Runnable() {//����һ���߳����ڲ��ű�������
			File f;
			URI uri;
			URL url;
			@Override
			public void run() {
				while (true) {
					try {
						f = new File("src/music/�ɻ���ս.wav");
						uri = f.toURI();
						url = uri.toURL();
						AudioClip aau;
						aau = Applet.newAudioClip(url);
						aau.play();// ѭ������
						Thread.sleep(20000);
					} catch (Exception e) {
						e.printStackTrace();
			} 	}	}
		});
		thread.start();// �����߳�

	}

}
