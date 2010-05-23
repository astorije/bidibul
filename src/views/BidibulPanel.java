package views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class BidibulPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private EyePanel _panLeftEye;
	private EyePanel _panRightEye;

	public BidibulPanel(JFrame frame) {
		super();

		this.setLayout(null);
		this.setOpaque(false);

		JLabel bidibul = new JLabel(new ImageIcon("img/bidibul.png"));
		bidibul.setBounds(0, 0, 300, 300);

		this._panLeftEye = new EyePanel();
		this._panLeftEye.setPreferredSize(this.getMaximumSize());
		this._panLeftEye.setBounds(
				136,
				133,
				this._panLeftEye.getWidth(),
				this._panLeftEye.getHeight()
		);
		this.add(this._panLeftEye);

		this._panRightEye = new EyePanel();
		this._panRightEye.setPreferredSize(this.getMaximumSize());
		this._panRightEye.setBounds(
				117,
				122,
				this._panRightEye.getWidth(),
				this._panRightEye.getHeight()
		);
		this.add(this._panRightEye);

		// @deprecated Cette solution utilise le GlassPane, et donc les MouseEvent ne sont plus détectés...
		//frame.getGlassPane().addMouseMotionListener(this._panLeftEye);
		//frame.getGlassPane().addMouseMotionListener(this._panRightEye);

	    ActionListener taskPerformer = new ActionListener() {
	        public void actionPerformed(ActionEvent evt) {
	        	BidibulPanel.this._panLeftEye.update();
	        	BidibulPanel.this._panRightEye.update();
	        }
	    };

	    // Toutes les X milliseconds...
	    new Timer(50, taskPerformer).start();

		this.add(bidibul);
	}
}
