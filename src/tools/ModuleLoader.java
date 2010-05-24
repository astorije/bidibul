package tools;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Observable;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
     * Le nom du dossier où sont stockés les .class des modules.
     * @see ModuleLoader#loadModules()
     */
	private static String moduleDir = "modules";

	/**
	 * Constructeur privé.
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
	 * Retourne la liste des modules.
	 * @return List<BidibulModule> null si aucun module chargé
	 * @see ModuleLoader#loadModules()
	 */
	public List<BidibulModule> getListModules(){
		return listModule;
	}

	/**
	 * Charge les modules. Remplit la liste listModule.
	 * @see ModuleLoader#findModulesName()
	 * @see ModuleLoader#listModule
	 */
	public boolean loadModules(){
		String[] jarName = findJarNames();

		if (jarName != null){
			Enumeration<JarEntry> jarFiles;
			listModule.clear();
			for (int i=0; i<jarName.length; i++){
				// Prépare le ClassLoader
				URLClassLoader loader=null;
				try {
					loader = new URLClassLoader(new URL[] {new URL("file:///" + System.getProperty("user.dir") + "/" + moduleDir + "/" + jarName[i])});
				} catch (MalformedURLException e) {
					e.printStackTrace(); return false;
				}

				//On charge le jar en mémoire
				JarFile jar = null;
				try {
					jar = new JarFile("./" + moduleDir + "/" + jarName[i]);
				} catch (IOException e) {
					e.printStackTrace(); return false;
				}

				//On récupère le contenu du jar
				jarFiles = jar.entries();

				while(jarFiles.hasMoreElements()){
					String fileName = jarFiles.nextElement().toString();

					//Vérifie que le fichier courant est un .class
					if (fileName.endsWith(".class"))
					{
						String className = fileName.substring(0,fileName.length()-6);
						className = className.replaceAll("/",".");

						try {
							Class<?> externalClass = Class.forName(className ,true,loader);
							//Class<?> externalClass = loader.loadClass(className);

							// Vérifie que la classe est un BidibulModule
							if (extends_BidibulModule(externalClass) ) {
								Object externalObject = externalClass.newInstance();
								((Observable)externalObject).addObserver(Flash.getInstance());
								listModule.add((BidibulModule) externalObject);
								System.out.println("BidibulModule loaded: " + externalClass.getCanonicalName());
							}
						}
						catch (ClassNotFoundException e) {
							e.printStackTrace(); return false;
						} catch (InstantiationException e) {
							e.printStackTrace(); return false;
						} catch (IllegalAccessException e) {
							e.printStackTrace(); return false;
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * Renvoie un tableau contenant le nom des fichiers jar à charger
	 * @see ModuleLoader#loadModules()
	 * @see ModuleLoader#moduleDir
	 */
	private String[] findJarNames(){
		// Filtre les fichiers .jar du du dossier contenant les modules
		FilenameFilter javaFilter = new FilenameFilter() {
			public boolean accept(File arg0, String arg1) {
				return (arg1.endsWith(".jar")) ;
			}
		};

		File dir = new File("./" + moduleDir);
		String[] fileNames = dir.list(javaFilter);

		return fileNames;
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