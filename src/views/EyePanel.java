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
	private ImageIcon _iconEye = new ImageIcon("img/oeil_gauche.png");
	private ImageIcon _iconPupil= new ImageIcon("img/pupille_gauche.png");
	private Point _previousMouseLocation = new Point();

	public EyePanel() {
		this.setVisible(true);
		this.setLayout(null);
		this.setOpaque(false); //Cache le background de la bulle de notification

		_lblPupil = new JLabel(_iconPupil);
		_lblPupil.setBounds(
				_iconEye.getIconWidth()/2 - _iconPupil.getIconWidth()/2,
				_iconEye.getIconHeight()/2 - _iconPupil.getIconHeight()/2,
				_iconPupil.getIconWidth(),
				_iconPupil.getIconHeight()
		);
		this.add(_lblPupil);

		JLabel lbl_eye = new JLabel(_iconEye);
		lbl_eye.setBounds(
				0,
				0,
				_iconEye.getIconWidth(),
				_iconEye.getIconHeight()
		);
		this.add(lbl_eye);
	}

/*	@Override
	public void mouseDragged(MouseEvent e) {}

	@Deprecated
	@Override
	public void mouseMoved(MouseEvent e) {
		// Changement de référentiel :
		// les coordonnées de la souris sont maintenant par rapport à l'oeil
		e = SwingUtilities.convertMouseEvent(SwingUtilities.getRootPane(this), e, this);

		// On veut la distance par rapport au centre de l'oeil gauche
		int x = (int) e.getPoint().getX() - this._iconEye.getIconWidth()/2;
		int y = (int) e.getPoint().getY() - this._iconEye.getIconHeight()/2;

		double angle = Math.atan2(y, x);
		double radius = Math.sqrt(x*x + y*y);

		int x_final = this._iconPupil.getIconWidth()/2 + 1 // "+1" : Correction de dioptrie horizontale...
				+ (int)(Math.min(7, radius) * Math.cos(angle)); // "+2" : Correction de dioptrie verticale...
		int y_final = this._iconPupil.getIconHeight()/2 + 2
				+ (int)(Math.min(7, radius) * Math.sin(angle));

		_lblPupil.setLocation(x_final, y_final);
	}
*/
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
			int x = (int) e.getX() - this._iconEye.getIconWidth()/2;
			int y = (int) e.getY() - this._iconEye.getIconHeight()/2;

			double angle = Math.atan2(y, x);
			double radius = Math.sqrt(x*x + y*y);

			int x_final = this._iconPupil.getIconWidth()/2 + 1 // "+1" : Correction de dioptrie horizontale...
					+ (int)(Math.min(7, radius) * Math.cos(angle)); // "+2" : Correction de dioptrie verticale...
			int y_final = this._iconPupil.getIconHeight()/2 + 2
					+ (int)(Math.min(7, radius) * Math.sin(angle));

			_lblPupil.setLocation(x_final, y_final);
		}
	}

	@Override
	public int getHeight() {
		return this._iconEye.getIconHeight();
	}

	@Override
	public int getWidth() {
		return this._iconEye.getIconWidth();
	}
}
