package polaris.administration.tables.batchRoutes;

import polaris.administration.tables.*;
import polaris.constants.RecDelDesc;
import polaris.dates.NrGDate;
import polaris.constants.SpecialItems;
import polaris.db.ConnectionPool;
import polaris.db.DBConn;
import polaris.frame.ContextHelpAction;
import polaris.frame.PolarisUI;
import polaris.util.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Creates the Meters Tab Panel for a configuration screen.
 */
public class BatchRoutesMetersPanel extends GenericTabPanel implements ActionListener
{
    public BatchRoutesMetersTableModel brMetersTableModel;

    private VisiComboBox defaultRecMeterCombo;
    private VisiComboBox defaultDelMeterCombo;

    /**
     * Instanciates the Maintenance Panel.
     *
     * @param mainPanel
     * @throws Exception
     */
    public BatchRoutesMetersPanel(MainPanel mainPanel) throws Exception
    {
        super(mainPanel);


    }

    @Override
    public String[] getColumnNames()
    {
        return new String[]{"EDIT_MODE", "REC_DEL_ID", "METER_ID"};
    }

    @Override
    public String[] getTableNames()
    {
        return new String[]{"BATCH_ROUTE_METER_XREF"};
    }

    @Override
    public String[] getForeignKeyConstColNames()
    {
        return new String[]{"BATCH_ROUTE_ID"};
    }

    @Override
    public String[][] getSelectColNames()
    {
        return new String[][]{{}, {}};
    }

    @Override
    public String getOrderBy()
    {
        return null;
    }

