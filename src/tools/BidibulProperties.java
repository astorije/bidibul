package tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Classe g�rant les .properties des modules
 * @author Nicolas B.
 */
public class BidibulProperties extends Properties{
	private static final long serialVersionUID = 1L;
	/**
     * Le nom du dossier o� sont stock�s les .properties de l'application.
     * @see BidibulProperties#save()
     */
	private static final String _propertiesDir = "properties";

	/**
     * Le nom du fichier .properties
     * @see BidibulProperties#save()
     * @see BidibulProperties#load()
     */
	private final String _name;


	/**
     * Constructeur
     * Si un fichier existe d�j� avec le m�me nom, les properties seront charg�es.
     * @param String Le nom des properties
     */
	public BidibulProperties(String name) {
		super();
		_name = name;

		File f = new File(_propertiesDir + "/" + name + ".properties");
		if (f.exists()) {
			try {
				this.load(new FileInputStream(_propertiesDir + "/" + name + ".properties"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Enregistre les Properties
	 * @return boolean true si la sauvegarde a �t� effectu�e, false sinon
	 */
	public boolean save() {
		OutputStream out;
		try {
			out = new FileOutputStream(_propertiesDir + "/" + _name + ".properties");
			this.store(out, null);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace(); return false;
		} catch (IOException e) {
			e.printStackTrace(); return false;
		}
		return true;
	}

	/**
	 * Charge le fichier properties contenu dans le r�pertoire par d�faut
	 * @param String le nom des properties
	 * @return les BidibulProperties charg�es
	 */
	public static BidibulProperties load(String name) {
		BidibulProperties p = new BidibulProperties(name);
		try {
			p.load(new FileInputStream(_propertiesDir + "/" + name + ".properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace(); return null;
		} catch (IOException e) {
			e.printStackTrace(); return null;
		}
		return p;
	}
}
