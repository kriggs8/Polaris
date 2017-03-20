package polaris.administration.tables.facilities;

import polaris.administration.tables.ComponentFactory;
import polaris.administration.tables.GenericFilterPanel;
import polaris.administration.tables.MainPanel;
import polaris.constants.SpecialItems;
import polaris.util.ValidatedTextField;
import polaris.util.VisiComboBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Filter Panel Class for FACILITY_CONF. This panel will be displayed in the Tabbed Panel class.
 * This class creates all components required for the filter tab.
 * The component's have dependencies with each other
 *
 * @Author TCI Krista Riggs
 * Created 3/16/2017.
 */
public class FacilitiesFilterPanel extends GenericFilterPanel
{
    private VisiComboBox       companyCombo;
    private VisiComboBox       systemGroupCombo;
    private VisiComboBox       systemCombo;
    private VisiComboBox       locationCombo;
    private ValidatedTextField descriptionTextField;

    public FacilitiesFilterPanel(MainPanel mainPanel) throws Exception
    {
        super();
        this.mainPanel = mainPanel;
        this.mainTable = mainPanel.mainTable;

        /**---BEGIN Module Specific Code
         * This section will be used to add the comboboxes to the filter panel.
         * all combo boxes must be defined in the ComponentFactory.
         */
        companyCombo = ComponentFactory.getComboBox(true, "COMPANY_CONF", FacilitiesConstants.companyDBWhere0,
                                                    FacilitiesConstants.companyDBName, SpecialItems.ALL);
        descriptionTextField = ComponentFactory.getTextField(true, FacilitiesConstants.descriptionDBName, 255);
        systemGroupCombo = ComponentFactory.getComboBox(true, "SYSTEM_GROUP_CONF",
                                                        FacilitiesConstants.systemGroupDBWhere0,
                                                        FacilitiesConstants.systemGroupDBName, SpecialItems.ALL);
        systemCombo = ComponentFactory.getComboBox(true, "SYSTEM_CONF", FacilitiesConstants.systemDBWhere0,
                                                   FacilitiesConstants.systemDBName, SpecialItems.ALL);
        locationCombo = ComponentFactory.getComboBox(true, "LOCATION_CONF", FacilitiesConstants.locationDBWhere0,
                                                     FacilitiesConstants.locationDBName, SpecialItems.ALL);

        //Arrange elements on the Panel
        this.resetGridBag();
        this.addToThisPanelAutoLayout(2, "COMPANY", companyCombo);
        this.addToThisPanelAutoLayout(2, "DESCRIPTION", descriptionTextField);
        this.addToThisPanelAutoLayout(2, "SYSTEM_GROUP_ID", systemGroupCombo);
        this.addToThisPanelAutoLayout(2, "SYSTEM", systemCombo);
        this.addToThisPanelAutoLayout(2, "LOCATION", locationCombo);

        /**
         * ---END Module Specific Code
         */
        //adds the inactive indicator flag to this panel
        addActiveInactiveComboBox(2);
        ComponentFactory.addListeners(this);
    }

