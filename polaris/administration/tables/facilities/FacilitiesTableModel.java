package polaris.administration.tables.facilities;

import polaris.administration.tables.ColumnRecordSet;
import polaris.administration.tables.GenericTableModel;
import polaris.frame.PolarisUI;

import javax.swing.event.TableModelListener;

/**
 * This class creates the Facilities Table Model to be used
 * for the Main Table
 *
 * @Author TCI Krista Riggs
 * Created 3/15/2017.
 */
public class FacilitiesTableModel extends GenericTableModel
{
    /**
     * Create the Facilities Table Model constructor
     *
     * @param crs - the column record set
     */
    public FacilitiesTableModel(ColumnRecordSet crs)
    {
        super(crs, PolarisUI.getMessage("CF_MAINTENANCE_PANEL", PolarisUI.getMessage("FACILITY")));
    }

    /**
     * @return - the column count
     */
    public int getColumnCount()
    {
        return FacilitiesTableColumnModel.names.length;
    }

    /**
     * @param v
     * @param row
     * @param col
     */
    public void setValueAt(Object v, int row, int col)
    {
        //do nothing in this since the main table is not editable;
    }

    /**
     * @param row
     * @param col
     * @return
     */
    public boolean isCellEditable(int row, int col)
    {
        return false;
    }

    /**
     * @param column
     * @return
     */
    public String getColumnName(int column)
    {
        return FacilitiesTableColumnModel.names[column];
    }

    /**
     * @param col
     * @return
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
