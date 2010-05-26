package utils;

import java.awt.datatransfer.DataFlavor;
import java.util.ArrayList;

import javax.swing.ImageIcon;

/**
 * Interface pour les modules devant r�agir au drop d'un fichier sur le Bidibul
 * @author J�r�mie ASTORI
 */
public interface iDroppable {
	/**
	 * Action � ex�cuter lorsqu'un �l�ment est d�pos� sur le Bidibul
	 */
	abstract public void drop(String[] listeFichier);

	/**
	 * Retourne la liste des extensions support�es par le glisser-d�poser
	 * @return Liste d'extensions
	 */
	abstract public ArrayList<String> getAllowedExtensions();

	/**
	 * Retourne la liste des types des types autoris�es.
	 * @return
	 */
	abstract public ArrayList<DataFlavor> getAllowedFlavor();

	/**
	 * Renvoit une information tooltip pour le cas "droppable"
	 */
	abstract public String getDropTooltip();
	/**
	 * Renvoit une icone pour le cas "droppable"
	 */
	abstract public ImageIcon getDropIcon();
}
