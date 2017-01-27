package polaris.administration.tables.movements;

import polaris.administration.tables.GenericTabbedPane;
import polaris.frame.PolarisUI;

import javax.swing.*;

/**
 * Tabbed Panel Class for MOVEMENT_CONF
 * Creation Date: 1/9/2017
 * @Author: TCI- Waleed Elsaid
 * Update Date: 1/19/2017 - Added the maintenance tab to the panel
 */

public class MovementsTabbedPane extends GenericTabbedPane
{
    private MovementsFilterPanel movementsFilterPanel;
    private MovementsMaintenancePanel movementsMaintenancePanel;
    public MovementsTabbedPane() throws Exception
    {
        super();

        mainPanel = MovementsPanel.mainPanel;
        mainTable = mainPanel.mainTable;

        PolarisUI ui = PolarisUI.getUIInstance();
        ImageIcon tabIcon = (ImageIcon) ui.getValue(ui.DEFAULT_TAB_ICON);

        //---BEGIN Module Specific Code
        // This section will be used to add the tabs to the Tabbed Pane.
        // tab#0 Filter
        movementsFilterPanel = new MovementsFilterPanel(mainPanel);
        addTab(PolarisUI.getMessage("CF_FILTER"), tabIcon, movementsFilterPanel,
                PolarisUI.getMessage("CF_FILTER_PANEL", PolarisUI.getMessage("MOVEMENT")));
        // END tab#0
        // tab#1 Maintenance
        movementsMaintenancePanel = new MovementsMaintenancePanel(mainPanel);
        addTab(PolarisUI.getMessage("CF_MAINTENANCE"), tabIcon, movementsMaintenancePanel,
                PolarisUI.getMessage("CF_MAINTENANCE_PANEL", PolarisUI.getMessage("MOVEMENT")));
        // END tab#1
        //---END Module Specific Code

        setTabPlacement(JTabbedPane.TOP);

        // Setup listeners/actions
        addChangeListener(this);
    }


}
