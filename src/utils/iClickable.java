package utils;

import javax.swing.ImageIcon;


/**
 * Interface pour les modules devant r�agir au clic sur le menu
 * @author J�r�mie ASTORI
 */
public interface iClickable {

	/**
	 * Action � ex�cuter au clic sur le menu
	 */
	abstract public void click();
	abstract public ImageIcon getIcon();
}
