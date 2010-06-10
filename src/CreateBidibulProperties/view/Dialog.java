package CreateBidibulProperties.view;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JTextArea;

public class Dialog extends JDialog{
	private static final long serialVersionUID = 1L;

	private JTextArea txt;

	public Dialog() {
		this.setSize(400, 150);
		txt = new JTextArea();
		txt.setSize(new Dimension(400, 150));
		txt.setEnabled(false); // Le texte n'est pas modifiable
		txt.setOpaque(false); // Fond transparent
		txt.setLineWrap(true); // Retour chariot auto
		txt.setWrapStyleWord(true); // Le retour chariot ne coupe pas les mots
		txt.setDisabledTextColor(txt.getForeground()); // Couleur du JTextArea en mode édition désactivée
		txt.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.add(txt);
	}

	public void setText(String s){
		txt.setText(s);
	}

}
