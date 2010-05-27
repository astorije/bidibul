package utils;
import java.util.Observable;

/**
 * Superclasse abstraite des modules.
 * @author Nicolas BRUNET
 * @author Jérémie ASTORI
 */
abstract public class BidibulModule extends Observable {
	private String _notice;
	private String _error;

	/**
	 * Affecte un message d'information et notifie ses observeurs.
	 * @param msg Message d'information
	 */
	protected final void setNotice(String msg) {
		this._notice = msg;
		this.setChanged();
		this.notifyObservers("notice");
	}

	/**
	 * Retourne le contenu actuel du message d'information.
	 * @return message d'information
	 */
	public final String getNotice() {
		return this._notice;
	}

	/**
	 * Affecte un message d'erreur et notifie ses observeurs.
	 * @param msg Message d'erreur
	 */
	protected final void setError(String msg) {
		this._error = msg;
		this.setChanged();
		this.notifyObservers("error");
	}

	/**
	 * Retourne le contenu actuel du message d'erreur.
	 * @return message d'erreur
	 */
	public final String getError() {
		return this._error;
	}

	/**
	 * Effectue une action au chargement en surcharge du constructeur
	 */
	abstract public void onLoad();
}
