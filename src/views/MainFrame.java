package views;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.WindowConstants;

import models.Flash;
import tools.BidibulInformation;
import tools.ModuleLoader;
import tools.MyFileTransferHandler;
import tools.TranslucentFrame;
import utils.BidibulModule;

// @todo kill me

/**
 * Frame principale du programme.
 * Appelée par le main.
 *
 * @author Jérémie ASTORI
 * @author Dominique CLAUSE
 */
public class MainFrame extends TranslucentFrame implements WindowListener {
	private static final long serialVersionUID = 1L;

	private BidibulPanel _bidibul;
	private PieMenuPanel _pieMenuPanel = null;
	private BidibulPopupMenu _bibidulPopupMenu;
	private ArrayList<BidibulModule> _listeModules, _listeModulesClickable;

	/**
	 * CONSTRUCTEUR
	 */
	public MainFrame(ArrayList<BidibulModule> listModuleTemp) {
		_listeModules = new ArrayList<BidibulModule>(listModuleTemp);
		this.output();
		this.initialize();
		// ---
		// Surcharge du constructeur. Lance la fonction onLoad des modules:
		// cette fonction permet la surcharge éventuelle du constructeur
		// ex: un module qui change l'apparence du bidibul
		if (_listeModules != null && !_listeModules.isEmpty()) {
			Iterator<BidibulModule> i = _listeModules.listIterator();
			while (i.hasNext()) {
				System.out.println("mod");
				i.next().onLoad();
			}
		}
	}

	/**
	 * @todo Constructeur installé par dépit, rien d'autre ne marchant quand aucun module n'est présent...
	 */
	public MainFrame() {
		this.output();
		this.initialize();
	}

	/**
	 * Définition des paramètres de fenêtrage
	 */
	public void output() {
		// Ne rien faire à la fermeture ([X] ou Alt-F4) car on veut une
		// fermeture personnalisée
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		// Surcharge les opérations de fenêtrage, comme la fermeture de
		// l'application par exemple
		this.addWindowListener(this);

		this.setVisible(true);
		this.pack();
	}

	/**
	 * Initialisation des composants et affichage
	 */
	public void initialize() {

		// Initialisation des listes:
		_listeModulesClickable = new ArrayList<BidibulModule>();

		// Vérif
		VerifList("_listModules", _listeModules); // @todo JA a desactivé pour cause de crash complet

		this.setLocation(100, 100);
		this.setSize(550, 600);
		this.setLayout(null);

		// --CREATION DU BIDIBUL
		// @todo Séparer la classe pour plus de modularité
		_bidibul = new BidibulPanel(this);
		_bidibul.setBounds(100, 200, 300, 300);
		_bidibul.addMouseListener(new actionOnClic());
		this.add(_bidibul);

		// -- Fin création bidibul


		// NotificationPanel
		FlashPanel panFlash = new FlashPanel(Flash.getInstance());
		panFlash.setPreferredSize(this.getMaximumSize());
		panFlash.setBounds(200, 0, 311, 133);
		this.add(panFlash);

		// PieMenuPanel
		_pieMenuPanel = new PieMenuPanel (MainFrame.this, _bidibul.getX()+ _bidibul.getWidth()/2, _bidibul.getY()+ _bidibul.getHeight()/2 );
		_pieMenuPanel.setBounds(0, 0 , 10000, 10000);
		this.add(_pieMenuPanel);

		// Analyse des listes (clickable)
		updateClickableModules();
		VerifList("_listModulesClickable", _listeModulesClickable);

		// Création du menu contextuel
		this._bibidulPopupMenu = new BidibulPopupMenu(this);

		// Ajout du listener de menu contextuel

		this._bidibul.setTransferHandler(new MyFileTransferHandler(
				_pieMenuPanel, _listeModules));

	    this._bidibul.addMouseListener(new PopupMenuListener());

	    // new ModuleManagerFrame(); // @todo DEV Jérémie ASTORI



	}

	public class actionOnClic extends MouseAdapter {
		@Override
		public void mouseReleased(MouseEvent e) {
			// Clic gauche
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (_pieMenuPanel.getIconVisible() == false) {
					_pieMenuPanel.refresh(_listeModulesClickable, 1);
					_pieMenuPanel.setIconVisible(true);				//Affiche le PieMenu
					MainFrame.this.update(MainFrame.this.getGraphics());
					//MainFrame.this.repaint();
					System.out.println("click show!");
				}
				else {
					_pieMenuPanel.setIconVisible(false);			//Cache le PieMenu
					MainFrame.this.update(MainFrame.this.getContentPane().getGraphics());
					System.out.println("click hide!");
				}
			}
		}
	}

	class PopupMenuListener extends MouseAdapter { // Menu contextuel au clic droit
	//	@Override
/*<<<<<<< .mine
		public void mouseReleased(MouseEvent e) { // Menu contextuel au clic
			// droit
			if (e.getButton() == MouseEvent.BUTTON3) {
				MainFrame.this._rightClickMenu.show(e.getComponent(), e.getX(),
						e.getY());
			}
		}
=======*/
		@Override
		public void mousePressed(MouseEvent e) {
			if (e.isPopupTrigger()) {
	            _bibidulPopupMenu.show(e.getComponent(), e.getX(), e.getY());
	        }
	    }
//>>>>>>> .r23

		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger()) {
	            _bibidulPopupMenu.show(e.getComponent(), e.getX(), e.getY());
	        }
	    }
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

	/**
	 * Construit l'ensemble des listes de modules nécessaires (générale,
	 * clickable, droppable)
	 */
	public void updateClickableModules() {
		// Mise à jour des modclickable
		Iterator<BidibulModule> i = _listeModules.listIterator();
		BidibulModule tempMod;
		while (i.hasNext()) {
			tempMod = i.next();
			if (extends_iClickable(tempMod))
				_listeModulesClickable.add(tempMod);
		}

	}

	/**
	 * Renvoie true si la classe le module hérite de la classe iClickable
	 *
	 * @see BidibulModule
	 * @see ModuleLoader#loadModules()
	 * @todo Pourquoi ne pas utiliser instanceof à  la place ?
	 * @todo Mieux que "Class<?>" ?
	 */
	private boolean extends_iClickable(BidibulModule mod) {
		Class<?>[] interfaces = mod.getClass().getInterfaces();
		for (int i = 0; i < interfaces.length; i++) {
			System.out.println("interface :" + interfaces[i].getName());

			if (interfaces[i].getName() == "utils.iClickable")
				return true;
		}
		return false;
	}


	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	/**
	 * Surcharge la fermeture de la fenêtre pour la personnaliser
	 *
	 * @see RightClickMenu#exit()
	 */
	@Override
	public void windowClosing(WindowEvent arg0) {
		BidibulPopupMenu.exit();
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
	}
}