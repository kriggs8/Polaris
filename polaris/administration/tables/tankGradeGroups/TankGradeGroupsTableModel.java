package polaris.administration.tables.tankGradeGroups;

import javax.swing.event.TableModelListener;

import polaris.administration.tables.ColumnRecordSet;
import polaris.administration.tables.GenericTableModel;
import polaris.frame.PolarisUI;

/**
 * Table Model Class for TANK_GRADE_GROUP_CONF
 * Create the methods available for the TankGradeGroupsTableModel
 *
 * @Author - Krista Riggs(TCI) @ 1/10/17
 */
public class TankGradeGroupsTableModel extends GenericTableModel
{

    //create the constructor
    public TankGradeGroupsTableModel(ColumnRecordSet crs)
    {
        super(crs, PolarisUI.getMessage("TANK_GRADE_GROUP"));
    }

    //method to count the number of columns
    @Override
    public int getColumnCount()
    {
        return TankGradeGroupsTableColumnModel.names.length;
    }

    //method so set the value, main table is not editable
    @Override
    public void setValueAt(Object v, int row, int col)
    {
        // do nothing in this since the main table is not editable;
    }

    //method to set whether the cell is editable
    @Override
    public boolean isCellEditable(int row, int col)
    {
        return false;
    }

    //method to retrieve the column name
    @Override
    public String getColumnName(int column)
    {
        return TankGradeGroupsTableColumnModel.names[column];
    }

    //method to return the class for associated column
    @Override
    public Class getColumnClass(int col)
    {
        if (col == 0)
        {
            return String.class;
        } else
        {
            return getValueAt(0, col).getClass();
        }
    }

    //method for the table model listener
    @Override
    public void addTableModelListener(TableModelListener l)
    {
        super.addTableModelListener(l);
    }
}