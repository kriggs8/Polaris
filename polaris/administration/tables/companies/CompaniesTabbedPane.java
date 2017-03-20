package polaris.administration.tables.companies;

import polaris.administration.tables.GenericTabbedPane;
import polaris.frame.PolarisUI;

import javax.swing.*;

/**
 * Tabbed Panel Class for COMPANY_CONF
 * It instantiates the Filter and Maintenance Tabs
 * @Author TCI-Krista Riggs
 * Created 3/7/2017.
 */
public class CompaniesTabbedPane extends GenericTabbedPane
{
    private CompaniesFilterPanel companiesFilterPanel;
    private CompaniesMaintenancePanel companiesMaintenancePanel;

    /**
     * Create Companies tabbed pane constructor
     * @throws Exception
     */
    public CompaniesTabbedPane() throws Exception{
        super();

        mainPanel = CompaniesPanel.mainPanel;
        mainTable = mainPanel.mainTable;

        //get the UI and the tabIcon
        PolarisUI ui      = PolarisUI.getUIInstance();
        ImageIcon tabIcon = (ImageIcon) ui.getValue(ui.DEFAULT_TAB_ICON);
        /**
         *  ---BEGIN Module Specific Code
         *  This section will be used to add the tabs to the Tabbed Pane.
         */

        //tab0 Filter
        companiesFilterPanel = new CompaniesFilterPanel(mainPanel);
        addTab(PolarisUI.getMessage("CF_FILTER"), tabIcon, companiesFilterPanel,
               PolarisUI.getMessage("CF_FILTER_PANEL", PolarisUI.getMessage("COMPANY")));

        //tab1 Maintenance
        companiesMaintenancePanel = new CompaniesMaintenancePanel(mainPanel);
        addTab(PolarisUI.getMessage("CF_MAINTENANCE"), tabIcon, new JScrollPane(companiesMaintenancePanel),
               PolarisUI.getMessage("CF_MAINTENANCE_PANEL", PolarisUI.getMessage("CONTACT")));

        /**
         * ---END Module Specific Code
         * Set the placement for the Tabbed panels
         */
        setTabPlacement(JTabbedPane.TOP);
        //Setup listeners/actions
        addChangeListener(this);
    }

}
