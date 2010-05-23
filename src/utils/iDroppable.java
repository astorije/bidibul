package utils;

import java.util.ArrayList;

/**
 * Interface pour les modules devant r�agir au drop d'un fichier sur le Bidibul
 * @author J�r�mie ASTORI
 */
public interface iDroppable {
	/**
	 * Action � ex�cuter lorsqu'un �l�ment est d�pos� sur le Bidibul
	 */
	abstract public void drop();

	/**
	 * Retourne la liste des extensions support�es par le glisser-d�poser
	 * @return Liste d'extensions
	 */
	abstract public ArrayList<String> getAllowedExtensions();
}
