package views;

import java.awt.datatransfer.DataFlavor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import models.Flash;
import tools.BidibulInformation;
import tools.ModuleLoader;
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
	private ArrayList<BidibulModule> _listeModules, _listeAffichage;
	private DataFlavor _flavor;
	private int _posX=0;
	private int _posY = 0;
	int widthDock = 300;
	int heightDock = 300;
	int nbreIcons = 0;
	double thetaAngle;
	double pi = 3.1416;
	int sizeRotor = 135;
	private static int _ICONSIZE = 50;
	Boolean iconVisible = false;
	String[] _listeFichier;
	private static int _HEIGHT_FOR_HIDE = 30;
	private int num_icon_aff = 1;

	/**
	 * Constructeur du PieMenu:
	 * @param panelAff
	 * @param PosX
	 * @param PosY
	 */
	public PieMenuPanel(int PosX, int PosY){//constructor
		setLayout(null);
		//_listIcon = listIcon;
		_posX = PosX;
		_posY = PosY;
		icons = new Hashtable<String, JLabel>();
		modules = new Hashtable<String, BidibulModule>();
		_listeAffichage = new ArrayList<BidibulModule>();
		_listeModules = new ArrayList<BidibulModule>();
//		_container.update(_container.getGraphics());
		setVisible(false);
		setOpaque(false);
	//	_listeFichier;
	}

	public void setFichierInfo(ArrayList<String> listFichier, DataFlavor flavor) {
		_listeFichier = listFichier.toArray(new String[listFichier.size()]);
		_flavor = flavor;
	}

	/**
	 * Cette fonction rafraichit le panel en fonction des modules à afficher
	 * @param listeModulesClickable
	 * @mode Le mode des modules passé en paramètre:
	 * 		0: tous les modules
	 * 		1: modules clickable
	 * 		2: modules droppable
	 */
	public void refresh(int mode) {
		num_icon_aff = 1;
		if (mode == 1)
		{
			updateClickableModules();
		}
		if (mode == 2)
		{
			updateDroppableModules();
		}

		nbreIcons = _listeAffichage.size();
		System.out.println("rafraichissement demandé : "+ (nbreIcons-num_icon_aff));

		//Mise à zéro
		Flash.down();
		CreateDynamicIcon(mode);
	}

	/**
	 * Création dynamique des icones
	 */
	private void CreateDynamicIcon(int mode)
	{
		if ((nbreIcons-num_icon_aff)<8)
		{
			thetaAngle = (2*pi/(nbreIcons-num_icon_aff+1));
		} else {
			thetaAngle = (2*pi/8);
		}
		System.out.println("num_icon_aff:" + num_icon_aff);
		icons.clear();
		this.removeAll();
		int X=0, Xprim =0;
		int Y=sizeRotor, Yprim =sizeRotor;
		double theta = thetaAngle;
		int i, next = 1;
		if (nbreIcons>8)
		{
			icons.put("icon" + num_icon_aff, new JLabel(new ImageIcon("img/next.png")));
			System.out.println("next is needed");
			icons.get("icon" + num_icon_aff).setBounds(Xprim+_posX - _ICONSIZE/2, Yprim+_posY- _ICONSIZE/2, _ICONSIZE, _ICONSIZE);
			icons.get("icon" + num_icon_aff).setVisible(true);
			icons.get("icon" + num_icon_aff).addMouseListener(new actionOnClic(num_icon_aff, mode, next));
			Xprim = (int) (X * Math.cos(theta) - Y * Math.sin(theta));
			Yprim = (int) (X* Math.sin(theta) + Y * Math.cos(theta));
			if (Yprim < _HEIGHT_FOR_HIDE) {
				Flash.up();
			}
			theta+=thetaAngle;
			this.add(icons.get("icon" + num_icon_aff));
			next = 0;
		} else {
			next = 0;
		}
		//Création dynamique des icones
		for (i=num_icon_aff+next; i<=nbreIcons; i++)
		{
			//Cas Clickable
			if (mode == 1)
			{
				icons.put("icon" + i, new JLabel(((iClickable) _listeAffichage.get(i-1)).getClickIcon()));
				System.out.println(((iClickable) _listeAffichage.get(i-1)).getClickIcon().toString());
			}
			if (mode == 2)
			{
				icons.put("icon" + i, new JLabel(((iDroppable) _listeAffichage.get(i-1)).getDropIcon()));
				System.out.println(((iDroppable) _listeAffichage.get(i-1)).getDropIcon().toString());
			}
			modules.put("icon" + i, _listeAffichage.get(i-1));
			icons.get("icon" + i).setBounds(Xprim+_posX - _ICONSIZE/2, Yprim+_posY- _ICONSIZE/2, _ICONSIZE, _ICONSIZE);
			icons.get("icon" + i).setVisible(true);
			icons.get("icon" + i).addMouseListener(new actionOnClic(i, mode, next));
			Xprim = (int) (X * Math.cos(theta) - Y * Math.sin(theta));
			Yprim = (int) (X* Math.sin(theta) + Y * Math.cos(theta));
			if (Yprim < _HEIGHT_FOR_HIDE) {
				Flash.up();
			}
			theta+=thetaAngle;
			this.add(icons.get("icon" + i));
			if (i==8) {
				break;
			}
		}
		this.setVisible(true);
	}

	/**
	 * Construit l'ensemble des listes de modules nécessaires (générale,
	 * clickable, droppable)
	 */
	public void updateClickableModules() {
		_listeAffichage.clear();
		_listeModules.clear();
		_listeModules = new ArrayList<BidibulModule>(ModuleLoader.getInstance().getListActiveModules());
		// Mise à jour des modclickable
		Iterator<BidibulModule> i = _listeModules.listIterator();
		BidibulModule tempMod;
		while (i.hasNext()) {
			tempMod = i.next();
			if (tempMod instanceof iClickable)
				_listeAffichage.add(tempMod);
		}

	}

	public class actionOnClic extends MouseAdapter {
		int numIcon;
		int mode;
		int next;
		public actionOnClic(int num, int iMode, int inext) {
			numIcon = num;
			mode = iMode;
			next = inext;
		}
		// Entrée de survol du module
		@Override
		public void mouseEntered(MouseEvent e)
		{
			System.out.println("next: " + next);
			if (next == 1)
			{
				String tooltip = "Autres modules disponibles";
				Flash.notice(tooltip);
				return;
			}
			if (mode == 1)
			{
				System.out.println("mode: " + mode);
				System.out.println("numIcon: " + numIcon);
				String tooltip = ((iClickable) modules.get("icon" + numIcon)).getClickTooltip();
				Flash.notice(tooltip);
			}
			if (mode == 2)
			{
				String tooltip = ((iDroppable) modules.get("icon" + numIcon)).getDropTooltip();
				Flash.notice(tooltip);
			}

		}

		//Sortie de survol du module
		@Override
		public void mouseExited(MouseEvent e)
		{
			Flash.rollback();
		}
		@Override
		public void mouseReleased(MouseEvent e)						//sur clic de la souris
		{
			if (e.getButton() == MouseEvent.BUTTON1) 				//Si clic gauche
			{
				if (next == 1)
				{
					setVisible(false);
					num_icon_aff += 7;
					if (num_icon_aff>nbreIcons)
					{
						num_icon_aff = 1;
					}
					System.out.println("num_icon_aff:" + num_icon_aff);
					CreateDynamicIcon(mode);
					return;
				}
				// Cas du click
				if (mode == 1)
				{
					//setIconVisible(false);			//Cache le PieMenu
					setVisible(false);			//Cache le PieMenu
					//_container.update(_container.getGraphics());
					System.out.println("click hide!");
					((iClickable) modules.get("icon" + numIcon)).click();
				}
				if (mode == 2)
				{
					//setIconVisible(false);			//Cache le PieMenu
					setVisible(false);			//Cache le PieMenu
					//_container.update(_container.getGraphics());
					((iDroppable) modules.get("icon" + numIcon)).drop(_listeFichier);
				}
			}
		}
	}

	/**
	 * Construit l'ensemble des listes de modules nécessaires (générale,
	 * clickable, droppable)
	 *
	 * @param flavor
	 * @param flavor
	 *            Le type des fichiers passés en paramètres
	 */
	public void updateDroppableModules() {
		_listeAffichage.clear();
		_listeModules.clear();
		_listeModules = new ArrayList<BidibulModule>(ModuleLoader.getInstance().getListActiveModules());
		Iterator<BidibulModule> i = _listeModules.listIterator();
		BidibulModule tempMod;
		// Parcours de l'ensemble des modules...
		while (i.hasNext()) {
			tempMod = i.next();
			// Si le module est droppable...
			if (tempMod instanceof iDroppable) {
				// Vérifie que le module peut agir sur le type des documents
				// drag&droppés:
				if (((iDroppable) tempMod).getAllowedFlavor().contains(_flavor))
					// S'il s'agit de chaine: ajout direct du module direct:
					if (_flavor.equals(DataFlavor.stringFlavor)) {
						System.out.println("travail sur des string");
						_listeAffichage.add(tempMod);
					}
				// S'il s'agit de fichiers
				if (_flavor.equals(DataFlavor.javaFileListFlavor)) {
					System.out.println("travail sur des fichiers");
					// Récupère la liste des extensions adminissibles par le
					// module en cours d'étude
					ArrayList<String> listeExtension = new ArrayList<String>();
					listeExtension = ((iDroppable) tempMod)
							.getAllowedExtensions();

					if (listeExtension.contains(".*")) {
						_listeAffichage.add(tempMod);
					} else {

						// Récupère la liste des extensions récupérées
						String tabExtensionRecupere[] = getExtensionFromListFile(_listeFichier);
						// Pour chaque extension récupéré
						for (int j = 0; j < tabExtensionRecupere.length; j++) {
							// Si l'extension se trouve dans la liste des
							// extensions acceptable
							if (listeExtension
									.contains(tabExtensionRecupere[j])) {
								// Le module est admis
								_listeAffichage.add(tempMod);
							}
							System.out.println(tabExtensionRecupere[j]);
						}
					}
				}

			}
		}
	}

	/**
	 * Récupère la liste des extensions dans les fichiers passés en paramètres
	 *
	 * @param listeFichier
	 * @return
	 */
	private String[] getExtensionFromListFile(String[] listeFichier) {
		System.out.println("recehrceh des extensions");
		ArrayList<String> listExt = new ArrayList<String>();
		//Iterator<String> i = _listeFichier.listIterator();
		// Parcours des fichiers...
		for (int i=0; i<_listeFichier.length; i++) {
			String tempFile = _listeFichier[i];
			// Récupération de l'extenstion
			String ext = tempFile.substring(tempFile.lastIndexOf("."));
			// Si l'extension n'a pas encore été récupérée:
			if (!listExt.contains(ext)) {
				// Ajout de l'extension
				listExt.add(ext);
			}
		}
		String ret[] = listExt.toArray(new String[listExt.size()]);
		return ret;
	}

	/**
	 * Fonction destinée à disparaître: permet de vérifier la validité des
	 * construction de List pour les modules
	 *
	 * @author Miro
	 * @return
	 */
	void VerifList(String nomListe, ArrayList<BidibulModule> maListTemp) {
		System.out.println("vérification de la liste '" + nomListe + "' : ");
		if (!maListTemp.isEmpty()) {
			Iterator<BidibulModule> i = maListTemp.listIterator();
			while (i.hasNext()) {
				System.out.println("     - " + BidibulInformation.get("name", i.next())/*i.next().getName()*/);
			}
		}
	}
}

