package polaris.administration.tables.movements;

import polaris.administration.tables.*;
import polaris.constants.RevenueAllocMethodDesc;
import polaris.constants.SpecialItems;
import polaris.db.ConnectionPool;
import polaris.db.DBConn;
import polaris.frame.PolarisUI;
import polaris.util.IdNameBag;
import polaris.util.TableSorter;
import polaris.util.VisiCheckbox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by  TCI-Waleed Elsaid on 3/1/2017.
 */
public class MovementsRevAllocationsPanel extends GenericTabPanel implements ActionListener {
    public MovementsRevAllocationsTableModel mvRevAllocationsTableModel;
    // historical check box, to be used in filtering the tab table results
    private VisiCheckbox historicalFlag;

    /**
     * Created by  TCI-Waleed Elsaid on 3/1/2017
     * Instantiates the Aggregate Movement Panel.
     *
     * @param mainPanel
     * @throws Exception
     */
    public MovementsRevAllocationsPanel(MainPanel mainPanel) throws Exception
    {
        super(mainPanel);
    }

    /**
     *  Created by  TCI-Waleed Elsaid on 3/1/2017
     *  list of columns to display on the table.
     * @return
     */
    @Override
    public String[] getColumnNames()
    {
        return new String[]{"EDIT_MODE", "MOVEMENT_SEQUENCE_SEGMENT_XREF.ALLOC_RATIO",
                "MOVEMENT_SEQUENCE_SEGMENT_XREF.SEGMENT_ID", "MOVEMENT_SEQUENCE_SEGMENT_XREF.TARIFF_SEQUENCE_ID",
                "TARIFF_CONF.GOVERNING_BODY_ID", "TARIFF_CONF.ISSUING_COMPANY_ID",
                "TARIFF_CONF.TARIFF_NUMBER", "TARIFF_SEQUENCE_CONF.TARIFF_SEQUENCE",
                "TARIFF_SEQUENCE_CONF.TARIFF_RATE", "TARIFF_SEQUENCE_CONF.EFFECTIVE_START_DATE",
                "TARIFF_SEQUENCE_CONF.EFFECTIVE_STOP_DATE"};
    }

    /**
     * Created by  TCI-Waleed Elsaid on 3/1/2017
     * @return
     */
    @Override
    public String[] getTableNames()
    {
        //gets a list of db tables to display on the table
        return new String[]{"MOVEMENT_SEQUENCE_SEGMENT_XREF","TARIFF_SEQUENCE_CONF", "TARIFF_CONF" };
    }

    /**
     * Created by  TCI-Waleed Elsaid on 3/1/2017
     * list of foreign Key Col Names.
     * @return
     */
    @Override
    public String[] getForeignKeyConstColNames()
    {
        return new String[]{"MOVEMENT_ID", "TARIFF_SEQUENCE_ID", "TARIFF_ID"};
    }

    /**
     * Created by  TCI-Waleed Elsaid on 3/1/2017
     * @return
     */
    @Override
    public String[][] getSelectColNames()
    {
        return new String[][]{{}, {"TARIFF_SEQUENCE", "TARIFF_RATE", "EFFECTIVE_START_DATE", "EFFECTIVE_STOP_DATE"},
                {"GOVERNING_BODY_ID", "ISSUING_COMPANY_ID", "TARIFF_NUMBER"}};

    }

    /**
     * Created by  TCI-Waleed Elsaid on 3/1/2017
     * @return
     */
    @Override
    public String getOrderBy()
    {
        return "ORDER BY TARIFF_CONF.NAME, TARIFF_CONF.TARIFF_NUMBER,TARIFF_SEQUENCE_CONF.TARIFF_SEQUENCE";
    }

