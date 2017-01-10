package polaris.administration.tables.movements;

import polaris.administration.tables.GenericTable;
import polaris.administration.tables.MainPanel;

/**
 * Main Panel Class for MOVEMENT_CONF
 * @Author: Waleed Elsaid on 1/9/2017.
 */
public class MovementsPanel extends MainPanel
{
    public final static String[] tableNames = {"MOVEMENT_CONF"};
    public static GenericTable mainTable;
    public static MovementsPanel mainPanel;

    public MovementsPanel()
    {
        super(tableNames, "polaris.administration.tables.movements.MovementsTableColumnModel",
                "polaris.administration.tables.movements.MovementsTableModel",
                "polaris.administration.tables.movements.MovementsTabbedPane");

        mainTable = getTable();
        mainPanel = this;
    }



    public void setValidators()
    {
        // The validator for the MainTable is set in the Maintenance Panel
    }
    protected void refineActionList()
    {
        // Moving records up and down in the main grid is not applicable to Movements configuration.
        actionContainers.setAction(moveDownAction, false);
        actionContainers.setAction(moveUpAction, false);
        actionContainers.setAction(cutAction, false);
    }

}
