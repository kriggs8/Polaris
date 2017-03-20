package polaris.administration.tables.companies;

import polaris.administration.tables.*;

import java.awt.event.*;

/**
 * Creates the Maintenance Panel for the configuration screen.
 * This class implements ActionListener and TextListener
 *
 * @Author TCI - Krista Riggs
 * Created 3/7/2017.
 */
public class CompaniesMaintenancePanel extends GenericTabPanel implements ActionListener, TextListener
{
    private ContactPanel contactPanel;

    public CompaniesMaintenancePanel(MainPanel mainPanel) throws Exception
    {
        super(mainPanel);
    }

    /**
     * Create the panel for Maintenance Tab
     */
    @Override
    public void createPanel()
    {
        contactPanel = new ContactPanel(this, tableModel, true);
        contactPanel.displayNamePanel();
        contactPanel.displayAddressPanel();
        contactPanel.displayPhoneNumberPanel();
        contactPanel.displayMiscPanel();

        addToThisPanelAutoLayout(1, contactPanel);

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
     * @param e
     */
    public void actionPerformed(ActionEvent e)
    {
        //get tablerow object from the current selected row.
        TableRow tableRow = resultRS.getTableRowAtCursor();
        //makes sure all required data is valid and valuechanged is not ignored
        //before it processes the action.
        if (resultRS.isEmpty() || tableRow == null || resultRS.ignoreValueChanged)
        {
            //no data in the main grid and tableRow is not valid
            return;
        }
        //set the update flag to true on the tablerow.field
        //and also on the tablerow
        resultRS.setUpdated(e.getSource(), tableRow);
        //sets the dirty flag to panel to true.
        setDirty(true);
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
}
