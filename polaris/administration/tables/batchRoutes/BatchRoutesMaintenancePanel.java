package polaris.administration.tables.batchRoutes;

import polaris.administration.tables.*;
import polaris.constants.Formats;
import polaris.constants.SpecialItems;
import polaris.db.ConnectionPool;
import polaris.db.DBConn;
import polaris.frame.PolarisSecurity;
import polaris.frame.PolarisUI;
import polaris.modelFactory.ConCarrierModelFactory;
import polaris.modelFactory.LocationModelFactory;
import polaris.ticket.shared.VisiMessage;
import polaris.util.*;
import polaris.util.tableCombobox.VisiTableComboBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

/**
 * Creates the Maintenance Panel for a configuration screen.
 */
public class BatchRoutesMaintenancePanel extends GenericTabPanel
        implements ActionListener, TextListener, ItemListener
{


    private VisiComboBox        systemCombo;
    private VisiComboBox        scheduleSystemCombo;
    private VisiComboBox        recLocationCombo;
    private VisiComboBox        delLocationCombo;
    private VisiComboBox        recConCarrierCombo;
    private VisiComboBox        delConCarrierCombo;
    private VisiComboBox        osAreaCombo;
    private ValidatedTextField  description;
    private ValidatedTextField  name;
    private ValidatedTextField  alias;
    private ValidatedTextField  recSchedNote;
    private ValidatedTextField  delSchedNote;
    private ValidatedTextField  physicalDesc;
    private VisiTextField       updateUser;
    private VisiTextField       userUpdateDate;
    private VisiCheckbox        batchGenFlag;
    private VisiCheckbox        localMovFlag;
    private VisiCheckbox        recCustodyFlag;
    private VisiCheckbox        delCustodyFlag;
    private VisiCheckbox        recTankGroupFlag;
    private VisiCheckbox        delTankGroupFlag;
    private VisiCheckbox        recAutoTankFlag;
    private VisiCheckbox        delAutoTankFlag;
    private VisiCheckbox        acctBatchFlag;
    private VisiCheckbox        chkViscosityFlag;
    private VisiCheckbox        inactiveIndicatorFlag;
    private BigDecimalTextField AvgTransDay;
    private BigDecimalTextField DefaultBatchVolume;
    private DBIdNameFactory     locationDescModel;
    private DBIdNameFactory     conCarrierDescModel;
    private DBIdNameFactory     locationNameModel;
    private DBIdNameFactory     conCarrierNameModel;

    /**
     * Instantiates the Maintenance Panel.
     *
     * @param mainPanel
     * @throws Exception
     */
    public BatchRoutesMaintenancePanel(MainPanel mainPanel) throws Exception
    {
        super(mainPanel);


    }

    @Override
    public String[] getColumnNames()
    {
        //no table to display on panel
        return new String[0];
    }

    @Override
    public String[] getTableNames()
    {
        //no table to display on panel
        return new String[0];
    }

    @Override
    public String[] getForeignKeyConstColNames()
    {
        //no table to display on panel
        return new String[0];
    }

    @Override
    public String[][] getSelectColNames()
    {
        //no table to display on panel
        return new String[0][];
    }

    @Override
    public String getOrderBy()
    {
        //no table to display on panel
        return null;
    }

    @Override
    public void createPanel()
    {
        //creates all the compononents for the panel.
        //-- creates comboboxes, these comboboxes do no have a default "All" record
        recLocationCombo = ComponentFactory.getLocationComboBox("", Configuration.recLocationDBName, null);
        delLocationCombo = ComponentFactory.getLocationComboBox("", Configuration.delLocationDBName, null);
        recConCarrierCombo = ComponentFactory.getConCarrierComboBox("", Configuration.recConCarrierDBName, null);
        delConCarrierCombo = ComponentFactory.getConCarrierComboBox("", Configuration.delConCarrierDBName, null);
        scheduleSystemCombo = ComponentFactory.getScheduleSystemComboBox("", Configuration.scheduleSystemDBName, null);
        systemCombo = ComponentFactory.getSystemComboBox("", Configuration.systemDBName, null);
        osAreaCombo = ComponentFactory.getOSAreaComboBox("", Configuration.osAreaDBName, null);

        //-- creates validatedtextfields
        //used a validated text field instead of visitextfield, because
        //validateTextField turns green when modified and
        //it triggers the textvaluechanged only when it loses focus and value has changed.
        description = ComponentFactory.getDescriptionTextField();
        alias = ComponentFactory.getAliasTextField();
        name = ComponentFactory.getNameTextField();
        recSchedNote = ComponentFactory.getNoteTextField("(REC_SCHED_NOTE");
        delSchedNote = ComponentFactory.getNoteTextField("(DEL_SCHED_NOTE");
        physicalDesc = ComponentFactory.getPhysRouteDescTextField("(PHYSICAL_ROUTE_NAME");

        //-- creates BigDecimalTextField
        //triggers the textvaluechanged only when it loses focus and value has changed.
        int precision = 5;
        int scale     = 2;
        AvgTransDay = new BigDecimalTextField(precision);
        AvgTransDay.setName("AVERAGE_TRANSIT_DAYS");
        AvgTransDay.setScale(scale, BigDecimal.ROUND_HALF_UP);
        AvgTransDay.setUnscaledValueDigitLimit(precision - scale);
        AvgTransDay.setFormatter(new DecimalFormat(Formats.TWODP));
        AvgTransDay.addParser(new DecimalFormat(Formats.TWODP));


        precision = 9;
        scale = 0;
        DefaultBatchVolume = new BigDecimalTextField(precision);
        DefaultBatchVolume.setName("DEFAULT_BATCH_SIZE");
        DefaultBatchVolume.setScale(scale, BigDecimal.ROUND_HALF_UP);
        DefaultBatchVolume.setUnscaledValueDigitLimit(precision - scale);
        DefaultBatchVolume.setFormatter(new DecimalFormat(Formats.LONG));
        DefaultBatchVolume.addParser(new DecimalFormat(Formats.LONG));


        //--creates textfields.
        //these text fields are not editable therefore will not trigger textvaluechanged.
        //todo: double check it doesnt trigger the textvalue changed when it is populated on updatePanel()
        updateUser = ComponentFactory.getUserTextField(false, Configuration.userDBName);
        userUpdateDate = ComponentFactory.getUserUpdateDateTextField(false, Configuration.userUpdateDateDBName);

        //-creates Checkboxes
        batchGenFlag = new VisiCheckbox(PolarisUI.getMessage("CF_BATCH_GEN_ROUTE"));
        batchGenFlag.setName("GENERATE_BATCH_FLAG");
        localMovFlag = new VisiCheckbox(PolarisUI.getMessage("CF_LOCAL_MOVEMENT_ROUTE"));
        localMovFlag.setName("LOCAL_MOVEMENT_FLAG");
        recCustodyFlag = new VisiCheckbox(PolarisUI.getMessage("CF_RD_CUSTODY_FLAG", PolarisUI.getMessage("CF_REC")));
        recCustodyFlag.setName("REC_CUSTODY_FLAG");
        delCustodyFlag = new VisiCheckbox(PolarisUI.getMessage("CF_RD_CUSTODY_FLAG", PolarisUI.getMessage("CF_DEL")));
        delCustodyFlag.setName("DEL_CUSTODY_FLAG");
        recTankGroupFlag = new VisiCheckbox(
                PolarisUI.getMessage("CF_RD_TANK_GROUP_FLAG", PolarisUI.getMessage("CF_REC")));
        recTankGroupFlag.setName("REC_TANK_GROUP_FLAG");
        delTankGroupFlag = new VisiCheckbox(
                PolarisUI.getMessage("CF_RD_TANK_GROUP_FLAG", PolarisUI.getMessage("CF_DEL")));
        delTankGroupFlag.setName("DEL_TANK_GROUP_FLAG");
        recAutoTankFlag = new VisiCheckbox(
                PolarisUI.getMessage("CF_RD_AUTO_TANK_FLAG", PolarisUI.getMessage("CF_REC")));
        recAutoTankFlag.setName("REC_AUTO_TANK_FLAG");
        delAutoTankFlag = new VisiCheckbox(
                PolarisUI.getMessage("CF_RD_AUTO_TANK_FLAG", PolarisUI.getMessage("CF_DEL")));
        delAutoTankFlag.setName("DEL_AUTO_TANK_FLAG");
        inactiveIndicatorFlag = ComponentFactory.getInactiveIndicatorCheckBox();
        acctBatchFlag = new VisiCheckbox(PolarisUI.getMessage("CF_ACCT_BATCH_ONLY"));
        acctBatchFlag.setName("ACCOUNTING_BATCH_FLAG");
        chkViscosityFlag = new VisiCheckbox(PolarisUI.getMessage("CF_CHK_VISCOSITY_FLAG"));
        chkViscosityFlag.setName("CHK_VISCOSITY_FLAG");

        //-- create model factories for location and for connecting carriers
        //used to populated name and description field.
        //the model factory, is a list of IDs and Names.
        locationNameModel = new LocationModelFactory("NAME");
        //the model factory, is a list of IDs and Descriptions.
        locationDescModel = new LocationModelFactory("DESCRIPTION");

        conCarrierNameModel = new ConCarrierModelFactory("NAME");
        conCarrierDescModel = new ConCarrierModelFactory("DESCRIPTION");

        //add the components to the panel.
        this.addToThisPanel(0, 0, 1, "REC_LOCATION", recLocationCombo);
        this.addToThisPanel(2, 0, 1, "DEL_LOCATION", delLocationCombo);
        this.addToThisPanel(4, 0, 1, "REC_CON_CARRIER", recConCarrierCombo);
        this.addToThisPanel(6, 0, 1, "DEL_CON_CARRIER", delConCarrierCombo);
        //row1
        this.addToThisPanel(0, 1, 1, "SCHED_SYSTEM", scheduleSystemCombo);
        this.addToThisPanel(2, 1, 1, "SYSTEM", systemCombo);
        this.addToThisPanel(4, 1, 1, "OS_AREA", osAreaCombo);

        this.addToThisPanel(0, 2, 3, "NAME", name);
        this.addToThisPanel(4, 2, 3, "ALIAS", alias);

        this.addToThisPanel(0, 3, 3, "DESCRIPTION", description);

        this.addToThisPanel(0, 4, 3, PolarisUI.getMessage("CF_RD_SCHED_NOTE", PolarisUI.getMessage("CF_REC")),
                            recSchedNote);
        this.addToThisPanel(4, 4, 3, PolarisUI.getMessage("CF_RD_SCHED_NOTE", PolarisUI.getMessage("CF_DEL")),
                            delSchedNote);

        this.addToThisPanel(0, 5, 1, "CF_PHYS_DESC", physicalDesc);
        this.addToThisPanel(2, 5, 1, "CF_AVG_TRANS_DAYS", AvgTransDay);
        this.addToThisPanel(4, 5, 1, "CF_DEF_BATCH_VOLUME", DefaultBatchVolume);

        //row6
        this.addToThisPanel(1, 6, 1, recCustodyFlag);
        this.addToThisPanel(3, 6, 1, delCustodyFlag);

        //row 7
        this.addToThisPanel(1, 7, 1, recTankGroupFlag);
        this.addToThisPanel(3, 7, 1, delTankGroupFlag);

        //row 8
        this.addToThisPanel(1, 8, 1, recAutoTankFlag);
        this.addToThisPanel(3, 8, 1, delAutoTankFlag);

        //row 9
        this.addToThisPanel(1, 9, 1, batchGenFlag);
        this.addToThisPanel(3, 9, 1, localMovFlag);

        //row 10
        this.addToThisPanel(1, 10, 1, acctBatchFlag);
        this.addToThisPanel(3, 10, 1, chkViscosityFlag);

        //row 11
        this.addToThisPanel(1, 11, 1, inactiveIndicatorFlag);

        this.addToThisPanel(4, 6, 1, "UPDATE_USER", updateUser);
        this.addToThisPanel(4, 7, 1, "USER_UPDATE_DATE", userUpdateDate);

        // This call is only to test the VisiTableComboBox functionality
        //this.createTestForVisiTableComboBox();

        //sets the tableModel class name for each data entry component.
        //since all the components in this panel is associated to one tableModel
        //call the ComponentFactor stTableModelClassName.  This method is used only if you
        //only have one tableModel in a panel.
        ComponentFactory.setTableModelClassName(this,tableModel.getClass().getName());
    }

    // TODO: Remove this test method before release
    public void createTestForVisiTableComboBox()
    {
        try
        {
            // Used for testing a VisiTableComboBox control only.
            String query;
            query  = "SELECT ";
            query +=   " A.id ID, ";
            query +=   " C.name \"TS Name\", ";
            query +=   " D.name \"T Name\", ";
            query +=   " B.tariff_number \"Number\", ";
            query +=   " A.tariff_sequence \"Seq.\",";
            query +=   " A.tariff_rate \"Rate\", ";
            query +=   " TO_CHAR(A.effective_start_date, 'mm/dd/yyyy hh24:mi') \"Eff. Start Date\",";
            query +=   " TO_CHAR(A.effective_stop_date, 'mm/dd/yyyy hh24:mi') \"Eff. End Date\",";
            query +=   " B.minimum_tender_volume \"Min. Tend. Vol.\"";   //added by MJT -- July 2000
            query += "FROM ";
            query +=   PolarisUI.DB_OWNER + "tariff_sequence_conf A, ";
            query +=   PolarisUI.DB_OWNER + "tariff_conf B, ";
            query +=   PolarisUI.DB_OWNER + "governing_body_conf C, ";
            query +=   PolarisUI.DB_OWNER + "company_conf D, ";
            query +=   PolarisUI.DB_OWNER + "grade_spec_conf F ";
            query += "WHERE ";
            query +=   " A.inactive_indicator_flag = 0 and ";
            query +=   " A.tariff_id = B.id and ";
            query +=   " B.governing_body_id = C.id and ";
            query +=   " B.issuing_company_id = D.id and ";
            //query +=   " F.linespace_id = " + queryParameters[1] + " and ";
            //query +=    " F.id = " + queryParameters[2] + " and ";
            query +=   " B.grade_spec_id = F.id ";
            query += "ORDER BY ";
            query +=   " UPPER(B.tariff_number), ";
            query +=   " UPPER(C.name), ";
            query +=   " UPPER(D.name), ";
            query +=   " UPPER(A.tariff_sequence) ";

            VisiTableComboBox tempTest = new VisiTableComboBox(query, 2, "SYSTEM_ID"); // Tests Factory based logic
            //VisiTableComboBox tempTest = new VisiTableComboBox(query, 2, ""); // Tests Query Above
            this.addToThisPanel(4,8,1, "JTableCombobox", tempTest);
            tempTest.insertSpecialItem(SpecialItems.NONE, 0);
            tempTest.addSpecialItem(SpecialItems.ALL);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * set Help Topic Id
     */
    @Override
    public void setHelp()
    {
        this.mainPanel.setContextHelpTopicId("BatchRoutesConfigurationModule_MaintenanceTab");
    }

    @Override
    protected void setValidators()
    {
        //sets the validator for a column in the ColumnRecordSet.
        //since this tab does not have a table in the tab, do nothing in this method
        for (int i = 0; i < this.mainPanel.mainTableCrs.size(); i++)
        {
            Column column = this.mainPanel.mainTableCrs.elementAt(i);
            if (column.columnName.equals("NAME") ||
                column.columnName.equals("DESCRIPTION") ||
                column.columnName.equals("REC_LOCATION_ID") ||
                column.columnName.equals("DEL_LOCATION_ID") ||
                column.columnName.equals("REC_CON_CARRER_ID") ||
                column.columnName.equals("DEL_CON_CARRER_ID") ||
                column.columnName.equals("PHYSICAL_ROUTE_NAME") ||
                column.columnName.equals("SCHED_SYSTEM_ID") ||
                column.columnName.equals("SYSTEM_ID") ||
                column.columnName.equals("OS_AREA_ID"))
            {
                column.setValidator(defaultValidator(column));
            }

            if (column.columnName.equals("LOCAL_MOVEMENT_FLAG"))
            {
                column.setValidator(localMovementValidator(column));
            }
        }
    }

    private ColumnValidator localMovementValidator(Column column)
    {


        ColumnValidator validator = new ColumnValidator(column)
        {


            public boolean validate(String text, String brId) throws SQLException
            {
                //if the local movement flag is set to 1,
                //or this is a newly inserted record.
                //dont continue with validation.
                if(text.equals("1") || brId.equals(ResultRecordSet.newKey))
                    return true;

                //if this is a local movement route,
                //then get a list of batches that have local times.
                String prmBatchIds = getLocalMovementBatches(brId);
                if (prmBatchIds.isEmpty())
                {
                    //dont continue with validation if the batches for this route does not exist with
                    //local times.
                    return true;
                }

                //batches exists with local times.
                //ask the user if they wish to proceed with the update.
                //if user selects yes, the system will clear all local times
                //if user selects no, the system will undo the changes.
                Object[] options   = {"Yes", "No"};
                int      selection = 0;
                selection = VisiMessage.warningOverride(PolarisUI.getMessage("CF_VALIDATION_CLEAR_LOCAL_TIMES", prmBatchIds),
                                                        options);

                if (selection == 1)
                {
                    clearPrmBatchLocalTimes(brId);
                    return true;
                }
                else
                    return false;

            }


            public boolean validate(Object value, String brId) throws SQLException
            {
                if (value instanceof String)
                {
                    return validate((String) value, brId);
                } else
                {

                    if (value == null || value.equals(new Long(0)))
                    {
                        return false;
                    }
                }


                return true;
            }

            public String getErrorMessage(Object value)
            {
                return PolarisUI.getMessage("CF_VALIDATION_SAVE_ERROR", tableNames[0]);
            }

        };


        return validator;
    }

    /**
     * gets the polaris id
     *
     * @return
     * @throws SQLException
     */
    private String getLocalMovementBatches(String brId) throws SQLException
    {
        String batchList = "";

        DBConn    dbconn = ConnectionPool.getConnection();
        Statement stmt   = null;
        try
        {
            stmt = dbconn.connection.createStatement();

            String    query = getQuery(QueryType.LOCAL_PRM_BATCHES, brId,"");
            ResultSet rs    = stmt.executeQuery(query);

            int count = 0;
            while (rs.next())
            {
                count++;
                String prmBatchId = rs.getString(1);
                String batchCode  = rs.getString(2);
                batchList += batchCode + "\r\n";
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
        return batchList;
    }

    private void clearPrmBatchLocalTimes(String brId) throws SQLException
    {


        DBConn    dbconn = ConnectionPool.getConnection();
        Statement stmt   = null;
        try
        {
            stmt = dbconn.connection.createStatement();

            String query = getQuery(QueryType.CLEAR_PRM_BATCH_LOCAL_TIMES, brId);
            stmt.executeUpdate(query);

        }
        catch (SQLException ex)
        {
            throw ex; // doesn't this mean the dude won't be released?
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
     * method is triggered when components (comboboxes) in the maintenance panel has an action being done on it
     * Actions on location comboboxes and con carrier comboboxes, will update the name and description fields
     * Actions on the System combobox will update the OS Area and vice versa.
     * This also sets the field to updated and the tablerow object
     * to updated.
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
        //makes sure all required data is valid and valuechanged is not ignored
        //before it process the action.
        if (resultRS.isEmpty() || tableRow == null || resultRS.ignoreValueChanged)
        {
            //no data in the main grid and tableRow is not valid
            return;
        }

        ui.setBusyCursor();

        String action = e.getActionCommand();
        if (e.getSource() instanceof VisiComboBox)
        {
            //if actions are on a visiCombobox then
            //update the dependent compboboxes (System or OS Area)
            //and update the Name and Description component
            updateCombo((VisiComboBox) component, action);
            updateNameDescription((VisiComboBox) component, action);
        }

        //set the update flag to true on the tablerow.field
        //and also on the tablerow
        resultRS.setUpdated(e.getSource(), tableRow);
        //sets the dirty flag to panel to true.
        setDirty(true);

        ui.setDefaultCursor();


    }

    /**
     * Method is called if the System is changed
     * then the dependent combobox model is updated.
     * If system combobox is changed, os area model factor is filtered by the selected System
     *
     * @param component the combobox having the focus
     * @param action    the action name of the combobox
     */
    private void updateCombo(VisiComboBox component, String action)
    {


        String where = "";
        if (component.getName().equals(Configuration.systemDBName))
        {
            //if the system is updated
            //get a where string to be used for the OS Area model factory
            where = getWhereStr((VisiComboBox) component, Configuration.systemDBName);
        }

        //if viewing in activeView/not readonly
        //then get only records that are active.
        if (!resultRS.isReadOnly())
        {
            where += where.isEmpty() ? " INACTIVE_INDICATOR_FLAG = 0 " : " AND INACTIVE_INDICATOR_FLAG = 0 ";
        }


        if (action.equals(systemCombo.getName()) && systemCombo.hasFocus())
        {
            //the focus is on systemCombo and the action is on systemCombo.
            //update OS Area combobox.
            ComponentFactory.updateCombo(osAreaCombo, where);

        }


    }

    /**
     * Method is called only if the location and con carrier comboboxes are changed.
     * This method, grabs the location and connecting carrier selected items and
     * creates concatenated string of loc and cc values for the Name field and for the Description field
     * Since the Name and Description field is being updated, then the TableRow and Field objects have
     * to have the update flag set to true.
     *
     * @param component
     * @param action
     */
    private void updateNameDescription(VisiComboBox component, String action)
    {

        //only update the Name and Description fields if the location and con carrier comboboxes are updated.
        if (component.getName().equals(Configuration.recLocationDBName) ||
            component.getName().equals(Configuration.delLocationDBName) ||
            component.getName().equals(Configuration.recConCarrierDBName) ||
            component.getName().equals(Configuration.delConCarrierDBName))
        {
            //get the tableRow object for the current row.
            //no need to check if the tableRow is valid
            //before this method can be called in ActionPerformed.
            //the tableRow has to be valid.
            TableRow tableRow = resultRS.getTableRowAtCursor();

            //gets the concatenated loc and cc description/name string and
            //sets the Description/Name fields text value.
            description.setText(getNameDescriptionValue(this, true));
            name.setText(getNameDescriptionValue(this, false));

            //set the ResultSet.Field (description/name field) to true
            //this flags tells the insert or update methods that component field value has been update/modified
            //this flag has to be set true here, since these fields are being updated
            //as a result to user making changes to a location and conn carrier comboboxes.
            resultRS.setUpdated(description, tableRow);
            resultRS.setUpdated(name, tableRow);
        }


    }

    /**
     * Method creates a where string for the combobox value being selected.
     *
     * @param c
     * @param fieldName
     * @return
     */
    private String getWhereStr(VisiComboBox c, String fieldName)
    {
        String where = "";

        //if component value is null or empty string
        //then return out of this method
        //since the component is configured to be used to build a where string.
        if (c.getName() == null || c.getName().equals(""))
        {
        } else if (c.getSelectedIndex() > -1)
        {
            //if this is combobox and has a name
            //then get the select item from combobox.
            Object o = c.getSelectedItem();
            if (o instanceof NameId)
            {
                //make sure that the selected item is NameId instance
                NameId nameId = (NameId) o;

                //creates the where clause.
                where = fieldName + " = " + nameId.getId() + ")";

            }
        }

        return where;

    }

    /**
     * Method creates a string of concatenated rec location, del location, rec con carrier and del con carrier
     * description values.
     * Loops through Panel's components and only process comboboxes for
     * rec location, del location, rec con carrier and del con carrier
     * <p>
     * Gets the selected ID for each combobox and gets the related Name value from IdNameBag.
     * In this method the Name value being returned is the description field from the database.
     *
     * @param panel
     * @param descValue true - gets the description values, else false for the name values
     * @return
     */
    private String getNameDescriptionValue(JPanel panel, boolean descValue)
    {
        Component[] allCom;
        allCom = panel.getComponents();

        String    value               = "";
        String    recLocation         = "";
        String    delLocation         = "";
        String    recConCarrier       = "";
        String    delConCarrier       = "";
        IdNameBag locationIdNameBag   = new IdNameBag();
        IdNameBag conCarrierIdNameBag = new IdNameBag();


        try
        {

            if (descValue)
            {
                //check if the model has been loaded or exist in cache.
                //if it does not it will load data from database.
                if (locationDescModel.getProduct() == null)
                {
                    locationDescModel.loadData();
                }
                //gets the model's idNameBag
                //Id is associated to the Location's Primary Index
                //Name is associated to the Locations's Description value.
                locationIdNameBag = (IdNameBag) locationDescModel.getProduct().getProduct();


                if (conCarrierDescModel.getProduct() == null)
                {
                    conCarrierDescModel.loadData();
                }
                conCarrierIdNameBag = (IdNameBag) conCarrierDescModel.getProduct().getProduct();
            } else
            {
                //check if the model has been loaded or exist in cache.
                //if it does not it will load data from database.
                if (locationNameModel.getProduct() == null)
                {
                    locationNameModel.loadData();
                }
                //gets the model's idNameBag
                //Id is associated to the Location's Primary Index
                //Name is associated to the Locations's Name value.
                locationIdNameBag = (IdNameBag) locationNameModel.getProduct().getProduct();


                if (conCarrierNameModel.getProduct() == null)
                {
                    conCarrierNameModel.loadData();
                }
                conCarrierIdNameBag = (IdNameBag) conCarrierNameModel.getProduct().getProduct();

            }


        }
        catch (Exception e)
        {
            //if it cannot load the data from database display an error message.
            //todo:check if the is being displayed in the logfile only and not a dialog error message
            //must make this consistent.
            System.out.println(e.getMessage());
        }


        //loops thru the list of components.  this only process comboboxes.
        for (int i = 0; i < allCom.length; i++)
        {
            Component a = allCom[i];
            if (a instanceof VisiComboBox)
            {
                //if this is a combobox  and has the name set.
                //if name is not set it returns an empty string.
                VisiComboBox c = (VisiComboBox) allCom[i];
                if (c.getName() == null || c.getName().equals(""))
                {
                } else if (c.getSelectedIndex() > -1)
                {
                    //gets the selected Item and it must be of NameId instance
                    Object o = c.getSelectedItem();
                    if (o instanceof NameId)
                    {
                        NameId nameId = (NameId) o;


                        if (c.getName().equals(Configuration.recLocationDBName))
                        {
                            //if the componenet is a Rec Location.
                            //finds the selected Id from the locationIDNameBag
                            //and gets the name for the Rec Location.
                            NameId location = locationIdNameBag.getNameId(nameId.getId());
                            recLocation = location.getName();

                        } else if (c.getName().equals(Configuration.delLocationDBName))
                        {
                            //if the componenet is a Del Location.
                            //finds the selected Id from the locationIDNameBag
                            //and gets the name value for the Del Location
                            NameId location = locationIdNameBag.getNameId(nameId.getId());
                            delLocation = location.getName();

                        } else if (c.getName().equals(Configuration.recConCarrierDBName))
                        {
                            //if the componenet is a Rec Con Carrier.
                            //finds the selected Id from the conCarrierIDNameBag
                            //and gets the name value for the Rec Con Carrier
                            NameId conCarrier = conCarrierIdNameBag.getNameId(nameId.getId());
                            recConCarrier = conCarrier.getName();

                        } else if (c.getName().equals(Configuration.delConCarrierDBName))
                        {
                            //if the componenet is a Del Con Carrier.
                            //finds the selected Id from the conCarrierIDNameBag
                            //and gets the name value for the Del Con Carrier
                            NameId conCarrier = conCarrierIdNameBag.getNameId(nameId.getId());
                            delConCarrier = conCarrier.getName();

                        }
                    }
                }


            }

        }

        if (descValue)
        {
            //creates the description string value.
            value = recLocation + " / " + recConCarrier + " to " + delLocation + " / " + delConCarrier;
        } else
        {
            //creates the name string value.
            value = recLocation + "_" + recConCarrier + "_" + delLocation + "_" + delConCarrier;

        }


        return value;

    }

    /**
     * Gets the where clause for the DBNameFactory.  Used to populate the combo boxes model factory.
     *
     * @param columnName   -  column name of the table field.
     * @param lookUpTable  - table name
     * @param mainTableRow - the selected main table's row.
     * @return
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



        //the only other lookup table that requires a where clause is OS_AREA_CONF
        //code below is specifically for OS_AREA_CONF.
        if (lookUpTable.equals("OS_AREA_CONF"))
        {
            //where clause for the model factory.
            //if this is in readonly/InactiveView
            //then get all records from the database
            //else if viewing in activeView/not readonly
            //then get only records that are active.
            whereClause = ComponentFactory.getDBIdNameFactoryWhereClause(activeView);

            //if the look up table is OS_AREA_CONF and valid main table row
            //then create model factory.
            long systemId = -1;

            Field systemField = resultRS.getField("SYSTEM_ID", mainTableRow);


            //get the long values from the fields.
            systemId = Long.parseLong(
                    systemField == null || systemField.getValue() == null ? "-1" : systemField.getValue());

            //only filter the records in the active view
            //else display all records in the drop down.
            if (columnName.equals("OS_AREA_ID") && activeView)
            {
                whereClause += " AND SYSTEM_ID = " + systemId;
            }
        }

        return whereClause;

    }

    /**
     * triggered when the following occurs:
     * validatedtextfield has lost focus and field value has changed.
     * BigDecimalTextField has lost focus and value has changed.
     * TextField, any value has changed.  It doesnt wait for lost focus.
     * if method is triggered, it sets the field to updated and the tablerow object
     * to updated.
     *
     * @param e
     */
    public void textValueChanged(TextEvent e)
    {
        //get tablerow object at cursor.
        TableRow tableRow = resultRS.getTableRowAtCursor();

        //no data in the main grid
        if (resultRS.isEmpty() || tableRow == null)
        {
            return;
        }

        //set the update flag to true on the tablerow.field
        //and also on the tablerow
        resultRS.setUpdated(e.getSource(), tableRow);
        //sets the dirty flag to panel to true.
        setDirty(true);


    }

    /**
     * method is triggered if the checkboxes are checked.
     * if method is triggered, it sets the field to updated and the tablerow object
     * to updated.
     *
     * @param e
     */
    public void itemStateChanged(ItemEvent e)
    {
        //get tablerow object at cursor.
        TableRow tableRow = resultRS.getTableRowAtCursor();
        //no data in the main grid
        if (resultRS.isEmpty() || tableRow == null)
        {
            return;
        }


        //set the update flag to true on the tablerow.field
        //and also on the tablerow
        resultRS.setUpdated(e.getSource(), tableRow);
        //sets the dirty flag to panel to true.
        setDirty(true);


    }

    private String getQuery(QueryType queryType, String... queryParameters)
    {
        String query = "";

        switch (queryType)
        {
            case LOCAL_PRM_BATCHES:
                query = "SELECT \n";
                if (queryParameters[1]!=null && !queryParameters[1].isEmpty())
                {
                    query += queryParameters[1] + " \n";
                }
                else
                {
                    query += " id, \n";
                    query += " batch_code \n";
                }
                query += "FROM \n";
                query += "POLARIS.prm_batch_tran \n";
                query += "WHERE  \n";
                query += " batch_route_id = " + queryParameters[0] + "\n";
                query += " and historical_flag =  0 \n";
                query += " and pending_flag =  0 \n";
                query += " and (local_start_date is not null or local_stop_date is not null or \n";
                query += " local_rate is not null or local_cond_instr_id is not null) \n";
                break;
            case CLEAR_PRM_BATCH_LOCAL_TIMES:

                query = "UPDATE  \n";
                query += "POLARIS.prm_batch_tran  \n";
                query += "SET  \n";
                query += "		local_rate					= NULL, \n";
                query += "		local_cond_instr_id			= NULL, \n";
                query += "		auto_calc_local_batch_flag	= 0, \n";
                query += "		local_start_date			= NULL, \n";
                query += "		local_stop_date				= NULL, \n";
                query += "		user_id	= " + PolarisSecurity.userData.getId() + ", \n";
                query += "		user_update_date = sysdate  \n";
                query += "WHERE  \n";
                query += "		id	in (  \n";
                query += getQuery(QueryType.LOCAL_PRM_BATCHES, queryParameters[0], "ID");
                query += ")\n";

                break;

        }

        return query;

    }


    private enum QueryType
    {
        LOCAL_PRM_BATCHES,
        CLEAR_PRM_BATCH_LOCAL_TIMES
    }
}
