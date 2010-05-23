package views;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import models.Flash;
import tools.ModuleLoader;
import utils.BidibulModule;

//import com.sun.awt.AWTUtilities; // @todo kill me

/**
 * Frame principale du programme.
 * Appel�e par le main.
 *
 * @author J�r�mie ASTORI
 * @author Dominique CLAUSE
 */
public class MainFrame extends JFrame implements WindowListener {
	private static final long serialVersionUID = 1L;

	private BidibulPanel _bidibul;
	//private String[] listIconMenuClicSimple = {"img/mail.png", "img/facebook.png", "img/msn.png", "img/word.png", "img/zip.png", "img/play.png", "img/trash.png", "img/search.png"};
	private PieMenuPanel _pieMenuPanel = null;
	private BidibulPopupMenu _bibidulPopupMenu;
	private ArrayList<BidibulModule> _listeModules, _listeModulesClickable, _listeModulesDroppable;
	//private Window _backgroundWindow = new Window(this);

	/**
	 * CONSTRUCTEUR
	 */
	public MainFrame(ArrayList<BidibulModule> listModuleTemp) {
		_listeModules = new ArrayList<BidibulModule>(listModuleTemp);
		this.output();
		this.initialize();
		//---
		//Surcharge du constructeur. Lance la fonction onLoad des modules:
		//   cette fonction permet la surcharge �ventuelle du constructeur
		//   ex: un module qui change l'apparence du bidibul
		if(!_listeModules.isEmpty())
		{
			Iterator<BidibulModule> i = _listeModules.listIterator();
			while (i.hasNext()) {
				System.out.println("mod");
				i.next().onLoad();
			}
		}
	}

	/**
	 * @todo Constructeur install� par d�pit, rien d'autre ne marchant quand aucun module n'est pr�sent...
	 */
	public MainFrame() {
		this.output();
		this.initialize();
	}

	/**
	 * D�finition des param�tres de fen�trage
	 */
	public void output() {
		// Ne rien faire � la fermeture ([X] ou Alt-F4) car on veut une fermeture personnalis�e
	    this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

	    // Surcharge les op�rations de fen�trage, comme la fermeture de l'application par exemple
	    this.addWindowListener(this);

	    // Supprime la barre de titre et la bordure de la fen�tre
	    //this.setUndecorated(true); // @todo, en mode dev, super chiant le Undecorated...

		super.setVisible(true);

		// @todo Sous Linux, d�clenche : Exception in thread "main" java.lang.IllegalArgumentException: Window GraphicsConfiguration 'X11GraphicsConfig[dev=X11GraphicsDevice[screen=0],vis=0x21]' does not support transparency
		// at com.sun.jna.platform.WindowUtils$X11WindowUtils.setWindowTransparent(WindowUtils.java:1401)
		// at com.sun.jna.platform.WindowUtils.setWindowTransparent(WindowUtils.java:1542)
		// Mise en place de la transparence de la fen�tre
		//System.setProperty("sun.java2d.noddraw", "true");
		//WindowUtils.setWindowTransparent(this, true);

		this.pack();

		// AWTUtilities est...
		// @Deprecated
		//AWTUtilities.setWindowOpaque(this, false); 				//Rend la fen�tre transparente
		//System.out.println("opacity : " + AWTUtilities.getWindowOpacity(this));
	}

	/**
	 * Initialisation des composants et affichage
	 */
	public void initialize() {

		//Initialisation des listes:
		_listeModulesClickable = new ArrayList<BidibulModule>();
		_listeModulesDroppable = new ArrayList<BidibulModule>();
		// V�rif
		//VerifList("_listModules", _listeModules); // @todo JA a desactiv� pour cause de crash complet

		this.setSize(640, 480);
		this.setLayout(null);

		/**try {
	        System.setProperty("sun.java2d.noddraw", "true");
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    }
	    catch(Exception e) {
	    }
	    System.out.println("alpha ? " + WindowUtils.isWindowAlphaSupported());
		WindowUtils.setWindowAlpha(this, .5f);*/

	//--CREATION DU BIDIBUL
		// @todo S�parer la classe pour plus de modularit�
		_bidibul = new BidibulPanel(this);
		_bidibul.setBounds(200, 200, 300, 300);
		_bidibul.addMouseListener(new actionOnClic());
		this.add(_bidibul);
	//-- Fin cr�ation bidibul

		// NotificationPanel
		FlashPanel panFlash = new FlashPanel(Flash.getInstance());
		panFlash.setPreferredSize(this.getMaximumSize());
		panFlash.setBounds(200, 50, 311, 133);
		this.add(panFlash);

		// PieMenuPanel
		_pieMenuPanel = new PieMenuPanel (MainFrame.this.getContentPane(), _bidibul.getX()+ _bidibul.getWidth()/2, _bidibul.getY()+ _bidibul.getHeight()/2 );


		//Analyse des listes (clickable)
		//MiseAJourListeModules(true, null); // @todo JA a desactiv� pour cause de crash complet
		//VerifList("_listModulesClickable", _listeModulesClickable); // @todo JA a desactiv� pour cause de crash complet

		// Cr�ation du menu contextuel
		this._bibidulPopupMenu = new BidibulPopupMenu(this);

		// Ajout du listener de menu contextuel
	    this._bidibul.addMouseListener(new PopupMenuListener());

	    // new ModuleManagerFrame(); // @todo DEV J�r�mie ASTORI


	}

