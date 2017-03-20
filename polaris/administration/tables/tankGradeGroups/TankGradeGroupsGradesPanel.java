package polaris.administration.tables.tankGradeGroups;

import polaris.administration.tables.*;
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
 * Creates the Tank Grade Groups Grades Panel.
 *
 * @Author TCI - Krista Riggs
 * Created  1/9/2017
 * Updated :
 * 1/26/17 - TCI Krista Riggs - added comments
 * 2/3/17 - TCI Krista Riggs - Update the MULTIPLE_INTERVAL_SELECTION so that multiple rows can
 * be selected at one time
 */
public class TankGradeGroupsGradesPanel extends GenericTabPanel
{
    public TankGradeGroupsGradesTableModel tankGrdGrpsGradesTableModel;

    /**
     * Instantiate the Grades Panel
     *
     * @param mainPanel - the Grades Panel
     * @throws Exception
     */
    public TankGradeGroupsGradesPanel(MainPanel mainPanel) throws Exception
    {
        super(mainPanel);
    }

    /**
     * gets a list of columns to display on the table.
     *
     * @return - The column names
     */
    @Override
    public String[] getColumnNames()
    {
        return new String[]{"EDIT_MODE", "TANK_GRADE_GROUP_GRADE_XREF.GRADE_ID", "GRADE_CONF.FLUID_TYPE_ID",
                            "GRADE_CONF.DESCRIPTION"};
    }

    /**
     * gets a list of db tables to display on the table
     *
     * @return - the table names
     */
    @Override
    public String[] getTableNames()
    {
        return new String[]{"TANK_GRADE_GROUP_GRADE_XREF", "GRADE_CONF"};
    }

    /**
     * Stores a list of foreign Key Col Names.  In this instance two tables are displayed in the grid.
     * therefore it should  have two foreign key col names.
     *
     * @return - the foreign key column names
     */
    @Override
    public String[] getForeignKeyConstColNames()
    {
        return new String[]{"TANK_GRADE_GROUP_ID", "GRADE_ID"};
    }

    /**
     * Get the column names
     *
     * @return - The column names
     */
    @Override
    public String[][] getSelectColNames()
    {
        return new String[][]{{}, {"FLUID_TYPE_ID", "NAME", "DESCRIPTION"}};
    }

    /**
     * Gets the column to order by
     *
     * @return - the column to order by
     */
    @Override
    public String getOrderBy()
    {
        return " ORDER BY GRADE_CONF.NAME";
    }

    /**
     * Create the Grades Panel
     */
    @Override
    public void createPanel()
    {
        //populate the table's column definitions.
        tabCRS = new ColumnRecordSet(tableNames, selectColNames, foreignKeyConstColNames);
        //creates the tables TableModel
        tankGrdGrpsGradesTableModel = new TankGradeGroupsGradesTableModel(tabCRS, columnNames);
        //creates the TableColumnModel
        GenericTableColumnModel tankGrdGrpsGradesTableColumnModel = new GenericTableColumnModel(columnNames, tabCRS);

        //creates the table.
        tabTable = new GenericTable(new TableSorter(tankGrdGrpsGradesTableModel));
        tabTable.setMainPanel(mainPanel);
        tabTable.setColumnModel(tankGrdGrpsGradesTableColumnModel);
        tabTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabTable.setFillsViewportHeight(true);
        tabTable.setIntercellSpacing(new Dimension(0, 0));
        //makes the Panel scrollable.
        JScrollPane tableScrollPanel = new JScrollPane(tabTable);
        tableScrollPanel.setPreferredSize(new Dimension(300, 100));

        //gets table's ResultRecordSet.
        tabRS = tankGrdGrpsGradesTableModel.getResultSet();

        //Change listener for the main table.
        //If a user selects a different row on the table grid it will trigger this listener on the ResultRecordSet.
        ListSelectionModel listSelectionModel = (ListSelectionModel) tabTable.getSelectionModel();
        listSelectionModel.addListSelectionListener(tabRS);

        //listener for any changes to the table Model.
        tankGrdGrpsGradesTableModel.addTableModelListener(tabRS);

        //Associates the JTable to the ResultRecordSet.
        tabRS.setTable(tabTable);
        //add the scrollable table to the panel
        this.addToThisPanel(0, 0, 1, tableScrollPanel, true, true);
    }

