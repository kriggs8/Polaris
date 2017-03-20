package polaris.administration.tables.contacts;

import polaris.administration.tables.GenericTable;
import polaris.administration.tables.MainPanel;

/**
 * Main Panel Class for CONTACT_CONF. it defines the
 * Configuration's database table. It also defines the Contacts'
 * TableColumnModel, TableModel and Tabbed Panel. The the names of these class
 * will be passed on to the MainTable class which creates the main panel
 * (JPanel) of the main table (Jtable) and tabbed Panel. to display
 * <p>
 *
 * @Author: TCI - Krista Riggs
 * Created 2/2/17
 * Updated
 */
public class ContactsPanel extends MainPanel
{
    //variable names
    public final static String[] tableNames = {"CONTACT_CONF"};
    public static GenericTable  mainTable;
    public static ContactsPanel mainPanel;

    /**
     * Instantiate the Contacts Panel
     */
    public ContactsPanel()
    {
        //base classes used to display Contacts configuration
        super(tableNames, "polaris.administration.tables.contacts.ContactsTableColumnModel",
              "polaris.administration.tables.contacts.ContactsTableModel",
              "polaris.administration.tables.contacts.ContactsTabbedPane");

        mainTable = getTable();
        mainPanel = this;

    }

    /**
     * Create the Main Table
     *
     * @return - the Main Table for Contacts
     */
    public static GenericTable getMainTable()
    {
        return mainTable;
    }

    /**
     * Method allows customization of the actions that are enabled or available
     * to the user based on the specific requirements of this interface. The
     * superclass exposes all actions by default. In the case of
     * ContactPanel, the method removes the Move Up and Move Down and Cut
     * actions as they are not applicable.
     */
    @Override
    protected void refineActionList()
    {
        actionContainers.setAction(moveDownAction, false);
        actionContainers.setAction(moveUpAction, false);
        actionContainers.setAction(cutAction, false);
    }

    /**
     * The validator for the MainTable is set in the Maintenance Panel
     */
    @Override
    public void setValidators()
    {
    }

    /**
     * Set the Main table
     *
     * @param table - the Main Table
     */
    @Override
    public void setTable(GenericTable table)
    {
        this.mainTable = table;
    }
}

