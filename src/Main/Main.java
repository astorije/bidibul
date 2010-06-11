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
		//new PieMenuPanel (null, 100 + 100,200+ 100 );
		//this.add(_pieMenuPanel);
/*
		Set<Class<BidibulModule>> s = ModuleLoader.getInstance().getSetAllModules();
		Iterator<Class<BidibulModule>> i = s.iterator();
		Class<BidibulModule> y=null;
		while(i.hasNext()) {
			Class<BidibulModule> c = i.next();
			if (BidibulInformation.get("name", c).equals("yep"))
				y = c;
			System.out.println("name : "+ BidibulInformation.get("name", c) + " description : " + BidibulInformation.get("description", c) + " actif : "  + ModuleLoader.getInstance().isActive(c));
		}
		ModuleLoader.getInstance().stopModule(y);
		System.out.println("name : "+ BidibulInformation.get("name", y) + " description : " + BidibulInformation.get("description", y) + " est actif : "  + ModuleLoader.getInstance().isActive(y));
		ModuleLoader.getInstance().startModule(y);
		System.out.println("name : "+ BidibulInformation.get("name", y) + " description : " + BidibulInformation.get("description", y) + " est actif : "  + ModuleLoader.getInstance().isActive(y));
*/	}
}
