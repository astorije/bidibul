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

	//private HashMap<Class<BidibulModule>, BidibulModule> _m;
	private List<Class<BidibulModule>> _c;
	private List<BidibulModule> _m;

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
		//_m = new HashMap<Class<BidibulModule>, BidibulModule>();
		_c = new ArrayList<Class<BidibulModule>>();
		_m = new ArrayList<BidibulModule>();
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
		/*List<BidibulModule> l = new ArrayList<BidibulModule>();
		Iterator<Class<BidibulModule>> i = _m.keySet().iterator();
		while(i.hasNext()) {
			BidibulModule m = _m.get(i.next());
			if (m != null)
				l.add(m);
		}
		return l;*/
		List<BidibulModule> l = new ArrayList<BidibulModule>();
		for (int i=0; i<_m.size(); i++) {
			BidibulModule m = _m.get(i);
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
	public /*Set<Class<BidibulModule>>*/List<Class<BidibulModule>> /*getSetAllModules*/getListAllModules(){
		return _c;
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
			_m.clear();
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
								Class<BidibulModule> c  = (Class<BidibulModule>) externalClass;
								_c.add(c);
								BidibulInformation.add(c);
								BidibulProperties p = new BidibulProperties("global");
								if (p.get(c.getCanonicalName()) != null && p.get(c.getCanonicalName()).equals("0")) {
									//_m.put(c, null);
									_m.add(null);
									System.out.println("BidibulModule inactif: " + externalClass.getCanonicalName());
								} else {
									//startModule(c);
									BidibulModule m  = c.newInstance();
									_m.add(_c.indexOf(c), m);
									m.addObserver(Flash.getInstance());
									System.out.println("Démarrage module : " + BidibulInformation.get("name", c));
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
	 */
	private boolean extends_BidibulModule(Class<?> c){
		Class<?> superclass = c.getSuperclass();
		//System.out.println("pas BidibulModule :" + superclass.getName());
		if (superclass.getName() == "utils.BidibulModule")
				return true;
		else {
			System.out.println("pas BidibulModule :" + superclass.getName());
			return false;
		}
	}

//	private void stopModule(Class<BidibulModule> c){
//		/*if (_m.containsKey(c)) {
//			_m.put(c, null);*/
//		if (_c.contains(c)) {
//			_m.set(_c.indexOf(c), null);
//			System.out.println("Arrêt module : " + BidibulInformation.get("name", c));
//			// Sauvegarde
//			BidibulProperties p = new BidibulProperties("global");
//			p.put(c.getCanonicalName(), "0");
//			p.save();
//		}
//	}

	public void stopModule(int n){
		/*if (_m.containsKey(c)) {
			_m.put(c, null);*/
		if (_c.size() >= n) {
			_m.set(n, null);
			System.out.println("Arrêt module : " + BidibulInformation.get("name", _c.get(n)));
			// Sauvegarde
			BidibulProperties p = new BidibulProperties("global");
			p.put(_c.get(n).getCanonicalName(), "0");
			p.save();
		}
	}

//	private void startModule(Class<BidibulModule> c){
//		try {
//			BidibulModule m  = c.newInstance();
//			//_m.put(c, m);
//			_m.remove(_c.indexOf(c));
//			_m.add(_c.indexOf(c), m);
//			m.addObserver(Flash.getInstance());
//			System.out.println("Démarrage module : " + BidibulInformation.get("name", c));
//			// Sauvegarde
//			BidibulProperties p = new BidibulProperties("global");
//			p.put(c.getCanonicalName(), "1");
//			p.save();
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		}
//	}

	public void startModule(int n){
		try {
			BidibulModule m  = _c.get(n).newInstance();
			_m.remove(n);
			_m.add(n, m);
			m.addObserver(Flash.getInstance());
			System.out.println("Démarrage module : " + BidibulInformation.get("name", _c.get(n)));
			// Sauvegarde
			BidibulProperties p = new BidibulProperties("global");
			p.put(_c.get(n).getCanonicalName(), "1");
			p.save();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public boolean isActive(Class<BidibulModule> c){
		//_m.get(c);
		if (/*_m.get(c)*/_m.get(_c.indexOf(c)) != null)
			return true;
		return false;
	}
}