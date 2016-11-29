package ecm.main;

import java.awt.EventQueue;
import java.io.File;

import javax.imageio.ImageIO;

import cn.edu.nju.ecm.view.ECMMainFrame;

public class ECMMain {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ECMMainFrame window = new ECMMainFrame();
					window.getFrmEcm().setIconImage(ImageIO.read(new File("resources/Logo.png")));
					window.getFrmEcm().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
