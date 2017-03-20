package polaris.administration.tables.batchRoutes;

import polaris.administration.tables.*;
import polaris.constants.RecDelDesc;
import polaris.db.ConnectionPool;
import polaris.db.DBConn;
import polaris.frame.ContextHelpAction;
import polaris.frame.PolarisUI;
import polaris.util.IdNameBag;
import polaris.util.TableSorter;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Creates the Maintenance Panel for a configuration screen.
 */
public class BatchRoutesSegmentsPanel extends GenericTabPanel
{
    public BatchRoutesSegmentsTableModel brSegmentsTableModel;

    /**
     * Instanciates the Maintenance Panel.
     *
     * @param mainPanel
     * @throws Exception
     */
    public BatchRoutesSegmentsPanel(MainPanel mainPanel) throws Exception
    {
        super(mainPanel);
    }

    @Override
    public String[] getColumnNames()
    {
        //no table to display on panel
        return new String[]{"EDIT_MODE", "BATCH_ROUTE_SEGMENT_XREF.REVERSE_FLAG", "BATCH_ROUTE_SEGMENT_XREF.SEQUENCE",
                            "BATCH_ROUTE_SEGMENT_XREF.ALLOC_RATIO", "BATCH_ROUTE_SEGMENT_XREF.SEGMENT_ID",
                            "SEGMENT_CONF.SYSTEM_ID", "SEGMENT_CONF.POINTA_LOCATION_ID",
                            "SEGMENT_CONF.POINTB_LOCATION_ID", "SEGMENT_CONF.DESCRIPTION"};
    }

    @Override
    public String[] getTableNames()
    {
        //no table to display on panel
        return new String[]{"BATCH_ROUTE_SEGMENT_XREF", "SEGMENT_CONF"};
    }

    @Override
    public String[] getForeignKeyConstColNames()
    {
        //no table to display on panel
        return new String[]{"BATCH_ROUTE_ID", "SEGMENT_ID"};
    }

    @Override
    public String[][] getSelectColNames()
    {
        //no table to display on panel
        return new String[][]{{}, {"SYSTEM_ID", "POINTA_LOCATION_ID", "POINTB_LOCATION_ID", "DESCRIPTION"}};
    }

    @Override
    public String getOrderBy()
    {
        return "ORDER BY BATCH_ROUTE_SEGMENT_XREF.SEQUENCE";
    }

    @Override
    public void createPanel()
    {
        tabCRS = new ColumnRecordSet(tableNames, selectColNames, foreignKeyConstColNames);

        brSegmentsTableModel = new BatchRoutesSegmentsTableModel(tabCRS, columnNames);
        GenericTableColumnModel brSegmentsColumnModel = new GenericTableColumnModel(columnNames, tabCRS);

        //creates the main table.
        tabTable = new GenericTable(new TableSorter(brSegmentsTableModel));
        tabTable.setMainPanel(mainPanel);
        tabTable.setColumnModel(brSegmentsColumnModel);
        tabTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabTable.setFillsViewportHeight(true);
        tabTable.setIntercellSpacing(new Dimension(0, 0));

        // JoeH - Per Jean Wurster Request, commenting out DragNDrop until there is a requirement for it
        //tabTable.enableDragNDrop();

        JScrollPane tableScrollPanel = new JScrollPane(tabTable);
        tableScrollPanel.setPreferredSize(new Dimension(124, 124));

        tabRS = brSegmentsTableModel.getResultSet();

        //change listener for the main table.  If users selects a different row
        //on the table grid. it will trigger this listener on the ResultRecordSet.
        ListSelectionModel listSelectionModel = (ListSelectionModel) tabTable.getSelectionModel();
        listSelectionModel.addListSelectionListener(tabRS);

        //listener for any changes to the table Model.
        brSegmentsTableModel.addTableModelListener(tabRS);
        tabRS.setTable(tabTable);

        this.addToThisPanel(0, 0, 1, tableScrollPanel, true, true);
    }



