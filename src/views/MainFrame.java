package views;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

/**
 * Frame principale du programme.
 * Appel�e par le main.
 *
 * @author J�r�mie ASTORI
 * @author Dominique CLAUSE
 */
public class MainFrame extends TranslucentFrame implements WindowListener {
	private static final long serialVersionUID = 1L;

	private BidibulPanel _bidibul;
	private PieMenuPanel _pieMenuPanel = null;
	private BidibulPopupMenu _bibidulPopupMenu;
	private ArrayList<BidibulModule> _listeModules, _listeModulesClickable;
	private SystemTray _tray = null;
	private TrayIcon _trayIcon = null;

	/**
	 * CONSTRUCTEUR
	 */
	public MainFrame(ArrayList<BidibulModule> listModuleTemp) {
		_listeModules = new ArrayList<BidibulModule>(listModuleTemp);
		initialize();
		output();
		//initSystray(); // @todo A r�activer __PLUS TARD__, quand tout marchera � parfaitement
		// ---
		// Surcharge du constructeur. Lance la fonction onLoad des modules:
		// cette fonction permet la surcharge �ventuelle du constructeur
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
	 * @todo Constructeur install� par d�pit, rien d'autre ne marchant quand aucun module n'est pr�sent...
	 */
	public MainFrame() {
		this.initialize();
		this.output();
//		this.initSystray();
	}

	/**
	 * D�finition des param�tres de fen�trage
	 */
	public void output() {
		// Ne rien faire � la fermeture ([X] ou Alt-F4) car on veut une fermeture personnalis�e
	    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

	    // Surcharge les op�rations de fen�trage, comme la fermeture de l'application par exemple
	    addWindowListener(this);

	    setVisible(true);
	}

	/**
	 * Initialisation des composants et affichage
	 */
	public void initialize() {
		// Initialisation des listes:
		_listeModulesClickable = new ArrayList<BidibulModule>();

		// V�rif
		VerifList("_listModules", _listeModules); // @todo JA a desactiv� pour cause de crash complet

		setLocation(100, 100);
		setSize(331, 440);
		setLayout(null);

		// --CREATION DU BIDIBUL
		_bidibul = new BidibulPanel(this);
		_bidibul.setBounds(
				getWidth()/2 - _bidibul.getWidth()/2,
				160,
				_bidibul.getWidth(),
				_bidibul.getHeight());
		_bidibul.addMouseListener(new actionOnClic());
		// -- Fin cr�ation bidibul

		// NotificationPanel
		FlashPanel panFlash = new FlashPanel(Flash.getInstance());
		panFlash.setPreferredSize(this.getMaximumSize());
		panFlash.setBounds(10, 10, 311, 133);
		Flash.error("Qu'est-ce que tu veux, morveux ?");
		this.add(panFlash);

		// PieMenuPanel
		_pieMenuPanel = new PieMenuPanel (this, _bidibul.getX()+ _bidibul.getWidth()/2, _bidibul.getY()+ _bidibul.getHeight()/2 );
		_pieMenuPanel.setBounds(0, 0 , 400, 400);
		this.add(_pieMenuPanel);


		this.add(_bidibul);

		// Analyse des listes (clickable)
		updateClickableModules();
		VerifList("_listModulesClickable", _listeModulesClickable);

		// Cr�ation du menu contextuel
		this._bibidulPopupMenu = new BidibulPopupMenu(this);

		// Ajout du listener de menu contextuel
	    _bidibul.addMouseListener(new PopupMenuListener());

		this._bidibul.setTransferHandler(new MyFileTransferHandler(
				_pieMenuPanel, _listeModules));

	    // new ModuleManagerFrame(); // @todo DEV J�r�mie ASTORI
	}

	public class actionOnClic extends MouseAdapter {
		@Override
		public void mouseReleased(MouseEvent e) {
			// Clic gauche
			if (e.getButton() == MouseEvent.BUTTON1) {
				//if (_pieMenuPanel.getIconVisible() == false) {
				if (!_pieMenuPanel.isVisible()) {
					_pieMenuPanel.refresh(_listeModulesClickable, 1);
				//	_pieMenuPanel.setIconVisible(true);				//Affiche le PieMenu
					_pieMenuPanel.setVisible(true);				//Affiche le PieMenu
				//	MainFrame.this.update(MainFrame.this.getGraphics());
					//MainFrame.this.repaint();
					System.out.println("click show!");
				}
				else {
					//_pieMenuPanel.setIconVisible(false);			//Cache le PieMenu
					_pieMenuPanel.setVisible(false);			//Cache le PieMenu
				//	MainFrame.this.update(MainFrame.this.getGraphics());
					System.out.println("click hide!");
				}
			}
		}
	}

	class PopupMenuListener extends MouseAdapter { // Menu contextuel au clic droit
		@Override
		public void mousePressed(MouseEvent e) {
			if (e.isPopupTrigger())
	            _bibidulPopupMenu.show(e.getComponent(), e.getX(), e.getY());
	    }

		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger())
	            _bibidulPopupMenu.show(e.getComponent(), e.getX(), e.getY());
	    }
	}

	/**
	 * Fonction destin�e � dispara�tre: permet de v�rifier la validit� des
	 * construction de List pour les modules
	 *
	 * @author Miro
	 * @return
	 */
	void VerifList(String nomListe, ArrayList<BidibulModule> maListTemp) {
		System.out.println("v�rification de la liste '" + nomListe + "' : ");
		if (!maListTemp.isEmpty()) {
			Iterator<BidibulModule> i = maListTemp.listIterator();
			while (i.hasNext()) {
				System.out.println("     - " + BidibulInformation.get("name", i.next())/*i.next().getName()*/);
			}
		}
	}

	/**
	 * Construit l'ensemble des listes de modules n�cessaires (g�n�rale,
	 * clickable, droppable)
	 */
	public void updateClickableModules() {
		// Mise � jour des modclickable
		Iterator<BidibulModule> i = _listeModules.listIterator();
		BidibulModule tempMod;
		while (i.hasNext()) {
			tempMod = i.next();
			if (extends_iClickable(tempMod))
				_listeModulesClickable.add(tempMod);
		}

	}

	/**
	 * Renvoie true si la classe le module h�rite de la classe iClickable
	 *
	 * @see BidibulModule
	 * @see ModuleLoader#loadModules()
	 * @todo Pourquoi ne pas utiliser instanceof � la place ?
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


	public void initSystray() {
	     if (SystemTray.isSupported()) {
	         //Cr�e une instance de systray
	         _tray = SystemTray.getSystemTray();
	         // Charger l'image du bidibul dans le systray
	         Image image = Toolkit.getDefaultToolkit().getImage("img/bidibul_tray.png");

	         //R�ctive le bidibul
	         ActionListener listener = new ActionListener() {
	             public void actionPerformed(ActionEvent e) {
	                 setVisible(true);
	                 _tray.remove(_trayIcon);
	             }
	         };
	         // Quitte l'application
	         ActionListener quit = new ActionListener() {
	             public void actionPerformed(ActionEvent e) {
	                 setVisible(true);
	                 _tray.remove(_trayIcon);
	                 BidibulPopupMenu.exit();
	             }
	         };
	         // Cr�er un popup menu sur le l'icone du Systray:
	         PopupMenu popup = new PopupMenu();
	         // Cr�er un menu:
	         MenuItem quitItem = new MenuItem("Fermer le bidibul!");
	         MenuItem showItem = new MenuItem("Montrer le Bidibul!");
	         quitItem.addActionListener(quit);
	         showItem.addActionListener(listener);
	         popup.add(showItem);
	         popup.addSeparator();
	         popup.add(quitItem);

	         _trayIcon = new TrayIcon(image, "Double-clic pour faire appara�tre le bidibul!", popup);
	         _trayIcon.setImageAutoSize(true);
	          //Double-clic sur l'icone: montre le bidibul
	         _trayIcon.addActionListener(listener);

	     } else {
	    	 System.out.println("systray refus�");
	     }
	}

	public SystemTray getSystemTray() {
		return this._tray;
	}

	public TrayIcon getTrayIcon() {
		return this._trayIcon;
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	/**
	 * Surcharge la fermeture de la fen�tre pour la personnaliser
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