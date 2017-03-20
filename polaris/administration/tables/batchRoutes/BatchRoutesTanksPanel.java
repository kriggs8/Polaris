package polaris.administration.tables.batchRoutes;

import polaris.administration.tables.*;
import polaris.frame.ContextHelpAction;
import polaris.frame.PolarisUI;
import polaris.util.PolarisJTable;
import polaris.util.TableSorter;

import javax.swing.*;
import java.awt.*;

/**
 * Creates the Tanks Panel for a configuration screen.
 */
public class BatchRoutesTanksPanel extends GenericTabPanel
{
    public BatchRoutesTanksTableModel brTanksTableModel;

    /**
     * Instantiates the Tanks Panel.
     *
     * @param mainPanel
     * @throws Exception
     */
    public BatchRoutesTanksPanel(MainPanel mainPanel) throws Exception
    {
        super(mainPanel);
    }

    @Override
    public String[] getColumnNames()
    {
        //gets a list of column names to display on the table.
        return new String[]{"EDIT_MODE", "REC_DEL_ID", "TANK_ID"};
    }

    @Override
    public String[] getTableNames()
    {
        //gets a list of db tables to display on the table.
        return new String[]{"BATCH_ROUTE_TANK_XREF"};
    }

    @Override
    public String[] getForeignKeyConstColNames()
    {
        //gets a list of foreign Key Col Names.  In this example it only has one db table in the list
        //therefore it should only have one foreign Key Col Name.
        return new String[]{"BATCH_ROUTE_ID"};
    }

    @Override
    public String[][] getSelectColNames()
    {
        //gets a list of table columns to get table defintions.
        //in this instance it grabs all column names from the tables.
        return new String[][]{{}, {}};
    }

    @Override
    public String getOrderBy()
    {
        //Gets the column name to sort by.
        //in this instance, no column to sort by
        return null;
    }

    /**
     * Creates the Panel and the controls/components in the panel.
     */
    @Override
    public void createPanel()
    {


        //populate the table's column definitions.
        tabCRS = new ColumnRecordSet(tableNames, selectColNames, foreignKeyConstColNames);
        //creates the tables TableModel
        brTanksTableModel = new BatchRoutesTanksTableModel(tabCRS, columnNames);
        //creates the TableColumnModel
        GenericTableColumnModel brMetersTableColumnModel = new GenericTableColumnModel(columnNames, tabCRS);

        //creates the table.
        tabTable = new GenericTable(new TableSorter(brTanksTableModel));
        tabTable.setMainPanel(mainPanel);
        tabTable.setColumnModel(brMetersTableColumnModel);
        tabTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabTable.setFillsViewportHeight(true);
        tabTable.setIntercellSpacing(new Dimension(0, 0));

        //makes the Panel scrollable.
        JScrollPane tableScrollPanel = new JScrollPane(tabTable);
        tableScrollPanel.setPreferredSize(new Dimension(124, 124));

        //gets table's ResultRecordSet
        tabRS = brTanksTableModel.getResultSet();

        //change listener for the main table.  If users selects a different row
        //on the table grid. it will trigger this listener on the ResultRecordSet.
        ListSelectionModel listSelectionModel = (ListSelectionModel) tabTable.getSelectionModel();
        listSelectionModel.addListSelectionListener(tabRS);

        //listener for any changes to the table Model.
        brTanksTableModel.addTableModelListener(tabRS);

        //Associates the JTable to the ResultRecordSet.
        tabRS.setTable(tabTable);


        this.addToThisPanel(0, 0, 1, tableScrollPanel, true, true);

    }

    /**
     * set Help Topic Id
     */
    @Override
    public void setHelp()
    {
        this.mainPanel.setContextHelpTopicId("BatchRoutesConfigurationModule_TanksTab");
    }

    @Override
    protected void setValidators()
    {
        ColumnRecordSet crs = this.brTanksTableModel.getColumnRecordSet();

        // Loop through the columnNames and set the default validator
        for (int j = 0; j < columnNames.length; j++)
        {
            for (int i = 0; i < crs.size(); i++)
            {
                // Validate defaults
                Column column = crs.elementAt(i);

                if (column.columnName.equals(columnNames[j]))
                {
                    // Set the default validator
                    column.setValidator(defaultValidator(column));

                    // Found this columnName, break and search for next one
                    break;
                }
            }
        }
    }





    /**
     * gets the where clause for all combo boxes of the panel.
     * TANK_ID is the only combobox that requires a filter for inactive indicator flag.
     * REC_DEL_ID has no filter.
     */
    public String getDBNameFactoryWhereClause(boolean activeView,String columnName, String lookUpTable, TableRow mainTableRow)
    {

        String whereClause = "";
        if (mainTableRow == null || lookUpTable.contains("_DESC"))
        {
            return whereClause;
        }


        //if view is inactive, get all records dont filter.
        if (!activeView)
        {
            return whereClause;
        }


        if (lookUpTable.equals("TANK_CONF"))
        {
            //where clause for the model factory.
            //if this is in readonly/InactiveView
            //then get all records from the database
            //else if viewing in activeView/not readonly
            //then get only records that are active.
            whereClause = ComponentFactory.getDBIdNameFactoryWhereClause(activeView);

            //if the look up table is METER_CONF and valid main table row
            //then create model factory.

            long recLocId = -1;
            long delLocId = -1;

            //get the main table's location values.
            Field recLocField = resultRS.getField("REC_LOCATION_ID", mainTableRow);
            Field delLocField = resultRS.getField("DEL_LOCATION_ID", mainTableRow);

            //get the long values from the fields.
            recLocId = Long.parseLong(
                    recLocField == null || recLocField.getValue() == null ? "-1" : recLocField.getValue());
            delLocId = Long.parseLong(
                    delLocField == null || delLocField.getValue() == null ? "-1" : delLocField.getValue());

            //store the location IDs in a string.
            String locIDs = "";

            if (columnName.equals("TANK_ID"))
            {
                //if component does not have of a database name
                //ie.. this combobox is related to table
                locIDs = recLocId == -1 ? "" : "" + recLocId;

                if (locIDs.isEmpty())
                {
                    locIDs = delLocId == -1 ? "" : "" + delLocId;
                } else
                {
                    locIDs += ",";

                    locIDs += delLocId == -1 ? "" : "" + delLocId;
                }

            }

            if (!locIDs.isEmpty())
            {
                whereClause += " AND LOCATION_ID IN (" + locIDs + ")";
            }
        }
        return whereClause;

    }

    public class BatchRoutesTanksTableModel extends GenericTableModel
    {


        public BatchRoutesTanksTableModel(ColumnRecordSet crs, String[] names)
        {
            super(crs, names, false, PolarisUI.getMessage("CF_TANKS_PANEL", PolarisUI.getMessage("BATCH_ROUTE")));


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


