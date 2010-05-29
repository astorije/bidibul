package views;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BidibulPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private EyePanel _panLeftEye;
	private EyePanel _panRightEye;

	public BidibulPanel(JFrame frame) {
		super();
		setLayout(null);
		setOpaque(false);

		JLabel bidibul = new JLabel(new ImageIcon("img/bidibul200/bidibul.png"));
		bidibul.setBounds(0, 0, 190, 187);

		_panLeftEye = new EyePanel(0);
		_panLeftEye.setBounds(
				85, 84,
				_panLeftEye.getWidth(),
				_panLeftEye.getHeight()
		);
		add(this._panLeftEye);

		_panRightEye = new EyePanel(100);
		_panRightEye.setBounds(
				72, 76,
				_panRightEye.getWidth(),
				_panRightEye.getHeight()
		);
		add(_panRightEye);

		Timer t = new Timer();
	    t.schedule(new TimerTask() {
			@Override
			public void run() {
	        	_panLeftEye.update();
	        	_panRightEye.update();
			}
	    }, 0, 50);

		add(bidibul);
	}

	@Override
	public int getWidth() {
		return 190;
	}

	@Override
	public int getHeight() {
		return 187;
	}
}
