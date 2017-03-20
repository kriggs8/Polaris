package polaris.administration.tables.contacts;

import polaris.administration.tables.ColumnRecordSet;
import polaris.administration.tables.GenericTableModel;
import polaris.frame.PolarisUI;

import javax.swing.event.TableModelListener;

/**
 * This class creates the Contacts Table Model to be used
 * for the Main Table
 *
 * @Author TCI - Krista Riggs
 * Created on 2/2/2017.
 * Updated 2/7/2017 TCI - Krista Riggs - Updated construction and comments
 */
public class ContactsTableModel extends GenericTableModel
{
    /**
     * Create the Contacts Table Model constructor
     *
     * @param crs - the column record set
     */
    public ContactsTableModel(ColumnRecordSet crs)
    {
        super(crs, PolarisUI.getMessage("CF_MAINTENANCE_PANEL", PolarisUI.getMessage("CONTACT")));
    }

    /**
     * @return - the column count
     */
    public int getColumnCount()
    {
        return ContactsTableColumnModel.names.length;
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
        return ContactsTableColumnModel.names[column];
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
