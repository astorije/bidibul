package Main;

import java.net.URL;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import views.LoaderFrame;

public class Main {

	public static URL getResource(String url) {
		return Main.class.getResource("/img/" + url);
	}

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(
				UIManager.getSystemLookAndFeelClassName()
			);
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		new LoaderFrame();
	}
}
