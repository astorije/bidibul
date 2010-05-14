package views;

import java.awt.Color;
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
 * @author Jérémie A.
 */
public class NotificationPanel extends JPanel implements Observer {
	private static final long serialVersionUID = 1L;
	private final AbstractModel _model;
	private final JTextArea _txtaMessage;
	//private String _type = "notice";

	private JLabel _backgroundImage;

	public static final int BUBBLE_PADDING = 10; // pixels
	public static final ImageIcon NOTICE_BUBBLE_IMG = new ImageIcon("img/speech_bubble_notice.png");//.getImage();
	public static final ImageIcon ERROR_BUBBLE_IMG = new ImageIcon("img/speech_bubble_error.png");//.getImage();

	public NotificationPanel(AbstractModel model) {
		this._model = model;
		model.addObserver(this);

		this.setVisible(true);
		this.setLayout(null);

		this._txtaMessage = new JTextArea();
		this._txtaMessage.setEnabled(false); // Le texte n'est pas modifiable
		this._txtaMessage.setLineWrap(true); // Retour chariot auto
		this._txtaMessage.setWrapStyleWord(true); // Le retour chariot ne ocupe pas les mots
		this._txtaMessage.setOpaque(false); // Fond transparent
		Font f = this._txtaMessage.getFont();
		this._txtaMessage.setFont(new Font(f.getFontName(), f.getStyle(), f.getSize()+3)); // Police et style du message
		this._txtaMessage.setDisabledTextColor(Color.BLACK); // Couleur du JTextArea en mode édition désactivée
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
/*
	@Override
	public void paintComponent(Graphics g) {
		if(this._type == "notice")
			g.drawImage(NOTICE_BUBBLE_IMG, 0, 0, null);
		else
			g.drawImage(ERROR_BUBBLE_IMG, 0, 0, null);
	}
*/
	public void displayError(String msg) {
		//this._type = "error";
		//this.repaint();
		this._backgroundImage.setIcon(ERROR_BUBBLE_IMG);
		this._txtaMessage.setText(msg);
	}

	public void displayNotice(String msg) {
		//this._type = "notice";
		//this.repaint();
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
