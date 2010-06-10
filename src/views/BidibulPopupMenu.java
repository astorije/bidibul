package views;

import java.awt.AWTException;
import java.awt.Frame;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import models.Flash;
import tools.BidibulProperties;

/**
 *
 * @author Jérémie ASTORI
 *
 */
public class BidibulPopupMenu extends JPopupMenu implements ActionListener,
															ItemListener {
	private static final long serialVersionUID = 1L;

	private static BidibulPopupMenu _instance;
	private MainFrame _frame;

	private static final String MODULE_MANAGEMENT = "Activer/Désactiver un module";
	private static final String ALWAYS_ON_TOP = "Toujours au premier plan";
	private static final String BIDIBUL_HIDE = "Cacher Bidibul";
	private static final String BIDIBUL_MOVE = "Déplacer Bidibul";
	private static final String EXIT = "Quitter Bidibul";

	public BidibulPopupMenu(MainFrame frame) {
	    super();
	    _instance = this;
	    _frame = frame;

	    // Gestion des modules
	    JMenuItem itmModuleManagement = new JMenuItem(MODULE_MANAGEMENT);
	    itmModuleManagement.setMnemonic(KeyEvent.VK_M);
	    itmModuleManagement.addActionListener(this);
	    this.add(itmModuleManagement);

	    // Toujours au premier plan
	    JCheckBoxMenuItem itmAlwaysOnTop = new JCheckBoxMenuItem(ALWAYS_ON_TOP);
	    if(new BidibulProperties("global").isAlwaysOnTop())
	    	itmAlwaysOnTop.setSelected(true);
	    itmAlwaysOnTop.setMnemonic(KeyEvent.VK_T);
	    itmAlwaysOnTop.addItemListener(this);
	    this.add(itmAlwaysOnTop);

	    // Déplacer le bidibul
	    JCheckBoxMenuItem itmMoveBidibul = new JCheckBoxMenuItem(BIDIBUL_MOVE);
	    itmMoveBidibul.setMnemonic(KeyEvent.VK_D);
	    itmMoveBidibul.addItemListener(this);
	    this.add(itmMoveBidibul);

	    this.addSeparator();

	    // Place le bidibul dans le systray
	    JMenuItem itmHideBidibul = new JMenuItem(BIDIBUL_HIDE);
	    itmHideBidibul.setMnemonic(KeyEvent.VK_H);
	    itmHideBidibul.addActionListener(this);
	    this.add(itmHideBidibul);

	    this.addSeparator();

	    // Quitter
	    JMenuItem itmExit = new JMenuItem(EXIT);
	    itmExit.setMnemonic(KeyEvent.VK_Q);
	    itmExit.setAccelerator(KeyStroke.getKeyStroke(
	            KeyEvent.VK_F4, ActionEvent.ALT_MASK)); // Ne fonctionne pas, c'est juste pour information de l'utilisateur
	    itmExit.addActionListener(this);
	    this.add(itmExit);
	}

	/**
	 * Action à lancer par défaut à la fermeture du programme
	 */
	public static void exit() {
		// Sauvegarde de la position
		BidibulProperties p = new BidibulProperties("global");
		p.put("posX", ((Integer) _instance._frame.getLocation().x).toString());
		p.put("posY", ((Integer) _instance._frame.getLocation().y).toString());
		p.save();

		Flash.notice("A bientôt !");

		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(2000);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
		      	System.exit(0);
		      }
		  }).start();
	}

	@Override
    public void actionPerformed(ActionEvent e) {
		JMenuItem source = (JMenuItem)(e.getSource());
		if(source.getText() == MODULE_MANAGEMENT)
			new ModuleManagerFrame();
		else if(source.getText() == EXIT) {
			exit();
		}
		else if (source.getText() == BIDIBUL_HIDE) { //Cas du systray
			try {
	             this._frame.getSystemTray().add(this._frame.getTrayIcon());
	             this._frame.getTrayIcon().displayMessage("Bidibul", "Je suis caché. Double-clique sur cette icone lorsque tu voudras me faire réapparaître !", TrayIcon.MessageType.INFO);
	             int state = this._frame.getExtendedState(); // get the current state
	             state = state & Frame.ICONIFIED;
	             this._frame.setVisible(false);
	         } catch (AWTException e1) {
	        	 System.out.println("no");
	             System.err.println(e1);
	         }
		}
		//Déplacer le bidibul
    }

	@Override
    public void itemStateChanged(ItemEvent e) {
        JMenuItem source = (JMenuItem)(e.getSource());
		if(source.getText() == ALWAYS_ON_TOP) {
			BidibulProperties p = new BidibulProperties("global");
        	if(e.getStateChange() == ItemEvent.SELECTED) {
    			this._frame.setAlwaysOnTop(true);
    			// Sauvegarde
    			p.put("alwaysOnTop", "1");
    			p.save();
        	}
        	else{
    			this._frame.setAlwaysOnTop(false);
    			// Sauvegarde
    			p.put("alwaysOnTop", "0");
    			p.save();
        	}
		}
		else if (source.getText() == BIDIBUL_MOVE){
			this._frame.toggleMoveableFrame();
		}
    }
}
