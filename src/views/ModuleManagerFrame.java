package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;

import tools.ModuleLoader;

// FIXME Ouverture de fenêtre multiple
public class ModuleManagerFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JButton _btnAbout;
	private ListSelectionModel _selectionModelTable;
/*
  // @todo Tentative d'ouverture multiple d'une même fenêtre... fail!
	private static ModuleManagerFrame _instance;

	public static ModuleManagerFrame getInstance() {
		if(_instance == null)
			_instance = new ModuleManagerFrame();
		else {
			_instance.setAlwaysOnTop(true);
			_instance.setAlwaysOnTop(false);
		}
		return _instance;
	}
*/
	public ModuleManagerFrame() {
		super("Bidibul - Gestion des modules");

		// AFFICHAGE DU TABLEAU
		// --------------------

		JTable table = new JTable(new ModuleManagerTableModel());

		// On met le tableau dans un JScrollPane pour pouvoir scroller dedans
		JScrollPane scroll_pane = new JScrollPane(table);

		// Suppression de la bordure du JScrollPane
		scroll_pane.setBorder(new MatteBorder(null));

		// On n'affiche pas le header
		table.setTableHeader(null);

		// Les cases font 2 lignes de hauteur
		table.setRowHeight(table.getRowHeight() * 2);

		TableColumnModel tcm = table.getColumnModel();

		tcm.getColumn(0).setPreferredWidth(table.getRowHeight());
		tcm.getColumn(0).setMinWidth(table.getRowHeight());
		tcm.getColumn(0).setMaxWidth(table.getRowHeight());

		tcm.getColumn(1).setPreferredWidth(350);
		table.setGridColor(table.getSelectionBackground());

		// La colonne 1 a un CellRenderer spécial, pour avoir 2 lignes
		tcm.getColumn(1).setCellRenderer(new ModuleManagerMultilineCellRenderer());

		// Dimensions : 5 modules de haut
		table.setPreferredScrollableViewportSize(
				new Dimension(
						table.getPreferredSize().width,
						table.getRowHeight() * 5
				)
		);

		// Changement du mode de sélection
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Force le tableau à utiliser tout le panel
		table.setFillsViewportHeight(true);

		_selectionModelTable = table.getSelectionModel();
		_selectionModelTable.addListSelectionListener(new ModuleTableSelectionListener());

		// AFFICHAGE DES BOUTONS
		// ---------------------

		JPanel pan_line_end = new JPanel();

		// Bouton "A propos"
		_btnAbout = new JButton("A propos du module");
		_btnAbout.setEnabled(false);
		_btnAbout.addActionListener(new AboutActionListener());

		// Bouton "Fermer"
		JButton btn_close = new JButton("Fermer");
		btn_close.addActionListener(new ActionListener() { // Fermeture de la fenêtre
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				setVisible(false);
			}
		});

		pan_line_end.setLayout(new BoxLayout(pan_line_end, BoxLayout.X_AXIS));
		pan_line_end.add(_btnAbout);
		pan_line_end.add(Box.createHorizontalGlue());
		pan_line_end.add(btn_close);
		pan_line_end.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		// ON MET TOUT ÇA DANS LE PANEL
		// ----------------------------

		this.add(scroll_pane, BorderLayout.CENTER); // Tableau
		this.add(pan_line_end, BorderLayout.PAGE_END); // Boutons

		this.pack();
		this.setVisible(true);
	}

	public class ModuleTableSelectionListener implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			_btnAbout.setEnabled(true);
		}
	}

	public class AboutActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(!_selectionModelTable.isSelectionEmpty()) {
				int index = _selectionModelTable.getMinSelectionIndex();
				new AboutModuleDialog(
						ModuleLoader
						.getInstance()
						.getListAllModules().get(index));
			}
		}

	}
}