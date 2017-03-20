package polaris.administration.tables.movements;

import polaris.administration.tables.ColumnRecordSet;
import polaris.administration.tables.GenericTableModel;
import polaris.frame.PolarisUI;

import javax.swing.event.TableModelListener;

/**
 * Table Model Class for MOVEMENT_CONF
 * Created by  TCI-Waleed Elsaid on  1/9/2017
 */
public class MovementsTableModel  extends GenericTableModel {
    public MovementsTableModel(ColumnRecordSet crs) {
        super(crs, PolarisUI.getMessage("MOVEMENT"));
    }

    public int getColumnCount() {
        return MovementsTableColumnModel.names.length;
    }

    public void setValueAt(Object v, int row, int col) {
        //do nothing in this since the main table is not editable;
    }

    /**
     * @param row
     * @param col
     * @return a boolean indicating if the cell is editable
     */


    public boolean isCellEditable(int row, int col) {
        return false;
    }

    /**
     * Created by  TCI-Waleed Elsaid on  1/9/2017
     * @param column, the column index
     * @return the string name of the column
     */
    public String getColumnName(int column) {
        return MovementsTableColumnModel.names[column];
    }

    /**
     * Created by  TCI-Waleed Elsaid on  1/9/2017
     * @param col the col index
     * @return the reflection API, class object implementing the input column at the given index
     */
    public Class getColumnClass(int col) {
        if (col == 0)
            return String.class;
        else
            return getValueAt(0, col).getClass();
    }


    public void addTableModelListener(TableModelListener l) {
        super.addTableModelListener(l);
    }

    public void apply(String filter) {
        this.apply(filter, -1L);
        this.targetRC = new int[this.resultSet.size()][this.getColumnCount()];
    }
}