    /**
     * gets the where clause for all combo boxes of the panel.
     * SEGMENT_ID is the only combobox that requires a filter for inactive indicator flag and the SCHED_SYSTEM_ID.
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

        if (lookUpTable.equals("SEGMENT_CONF"))
        {
            //where clause for the model factory.
            //if this is in readonly/InactiveView
            //then get all records from the database
            //else if viewing in activeView/not readonly
            //then get only records that are active.
            whereClause = ComponentFactory.getDBIdNameFactoryWhereClause(activeView);

            //if the look up table is SEGMENT_CONF and valid main table row
            //then create model factory.
            String schedSystemId = "";

            //get the main table's SCHED_SYSTEM_ID value.
            Field schedSystemIdField = resultRS.getField("SCHED_SYSTEM_ID", mainTableRow);

            //get the schedSystemId values from the field.
            schedSystemId = (schedSystemIdField == null || schedSystemIdField.getValue() == null) ? "" : schedSystemIdField.getValue();

            if (columnName.equals("BATCH_ROUTE_SEGMENT_XREF.SEGMENT_ID"))
            {
                if (!schedSystemId.isEmpty())
                {
                    // Build the where clause using by appending a sub query at the end that includes the list of segment ID's that are valid
                    whereClause += " AND ID IN (" + getQuery(QueryType.SEGMENT_DROPDOWN, schedSystemId) + ")";
                }
            }
        }
        return whereClause;

    }

    public boolean isActiveView()
    {
        return false;
    }

    /**
     * Signifies that the panel values has been modified.
     *
     * @return
     */
    public boolean isDirty()
    {
        return dirty;
    }

    /**
     * sets the dirty flag, which states whether the panel has bee modifed or not.
     *
     * @param dirty
     */
    public void setDirty(boolean dirty)
    {
        this.dirty = dirty;
    }

