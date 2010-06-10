package tools;

import java.io.InputStream;
import java.util.HashMap;

import utils.BidibulModule;

/**
 * Classe API de gestion des propriétés des modules
 * @author Nicolas B.
 */
abstract public class BidibulInformation {
	/**
     * Associe le nom de la classe à ses Properties.
     */
	private static HashMap<String, BidibulProperties> _prop = new HashMap<String, BidibulProperties>();

	/**
     * Ajoute les propriétés d'un module
     * @param mod : le module à ajouter
     */
	public static void add(BidibulModule mod) {
		_prop.put(mod.getClass().getCanonicalName(), new BidibulProperties(mod.getClass().getCanonicalName()));
	}

	/**
     * Ajoute les propriétés d'un module
     * @param c : la classe à ajouter
     */
	public static void add(Class<BidibulModule> c) {
		_prop.put(c.getCanonicalName(), new BidibulProperties(c.getCanonicalName()));
	}

	/**
     * Ajoute les propriétés d'un module
     * @param c : la classe à ajouter
     * @param in : le flux vers le fichier properties
     */
	public static void add(Class<BidibulModule> c, InputStream in) {
		_prop.put(c.getCanonicalName(), new BidibulProperties(c.getCanonicalName(), in));
	}

	/**
     * Retourne la propriété d'un module
     * @param property : la propriété demandée
     * @param mod : le module
     */
	public static String get(String property, BidibulModule mod){
		if (_prop.containsKey(mod.getClass().getCanonicalName()))
			return _prop.get(mod.getClass().getCanonicalName()).getProperty(property);
		return null;
	}

	/**
     * Retourne la propriété d'un module
     * @param property : la propriété demandée
     * @param c : la classe du module
     */
	public static String get(String property, Class<BidibulModule> c){
		if (_prop.containsKey(c.getCanonicalName())) {
			return _prop.get(c.getCanonicalName()).getProperty(property);
		}
		return null;
	}

	/**
     * Ajoute une propriété à un module
     * @param property : la propriété demandée
     * @param value : la valeur de la propriété
     * @param mod : le module
     */
	public static void put(String property, String value, BidibulModule mod){
		if (_prop.containsKey(mod.getClass().getCanonicalName()))
			_prop.get(mod.getClass().getCanonicalName()).put(property, value);
	}

	/**
     * Ajoute une propriété à un module
     * @param property : la propriété demandée
     * @param value : la valeur de la propriété
     * @param c : la classe du module
     */
	public static void put(String property, String value, Class<BidibulModule> c){
		if (_prop.containsKey(c.getCanonicalName()))
			_prop.get(c.getCanonicalName()).put(property, value);
	}

	public static String getName(Class<BidibulModule> c) {
		String s = get("name", c);
		return (s == null) ? "Module sans nom" : s;
	}

	public static String getDescription(Class<BidibulModule> c) {
		String s = get("description", c);
		return (s == null) ? "Pas de description" : s;
	}

	public static String getAuthor(Class<BidibulModule> c) {
		String s = get("author", c);
		return (s == null) ? "Anonyme" : s;
	}

	public static String getWebsite(Class<BidibulModule> c) {
		return get("website", c);
	}
}
