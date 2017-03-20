package polaris.administration.tables.movements;

import polaris.administration.tables.*;
import polaris.administration.tables.batchRoutes.BatchRoutesSegmentsPanel;
import polaris.db.ConnectionPool;
import polaris.db.DBConn;
import polaris.frame.PolarisUI;
import polaris.util.TableSorter;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by TCI-Waleed Elsaid on 1/30/2017.
 */
public class MovementsBatchRoutesPanel extends GenericTabPanel {

    private MovementsBatchRoutesTableModel mvBatchRoutesTableModel;

    public MovementsBatchRoutesPanel(MainPanel mainPanel) throws Exception
    {
        super(mainPanel);
    }

    /**
     * Created by TCI-Waleed Elsaid on 1/30/2017.
     * the column names that will show up in the tab table
     * @return
     */
    @Override
    public String[] getColumnNames() {
        return new String[]{"EDIT_MODE", "MOVEMENT_BATCH_ROUTE_XREF.PRM_SEQUENCE", "MOVEMENT_BATCH_ROUTE_XREF.SEQUENCE",
                "MOVEMENT_BATCH_ROUTE_XREF.BATCH_ROUTE_ID",
                "BATCH_ROUTE_CONF.LOCAL_MOVEMENT_FLAG",
                "BATCH_ROUTE_CONF.REC_LOCATION_ID",
                "BATCH_ROUTE_CONF.REC_CON_CARRIER_ID",
                "BATCH_ROUTE_CONF.DEL_LOCATION_ID",
                "BATCH_ROUTE_CONF.DEL_CON_CARRIER_ID",
                "BATCH_ROUTE_CONF.DESCRIPTION"};
    }

    /**
     * Created by TCI-Waleed Elsaid on 1/30/2017.
     * The table names used in the queries for the tab
     * @return
     */
    @Override
    public String[] getTableNames()
    {
        return new String[]{"MOVEMENT_BATCH_ROUTE_XREF", "BATCH_ROUTE_CONF"};
    }

    /**
     * Created by TCI-Waleed Elsaid on 1/30/2017.
     * Foreign keys that link MOVEMENT_BATCH_ROUTE_XREF table to MOVEMENT_CONF, AND BATCH_ROUTE_CONF
     * @return
     */
    @Override
    public String[] getForeignKeyConstColNames()
    {
        return new String[]{"MOVEMENT_ID", "BATCH_ROUTE_ID"};
    }


    /**
     * Created by TCI-Waleed Elsaid on 1/30/2017.
     * Columns that get populated when selecting a batch route
     * @return
     */
    @Override
    public String[][] getSelectColNames()
    {
        return new String[][]{{}, {"LOCAL_MOVEMENT_FLAG", "REC_LOCATION_ID", "REC_CON_CARRIER_ID","DEL_LOCATION_ID", "DEL_CON_CARRIER_ID", "DESCRIPTION"}};
    }


    /**
     * Created by TCI-Waleed Elsaid on 1/30/2017.
     * The order by clause used to in the query to populate the select columns from the batch route
     * @return
     */
    @Override
    public String getOrderBy()
    {
        return "ORDER BY MOVEMENT_BATCH_ROUTE_XREF.PRM_SEQUENCE, MOVEMENT_BATCH_ROUTE_XREF.SEQUENCE ";
    }

    /**
     * Created by TCI-Waleed Elsaid on 1/30/2017.
     * Method called by the super constructor
     */
    @Override
    public void createPanel()
    {
        // a columnRecordSet declared in the GenericTabPanel super class
        tabCRS = new ColumnRecordSet(tableNames, selectColNames, foreignKeyConstColNames);

        mvBatchRoutesTableModel = new MovementsBatchRoutesTableModel(tabCRS, columnNames);
        GenericTableColumnModel mvBatchRoutesColumnModel = new GenericTableColumnModel(columnNames, tabCRS);

        //creates the main table.
        tabTable = new GenericTable(new TableSorter(mvBatchRoutesTableModel));
        tabTable.setMainPanel(mainPanel);
        tabTable.setColumnModel(mvBatchRoutesColumnModel);
        // set the selection mode for the tab table to allow multi selection
        tabTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabTable.setFillsViewportHeight(true);
        tabTable.setIntercellSpacing(new Dimension(0, 0));

        JScrollPane tableScrollPanel = new JScrollPane(tabTable);
        tableScrollPanel.setPreferredSize(new Dimension(124, 124));

        tabRS = mvBatchRoutesTableModel.getResultSet();

        //change listener for the tab table.  If users selects a different row
        //on the table grid. it will trigger this listener on the ResultRecordSet.
        ListSelectionModel listSelectionModel = (ListSelectionModel) tabTable.getSelectionModel();
        listSelectionModel.addListSelectionListener(tabRS);

        //listener for any changes to the table Model.
        mvBatchRoutesTableModel.addTableModelListener(tabRS);
        tabRS.setTable(tabTable);

        this.addToThisPanel(0, 0, 1, tableScrollPanel, true, true);
    }


