package views;

import java.awt.Font;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import models.Flash;
import models.FlashMessage;
import Main.Main;

/**
 * Panel des messages d'information et d'erreur
 * @author JÃ©rÃ©mie ASTORI
 */
public class FlashPanel extends JPanel implements Observer {
	private static final long serialVersionUID = 1L;
	private Flash _model;
	private JTextArea _txtaMessage;

	private JLabel _backgroundImage;

	private BidibulPanel _bidibul;
	private Timer _timerHidingFlashPanel;

	public static final ImageIcon NOTICE_BUBBLE_IMG = new ImageIcon(Main.getResource("bidibul200/bubble_notice.png"));
	public static final ImageIcon ERROR_BUBBLE_IMG = new ImageIcon(Main.getResource("bidibul200/bubble_error.png"));

	public FlashPanel(Flash model, BidibulPanel bidibul) {
		_model = model;
		model.addObserver(this);
		_bidibul = bidibul;

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
				Font.BOLD,
				_txtaMessage.getFont().getSize()
		));
		_txtaMessage.setDisabledTextColor(_txtaMessage.getForeground()); // Couleur du JTextArea en mode édition désactivée
		// Pour que le message soit scrollable
		JScrollPane scrollpanMessage = new JScrollPane(_txtaMessage);
		scrollpanMessage.setViewportBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		scrollpanMessage.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		scrollpanMessage.setOpaque(false); // Composant ET viewport doivent être transparents
		scrollpanMessage.getViewport().setOpaque(false);
		scrollpanMessage.setBounds( // Position du JTextArea
			18, 16, 168, 43
		);
		add(scrollpanMessage);

		_backgroundImage = new JLabel(NOTICE_BUBBLE_IMG);
		_backgroundImage.setBounds(
				0, 0,
				getWidth(),
				getHeight()
		);

		add(_backgroundImage);
	}

	public void displayError(String msg) {
		_backgroundImage.setIcon(ERROR_BUBBLE_IMG);
		_txtaMessage.setText(msg);
		_bidibul.setMouth("error");
		showBubble();
	}

	public void displayNotice(String msg) {
		_backgroundImage.setIcon(NOTICE_BUBBLE_IMG);
		_txtaMessage.setText(msg);
		_bidibul.setMouth("notice");
		showBubble();
	}

	@Override
	public void update(Observable o, Object arg) {
		FlashMessage message = (this._model).getLastFlashMessage();
		if(message != null)
			if(message.getType() == "error")
				displayError(message.getMessage());
			else if(message.getType() == "notice")
				displayNotice(message.getMessage());
	}

	@Override
	public int getHeight() {
		return NOTICE_BUBBLE_IMG.getIconHeight();
	}

	@Override
	public int getWidth() {
		return NOTICE_BUBBLE_IMG.getIconWidth();
	}

	public void onMouseEnter() {
		showBubble();
	}

	public void onMouseExit() {
		_timerHidingFlashPanel = new Timer();
		_timerHidingFlashPanel.schedule(new TimerTask() {
			@Override
			public void run() {
				Flash.notice("Clique ou déplace un fichier sur moi !");
				setVisible(false);
			}
	    }, 2000);
	}

	public void showBubble() {
		setVisible(true);
		if(_timerHidingFlashPanel != null) {
			_timerHidingFlashPanel.cancel();
		}
	}
}
