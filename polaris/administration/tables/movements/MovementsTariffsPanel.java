package polaris.administration.tables.movements;

import polaris.administration.tables.*;
import polaris.db.ConnectionPool;
import polaris.db.DBConn;
import polaris.frame.PolarisUI;
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
 * Created by TCI-Waleed Elsaid on 02/10/2017.
 */
public class MovementsTariffsPanel extends GenericTabPanel implements ActionListener {
    // the tab table model
    private MovementsTariffsTableModel movementsTariffsTableModel;

    // historical check box, to be used in filtering the tab table results
    private VisiCheckbox historicalFlag;

    public MovementsTariffsPanel(MainPanel mainPanel) throws Exception
    {
        super(mainPanel);
    }


    /**
     * Created by TCI-Waleed Elsaid on 02/10/2017.
     * the column names that will show up in the tab table
     *
     * @return
     */
    @Override
    public String[] getColumnNames()
    {
        return new String[]{"EDIT_MODE", "MOVEMENT_TARIFF_SEQUENCE_XREF.TARIFF_SEQUENCE_ID",
                "TARIFF_CONF.GOVERNING_BODY_ID",
                "TARIFF_CONF.ISSUING_COMPANY_ID",
                "TARIFF_CONF.TARIFF_NUMBER",
                "TARIFF_SEQUENCE_CONF.TARIFF_SEQUENCE",
                "TARIFF_SEQUENCE_CONF.TARIFF_RATE",
                "TARIFF_SEQUENCE_CONF.EFFECTIVE_START_DATE",
                "TARIFF_SEQUENCE_CONF.EFFECTIVE_STOP_DATE",
                "TARIFF_CONF.MINIMUM_TENDER_VOLUME"};
    }

    /**
     * Created by TCI-Waleed Elsaid on 02/10/2017.
     * The table names used in the queries for the tab
     * @return
     */
    @Override
    public String[] getTableNames()
    {
        return new String[]{"MOVEMENT_TARIFF_SEQUENCE_XREF","TARIFF_SEQUENCE_CONF","TARIFF_CONF"};
    }

    /**
     * Created by TCI-Waleed Elsaid on 02/10/2017.
     * Foreign keys that link MOVEMENT_TARIFF_SEQUENCE_XREF table to MOVEMENT_CONF, TARIFF_SEQUENCE_CONF, and TARIFF_CONF
     * @return
     */
    @Override
    public String[] getForeignKeyConstColNames()
    {
        return new String[]{"MOVEMENT_ID", "TARIFF_SEQUENCE_ID", "TARIFF_ID"};
    }

    /**
     * Created by TCI-Waleed Elsaid on 02/10/2017.
     * Columns that get populated when selecting a movement_conf record
     * @return
     */
    @Override
    public String[][] getSelectColNames()
    {
        return new String[][]{{}, {"TARIFF_SEQUENCE", "TARIFF_RATE", "EFFECTIVE_START_DATE", "EFFECTIVE_STOP_DATE"},
                {"GOVERNING_BODY_ID", "ISSUING_COMPANY_ID", "TARIFF_NUMBER", "MINIMUM_TENDER_VOLUME"}};
    }


    /**
     * Created by TCI-Waleed Elsaid on 02/10/2017.
     * The order by clause used to in the query to populate the select columns from the MOVEMENT_TARIFF_SEQUENCE_XREF
     * @return
     */
    @Override
    public String getOrderBy()
    {
        return "ORDER BY TARIFF_CONF.NAME, TARIFF_CONF.TARIFF_NUMBER,TARIFF_SEQUENCE_CONF.TARIFF_SEQUENCE ";
    }

    /**
     * Created by TCI-Waleed Elsaid on 02/10/2017.
     */
    @Override
    public void createPanel()
    {
        //populate the table's column definitions.
        tabCRS = new ColumnRecordSet(tableNames, selectColNames, foreignKeyConstColNames);
        //creates the tables TableModel
        movementsTariffsTableModel = new MovementsTariffsPanel.MovementsTariffsTableModel(tabCRS, columnNames);
        //creates the TableColumnModel
        GenericTableColumnModel movementsTariffsColumnModel = new GenericTableColumnModel(columnNames, tabCRS);

        //creates the table.
        tabTable = new GenericTable(new TableSorter(movementsTariffsTableModel));
        tabTable.setMainPanel(mainPanel);
        tabTable.setColumnModel(movementsTariffsColumnModel);
        tabTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabTable.setFillsViewportHeight(true);
        tabTable.setIntercellSpacing(new Dimension(0, 0));

        //makes the Panel scrollable.
        JScrollPane tableScrollPanel = new JScrollPane(tabTable);
        tableScrollPanel.setPreferredSize(new Dimension(300, 100));

        //gets table's ResultRecordSet
        tabRS = movementsTariffsTableModel.getResultSet();

        //change listener for the main table.  If users selects a different row
        //on the table grid. it will trigger this listener on the ResultRecordSet.
        ListSelectionModel listSelectionModel = (ListSelectionModel) tabTable.getSelectionModel();
        listSelectionModel.addListSelectionListener(tabRS);

        //listener for any changes to the table Model.
        movementsTariffsTableModel.addTableModelListener(tabRS);

        //Associates the JTable to the ResultRecordSet.
        tabRS.setTable(tabTable);

        historicalFlag = new VisiCheckbox(PolarisUI.getMessage("TARIFF_HISTORICAL_FILTER"));
        historicalFlag.setTableModelClassName(movementsTariffsTableModel.getClass().getName());
        // Add the check box to this panel
        this.addToThisPanel(0, 0, 1, 1, historicalFlag);
        // and add the tabTable
        this.addToThisPanel(0, 1, 1, 1, tableScrollPanel, true, true);

    }

