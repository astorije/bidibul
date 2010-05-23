package utils;

import java.util.ArrayList;

/**
 * Interface pour les modules devant réagir au drop d'un fichier sur le Bidibul
 * @author Jérémie ASTORI
 */
public interface iDroppable {
	/**
	 * Action à  exécuter lorsqu'un élément est déposé sur le Bidibul
	 */
	abstract public void drop();

	/**
	 * Retourne la liste des extensions supportées par le glisser-déposer
	 * @return Liste d'extensions
	 */
	abstract public ArrayList<String> getAllowedExtensions();
}
