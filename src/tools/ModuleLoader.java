package tools;
import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import models.Flash;
import utils.BidibulModule;

/**
 * Classe de chargement des modules. Le ModuleLoader est un singleton.
 * @author Nicolas B.
 * @todo Gérer les modules activés/desactivés
 */
public class ModuleLoader {
	 /**
     * L'instance du ModuleLoader.
     * @see ModuleLoader#getInstance()
     */
	private static ModuleLoader instance;

	/**
     * La liste qui contient une instance de chaque module.
     * @see ModuleLoader#loadModules()
     * @see ModuleLoader#getListModules()
     */
	private List<BidibulModule> listModule;

	/**
     * Le nom du dossier o� sont stock�s les .class des modules.
     * @see ModuleLoader#loadModules()
     */
	private static String moduleDir = "modules";

	/**
	 * Constructeur priv�.
	 * @see ModuleLoader#getInstance()
	 */
	private ModuleLoader() {
		listModule = new ArrayList<BidibulModule>();
	}

	/**
	 * Retourne l'instance du ModuleLoader.
	 * @return ModuleLoader
	 */
	public static ModuleLoader getInstance() {
        if (null == instance) {
            instance = new ModuleLoader();
        }
        return instance;
    }

	/**
	 * Retourne le tableau des modules.
	 * @return iModule[] null si aucun module charg�
	 * @see ModuleLoader#loadModules()
	 */
	public List<BidibulModule> getListModules(){
		return listModule;
	}

	/**
	 * Charge les modules. Remplit le tableau moduleArray.
	 * @see ModuleLoader#findModulesName()
	 * @see ModuleLoader#listModule
	 */
	public boolean loadModules(){
		String[] className = findModulesName();

		if (className != null){
			// Pr�pare le ClassLoader
			URLClassLoader loader=null;
			try {
				loader = new URLClassLoader(new URL[] {new URL("file://" + System.getProperty("user.dir") + "/" + moduleDir + "/")});
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return false;
			}

			listModule.clear();
			for (int i=0; i<className.length; i++){
				try {
					Class<?> externalClass = loader.loadClass(className[i]);

					if (extends_BidibulModule(externalClass) ) {
						Object externalObject = externalClass.newInstance();
						((Observable)externalObject).addObserver(Flash.getInstance());
						listModule.add((BidibulModule) externalObject);
						System.out.println("Class loaded: " + externalClass.getCanonicalName());
					}
				}
				catch (ClassNotFoundException e) {
					e.printStackTrace();
					return false;
				} catch (InstantiationException e) {
					e.printStackTrace();
					return false;
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Renvoie un tableau contenant le nom des classes � charger
	 * @see ModuleLoader#loadModules()
	 * @see ModuleLoader#moduleDir
	 */
	private String[] findModulesName(){
		// Filtre les fichiers .class du du dossier contenant les modules
		FilenameFilter javaFilter = new FilenameFilter() {
			public boolean accept(File arg0, String arg1) {
				return arg1.endsWith(".class");
			}
		};
		// Extrait les noms des classes par rapport aux noms des fichiers du dossier
		File dir = new File("./" + moduleDir);
		String[] fileName = dir.list(javaFilter);
		String[] className = null;
		if (fileName != null){
			className = new String[fileName.length];
			for(int i=0;i<fileName.length;i++){
				className[i] = fileName[i].substring(0,fileName[i].lastIndexOf(".class"));
			}
		}
		return className;
	}

	/**
	 * Renvoie true si la classe hérite de la classe BidibulModule
	 * @see BidibulModule
	 * @see ModuleLoader#loadModules()
	 * @todo Mieux que "Class<?>" ?
	 */
	private boolean extends_BidibulModule(Class<?> c){
		Class<?> superclass = c.getSuperclass();
		if (superclass.getName() == "utils.BidibulModule")
				return true;
		else {
			System.out.println("pas BidibulModule :" + superclass.getName());
			return false;
		}
	}
}