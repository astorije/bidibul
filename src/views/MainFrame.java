package views;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.WindowConstants;

import models.Flash;
import tools.BidibulProperties;
import tools.MyFileTransferHandler;
import tools.TranslucentFrame;

/**
 * Frame principale du programme.
 * Appelée par le main.
 *
 * @author Jérémie ASTORI
 * @author Dominique CLAUSE
 */
public class MainFrame extends TranslucentFrame implements WindowListener, MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;

	private BidibulPanel _bidibul;
	private FlashPanel _panFlash;
	private PieMenuPanel _pieMenuPanel = null;
	private BidibulPopupMenu _bibidulPopupMenu;
	private SystemTray _tray = null;
	private TrayIcon _trayIcon = null;

	private boolean moveableFrame = false;
	private Point mMouseClickPoint = null;
	static private int _CLICKABLE = 1;

	public MainFrame() {
		initialize();
		output();
		initSystray();
	}

	/**
	 * Définition des paramètres de fenêtrage
	 */
	public void output() {
		setTitle("Bidibul");

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

		// Chargement de la position initiale
		BidibulProperties p = new BidibulProperties("global");
		setLocation(
			p.getPosX(),
			p.getPosY()
		);
		if(p.isAlwaysOnTop())
			setAlwaysOnTop(true);
		else
			setAlwaysOnTop(false);

		setSize(350, 450);
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
		_panFlash = new FlashPanel(Flash.getInstance(), _bidibul);
		_panFlash.setPreferredSize(this.getMaximumSize());
		_panFlash.setBounds(
				getWidth()/2 - _panFlash.getWidth()/2,
				10,
				_panFlash.getWidth(),
				_panFlash.getHeight());
		Flash.notice("Clique ou déplace un fichier sur moi !");
		add(_panFlash);

		_bidibul.addMouseListener(this);
		_panFlash.addMouseListener(this);

		add(_bidibul);

		// PieMenuPanel
		_pieMenuPanel = new PieMenuPanel (
				_bidibul.getX() + _bidibul.getWidth()/2,
				_bidibul.getY() + _bidibul.getHeight()/2
		);
		_pieMenuPanel.setBounds(0, 0 , 350, 450);
		add(_pieMenuPanel);

		// Création du menu contextuel
		_bibidulPopupMenu = new BidibulPopupMenu(this);

		// Ajout du listener de menu contextuel
	    _bidibul.addMouseListener(new PopupMenuListener());

		this._bidibul.setTransferHandler(new MyFileTransferHandler(
				_pieMenuPanel));
	}

	public class actionOnClic extends MouseAdapter {
		@Override
		public void mouseReleased(MouseEvent e) {
			// Clic gauche
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (moveableFrame){
					_bidibul.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}
				else if (!_pieMenuPanel.isVisible()) {
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

		@Override
		public void mousePressed(MouseEvent e) {
			if (moveableFrame && e.getButton() == MouseEvent.BUTTON1){
				mMouseClickPoint = e.getPoint();
				_bidibul.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			} else if (moveableFrame && e.getButton() == MouseEvent.BUTTON2){
				mMouseClickPoint = e.getPoint();
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

	         //Réactive le bidibul
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
	         MenuItem quitItem = new MenuItem("Quitter Bidibul");
	         MenuItem showItem = new MenuItem("Montrer Bidibul");
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
	    	 Flash.error("Je ne peux pas me cacher pour le moment.");
	     }
	}

	public SystemTray getSystemTray() {
		return this._tray;
	}

	public TrayIcon getTrayIcon() {
		return this._trayIcon;
	}

	private void _moveWindowTo(Point point) {
		int musDiffX = point.x - mMouseClickPoint.x;
		int musDiffY = point.y - mMouseClickPoint.y;

		setLocation( getLocation().x + musDiffX, getLocation().y + musDiffY );
		this.setLocation( getLocation() );
	}

	public void toggleMoveableFrame(){
		if (moveableFrame){
			moveableFrame = false;
			_bidibul.removeMouseMotionListener(this);
			Flash.rollback();
		}
		else {
			moveableFrame = true;
			_bidibul.addMouseMotionListener(this);
			Flash.notice("Déplace moi sur l'écran !");
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {}

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
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0) {}

	@Override
	public void mouseClicked(MouseEvent e) {}

	/**
	 * Lorsque la souris sort de l'écran, le FlashPanel doit se cacher.
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		_panFlash.onMouseExit();
	}

	/**
	 * Lorsque la souris sort de l'écran, le FlashPanel doit s'afficher.
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		_panFlash.onMouseEnter();
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	// Fonctions relatives au déplacement du bidibul à l'écran
	@Override
	public void mouseDragged(MouseEvent e) {
		if ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK){
			_moveWindowTo(e.getPoint());
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {}
}