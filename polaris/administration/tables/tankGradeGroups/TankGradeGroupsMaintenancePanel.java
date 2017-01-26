package polaris.administration.tables.tankGradeGroups;

import polaris.administration.tables.*;
//import polaris.administration.tables.batchRoutes.BatchRoutesMaintenancePanel;
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
 * Created 1/17/2017
 * Updated 1/20/2017 by TCI-KRiggs
 *
 * @Author TCI - Krista Riggs
 */

/**
 * Creates the Maintenance Panel for a configuration screen.
 * This class implements ActionListener and TextListener
 */
public class TankGradeGroupsMaintenancePanel extends GenericTabPanel implements ActionListener, TextListener
{

    //Instantiate variables
    private ValidatedTextField description;
    private ValidatedTextField name;
    private ValidatedTextField alias;
    private VisiTextField      updateUser;
    private VisiTextField      userUpdateDate;

    /**
     * Instantiate the Maintenance Panel
     *
     * @param mainPanel
     * @throws Exception
     */

    //main method
    public TankGradeGroupsMaintenancePanel(MainPanel mainPanel) throws Exception
    {
        super(mainPanel);
    }

    /**
     * The following non-implemented methods are declared
     * as they need to be overridden from the GenericTabPanel
     */
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
        //-- creates validatedtextfields
        /**use a validated text field instead of visitextfield, because a
        * validateTextField turns green when modified and the visitextfield does not
        * It also triggers the textvaluechanged only when it loses focus and value has changed.
         *
         */
        description = ComponentFactory.getDescriptionTextField();
        alias = ComponentFactory.getAliasTextField();
        name = ComponentFactory.getNameTextField();

        //these text fields are not editable therefore will not trigger textvaluechanged.
        updateUser = ComponentFactory.getUserTextField(false, Configuration.userDBName);
        userUpdateDate = ComponentFactory.getUserUpdateDateTextField(false, Configuration.userUpdateDateDBName);

        //place the components on the panel
        addToThisPanelAutoLayout(1, "NAME", name);
        addToThisPanelAutoLayout(1, "ALIAS", alias);
        addToThisPanelAutoLayout(1, "DESCRIPTION", description);
        addToThisPanelAutoLayout(1, "UPDATE_USER", updateUser);
        addToThisPanelAutoLayout(1, "USER_UPDATE_DATE", userUpdateDate);

        //sets the tableModel class name for each data entry component.
        //since all the components in this panel are associated to one tableModel
        //call the ComponentFactory setTableModelClassName.  This method is used only if you
        //have one tableModel in a panel.
        ComponentFactory.setTableModelClassName(this, tableModel.getClass().getName());
    }

    /**
     * This method is triggered when the components in the maintenance panel
     * have an action being done on it
     *
     * This also sets the field to updated and the table row object to updated.
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e)
    {

        //if the component does not have focus return out of this method.
        //this occurs if the action being done on the component is from another method
        //not from a user doing the action
        Component component = (Component) e.getSource();
        if (!component.hasFocus())
        {
            return;
        }

        //get tablerow object from the current selected row.
        TableRow tableRow = resultRS.getTableRowAtCursor();
        //makes sure all required data is valid and valuechanged is not ignored
        //before it processes the action.
        if (resultRS.isEmpty() || tableRow == null || resultRS.ignoreValueChanged)
        {
            //no data in the main grid and tableRow is not valid
            return;
        }

        ui.setBusyCursor();

        //set the update flag to true on the tablerow.field
        //and also on the tablerow
        resultRS.setUpdated(e.getSource(), tableRow);
        //sets the dirty flag to panel to true.
        setDirty(true);

        ui.setDefaultCursor();


    }

    /**
     * triggered when the following occurs:
     * validatedtextfield has lost focus and field value has changed.
     * <p>
     * If this method is triggered, it sets the field to updated and the tablerow object
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
     * This method validates text entered into the text fields for the table in the tab,
     * since there is no table in the Maintenance Tab it just validates that the columns exist
     */
    @Override
    protected void setValidators()
    {
        //sets the validator for a column in the ColumnRecordSet.
        //since this tab does not have a table in the tab, do nothing in this method
        for (int i = 0; i < this.mainPanel.mainTableCrs.size(); i++)
        {
            Column column = this.mainPanel.mainTableCrs.elementAt(i);
            if (column.columnName.equals("NAME") || column.columnName.equals("DESCRIPTION") ||
                column.columnName.equals("ALIAS"))
            {
                column.setValidator(defaultValidator(column));
            }
        }
    }
}
