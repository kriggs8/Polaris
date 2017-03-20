package polaris.administration.tables.movements;

import jdk.nashorn.internal.runtime.regexp.joni.Config;
import polaris.administration.tables.*;
import polaris.administration.tables.batchRoutes.BatchRoutesMaintenancePanel;
import polaris.constants.Formats;
import polaris.constants.RevenueAllocMethodDesc;
import polaris.constants.SpecialItems;
import polaris.db.ConnectionPool;
import polaris.db.DBConn;
import polaris.frame.PolarisSecurity;
import polaris.frame.PolarisUI;
import polaris.modelFactory.ConCarrierModelFactory;
import polaris.modelFactory.GradeSpecModelFactory;
import polaris.modelFactory.LocationModelFactory;
import polaris.ticket.shared.VisiMessage;
import polaris.util.*;
import polaris.util.tableCombobox.VisiTableComboBox;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

/**
 * Maintenance Panel Class for MOVEMENT_CONF. This panel will be displayed in the Tabbed Panel class.
 * Created by  TCI-Waleed Elsaid on  1/18/2017
 * Update Date: 1/26 updated the getDBNameFactoryWhereClause() to use the result set instead of the combobox selected values
 * Update Date: 1/27 updated the updateCombo() to use the comboBos selected items for the System Group and Line Space
 */

