package views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import tools.BidibulInformation;
import utils.BidibulModule;

public class AboutModuleDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private String _website;

	public AboutModuleDialog(Class<BidibulModule> module) {
		super();

		// @todo Récupérer les vraies valeurs...
		String name = BidibulInformation.getName(module);
		String description = BidibulInformation.getDescription(module);
		String author = BidibulInformation.getAuthor(module);
		_website = BidibulInformation.getWebsite(module);

		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		// Titre du module
		JLabel lbl_title = new JLabel(name);
		lbl_title.setAlignmentX(CENTER_ALIGNMENT);
		lbl_title.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		lbl_title.setFont(new Font(
				lbl_title.getFont().getName(),
				Font.BOLD,
				lbl_title.getFont().getSize()+5
		));

		// Description du module
		JTextArea txta_description = new JTextArea();
		txta_description.setText(description);

		txta_description.setSize(new Dimension(400, 50));
		txta_description.setEnabled(false); // Le texte n'est pas modifiable
		txta_description.setOpaque(false); // Fond transparent
		txta_description.setLineWrap(true); // Retour chariot auto
		txta_description.setWrapStyleWord(true); // Le retour chariot ne coupe pas les mots
		txta_description.setDisabledTextColor(txta_description.getForeground()); // Couleur du JTextArea en mode édition désactivée
		//txta_description.setAlignmentX(CENTER_ALIGNMENT); // NE MARCHE PAS
		txta_description.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		// Auteur du module
		JLabel lbl_author = new JLabel("Copyright @ "+author);
		lbl_author.setAlignmentX(CENTER_ALIGNMENT);
		lbl_author.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		lbl_author.setFont(new Font(
				lbl_author.getFont().getName(),
				Font.PLAIN,
				lbl_author.getFont().getSize()-2
		));

		// Site web relatif au module
		JLabel lbl_website = new JLabel();
		if(_website != null) {
			lbl_website.setText(_website);
			lbl_website.setAlignmentX(Component.CENTER_ALIGNMENT);
			lbl_website.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			lbl_website.setForeground(Color.BLUE);
			lbl_website.setFont(new Font(
					lbl_website.getFont().getName(),
					Font.PLAIN,
					lbl_website.getFont().getSize()
			));
			lbl_website.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			lbl_website.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					if(SwingUtilities.isLeftMouseButton(arg0))
						if (Desktop.isDesktopSupported())
							try {
								Desktop.getDesktop().browse(URI.create(_website));
							} catch (IOException e) {
								e.printStackTrace();
							}
				}
			});
		}

		// Bouton "Fermer"
		JButton btn_close = new JButton("Fermer");
		btn_close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				setVisible(false);
			}
		});
		JPanel pan_close = new JPanel();
		pan_close.setLayout(new BoxLayout(pan_close, BoxLayout.X_AXIS));
		pan_close.add(Box.createHorizontalGlue());
		pan_close.add(btn_close);
		pan_close.setBorder(new EmptyBorder(5, 5, 5, 5));

		// Positionnement des éléments
		add(lbl_title);
		add(txta_description);
		add(lbl_author);

		add(lbl_website);

		add(pan_close);

		setTitle("A propos du module \""+name+"\"");
		setResizable(false);
		setVisible(true);
		pack();
	}
}
