package tools;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import models.Flash;
import utils.BidibulModule;

/**
 * Classe de chargement des modules. Le ModuleLoader est un singleton.
 * @author Nicolas B.
 */
public class ModuleLoader {
	/**
     * L'instance du ModuleLoader.
     * @see ModuleLoader#getInstance()
     */
	private static ModuleLoader _instance;

	/**
     * La liste des class des modules chargés
     */
	private List<Class<BidibulModule>> _allClasses;

	/**
     * La liste des modules actifs
     */
	private List<BidibulModule> _activeModule;

	/**
     * Le nom du dossier où sont stockés les .class des modules.
     * @see ModuleLoader#loadModules()
     */
	private final String moduleDir = "modules";

	/**
	 * Constructeur privé.
	 * @see ModuleLoader#getInstance()
	 */
	private ModuleLoader() {
		_allClasses = new ArrayList<Class<BidibulModule>>();
		_activeModule = new ArrayList<BidibulModule>();
	}

	/**
	 * Retourne l'instance du ModuleLoader.
	 * @return ModuleLoader
	 */
	public static ModuleLoader getInstance() {
        if (null == _instance) {
        	_instance = new ModuleLoader();
        }
        return _instance;
    }

	/**
	 * Retourne la liste des modules.
	 * @return List<BidibulModule> null si aucun module chargé
	 * @see ModuleLoader#loadModules()
	 */
	public List<BidibulModule> getListActiveModules(){
		List<BidibulModule> l = new ArrayList<BidibulModule>();
		for (int i=0; i<_activeModule.size(); i++) {
			BidibulModule m = _activeModule.get(i);
			if (m != null)
				l.add(m);
		}
		return l;
	}

	/**
	 * Retourne un Set de toutes les classes des modules.
	 * @return Set<Class<BidibulModule>> null si aucun module chargé
	 * @see ModuleLoader#loadModules()
	 */
	public List<Class<BidibulModule>> getListAllModules(){
		return _allClasses;
	}

	/**
	 * Charge les modules. Remplit la liste listModule.
	 * @see ModuleLoader#findModulesName()
	 * @see ModuleLoader#listModule
	 */
	public boolean loadModules(){
		String[] jarName = findJarNames();
		String propertiesFileName=null;
		URLClassLoader loader=null;
		boolean classLoaded = false;
		Class<BidibulModule> c = null;

		if (jarName != null){
			Enumeration<JarEntry> jarFiles;
			_activeModule.clear();
			for (int i=0; i<jarName.length; i++){
				// Prépare le ClassLoader
				try {
					loader = new URLClassLoader(new URL[] {new URL("file:///" + System.getProperty("user.dir") + File.separator + moduleDir + File.separator + jarName[i])});
				} catch (MalformedURLException e) {
					e.printStackTrace(); return false;
				}

				//On charge le jar en mémoire
				JarFile jar = null;
				try {
					jar = new JarFile("."+File.separator + moduleDir + File.separator + jarName[i]);
				} catch (IOException e) {
					e.printStackTrace(); return false;
				}

				//On récupère le contenu du jar
				jarFiles = jar.entries();

				while(jarFiles.hasMoreElements()){
					JarEntry jarFile = jarFiles.nextElement();
					String fileName = jarFile.toString();


					//Vérifie que le fichier courant est un .class
					if (fileName.endsWith(".class"))
					{
						String className = fileName.substring(0,fileName.length()-6);
						className = className.replaceAll("/",".");

						try {
							Class<?> externalClass = Class.forName(className ,true,loader); //ou : Class<?> externalClass = loader.loadClass(className);

							// Vérifie que la classe est un BidibulModule
							if (extends_BidibulModule(externalClass) ) {
								c  = (Class<BidibulModule>) externalClass;
								_allClasses.add(c);
								classLoaded = true;
								BidibulProperties p = new BidibulProperties("global");
								if (p.get(c.getCanonicalName()) != null && p.get(c.getCanonicalName()).equals("0")) {
									_activeModule.add(null);
									System.out.println("BidibulModule inactif: " + externalClass.getCanonicalName());
								} else {
									BidibulModule m  = c.newInstance();
									m.onLoad();
									_activeModule.add(_allClasses.indexOf(c), m);
									m.addObserver(Flash.getInstance());
									System.out.println("BidibulModule actif : " + externalClass.getCanonicalName());
								}
							}
						}
						catch (ClassNotFoundException e) {
							e.printStackTrace(); return false;
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}
					else if(fileName.endsWith(".properties"))
						propertiesFileName = fileName;
				}
				if (classLoaded){
					InputStream in = loader.getResourceAsStream(propertiesFileName);
					BidibulInformation.add(c, in);
					classLoaded = false;
					propertiesFileName = null;
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

		File dir = new File("." + File.separator + moduleDir);
		String[] fileNames = dir.list(javaFilter);

		return fileNames;
	}

	/**
	 * Renvoie true si la classe hérite de la classe BidibulModule
	 * @see BidibulModule
	 * @see ModuleLoader#loadModules()
	 */
	private boolean extends_BidibulModule(Class<?> c){
		Class<?> superclass = c.getSuperclass();
		if(superclass == null) {
			System.out.println("pas une sous-classe");
			return false;
		}
		else if (superclass.getName() == "utils.BidibulModule")
				return true;
		else {
			System.out.println("pas BidibulModule :" + superclass.getName());
			return false;
		}
	}

	/**
	 * Supprime le module de la liste des modules et appelle BidibulModule.onStop()
	 * @param n : le numéro du module dans la liste
	 */
	public void stopModule(int n){
		if (_allClasses.size() >= n) {
			_activeModule.get(n).onStop();
			_activeModule.set(n, null);
			System.out.println("Arrêt module : " + BidibulInformation.get("name", _allClasses.get(n)));
			// Sauvegarde
			BidibulProperties p = new BidibulProperties("global");
			p.put(_allClasses.get(n).getCanonicalName(), "0");
			p.save();
		}
	}

	/**
	 * Ajoute le module à la liste des modules et appelle BidibulModule.onLoad()
	 * @param n : le numéro du module dans la liste
	 */
	public void startModule(int n){
		try {
			BidibulModule m  = _allClasses.get(n).newInstance();
			m.onLoad();
			_activeModule.remove(n);
			_activeModule.add(n, m);
			m.addObserver(Flash.getInstance());
			System.out.println("Démarrage module : " + BidibulInformation.get("name", _allClasses.get(n)));
			// Sauvegarde
			BidibulProperties p = new BidibulProperties("global");
			p.put(_allClasses.get(n).getCanonicalName(), "1");
			p.save();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Renvoie true si le module est actuellement activé. False sinon.
	 * @param c : la classe du module
	 */
	public boolean isActive(Class<BidibulModule> c){
		if (_activeModule.get(_allClasses.indexOf(c)) != null)
			return true;
		return false;
	}
}