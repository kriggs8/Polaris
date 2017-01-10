package polaris.administration.tables.batchRoutes;


import polaris.administration.tables.*;
import polaris.constants.SpecialItems;
import polaris.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


/**
 * Prototype Code - Filter Panel Class for BATCH_ROUTES_CONF. This panel will be displayed in the Tabbed Panel class.
 * This class creates the all components required for the filter tab.  The component's have a dependencies with each other
 * For example; Changing the selection in the Scheduling System will filter the data being displayed
 * in all the combo box controls in this panel.
 * <p>
 * TODO: MORE COMMENTS
 *
 * @Author: Jean Wurster
 */

public class BatchRoutesFilterPanel extends GenericFilterPanel
{


    private VisiComboBox systemCombo;
    private VisiComboBox scheduleSystemCombo;
    private VisiComboBox batchRouteCombo;
    private VisiComboBox recLocationCombo;
    private VisiComboBox delLocationCombo;
    private VisiComboBox recConCarrierCombo;
    private VisiComboBox delConCarrierCombo;


    public BatchRoutesFilterPanel(MainPanel mainPanel) throws Exception
    {
        super();

        this.mainPanel = mainPanel;
        this.mainTable = mainPanel.mainTable;

        //---BEGIN Module Specific Code
        // This section will be used to add the comboboxes to the filter panel.
        // all combo boxes must be defined in the ComponentFactory.
        batchRouteCombo = ComponentFactory.getBatchRouteComboBox(Configuration.batchRouteWhere,
                                                                 Configuration.idDBName);
        scheduleSystemCombo = ComponentFactory.getScheduleSystemComboBox(Configuration.scheduleSystemBRWhere,
                                                                         Configuration.scheduleSystemDBName,
                                                                         SpecialItems.ALL);
        systemCombo = ComponentFactory.getSystemComboBox(Configuration.systemBRWhere, Configuration.systemDBName,
                                                         SpecialItems.ALL);
        recLocationCombo = ComponentFactory.getLocationComboBox(Configuration.recLocationBRWhere,
                                                                Configuration.recLocationDBName, SpecialItems.ALL);
        delLocationCombo = ComponentFactory.getLocationComboBox(Configuration.delLocationBRWhere,
                                                                Configuration.delLocationDBName, SpecialItems.ALL);
        recConCarrierCombo = ComponentFactory.getConCarrierComboBox(Configuration.recConCarrierBRWhere,
                                                                    Configuration.recConCarrierDBName,
                                                                    SpecialItems.ALL);
        delConCarrierCombo = ComponentFactory.getConCarrierComboBox(Configuration.delConCarrierBRWhere,
                                                                    Configuration.delConCarrierDBName,
                                                                    SpecialItems.ALL);

        this.resetGridBag();
        this.addToThisPanelAutoLayout(2, "SCHED_SYSTEM", scheduleSystemCombo);
        this.addToThisPanelAutoLayout(2, "SYSTEM", systemCombo);
        this.addToThisPanelAutoLayout(2, "REC_LOCATION", recLocationCombo);
        this.addToThisPanelAutoLayout(2, "DEL_LOCATION", delLocationCombo);
        this.addToThisPanelAutoLayout(2, "REC_CON_CARRIER", recConCarrierCombo);
        this.addToThisPanelAutoLayout(2, "DEL_CON_CARRIER", delConCarrierCombo);
        this.addToThisPanelAutoLayout(2, "BATCH_ROUTE", batchRouteCombo);
        //---END Module Specific Code
        //adds the inactive indicator flag to this panel.
        addActiveInactiveComboBox(2);
        ComponentFactory.addListeners(this);
    }

    @Override
    public void setMenu(boolean onFlag)
    {
    }

    /**
     * set Help Topic Id
     */
    @Override
    public void setHelp()
    {
        this.mainPanel.setContextHelpTopicId("BatchRoutesConfigurationModule_FilterTab");
    }




    /**
     * method is triggered when components (comboboxes) in the filter panel has an action being done on it
     * Actions on comboboxes will update dependent comboboxes model factory.
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e)
    {
        //if the component does not have focus
        //return out of this method.
        //this occurs if action being done on the component is from another method
        //not from user doing the action
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
     * @param e
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
            //if actions are on a JComboBox then
            //update the dependent compboboxes

            if (action != systemCombo.getName() && !systemCombo.hasFocus() ||
                systemCombo.hasFocus() && systemCombo.getSelectedItem().equals(SpecialItems.ALL))
            {

                //the focus is not on system and the action is not on the system.
                //update system combobox.
                //or the focus is on system and the user selects All, which causes the system
                //combo box to reload the model factory.
                ComponentFactory.updateCombo(systemCombo, Configuration.systemBRWhere + where + ")");

            }


            if (action != scheduleSystemCombo.getName() && !scheduleSystemCombo.hasFocus() ||
                scheduleSystemCombo.hasFocus() && scheduleSystemCombo.getSelectedItem() != null &&
                scheduleSystemCombo.getSelectedItem().equals(SpecialItems.ALL))
            {
                //the focus is not on sched system and the action is not on the sched system.
                //update sched system combobox.
                //or the focus is on sched system and the user selects All, which causes the sched system
                //combo box to reload the model factory.
                ComponentFactory.updateCombo(scheduleSystemCombo, Configuration.scheduleSystemBRWhere + where + ")");

            }

            if (action != batchRouteCombo.getName() && !batchRouteCombo.hasFocus() ||
                batchRouteCombo.hasFocus() && batchRouteCombo.getSelectedItem() != null &&
                batchRouteCombo.getSelectedItem().equals(SpecialItems.ALL))
            {
                ComponentFactory.updateCombo(batchRouteCombo, Configuration.batchRouteWhere + where + ")");
            }

            if (action != recLocationCombo.getName() && !recLocationCombo.hasFocus() ||
                recLocationCombo.hasFocus() && recLocationCombo.getSelectedItem() != null &&
                recLocationCombo.getSelectedItem().equals(SpecialItems.ALL))
            {
                ComponentFactory.updateCombo(recLocationCombo, Configuration.recLocationBRWhere + where + ")");
            }

            if (action != delLocationCombo.getName() && !delLocationCombo.hasFocus() ||
                delLocationCombo.hasFocus() && delLocationCombo.getSelectedItem() != null &&
                delLocationCombo.getSelectedItem().equals(SpecialItems.ALL))
            {
                ComponentFactory.updateCombo(delLocationCombo, Configuration.delLocationBRWhere + where + ")");
            }

            if (action != recConCarrierCombo.getName() && !recConCarrierCombo.hasFocus() ||
                recConCarrierCombo.hasFocus() && recConCarrierCombo.getSelectedItem() != null &&
                recConCarrierCombo.getSelectedItem().equals(SpecialItems.ALL))
            {
                ComponentFactory.updateCombo(recConCarrierCombo, Configuration.recConCarrierBRWhere + where + ")");
            }

            if (action != delConCarrierCombo.getName() && !delConCarrierCombo.hasFocus() ||
                delConCarrierCombo.hasFocus() && delConCarrierCombo.getSelectedItem() != null &&
                delConCarrierCombo.getSelectedItem().equals(SpecialItems.ALL))
            {
                ComponentFactory.updateCombo(delConCarrierCombo, Configuration.delConCarrierBRWhere + where + ")");
            }


        }
        ui.setDefaultCursor();


    }



}
