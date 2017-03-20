package polaris.administration.tables.companies;

import polaris.administration.tables.GenericTable;
import polaris.administration.tables.MainPanel;

/**
 *  Main Panel Class for COMPANY_CONF. It defines the
 * Configuration's database table. It also defines the Companies'
 * TableColumnModel, TableModel and Tabbed Panel. The the names of these classes
 * will be passed on to the MainTable class which creates the main panel
 * (JPanel) of the main table (JTable) and Tabbed Panel to display
 *
 * @Author TCI-Krista Riggs
 * Created 3/6/2017.
 */
public class CompaniesPanel extends MainPanel
{
    //initialise variables
    public final static String[] tableNames = {"COMPANY_CONF"};
    public static GenericTable   mainTable;
    public static CompaniesPanel mainPanel;

    /**
     * Instantiate the Companies Panel
     */
    public CompaniesPanel()
    {
        //Base classes used to display Companies configuration
        super(tableNames, "polaris.administration.tables.companies.CompaniesTableColumnModel",
              "polaris.administration.tables.companies.CompaniesTableModel",
              "polaris.administration.tables.companies.CompaniesTabbedPane");

        mainTable = getTable();
        mainPanel = this;
    }

    /**
     * Uses the Generic table to set up the Main table
     *
     * @return - the Main Table
     */
    public static GenericTable getMainTable()
    {
        return mainTable;
    }

    /**
     * Method allows customization of the actions that are enabled or available
     * to the user based on the specific requirements of this interface. The
     * superclass exposes all actions by default. In the case of
     * CompaniesPanel, the method removes the Move Up and Move Down and Cut
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
