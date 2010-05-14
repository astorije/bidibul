
/**
 * Interface des modules
 * @author Nicolas B.
 */
@Deprecated
public interface iModule {

	/**
	 * Renvoie le nom du module. Ce nom est utilis� par le rotorMenu.
	 * @return String : Le nom du module
	 */
	public String getName();

	/**
	 * Lance l'ex�cution du module.
	 */
	public void run();

}
