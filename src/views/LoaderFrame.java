package views;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import tools.ModuleLoader;
import utils.BidibulModule;

/**pas
 * Cette frame permet simplement
 * @author Miroslav
 *
 */
public class LoaderFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private List<BidibulModule> listeModule;

	public LoaderFrame()
	{
		this.setUndecorated(true);

		JLabel indic = new JLabel("Chargement du bidibul en cours!");
		indic.setBounds(10, 10, 100, 20);
		this.getContentPane().add(indic);

		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		progressBar.setBounds(10, 40, 100, 20);
		this.getContentPane().add(progressBar);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(
		        (screenSize.width-this.getWidth())/2,
		        (screenSize.height-this.getHeight())/2
		        );

		this.setVisible(true);
		listeModule = new ArrayList<BidibulModule>();
		listeModule = ChargerModule();
		if (listeModule.size() != 0)
			new MainFrame();
		else
			new MainFrame(); // @todo JA a ajouté pour cause de crash complet ( = 0 modules)
		this.dispose();
		//if (listeModule != null)

}

	public List<BidibulModule> getAvailableModule() {
		return listeModule;
	}

	/**
	 * Chargement des modules
	 * @return
	 */
	public List<BidibulModule> ChargerModule() {
		if (ModuleLoader.getInstance().loadModules())
		{
			List<BidibulModule> modules = ModuleLoader.getInstance().getListActiveModules();
			return modules;
		}
		else
			return null;
	}
}
