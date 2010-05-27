package utils;

import javax.swing.ImageIcon;


/**
 * Interface pour les modules devant r�agir au clic sur le menu
 * @author J�r�mie ASTORI
 * @author Dominique CLAUSE
 */
public interface iClickable {

	/**
	 * Action � ex�cuter au clic sur le menu
	 */
	abstract public void click();
	/**
	 * Renvoit l'icone � afficher pour le cas Clickable
	 * @return
	 */
	abstract public ImageIcon getClickIcon();

	/**
	 * Renvoit une information tooltip pour le cas "clickable"
	 */
	abstract public String getClickTooltip();
}
