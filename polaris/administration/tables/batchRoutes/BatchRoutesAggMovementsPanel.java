package polaris.administration.tables.batchRoutes;

import polaris.administration.tables.*;
import polaris.db.ConnectionPool;
import polaris.db.DBConn;
import polaris.frame.ContextHelpAction;
import polaris.frame.PolarisUI;
import polaris.util.TableSorter;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Creates the Aggregate Movements Panel for a configuration screen.
 */
public class BatchRoutesAggMovementsPanel extends GenericTabPanel

{


    public BatchRoutesAggMovementsTableModel brAggMovementsTableModel;


    /**
     * Instantiates the Aggregate Movement Panel.
     *
     * @param mainPanel
     * @throws Exception
     */
    public BatchRoutesAggMovementsPanel(MainPanel mainPanel) throws Exception
    {
        super(mainPanel);


    }

    @Override
    public String[] getColumnNames()
    {
        //gets a list of columns to display on the table.
        return new String[]{"EDIT_MODE","BATCH_ROUTE_AGG_MOVEMENT_XREF.AGGREGATE_MOVEMENT_ID", "AGGREGATE_MOVEMENT_CONF.REC_DEL_ID",
                            "AGGREGATE_MOVEMENT_CONF.REC_LOCATION_ID", "AGGREGATE_MOVEMENT_CONF.REC_CON_CARRIER_ID",
                            "AGGREGATE_MOVEMENT_CONF.DEL_LOCATION_ID", "AGGREGATE_MOVEMENT_CONF.DEL_CON_CARRIER_ID",
                            "AGGREGATE_MOVEMENT_CONF.SHIPPER_ID", "AGGREGATE_MOVEMENT_CONF.GRADE_ID",
                            "AGGREGATE_MOVEMENT_CONF.DESCRIPTION"};
    }

    @Override
    public String[] getTableNames()
    {
        //gets a list of db tables to display on the table
        return new String[]{"BATCH_ROUTE_AGG_MOVEMENT_XREF", "AGGREGATE_MOVEMENT_CONF"};
    }

    @Override
    public String[] getForeignKeyConstColNames()
    {
        //stores a list of foreign Key Col Names.  In this instance two tables are displayed in the grid.
        //therefore it should  have two foreign key col names.
        return new String[]{"BATCH_ROUTE_ID", "AGGREGATE_MOVEMENT_ID"};
    }

    @Override
    public String[][] getSelectColNames()
    {
        return new String[][]{{}, {"REC_DEL_ID", "REC_LOCATION_ID", "REC_CON_CARRIER_ID", "DEL_LOCATION_ID",
                                   "DEL_CON_CARRIER_ID", "SHIPPER_ID", "GRADE_ID", "DESCRIPTION"}};
    }

    @Override
    public String getOrderBy()
    {
        //Gets the column name to sort by.
        return " ORDER BY AGGREGATE_MOVEMENT_CONF.DESCRIPTION";
    }

    @Override
    public void createPanel()
    {

        //populate the table's column definitions.
        tabCRS = new ColumnRecordSet(tableNames, selectColNames, foreignKeyConstColNames);
        //creates the tables TableModel
        brAggMovementsTableModel = new BatchRoutesAggMovementsTableModel(tabCRS, columnNames);
        //creates the TableColumnModel
        GenericTableColumnModel brAggMovementsTableColumnModel = new GenericTableColumnModel(columnNames, tabCRS);

        //creates the table.
        tabTable = new GenericTable(new TableSorter(brAggMovementsTableModel));
        tabTable.setMainPanel(mainPanel);
        tabTable.setColumnModel(brAggMovementsTableColumnModel);
        tabTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabTable.setFillsViewportHeight(true);
        tabTable.setIntercellSpacing(new Dimension(0, 0));
        //makes the Panel scrollable.
        JScrollPane tableScrollPanel = new JScrollPane(tabTable);
        tableScrollPanel.setPreferredSize(new Dimension(300, 100));

        //gets table's ResultRecordSet.
        tabRS = brAggMovementsTableModel.getResultSet();


        //change listener for the main table.  If users selects a different row
        //on the table grid. it will trigger this listener on the ResultRecordSet.
        ListSelectionModel listSelectionModel = (ListSelectionModel) tabTable.getSelectionModel();
        listSelectionModel.addListSelectionListener(tabRS);

        //listener for any changes to the table Model.
        brAggMovementsTableModel.addTableModelListener(tabRS);

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
        this.mainPanel.setContextHelpTopicId("BatchRoutesConfigurationModule_AggMovementsTab");
    }

