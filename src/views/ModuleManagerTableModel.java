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

		String module_name;
		String module_description;
		Boolean module_is_active;

		Iterator<Class<BidibulModule>> it = ModuleLoader.getInstance().getListAllModules().iterator();
		while (it.hasNext()) {
			module = it.next();

			// @todo : Les champs par défaut sont amenés à disparaitre
			// Au pire, ils doivent être affectés à la création
			module_name = BidibulInformation.get("name", module);
			if(module_name == null) module_name = "<Module sans nom>";
			module_description = BidibulInformation.get("description", module);
			if(module_description == null) module_description = "<Module sans description>";
			module_is_active = ModuleLoader.getInstance().isActive(module);

			if(module_name != null && module_description != null)
				data.add(new Object[] {
					new Boolean(module_is_active),
					new String[] {module_name, module_description}
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