    /**
     * Created by  TCI-Waleed Elsaid on 3/1/2017
     */
    @Override
    public void createPanel()
    {
        //populate the table's column definitions.
        tabCRS = new ColumnRecordSet(tableNames, selectColNames, foreignKeyConstColNames);
        //creates the tables TableModel
        mvRevAllocationsTableModel = new MovementsRevAllocationsTableModel(tabCRS, columnNames);
        //creates the TableColumnModel
        GenericTableColumnModel brAggMovementsTableColumnModel = new GenericTableColumnModel(columnNames, tabCRS);

        //creates the table.
        tabTable = new GenericTable(new TableSorter(mvRevAllocationsTableModel));
        tabTable.setMainPanel(mainPanel);
        tabTable.setColumnModel(brAggMovementsTableColumnModel);
        tabTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabTable.setFillsViewportHeight(true);
        tabTable.setIntercellSpacing(new Dimension(0, 0));
        //makes the Panel scrollable.
        JScrollPane tableScrollPanel = new JScrollPane(tabTable);
        tableScrollPanel.setPreferredSize(new Dimension(300, 100));

        //gets table's ResultRecordSet.
        tabRS = mvRevAllocationsTableModel.getResultSet();


        //change listener for the main table.  If users selects a different row
        //on the table grid. it will trigger this listener on the ResultRecordSet.
        ListSelectionModel listSelectionModel = (ListSelectionModel) tabTable.getSelectionModel();
        listSelectionModel.addListSelectionListener(tabRS);

        //listener for any changes to the table Model.
        mvRevAllocationsTableModel.addTableModelListener(tabRS);

        //Associates the JTable to the ResultRecordSet.
        tabRS.setTable(tabTable);

        historicalFlag = new VisiCheckbox(PolarisUI.getMessage("TARIFF_HISTORICAL_FILTER"));
        historicalFlag.setTableModelClassName(mvRevAllocationsTableModel.getClass().getName());
        // Add the check box to this panel
        this.addToThisPanel(0, 0, 1, 1, historicalFlag);

        this.addToThisPanel(0, 1, 1, tableScrollPanel, true, true);
    }



