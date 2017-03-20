package polaris.administration.tables.facilities;


import polaris.administration.tables.*;
import polaris.db.ConnectionPool;
import polaris.db.DBConn;
import polaris.frame.ContextHelpAction;
import polaris.frame.PolarisUI;
import polaris.util.TableSorter;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * @Author TCI Krista Riggs
 * Created 3/15/2017.
 */
public class FacilitiesContactsPanel extends GenericTabPanel
{
    public FacilitiesContactsTableModel facilitiesContactsTableModel;

    public FacilitiesContactsPanel(MainPanel mainPanel) throws Exception
    {
        super(mainPanel);
    }

    @Override
    public void createPanel()
    {

        //populate the table's column definitions.
        tabCRS = new ColumnRecordSet(tableNames, selectColNames, foreignKeyConstColNames);
        //creates the tables TableModel
        facilitiesContactsTableModel = new FacilitiesContactsTableModel(tabCRS, columnNames);
        //creates the TableColumnModel
        GenericTableColumnModel facilitiesContactsTableColumnModel = new GenericTableColumnModel(columnNames, tabCRS);

        //creates the table.
        tabTable = new GenericTable(new TableSorter(facilitiesContactsTableModel));
        tabTable.setMainPanel(mainPanel);
        tabTable.setColumnModel(facilitiesContactsTableColumnModel);
        tabTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabTable.setFillsViewportHeight(true);
        tabTable.setIntercellSpacing(new Dimension(0, 0));
        //makes the Panel scrollable.
        JScrollPane tableScrollPanel = new JScrollPane(tabTable);
        tableScrollPanel.setPreferredSize(new Dimension(300, 100));
        tableScrollPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY, 1, true), PolarisUI
                .getMessage("FACILITY_LOCATION_CONTACT_XREF.LOCATION_CONTACTS")));

        //gets table's ResultRecordSet.
        tabRS = facilitiesContactsTableModel.getResultSet();


        //change listener for the main table.  If users selects a different row
        //on the table grid. it will trigger this listener on the ResultRecordSet.
        ListSelectionModel listSelectionModel = (ListSelectionModel) tabTable.getSelectionModel();
        listSelectionModel.addListSelectionListener(tabRS);

        //listener for any changes to the table Model.
        facilitiesContactsTableModel.addTableModelListener(tabRS);

        //Associates the JTable to the ResultRecordSet.
        tabRS.setTable(tabTable);

        ContactPanel contactPanel = new ContactPanel(this, facilitiesContactsTableModel, false);
        contactPanel.displayAddressPanel();
        contactPanel.displayPhoneNumberPanel();

        this.resetGridBag();

        this.addToThisPanel(0, 0,1, 1, tableScrollPanel, true, true);
        this.addToThisPanel(1,0,1,1,contactPanel,true,true);
    }

    @Override
    protected void setValidators()
    {

    }

    @Override
    public String[] getColumnNames()
    {
        // Associate fields from FACILITY_LOCATION_CONTACT_XREF
        return new String[]{"EDIT_MODE", "FACILITY_LOCATION_CONTACT_XREF.LOCATION_ID",
                            "FACILITY_LOCATION_CONTACT_XREF.CONTACT_ID"};
    }

    @Override
    public String[] getTableNames()
    {
        // The FACILITY_LOCATION_CONTACT_XREF is the main table that data is displayed in the tab table.
        // The CONTACT_CONF table is required for the ContactPanel read only fields to get linked to the CONTACT_CONF records.
        return new String[]{"FACILITY_LOCATION_CONTACT_XREF", "CONTACT_CONF"};
    }

    @Override
    public String[] getForeignKeyConstColNames()
    {
        return new String[]{"FACILITY_ID", "CONTACT_ID"};
    }

    @Override
    public String[][] getSelectColNames()
    {
        return new String[][]{{}, {}};
    }

    @Override
    public String getOrderBy()
    {
        // Order by LOCATION_ID
        return "ORDER BY LOCATION_ID ";
    }

    /**
     * Create the Facilities Contacts Table Model
     */
    public class FacilitiesContactsTableModel extends GenericTableModel
    {
        /**
         * Instantiate the FacilitiesContactsTableModel
         */

        public FacilitiesContactsTableModel(ColumnRecordSet crs, String[] names)
        {
            super(crs, names, false, PolarisUI.getMessage("CF_CONTACTS_PANEL", PolarisUI.getMessage("FACILITIES")));
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
//                    try
//                    {//set the grade field to the selected grade
//                        setGradeFields(field.getLong(), dbRow);
//                    }
//                    catch (SQLException sqlex)
//                    {//display error message dialog if there is an error
//                        JOptionPane.showMessageDialog(null, sqlex.getMessage(), "SQL Error:" + sqlex.getErrorCode(),
//                                                      JOptionPane.ERROR_MESSAGE);
//                    }
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