    /**
     * Set the Validators for the Grades Panel
     * The Grade column cannot be null, empty, or duplicate
     */
    @Override
    protected void setValidators()
    {
        ColumnRecordSet crs = this.tankGrdGrpsGradesTableModel.getColumnRecordSet();

        // Loop through the columnNames and set the default validator
        for (int i = 0; i < crs.size(); i++)
        {
            // Validate defaults
            Column column = crs.elementAt(i);

            // Only validate the Grade column combo box as it is the only editable column
            if (column.columnName.equals("TANK_GRADE_GROUP_GRADE_XREF.GRADE_ID"))
            {
                // Set the default validator
                column.setValidator(defaultValidator(column));

                // Found this columnName, break and search for next one
                break;
            }
        }
    }

    /**
     * When the user selects an item from Grade combo box The following columns will be populated:
     * Fluid Type and Description
     *
     * @param gradeId  - grade ID of the value selected from the Grade combo box
     * @param tableRow - the current row being edited.
     * @throws SQLException
     */
    public void setGradeFields(long gradeId, TableRow tableRow) throws SQLException
    {
        System.out.println("Getting a grade record");
        DBConn    dbconn = ConnectionPool.getConnection();
        Statement stmt   = null;
        try
        {
            //create db connection
            stmt = dbconn.connection.createStatement();
            String columnNames = "";
            for (int i = 0; i < selectColNames[1].length; i++)
            {
                //get the column names
                columnNames = columnNames + selectColNames[1][i];
                if (i < selectColNames[1].length - 1)
                {
                    columnNames = columnNames + ", ";
                }
            }
            //Create the query string to retrieve the information for each column based on the grade selected
            String    query = "SELECT " + columnNames + " FROM POLARIS." + tableNames[1] + " WHERE ID = " + gradeId;
            ResultSet rs    = stmt.executeQuery(query);
            while (rs.next())
            {
                //Retrieve the name of the table
                String tableNamePrefix = tableNames[1] + ".";
                for (int i = 0; i < selectColNames[1].length; i++)
                {
                    //for each column get the information using the result set
                    String value  = rs.getString(i + 1);
                    Field  field  = tabRS.getField(tableNamePrefix + selectColNames[1][i], tableRow);
                    Column column = field.getColumn();

                    if (value != null)
                    {
                        //if not null and it is a foreign key set the long value
                        if (column.foreignKey || (column.dataType.equals("NUMBER") && column.columnName.contains(
                                "_ID")))
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
                    }
                    else
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
            {//if no statement is created close the db connection
                stmt.close();
            }
            ConnectionPool.releaseConnection(dbconn);
        }
    }

    /**
     * Create the Tank Grade Group Grades Table Model
     */
    public class TankGradeGroupsGradesTableModel extends GenericTableModel
    {
        /**
         * Instantiate the TankGradeGroupsGradesTableModel
         */

        public TankGradeGroupsGradesTableModel(ColumnRecordSet crs, String[] names)
        {
            super(crs, names, false, PolarisUI.getMessage("CF_GRADES_PANEL", PolarisUI.getMessage("TANK_GRADE_GROUP")));
        }

        /**
         * Create setValueAt
         *
         * @param aValue - the value
         * @param row    - the row
         * @param col    - the column
         */
        public void setValueAt(Object aValue, int row, int col)
        {
            super.setValueAt(aValue, row, col);

            //if it is grade column
            if (col == 1)
            {
                TableRow dbRow = (TableRow) resultSet.elementAt(row);
                Field    field = resultSet.getField(getColumnName(col), dbRow);
                if (field.isUpdated() && field.getLong() != null)
                {//if the grade field is updated and it is not null
                    try
                    {//set the grade field to the selected grade
                        setGradeFields(field.getLong(), dbRow);
                    }
                    catch (SQLException sqlex)
                    {//display error message dialog if there is an error
                        JOptionPane.showMessageDialog(null, sqlex.getMessage(), "SQL Error:" + sqlex.getErrorCode(),
                                                      JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }

        /**
         * Create boolean to determine if the cell is editable
         *
         * @param row - the row
         * @param col - the column
         * @return - editable T/F
         */
        public boolean isCellEditable(int row, int col)
        {
            //the only field that should be editable is col 1 GRADE_ID
            boolean colEditable = super.isCellEditable(row, col) && col == 1 ? true : false;

            //checks if the main table is readonly.
            //if read only make sure all related tab fields and tables are not editable as well.
            boolean editable = resultRS.isReadOnly() || tabRS.isReadOnly() ? false : true && colEditable;
            return editable;
        }
    }
}


