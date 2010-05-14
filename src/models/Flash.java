package models;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import utils.BidibulModule;

/**
 * Modèle du gestionnaire des messages d'information et d'erreur.
 * Flash est un singleton.
 * @author Jérémie ASTORI
 */
public class Flash extends AbstractModel implements Observer {
	private static Flash _instance;
	private final ArrayList<FlashMessage> _flashMessages;

	private Flash() {
		this._flashMessages = new ArrayList<FlashMessage>();
	}

	private void setFlashMessage(String type, String msg) {
		FlashMessage message = new FlashMessage(type, msg);
		this._flashMessages.add(message);
		this.setChanged();
		this.notifyObservers();
	}

	public void setNotice(String msg) {
		this.setFlashMessage("notice", msg);
	}

	public void setError(String msg) {
		this.setFlashMessage("error", msg);
	}

	public static Flash getInstance() {
		if(Flash._instance == null)
			Flash._instance = new Flash();
		return Flash._instance;
	}

	public FlashMessage getLastFlashMessage() {
		if (!this._flashMessages.isEmpty())
			return this._flashMessages.get(this._flashMessages.size()-1);
		else return null;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if(arg1 == "notice")
			this.setNotice(((BidibulModule)arg0).getNotice());
		else if(arg1 == "error")
			this.setError(((BidibulModule)arg0).getError());
	}
}
