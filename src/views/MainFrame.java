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

import javax.swing.WindowConstants;

import models.Flash;
import tools.MyFileTransferHandler;
import tools.TranslucentFrame;

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
	private SystemTray _tray = null;
	private TrayIcon _trayIcon = null;
	static private int _CLICKABLE = 1;

	/**
	 * Constructeur de MainFrame
	 */
	public MainFrame() {
		this.initialize();
		this.output();
		this.initSystray();
	}

	/**
	 * Définition des paramètres de fenêtrage
	 */
	public void output() {
		// Ne rien faire à la fermeture ([X] ou Alt-F4) car on veut une fermeture personnalisée
	    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

	    // Surcharge les opérations de fenêtrage, comme la fermeture de l'application par exemple
	    addWindowListener(this);

	    setVisible(true);
	}

	/**
	 * Initialisation des composants et affichage
	 */
	public void initialize() {
		// Initialisation des listes:

		setLocation(100, 100);
		setSize(500, 500);
		setLayout(null);

		// --CREATION DU BIDIBUL
		_bidibul = new BidibulPanel(this);
		_bidibul.setBounds(
				getWidth()/2 - _bidibul.getWidth()/2,
				160,
				_bidibul.getWidth(),
				_bidibul.getHeight());
		_bidibul.addMouseListener(new actionOnClic());
		// -- Fin création bidibul

		// NotificationPanel
		FlashPanel panFlash = new FlashPanel(Flash.getInstance());
		panFlash.setPreferredSize(this.getMaximumSize());
		panFlash.setBounds(10, 10, 311, 133);
		Flash.error("Qu'est-ce que tu veux, morveux ?");
		this.add(panFlash);

		// PieMenuPanel
		_pieMenuPanel = new PieMenuPanel (_bidibul.getX()+ _bidibul.getWidth()/2, _bidibul.getY()+ _bidibul.getHeight()/2 );
		_pieMenuPanel.setBounds(0, 0 , 500, 500);
		this.add(_pieMenuPanel);


		this.add(_bidibul);

		// Création du menu contextuel
		this._bibidulPopupMenu = new BidibulPopupMenu(this);

		// Ajout du listener de menu contextuel
	    _bidibul.addMouseListener(new PopupMenuListener());

		this._bidibul.setTransferHandler(new MyFileTransferHandler(
				_pieMenuPanel));

	    // new ModuleManagerFrame(); // @todo DEV Jérémie ASTORI
	}

	public class actionOnClic extends MouseAdapter {
		@Override
		public void mouseReleased(MouseEvent e) {
			// Clic gauche
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (!_pieMenuPanel.isVisible()) {
					_pieMenuPanel.refresh(_CLICKABLE);
					_pieMenuPanel.setVisible(true);				//Affiche le PieMenu
					System.out.println("click show!");
				}
				else {
					_pieMenuPanel.setVisible(false);			//Cache le PieMenu
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


	public void initSystray() {
	     if (SystemTray.isSupported()) {
	         //Crée une instance de systray
	         _tray = SystemTray.getSystemTray();
	         // Charger l'image du bidibul dans le systray
	         Image image = Toolkit.getDefaultToolkit().getImage("img/bidibul_tray.png");

	         //Réctive le bidibul
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
	         // Créer un popup menu sur le l'icone du Systray:
	         PopupMenu popup = new PopupMenu();
	         // Créer un menu:
	         MenuItem quitItem = new MenuItem("Fermer le bidibul!");
	         MenuItem showItem = new MenuItem("Montrer le Bidibul!");
	         quitItem.addActionListener(quit);
	         showItem.addActionListener(listener);
	         popup.add(showItem);
	         popup.addSeparator();
	         popup.add(quitItem);

	         _trayIcon = new TrayIcon(image, "Double-clic pour faire apparaître le bidibul!", popup);
	         _trayIcon.setImageAutoSize(true);
	          //Double-clic sur l'icone: montre le bidibul
	         _trayIcon.addActionListener(listener);

	     } else {
	    	 System.out.println("systray refusé");
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