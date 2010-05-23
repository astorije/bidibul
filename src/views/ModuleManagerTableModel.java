package views;

import javax.swing.table.AbstractTableModel;

class ModuleManagerTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	String[] columnNames = {"Actif","Module"};

	Object[][] data = {
		    {new Boolean(false), new String[] {"Module 1", "Description du module 1"}},
		    {new Boolean(true), new String[] {"Module 2", "Description du module 2"}},
		    {new Boolean(true), new String[] {"Module 2 bis", "Description du module 2"}},
		    {new Boolean(false), new String[] {"Module 3", "Description du module 3"}},
		};

	@Override
	public int getColumnCount() {
        return this.columnNames.length;
	}

	@Override
	public int getRowCount() {
        return this.data.length;
	}

	@Override
	public String getColumnName(int col) {
        return this.columnNames[col];
    }

	@Override
    public Object getValueAt(int row, int col) {
        return this.data[row][col];
    }

    /**
     * Permet l'affichage d'un checkbox plutôt que "true/false"
     */
	@Override
    public Class<?> getColumnClass(int c) {
        return this.getValueAt(0, c).getClass();
    }

	@Override
    public boolean isCellEditable(int row, int col) {
        if (col == 0)
            return true;
        else return false;
    }

    @Override
	public void setValueAt(Object value, int row, int col) {
		if(col == 0) {
			this.data[row][0] = value;
			System.out.println("Module " + row + " est maintenant activé ? " + value);
		}
    }
}