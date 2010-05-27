package tools;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import utils.BidibulModule;
import utils.iDroppable;
import views.PieMenuPanel;

public class MyFileTransferHandler extends TransferHandler {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private PieMenuPanel _piePanel;
	private ArrayList<BidibulModule> _listeModules;
	private ArrayList<BidibulModule> _listeModulesDroppable;
	private ArrayList<String> _listeFichier;

	public MyFileTransferHandler(PieMenuPanel pieMenuPanel,
			ArrayList<BidibulModule> listeModules) {
		_piePanel = pieMenuPanel;
		_listeModules = new ArrayList<BidibulModule>();
		_listeModulesDroppable = new ArrayList<BidibulModule>();
		_listeFichier = new ArrayList<String>();
		_listeModules = listeModules;
	}

	/**
	 * @see javax.swing.TransferHandler#canImport(javax.swing.JComponent,
	 *      java.awt.datatransfer.DataFlavor[])
	 */
	@Override
	public boolean canImport(JComponent arg0, DataFlavor[] arg1) {
		for (int i = 0; i < arg1.length; i++) {
			DataFlavor flavor = arg1[i];
			if (flavor.equals(DataFlavor.javaFileListFlavor)) {
				System.out.println("canImport: JavaFileList FLAVOR: " + flavor);
				return true;
			}
			if (flavor.equals(DataFlavor.stringFlavor)) {
				System.out.println("canImport: String FLAVOR: " + flavor);
				return true;
			}
			System.err.println("canImport: Rejected Flavor: " + flavor);
		}
		// Didn't find any that match, so:
		return false;
	}

	/**
	 * Do the actual import.
	 *
	 * @see javax.swing.TransferHandler#importData(javax.swing.JComponent,
	 *      java.awt.datatransfer.Transferable)
	 */
	@Override
	public boolean importData(JComponent comp, Transferable t) {
		_listeFichier.clear();
		DataFlavor[] flavors = t.getTransferDataFlavors();
		System.out.println("Trying to import:" + t);
		System.out.println("... which has " + flavors.length + " flavors.");
		for (int i = 0; i < flavors.length; i++) {
			DataFlavor flavor = flavors[i];
			try {
				if (flavor.equals(DataFlavor.javaFileListFlavor)) {
					System.out.println("importData: FileListFlavor");

					List l = (List) t
							.getTransferData(DataFlavor.javaFileListFlavor);
					Iterator iter = l.iterator();
					while (iter.hasNext()) {
						File file = (File) iter.next();
						System.out.println("GOT FILE: "
								+ file.getCanonicalPath());
						_listeFichier.add(file.getCanonicalPath());
						// Now do something with the file...
					}

					// ----
					updateDroppableModules(flavor, _listeFichier);
					_piePanel.setListeFichier(_listeFichier);
					_piePanel.setIconVisible(true);
					_piePanel.getContainer().update(
							_piePanel.getContainer().getGraphics());
					// -----
					return true;
				} else if (flavor.equals(DataFlavor.stringFlavor)) {
					System.out.println("importData: String Flavor");
					String fileOrURL = (String) t.getTransferData(flavor);
					System.out.println("GOT STRING: " + fileOrURL);
					try {
						System.out.println("test URL : ");
						URL url = new URL(fileOrURL);
						_listeFichier.add(url.toString());
						// Do something with the contents...
					} catch (MalformedURLException ex) {
						System.err.println("Not a valid URL");
					}
					// now do something with the String.
					System.out.println("tri des modules: ");
					updateDroppableModules(flavor, _listeFichier);
					_piePanel.setListeFichier(_listeFichier);
					_piePanel.setIconVisible(true);
					_piePanel.getContainer().update(
							_piePanel.getContainer().getGraphics());

				} else {
					System.out.println("importData rejected: " + flavor);
					// Don't return; try next flavor.
				}
			} catch (IOException ex) {
				System.err.println("IOError getting data: " + ex);
			} catch (UnsupportedFlavorException e) {
				System.err.println("Unsupported Flavor: " + e);
			}
		}
		// If you get here, I didn't like the flavor.
		Toolkit.getDefaultToolkit().beep();
		return false;
	}

