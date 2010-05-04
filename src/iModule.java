import java.util.Observer;

/**
 * Interface des modules
 * @author Nicolas B.
 */
public interface iModule extends Observer {
	
	/**
	 * Renvoie le nom du module. Ce nom est utilisé par le rotorMenu.
	 * @return String : Le nom du module
	 */
	public String getName();
	
	/**
	 * Lance l'exécution du module.
	 */
	public void run();
	
}
