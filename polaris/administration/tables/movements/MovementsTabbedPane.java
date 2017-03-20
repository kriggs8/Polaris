package polaris.administration.tables.movements;

import polaris.administration.tables.GenericTabbedPane;
import polaris.frame.PolarisUI;

import javax.swing.*;

/**
 * Tabbed Panel Class for MOVEMENT_CONF
 * Creation Date: 1/9/2017
 * @Author: TCI- Waleed Elsaid
 * Update Date: 1/19/2017 - Added the maintenance tab to the panel
 * Update Date: 1/30/2017 - Added the batch routes tab to the panel
 * Update Date: 02/06/2017 - Updated maintenance, and batch route panels to be scrollable
 * Update Date: 02/07/2017 - Added the Conn Carrier Detail Tab
 * Update Date: 02/20/2017 - Added the Inventories Detail Tab
 * Update Date: 03/02/2017 - Added the Rev Alloc Detail Tab
 */

public class MovementsTabbedPane extends GenericTabbedPane
{
    private MovementsFilterPanel movementsFilterPanel;
    private MovementsMaintenancePanel movementsMaintenancePanel;
    private MovementsBatchRoutesPanel movementsBatchRoutesPanel;
    private MovementsCCDetailsPanel movementsCCDetailsPanel;
    private MovementsTariffsPanel movementsTariffsPanel;
    private MovementsInventoriesPanel movementsInventoriesPanel;
    private MovementsRevAllocationsPanel movementsRevAllocationsPanel;

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
        addTab(PolarisUI.getMessage("CF_MAINTENANCE"), tabIcon, new JScrollPane(movementsMaintenancePanel),
                PolarisUI.getMessage("CF_MAINTENANCE_PANEL", PolarisUI.getMessage("MOVEMENT")));
        // END tab#1
        // tab#2 Batch Routes
        movementsBatchRoutesPanel = new MovementsBatchRoutesPanel(mainPanel);
        addTab(PolarisUI.getMessage("CF_BATCH_ROUTES"), tabIcon, new JScrollPane(movementsBatchRoutesPanel),
                PolarisUI.getMessage("CF_BATCH_ROUTES_PANEL", PolarisUI.getMessage("MOVEMENT")));
        // END tab#2
        // tab#3 Con Carrier Detail
        movementsCCDetailsPanel = new MovementsCCDetailsPanel(mainPanel);
        addTab(PolarisUI.getMessage("CF_CC_DETAILS"), tabIcon, new JScrollPane(movementsCCDetailsPanel),
                PolarisUI.getMessage("CF_CC_DETAILS_PANEL", PolarisUI.getMessage("MOVEMENT")));
        // END tab#3
        // tab#4 Tariffs
        movementsTariffsPanel = new MovementsTariffsPanel(mainPanel);
        addTab(PolarisUI.getMessage("CF_TARIFFS"), tabIcon, new JScrollPane(movementsTariffsPanel),
                PolarisUI.getMessage("CF_TARIFFS_PANEL", PolarisUI.getMessage("MOVEMENT")));
        // END tab#4
        // tab#5 Inventories
        movementsInventoriesPanel = new MovementsInventoriesPanel(mainPanel);
        addTab(PolarisUI.getMessage("CF_INVENTORIES"), tabIcon, new JScrollPane(movementsInventoriesPanel),
                PolarisUI.getMessage("CF_INVENTORIES_PANEL", PolarisUI.getMessage("MOVEMENT")));
        // END tab#5
        // tab#6 Rev Alloc
        movementsRevAllocationsPanel = new MovementsRevAllocationsPanel(mainPanel);
        addTab(PolarisUI.getMessage("CF_REV_ALLOCATIONS"), tabIcon, new JScrollPane(movementsRevAllocationsPanel),
                PolarisUI.getMessage("CF_REV_ALLOCATIONS_PANEL", PolarisUI.getMessage("MOVEMENT")));
        // END tab#6
        //---END Module Specific Code

        setTabPlacement(JTabbedPane.TOP);

        // Setup listeners/actions
        addChangeListener(this);
    }


}


