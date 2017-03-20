package polaris.administration.tables.tankGradeGroups;

import polaris.administration.tables.ColumnRecordSet;
import polaris.administration.tables.GenericTableModel;
import polaris.frame.PolarisUI;

import javax.swing.event.TableModelListener;

/**
 * Table Model Class for TANK_GRADE_GROUP_CONF
 * Create the methods available for the TankGradeGroupsTableModel
 *
 * @Author - TCI - Krista Riggs
 * Created  1/10/17
 * Updated  2/6/17 TCI - Krista Riggs - added comments
 */
public class TankGradeGroupsTableModel extends GenericTableModel
{
    /**
     * create the constructor
     *
     * @param crs - The column record set
     */
    public TankGradeGroupsTableModel(ColumnRecordSet crs)
    {
        super(crs, PolarisUI.getMessage("TANK_GRADE_GROUP"));
    }

    /**
     * method to count the number of columns
     *
     * @return - the column count
     */
    @Override
    public int getColumnCount()
    {
        return TankGradeGroupsTableColumnModel.names.length;
    }

    /**
     * method to set the value, main table is not editable
     *
     * @param v   - the object value
     * @param row - the row
     * @param col - the column
     */
    @Override
    public void setValueAt(Object v, int row, int col)
    {
        // do nothing in this since the main table is not editable;
    }

    /**
     * method to set whether the cell is editable
     *
     * @param row - the row
     * @param col - the column
     * @return - is editable T/F
     */
    @Override
    public boolean isCellEditable(int row, int col)
    {
        return false;
    }

    /**
     * method to retrieve the column name
     *
     * @param column - the column
     * @return - the column name
     */
    @Override
    public String getColumnName(int column)
    {
        return TankGradeGroupsTableColumnModel.names[column];
    }

    /**
     * method to return the class for associated column
     *
     * @param col - the column
     * @return - the column class
     */
    @Override
    public Class getColumnClass(int col)
    {
        if (col == 0)
        {
            return String.class;
        }
        else
        {
            return getValueAt(0, col).getClass();
        }
    }

    /**
     * method for the table model listener
     *
     * @param l - the table model listener
     */
    @Override
    public void addTableModelListener(TableModelListener l)
    {
        super.addTableModelListener(l);
    }
}