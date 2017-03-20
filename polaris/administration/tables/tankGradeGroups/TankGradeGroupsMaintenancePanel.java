package polaris.administration.tables.tankGradeGroups;

import polaris.administration.tables.*;
import polaris.util.ValidatedTextField;
import polaris.util.VisiTextField;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;


/**
 * Creates the Maintenance Panel for a configuration screen.
 * This class implements ActionListener and TextListener
 *
 * @Author TCI - Krista Riggs
 * Created 1/17/2017
 * Updated 1/20/2017 by TCI-Krista Riggs - added comments
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
    public TankGradeGroupsMaintenancePanel(MainPanel mainPanel) throws Exception
    {
        super(mainPanel);
    }

    /**
     * The following non-implemented methods are declared
     * as they need to be overridden from the GenericTabPanel
     * <p>
     * no table to display on panel
     *
     * @return
     */
    @Override
    public String[] getColumnNames()
    {
        return new String[0];
    }

    /**
     * no table to display on panel
     *
     * @return
     */
    @Override
    public String[] getTableNames()
    {
        return new String[0];
    }

    /**
     * no table to display on panel
     *
     * @return
     */
    @Override
    public String[] getForeignKeyConstColNames()
    {
        return new String[0];
    }

    /**
     * no table to display on panel
     *
     * @return
     */
    @Override
    public String[][] getSelectColNames()
    {
        return new String[0][];
    }

    /**
     * no table to display on panel
     *
     * @return
     */
    @Override
    public String getOrderBy()
    {
        return null;
    }

    /**
     * creates validatedtextfields
     */
    @Override
    public void createPanel()
    {
        /**use a validated text field instead of visitextfield, because a
         * validateTextField turns green when modified and the visitextfield does not
         * It also triggers the textvaluechanged only when it loses focus and value has changed.
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
     * <p>
     * This also sets the field to updated and the table row object to updated.
     *
     * @param e - the action performed
     */
    public void actionPerformed(ActionEvent e)
    {

        /** if the component does not have focus return out of this method.
         * this occurs if the action being done on the component is from another method
         * not from a user doing the action
         */
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
     * @param e - the text value changed
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
     * This method validates text entered into the text fields for the table,
     * this checks the constraints set for the components (ex. NAME: unique)
     */
    @Override
    protected void setValidators()
    {
        //sets the validator for a column in the ColumnRecordSet.
        //since this tab does not have a table in the tab, do nothing in this method
        for (int i = 0; i < this.mainPanel.mainTableCrs.size(); i++)
        {
            Column column = this.mainPanel.mainTableCrs.elementAt(i);
            if (column.columnName.equals("NAME") || column.columnName.equals("DESCRIPTION"))
            {//No validator needed for Alias as it can be left null
                column.setValidator(defaultValidator(column));
            }
        }
    }
}
