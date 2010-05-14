package views;

import java.awt.Container;
import java.awt.Point;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


//The following class is used to instantiate a graphical
// user interface object.
public class PieMenuPanel extends JPanel {
	private Hashtable<String, JLabel> icons;
	private Container _container;
	private String[] _listIcon;
	private int _posX=0;
	private int _posY = 0;
	int widthDock = 500;
	int heightDock = 500;
	int nbreIcon = 0;
	double thetaAngle;
	double pi = 3.1416;
	int sizeRotor = 150;
	int iconSize = 70;

public PieMenuPanel(Container panelAff, String[] listIcon, int PosX, int PosY){//constructor
 	_container = panelAff;
 	_listIcon = listIcon;
 	_posX = PosX;
 	_posY = PosY;
 	initialize();

}

 private void initialize() {
	 // Récupère le nombre d'icone du menu
	 nbreIcon = _listIcon.length;
	 thetaAngle = (2*pi/nbreIcon);
	 System.out.println("nbreIcon: " + nbreIcon);
	 int X=0, Xprim =0;
	 int Y=sizeRotor, Yprim =sizeRotor;
	 double theta = thetaAngle;
	 System.out.println("theta: " + theta);
	 //	Création dynamique des icones
	 icons = new Hashtable<String, JLabel>();
	 for (int i=1;i<=nbreIcon; i++) {
		 icons.put("icon" + i, new JLabel(new ImageIcon(_listIcon[i-1])));
		 System.out.println("ok1");
		 icons.get("icon" + i).setBounds(Xprim+_posX - iconSize/2, Yprim+_posY- iconSize/2, iconSize, iconSize);
		 Xprim = (int) (X * Math.cos(theta) - Y * Math.sin(theta));
		 Yprim = (int) (X* Math.sin(theta) + Y * Math.cos(theta));
		 theta+=thetaAngle;
		 _container.add(icons.get("icon" + i));
	 }
	 _container.update(_container.getGraphics());

}//end class GUI definition*

public Point getCentreMenu(){
	Point point= new Point(_posX, _posY);
	return point;
}
public boolean isFermerMenu(Point point) {
	int x = point.x;
	int y = point.y;
	int rayonCarre = (sizeRotor+iconSize)*(sizeRotor+iconSize);
	int dist = (_posX - x)*(_posX - x) + (_posY - y)*(_posY - y);
	if (dist <=rayonCarre)
		return false;
	else
		return true;
}

public void fermerMenu() {
	for (int i=1; i<= nbreIcon; i++) {
		_container.remove(icons.get("icon" + i));
		//icons.put("icon" + i, null);
	}
	 _container.update(_container.getGraphics());
}

}
