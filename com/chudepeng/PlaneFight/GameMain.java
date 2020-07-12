package com.chudepeng.PlaneFight;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.URI;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * 主函数的类
 *
 * @author 储德鹏
 * @version 1.00
 */
public class GameMain extends JFrame {

	public static void main(String[] args) {
		GameMain j = new GameMain(); // 创建窗口
		GameJPanel g = new GameJPanel(); // 创建画布
		j.add(g); // 添加画布
		j.setSize(480, 650);
		j.setTitle("PlaneFight"); // 设置标题
        j.setResizable(false);//设置大小是否可调
		j.setLocationRelativeTo(null); // 设置居中
		j.setDefaultCloseOperation(3); // 默认关闭
		j.setVisible(true); // 窗口可见性
		j.setIconImage(new ImageIcon("src/images/卡通飞机.png").getImage()); // 设置窗体的图标
		j.setVisible(true); // 尽快调用paint
		g.action();//运行关键方法
		Thread thread = new Thread(new Runnable() {//建立一个线程用于播放背景音乐
			File f;
			URI uri;
			URL url;
			@Override
			public void run() {
				while (true) {
					try {
						f = new File("src/music/飞机大战.wav");
						uri = f.toURI();
						url = uri.toURL();
						AudioClip aau;
						aau = Applet.newAudioClip(url);
						aau.play();// 循环播放
						Thread.sleep(20000);
					} catch (Exception e) {
						e.printStackTrace();
			} 	}	}
		});
		thread.start();// 开启线程

	}

}
