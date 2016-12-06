package ecm.main;

import java.awt.EventQueue;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.UIManager;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import cn.edu.nju.ecm.view.ECMMainFrame;

public class ECMMain {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					try {
						BeautyEyeLNFHelper.launchBeautyEyeLNF();
						UIManager.put("RootPane.setupButtonVisible", false);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