    /**
     * Created by TCI-Waleed Elsaid on 1/30/2017.
     * Given the selected batch route from the batch route combo box,
     * This method will query the BATCH_ROUTE_CONF and populate specific columns in the tab table
     * When the user selects an item from batch route combo box The following columns will be populated:
     *
     * @param batchRouteID  - batchRouteID of the value selected from combo box
     * @param tableRow - The current row being edited.
     * @throws SQLException
     */
    public void setBatchRouteFields(Long batchRouteID, TableRow tableRow) throws SQLException
    {
        System.out.println("Getting a batch route record");
        DBConn dbconn = ConnectionPool.getConnection();
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

            if (batchRouteID == null)
            {
                for (int i = 0; i < selectColNames[1].length; i++)
                {
                    String tableNamePrefix = tableNames[1] + ".";
                    Field  field           = tabRS.getField(tableNamePrefix + selectColNames[1][i], tableRow);
                    field.setLvalue(null);
                    field.setValue(null);
                }
            } else
            {
                String query = "SELECT " + columnNames + " FROM POLARIS." + tableNames[1] + " WHERE ID = " + batchRouteID;
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next())
                {

                    String tableNamePrefix = tableNames[1] + ".";
                    for (int i = 0; i < selectColNames[1].length; i++)
                    {
                        //gets the value for each column in the recordset.
                        String value  = rs.getString(i + 1);
                        //gets the field associated to the column
                        Field  field  = tabRS.getField(tableNamePrefix + selectColNames[1][i], tableRow);
                        //gets the columnRS associated to the column
                        Column column = field.getColumn();
                        //if the value is null set the field to null.
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

    /**
     * Created by TCI-Waleed Elsaid on 1/30/2017.
     * Sets the validators that get called upon save action
     */
    @Override
    protected void setValidators()
    {
        ColumnRecordSet crs = this.mvBatchRoutesTableModel.getColumnRecordSet();

        for (int i = 0; i < crs.size(); i++)
        {
            // Validate defaults
            Column column = crs.elementAt(i);

            // PRM_SEQUENCE make sure not null and unique
            if (column.columnName.equals("MOVEMENT_BATCH_ROUTE_XREF.PRM_SEQUENCE"))
            {
                column.setValidator(batchRouteValidator(column));
            }

            // Validate Sequences
            if (column.columnName.equals("MOVEMENT_BATCH_ROUTE_XREF.SEQUENCE"))
            {
                column.setValidator(batchRouteValidator(column));
            }

            // Validate BATCH_ROUTE_ID
            if (column.columnName.equals("MOVEMENT_BATCH_ROUTE_XREF.BATCH_ROUTE_ID"))
            {
                column.setValidator(batchRouteValidator(column));
            }
        }

    }

    /**
     * Created by TCI-Waleed Elsaid on 1/30/2017.
     * This method creates and returns the right validator according to the given column
     * @param column
     * @return
     */
    private ColumnValidator batchRouteValidator(Column column)
    {
        ColumnValidator validator = new ColumnValidator(column)
        {
            private String errorMsg = "";

            public boolean validate(String text, String brId) throws SQLException
            {
                errorMsg = "";

                return validate((Object) text, brId);
            }

            public boolean validate(Object value, String mvId) throws SQLException
            {
                errorMsg = "";
                if (mvId.equals(ResultRecordSet.newKey))
                {
                    return true;
                }

                //get tab's selected table row.
                TableRow tableRow    = tabRS.getTableRowAtCursor();
                Field    idField = tabRS.getField("MOVEMENT_BATCH_ROUTE_XREF.ID", tableRow);
                Field    sequenceField = tabRS.getField("MOVEMENT_BATCH_ROUTE_XREF.SEQUENCE", tableRow);
                Field    prmSequenceField  = tabRS.getField("MOVEMENT_BATCH_ROUTE_XREF.PRM_SEQUENCE", tableRow);
                Field    batchRouteIdField  = tabRS.getField("MOVEMENT_BATCH_ROUTE_XREF.BATCH_ROUTE_ID", tableRow);

                //if required fields are null return false;
                if (sequenceField == null || prmSequenceField == null || batchRouteIdField == null)
                {
                    errorMsg = PolarisUI.getMessage("CF_VALIDATION_NULL","Prm. Seq., Seq., and Batch Route");
                    //display the error message and not allow them to save it
                    mode = MODAL_AND_FOCUS;
                    return false;

                }


                String id = idField.getValue() == null ? "" : idField.getValue();
                String sequence = sequenceField.getValue() == null ? "" : sequenceField.getValue();
                String prmSeq  = prmSequenceField.getValue() == null ? "" : prmSequenceField.getValue();
                String batchRouteId  = batchRouteIdField.getValue() == null ? "" : batchRouteIdField.getValue();

                // if required field values are empty return false;
                if (sequence.isEmpty() || prmSeq.isEmpty() || batchRouteId.isEmpty())
                {
                    errorMsg = PolarisUI.getMessage("CF_VALIDATION_NULL","Prm. Seq., Seq., and Batch Route");
                    return false;
                }
                // PERMSEQ and SEQ cannot have  negative values
                else if (Integer.parseInt(sequence)<0 || Integer.parseInt(prmSeq)<0)
                {
                    errorMsg = PolarisUI.getMessage("CF_MV_NEG_PRM_SEQ_SEQ");
                    mode = MODAL_AND_FOCUS;
                    return false;
                }
                else {
                    //check if the prmSeq and batchRouteId already exist in the database
                    id = tableRow.isInserted() ? "0" : id;
                    boolean validMvBatchRoute = checkUniqueMvBatchRoute(mvId, id, prmSeq, batchRouteId);
                    if (!validMvBatchRoute) {
                        //if the batch route record exist in the database, display an error msg
                        errorMsg = PolarisUI.getMessage("CF_MV_BATCH_ROUTES_UNIQUE");
                        //display the error message and not allow them to save it
                        mode = MODAL_AND_FOCUS;
                        return false;
                    }

                }
                return true;
            }

            public String getErrorMessage(Object value)
            {
                String defaultErrorMsg = PolarisUI.getMessage("CF_VALIDATION_SAVE_ERROR",
                        mvBatchRoutesTableModel.getPanelName()) + "\n";


                if (errorMsg.isEmpty())
                {
                    errorMsg = defaultErrorMsg;
                    errorMsg += PolarisUI.getMessage("CF_VALIDATION_NULL", column.columnName);
                } else
                {
                    errorMsg = defaultErrorMsg + errorMsg;
                }


                return errorMsg;
            }

        };


        return validator;
    }


    /**
     * Created by TCI-Waleed Elsaid on 1/30/2017.
     *  This method executes the query
     *  SELECT id, sequence FROM movement_batch_route_xref
     *  WHERE  movement_id mvId and id <> id and prm_sequence prmSeq and batch_route_id = batchRouteId
     *  to check if this record is unique or not in movement_batch_route_xref table
     * @param mvId          the movement_conf record id
     * @param id            the movement_batch_route_xref record id
     * @param prmSeq        the movement_batch_route_xref record prmSeq
     * @param batchRouteId  the movement_batch_route_xref record batchRouteId
     * @return
     * @throws SQLException
     */
    private boolean checkUniqueMvBatchRoute(String mvId,String id,String prmSeq, String batchRouteId) throws SQLException
    {
        boolean result = true;

        DBConn    dbconn = ConnectionPool.getConnection();
        Statement stmt   = null;
        try
        {
            stmt = dbconn.connection.createStatement();
            String    query = getQuery(QueryType.CHECK_UNIQUE_MV_BR, mvId, id,prmSeq,batchRouteId);
            ResultSet rs    = stmt.executeQuery(query);
            if (rs.next())
            {
                result = false;
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

        return result;
    }


    /**
     * Created by TCI-Waleed Elsaid on 1/30/2017.
     * gets the where clause for the batch route combo boxe.
     * BATCHROUTE_ID is the only combobox that requires a filter for inactive indicator flag
     */
    public String getDBNameFactoryWhereClause(boolean activeView, String columnName, String lookUpTable, TableRow mainTableRow)
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

        if (lookUpTable.equals("BATCH_ROUTE_CONF"))
        {
            //where clause for the model factory.
            //if this is in readonly/InactiveView
            //then get all records from the database
            //else if viewing in activeView/not readonly
            //then get only records that are active.
            whereClause = ComponentFactory.getDBIdNameFactoryWhereClause(activeView);

            //if the look up table is BATCH_ROUTE_CONF and valid main table row
            //then create model factory.
            String systemGroupId  = "";

            //get the main table's SYSTEM_GROUP_ID value.
            Field systemGroupIdField  = resultRS.getField("SYSTEM_GROUP_ID", mainTableRow);

            //get the schedSystemId values from the field.
            systemGroupId  = (systemGroupIdField  == null || systemGroupIdField .getValue() == null) ? "" : systemGroupIdField .getValue();

            if (columnName.equals("MOVEMENT_BATCH_ROUTE_XREF.BATCH_ROUTE_ID"))
            {
                if (!systemGroupId .isEmpty())
                {
                    // Build the where clause using by appending a sub query at the end that includes the list of SYSTEM ID's that are valid
                    whereClause += " AND SYSTEM_ID  IN (" + getQuery(QueryType.SYSTEM_IDS,  systemGroupId) + ")";
                }
            }
        }
        return whereClause;

    }

    /**
     * Created by TCI-Waleed Elsaid on 1/30/2017.
     * This method returns the query string fro the given type and parameters
     * @param queryType
     * @param queryParameters
     * @return
     */
    protected String getQuery(QueryType queryType, String... queryParameters)
    {
        String query = "";

        switch (queryType)
        {
            case CHECK_UNIQUE_MV_BR:
                query = "SELECT id, sequence\n";
                query += "FROM \n";
                query += PolarisUI.DB_OWNER + "movement_batch_route_xref \n";
                query += "WHERE  \n";
                query += " movement_id = " + queryParameters[0] + "\n";
                query += " and id <> " + queryParameters[1] + "\n";
                query += " and prm_sequence =  " + queryParameters[2];
                query += " and batch_route_id =  " + queryParameters[3];
                break;
            case SYSTEM_IDS:
                query  = "SELECT ";
                query += " SYS.id ";
                query += "FROM ";
                query += PolarisUI.DB_OWNER +"SYSTEM_CONF SYS ";
                query += "WHERE ";
                query += " SYS.system_group_id = " + queryParameters[0] + " ";
                break;

        }

        return query;
    }


    protected enum QueryType
    {
      SYSTEM_IDS,
      CHECK_UNIQUE_MV_BR
    }

    /**
     * Created by TCI-Waleed Elsaid on 1/30/2017.
     * MovementsBatchRoutesTableModel class
     * The table model used for the tab panel
     */

    public class MovementsBatchRoutesTableModel extends GenericTableModel
    {
        public MovementsBatchRoutesTableModel(ColumnRecordSet crs, String[] names)
        {
            super(crs, names, false, PolarisUI.getMessage("CF_BATCH_ROUTES_PANEL", PolarisUI.getMessage("MOVEMENT")));
        }

        public void setValueAt(Object aValue, int row, int col)
        {
            super.setValueAt(aValue, row, col);

            //if user updates the batch route column
            if (col == 3)
            {
                TableRow dbRow = (TableRow) resultSet.elementAt(row);

                Field field = resultSet.getField(getColumnName(col), dbRow);

                if (field.isUpdated() && field.getLong() != null)
                {
                    try
                    {
                        setBatchRouteFields(field.getLong(), dbRow);
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

            //the only field that should be editable are cols less than 5
            boolean colEditable = super.isCellEditable(row,col) && col<4?true:false;

            //checks if the main table is readonly.
            //if read only make sure all related tab fields and tables are not editable as well.
            boolean editable = resultRS.isReadOnly() || tabRS.isReadOnly() ? false : true&&colEditable;

            return editable;


        }

    }

}