	/**
	 * Construit l'ensemble des listes de modules nécessaires (générale,
	 * clickable, droppable)
	 *
	 * @param flavor
	 * @param flavor
	 *            Le type des fichiers passés en paramètres
	 */
	public void updateDroppableModules(DataFlavor flavor,
			ArrayList<String> listeFichier) {
		_listeModulesDroppable.clear();
		Iterator<BidibulModule> i = _listeModules.listIterator();
		BidibulModule tempMod;
		// Parcours de l'ensemble des modules...
		while (i.hasNext()) {
			tempMod = i.next();
			// Si le module est droppable...
			if (extends_iDroppable(tempMod)) {
				// Vérifie que le module peut agir sur le type des documents
				// drag&droppés:
				if (((iDroppable) tempMod).getAllowedFlavor().contains(flavor))
					// S'il s'agit de chaine: ajout direct du module direct:
					if (flavor.equals(DataFlavor.stringFlavor)) {
						System.out.println("travail sur des string");
						_listeModulesDroppable.add(tempMod);
					}
				// S'il s'agit de fichiers
				if (flavor.equals(DataFlavor.javaFileListFlavor)) {
					System.out.println("travail sur des fichiers");
					// Récupère la liste des extensions adminissibles par le
					// module en cours d'étude
					ArrayList<String> listeExtension = new ArrayList<String>();
					listeExtension = ((iDroppable) tempMod)
							.getAllowedExtensions();

					if (listeExtension.contains(".*")) {
						_listeModulesDroppable.add(tempMod);
					} else {

						// Récupère la liste des extensions récupérées
						String tabExtensionRecupere[] = getExtensionFromListFile(listeFichier);
						// Pour chaque extension récupéré
						for (int j = 0; j < tabExtensionRecupere.length; j++) {
							// Si l'extension se trouve dans la liste des
							// extensions acceptable
							if (listeExtension
									.contains(tabExtensionRecupere[j])) {
								// Le module est admis
								_listeModulesDroppable.add(tempMod);
							}
							System.out.println(tabExtensionRecupere[j]);
						}
					}
				}

			}
		}
		VerifList("liste des odules droppable détectés: ",
				_listeModulesDroppable);
		_piePanel.refresh(_listeModulesDroppable, 2);

	}

	/**
	 * Récupère la liste des extensions dans les fichiers passés en paramètres
	 *
	 * @param listeFichier
	 * @return
	 */
	private String[] getExtensionFromListFile(ArrayList<String> listeFichier) {
		System.out.println("recehrceh des extensions");
		ArrayList<String> listExt = new ArrayList<String>();
		Iterator<String> i = listeFichier.listIterator();
		// Parcours des fichiers...
		while (i.hasNext()) {
			String tempFile = i.next();
			// Récupération de l'extenstion
			String ext = tempFile.substring(tempFile.lastIndexOf("."));
			// Si l'extension n'a pas encore été récupérée:
			if (!listExt.contains(ext)) {
				// Ajout de l'extension
				listExt.add(ext);
			}
		}
		String ret[] = listExt.toArray(new String[listExt.size()]);
		return ret;
	}

	/**
	 * Fonction destinée à disparaître: permet de vérifier la validité des
	 * construction de List pour les modules
	 *
	 * @author Miro
	 * @return
	 */
	void VerifList(String nomListe, ArrayList<BidibulModule> maListTemp) {
		System.out.println("vérification de la liste '" + nomListe + "' : ");
		if (!maListTemp.isEmpty()) {
			Iterator<BidibulModule> i = maListTemp.listIterator();
			while (i.hasNext()) {
				System.out.println("     - " + BidibulInformation.get("name", i.next())/*i.next().getName()*/);
			}
		}
	}

	/**
	 * Renvoie true si la classe le module hérite de la classe iClickable
	 *
	 * @see BidibulModule
	 * @see ModuleLoader#loadModules()
	 * @todo Pourquoi ne pas utiliser instanceof à  la place ?
	 * @todo Mieux que "Class<?>" ?
	 */
	private boolean extends_iDroppable(BidibulModule mod) {
		Class<?>[] interfaces = mod.getClass().getInterfaces();
		for (int i = 0; i < interfaces.length; i++) {
			System.out.println("interface :" + interfaces[i].getName());

			if (interfaces[i].getName() == "utils.iDroppable")
				return true;
		}
		return false;
	}
}