public class MovementsMaintenancePanel extends GenericTabPanel
        implements ActionListener, TextListener, ItemListener
{

    // all the combo boxes for the maintenance tab pan
    private VisiComboBox recLocationCombo;
    private VisiComboBox delLocationCombo;
    private VisiComboBox recConCarrierCombo;
    private VisiComboBox delConCarrierCombo;
    private VisiComboBox systemGroupCombo;
    private VisiComboBox lineSpaceCombo;
    private VisiComboBox gradeSpecCombo;
    private VisiComboBox reportVolSystemCombo;
    private VisiComboBox revAllocMethodCombo;
    private VisiComboBox bookInvCombo;

    // All editable text fields
    private ValidatedTextField  name;
    private ValidatedTextField  alias;
    private ValidatedTextField description;
    private ValidatedTextField genericName;

    // All the non-editable test fields
    private VisiTextField updateUser;
    private VisiTextField userUpdateDate;

    //All the check boxes
    private VisiCheckbox settledFlag;
    private VisiCheckbox allocCreditFlag;
    private VisiCheckbox crossOverFlag;

    // All the data models
    private DBIdNameFactory     locationDescModel;
    private DBIdNameFactory     conCarrierDescModel;
    private DBIdNameFactory     gradeSpecDescModel;
    private DBIdNameFactory     locationNameModel;
    private DBIdNameFactory     conCarrierNameModel;
    private DBIdNameFactory     gradeSpecNameModel;

    /**
     * Created by  TCI-Waleed Elsaid on  1/18/2017
     * Instantiates the Maintenance Panel.
     *
     * @param mainPanel
     * @throws Exception
     */
    public MovementsMaintenancePanel(MainPanel mainPanel) throws Exception {
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


    /**
     * Created by  TCI-Waleed Elsaid on  1/18/2017
     * This method is being called from the super class
     *
     */
    @Override
    public void createPanel()
    {
        //creates all the compononents for the panel.
        //-- creates comboboxes, these comboboxes do no have a default "All" record
        recLocationCombo = ComponentFactory.getComboBox(true,
                                                        Configuration.locationTableName,
                                                        "",
                                                        Configuration.recLocationDBName,
                                                        null);
        delLocationCombo = ComponentFactory.getComboBox(true,
                                                        Configuration.locationTableName,
                                                        "",
                                                        Configuration.delLocationDBName,
                                                        null);
        recConCarrierCombo = ComponentFactory.getComboBox(true,
                                                        Configuration.conCarrierTableName,
                                                        "",
                                                        Configuration.recConCarrierDBName,
                                                        null);
        delConCarrierCombo = ComponentFactory.getComboBox(true,
                                                        Configuration.conCarrierTableName,
                                                        "",
                                                        Configuration.delConCarrierDBName,
                                                        null);
        systemGroupCombo =  ComponentFactory.getComboBox(true,
                                                        Configuration.systemGroupTableName,
                                                        "",
                                                        Configuration.systemGroupDBName,
                                                        null);
        lineSpaceCombo = ComponentFactory.getComboBox(true,
                                                        Configuration.lineSpaceTableName,
                                                        "",
                                                        Configuration.linespaceDBName,
                                                        null);
        gradeSpecCombo = ComponentFactory.getComboBox(true,
                                                        Configuration.gradeSpecTableName,
                                                        "",
                                                        Configuration.gradeSpecDBName,
                                                        null);

        reportVolSystemCombo = ComponentFactory.getComboBox(true,
                                                        Configuration.systemTableName,
                                                        "",
                                                        Configuration.reportableVolDBName,
                                                        null);

        revAllocMethodCombo = ComponentFactory.getComboBox(true,
                                                        Configuration.revAllocMethodTableName,
                                                        "",
                                                        Configuration.revenueAllocMethodDBName,
                                                        null);

        bookInvCombo = ComponentFactory.getComboBox(true,
                                                    Configuration.bookInvLocationTableName,
                                                    "",
                                                    Configuration.bookInvLocationDBName,
                                                    null);

        //-- creates validatedtextfields
        //used a validated text field instead of visitextfield, because
        //validateTextField turns green when modified and
        //it triggers the textValueChanged only when it loses focus and value has changed.
        description = ComponentFactory.getDescriptionTextField();
        alias = ComponentFactory.getAliasTextField();
        name = ComponentFactory.getNameTextField();
        genericName =  ComponentFactory.getNameTextField(Configuration.genericNameDBName);

        //--creates textfields.
        //These text fields are not editable therefore will not trigger textValueChanged.
        updateUser = ComponentFactory.getUserTextField(false, Configuration.userDBName);
        userUpdateDate = ComponentFactory.getUserUpdateDateTextField(false, Configuration.userUpdateDateDBName);

        //-creates Checkboxes
        settledFlag = new VisiCheckbox(PolarisUI.getMessage("SETTLED"));
        settledFlag.setName(MovementsConstants.settledDBName);
        allocCreditFlag = new VisiCheckbox(PolarisUI.getMessage("ALLOC_CREDIT"));
        allocCreditFlag.setName(MovementsConstants.allocCreditDBName);
        crossOverFlag = new VisiCheckbox(PolarisUI.getMessage("CROSSOVER"));
        crossOverFlag.setName(MovementsConstants.crossoverDBName);

        // create model factories for location and for connecting carriers
        // used to populated name and description field.
        // the model factory, is a list of IDs and Names.
        locationNameModel = new LocationModelFactory("NAME");
        //the model factory, is a list of IDs and Descriptions.
        locationDescModel = new LocationModelFactory("DESCRIPTION");
        conCarrierNameModel = new ConCarrierModelFactory("NAME");
        conCarrierDescModel = new ConCarrierModelFactory("DESCRIPTION");
        gradeSpecNameModel = new GradeSpecModelFactory("NAME");
        gradeSpecDescModel = new GradeSpecModelFactory("DESCRIPTION");


        //add the components to the panel.
        // row 0
        this.addToThisPanel(0, 0, 1, "REC_LOCATION", recLocationCombo);
        this.addToThisPanel(2, 0, 1, "DEL_LOCATION", delLocationCombo);
        this.addToThisPanel(4, 0, 1, "REC_CON_CARRIER", recConCarrierCombo);
        this.addToThisPanel(6, 0, 1, "DEL_CON_CARRIER", delConCarrierCombo);

        // row 1
        this.addToThisPanel(0, 1, 1, "SYSTEM_GROUP", systemGroupCombo);
        this.addToThisPanel(2, 1, 1, "LINESPACE", lineSpaceCombo);
        this.addToThisPanel(4, 1, 1, "GRADE_SPEC", gradeSpecCombo);

        // row 2
        this.addToThisPanel(0, 2, 3, "NAME", name);
        this.addToThisPanel(4, 2, 3, "ALIAS", alias);

        // row 3

        this.addToThisPanel(0, 3, 3, "DESCRIPTION", description);

        // row 4
        JLabel revAccLabel = new JLabel(PolarisUI.getMessage("CF_REV_ACCOUNTING"));
        Font font = revAccLabel.getFont();
        Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
        revAccLabel.setFont(boldFont);
        this.addToThisPanel(0, 4, 2, revAccLabel);
        JLabel dailyNomLabel = new JLabel(PolarisUI.getMessage("CF_DAILY_NOMINATIONS"));
        dailyNomLabel.setFont(boldFont);
        this.addToThisPanel(4, 4, 2, dailyNomLabel);

        // row 5
        // First construct the two small panels and then embed them into the parnent panel

        // a border for the mini panel
        Border simpleBorder = BorderFactory.createLineBorder(Color.black);
        // Revenue Accounting mini panel
        VisiGridBagPanel revAccPanel = new VisiGridBagPanel();
        revAccPanel.setBorder(simpleBorder);
        revAccPanel.addToThisPanel(0, 0, 1, "REPORTABLE_VOLUME_SYSTEM", reportVolSystemCombo);
        revAccPanel.addToThisPanel(2, 0, 1, "REVENUE_ALLOC_METHOD", revAllocMethodCombo);
        revAccPanel.addToThisPanel(0, 1, 1, "BOOK_INVENTORY", bookInvCombo);
        revAccPanel.addToThisPanel(2, 1, 1, "", settledFlag);

        // Daily Nominations mini panel
        VisiGridBagPanel dailyNomPanel = new VisiGridBagPanel();
        dailyNomPanel.setBorder(simpleBorder);
        dailyNomPanel.addToThisPanel(0, 0, 3, "GENERIC_NAME", genericName);
        dailyNomPanel.addToThisPanel(0, 1, 1,  allocCreditFlag);
        dailyNomPanel.addToThisPanel(2, 1, 1,  crossOverFlag);

        // Now add them and voila
        this.addToThisPanel(0, 5, 4, revAccPanel);
        this.addToThisPanel(4, 5, 4, dailyNomPanel);

        // row 6
        this.addToThisPanel(2, 6, 1, "UPDATE_USER", updateUser);
        this.addToThisPanel(4, 6, 1, "USER_UPDATE_DATE", userUpdateDate);

        // The objective of these calls below for the main panel and the two sub-panels is to allow the controls in
        // those panels to automatically be populated based on the selected row in the main table

        ComponentFactory.setTableModelClassName(this,tableModel.getClass().getName());
        ComponentFactory.setTableModelClassName(revAccPanel,tableModel.getClass().getName());
        ComponentFactory.setTableModelClassName(dailyNomPanel,tableModel.getClass().getName());

    }


    /**
     * Created by  TCI-Waleed Elsaid on  1/18/2017
     * This method returns the selected ID from the lookuptable that the given combo box is pointing to
     * @param comboBox
     * @return the ID or -1 if the combobox is empty
     */
    private long getSelectedID(VisiComboBox comboBox){
        if (comboBox.getSelectedIndex() > -1)
        {
            // get the select item from combobox.
            Object o = comboBox.getSelectedItem();
            if (o instanceof NameId) {
                //make sure that the selected item is NameId instance
                NameId nameId = (NameId) o;
                return nameId.getId();
            }
        }
        return -1;
    }

    @Override
    /**
     * Created by  TCI-Waleed Elsaid on  1/18/2017
     * This method sets the validators to be called at the save action
     */
    protected void setValidators()
    {

        //sets the validator for a column in the ColumnRecordSet.
        for (int i = 0; i < this.mainPanel.mainTableCrs.size(); i++)
        {
            Column column = this.mainPanel.mainTableCrs.elementAt(i);

            if (    column.columnName.equals("REC_LOCATION_ID") ||
                    column.columnName.equals("REC_CON_CARRIER_ID") ||
                    column.columnName.equals("DEL_LOCATION_ID") ||
                    column.columnName.equals("DEL_CON_CARRIER_ID") ||
                    column.columnName.equals("SYSTEM_GROUP_ID") ||
                    column.columnName.equals("LINESPACE_ID") ||
                    column.columnName.equals("GRADE_SPEC_ID") ||
                    column.columnName.equals("NAME") ||
                    column.columnName.equals("DESCRIPTION") ||
                    column.columnName.equals("REPORTABLE_VOLUME_SYSTEM_ID") ||
                    column.columnName.equals("BOOK_INV_LOCATION_ID"))
            {
                column.setValidator(defaultValidator(column));
            }


            if (column.columnName.equals("REVENUE_ALLOC_METHOD_ID"))
            {
                column.setValidator(revAllocMethodValidator(column));
            }
        }
    }

    /**
     * Created by  TCI-Waleed Elsaid on  1/18/2017
     * This method returns a validator for the Rev Alloc Method combo box
     * to check for the user selection and gives warning message to populate the right tab according to the selection
     * or an error if the user does not select anything
     * @param column
     * @return
     */
    private ColumnValidator revAllocMethodValidator(Column column)
    {
        ColumnValidator validator = new ColumnValidator(column)
        {
            String errorMsg;

            public boolean validate(String value, String brId) throws SQLException
            {
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
                        errorMsg = PolarisUI.getMessage("CF_VALIDATION_NULL", "Rev Alloc Method");
                        return false;
                    }
                    if (value.equals(RevenueAllocMethodDesc.PERCENTAGE))
                    {
                        errorMsg = PolarisUI.getMessage("CF_MV_REV_ALLOC_METHOD","Rev. Allocations", "PERCENTAGE");
                    }
                    else
                    {
                        errorMsg = PolarisUI.getMessage("CF_MV_REV_ALLOC_METHOD","Tariffs", "MILES/BARREL_MILES");
                    }
                    this.setMode(ColumnValidator.MODAL_AND_FOCUS_AND_BYPASS);
                    return false;
                }
            }

            public String getErrorMessage(Object value)
            {
                return PolarisUI.getMessage(errorMsg);
            }

        };


        return validator;
    }

    /**
     * Created by  TCI-Waleed Elsaid on  1/18/2017
     * This method is triggered when components (comboboxes) in the maintenance panel has an action being done on it
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

        // get tablerow object from the current selected row.
        // this value was set in the textValueChanged() method
        TableRow tableRow = resultRS.getTableRowAtCursor();

        // makes sure all required data is valid and valueChanged is not ignored
        // before it process the action.
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
            //update all the dependent combo boxes
            //and update the Name and Description components
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
     * Created by  TCI-Waleed Elsaid on  1/18/2017
     * Method is called if a combo box value has been changed
     * Dependent combobox model is updated.
     *
     * @param component the combobox having the focus
     * @param action    the action name of the combobox
     */
    private void updateCombo(VisiComboBox component, String action)
    {
        String where = "";
        // first check for active views
        if (!resultRS.isReadOnly()) {
            where += " INACTIVE_INDICATOR_FLAG = 0 ";
        }
        // Now check on the action and update the corresponding combo
        if (action.equals(systemGroupCombo.getName()))
        {
            // read from the combo box not the result set as values are not stored in the result set yet
            where += " AND LINESPACE_ID = " + getSelectedID(lineSpaceCombo);
            where += " AND SYSTEM_GROUP_ID = " + getSelectedID(systemGroupCombo);
            ComponentFactory.updateCombo(bookInvCombo, where);
        }

        if (action.equals(lineSpaceCombo.getName()))
        {
            where += " AND LINESPACE_ID = " + getSelectedID(lineSpaceCombo);
            // update grade spec combo using the new line space
            ComponentFactory.updateCombo(gradeSpecCombo, where);

            // and add the system group for the book inv
            where += " AND SYSTEM_GROUP_ID = " + getSelectedID(systemGroupCombo);
            ComponentFactory.updateCombo(bookInvCombo, where);
        }

    }

    /**
     * Created by  TCI-Waleed Elsaid on  1/18/2017
     * Method is called only if the Rec. Location, Del. Location, Rec. Con. Carrier, Del. Con. Carrier, and Grade Spec.
     * comboboxes are changed.
     * This method, grabs the combo boxs selected items and
     * creates concatenated string of loc and cc values for the Name field and for the Description field
     * Since the Name and Description field is being updated, then the TableRow and Field objects have
     * to have the update flag set to true.
     *
     * @param component
     * @param action
     */
    private void updateNameDescription(VisiComboBox component, String action)
    {

        //only update the Name and Description fields if the location, con carrier, and grade spec combo boxes are updated.
        if (    action.equals(recLocationCombo.getName()) ||
                action.equals(delLocationCombo.getName()) ||
                action.equals(recConCarrierCombo.getName()) ||
                action.equals(delConCarrierCombo.getName()) ||
                action.equals(gradeSpecCombo.getName()))
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
     * Created by  TCI-Waleed Elsaid on  1/18/2017
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
        String    gradeSpecString       = "";
        IdNameBag locationIdNameBag   = new IdNameBag();
        IdNameBag conCarrierIdNameBag = new IdNameBag();
        IdNameBag gradeSpecIdNameBag = new IdNameBag();

        try
        {
            if (descValue)
            {
                //check if the model has been loaded or exist in cache.
                //if it does not load data from database.
                locationDescModel.loadData();

                //gets the model's idNameBag
                //Id is associated to the Location's Primary Index
                //Name is associated to the Locations's Description value.
                locationIdNameBag = (IdNameBag) locationDescModel.getProduct().getProduct();


                conCarrierDescModel.loadData();

                conCarrierIdNameBag = (IdNameBag) conCarrierDescModel.getProduct().getProduct();

                gradeSpecDescModel.loadData();

                gradeSpecIdNameBag = (IdNameBag) gradeSpecDescModel.getProduct().getProduct();
            } else
            {
                //check if the model has been loaded or exist in cache.
                //if it does not it will load data from database.
                locationNameModel.loadData();

                //gets the model's idNameBag
                //Id is associated to the Location's Primary Index
                //Name is associated to the Locations's Name value.
                locationIdNameBag = (IdNameBag) locationNameModel.getProduct().getProduct();


                conCarrierNameModel.loadData();

                conCarrierIdNameBag = (IdNameBag) conCarrierNameModel.getProduct().getProduct();

                gradeSpecNameModel.loadData();

                gradeSpecIdNameBag = (IdNameBag) gradeSpecNameModel.getProduct().getProduct();

            }


        }
        catch (Exception e)
        {
            //if it cannot load the data from database display an error message.
            //must make this consistent.
            System.out.println(e.getMessage());
        }


        //loop through the list of components.  this only process comboboxes.
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
                        else if (c.getName().equals(Configuration.gradeSpecDBName))
                        {
                            //if the componenet is a grade spec.
                            //finds the selected Id from the gradeSpecIDNameBag
                            //and gets the name value for the gradeSpec
                            NameId gradeSpec = gradeSpecIdNameBag.getNameId(nameId.getId());
                            gradeSpecString = gradeSpec.getName();

                        }
                    }
                }


            }

        }

        if (descValue)
        {
            //creates the description string value.
            value = recLocation + " / " + recConCarrier + " to " + delLocation + " / " + delConCarrier + " via " + gradeSpecString;
        } else
        {
            //creates the name string value.
            value = recLocation + "_" + recConCarrier + "_" + delLocation + "_" + delConCarrier + "_" + gradeSpecString;

        }


        return value;

    }

    /**
     * Created by  TCI-Waleed Elsaid on  1/18/2017
     * This method is triggered when the following occurs:
     * validatedtextfield has lost focus and field value has changed.
     * TextField, any value has changed.  It does not wait for lost focus.
     * if method is triggered, it sets the field to updated and the tableRow object
     * to updated.
     *
     * @param e the text event
     */
    public void textValueChanged(TextEvent e)
    {
        //get tableRow object at cursor.
        TableRow tableRow = resultRS.getTableRowAtCursor();

        //no data in the main grid
        if (resultRS.isEmpty() || tableRow == null)
        {
            return;
        }

        //set the update flag to true on the tableRow.field
        //and also on the tableRow
        resultRS.setUpdated(e.getSource(), tableRow);
        //sets the dirty flag to panel to true.
        setDirty(true);


    }

    /**
     * Created by  TCI-Waleed Elsaid on  1/18/2017
     * This method is triggered if the checkboxes are checked.
     * if method is triggered, it sets the field to updated and the tableRow object
     * to updated.
     *
     * @param e the event
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

    /**
     * Created by  TCI-Waleed Elsaid on  1/18/2017
     * This method returns the ID from the result set for a given column name in the given row
     * @param columnName the name of the column in the DB
     * @param mainTableRow the selected row in the main table
     * @return
     */
    private long getIDForRSColumn(String columnName, TableRow mainTableRow){
        long systemId = -1;

        Field field = resultRS.getField(columnName, mainTableRow);
        //get the long values from the fields.
        return Long.parseLong((field == null || field.getValue() == null) ? "-1" : field.getValue());
    }

    /**
     * Created by  TCI-Waleed Elsaid on  1/18/2017
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
        // check for empty lookups
        if ((lookUpTable == null)   ||
            (mainTableRow == null)  ||
            (lookUpTable.contains("_DESC")) ||
            (!activeView))
        {
            return whereClause;
        }

        // Now we have to make sure there is line space data selected to load the grade Spec combo box
        else if (lookUpTable.equals(Configuration.gradeSpecTableName))
            {
                //where clause for the model factory.
                //if this is in readonly/InactiveView
                //then get all records from the database
                //else if viewing in activeView/not readonly
                //then get only records that are active.
                whereClause = ComponentFactory.getDBIdNameFactoryWhereClause(activeView);
                //if the look up table is GRADE_SPEC_CONF and valid main table row
                //then create model factory.
                if (columnName.equals("GRADE_SPEC_ID") && activeView)
                {
                    whereClause += " AND LINESPACE_ID = " + getIDForRSColumn("LINESPACE_ID", mainTableRow);
                }
            }
        // and check there is a system group and a line space to load the book inv combo box
        else if (lookUpTable.equals(Configuration.bookInvLocationTableName))            {
            whereClause = ComponentFactory.getDBIdNameFactoryWhereClause(activeView);
            if (columnName.equals("BOOK_INV_LOCATION_ID") && activeView)
            {
                whereClause += " AND SYSTEM_GROUP_ID = " + getIDForRSColumn("SYSTEM_GROUP_ID", mainTableRow);
                whereClause += " AND LINESPACE_ID = " + getIDForRSColumn("LINESPACE_ID", mainTableRow);
            }
            }



        return whereClause;
    }



}
