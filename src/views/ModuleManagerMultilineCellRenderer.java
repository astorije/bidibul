package views;

import java.awt.Component;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

class ModuleManagerMultilineCellRenderer extends JPanel implements TableCellRenderer {
	private static final long serialVersionUID = 1L;

	public ModuleManagerMultilineCellRenderer() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setOpaque(true);
	}

	// Implementation de l'interface TableCellRenderer
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		removeAll();
		invalidate();

		// Marges
		setBorder(new EmptyBorder(2, 2, 2, 2));

		// Do nothing if no value
		if (value == null || table == null)
			return this;

		if (isSelected) {
			setForeground(table.getSelectionForeground());
			setBackground(table.getSelectionBackground());
		} else {
			setForeground(table.getForeground());
			setBackground(table.getBackground());
		}

		String[] values = (String[]) value;

		JLabel lbl_name = new JLabel();
		lbl_name.setText(values[0].toString());
		lbl_name.setFont(new Font(lbl_name.getFont().getName(), Font.BOLD, lbl_name.getFont().getSize()));
		add(lbl_name);

		JLabel lbl_description = new JLabel();
		lbl_description.setText(values[1].toString());
		lbl_description.setFont(new Font(lbl_description.getFont().getName(), Font.PLAIN, lbl_description.getFont().getSize()));
		add(lbl_description);

		return this;
	}
}