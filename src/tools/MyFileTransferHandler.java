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

	public MyFileTransferHandler(PieMenuPanel pieMenuPanel) {
		_piePanel = pieMenuPanel;
		_listeFichier = new ArrayList<String>();
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

					}
				// TRAITEMENT DES FICHIERS
					// ----
					_piePanel.setFichierInfo(_listeFichier, flavor);
					_piePanel.refresh(2);
					_piePanel.setVisible(true);
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
					_piePanel.setFichierInfo(_listeFichier, flavor);
					_piePanel.refresh(2);
					_piePanel.setVisible(true);
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
}

