package polaris.administration.tables.contacts;

import polaris.administration.tables.ComponentFactory;
import polaris.administration.tables.GenericFilterPanel;
import polaris.administration.tables.MainPanel;
import polaris.constants.SpecialItems;
import polaris.util.ValidatedTextField;
import polaris.util.VisiComboBox;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Filter Panel Class for CONTACTS_CONF. This panel will be displayed in the Tabbed Panel class.
 * This class creates all components required for the filter tab.
 * The component's have dependencies with each other
 *
 * @Author TCI - Krista Riggs
 * Created on 2/2/2017.
 */
public class ContactsFilterPanel extends GenericFilterPanel
{
    private VisiComboBox       companyCombo;
    private VisiComboBox       systemGroupCombo;
    private VisiComboBox       schedSystemCombo;
    private VisiComboBox       locationCombo;
    private VisiComboBox       conCarrierCombo;
    private VisiComboBox       shipperCombo;
    private VisiComboBox       physicalMeterCombo;
    private ValidatedTextField descriptionTextField;

    public ContactsFilterPanel(MainPanel mainPanel) throws Exception
    {
        super();

        this.mainPanel = mainPanel;
        this.mainTable = mainPanel.mainTable;

        /**---BEGIN Module Specific Code
         * This section will be used to add the comboboxes to the filter panel.
         * all combo boxes must be defined in the ComponentFactory.
         */
        //Set up Contacts company combo box
        companyCombo = ComponentFactory.getComboBox(true, "COMPANY_CONF", ContactsConstants.companyDBWhere0,
                                                    ContactsConstants.companyDBName, SpecialItems.ALL);

        descriptionTextField = ComponentFactory.getTextField(true, ContactsConstants.descriptionDBName, 255);

        systemGroupCombo = ComponentFactory.getComboBox(true, "SYSTEM_GROUP_CONF",
                                                        ContactsConstants.systemGroupDBWhere0,
                                                        ContactsConstants.systemGroupDBName, SpecialItems.ALL);

        schedSystemCombo = ComponentFactory.getComboBox(true, "SCHED_SYSTEM_CONF",
                                                        ContactsConstants.schedSystemDBWhere0,
                                                        ContactsConstants.schedSystemDBName, SpecialItems.ALL);

        locationCombo = ComponentFactory.getComboBox(true, "LOCATION_CONF", ContactsConstants.locationDBWhere0,
                                                     ContactsConstants.locationDBName, SpecialItems.ALL);

        conCarrierCombo = ComponentFactory.getComboBox(true, "CON_CARRIER_CONF", ContactsConstants.conCarrierDBWhere0,
                                                       ContactsConstants.conCarrierDBName, SpecialItems.ALL);

        shipperCombo = ComponentFactory.getComboBox(true, "SHIPPER_CONF", ContactsConstants.shipperDBWhere0,
                                                    ContactsConstants.shipperDBName, SpecialItems.ALL);
        physicalMeterCombo = ComponentFactory.getComboBox(true, "PHYSICAL_METER_CONF",
                                                          ContactsConstants.physicalMeterDBWhere0,
                                                          ContactsConstants.physicalMeterDBName, SpecialItems.ALL);


        //Arrange elements on the Panel
        this.resetGridBag();
        this.addToThisPanelAutoLayout(2, "COMPANY", companyCombo);
        this.addToThisPanelAutoLayout(2, "DESCRIPTION", descriptionTextField);
        this.addToThisPanelAutoLayout(2, "SYSTEM_GROUP_ID", systemGroupCombo);
        this.addToThisPanelAutoLayout(2, "SCHED_SYSTEM", schedSystemCombo);
        this.addToThisPanelAutoLayout(2, "LOCATION", locationCombo);
        this.addToThisPanelAutoLayout(2, "CON_CARRIER", conCarrierCombo);
        this.addToThisPanelAutoLayout(2, "SHIPPER", shipperCombo);
        this.addToThisPanelAutoLayout(2, "PROVER_DISTRIBUTION", physicalMeterCombo);
        /**
         * ---END Module Specific Code
         */
        //blank filler so layout looks correct
        this.addToThisPanelAutoLayout(2, null, null);
        //adds the inactive indicator flag to this panel.
        addActiveInactiveComboBox(2);
        ComponentFactory.addListeners(this);
    }

