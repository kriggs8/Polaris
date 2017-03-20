package polaris.administration.tables.facilities;

import polaris.administration.tables.GenericTabbedPane;
import polaris.frame.PolarisUI;

import javax.swing.*;

/**
 * Tabbed Panel Class for FACILITY_CONF.
 * It instantiates the Filter, Maintenance Tabs.
 *
 * @Author TCI Krista Riggs
 * Created 3/16/2017.
 */
public class FacilitiesTabbedPane extends GenericTabbedPane
{
    //Instantiate the Panels
    private FacilitiesFilterPanel facilitiesFilterPanel;
    private FacilitiesMaintenancePanel facilitiesMaintenancePanel;
    private FacilitiesContactsPanel facilitiesContactsPanel;

    public FacilitiesTabbedPane() throws Exception
    {
        super();
        mainPanel = FacilitiesPanel.mainPanel;
        mainTable = mainPanel.mainTable;

        //get the UI and the tabIcon
        PolarisUI ui      = PolarisUI.getUIInstance();
        ImageIcon tabIcon = (ImageIcon) ui.getValue(ui.DEFAULT_TAB_ICON);

        //---BEGIN Module Specific Code
        // This section will be used to add the tabs to the Tabbed Pane.

        //tab0--Filter Panel
        facilitiesFilterPanel = new FacilitiesFilterPanel(mainPanel);
        addTab(PolarisUI.getMessage("CF_FILTER"), tabIcon, facilitiesFilterPanel,
               PolarisUI.getMessage("CF_FILTER_PANEL", PolarisUI.getMessage("FACILITY")));

        //tab1--Maintenance Panel
        facilitiesMaintenancePanel = new FacilitiesMaintenancePanel(mainPanel);
        addTab(PolarisUI.getMessage("CF_MAINTENANCE"), tabIcon, new JScrollPane(facilitiesMaintenancePanel),
               PolarisUI.getMessage("CF_MAINTENANCE_PANEL", PolarisUI.getMessage("FACILITY")));

        //tab2--Contacts Panel
        facilitiesContactsPanel = new FacilitiesContactsPanel(mainPanel);
        addTab(PolarisUI.getMessage("CF_CONTACTS"), tabIcon, new JScrollPane(facilitiesContactsPanel),
               PolarisUI.getMessage("CF_CONTACTS_PANEL", PolarisUI.getMessage("FACILITY")));

        //---END Module Specific Code
        //set the placement for the tabbed panels
        setTabPlacement(JTabbedPane.TOP);

        // Setup listeners/actions
        addChangeListener(this);
    }
}
