package polaris.administration.tables.contacts;

import polaris.administration.tables.GenericTabbedPane;
import polaris.frame.PolarisUI;

import javax.swing.*;

/**
 * Tabbed Panel Class for CONTACTS_CONF.
 * It instantiates the Filter, Maintenance and Companies Tabs.
 *
 * @Author TCI - Krista Riggs
 * Created on 2/2/2017.
 */
public class ContactsTabbedPane extends GenericTabbedPane
{
    //Instantiate the Panels
    private ContactsFilterPanel      contactsFilterPanel;
    private ContactsMaintenancePanel contactsMaintenancePanel;
    private ContactsCompaniesPanel   contactsCompaniesPanel;

    /**
     * Create Contacts tabbed pane constructor
     *
     * @throws Exception
     */
    public ContactsTabbedPane() throws Exception
    {
        super();

        mainPanel = ContactsPanel.mainPanel;
        mainTable = mainPanel.mainTable;

        //get the UI and the tabIcon
        PolarisUI ui      = PolarisUI.getUIInstance();
        ImageIcon tabIcon = (ImageIcon) ui.getValue(ui.DEFAULT_TAB_ICON);

        //---BEGIN Module Specific Code
        // This section will be used to add the tabs to the Tabbed Pane.

        //tab0--Filter Panel
        contactsFilterPanel = new ContactsFilterPanel(mainPanel);
        addTab(PolarisUI.getMessage("CF_FILTER"), tabIcon, contactsFilterPanel,
               PolarisUI.getMessage("CF_FILTER_PANEL", PolarisUI.getMessage("CONTACT")));

        //tab1--Maintenance Panel
        contactsMaintenancePanel = new ContactsMaintenancePanel(mainPanel);
        addTab(PolarisUI.getMessage("CF_MAINTENANCE"), tabIcon, new JScrollPane(contactsMaintenancePanel),
               PolarisUI.getMessage("CF_MAINTENANCE_PANEL", PolarisUI.getMessage("CONTACT")));

        //tab2--Companies Panel
        contactsCompaniesPanel = new ContactsCompaniesPanel(mainPanel);
        addTab(PolarisUI.getMessage("CF_COMPANIES"), tabIcon, new JScrollPane(contactsCompaniesPanel),
               PolarisUI.getMessage("CF_COMPANIES_PANEL", PolarisUI.getMessage("CONTACT")));

        //---END Module Specific Code
        //set the placement for the tabbed panels
        setTabPlacement(JTabbedPane.TOP);

        // Setup listeners/actions
        addChangeListener(this);
    }
}
