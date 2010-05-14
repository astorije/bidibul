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

/**
 * Frame principale du programme.
 * Appelée par le main.
 *
 * @author Jérémie ASTORI
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
		this.setUndecorated(true);	//rend la fenêtre non décorée
		output();					//définition des paramètres de fermeture
		initialize();				//Initialisation et affichage
	}

	/**
	 * Définition des paramètres de fermture
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

		this.setLayout(null);									//Définition du Layout

		JButton fermer = new JButton("Fermer");					//Création d'un bouton fermeture
		fermer.setBounds(20, 60, 70, 20);						//Positionnement du bouton
		fermer.addActionListener(new ActionListener() {			//Sur clic sur le bouton...
			@Override
            public void actionPerformed(ActionEvent event) {		//...fermeture de l'application
                System.exit(0);
            }
        });
		this.add(fermer);										//Ajout du bouton à la fenêtre

	//--CREATION DU BIDIBUL
		_bidibul = new JLabel(new ImageIcon("img/bidibul.png"));	//Création du bidibul
		_bidibul.setBounds(300, 250, 100, 100);						//Positionnement
		_bidibul.addMouseListener(new actionOnClic());
		this.add(_bidibul);
	//-- Fin création bidibul

		// NotificationPanel
		NotificationPanel panNotification = new NotificationPanel(Flash.getInstance());
		panNotification.setPreferredSize(getMaximumSize());
		panNotification.setBounds(200, 100, 311, 133);
		this.add(panNotification);

		this.setSize(640, 480);									//Taille de la fenêtre

		/**
		 * @todo Remplir le String[] listIconMenuClicSimple
		 * @todo Implémenter la création d'une String[] pour le cas iDroppable
		 */

	}

	public class actionOnClic extends MouseAdapter {
			@Override
			public void mouseReleased(MouseEvent e)						//sur clic de la souris
			{
				/* -- clic gauche -- */
				if (e.getButton() == MouseEvent.BUTTON1) 				//Si clic gauche
				{
					if (_pieMenuPanel == null)							//Ouverture d'un PieMenu
						_pieMenuPanel = new PieMenuPanel (MainFrame.this.getContentPane() , listIconMenuClicSimple, _bidibul.getX()+ _bidibul.getWidth()/2, _bidibul.getY()+ _bidibul.getHeight()/2 );
					else
					{
						_pieMenuPanel.fermerMenu();						//Fermeture du PieMenu
						e.getComponent().getParent().remove(_pieMenuPanel);
						_pieMenuPanel = null;							//Annulation
					}
				}
				/* -- clic droit -- */
				if (e.getButton() == MouseEvent.BUTTON2)
				{
					/**
					 * @todo implémenter le menu contextuel
					 */
				}
			}
		}
}