    /**
     * Given the selected segment from the segment combo box,
     * This method will query the SEGMENT_CONF and populate specific columns in the tab table
     * When the user selects an item from Segment combo box The following columns will be populated:
     *  System, Loc-A, Loc-B and Description
     * @param segmentId  - segmentId of the value selected from combo box
     * @param tableRow - The current row being edited.
     * @throws SQLException
     */
    public void setSegmentFields(Long segmentId, TableRow tableRow) throws SQLException
    {
        System.out.println("Getting a segment record");
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

            if (segmentId == null)
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
                String query = "SELECT " + columnNames + " FROM POLARIS." + tableNames[1] + " WHERE ID = " + segmentId;
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
                            // Joe Hunsaker - 1/23/2017 - Fixed SPR 131 by checking _ID on end of string instead of anywhere in string
                            if (column.foreignKey || (column.dataType.equals("NUMBER") && column.columnName.endsWith("_ID")))
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
     * set Help Topic Id
     */
    @Override
    public void setHelp()
    {
        this.mainPanel.setContextHelpTopicId("BatchRoutesConfigurationModule_SegmentsTab");
    }

    @Override
    protected void setValidators()
    {
        ColumnRecordSet crs = this.brSegmentsTableModel.getColumnRecordSet();

        for (int i = 0; i < crs.size(); i++)
        {
            // Validate defaults
            Column column = crs.elementAt(i);

            // REV Flag - don't validate because is defaulted by database to 0 (false).

            // Alloc Ratio, ensure set to something
            if (column.columnName.equals("BATCH_ROUTE_SEGMENT_XREF.ALLOC_RATIO"))
            {
                column.setValidator(segmentValidator(column));
            }

            // Validate Sequences
            if (column.columnName.equals("BATCH_ROUTE_SEGMENT_XREF.SEQUENCE"))
            {
                column.setValidator(segmentValidator(column));
            }

            // Validate Segment
            if (column.columnName.equals("BATCH_ROUTE_SEGMENT_XREF.SEGMENT_ID"))
            {
                column.setValidator(segmentValidator(column));
            }
        }

    }

    private ColumnValidator segmentValidator(Column column)
    {


        ColumnValidator validator = new ColumnValidator(column)
        {


            private String errorMsg = "";

            public boolean validate(String text, String brId) throws SQLException
            {
                errorMsg = "";

                return validate((Object) text, brId);
            }

            public boolean validate(Object value, String brId) throws SQLException
            {

                errorMsg = "";
                if (brId.equals(ResultRecordSet.newKey))
                {
                    return true;
                }

                // Check if the value is not set
                if (value == null || (value instanceof String && ((String) value).isEmpty()) || (value instanceof Long && value.equals(new Long(0))))
                {
                    errorMsg = PolarisUI.getMessage("CF_BR_SEGMENTS_NULL_VALUES");
                    return false;
                }

                //get tab's selected table row.
                TableRow tableRow    = tabRS.getTableRowAtCursor();
                Field    idField = tabRS.getField("BATCH_ROUTE_SEGMENT_XREF.ID", tableRow);
                Field    sequenceField = tabRS.getField("BATCH_ROUTE_SEGMENT_XREF.SEQUENCE", tableRow);
                Field    allocRatioField  = tabRS.getField("BATCH_ROUTE_SEGMENT_XREF.ALLOC_RATIO", tableRow);
                Field    segmentIdField  = tabRS.getField("BATCH_ROUTE_SEGMENT_XREF.SEGMENT_ID", tableRow);

                //if required fields are null return false;
                if (sequenceField == null || allocRatioField == null || segmentIdField == null)
                {
                    errorMsg = PolarisUI.getMessage("CF_BR_SEGMENTS_NULL_VALUES");
                    return false;
                }

                String id = sequenceField.getValue() == null ? "" : idField.getValue();
                String sequence = sequenceField.getValue() == null ? "" : sequenceField.getValue();
                String allocRatio  = allocRatioField.getValue() == null ? "" : allocRatioField.getValue();
                String segmentId  = segmentIdField.getValue() == null ? "" : segmentIdField.getValue();

                // if required field values are empty return false;
                if (sequence.isEmpty() || allocRatio.isEmpty() || segmentId.isEmpty())
                {
                    errorMsg = PolarisUI.getMessage("CF_BR_SEGMENTS_NULL_VALUES");
                    return false;
                } else
                {
                    //check if the sequence is valid
                    boolean validSequence = checkSequenceValid(brId, id, sequence);

                    if (!validSequence)
                    {
                        errorMsg = PolarisUI.getMessage("CF_BR_SEGMENTS_UNIQUE_SEQUENCE");

                        return false;
                    }

                    // checkIfLocalMovement
                    TableRow mainTableRow = resultRS.getTableRowAtCursor();
                    // Column = LOCAL_MOVEMENT_FLAG
                    String localMovementFlag = resultRS.getField("LOCAL_MOVEMENT_FLAG", mainTableRow).getValue();
                    if (localMovementFlag.equals("1"))
                    {
                        localMovementFlag = "0";
                    } else
                    {
                        localMovementFlag = "1";
                    }

                    // Check if segmentID is valid
                    String validSegmentId = checkSegmentIdValid(localMovementFlag, segmentId);

                    if (validSegmentId.length() > 0)
                    {
                        // Get the BR Name
                        Field    brName = resultRS.getField("NAME", mainTableRow);
                        validSegmentId = brName.getValue() + "\n" + validSegmentId;
                        errorMsg = PolarisUI.getMessage("CF_BR_SEGMENTS_1000", validSegmentId);

                        return false;
                    }
                }

                return true;
            }

            public String getErrorMessage(Object value)
            {
                String defaultErrorMsg = PolarisUI.getMessage("CF_VALIDATION_SAVE_ERROR",
                                                              brSegmentsTableModel.getPanelName()) + "\n";


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
     * Check if the Sequence already exists on another associated segment.
     *
     * @return
     * @throws SQLException
     */
    private boolean checkSequenceValid(String brId, String id, String sequence) throws SQLException
    {
        boolean result = true;

        DBConn    dbconn = ConnectionPool.getConnection();
        Statement stmt   = null;
        try
        {
            stmt = dbconn.connection.createStatement();
            String    query = getQuery(QueryType.CHECK_SEQ, brId, id.equals(Configuration.newKey) ? "0" : id, sequence);
            ResultSet rs    = stmt.executeQuery(query);

            // If not empty, then sequence is already in use
            if (rs.next())
            {
                result = false;
            }

            rs.close();
        }
        catch (SQLException ex)
        {
            throw ex;
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
     * Check if the SegmentId is Valid by checking if the Segment is associated to a Batch Route with a different setting for the Local Movement Flag.
     *
     * @return
     * @throws SQLException
     */
    private String checkSegmentIdValid(String movementFlag, String segmentId) throws SQLException
    {
        String  batchRoutesList = "";

        DBConn    dbconn = ConnectionPool.getConnection();
        Statement stmt   = null;
        try
        {
            stmt = dbconn.connection.createStatement();

            String    query = getQuery(QueryType.CHECK_SEGMENT_LCL_OR_NON, segmentId, movementFlag);
            ResultSet rs    = stmt.executeQuery(query);

            // If not empty, then sequence is already in use
            while (rs.next())
            {
                batchRoutesList += rs.getString(1) + "\n";
            }
            rs.close();
        }
        catch (SQLException ex)
        {
            throw ex;
        }
        finally
        {
            if (stmt != null)
            {
                stmt.close();
            }
            ConnectionPool.releaseConnection(dbconn);
        }

        return batchRoutesList;
    }

    protected String getQuery(QueryType queryType, String... queryParameters)
    {
        String query = "";

        switch (queryType)
        {
            case CHECK_SEQ:
                query = "SELECT id, sequence\n";
                query += "FROM \n";
                query += PolarisUI.DB_OWNER + "batch_route_segment_xref \n";
                query += "WHERE  \n";
                query += " batch_route_id = " + queryParameters[0] + "\n";
                query += " and inactive_indicator_flag =  0 \n";
                query += " and id <> " + queryParameters[1] + "\n";
                query += " and sequence =  " + queryParameters[2];
                break;
            case CHECK_SEGMENT_LCL_OR_NON:
                query = "SELECT DISTINCT";
                query += " B.name ";
                query += "FROM ";
                query += PolarisUI.DB_OWNER + "batch_route_segment_xref  A, ";
                query += PolarisUI.DB_OWNER + "batch_route_conf   B ";
                query += "WHERE ";
                query += " A.inactive_indicator_flag = 0  and ";
                query += " B.inactive_indicator_flag = 0  and ";
                query += " A.batch_route_id = B.id and ";
                query += " A.segment_id	= " + queryParameters[0] + " and ";
                query += " B.local_movement_flag	= " + queryParameters[1] + " ";
                break;
            case SEGMENT_DROPDOWN:
                query  = "SELECT ";
                query += " A.id ";
                query += "FROM ";
                query += PolarisUI.DB_OWNER +"segment_conf A,";
                query += PolarisUI.DB_OWNER +"system_conf B,";
                query += PolarisUI.DB_OWNER +"location_conf C,";
                query += PolarisUI.DB_OWNER +"location_conf D ";
                query += "WHERE ";
                query += " A.sched_system_id = " + queryParameters[0] + " ";
                query += " and A.system_id=B.id ";
                query += " and A.pointa_location_id=C.id ";
                query += " and A.pointb_location_id=D.id ";
                break;
        }

        return query;
    }

    protected enum QueryType
    {
        CHECK_SEQ,
        CHECK_SEGMENT_LCL_OR_NON,
        SEGMENT_DROPDOWN
    }

    public class BatchRoutesSegmentsTableModel extends GenericTableModel
    {


        public BatchRoutesSegmentsTableModel(ColumnRecordSet crs, String[] names)
        {
            super(crs, names, true, PolarisUI.getMessage("CF_SEGMENTS_PANEL", PolarisUI.getMessage("BATCH_ROUTE")));


        }

        public void setValueAt(Object aValue, int row, int col)
        {

            super.setValueAt(aValue, row, col);

            //if user updates the segments columns
            if (col == 4)
            {
                TableRow dbRow = (TableRow) resultSet.elementAt(row);

                Field field = resultSet.getField(getColumnName(col), dbRow);

                if (field.isUpdated() && field.getLong() != null)
                {

                    try
                    {
                        setSegmentFields(field.getLong(), dbRow);
                    }
                    catch (SQLException sqlex)
                    {
                        JOptionPane.showMessageDialog(null, sqlex.getMessage(), "SQL Error:" + sqlex.getErrorCode(),
                                                      JOptionPane.ERROR_MESSAGE);
                    }

                }

            }
        }


        public Class getColumnClass(int col)

        {
            if (col == 1)
            {
                return boolean.class;
            } else
            {
                return getValueAt(0, col).getClass();
            }
        }


        public boolean isCellEditable(int row, int col)
        {

            //the only field that should be editable are cols less than 5
            boolean colEditable = super.isCellEditable(row,col) && col<5?true:false;

            //checks if the main table is readonly.
            //if read only make sure all related tab fields and tables are not editable as well.
            boolean editable = resultRS.isReadOnly() || tabRS.isReadOnly() ? false : true&&colEditable;

            return editable;


        }

    }
}
