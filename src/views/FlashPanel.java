package views;

import java.awt.Font;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import models.AbstractModel;
import models.Flash;
import models.FlashMessage;

/**
 * Panel des messages d'information et d'erreur
 * @author JÃ©rÃ©mie ASTORI
 */
public class FlashPanel extends JPanel implements Observer {
	private static final long serialVersionUID = 1L;
	private final AbstractModel _model;
	private final JTextArea _txtaMessage;

	private JLabel _backgroundImage;

	public static final int BUBBLE_PADDING = 10; // pixels
	public static final ImageIcon NOTICE_BUBBLE_IMG = new ImageIcon("img/speech_bubble_notice.png");//.getImage();
	public static final ImageIcon ERROR_BUBBLE_IMG = new ImageIcon("img/speech_bubble_error.png");//.getImage();

	public FlashPanel(AbstractModel model) {
		this._model = model;
		model.addObserver(this);

		this.setVisible(true);
		this.setOpaque(false); //Cache le background de la bulle de notification

		this.setLayout(null);

		this._txtaMessage = new JTextArea();
		this._txtaMessage.setEnabled(false); // Le texte n'est pas modifiable
		this._txtaMessage.setLineWrap(true); // Retour chariot auto
		this._txtaMessage.setWrapStyleWord(true); // Le retour chariot ne coupe pas les mots
		this._txtaMessage.setOpaque(false); // Fond transparent
		Font f = this._txtaMessage.getFont();
		this._txtaMessage.setFont(new Font(f.getFontName(), f.getStyle(), f.getSize()+3)); // Police et style du message
		this._txtaMessage.setDisabledTextColor(this._txtaMessage.getForeground()); // Couleur du JTextArea en mode édition désactivée
		this._txtaMessage.setBounds( // Position du JTextArea
			1 + BUBBLE_PADDING,
			BUBBLE_PADDING,
			300 - BUBBLE_PADDING*2,
			100 - BUBBLE_PADDING*2
		);
		this.add(this._txtaMessage);

		this._backgroundImage = new JLabel();
		this._backgroundImage.setBounds(0, 0, 311, 133);
		this.add(this._backgroundImage);
	}

	public void displayError(String msg) {
		this._backgroundImage.setIcon(ERROR_BUBBLE_IMG);
		this._txtaMessage.setText(msg);
	}

	public void displayNotice(String msg) {
		this._backgroundImage.setIcon(NOTICE_BUBBLE_IMG);
		this._txtaMessage.setText(msg);
	}

	@Override
	public void update(Observable o, Object arg) {
		FlashMessage message = ((Flash)this._model).getLastFlashMessage();
		if(message.getType() == "error")
			this.displayError(message.getMessage());
		else if(message.getType() == "notice")
			this.displayNotice(message.getMessage());
	}
}
