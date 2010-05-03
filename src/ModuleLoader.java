import java.io.File;
import java.io.FilenameFilter;

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
     * Le tableau qui contient une instance de chaque module.
     * @see ModuleLoader#loadModules()
     * @see ModuleLoader#getModuleArray()
     */
	iModule moduleArray[];
	
	
	/**
	 * Constructeur privé.
	 * @see ModuleLoader#getInstance()
	 */
	private ModuleLoader() {
	}
	
	/**
	 * Retourne l'instance du ModuleLoader.
	 * @return ModuleLoader
	 */
	public static ModuleLoader moduleArray() {
        if (null == instance) {
            instance = new ModuleLoader();
        }
        return instance;
    }
	
	/**
	 * Retourne le tableau des modules.
	 * @return iModule[]
	 */
	public iModule[] getModuleArray(){
		return moduleArray;
	}
	
	/**
	 * Charge les modules. Remplit le tableau moduleArray.
	 * @see ModuleLoader#moduleArray
	 */
	public void loadModules()
	{
		moduleArray = new iModule[10];
		ClassLoader classLoader = ModuleLoader.class.getClassLoader();

		try {
			Class externalClass = classLoader.loadClass("Mod1");
		
			System.out.println("Class is loaded: " + externalClass.getCanonicalName());
			
			Object externalObject = externalClass.newInstance();
			iModule module;
			if (externalObject instanceof iModule)
			{
				module = (iModule) externalObject;
				moduleArray[0] = module;
				module.run();
				System.out.println("name: " + module.getName());
				//iModule moduleArray[] = null;
				
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
	
	public void test(){
		
		FilenameFilter javaFilter = new FilenameFilter() { 

			public boolean accept(File arg0, String arg1) { 
				return true;//return arg1.endsWith(".java"); 
			} 
		}; 

		File repertoire = new File("."); 
		String[] children = repertoire.list(javaFilter); 
		for(int i=0;i<children.length;i++){ 
			System.out.println("test " +children[i]/*.substring(0,children[i].lastIndexOf(".java"))*/); 
		} 
	}
}
