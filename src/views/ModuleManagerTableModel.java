package views;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.table.AbstractTableModel;

import tools.BidibulInformation;
import tools.ModuleLoader;
import utils.BidibulModule;

class ModuleManagerTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	String[] columnNames = {"Actif","Module"};

	ArrayList<Object[]> data = new ArrayList<Object[]>();

	public ModuleManagerTableModel() {
		Class<BidibulModule> module;

		Iterator<Class<BidibulModule>> it = ModuleLoader.getInstance().getListAllModules().iterator();
		while (it.hasNext()) {
			module = it.next();

			data.add(new Object[] {
				new Boolean(ModuleLoader.getInstance().isActive(module)),
				new String[] {
					BidibulInformation.getName(module),
					BidibulInformation.getDescription(module)
				}
			});
		}
	}

	@Override
	public int getColumnCount() {
        return columnNames.length;
	}

	@Override
	public int getRowCount() {
        return data.size();
	}

	@Override
	public String getColumnName(int col) {
        return columnNames[col];
    }

	@Override
    public Object getValueAt(int row, int col) {
		return data.get(row)[col];
    }

    /**
     * Permet l'affichage d'un checkbox plutôt que "true/false"
     */
	@Override
    public Class<?> getColumnClass(int c) {
        return getValueAt(0, c).getClass();
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
			data.get(row)[0] = value;
			System.out.println("Module " + row + " est maintenant activé ? " + value);

		//	ModuleLoader.getInstance().getSetAllModules().

	//		ModuleLoader.getInstance().startModule(c);
			if (value.equals(true))
				ModuleLoader.getInstance().startModule(row);
			else
				ModuleLoader.getInstance().stopModule(row);

		}
    }
}