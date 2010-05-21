package views;

import java.awt.Container;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JLabel;

import utils.BidibulModule;
import utils.iClickable;


//The following class is used to instantiate a graphical
// user interface object.
public class PieMenuPanel{
	private Hashtable<String, JLabel> icons;
	private Hashtable<String, BidibulModule> modules;
	private Container _container;
	private int _posX=0;
	private int _posY = 0;
	int widthDock = 500;
	int heightDock = 500;
	int nbreIcons = 0;
	double thetaAngle;
	double pi = 3.1416;
	int sizeRotor = 150;
	int iconSize = 70;
	Boolean iconVisible = false;

	public PieMenuPanel(Container panelAff, int PosX, int PosY){//constructor
		_container = panelAff;
		//_listIcon = listIcon;
		_posX = PosX;
		_posY = PosY;
		initialize();

	}

	private void initialize() {
		/*// Récupère le nombre d'icone du menu
		nbreIcons = _listIcon.length;
		thetaAngle = (2*pi/nbreIcons);
		System.out.println("nbreIcon: " + nbreIcons);
		int X=0, Xprim =0;
		int Y=sizeRotor, Yprim =sizeRotor;
		double theta = thetaAngle;
		System.out.println("theta: " + theta);
		//	Création dynamique des icones

		for (int i=1;i<=nbreIcons; i++) {
			icons.put("icon" + i, new JLabel(new ImageIcon(_listIcon[i-1])));
			//System.out.println("ok1");
			icons.get("icon" + i).setBounds(Xprim+_posX - iconSize/2, Yprim+_posY- iconSize/2, iconSize, iconSize);
			icons.get("icon" + i).setVisible(false);
			Xprim = (int) (X * Math.cos(theta) - Y * Math.sin(theta));
			Yprim = (int) (X* Math.sin(theta) + Y * Math.cos(theta));
			theta+=thetaAngle;
			_container.add(icons.get("icon" + i));
		}*/
		icons = new Hashtable<String, JLabel>();
		modules = new Hashtable<String, BidibulModule>();
		_container.update(_container.getGraphics());
//		_container.update(_container.getGraphics());

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
		for (int i=1; i<= nbreIcons; i++) {
			_container.remove(icons.get("icon" + i));
			//icons.put("icon" + i, null);
		}
		_container.update(_container.getGraphics());
	}

	public void setIconVisible(Boolean arg) { //affiche ou cache les icones du menu selon l'argument
		for (int i=1; i<= nbreIcons; i++){
			icons.get("icon" + i).setVisible(arg);
		}
		this.iconVisible = arg;
	}

	public Boolean getIconVisible() { //renvoie l'état d'affichage des icones
		return this.iconVisible;
	}

	/**
	 * Cette fonction rafraichit le panel en fonction des modules à afficher
	 * @param listeModulesClickable
	 */
	public void refresh(ArrayList<BidibulModule> listeAffichage) {
		nbreIcons = listeAffichage.size();
		thetaAngle = (2*pi/nbreIcons);
		int X=0, Xprim =0;
		int Y=sizeRotor, Yprim =sizeRotor;
		double theta = thetaAngle;

		//	Création dynamique des icones
		for (int i=1;i<=nbreIcons; i++) {
			icons.put("icon" + i, new JLabel(((iClickable) listeAffichage.get(i-1)).getIcon()));
			modules.put("icon" + i, listeAffichage.get(i-1));
			icons.get("icon" + i).setBounds(Xprim+_posX - iconSize/2, Yprim+_posY- iconSize/2, iconSize, iconSize);
			//icons.get("icon" + i).setVisible(false);
			icons.get("icon" + i).addMouseListener(new actionOnClic(i));
			Xprim = (int) (X * Math.cos(theta) - Y * Math.sin(theta));
			Yprim = (int) (X* Math.sin(theta) + Y * Math.cos(theta));
			theta+=thetaAngle;
			_container.add(icons.get("icon" + i));
		}
		_container.update(_container.getGraphics());

	}
	public class actionOnClic extends MouseAdapter {
		int numIcon;
		public actionOnClic(int i) {
			numIcon = i;
		}

		@Override
		public void mouseReleased(MouseEvent e)						//sur clic de la souris
		{
			/* -- clic gauche -- */
			if (e.getButton() == MouseEvent.BUTTON1) 				//Si clic gauche
			{
				((iClickable) modules.get("icon" + numIcon)).click();
			}
		}
	}
}