    /**
     * method is triggered when components (comboboxes) in the filter panel has an action being done on it
     * Actions on comboboxes will update dependent comboboxes model factory.
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e)
    {
        //if the component does not have focus return out of this method.
        //this occurs if action being done on the component is from another method not from user doing the action
        Component component = (Component) e.getSource();
        if (!component.hasFocus())
        {
            return;
        }
        //updates the combo boxes model factory.
        updateCombo(e);
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
        if (!where.isEmpty())
        {
            where = " WHERE \n" + where;
        }

        ui.setBusyCursor();
        String action = e.getActionCommand();
        if (e.getSource() instanceof JComboBox)
        {
            /**if actions are taken on a JComboBox then
             * update the dependent combo boxes
             */
            if (action != companyCombo.getName() && !companyCombo.hasFocus() ||
                companyCombo.hasFocus() && companyCombo.getSelectedItem().equals(SpecialItems.ALL))
            {
                //use the default query to update the list for only system combo being populated
                ComponentFactory.updateCombo(companyCombo, FacilitiesConstants.companyDBWhere0 + where + ")");
            }
            if (action != systemGroupCombo.getName() && !systemGroupCombo.hasFocus() ||
                systemGroupCombo.hasFocus() && systemGroupCombo.getSelectedItem().equals(SpecialItems.ALL))
            {
                //use the default query to update the list for only system combo being populated
                ComponentFactory.updateCombo(systemGroupCombo, FacilitiesConstants.systemGroupDBWhere0 + where + ")");
            }
            if (action != systemCombo.getName() && !systemCombo.hasFocus() ||
                systemCombo.hasFocus() && systemCombo.getSelectedItem().equals(SpecialItems.ALL))
            {
                //use the default query to update the list for only system combo being populated
                ComponentFactory.updateCombo(systemCombo, FacilitiesConstants.systemDBWhere0 + where + ")");
            }
            if (action != locationCombo.getName() && !locationCombo.hasFocus() ||
                locationCombo.hasFocus() && locationCombo.getSelectedItem().equals(SpecialItems.ALL))
            {
                //use the default query to update the list for only system combo being populated
                ComponentFactory.updateCombo(locationCombo, FacilitiesConstants.locationDBWhere0 + where + ")");
            }
        }
        ui.setDefaultCursor();
    }

    /**
     * Method to implement a specific panel where clause
     * this is used for filter fields that do not exist in the main table.
     * for example; FACILITY_CONF is filtered by scheduling system and system and grade.
     * these fields do not exist in the main table therefore a special where clause had to be created for
     * the Facilities panel to handel these filter fields
     *
     * @return - the Where Clause
     */
    public String getWhereClause()
    {
        //get the default where clause for the panel.
        String where = whereClause.buildWhereClause(this);

        //if the field populated is the company combo box then look up data using the following tables
        //FACILITY_CONF and UNIONED across POLARIS.FACILITY_CONF F, POLARIS.FACILITY_LOCATION_CONTACT_XREF X, POLARIS.GROUP_LOCATION_XREF GLX, POLARIS.SYSTEM_CONF SC
        if (where.contains(FacilitiesConstants.companyDBName))
        {
            return FacilitiesConstants.companyDBWhere0;
        }
        //if the field populated is the Systems Group combo box then look up data using the following tables
        //POLARIS.FACILITY_CONF F, POLARIS.FACILITY_LOCATION_CONTACT_XREF X, POLARIS.GROUP_LOCATION_XREF GLX, POLARIS.SYSTEM_CONF SC
        else if (where.contains(FacilitiesConstants.systemGroupDBName))
        {
            return FacilitiesConstants.systemGroupDBWhere0;
        }
        //if the field populated is the Systems combo box then look up data using the following tables
        //POLARIS.FACILITY_CONF F, POLARIS.FACILITY_LOCATION_CONTACT_XREF X, POLARIS.GROUP_LOCATION_XREF GLX, POLARIS.SYSTEM_CONF SC
        else if (where.contains(FacilitiesConstants.systemDBName))
        {
            return FacilitiesConstants.systemDBWhere0;
        }
        //if the field populated is the Locations combo box then look up data using the following tables
        //POLARIS.FACILITY_CONF F, POLARIS.FACILITY_LOCATION_CONTACT_XREF X, POLARIS.GROUP_LOCATION_XREF GLX, POLARIS.SYSTEM_CONF SC
        else if (where.contains(FacilitiesConstants.locationDBName))
        {
            return FacilitiesConstants.locationDBWhere0;
        }
        else
        {
            //if no other combo boxes are populated then tank grade group look up data using
            //FACILITY_CONF
            return FacilitiesConstants.facilityDBWhereDefault;
        }
    }
}