    /**
     * Created by TCI-Waleed Elsaid on 03/01/2017.
     * This method is triggered when the historical check box gets checked or un-checked
     * A call to updatePanel is made to refresh the tab table
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e)
    {
        // check if Historical checkbox is the source of the action
        if (e.getSource() instanceof VisiCheckbox ) {
            if (!tabRS.changesPending()) {
                boolean activeView = tabRS.isReadOnly() ? false : true;
                updatePanel(activeView);
            }
            else
            {
                // revert the status of the checkbox
                historicalFlag.setSelected(!historicalFlag.isSelected());
            }
        }
    }

    /**
     * Created by TCI-Waleed Elsaid on 03/01/2017.
     * gets the where clause for all combo boxes of the panel.
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

            //initialize variable.
            String systemGroupId = "";

            //get the main table's SYSTEM_GROUP_ID field.
            Field systemGroupIdField = resultRS.getField("SYSTEM_GROUP_ID", mainTableRow);

            //get the systemGroupId value from the field.
            systemGroupId = (systemGroupIdField == null || systemGroupIdField.getValue() == null) ? "": systemGroupIdField.getValue();

            if (columnName.equals("MOVEMENT_SEQUENCE_SEGMENT_XREF.SEGMENT_ID"))
            {
                if (!systemGroupId.isEmpty())
                {
                    // Build the where clause using by appending a sub query at the end that includes the list of SEGMENT ID's that are valid
                    whereClause += " AND SYSTEM_ID IN (" + getQuery(QueryType.SYSTEM_IDS, systemGroupId) + ")";
                }
            }
        }
        else if (lookUpTable.equals("TARIFF_SEQUENCE_CONF"))
        {
            //where clause for the model factory.
            //if this is in readonly/InactiveView
            //then get all records from the database
            //else if viewing in activeView/not readonly
            //then get only records that are active.
            whereClause = ComponentFactory.getDBIdNameFactoryWhereClause(activeView);

            //initialize variable.
            String gradeSpecId = "";

            //get the main table's GRADE_SPEC_ID field.
            Field gradeSpecIdField = resultRS.getField("GRADE_SPEC_ID", mainTableRow);

            //get the gradeSpecId value from the field
            gradeSpecId = (gradeSpecIdField == null || gradeSpecIdField.getValue() == null) ? "" : gradeSpecIdField.getValue();

            if (columnName.equals("MOVEMENT_SEQUENCE_SEGMENT_XREF.TARIFF_SEQUENCE_ID"))
            {
                if (!gradeSpecId.isEmpty() )
                {
                    // Build the where clause using by appending a sub query at the end
                    whereClause += " AND ID IN (" + getQuery(QueryType.TARIFF_SEQUENCE_IDS, gradeSpecId) + ")";
                }

                // Filter tariff sequences based on the Historical checkbox.  If non-        historical records are desired,
                // pull TARIFF_SEQUENCE_CONF records where EFFECTIVE_STOP_DATE is NULL.
                if(!historicalFlag.isSelected())
                {
                    whereClause += " AND TARIFF_SEQUENCE_CONF.EFFECTIVE_STOP_DATE IS NULL ";
                }
            }
        }
        return whereClause;

    }

    public String getWhereClause()
    {
        String whereClause = super.getWhereClause();
        if(!historicalFlag.isSelected())
        {
            whereClause += " AND TARIFF_SEQUENCE_CONF.EFFECTIVE_STOP_DATE IS NULL ";
        }
        return whereClause;
    }


    protected String getQuery(QueryType queryType, String... queryParameters)
    {
        String query = "";

        switch (queryType)
        {
            case CHECK_UNIQUE_REV_ALLOC:
                query = "SELECT \n";
                query += " MSS.ID \n";
                query += "FROM \n";
                query += PolarisUI.DB_OWNER + "MOVEMENT_SEQUENCE_SEGMENT_XREF MSS \n";
                query += "WHERE \n";
                query += " MSS.MOVEMENT_ID = " + queryParameters[0] + " \n";
                if (!queryParameters[1].isEmpty())
                    query += " AND MSS.ID <> " + queryParameters[1] + " \n";
                query += " AND MSS.SEGMENT_ID = " + queryParameters[2] + " \n";
                if (!queryParameters[3].isEmpty())
                    query += " AND MSS.TARIFF_SEQUENCE_ID = " + queryParameters[3] + " \n";
                else
                    query += " AND MSS.TARIFF_SEQUENCE_ID is null \n";
                break;
            case CHECK_REV_ALLOC_SUM_RATIO:
                query = "SELECT \n";
                query += " SUM(MSS.ALLOC_RATIO) \n";
                query += "FROM \n";
                query += PolarisUI.DB_OWNER + "MOVEMENT_SEQUENCE_SEGMENT_XREF MSS \n";
                query += "WHERE \n";
                query += " MSS.INACTIVE_INDICATOR_FLAG  = 0 \n";
                query += " AND MSS.MOVEMENT_ID = " + queryParameters[0] + " \n";
                query += " AND MSS.SEGMENT_ID <> " + queryParameters[1] + " \n";
                if (!queryParameters[2].isEmpty())
                    query += " AND MSS.TARIFF_SEQUENCE_ID = " + queryParameters[2] + " \n";
                else
                    query += " AND MSS.TARIFF_SEQUENCE_ID is null \n";
                break;
            case SYSTEM_IDS:
                query = "SELECT ";
                query += " SYS.id ";
                query += "FROM ";
                query += PolarisUI.DB_OWNER + "system_conf SYS ";
                query += "WHERE ";
                query += " SYS.system_group_id = " + queryParameters[0] + " ";
                break;
            case TARIFF_SEQUENCE_IDS:
                query = "SELECT \n";
                query += " TS.ID \n";
                query += "FROM \n";
                query += PolarisUI.DB_OWNER + "TARIFF_SEQUENCE_CONF TS, \n";
                query += PolarisUI.DB_OWNER + "TARIFF_CONF T \n";
                query += "WHERE \n";
                query += " T.GRADE_SPEC_ID = " + queryParameters[0] + " \n";
                query += " AND T.ID = TS.TARIFF_ID \n";
                break;
            case GET_TARIFF_SEQUENCE_FIELDS:
                query = "SELECT \n";
                query += "  TARIFF_CONF.GOVERNING_BODY_ID, \n";
                query += "  TARIFF_CONF.ISSUING_COMPANY_ID, \n";
                query += "  TARIFF_CONF.TARIFF_NUMBER, \n";
                query += "  TARIFF_SEQUENCE_CONF.TARIFF_SEQUENCE, \n";
                query += "  TARIFF_SEQUENCE_CONF.TARIFF_RATE, \n";
                query += "  TO_CHAR (TARIFF_SEQUENCE_CONF.EFFECTIVE_START_DATE,'" + Configuration.defaultDateFormat + "'), \n";
                query += "  TO_CHAR (TARIFF_SEQUENCE_CONF.EFFECTIVE_STOP_DATE,'" + Configuration.defaultDateFormat + "') \n";
                query += "FROM \n";
                query += PolarisUI.DB_OWNER + "TARIFF_SEQUENCE_CONF, \n";
                query += PolarisUI.DB_OWNER + "TARIFF_CONF \n";
                query += "WHERE \n";
                query += "  TARIFF_SEQUENCE_CONF.ID = " + queryParameters[0] + " \n";
                query += "  AND TARIFF_SEQUENCE_CONF.TARIFF_ID = TARIFF_CONF.ID ";
                break;

        }

        return query;
    }

    protected enum QueryType
    {
        SYSTEM_IDS,
        TARIFF_SEQUENCE_IDS,
        GET_TARIFF_SEQUENCE_FIELDS,
        CHECK_UNIQUE_REV_ALLOC,
        CHECK_REV_ALLOC_SUM_RATIO
    }


    /**
     * Given the selected tariff from the tariff combo box,
     * This method will query the TARIFF_SEQUENCE_CONF  and populate specific columns in the tab table
     * @param tariffSequenceId  - tariffSequenceId of the value selected from combo box
     * @param tableRow - The current row being edited.
     * @throws SQLException
     */
    public void setTariffSequenceFields(Long tariffSequenceId, TableRow tableRow) throws SQLException
    {

        System.out.println("Getting tariff sequence record");
        DBConn    dbconn = ConnectionPool.getConnection();
        Statement stmt   = null;
        try
        {
            stmt = dbconn.connection.createStatement();

            if (tariffSequenceId == null)
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

                String query = getQuery(QueryType.GET_TARIFF_SEQUENCE_FIELDS, ""+tariffSequenceId);
                ResultSet rs    = stmt.executeQuery(query);
                //get the list of column names of the table.
                String[] names       = this.getColumnNames();
                while (rs.next())
                {
                    //loop thru the name list, start from the Gov. Body column
                    //since the fields starting from Gov. Body will be auto populated
                    //when a tariff sequence is selected.
                    for (int i = 4; i < names.length; i++)
                    {
                        //gets the value for each column in the recordset.
                        String value  = rs.getString(i - 3);
                        //gets the field associated to the column
                        Field  field  = tabRS.getField(names[i], tableRow);
                        //gets the columnRS associated to the column
                        Column column = field.getColumn();

                        //if the value is null set the field to null.
                        if (value != null)
                        {
                            //if not null and it is a foreign key set the long value
                            if (column.foreignKey || column.columnName.contains("_ID"))
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
     * Created by TCI-Waleed Elsaid on 03/01/2017.
     */
    public class MovementsRevAllocationsTableModel extends GenericTableModel
    {
        public MovementsRevAllocationsTableModel(ColumnRecordSet crs, String[] names)
        {
            super(crs, names, false, PolarisUI.getMessage("CF_REV_ALLOCATIONS_PANEL", PolarisUI.getMessage("MOVEMENT")));
        }

        public void setValueAt(Object aValue, int row, int col)
        {
            super.setValueAt(aValue, row, col);

            //if user updates the tariff column
            if (col == 3)
            {
                TableRow dbRow = (TableRow) resultSet.elementAt(row);

                Field field = resultSet.getField(getColumnName(col), dbRow);

                if (field.isUpdated() && field.getLong() != null)
                {

                    try
                    {
                        setTariffSequenceFields(field.getLong(), dbRow);
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
            boolean colEditable = super.isCellEditable(row,col) && col<4?true:false;

            //checks if the main table is readonly.
            //if read only make sure all related tab fields and tables are not editable as well.
            boolean editable = resultRS.isReadOnly() || tabRS.isReadOnly() ? false : true&&colEditable;

            return editable;
        }

        public boolean isReadOnly()
        {
            //get the main table's tableRow.
            TableRow tableRow = resultRS.getTableRowAtCursor();
            if(tableRow != null)
            {
                //if valid tableRow
                //get the tableRow's REVENUE_ALLOC_METHOD_ID field.
                Field field            = resultRS.getField("REVENUE_ALLOC_METHOD_ID", tableRow);
                long  revAllocMethodId = -1;
                //get the field value.
                revAllocMethodId =
                        (field == null || field.getValue() == null || field.getValue().isEmpty()) ? -1 : (new Long(field.getValue())).longValue();

                //this tab should only be readOnly if the revenue allocation is of type PERCENTAGE
                if (revAllocMethodId != RevenueAllocMethodDesc.PERCENTAGE)
                {
                    return true;
                }
            }

            return false;
        }


    }

    private String getSumAllocRatio(String mvId,String segmentId,String tariffSeqId) throws SQLException
    {
        String sumAllocRatio = "0";

        DBConn    dbconn = ConnectionPool.getConnection();
        Statement stmt   = null;
        try
        {
            stmt = dbconn.connection.createStatement();
            String    query = getQuery(QueryType.CHECK_REV_ALLOC_SUM_RATIO, mvId,segmentId,tariffSeqId);
            ResultSet rs    = stmt.executeQuery(query);


            if (rs.next())
            {
                sumAllocRatio = rs.getString(1);
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


        return (sumAllocRatio == null) || sumAllocRatio.isEmpty() ? "0" : sumAllocRatio;
    }


    private boolean checkUniqueRevAlloc(String mvId,String id,String segmentId, String tariffSeqId) throws SQLException
    {
        boolean result = true;

        DBConn    dbconn = ConnectionPool.getConnection();
        Statement stmt   = null;
        try
        {
            stmt = dbconn.connection.createStatement();
            String    query = getQuery(QueryType.CHECK_UNIQUE_REV_ALLOC, mvId,
                    id.equals("<New>") ? "" : id
                    ,segmentId,tariffSeqId);
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

    @Override
    protected void setValidators()
    {
        ColumnRecordSet crs = this.mvRevAllocationsTableModel.getColumnRecordSet();

        for (int i = 0; i < crs.size(); i++)
        {
            // Validate defaults
            Column column = crs.elementAt(i);

            // REV Flag - don't validate because is defaulted by database to 0 (false).

            // Alloc Ratio, ensure set to something
            if (column.columnName.equals("MOVEMENT_SEQUENCE_SEGMENT_XREF.ALLOC_RATIO"))
            {
                column.setValidator(revAllocationsValidator(column));
            }

            // Validate Sequences
            if (column.columnName.equals("MOVEMENT_SEQUENCE_SEGMENT_XREF.SEGMENT_ID"))
            {
                column.setValidator(revAllocationsValidator(column));
            }

            // Validate Segment
            if (column.columnName.equals("MOVEMENT_SEQUENCE_SEGMENT_XREF.TARIFF_SEQUENCE_ID"))
            {
                column.setValidator(revAllocationsValidator(column));
            }
        }

    }

    private ColumnValidator revAllocationsValidator(Column column)
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
                    errorMsg = PolarisUI.getMessage("CF_VALIDATION_NULL","Alloc. Ratio, Sequence and Segment");
                    mode = MODAL_AND_FOCUS;
                    return false;
                }

                //get tab's selected table row.
                TableRow tableRow    = tabRS.getTableRowAtCursor();
                Field    idField = tabRS.getField("MOVEMENT_SEQUENCE_SEGMENT_XREF.ID", tableRow);
                Field    mvIdField = tabRS.getField("MOVEMENT_SEQUENCE_SEGMENT_XREF.MOVEMENT_ID", tableRow);
                Field    sequenceField = tabRS.getField("MOVEMENT_SEQUENCE_SEGMENT_XREF.TARIFF_SEQUENCE_ID", tableRow);
                Field    allocRatioField  = tabRS.getField("MOVEMENT_SEQUENCE_SEGMENT_XREF.ALLOC_RATIO", tableRow);
                Field    segmentIdField  = tabRS.getField("MOVEMENT_SEQUENCE_SEGMENT_XREF.SEGMENT_ID", tableRow);

                //if required fields are null return false;
                if (sequenceField == null || allocRatioField == null || segmentIdField == null)
                {
                    errorMsg = PolarisUI.getMessage("CF_VALIDATION_NULL","Alloc. Ratio, Sequence and Segment");
                    return false;
                }

                String id = sequenceField.getValue() == null ? "" : idField.getValue();
                String mvId = sequenceField.getValue() == null ? "" : mvIdField.getValue();
                String tariffSeqId = sequenceField.getValue() == null ? "" : sequenceField.getValue();
                String allocRatio  = allocRatioField.getValue() == null ? "" : allocRatioField.getValue();
                String segmentId  = segmentIdField.getValue() == null ? "" : segmentIdField.getValue();

                // if required field values are empty return false;
                if (tariffSeqId.isEmpty() || allocRatio.isEmpty() || segmentId.isEmpty())
                {
                    errorMsg = PolarisUI.getMessage("CF_VALIDATION_NULL","Alloc. Ratio, Sequence and Segment");
                    return false;
                } else
                {
                    //check alloc ratio is greater than 1.
                    if (Double.parseDouble(allocRatio)> 1.00)
                    {
                        errorMsg = PolarisUI.getMessage("CF_MV_REV_ALLOC_ALLOC_RATIO");
                        //display the error message and not allow them to save it
                        mode = MODAL_AND_FOCUS;
                        return false;
                    }
                    //check if the mvId, segmentId and tariffSeqId already exist in the database
                    id = tableRow.isInserted()?"":id;
                    boolean validRevAlloc =  checkUniqueRevAlloc(mvId,id,segmentId,tariffSeqId);
                    if (!validRevAlloc)
                    {
                        //if the revenue alloc record exist in the database, display an error msg
                        errorMsg = PolarisUI.getMessage("CF_MV_REV_ALLOC_UNIQUE");
                        //display the error message and not allow them to save it
                        mode = MODAL_AND_FOCUS;
                        return false;
                    }
                    //This will get total alloc. ratio
                    //for the specific tariff sequence from the database.  The sum will not include the segment
                    //being updated or inserted.
                    String sumAllocRatio = getSumAllocRatio(mvId,segmentId,tariffSeqId);
                    //it will take the sum from the database and add it to the current row being inserted or updated.
                    double totalAllocRatio = Double.parseDouble(sumAllocRatio) + Double.parseDouble(allocRatio);
                    if (totalAllocRatio > 1)
                    {
                        //if the total is greater than 1, display error message.
                        IdNameBag idNameBag = (IdNameBag) ComponentFactory.getDBIdNameFactory("TARIFF_SEQUENCE_CONF", "").load();
                        String tariffSeqName = "";
                        if (!tariffSeqId.isEmpty())
                            tariffSeqName = idNameBag.getNameId(Long.parseLong(tariffSeqId)).getName();
                        else
                            tariffSeqName = SpecialItems.NONE;

                        idNameBag = (IdNameBag) ComponentFactory.getDBIdNameFactory("SEGMENT_CONF", "").load();
                        String     segmentName = idNameBag.getNameId(Long.parseLong(segmentId)).getName();

                        String errorParam = "\n\nTariff Sequence: " + tariffSeqName +"\n";
                        errorParam += "Segment: " + segmentName+"\n";
                        errorParam += "Alloc. Ratio: " + allocRatio+"\n";
                        errorParam += "Total Alloc. Ratio: " + totalAllocRatio +"\n\n";

                        //set error message.
                        errorMsg = PolarisUI.getMessage("CF_MV_REV_ALLOC_SUM_ALLOC_RATIO",errorParam);
                        //set mode so that the user has the ability to bypass or re-enter the value.
                        mode = MODAL_AND_FOCUS_AND_BYPASS;
                        return false;
                    }

                }

                return true;
            }

            public String getErrorMessage(Object value)
            {
                String defaultErrorMsg = PolarisUI.getMessage("CF_VALIDATION_SAVE_ERROR",
                        mvRevAllocationsTableModel.getPanelName()) + "\n";
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



}
