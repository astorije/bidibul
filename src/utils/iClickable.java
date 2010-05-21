package utils;

import javax.swing.ImageIcon;


/**
 * Interface pour les modules devant réagir au clic sur le menu
 * @author Jérémie ASTORI
 */
public interface iClickable {

	/**
	 * Action à exécuter au clic sur le menu
	 */
	abstract public void click();
	abstract public ImageIcon getIcon();
}