    @Override
    protected void setValidators()
    {
        ColumnRecordSet crs = this.brAggMovementsTableModel.getColumnRecordSet();

        // Loop through the columnNames and set the default validator
        for (int i = 0; i < crs.size(); i++)
        {
            // Validate defaults
            Column column = crs.elementAt(i);

            // Only validate the Agg Movement combo box
            if (column.columnName.equals("BATCH_ROUTE_AGG_MOVEMENT_XREF.AGGREGATE_MOVEMENT_ID"))
            {
                // Set the default validator
                column.setValidator(defaultValidator(column));

                // Found this columnName, break and search for next one
                break;
            }
        }
    }




    /**
     * Given the selected segment from the segment combo box,
     * This method will query the SEGMENT_CONF and populate specific columns in the tab table
     * When the user selects an item from Segment combo box The following columns will be populated:
     *  Rec/Del, Rec. Loaction, Rec. Con. Carrier, Del. Location, Del. Con. Carrier, Shipper, Grade and Description
     * @param aggMovementId - aggMovementId of the value selected from the combo box
     * @param tableRow - the current row being edited.
     * @throws SQLException
     */
    public void setAggMovementFields(long aggMovementId, TableRow tableRow) throws SQLException
    {
        System.out.println("Getting a aggregate movement record");
        DBConn    dbconn = ConnectionPool.getConnection();
        Statement stmt   = null;
        try
        {
            stmt = dbconn.connection.createStatement();


            String columnNames = "";
            for (int i = 0; i < selectColNames[1].length; i++)
            {
                columnNames = columnNames + selectColNames[1][i];
                if (i < selectColNames[1].length - 1)
                {
                    columnNames = columnNames + ", ";
                }

            }

            String    query =
                    "SELECT " + columnNames + " FROM POLARIS." + tableNames[1] + " WHERE ID = " + aggMovementId;
            ResultSet rs    = stmt.executeQuery(query);
            while (rs.next())
            {


                String tableNamePrefix = tableNames[1] + ".";
                for (int i = 0; i < selectColNames[1].length; i++)
                {
                    String value  = rs.getString(i + 1);
                    Field  field  = tabRS.getField(tableNamePrefix + selectColNames[1][i], tableRow);
                    Column column = field.getColumn();

                    if (value != null)
                    {
                        //if not null and it is a foreign key set the long value
                        if (column.foreignKey || (column.dataType.equals("NUMBER") && column.columnName.contains("_ID")))
                        {
                            //used specifically for combo boxes
                            //it requires a the long value to be populated.
                            field.setLvalue(new Long(value));
                        }
                        else
                        {
                            field.setLvalue(null);
                        }

                        field.setValue(value);

                    } else
                    {
                        field.setLvalue(null);
                        field.setValue(null);


                    }
                    field.setUpdated(true);

                }


            }
            rs.close();
        }
        finally
        {
            if (stmt != null)
            {
                stmt.close();
            }
            ConnectionPool.releaseConnection(dbconn);
        }

    }

    public class BatchRoutesAggMovementsTableModel extends GenericTableModel
    {


        public BatchRoutesAggMovementsTableModel(ColumnRecordSet crs, String[] names)
        {
            super(crs, names, false, PolarisUI.getMessage("CF_AGG_MOVEMENTS_PANEL", PolarisUI.getMessage("BATCH_ROUTE")));


        }

        public void setValueAt(Object aValue, int row, int col)
        {

            super.setValueAt(aValue, row, col);

            //if it is aggregate movement column.
            if (col == 1)
            {
                TableRow dbRow = (TableRow) resultSet.elementAt(row);

                Field field = resultSet.getField(getColumnName(col), dbRow);

                if (field.isUpdated() && field.getLong() != null)
                {

                    try
                    {
                        setAggMovementFields(field.getLong(), dbRow);
                    }
                    catch (SQLException sqlex)
                    {
                        JOptionPane.showMessageDialog(null, sqlex.getMessage(), "SQL Error:" + sqlex.getErrorCode(),
                                                      JOptionPane.ERROR_MESSAGE);
                    }

                }

            }
        }



        public boolean isCellEditable(int row, int col)
        {

            //the only field that should be editable is col 1 AGGREGATE_MOVEMENT_ID
            boolean colEditable = super.isCellEditable(row,col) && col==1?true:false;




            //checks if the main table is readonly.
            //if read only make sure all related tab fields and tables are not editable as well.
            boolean editable = resultRS.isReadOnly() || tabRS.isReadOnly() ? false : true&&colEditable;

            return editable;
        }
    }
}


