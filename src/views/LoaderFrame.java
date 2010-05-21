package views;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import tools.ModuleLoader;
import utils.BidibulModule;

/**
 * Cette frame permet simplement
 * @author Miroslav
 *
 */
public class LoaderFrame extends JFrame {

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

		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

		this.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocation(
		        (screenSize.width-this.getWidth())/2,
		        (screenSize.height-this.getHeight())/2
		        );

		this.setVisible(true);
		listeModule = new ArrayList<BidibulModule>();
		listeModule = ChargerModule();
		if (listeModule.size() != 0)
			new MainFrame((ArrayList<BidibulModule>) listeModule);
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
			List<BidibulModule> modules = ModuleLoader.getInstance().getListModules();
			return modules;
		}
		else
			return null;
	}
}
