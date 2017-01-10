
package polaris.administration.tables.batchRoutes;

import polaris.administration.tables.ColumnRecordSet;
import polaris.administration.tables.GenericTableModel;
import polaris.administration.tables.MainPanel;
import polaris.frame.PolarisUI;


import javax.swing.*;
import javax.swing.event.TableModelListener;

/**
 *  Prototype Code - Table Model Class for BATCH_ROUTES_CONF
 *  TODO: MORE COMMENTS
 *  @Author - Jean Wurster
 */
public class BatchRoutesTableModel extends GenericTableModel
{


	public BatchRoutesTableModel(ColumnRecordSet crs)
	{
		super(crs, PolarisUI.getMessage("BATCH_ROUTE"));
	}

	public int getColumnCount()
	{
		return BatchRoutesTableColumnModel.names.length;
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
		return BatchRoutesTableColumnModel.names[column];
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
		//todo: not sure if this is needed
		super.addTableModelListener(l);
	}




}
