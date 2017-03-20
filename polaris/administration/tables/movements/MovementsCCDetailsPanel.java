package polaris.administration.tables.movements;

import polaris.administration.tables.*;
import polaris.frame.PolarisUI;
import polaris.util.TableSorter;
import javax.swing.*;
import java.awt.*;

/**
 * Created by TCI-Waleed Elsaid on 02/07/2017.
 */
public class MovementsCCDetailsPanel extends GenericTabPanel {
    private MovementsCCDetailsTableModel mvCCDetailsTableModel;

    public MovementsCCDetailsPanel(MainPanel mainPanel) throws Exception
    {
        super(mainPanel);
    }

    /**
     * Created by TCI-Waleed Elsaid on 02/07/2017.
     * the column names that will show up in the tab table
     * @return
     */
    @Override
    public String[] getColumnNames()
    {
        return new String[]{"EDIT_MODE", "REC_DEL_ID", "CON_CARRIER_DETAIL_ID"};
    }

    /**
     * Created by TCI-Waleed Elsaid on 02/07/2017.
     * The table names used in the queries for the tab
     * @return
     */
    @Override
    public String[] getTableNames()
    {
        return new String[]{"MOVEMENT_CON_CARR_DETAIL_XREF"};
    }

    /**
     * Created by TCI-Waleed Elsaid on 02/07/2017.
     * Foreign keys that link MOVEMENT_CON_CARR_DETAIL_XREF table to MOVEMENT_CONF
     * @return
     */
    @Override
    public String[] getForeignKeyConstColNames()
    {
        return new String[]{"MOVEMENT_ID"};
    }

    /**
     * Created by TCI-Waleed Elsaid on 02/07/2017.
     * Columns that get populated when selecting Con Carrier
     * @return
     */
    @Override
    public String[][] getSelectColNames()
    {
        return new String[][]{{}, {}};
    }


    /**
     * Created by TCI-Waleed Elsaid on 02/07/2017.
     * The order by clause used to in the query to populate the select columns from the Conn Carrier detail
     * @return
     */
    @Override
    public String getOrderBy()
    {
        return "ORDER BY REC_DEL_ID ";
    }

    /**
     * Created by TCI-Waleed Elsaid on 02/07/2017.
     * Method called by the super constructor
     */
    @Override
    public void createPanel()
    {
        //populate the table's column definitions.
        tabCRS = new ColumnRecordSet(tableNames, selectColNames, foreignKeyConstColNames);
        //creates the tables TableModel
        mvCCDetailsTableModel = new MovementsCCDetailsPanel.MovementsCCDetailsTableModel(tabCRS, columnNames);
        //creates the TableColumnModel
        GenericTableColumnModel mvCCDetailsColumnModel = new GenericTableColumnModel(columnNames, tabCRS);

        //creates the table.
        tabTable = new GenericTable(new TableSorter(mvCCDetailsTableModel));
        tabTable.setMainPanel(mainPanel);
        tabTable.setColumnModel(mvCCDetailsColumnModel);
        tabTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabTable.setFillsViewportHeight(true);
        tabTable.setIntercellSpacing(new Dimension(0, 0));

        //makes the Panel scrollable.
        JScrollPane tableScrollPanel = new JScrollPane(tabTable);
        tableScrollPanel.setPreferredSize(new Dimension(300, 100));

        //gets table's ResultRecordSet
        tabRS = mvCCDetailsTableModel.getResultSet();

        //change listener for the main table.  If users selects a different row
        //on the table grid. it will trigger this listener on the ResultRecordSet.
        ListSelectionModel listSelectionModel = (ListSelectionModel) tabTable.getSelectionModel();
        listSelectionModel.addListSelectionListener(tabRS);

        //listener for any changes to the table Model.
        mvCCDetailsTableModel.addTableModelListener(tabRS);

        //Associates the JTable to the ResultRecordSet.
        tabRS.setTable(tabTable);

        this.addToThisPanel(0, 0, 1, 1, tableScrollPanel, true, true);

    }

    @Override
    /**
     * Created by TCI-Waleed Elsaid on 02/07/2017.
     * This method sets the validators to be called at the save action
     */
    protected void setValidators()
    {
        //sets the validator for a column in the ColumnRecordSet.
        for (int i = 0; i < tabCRS.size(); i++)
        {
            Column column = tabCRS.elementAt(i);
            if (    column.columnName.equals("REC_DEL_ID") ||
                    column.columnName.equals("CON_CARRIER_DETAIL_ID"))
            {
                column.setValidator(defaultValidator(column));
            }
        }
    }

    /**
     * Created by TCI-Waleed Elsaid on 02/07/2017.
     * The tab table model
     */
    public class MovementsCCDetailsTableModel extends GenericTableModel
    {


        public MovementsCCDetailsTableModel(ColumnRecordSet crs, String[] names)
        {
            super(crs, names, false, PolarisUI.getMessage("CF_CC_DETAILS_PANEL", PolarisUI.getMessage("MOVEMENT")));
        }

        public boolean isCellEditable(int row, int col)
        {
            boolean colEditable = super.isCellEditable(row,col);
            //checks if the main table is readonly.
            //if read only make sure all related tab fields and tables are not editable as well.
            boolean editable = resultRS.isReadOnly() || tabRS.isReadOnly() ? false : true&&colEditable;
            return editable;
        }
    }



}
