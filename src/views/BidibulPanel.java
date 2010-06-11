package views;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Main.Main;

public class BidibulPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private EyePanel _panLeftEye;
	private EyePanel _panRightEye;
	private ImageIcon _iconBidibul = new ImageIcon(Main.getResource("bidibul200/bidibul.png"));
	private JLabel _lblMouth;

	public static final ImageIcon MOUTH_NOTICE_IMG = new ImageIcon(Main.getResource("bidibul200/mouths/normal.png"));
	public static final ImageIcon MOUTH_ERROR_IMG = new ImageIcon(Main.getResource("bidibul200/mouths/error.png"));

	public BidibulPanel(JFrame frame) {
		super();
		setLayout(null);
		setOpaque(false);

		JLabel bidibul = new JLabel(_iconBidibul);
		bidibul.setBounds(0, 0, getWidth(), getHeight());

		_panLeftEye = new EyePanel();
		_panLeftEye.setBounds(
				92, 90,
				_panLeftEye.getWidth(),
				_panLeftEye.getHeight()
		);
		add(this._panLeftEye);

		_panRightEye = new EyePanel();
		_panRightEye.setBounds(
				77, 82,
				_panRightEye.getWidth(),
				_panRightEye.getHeight()
		);
		add(_panRightEye);

		_lblMouth = new JLabel(MOUTH_NOTICE_IMG);
		_lblMouth.setBounds(
				68, 99,
				MOUTH_NOTICE_IMG.getIconWidth(),
				MOUTH_NOTICE_IMG.getIconHeight()
		);
		add(_lblMouth);

		add(bidibul);

		Timer t = new Timer();

	    t.schedule(new TimerTask() {
			@Override
			public void run() {
	        	_panLeftEye.update();
	        	_panRightEye.update();
			}
	    }, 0, 50);

		t.schedule(new TimerTask() {
			@Override
			public void run() {
				if(Math.random() <= 0.20) {
					_panLeftEye.blink();
					_panRightEye.blink();
				}
			}
	    }, 1000, 800);
	}

	@Override
	public int getWidth() {
		return _iconBidibul.getIconWidth();
	}

	@Override
	public int getHeight() {
		return _iconBidibul.getIconHeight();
	}

	public void setMouth(String type) {
		if(type == "error")
			_lblMouth.setIcon(MOUTH_ERROR_IMG);
		else if(type == "notice")
			_lblMouth.setIcon(MOUTH_NOTICE_IMG);
	}
}
