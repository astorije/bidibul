package views;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Main.Main;

public class EyeLidPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Timer _timerClosingInterval;
	boolean _forceBlink = false;

	private JLabel[] _lblsEyeLid = {
		new JLabel(new ImageIcon(Main.getResource("bidibul200/eyelids/0.png"))),
		new JLabel(new ImageIcon(Main.getResource("bidibul200/eyelids/1.png"))),
		new JLabel(new ImageIcon(Main.getResource("bidibul200/eyelids/2.png"))),
		new JLabel(new ImageIcon(Main.getResource("bidibul200/eyelids/3.png")))
	};

	public EyeLidPanel() {
		setVisible(true);
		setLayout(null);
		setOpaque(false);

	    for(int i=0; i<_lblsEyeLid.length; i++) {
	    	_lblsEyeLid[i].setBounds(
	    			0, 0,
	    			_lblsEyeLid[i].getIcon().getIconWidth(),
	    			_lblsEyeLid[i].getIcon().getIconHeight());
	    	_lblsEyeLid[i].setVisible(false);
	    }

	    for(int i=_lblsEyeLid.length-1; i>=0; i--)
	    	add(_lblsEyeLid[i]);

	}

	public void blink() {
    	_timerClosingInterval = new Timer();
    	_timerClosingInterval.schedule(new TimerTask() {
	    	int n = 0;
	    	int sens = 0;
			@Override
			public void run() {
	        	if(sens == 0) {
	        		_lblsEyeLid[n].setVisible(true);
    	        	if(n < _lblsEyeLid.length - 1) n++;
    	        	else sens = 1;
	        	}
	        	else {
	        		_lblsEyeLid[n].setVisible(false);
	        		if(n > 0) n--;
	        		else _timerClosingInterval.cancel();
	        	}
			}
	    }, 0, 50);
	}

	@Override
	public int getWidth() {
		return _lblsEyeLid[0].getIcon().getIconWidth();
	}

	@Override
	public int getHeight() {
		return _lblsEyeLid[0].getIcon().getIconHeight();
	}
}
