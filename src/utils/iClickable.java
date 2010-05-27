package utils;

import javax.swing.ImageIcon;


/**
 * Interface pour les modules devant réagir au clic sur le menu
 * @author Jérémie ASTORI
 * @author Dominique CLAUSE
 */
public interface iClickable {

	/**
	 * Action à exécuter au clic sur le menu
	 */
	abstract public void click();
	/**
	 * Renvoit l'icone à afficher pour le cas Clickable
	 * @return
	 */
	abstract public ImageIcon getClickIcon();

	/**
	 * Renvoit une information tooltip pour le cas "clickable"
	 */
	abstract public String getClickTooltip();
}
