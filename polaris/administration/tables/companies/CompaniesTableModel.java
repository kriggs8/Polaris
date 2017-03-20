package polaris.administration.tables.companies;

import polaris.administration.tables.ColumnRecordSet;
import polaris.administration.tables.GenericTableModel;
import polaris.frame.PolarisUI;

import javax.swing.event.TableModelListener;

/**
 * This class creates the Companies Table Model to be used
 * for the Main Table
 *
 * @Author TCI-Krista Riggs
 * Created 3/6/2017.
 */
public class CompaniesTableModel extends GenericTableModel
{
    /**
     * Create the Companies Table Model constructor
     *
     * @param crs - the column record set
     */
    public CompaniesTableModel(ColumnRecordSet crs)
    {
        super(crs, PolarisUI.getMessage("CF_MAINTENANCE_PANEL", PolarisUI.getMessage("COMPANY")));
    }

    /**
     * Sets whether the cell is editable
     *
     * @param row - the row of the cell
     * @param col - the column of the cell
     * @return - true/false if editable
     */
    public boolean isCellEditable(int row, int col)
    {
        return false;
    }

    /**
     * Does nothing since the main table is not editable
     *
     * @param v
     * @param row
     * @param col
     */
    public void setValueAt(Object v, int row, int col)
    {
    }

    /**
     * Gets the column count
     *
     * @return - number of columns
     */
    public int getColumnCount()
    {
        return CompaniesTableColumnModel.names.length;
    }

    /**
     * Gets the name of the column
     *
     * @param column - the column
     * @return - the name of the column
     */
    public String getColumnName(int column)
    {
        return CompaniesTableColumnModel.names[column];
    }

    /**
     * Gets the class of the column
     *
     * @param col - the column
     * @return - the class
     */
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
