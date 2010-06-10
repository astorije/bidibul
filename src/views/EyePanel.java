package views;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class EyePanel extends JPanel { //implements MouseMotionListener {
	private static final long serialVersionUID = 1L;
	private JLabel _lblPupil;
	private ImageIcon _iconEye = new ImageIcon("img/bidibul200/eye.png");
	private ImageIcon _iconPupil= new ImageIcon("img/bidibul200/pupil.png");
	private Point _previousMouseLocation = new Point();

	private EyeLidPanel _panEyeLid;

	public EyePanel() {
		setVisible(true);
		setLayout(null);
		setOpaque(false); // Cache le background de la bulle de notification

		_panEyeLid = new EyeLidPanel();
		_panEyeLid.setBounds(
				0, 0,
				_panEyeLid.getWidth(),
				_panEyeLid.getHeight()
		);
		add(_panEyeLid);

		_lblPupil = new JLabel(_iconPupil);
		_lblPupil.setBounds(
				_iconEye.getIconWidth()/2 - _iconPupil.getIconWidth()/2,
				_iconEye.getIconHeight()/2 - _iconPupil.getIconHeight()/2,
				_iconPupil.getIconWidth(),
				_iconPupil.getIconHeight()
		);
		add(_lblPupil);

		JLabel lbl_eye = new JLabel(_iconEye);
		lbl_eye.setBounds(
				0,
				0,
				_iconEye.getIconWidth(),
				_iconEye.getIconHeight()
		);
		add(lbl_eye);
	}

	public void update() {
		PointerInfo pointer = MouseInfo.getPointerInfo();
		Point e = pointer.getLocation();
		if(!this._previousMouseLocation.equals(e)) {
			// La souris s'est déplacée, il faut mettre à jour l'oeil
			this._previousMouseLocation.setLocation(e);

			// Changement de référentiel :
			// les coordonnées de la souris sont maintenant par rapport à l'oeil
			SwingUtilities.convertPointFromScreen(e, this);//(SwingUtilities.getRootPane(this), location, this);

			// On veut la distance par rapport au centre de l'oeil gauche
			int x = (int) e.getX() - _iconEye.getIconWidth()/2;
			int y = (int) e.getY() - _iconEye.getIconHeight()/2;

			double angle = Math.atan2(y, x);
			double radius = Math.min(6, Math.sqrt(x*x + y*y));

			int x_final = _iconPupil.getIconWidth()/2 + 2 // Correction de dioptrie horizontale...
					+ (int)(radius * Math.cos(angle));
			int y_final = _iconPupil.getIconHeight()/2 + 2 // Correction de dioptrie verticale...
					+ (int)(radius * Math.sin(angle));

			_lblPupil.setLocation(x_final, y_final);
		}
	}

	@Override
	public int getHeight() {
		return _iconEye.getIconHeight();
	}

	@Override
	public int getWidth() {
		return _iconEye.getIconWidth();
	}

	public void blink() {
		_panEyeLid.blink();
	}
}
