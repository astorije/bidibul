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
 * @todo Insérer dans l'architecture MVC
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
     * @todo Affecter une visibilité, la variable est-elle static ?
     */
	List<BidibulModule> listModule;

	/**
     * Le nom du dossier o� sont stock�s les .class des modules.
     * @see ModuleLoader#loadModules()
     */
	private static String moduleDir = "modules";

	/**
     * S�parateur syst�me
     * @see ModuleLoader#loadModules()
     * @see ModuleLoader#findModulesName
     */
	private static String sep = System.getProperty("file.separator");


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
	public void loadModules(){
		String[] className = findModulesName();

		if (className != null){
			// Pr�pare le ClassLoader
			URLClassLoader loader=null;
			try {
				loader = new URLClassLoader(new URL[] {new URL("file://" + System.getProperty("user.dir") + sep + moduleDir + sep)});
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

			listModule.clear();
			for (int i=0; i<className.length; i++){
				try {
					Class<?> externalClass = loader.loadClass(className[i]);

					//if (implement_iModule(externalClass) ) {
					if (extends_BidibulModule(externalClass) ) {
						Object externalObject = externalClass.newInstance();
						((Observable)externalObject).addObserver(Flash.getInstance());
						listModule.add((BidibulModule) externalObject);
						System.out.println("Class loaded: " + externalClass.getCanonicalName());
					}
				}
				catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
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
		File dir = new File("." + sep + moduleDir);
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
	 * Renvoi true si la classe impl�mente l'interface iModule
	 * @see iModule
	 * @see ModuleLoader#loadModules()
	 * @todo Pourquoi ne pas utiliser instanceof à la place ?
	 * @deprecated Je pense que l'on peut utiliser extends_BidibulModule désormais
	 */
/*	@Deprecated
	private boolean implement_iModule(Class<?> c){
		Class<?>[] interfaces = c.getInterfaces();
		for (int i=0; i<interfaces.length;i++){
			if (interfaces[i].getName() == "iModule")
				return true;
		}
		return false;
	}
*/
	/**
	 * Renvoie true si la classe hérite de la classe BidibulModule
	 * @see BidibulModule
	 * @see ModuleLoader#loadModules()
	 * @todo Pourquoi ne pas utiliser instanceof à la place ?
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