	public class actionOnClic extends MouseAdapter {
		@Override
		public void mouseReleased(MouseEvent e) {
			// Clic gauche
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (_pieMenuPanel.getIconVisible() == false) {
					_pieMenuPanel.refresh(_listeModulesClickable);
					_pieMenuPanel.setIconVisible(true);				//Affiche le PieMenu
					MainFrame.this.getContentPane().update(MainFrame.this.getContentPane().getGraphics());
					System.out.println("click show!");
				}
				else {
					_pieMenuPanel.setIconVisible(false);			//Cache le PieMenu
					MainFrame.this.getContentPane().update(MainFrame.this.getContentPane().getGraphics());
					System.out.println("click hide!");
				}
			}
		}
	}

	class PopupMenuListener extends MouseAdapter { // Menu contextuel au clic droit
		@Override
		public void mousePressed(MouseEvent e) {
			if (e.isPopupTrigger()) {
	            _bibidulPopupMenu.show(e.getComponent(), e.getX(), e.getY());
	        }
	    }

		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger()) {
	            _bibidulPopupMenu.show(e.getComponent(), e.getX(), e.getY());
	        }
	    }
	}

	/**
	 * Fonction destin�e � dispara�tre: permet de v�rifier la validit� des
	 * construction de List pour les modules
	 * @author Miro
	 * @return
	 */
	void VerifList(String nomListe, ArrayList<BidibulModule> maListTemp) {
		System.out.println("v�rification de la liste '" + nomListe + "' : ");
		if(!maListTemp.isEmpty())
		{
			Iterator<BidibulModule> i = maListTemp.listIterator();
			while (i.hasNext()) {
				System.out.println("     - " + i.next().getName());
			}
		}
	}

	/**
	 * Construit l'ensemble des listes de modules n�cessaires (g�n�rale, clickable, droppable)
	 * @param miseAJourClickable un boolean repr�sentant si la liste des modules clickable doit �tre mise � jour
	 * @param tabDraggableExtension tableau des extensions � anays� pour la mise � jour des modules droppable.
	 * 								Si ce param�tre est nul, la liste des modules droppable n'est pas mise � jour
	 */
	public void MiseAJourListeModules(boolean miseAjourClickable, String tabDroppableExtension[]) {
		//Mise � jour des modclickable
		if (miseAjourClickable) {
			Iterator<BidibulModule> i = _listeModules.listIterator();
			BidibulModule tempMod;
			while (i.hasNext()){
				tempMod = i.next();
				if (extends_iClickable(tempMod))
					_listeModulesClickable.add(tempMod);
			}
		}
	}


	/**
	 * Renvoie true si la classe le module h�rite de la classe iClickable
	 * @see BidibulModule
	 * @see ModuleLoader#loadModules()
	 * @todo Pourquoi ne pas utiliser instanceof � la place ?
	 * @todo Mieux que "Class<?>" ?
	 */
	private boolean extends_iClickable(BidibulModule mod){
		Class<?>[] interfaces = mod.getClass().getInterfaces();
		for (int i=0; i<interfaces.length;i++){
			System.out.println("interface :" + interfaces[i].getName());

			if (interfaces[i].getName() == "utils.iClickable")
				return true;
		}
		return false;
	}

	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {}

	/**
	 * Surcharge la fermeture de la fen�tre pour la personnaliser
	 * @see RightClickMenu#exit()
	 */
	@Override
	public void windowClosing(WindowEvent arg0) {
		BidibulPopupMenu.exit();
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0) {}
}