    @Override
    public void createPanel()
    {


        //populate the table's column definitions.
        tabCRS = new ColumnRecordSet(tableNames, selectColNames, foreignKeyConstColNames);
        //creates the tables TableModel
        brMetersTableModel = new BatchRoutesMetersTableModel(tabCRS, columnNames);
        //creates the TableColumnModel
        GenericTableColumnModel brMetersTableColumnModel = new GenericTableColumnModel(columnNames, tabCRS);

        //creates the table.
        tabTable = new GenericTable(new TableSorter(brMetersTableModel));
        tabTable.setMainPanel(mainPanel);
        tabTable.setColumnModel(brMetersTableColumnModel);
        tabTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabTable.setFillsViewportHeight(true);
        tabTable.setIntercellSpacing(new Dimension(0, 0));

        //makes the Panel scrollable.
        JScrollPane tableScrollPanel = new JScrollPane(tabTable);
        tableScrollPanel.setPreferredSize(new Dimension(300, 100));
        tableScrollPanel.setBorder(
                BorderFactory.createTitledBorder(new LineBorder(Color.GRAY, 1, true), PolarisUI.getMessage("BATCH_ROUTE_METERS_XREF.REC_DEL_METERS")));

        //gets table's ResultRecordSet
        tabRS = brMetersTableModel.getResultSet();

        //change listener for the main table.  If users selects a different row
        //on the table grid. it will trigger this listener on the ResultRecordSet.
        ListSelectionModel listSelectionModel = (ListSelectionModel) tabTable.getSelectionModel();
        listSelectionModel.addListSelectionListener(tabRS);

        //listener for any changes to the table Model.
        brMetersTableModel.addTableModelListener(tabRS);

        //Associates the JTable to the ResultRecordSet.
        tabRS.setTable(tabTable);


        defaultRecMeterCombo = ComponentFactory.getMeterComboBox("", Configuration.defaultRecMeterDBName,
                                                                 SpecialItems.NONE);
        defaultDelMeterCombo = ComponentFactory.getMeterComboBox("", Configuration.defaultDelMeterDBName,
                                                                 SpecialItems.NONE);


        //sets the tableModel class name for each data entry component.
        //these components are associated to the main table's table model.
        defaultRecMeterCombo.setTableModelClassName(tableModel.getClass().getName());
        defaultDelMeterCombo.setTableModelClassName(tableModel.getClass().getName());

        VisiGridBagPanel panelRight = new VisiGridBagPanel();
        panelRight.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY, 1, true),
                                                              PolarisUI.getMessage("CF_DEFAULT") + " " + PolarisUI.getMessage("CF_METERS")));
        panelRight.addToThisPanelAutoLayout(1, "CF_RECEIPT", defaultRecMeterCombo);
        panelRight.addToThisPanelAutoLayout(1, "CF_DELIVERY", defaultDelMeterCombo);

        this.resetGridBag();

        this.addToThisPanel(0, 0, 1, 1, tableScrollPanel, true, true);
        this.addToThisPanel(1, 0, 1, 1, panelRight, true, true);

    }

    /**
     * set Help Topic Id
     */
    @Override
    public void setHelp()
    {
        this.mainPanel.setContextHelpTopicId("BatchRoutesConfigurationModule_MetersTab");
    }

    @Override
    protected void setValidators()
    {
        //sets the validator for a column in the ColumnRecordSet.

        for (int i = 0; i < tabCRS.size(); i++)
        {
            Column column = tabCRS.elementAt(i);
            if (column.columnName.equals("REC_DEL_ID"))
            {
                column.setValidator(meterValidator(column));
            }

            if (column.columnName.equals("METER_ID"))
            {
                column.setValidator(meterValidator(column));
            }


        }

    }

    private ColumnValidator meterValidator(Column column)
    {


        ColumnValidator validator = new ColumnValidator(column)
        {


            private String errorMsg = "";

            public boolean validate(String text, String brId) throws SQLException
            {
                errorMsg = "";


                return true;

            }


            public boolean validate(Object value, String brId) throws SQLException
            {

                errorMsg = "";
                if (brId.equals(ResultRecordSet.newKey))
                {
                    return true;
                }


                if (value == null || value.equals(new Long(0)))
                {
                    //errorMsg="Rec/Del or Meter cannot be empty.  Please enter a value into the Rec/Del and Meter field before saving this row.";
                    errorMsg = PolarisUI.getMessage("CF_VALIDATION_NULL", "Rec./Del. and Meter");
                    return false;
                }


                TableRow mainTableRow = resultRS.getTableRowAtCursor();
                Field    recLocField  = resultRS.getField("REC_LOCATION_ID", mainTableRow);
                Field    delLocField  = resultRS.getField("DEL_LOCATION_ID", mainTableRow);


                //get tab's selected table row.
                TableRow tableRow    = tabRS.getTableRowAtCursor();
                Field    recDelField = tabRS.getField("REC_DEL_ID", tableRow);
                Field    meterField  = tabRS.getField("METER_ID", tableRow);


                //if rec_del_id is null return false;
                if (recDelField == null || meterField == null || recLocField == null || delLocField == null)
                {
                    //errorMsg = "Must have values for Rec/Del, Meter, Receipt Location and Delivery Location.\nPlease enter values into the field(s) before saving this row.";
                    errorMsg = PolarisUI.getMessage("CF_BR_METERS_NULL_VALUES2");
                    return false;
                }


                String recDelId = recDelField.getValue() == null ? "" : recDelField.getValue();
                String meterId  = meterField.getValue() == null ? "" : meterField.getValue();

                // if rec_del_id and meter is empty return false;
                if (recDelId.isEmpty() || meterId.isEmpty())
                {
                    //errorMsg = "Rec/Del or Meter cannot be empty.  Please enter a value into the Rec/Del and Meter field before saving this row.";
                    errorMsg = PolarisUI.getMessage("CF_VALIDATION_NULL","Rec./Del. and Meter");
                    return false;
                } else
                {
                    //check if the meter selected is valid for the rec/del location of the batch route.
                    boolean validMeterId = isValidMeter(brId, recDelId, meterId);


                    if (!validMeterId)
                    {
                        //if not valid display an error message.
                        String recDelValue = recDelId.equals(String.valueOf(RecDelDesc.RECEIPT)) ? "Receipt"
                                                                                                 : "Delivery";
                        String locationValue = recDelId.equals(String.valueOf(RecDelDesc.RECEIPT)) ?
                                               recLocField.getValue() == null ? "" : recLocField.getValue() :
                                               delLocField.getValue() == null ? "" : delLocField.getValue();


                        IdNameBag idNameBag = (IdNameBag) ComponentFactory.getDBIdNameFactory("METER_CONF", "").load();
                        String    meterName = idNameBag.getNameId(Long.parseLong(meterId)).getName();
                        idNameBag = (IdNameBag) ComponentFactory.getDBIdNameFactory("LOCATION_CONF", "").load();
                        String locationName = idNameBag.getNameId(Long.parseLong(locationValue)).getName();

                        /*errorMsg =
                                "The Meter selected (" + meterName + ") is not a valid meter for the " + recDelValue +
                                " Location (" + locationName + ").\n";
                        errorMsg += "Select another meter that has this location: " + locationName;*/

                        errorMsg = PolarisUI.getMessage("CF_BR_METERS_INVALID_METER",meterName,recDelValue,locationName);



                        return false;
                    }


                }

                return true;
            }

            public String getErrorMessage(Object value)
            {
                String defaultErrorMsg = PolarisUI.getMessage("CF_VALIDATION_SAVE_ERROR",
                                                              brMetersTableModel.getPanelName()) + "\n";


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
     * Verifies that the selected meter is valid for the rec/del location of the batch route.
     *
     * @return returns false if not valid
     * @throws SQLException
     */
    private boolean isValidMeter(String brId, String recDelId, String meterId) throws SQLException
    {
        boolean valid = false;

        DBConn    dbconn = ConnectionPool.getConnection();
        Statement stmt   = null;
        String    value  = "";
        try
        {
            stmt = dbconn.connection.createStatement();

            String    query = getQuery(QueryType.VALID_METER, brId, recDelId, meterId);
            ResultSet rs    = stmt.executeQuery(query);
            int       count = 0;
            while (rs.next())
            {
                count++;
                value = rs.getString(1);
                break;
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

        //if equal to meterId, then valid.
        valid = value.equals(meterId) ? true : false;

        return valid;
    }




    /**
     * Gets the where clause for the DBNameFactory.  Used to populate the combo boxes model factory.
     *
     * @param columnName   -  column name of the table field.
     * @param lookUpTable  - table name
     * @param mainTableRow - the selected main table's row.
     * @return
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

        //look up tables for REC_DEL_DESC does not require any filter.

        //the only other lookup table that requires a where clause is METER_CONF
        //code below is specifically for METER_CONF.

        //for meter's tab only the METER_ID column requires a where clause.
        if (lookUpTable.equals("METER_CONF"))
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

            if (columnName.equals("METER_ID"))
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

            } else if (columnName.equals("DEFAULT_REC_METER_ID"))
            {
                //filter the combobox by the receipt location id
                locIDs = recLocId == -1 ? "" : "" + recLocId;
            } else if (columnName.equals("DEFAULT_DEL_METER_ID"))
            {
                //filter the combobox by the delivery location id
                locIDs = delLocId == -1 ? "" : "" + delLocId;
            }

            if (!locIDs.isEmpty())
            {
                whereClause += " AND LOCATION_ID IN (" + locIDs + ")";
            }
        }
        return whereClause;

    }


    /**
     * method is triggered when components (comboboxes) in the  panel has an action being done on it
     * actions on comboboxes will set the field to updated and the tablerow object to upated.
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e)
    {

        //if the component does not have focus
        //return out of this method.
        //this occurs if action being done on the component is from another method
        //not from user doing the action
        Component component = (Component) e.getSource();
        if (!component.hasFocus())
        {
            return;
        }

        //get tablerow object from the current selected row.
        TableRow tableRow = resultRS.getTableRowAtCursor();

        if (resultRS.isEmpty() || tableRow == null)
        {
            //no data in the main grid and tableRow is not valid
            return;
        }

        ui.setBusyCursor();

        String action = e.getActionCommand();

        if (e.getSource() instanceof VisiComboBox || e.getSource() instanceof NrGDate)
        {
            //set the update flag to true on the tablerow.field
            //and also on the tablerow
            resultRS.setUpdated(e.getSource(), tableRow);
            //sets the dirty flag to panel to true.
            setDirty(true);
        }

        ui.setDefaultCursor();


    }

    protected String getQuery(QueryType queryType, String... queryParameters)
    {
        String query = "";
        switch (queryType)
        {
            case VALID_METER:

                String locID = "";
                if (queryParameters[1].equals(String.valueOf(RecDelDesc.RECEIPT)))
                {
                    locID = "		BR.REC_LOCATION_ID = M.LOCATION_ID AND \n";
                } else
                {
                    locID = "		BR.DEL_LOCATION_ID = M.LOCATION_ID AND \n";
                }

                query = "SELECT ";
                query += "		M.id, \n";
                query += "		M.NAME \n";
                query += "FROM \n";
                query += "POLARIS.batch_route_conf	BR, \n";
                query += "POLARIS.meter_conf	M \n";
                query += "WHERE ";
                query += "		BR.ID =" + queryParameters[0] + " and \n";
                query += locID;
                query += "		M.ID =" + queryParameters[2] + "  \n";
                query += "ORDER BY \n";
                query += "		M.ID \n";
                break;
        }

        return query;
    }

    protected enum QueryType
    {
        VALID_METER
    }

    public class BatchRoutesMetersTableModel extends GenericTableModel
    {


        public BatchRoutesMetersTableModel(ColumnRecordSet crs, String[] names)
        {
            super(crs, names, false, PolarisUI.getMessage("CF_METERS_PANEL", PolarisUI.getMessage("BATCH_ROUTE")));


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


