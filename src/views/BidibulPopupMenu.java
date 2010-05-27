package views;

import java.awt.AWTException;
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

/**
 *
 * @author Jérémie ASTORI
 *
 */
public class BidibulPopupMenu extends JPopupMenu implements ActionListener,
															ItemListener {
	private static final long serialVersionUID = 1L;

	private MainFrame _frame;

	private static final String MODULE_MANAGEMENT = "Activer/Désactiver un module";
	private static final String ALWAYS_ON_TOP = "Toujours au premier plan";
	private static final String BIDIBUL_HIDE = "Cacher le bidibul";
	private static final String EXIT = "Quitter Bidibul";

	public BidibulPopupMenu(MainFrame frame) {
	    super();

	    this._frame = frame;

	    // Gestion des modules
	    JMenuItem itmModuleManagement = new JMenuItem(MODULE_MANAGEMENT);
	    itmModuleManagement.setMnemonic(KeyEvent.VK_M);
	    itmModuleManagement.addActionListener(this);
	    this.add(itmModuleManagement);

	    // Toujours au premier plan
	    JCheckBoxMenuItem itmAlwaysOnTop = new JCheckBoxMenuItem(ALWAYS_ON_TOP);
	    itmAlwaysOnTop.setMnemonic(KeyEvent.VK_T);
	    itmAlwaysOnTop.addItemListener(this);
	    this.add(itmAlwaysOnTop);

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
	            KeyEvent.VK_F4, ActionEvent.ALT_MASK)); // Ne fonctionne pas, c'est juste pour information
	    itmExit.addActionListener(this);
	    this.add(itmExit);
	}

	/**
	 * Action à lancer par défaut à la fermeture du programme
	 */
	public static void exit() {
		Flash.notice("A bientôt !");

		new Thread(new Runnable() {
			public void run() {
				try {
					// @todo 500 est une valeur de développement, 3000 une valeur de production
					Thread.sleep(500);
					//Thread.sleep(3000);
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
		if(source.getText() == MODULE_MANAGEMENT) {
			// @todo : menu des modules
			System.out.println(MODULE_MANAGEMENT);
			new ModuleManagerFrame();
		}
		else if(source.getText() == EXIT) {
			exit();
		}
		//Cas du systray
		else if (source.getText() == BIDIBUL_HIDE) {
			try {
	             this._frame.getSystemTray().add(this._frame.getTrayIcon());
	             this._frame.getTrayIcon().displayMessage("Bidibul", "Ton bidibul à été caché. Double-clic sur cette icone lorsque tu voudras le faire réapparaitre!", TrayIcon.MessageType.INFO);
	             //int state = this._frame.getExtendedState(); // get the current state
	             //state = state & this._frame.ICONIFIED;
	             this._frame.setVisible(false);
	         } catch (AWTException e1) {
	        	 System.out.println("no");
	             System.err.println(e1);
	         }
		}
    }

	@Override
    public void itemStateChanged(ItemEvent e) {
        JMenuItem source = (JMenuItem)(e.getSource());
		if(source.getText() == ALWAYS_ON_TOP) {
        	if(e.getStateChange() == ItemEvent.SELECTED)
    			this._frame.setAlwaysOnTop(true);
        	else
    			this._frame.setAlwaysOnTop(false);
		}
    }
}
