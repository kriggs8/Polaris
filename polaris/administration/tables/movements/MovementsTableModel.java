package polaris.administration.tables.movements;

import polaris.administration.tables.ColumnRecordSet;
import polaris.administration.tables.GenericTableModel;
import polaris.frame.PolarisUI;

import javax.swing.event.TableModelListener;

/**
 * Table Model Class for MOVEMENT_CONF
 * @Author: Waleed Elsaid on 1/9/2017.
 */
public class MovementsTableModel  extends GenericTableModel
{
    public MovementsTableModel(ColumnRecordSet crs)
    {
        super(crs, PolarisUI.getMessage("MOVEMENT"));
    }
    public int getColumnCount()
    {
        return MovementsTableColumnModel.names.length;
    }

    public void setValueAt(Object v, int row, int col)
    {
        //do nothing in this since the main table is not editable;
    }
    public boolean isCellEditable(int row, int col)
    {
        return false;
    }


    public String getColumnName(int column)
    {
        return MovementsTableColumnModel.names[column];
    }


    public Class getColumnClass(int col)
    {
        if (col==0)
            return String.class;
        else
            return getValueAt(0, col).getClass();
    }


    public void addTableModelListener(TableModelListener l)
    {
        super.addTableModelListener(l);
    }



}