    /**
     * Created by TCI-Waleed Elsaid on 02/10/2017.
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
     * Created by TCI-Waleed Elsaid on 02/10/2017.
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

        if (lookUpTable.equals("TARIFF_SEQUENCE_CONF"))
        {
            //where clause for the model factory.
            //if this is in readonly/InactiveView
            //then get all records from the database
            //else if viewing in activeView/not readonly
            //then get only records that are active.
            whereClause = ComponentFactory.getDBIdNameFactoryWhereClause(activeView);

            //if the look up table is TARIFF_SEQUENCE_CONF and valid main table row
            //then create model factory.
            String gradeSpecId  = "";

            //get the main table's GRADE_SPEC_ID value.
            Field gradeSpecIdField  = resultRS.getField("GRADE_SPEC_ID", mainTableRow);

            //get the gradeSpecId  values from the field.
            gradeSpecId  = (gradeSpecIdField == null || gradeSpecIdField.getValue() == null) ? "" : gradeSpecIdField.getValue();

            if (columnName.equals("MOVEMENT_TARIFF_SEQUENCE_XREF.TARIFF_SEQUENCE_ID"))
            {
                if (!gradeSpecId.isEmpty())
                {
                    // Build the where clause using by appending a sub query at the end that includes the list of segment ID's that are valid
                    whereClause += " AND ID IN (" + getQuery(QueryType.TARIFF_SEQUENCE_IDS, gradeSpecId) + ")";
                }

            }
        }
        // Now we have to filter tariff sequences based on the Historical checkbox.
        // Rule is : If non-historical records are desired, pull TARIFF_SEQUENCE_CONF records where EFFECTIVE_STOP_DATE is NULL.
        if(!historicalFlag.isSelected())
        {
            whereClause += " AND TARIFF_SEQUENCE_CONF.EFFECTIVE_STOP_DATE IS NULL ";
        }
        System.out.println("at the end of getDBNameFactoryWhereClause whereClause is "+whereClause);
        return whereClause;

    }


    /**
     * Created by TCI-Waleed Elsaid on 02/10/2017.
     * @param queryType
     * @param queryParameters
     * @return
     */
    protected String getQuery(QueryType queryType, String... queryParameters)
    {
        String query = "";

        switch (queryType)
        {
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
                query += "  TO_CHAR (TARIFF_SEQUENCE_CONF.EFFECTIVE_STOP_DATE,'" + Configuration.defaultDateFormat + "'), \n";
                query += "  TARIFF_CONF.MINIMUM_TENDER_VOLUME \n";
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
        TARIFF_SEQUENCE_IDS,
        GET_TARIFF_SEQUENCE_FIELDS
    }


    /**
     * Created by TCI-Waleed Elsaid on 02/10/2017.
     * Given the selected tariff from the tariffs combo box,
     * This method will query the TARIFF_SEQUENCE_CONF and populate specific columns in the tab table
     * When the user selects an item from Segment combo box The following columns will be populated:
     *  Gov. Body, Issue Co., Tariff No., Seq., Rate, Eff. Start Date and Eff. Stop Date
     * @param tariffSequenceId  - tariff ID of the value selected from combo box
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
                    for (int i = 2; i < names.length; i++)
                    {
                        //gets the value for each column in the recordset.
                        String value  = rs.getString(i - 1);
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

    @Override
    /**
     * Created by TCI-Waleed Elsaid on 02/10/2017.
     * This method sets the validators to be called at the save action
     */
    protected void setValidators()
    {
        //sets the validator for a column in the ColumnRecordSet.
        for (int i = 0; i < tabCRS.size(); i++)
        {
            Column column = tabCRS.elementAt(i);
            if (    column.columnName.equals("MOVEMENT_TARIFF_SEQUENCE_XREF.TARIFF_SEQUENCE_ID") )
            {
                column.setValidator(defaultValidator(column));
            }
        }
    }

    @Override
    /**
     * Created by TCI-Waleed Elsaid on 02/10/2017.
     * This method overrides GenericTabPanel.getWhereClause to check for the Historical check box status
     * and update the where clause correspondingly
     */
    public String getWhereClause()
    {
        String whereClause = super.getWhereClause();
        if(!historicalFlag.isSelected())
        {
            whereClause += " AND TARIFF_SEQUENCE_CONF.EFFECTIVE_STOP_DATE IS NULL ";
        }
        return whereClause;
    }

    /**
     * Created by TCI-Waleed Elsaid on 02/10/2017.
     * The tab table model
     */
    public class MovementsTariffsTableModel extends GenericTableModel
    {
        public MovementsTariffsTableModel(ColumnRecordSet crs, String[] names)
        {
            super(crs, names, false, PolarisUI.getMessage("CF_TARIFFS_PANEL", PolarisUI.getMessage("MOVEMENT")));
        }

        public void setValueAt(Object aValue, int row, int col)
        {
            super.setValueAt(aValue, row, col);

            //if user updates the tariffs columns
            if (col == 1)
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


        public boolean isCellEditable(int row, int col)
        {
            //the only field that should be editable is column #1 (the tariffs)
            boolean colEditable = super.isCellEditable(row,col) && col==1?true:false;

            //checks if the main table is readonly.
            //if read only make sure all related tab fields and tables are not editable as well.
            boolean editable = resultRS.isReadOnly() || tabRS.isReadOnly() ? false : true&&colEditable;

            return editable;


        }

    }
}
