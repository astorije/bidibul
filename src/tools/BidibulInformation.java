package tools;

import java.util.HashMap;

import utils.BidibulModule;

/**
 * Classe API de gestion des propri�t�s des modules
 * @author Nicolas B.
 */
public class BidibulInformation {
	/**
     * Associe le nom de la classe � ses Properties.
     */
	private static HashMap<String, BidibulProperties> _prop = new HashMap<String, BidibulProperties>();

	/**
     * Ajoute les propri�t�s d'un module
     * @param mod : le module � ajouter
     */
	public static void add(BidibulModule mod) {
		_prop.put(mod.getClass().getCanonicalName(), new BidibulProperties(mod.getClass().getCanonicalName()));
	}

	/**
     * Ajoute les propri�t�s d'un module
     * @param c : la classe � ajouter
     */
	public static void add(Class<BidibulModule> c) {
		_prop.put(c.getCanonicalName(), new BidibulProperties(c.getCanonicalName()));
	}

	/**
     * Retourne la propri�t� d'un module
     * @param property : la propri�t� demand�e
     * @param mod : le module
     */
	public static String get(String property, BidibulModule mod){
		if (_prop.containsKey(mod.getClass().getCanonicalName()))
			return _prop.get(mod.getClass().getCanonicalName()).getProperty(property);
		return null;
	}

	/**
     * Retourne la propri�t� d'un module
     * @param property : la propri�t� demand�e
     * @param c : la classe du module
     */
	public static String get(String property, Class<BidibulModule> c){
		if (_prop.containsKey(c.getCanonicalName())) {
			return _prop.get(c.getCanonicalName()).getProperty(property);
		}
		return null;
	}

	/**
     * Ajoute une propri�t� � un module
     * @param property : la propri�t� demand�e
     * @param value : la valeur de la propri�t�
     * @param mod : le module
     */
	public static void put(String property, String value, BidibulModule mod){
		if (_prop.containsKey(mod.getClass().getCanonicalName()))
			_prop.get(mod.getClass().getCanonicalName()).put(property, value);
	}

	/**
     * Ajoute une propri�t� � un module
     * @param property : la propri�t� demand�e
     * @param value : la valeur de la propri�t�
     * @param c : la classe du module
     */
	public static void put(String property, String value, Class<BidibulModule> c){
		if (_prop.containsKey(c.getCanonicalName()))
			_prop.get(c.getCanonicalName()).put(property, value);
	}

}
