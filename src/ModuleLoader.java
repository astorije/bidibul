import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de chargement des modules. Le ModuleLoader est un singleton.
 * @author Nicolas B.
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
     * @see ModuleLoader#getModuleArray()
     */
	List<iModule> listModule;
	
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
		listModule = new ArrayList<iModule>();
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
	 * @return iModule[] null si aucun module chargé
	 * @see ModuleLoader#loadModules()
	 */
	public List<iModule> getListModules(){
		return listModule;
	}
	
	/**
	 * Charge les modules. Remplit le tableau moduleArray.
	 * @see ModuleLoader#findModulesName()
	 * @see ModuleLoader#listModule
	 */
	public void loadModules()
	{
		String[] className = findModulesName();
		if (className != null){
			
			// Prépare le ClassLoader
			URLClassLoader loader=null;
			try {
				loader = new URLClassLoader(new URL[] {new URL("file://"+System.getProperty("user.dir")+"\\"+moduleDir+"\\")});
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			listModule.clear();
			for (int i=0; i<className.length; i++){
				try {
					Class externalClass = loader.loadClass(className[i]);
					
					if (implement_iModule(externalClass)) {
						Object externalObject = externalClass.newInstance();
						listModule.add((iModule) externalObject);
						System.out.println("Class loaded: " + externalClass.getCanonicalName());
					}
				}
				catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Renvoi un tableau contenant le nom des classes à charger
	 * @see ModuleLoader#loadModules()
	 * @see ModuleLoader#moduleDir
	 */
	private String[] findModulesName()
	{
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
	 * Renvoi true si la classe implémente l'interface iModule
	 * @see iModule
	 * @see ModuleLoader#loadModules()
	 */
	private boolean implement_iModule(Class c){
		Class[] interfaces = c.getInterfaces();
		for (int i=0; i<interfaces.length;i++){
			if (interfaces[i].getName() == "iModule")
				return true;
		}
		return false;
	}
}
