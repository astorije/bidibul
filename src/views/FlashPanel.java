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
	private AbstractModel _model;
	private JTextArea _txtaMessage;

	private JLabel _backgroundImage;

	public static final int BUBBLE_PADDING = 10; // pixels
	public static final ImageIcon NOTICE_BUBBLE_IMG = new ImageIcon("img/speech_bubble_notice.png");//.getImage();
	public static final ImageIcon ERROR_BUBBLE_IMG = new ImageIcon("img/speech_bubble_error.png");//.getImage();

	public FlashPanel(AbstractModel model) {
		_model = model;
		model.addObserver(this);

		setVisible(true);
		setLayout(null);
		setOpaque(false); //Cache le background de la bulle de notification

		_txtaMessage = new JTextArea();
		_txtaMessage.setEnabled(false); // Le texte n'est pas modifiable
		_txtaMessage.setLineWrap(true); // Retour chariot auto
		_txtaMessage.setWrapStyleWord(true); // Le retour chariot ne coupe pas les mots
		_txtaMessage.setOpaque(false); // Fond transparent
		_txtaMessage.setFont(new Font( // Police et style du message
				_txtaMessage.getFont().getFontName(),
				_txtaMessage.getFont().getStyle(),
				_txtaMessage.getFont().getSize()+3
		));
		_txtaMessage.setDisabledTextColor(_txtaMessage.getForeground()); // Couleur du JTextArea en mode édition désactivée
		_txtaMessage.setBounds( // Position du JTextArea
			1 + BUBBLE_PADDING,
			BUBBLE_PADDING,
			300 - BUBBLE_PADDING*2,
			100 - BUBBLE_PADDING*2
		);
		add(_txtaMessage);

		_backgroundImage = new JLabel(NOTICE_BUBBLE_IMG);
		_backgroundImage.setBounds(0, 0, 311, 133);
		add(_backgroundImage);
	}

	public void displayError(String msg) {
		_backgroundImage.setIcon(ERROR_BUBBLE_IMG);
		_txtaMessage.setText(msg);
	}

	public void displayNotice(String msg) {
		_backgroundImage.setIcon(NOTICE_BUBBLE_IMG);
		_txtaMessage.setText(msg);
	}

	@Override
	public void update(Observable o, Object arg) {
		FlashMessage message = ((Flash)this._model).getLastFlashMessage();
		if(message != null)
			if(message.getType() == "error")
				displayError(message.getMessage());
			else if(message.getType() == "notice")
				displayNotice(message.getMessage());
	}
}
