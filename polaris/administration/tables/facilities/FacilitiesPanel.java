package polaris.administration.tables.facilities;

import polaris.administration.tables.GenericTable;
import polaris.administration.tables.MainPanel;

/**
 * Main Panel Class for FACILITY_CONF. it defines the
 * Configuration's database table. It also defines the Facilities'
 * TableColumnModel, TableModel and Tabbed Panel. The the names of these class
 * will be passed on to the MainTable class which creates the main panel
 * (JPanel) of the main table (Jtable) and tabbed Panel. to display
 * <p>
 * @Author TCI Krista Riggs
 * Created 3/15/2017.
 */
public class FacilitiesPanel extends MainPanel
{
    //variable names
    public final static String[] tableNames = {"FACILITY_CONF"};
    public static GenericTable  mainTable;
    public static FacilitiesPanel mainPanel;

    /**
     * Instantiate the Facilities Panel
     */
    public FacilitiesPanel()
    {
        //base classes used to display Facilities configuration
        super(tableNames, "polaris.administration.tables.facilities.FacilitiesTableColumnModel",
              "polaris.administration.tables.facilities.FacilitiesTableModel",
              "polaris.administration.tables.facilities.FacilitiesTabbedPane");

        mainTable = getTable();
        mainPanel = this;
    }
    /**
     * Create the Main Table
     * @return - the Main Table for Facilities
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
