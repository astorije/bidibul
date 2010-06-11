package tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Classe g�rant les .properties des modules
 * @author Nicolas B.
 */
public class BidibulProperties extends Properties {
	private static final long serialVersionUID = 1L;
	/**
     * Le nom du dossier o� sont stock�s les .properties de l'application.
     * @see BidibulProperties#save()
     */
	private  final String _propertiesDir;

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
		_propertiesDir = "properties";

		File f = new File(_propertiesDir + File.separator + name + ".properties");
		if (f.exists()) {
			try {
				this.load(new FileInputStream(_propertiesDir + File.separator + name + ".properties"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
     * Constructeur
     * @param name : Le nom des properties
     * @param in : Le flux vers le fichier properties
     */
	public BidibulProperties(String name, InputStream in) {
		super();
		_name = name;
		_propertiesDir = null;

		try {
			this.load(in);
			System.out.println("fichier properties du module "+name+" charg�.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Enregistre les Properties
	 * @return boolean true si la sauvegarde a �t� effectu�e, false sinon
	 */
	public boolean save() {
		if (_propertiesDir != null) {
			File f = new File(_propertiesDir);
			if (!f.exists())
				f.mkdir();
			OutputStream out;
			try {
				out = new FileOutputStream(_propertiesDir + File.separator + _name + ".properties");
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
		return false;
	}

	/**
	 * Charge le fichier properties contenu dans le r�pertoire par d�faut
	 * @param String le nom des properties
	 * @return les BidibulProperties charg�es
	 */
	public BidibulProperties load(String name) {
		if (_propertiesDir != null) {
			BidibulProperties p = new BidibulProperties(name);
			try {
				p.load(new FileInputStream(_propertiesDir + File.separator + name + ".properties"));
			} catch (FileNotFoundException e) {
				e.printStackTrace(); return null;
			} catch (IOException e) {
				e.printStackTrace(); return null;
			}
			return p;
		}
		return null;
	}

	/**
	 * Retourne la position sauvegard� pour x du Bidibul
	 * @return int : la position
	 */
	public int getPosX() {
		String str = getProperty("posX");
		if(str != null && Integer.parseInt(str) >= 0)
			return Integer.parseInt(str);
		else return 100;
	}

	/**
	 * Retourne la position sauvegard� pour y du Bidibul
	 * @return int : la position
	 */
	public int getPosY() {
		String str = getProperty("posY");
		if(str != null && Integer.parseInt(str) >= 0)
			return Integer.parseInt(str);
		else return 100;
	}

	/**
	 * Retourne true si la propri�t�e sauvegard�e alwaysOnTop est � 1. False sinon.
	 * @return boolean
	 */
	public boolean isAlwaysOnTop() {
		return (getProperty("alwaysOnTop")).equals("1");
	}
}
