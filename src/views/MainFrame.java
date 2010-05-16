package views;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import models.Flash;

//import com.sun.awt.AWTUtilities;

/**
 * Frame principale du programme.
 * AppelÃ©e par le main.
 *
 * @author Jérémie ASTORI
 * @author Dominique CLAUSE
 */
public class MainFrame extends JFrame implements WindowListener {
	private static final long serialVersionUID = 1L;

	private JLabel _bidibul;
	private String[] listIconMenuClicSimple = {"img/mail.png", "img/facebook.png", "img/msn.png", "img/word.png", "img/zip.png", "img/play.png", "img/trash.png", "img/search.png"};
	private PieMenuPanel _pieMenuPanel = null;
	private RightClickMenu _rightClickMenu;

	/**
	 * CONSTRUCTEUR
	 */
	public MainFrame() {
		this.output();
		this.initialize();
	}

	/**
	 * Définition des paramètres de fenêtrage
	 */
	public void output() {
		// Ne rien faire à la fermeture ([X] ou Alt-F4) car on veut une fermeture personnalisée
	    this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

	    // Surcharge les opérations de fenêtrage, comme la fermeture de l'application par exemple
	    this.addWindowListener(this);

	    // Supprime la barre de titre et la bordure de la fenêtre
	    this.setUndecorated(true);

	    this.setVisible(true);
		this.pack();

		// AWTUtilities est...
		// @Deprecated
		//AWTUtilities.setWindowOpaque(this, false); 				//Rend la fenêtre transparente
		//System.out.println("opacity : " + AWTUtilities.getWindowOpacity(this));

	}

	/**
	 * Initialisation des composants et affichage
	 */
	public void initialize() {
		this.setSize(640, 480);
		this.setLayout(null);

	//--CREATION DU BIDIBUL
		// @todo Séparer la classe pour plus de modularité
		_bidibul = new JLabel();
		_bidibul.setIcon(new ImageIcon("img/bidibul.png"));
		_bidibul.setBounds(300, 250, 100, 100);
		_bidibul.addMouseListener(new actionOnClic());
		_bidibul.setOpaque(false);
		this.add(_bidibul);
	//-- Fin création bidibul

		// NotificationPanel
		FlashPanel panFlash = new FlashPanel(Flash.getInstance());
		panFlash.setPreferredSize(this.getMaximumSize());
		panFlash.setBounds(200, 100, 311, 133);
		this.add(panFlash);

		// PieMenuPanel
		_pieMenuPanel = new PieMenuPanel (MainFrame.this.getContentPane() , listIconMenuClicSimple, _bidibul.getX()+ _bidibul.getWidth()/2, _bidibul.getY()+ _bidibul.getHeight()/2 );
		/**
		 * @todo Remplir le String[] listIconMenuClicSimple
		 * @todo Implémenter la création d'une String[] pour le cas iDroppable
		 */

		// Création du menu contextuel
		this._rightClickMenu = new RightClickMenu(this);

		// Ajout du listener de menu contextuel
	    this._bidibul.addMouseListener(new RightClickListener());
	}

	public class actionOnClic extends MouseAdapter {
			@Override
			public void mouseReleased(MouseEvent e) {
				// Clic gauche
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (_pieMenuPanel.getIconVisible() == false) {
						_pieMenuPanel.setIconVisible(true);				//Affiche le PieMenu
						MainFrame.this.getContentPane().update(MainFrame.this.getContentPane().getGraphics());
						//System.out.println("click show!");
					}
					else {
						_pieMenuPanel.setIconVisible(false);			//Cache le PieMenu
						MainFrame.this.getContentPane().update(MainFrame.this.getContentPane().getGraphics());
						//System.out.println("click hide!");
					}
				}
			}
		}

	class RightClickListener extends MouseAdapter {
		@Override
		public void mouseReleased(MouseEvent e) { // Menu contextuel au clic droit
			if (e.getButton() == MouseEvent.BUTTON3) {
	            MainFrame.this._rightClickMenu.show(e.getComponent(), e.getX(), e.getY());
	        }
	    }
	}

	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {}

	/**
	 * Surcharge la fermeture de la fenêtre pour la personnaliser
	 * @see RightClickMenu#exit()
	 */
	@Override
	public void windowClosing(WindowEvent arg0) {
		RightClickMenu.exit();
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
