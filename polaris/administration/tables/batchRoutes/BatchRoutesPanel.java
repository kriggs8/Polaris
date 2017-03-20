package polaris.administration.tables.batchRoutes;

import polaris.administration.tables.*;
import polaris.constants.RecDelDesc;
import polaris.db.ConnectionPool;
import polaris.db.DBConn;
import polaris.frame.ContextHelpAction;
import polaris.frame.PolarisSecurity;
import polaris.frame.PolarisUI;
import polaris.ticket.shared.VisiMessage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Prototype Code - Main Panel Class for BATCH_ROUTES_CONF. it defines the Configuration's database table
 * It also defines the Batch Route's TableColumnModel, TableModel and Tabbed Panel.  The the names of these
 * class will be passed on to the MainTable class which creates the main panel (JPanel) of the main table (Jtable) and
 * tabbed Panel.
 * to display
 *
 * @Author: Jean Wurster
 */
public class BatchRoutesPanel extends MainPanel
{
    public final static String[] tableNames = {"BATCH_ROUTE_CONF"};
    public static GenericTable mainTable;
    public static BatchRoutesPanel mainPanel;

    public BatchRoutesPanel()
    {
        super(tableNames, "polaris.administration.tables.batchRoutes.BatchRoutesTableColumnModel",
              "polaris.administration.tables.batchRoutes.BatchRoutesTableModel",
              "polaris.administration.tables.batchRoutes.BatchRoutesTabbedPane");

        mainTable = getTable();
        mainPanel = this;

        // Set the JavaHelp root topic id for this class instance
        this.rootJavaHelpTopicId = "BatchRoutesConfigurationModule";
    }

    public static GenericTable getMainTable()
    {
        return mainTable;
    }

    /**
     * Method allows customization of the actions that are enabled or available to the user based on the specific
     * requirements of this interface.  The superclass exposes all actions by default.  In the case of BatchRoutesPanel,
     * the method removes the Move Up and Move Down actions as they are not applicable.
     *
     * @author John McDonough
     * @modified 05/24/2016 Initial Revision
     */
    protected void refineActionList()
    {
        // Moving records up and down in the main grid is not applicable to Batch Routes configuration.
        actionContainers.setAction(moveDownAction, false);
        actionContainers.setAction(moveUpAction, false);
    }

    public void setValidators()
    {
        // The validator for the MainTable is set in the Maintenance Panel
    }

    public void setTable(GenericTable table)
    {
        this.mainTable = table;
    }
}
