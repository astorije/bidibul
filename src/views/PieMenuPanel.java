package views;

import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JPanel;

import models.Flash;
import utils.BidibulModule;
import utils.iClickable;
import utils.iDroppable;


/**
 * Implémentation d'un PieMenu.
 * @author Miroslav
 */
public class PieMenuPanel extends JPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
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
	int sizeRotor = 200;
	int iconSize = 70;
	Boolean iconVisible = false;
	String[] _listeFichier;

	/**
	 * Constructeur du PieMenu:
	 * @param panelAff
	 * @param PosX
	 * @param PosY
	 */
	public PieMenuPanel(Container panelAff, int PosX, int PosY){//constructor
		_container = panelAff;
		//_listIcon = listIcon;
		_posX = PosX;
		_posY = PosY;
		icons = new Hashtable<String, JLabel>();
		modules = new Hashtable<String, BidibulModule>();
		_container.update(_container.getGraphics());
		this.setVisible(true);
	//	_listeFichier;
	}
	public Container getContainer() {
		return this._container;
	}

	public void setListeFichier(ArrayList<String> listFichier) {
		_listeFichier = listFichier.toArray(new String[listFichier.size()]);
	}
	/**
	 * Rend le menu visible ou invisible
	 * @param arg
	 */
	public void setIconVisible(Boolean arg) { //affiche ou cache les icones du menu selon l'argument
		for (int i=1; i<= nbreIcons; i++){
			icons.get("icon" + i).setVisible(arg);
		}
		this.iconVisible = arg;
	}

	/**
	 * Vérifie si le menu est actuellement en cours d'affichage
	 * @return
	 */
	public Boolean getIconVisible() { //renvoie l'état d'affichage des icones
		return this.iconVisible;
	}

	/**
	 * Cette fonction rafraichit le panel en fonction des modules à afficher
	 * @param listeModulesClickable
	 * @mode Le mode des modules passé en paramètre:
	 * 		0: tous les modules
	 * 		1: modules clickable
	 * 		2: modules droppable
	 */
	public void refresh(ArrayList<BidibulModule> listeAffichage, int mode) {
		System.out.println("rafraichissement demandé");
		nbreIcons = listeAffichage.size();
		thetaAngle = (2*pi/nbreIcons);
		int X=0, Xprim =0;
		int Y=sizeRotor, Yprim =sizeRotor;
		double theta = thetaAngle;
		int i;
		icons.clear();
		//	Création dynamique des icones
		for (i=1;i<=nbreIcons; i++) {
			//Cas Clickable
			if (mode == 1)
			{
				icons.put("icon" + i, new JLabel(((iClickable) listeAffichage.get(i-1)).getClickIcon()));
				System.out.println(((iClickable) listeAffichage.get(i-1)).getClickIcon().toString());
			}
			if (mode == 2)
			{
				icons.put("icon" + i, new JLabel(((iDroppable) listeAffichage.get(i-1)).getDropIcon()));
				System.out.println(((iDroppable) listeAffichage.get(i-1)).getDropIcon().toString());
			}
			modules.put("icon" + i, listeAffichage.get(i-1));
			icons.get("icon" + i).setBounds(Xprim+_posX - iconSize/2, Yprim+_posY- iconSize/2, iconSize, iconSize);
			icons.get("icon" + i).setVisible(true);
			icons.get("icon" + i).addMouseListener(new actionOnClic(i, mode));
			Xprim = (int) (X * Math.cos(theta) - Y * Math.sin(theta));
			Yprim = (int) (X* Math.sin(theta) + Y * Math.cos(theta));
			theta+=thetaAngle;
			this.add(icons.get("icon" + i));
		}
		this.update(this.getGraphics());


		//_container.update(_container.getGraphics());

	}
	public class actionOnClic extends MouseAdapter {
		int numIcon;
		int mode;
		public actionOnClic(int num, int iMode) {
			numIcon = num;
			mode = iMode;
		}
		//Survole du module
		@Override
		public void mouseEntered(MouseEvent e)
		{
			if (mode == 1)
			{
				/*String tooltip = ((iClickable) modules.get("icon" + numIcon)).getClickTooltip();
				Flash.notice(tooltip);*/
			}
			if (mode ==2)
			{
				String tooltip = ((iDroppable) modules.get("icon" + numIcon)).getDropTooltip();
				Flash.notice(tooltip);
			}
		}
		@Override
		public void mouseReleased(MouseEvent e)						//sur clic de la souris
		{
			/* -- clic gauche -- */
			if (e.getButton() == MouseEvent.BUTTON1) 				//Si clic gauche
			{
				// Cas du click
				if (mode == 1)
				{
					setIconVisible(false);			//Cache le PieMenu
					_container.update(_container.getGraphics());
					System.out.println("click hide!");
					((iClickable) modules.get("icon" + numIcon)).click();
				}
				if (mode == 2)
				{
					setIconVisible(false);			//Cache le PieMenu
					_container.update(_container.getGraphics());
					((iDroppable) modules.get("icon" + numIcon)).drop(_listeFichier);
				}
			}
		}
	}

}
