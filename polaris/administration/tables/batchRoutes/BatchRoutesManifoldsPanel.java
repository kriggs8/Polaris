package polaris.administration.tables.batchRoutes;

import polaris.administration.tables.*;
import polaris.constants.RecDelDesc;
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
 * Creates the Manifolds Tab Panel for a configuration screen.
 */
public class BatchRoutesManifoldsPanel extends GenericTabPanel
        implements ActionListener

{


    public BatchRoutesManifoldsTableModel brManifoldsTableModel;

    private VisiComboBox defaultRecManifoldCombo;
    private VisiComboBox defaultDelManifoldCombo;


    /**
     * Instantiates the Manifold Tab Panel
     *
     * @param mainPanel
     * @throws Exception
     */
    public BatchRoutesManifoldsPanel(MainPanel mainPanel) throws Exception
    {
        super(mainPanel);
    }

    @Override
    public String[] getColumnNames()
    {
        //gets a list of column names to display on the table.
        return new String[]{"EDIT_MODE", "REC_DEL_ID", "MANIFOLD_ID"};
    }

    @Override
    public String[] getTableNames()
    {
        //gets a list of db tables to display on the table.
        return new String[]{"BATCH_ROUTE_MANIFOLD_XREF"};
    }

    @Override
    public String[] getForeignKeyConstColNames()
    {
        //gets a list of foreign Key Col Names.  if Only table in the tableName list, then
        // it should only have one foreign Key Col Name.
        return new String[]{"BATCH_ROUTE_ID"};
    }

    @Override
    public String[][] getSelectColNames()
    {
        //gets a list of table columns to get table defintions.
        //in this instance it selects all columns.
        return new String[][]{{}, {}};
    }

    @Override
    public String getOrderBy()
    {
        //no column to sort by
        return null;
    }

    @Override
    public void createPanel()
    {

        //populate the table's column definitions.
        tabCRS = new ColumnRecordSet(tableNames, selectColNames, foreignKeyConstColNames);
        //creates the tables TableModel
        brManifoldsTableModel = new BatchRoutesManifoldsTableModel(tabCRS, columnNames);
        //creates the TableColumnModel
        GenericTableColumnModel brManifoldsTableColumnModel = new GenericTableColumnModel(columnNames, tabCRS);

        //creates the  table.
        tabTable = new GenericTable(new TableSorter(brManifoldsTableModel));
        tabTable.setMainPanel(mainPanel);
        tabTable.setColumnModel(brManifoldsTableColumnModel);
        tabTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabTable.setFillsViewportHeight(true);
        tabTable.setIntercellSpacing(new Dimension(0, 0));

        //makes the Panel scrollable.
        JScrollPane tableScrollPanel = new JScrollPane(tabTable);
        tableScrollPanel.setPreferredSize(new Dimension(300, 100));
        tableScrollPanel.setBorder(
                BorderFactory.createTitledBorder(new LineBorder(Color.GRAY, 1, true), PolarisUI.getMessage("BATCH_ROUTE_MANIFOLDS_XREF.REC_DEL_MANIFOLDS")));

        //gets table's ResultRecordSet.
        tabRS = brManifoldsTableModel.getResultSet();

        //change listener for the main table.  If users selects a different row
        //on the table grid. it will trigger this listener on the ResultRecordSet.
        ListSelectionModel listSelectionModel = (ListSelectionModel) tabTable.getSelectionModel();
        listSelectionModel.addListSelectionListener(tabRS);

        //listener for any changes to the table Model.
        brManifoldsTableModel.addTableModelListener(tabRS);

        //Associates the JTable to the ResultRecordSet.
        tabRS.setTable(tabTable);


        defaultRecManifoldCombo = ComponentFactory.getManifoldComboBox("", Configuration.defaultRecManifoldDBName,
                                                                       SpecialItems.NONE);
        defaultDelManifoldCombo = ComponentFactory.getManifoldComboBox("", Configuration.defaultDelManifoldDBName,
                                                                       SpecialItems.NONE);

        //sets the tableModel class name for each data entry component.
        //these components are associated to the main table's table model.
        defaultRecManifoldCombo.setTableModelClassName(tableModel.getClass().getName());
        defaultDelManifoldCombo.setTableModelClassName(tableModel.getClass().getName());

        VisiGridBagPanel panelRight = new VisiGridBagPanel();
        panelRight.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY, 1, true), PolarisUI.getMessage("CF_DEFAULT") + " " + PolarisUI.getMessage("CF_MANIFOLDS")));
        panelRight.addToThisPanelAutoLayout(1, PolarisUI.getMessage("CF_RECEIPT"), defaultRecManifoldCombo);
        panelRight.addToThisPanelAutoLayout(1, PolarisUI.getMessage("CF_DELIVERY"), defaultDelManifoldCombo);

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
        this.mainPanel.setContextHelpTopicId("BatchRoutesConfigurationModule_ManifoldsTab");
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
                column.setValidator(manifoldValidator(column));
            }

            if (column.columnName.equals("METER_ID"))
            {
                column.setValidator(manifoldValidator(column));
            }


        }
    }
    private ColumnValidator manifoldValidator(Column column)
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
                    //errorMsg="Rec/Del or Manifold cannot be empty.  Please enter a value into the Rec/Del and Manifold field before saving this row.";
                    errorMsg = PolarisUI.getMessage("CF_BR_MANIFOLDS_NULL_VALUES1");
                    return false;
                }


                TableRow mainTableRow = resultRS.getTableRowAtCursor();
                Field    recLocField  = resultRS.getField("REC_LOCATION_ID", mainTableRow);
                Field    delLocField  = resultRS.getField("DEL_LOCATION_ID", mainTableRow);


                //get tab's selected table row.
                TableRow tableRow    = tabRS.getTableRowAtCursor();
                Field    recDelField = tabRS.getField("REC_DEL_ID", tableRow);
                Field    manifoldField  = tabRS.getField("MANIFOLD_ID", tableRow);


                //if rec_del_id is null return false;
                if (recDelField == null || manifoldField == null || recLocField == null || delLocField == null)
                {
                    //errorMsg = "Must have values for Rec/Del, Manifold, Receipt Location and Delivery Location.\nPlease enter values into the field(s) before saving this row.";
                    errorMsg = PolarisUI.getMessage("CF_BR_MANIFOLDS_NULL_VALUES2");
                    return false;
                }


                String recDelId = recDelField.getValue() == null ? "" : recDelField.getValue();
                String manifoldId  = manifoldField.getValue() == null ? "" : manifoldField.getValue();

                // if rec_del_id and manifold is empty return false;
                if (recDelId.isEmpty() || manifoldId.isEmpty())
                {
                    //errorMsg = "Rec/Del or Manifold cannot be empty.  Please enter a value into the Rec/Del and manifold field before saving this row.";
                    errorMsg = PolarisUI.getMessage("CF_BR_MANIFOLDS_NULL_VALUES1");
                    return false;
                } else
                {
                    //check if the manifold selected is valid for the rec/del location of the batch route.
                    boolean validManifoldId = isValidManfold(brId, recDelId, manifoldId);


                    if (!validManifoldId)
                    {
                        //if not valid display an error message.
                        String recDelValue = recDelId.equals(String.valueOf(RecDelDesc.RECEIPT)) ? "Receipt"
                                                                                                 : "Delivery";
                        String locationValue = recDelId.equals(String.valueOf(RecDelDesc.RECEIPT)) ?
                                               recLocField.getValue() == null ? "" : recLocField.getValue() :
                                               delLocField.getValue() == null ? "" : delLocField.getValue();


                        IdNameBag idNameBag = (IdNameBag) ComponentFactory.getDBIdNameFactory("MANIFOLD_CONF", "").load();
                        String    manifoldName = idNameBag.getNameId(Long.parseLong(manifoldId)).getName();
                        idNameBag = (IdNameBag) ComponentFactory.getDBIdNameFactory("LOCATION_CONF", "").load();
                        String locationName = idNameBag.getNameId(Long.parseLong(locationValue)).getName();

                        /*errorMsg =
                                "The Manifold selected (" + manifoldName + ") is not a valid manifold for the " + recDelValue +
                                " Location (" + locationName + ").\n";
                        errorMsg += "Select another manifold that has this location: " + locationName;*/

                        errorMsg = PolarisUI.getMessage("CF_BR_MANIFOLDS_INVALID_MANIFOLD",manifoldName,recDelValue,locationName);



                        return false;
                    }


                }

                return true;
            }

            public String getErrorMessage(Object value)
            {
                String defaultErrorMsg = PolarisUI.getMessage("CF_VALIDATION_SAVE_ERROR",
                                                              brManifoldsTableModel.getPanelName()) + "\n";


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
     * Verifies that the selected manifold is valid for the rec/del location of the batch route.
     *
     * @return returns false if not valid
     * @throws SQLException
     */
    private boolean isValidManfold(String brId, String recDelId, String manifoldId) throws SQLException
    {
        boolean valid = false;

        DBConn    dbconn = ConnectionPool.getConnection();
        Statement stmt   = null;
        String    value  = "";
        try
        {
            stmt = dbconn.connection.createStatement();

            String    query = getQuery(QueryType.VALID_MANIFOLD, brId, recDelId, manifoldId);
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

        //if equal to manifoldId, then valid.
        valid = value.equals(manifoldId) ? true : false;

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

        //the only other lookup table that requires a where clause is MANIFOLD_CONF
        //code below is specifically for MANIFOLD_CONF.


        //for manifold's tab only the MANIFOLD_ID column requires a where clause.
        if (lookUpTable.equals("MANIFOLD_CONF"))
        {
            //where clause for the model factory.
            //if this is in readonly/InactiveView
            //then get all records from the database
            //else if viewing in activeView/not readonly
            //then get only records that are active.
            whereClause = ComponentFactory.getDBIdNameFactoryWhereClause(activeView);

            //if the look up table is MANIFOLD_CONF and valid main table row
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

            if (columnName.equals("MANIFOLD_ID"))
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

            } else if (columnName.equals("DEFAULT_REC_MANIFOLD_ID"))
            {
                //filter the combobox by the receipt location id
                locIDs = recLocId == -1 ? "" : "" + recLocId;
            } else if (columnName.equals("DEFAULT_DEL_MANIFOLD_ID"))
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
        if (e.getSource() instanceof VisiComboBox)
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
            case VALID_MANIFOLD:

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
                query += "POLARIS.manifold_conf	M \n";
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
        VALID_MANIFOLD
    }

    public class BatchRoutesManifoldsTableModel extends GenericTableModel
    {


        public BatchRoutesManifoldsTableModel(ColumnRecordSet crs, String[] names)
        {
            super(crs, names, false, PolarisUI.getMessage("CF_MANIFOLDS_PANEL", PolarisUI.getMessage("BATCH_ROUTE")));


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