    /**
     * Updates the combo boxes model factory
     *
     * @param e - the Action event to update the combo box
     */
    public void updateCombo(ActionEvent e)
    {
        //loop thru the panel's components
        //and build a where clause string.
        String where = whereClause.buildWhereClause(this);
        String activeWhere = inactiveCombo.getSelectedId() == 0 ? " AND INACTIVE_INDICATOR_FLAG = 0 " : "";

        if (!where.isEmpty())
        {
            where = " WHERE \n" + where;
        }

        // Close the main where and add the final INACTIVE_INDICATOR_FLAG
        where += " ) " + activeWhere;


        ui.setBusyCursor();
        String action = e.getActionCommand();
        if (e.getSource() instanceof JComboBox)
        {
            /**if actions are taken on a JComboBox then
             * update the dependent combo boxes
             *
             * update for system group box
             * if focus is not on the system group combo box and the action is not on the system group combo
             * or the system group combo box has focus and the selected item is ALL
             */
            if (action != companyCombo.getName() && !companyCombo.hasFocus() ||
                companyCombo.hasFocus() && companyCombo.getSelectedItem().equals(SpecialItems.ALL))
            {
                //use the default query to update the list for only system combo being populated
                ComponentFactory.updateCombo(companyCombo, ContactsConstants.companyDBWhere0 + where);
            }

            if (action != systemGroupCombo.getName() && !systemGroupCombo.hasFocus() ||
                systemGroupCombo.hasFocus() && systemGroupCombo.getSelectedItem().equals(SpecialItems.ALL))
            {
                //use the default query to update the list for only system combo being populated
                ComponentFactory.updateCombo(systemGroupCombo, ContactsConstants.systemGroupDBWhere0 + where);
            }

            if (action != schedSystemCombo.getName() && !schedSystemCombo.hasFocus() ||
                schedSystemCombo.hasFocus() && schedSystemCombo.getSelectedItem().equals(SpecialItems.ALL))
            {
                //use the default query to update the list for only system combo being populated
                ComponentFactory.updateCombo(schedSystemCombo, ContactsConstants.schedSystemDBWhere0 + where);
            }

            if (action != locationCombo.getName() && !locationCombo.hasFocus() ||
                locationCombo.hasFocus() && locationCombo.getSelectedItem().equals(SpecialItems.ALL))
            {
                //use the default query to update the list for only system combo being populated
                ComponentFactory.updateCombo(locationCombo, ContactsConstants.locationDBWhere0 + where);
            }

            if (action != conCarrierCombo.getName() && !conCarrierCombo.hasFocus() ||
                conCarrierCombo.hasFocus() && conCarrierCombo.getSelectedItem().equals(SpecialItems.ALL))
            {
                //use the default query to update the list for only system combo being populated
                ComponentFactory.updateCombo(conCarrierCombo, ContactsConstants.conCarrierDBWhere0 + where);
            }

            if (action != shipperCombo.getName() && !shipperCombo.hasFocus() ||
                shipperCombo.hasFocus() && shipperCombo.getSelectedItem().equals(SpecialItems.ALL))
            {
                //use the default query to update the list for only system combo being populated
                ComponentFactory.updateCombo(shipperCombo, ContactsConstants.shipperDBWhere0 + where);
            }

            if (action != physicalMeterCombo.getName() && !physicalMeterCombo.hasFocus() ||
                physicalMeterCombo.hasFocus() && physicalMeterCombo.getSelectedItem().equals(SpecialItems.ALL))
            {
                //use the default query to update the list for only system combo being populated
                ComponentFactory.updateCombo(physicalMeterCombo, ContactsConstants.physicalMeterDBWhere0 + where);
            }
        }
        ui.setDefaultCursor();
    }

    @Override
    public String getOrderBy()
    {
        //need to order by Description (default is Name)
        return " ORDER BY DESCRIPTION ";
    }

    /**
     * Method to implement a specific panel where clause
     * this is used for filter fields that do not exist in the main table.
     * for example; CONTACT_CONF is filtered by
     * fields that do not exist in the main table therefore a special where clause had to be created for
     * the filter panel to handle these fields
     *
     * @return - the Where Clause
     */
    public String getWhereClause()
    {
        return ContactsConstants.contactDBWhere0;
    }
}
