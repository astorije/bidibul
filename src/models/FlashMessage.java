package models;

/**
 * Message d'erreur ou d'information
 */
public class FlashMessage {
	private final String _type;
	private final String _message;

	/**
	 * Constructeur de message
	 * @param type "notice" ou "error"
	 * @param message message d'erreur
	 */
	public FlashMessage(String type, String message) {
		this._type = type;
		this._message = message;
	}

	/**
	 * Retourne le type de message.
	 * @return "error" ou "notice"
	 */
	public String getType() {
		return _type;
	}

	/**
	 * Retourne le corps du message.
	 * @return message d'erreur
	 */
	public String getMessage() {
		return _message;
	}
}
