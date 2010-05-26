package utils;

import java.awt.datatransfer.DataFlavor;
import java.util.ArrayList;

import javax.swing.ImageIcon;

/**
 * Interface pour les modules devant réagir au drop d'un fichier sur le Bidibul
 * @author Jérémie ASTORI
 */
public interface iDroppable {
	/**
	 * Action à  exécuter lorsqu'un élément est déposé sur le Bidibul
	 */
	abstract public void drop(String[] listeFichier);

	/**
	 * Retourne la liste des extensions supportées par le glisser-déposer
	 * @return Liste d'extensions
	 */
	abstract public ArrayList<String> getAllowedExtensions();

	/**
	 * Retourne la liste des types des types autorisées.
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
