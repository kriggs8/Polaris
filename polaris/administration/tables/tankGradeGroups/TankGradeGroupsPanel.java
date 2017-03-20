package polaris.administration.tables.tankGradeGroups;

import polaris.administration.tables.GenericTable;
import polaris.administration.tables.MainPanel;

/**
 * Main Panel Class for TANK_GRADE_GROUP_CONF. it defines the
 * Configuration's database table It also defines the Tank Grade Groups'
 * TableColumnModel, TableModel and Tabbed Panel. The the names of these class
 * will be passed on to the MainTable class which creates the main panel
 * (JPanel) of the main table (Jtable) and tabbed Panel. to display
 *
 * @Author: TCI - Krista Riggs
 * Created 1/9/17
 * Updated 2/6/17 TCI-Krista Riggs - added comments
 */
public class TankGradeGroupsPanel extends MainPanel
{
    //variable names
    public final static String[] tableNames = {"TANK_GRADE_GROUP_CONF"};
    public static GenericTable         mainTable;
    public static TankGradeGroupsPanel mainPanel;

    /**
     * Instantiate the Tank Grade Groups Panel
     */
    public TankGradeGroupsPanel()
    {
        super(tableNames, "polaris.administration.tables.tankGradeGroups.TankGradeGroupsTableColumnModel",
              "polaris.administration.tables.tankGradeGroups.TankGradeGroupsTableModel",
              "polaris.administration.tables.tankGradeGroups.TankGradeGroupsTabbedPane");

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
     * TankGradeGroupsPanel, the method removes the Move Up and Move Down and Cut
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
     * Validators for the Main Table are set in the Maintenance Panel
     */
    @Override
    public void setValidators()
    {
    }

    /**
     * Set the table for the Main Panel
     *
     * @param table - the Main Table
     */
    @Override
    public void setTable(GenericTable table)
    {
        this.mainTable = table;
    }
}
