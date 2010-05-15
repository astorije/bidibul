package views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import models.Flash;

import com.sun.awt.AWTUtilities;

/**
 * Frame principale du programme.
 * Appel�e par le main.
 *
 * @author J�r�mie ASTORI
 * @author Dominique CLAUSE
 */
public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private JLabel _bidibul;							//!< le bidibul
	private String[] listIconMenuClicSimple = {"img/mail.png", "img/facebook.png", "img/msn.png", "img/word.png", "img/zip.png", "img/play.png", "img/trash.png", "img/search.png"};
														//!< Liste des icones au clic
	private PieMenuPanel _pieMenuPanel = null; 			//<! le PieMenu

	/**
	 * CONSTRUCTEUR
	 */
	public MainFrame() {
		this.setUndecorated(true);	//rend la fen�tre non d�cor�e
		output();					//d�finition des param�tres de fermeture
		initialize();				//Initialisation et affichage
	}

	/**
	 * D�finition des param�tres de fermture
	 */
	public void output() {
		this.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}

	/**
	 * Initialisation des composants et affichage
	 */
	public void initialize() {

		this.setLayout(null);							//D�finition du Layout

		AWTUtilities.setWindowOpaque(this, false); 				//Rend la fen�tre transparente
		System.out.println("opacity : " + AWTUtilities.getWindowOpacity(this));

		JButton fermer = new JButton("Fermer");					//Cr�ation d'un bouton fermeture
		fermer.setBounds(20, 60, 70, 20);						//Positionnement du bouton
		fermer.addActionListener(new ActionListener() {			//Sur clic sur le bouton...
			@Override
            public void actionPerformed(ActionEvent event) {		//...fermeture de l'application
                System.exit(0);
            }
        });
		this.add(fermer);										//Ajout du bouton � la fen�tre

	//--CREATION DU BIDIBUL
		_bidibul = new JLabel();	//Cr�ation du bidibul
		_bidibul.setIcon(new ImageIcon("img/bidibul.png"));
		_bidibul.setBounds(300, 250, 100, 100);						//Positionnement
		_bidibul.addMouseListener(new actionOnClic());
		_bidibul.setOpaque(false);
		this.add(_bidibul);

	//-- Fin cr�ation bidibul

	// PieMenuPanel
		// NotificationPanel
		NotificationPanel panNotification = new NotificationPanel(Flash.getInstance());
		panNotification.setPreferredSize(getMaximumSize());
		panNotification.setBounds(200, 100, 311, 133);
		panNotification.setOpaque(false);						//Cache le background de la bulle de notification
		this.add(panNotification);

		this.setSize(640, 480);									//Taille de la fen�tre

		_pieMenuPanel = new PieMenuPanel (MainFrame.this.getContentPane() , listIconMenuClicSimple, _bidibul.getX()+ _bidibul.getWidth()/2, _bidibul.getY()+ _bidibul.getHeight()/2 );
		/**
		 * @todo Remplir le String[] listIconMenuClicSimple
		 * @todo Impl�menter la cr�ation d'une String[] pour le cas iDroppable
		 */

	}

	public class actionOnClic extends MouseAdapter {
			@Override
			public void mouseReleased(MouseEvent e)						//sur clic de la souris
			{
				/* -- clic gauche -- */
				if (e.getButton() == MouseEvent.BUTTON1) 				//Si clic gauche
				{

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
				/* -- clic droit -- */
				if (e.getButton() == MouseEvent.BUTTON2)
				{
					/**
					 * @todo impl�menter le menu contextuel
					 */
				}
			}
		}
}
