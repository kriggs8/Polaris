package polaris.administration.tables.tankGradeGroups;


import polaris.administration.tables.*;
import polaris.constants.SpecialItems;
import polaris.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


/**
 * Filter Panel Class for TANK_GRADE_GROUPS_CONF. This panel will be displayed in the Tabbed Panel class.
 * This class creates all components required for the filter tab.  The component's have dependencies with each other
 * For example; Changing the selection in the Scheduling System will filter the data being displayed
 * in all the other combo box controls in this panel.
 *
 * @Author: TCI - Krista Riggs
 * Created:  1/10/17
 * Updated:  1/17/17 TCI - Krista Riggs - added comments
 */

public class TankGradeGroupsFilterPanel extends GenericFilterPanel
{
    //Instantiate combo box variables
    private VisiComboBox gradeCombo;
    private VisiComboBox systemCombo;
    private VisiComboBox scheduleSystemCombo;
    private VisiComboBox tankGradeGroupCombo;

    /**
     * Create the Tank Grade Groups Filter Panel
     *
     * @param mainPanel - the Filter Panel
     * @throws Exception
     */
    public TankGradeGroupsFilterPanel(MainPanel mainPanel) throws Exception
    {
        super();

        this.mainPanel = mainPanel;
        this.mainTable = mainPanel.mainTable;

        //---BEGIN Module Specific Code
        /**
         * This section will be used to add the comboboxes to the filter panel.
         * all combo boxes must be defined in the ComponentFactory.
         */

        //Set up Tank Grade Group combo box
        tankGradeGroupCombo = ComponentFactory.getComboBox(true, "TANK_GRADE_GROUP_CONF",
                                                           TankGradeGroupsConstants.tankGradeGroupDBWhere0,
                                                           TankGradeGroupsConstants.tankGradeGroupDBName,
                                                           SpecialItems.ALL);
        //Set up Schedule System combo box
        scheduleSystemCombo = ComponentFactory.getScheduleSystemComboBox(TankGradeGroupsConstants.schedSystemDBWhere0,
                                                                         TankGradeGroupsConstants.schedSystemDBName,
                                                                         SpecialItems.ALL);
        //Set up System combo box
        systemCombo = ComponentFactory.getSystemComboBox(TankGradeGroupsConstants.systemDBWhere0,
                                                         TankGradeGroupsConstants.systemDBName, SpecialItems.ALL);
        //Set up Grade combo box
        gradeCombo = ComponentFactory.getComboBox(true, "GRADE_CONF", TankGradeGroupsConstants.gradeDBWhere0,
                                                  TankGradeGroupsConstants.gradeDBName, SpecialItems.ALL);

        //Arrange elements on the Panel
        this.resetGridBag();
        this.addToThisPanelAutoLayout(2, "SCHED_SYSTEM", scheduleSystemCombo);
        this.addToThisPanelAutoLayout(2, "SYSTEM", systemCombo);
        this.addToThisPanelAutoLayout(2, "GRADE", gradeCombo);
        this.addToThisPanelAutoLayout(2, "TANK_GRADE_GROUP", tankGradeGroupCombo);
        //---END Module Specific Code

        //blank filler so layout looks correct
        this.addToThisPanelAutoLayout(2, null, null);
        //adds the inactive indicator flag to this panel.
        addActiveInactiveComboBox(2);
        ComponentFactory.addListeners(this);
    }

    /**
     * This method is triggered when the components (comboboxes) in the filter panel have an action being done on it
     * Actions on comboboxes will update the dependent combo boxes model factory.
     *
     * @param e - The action performed
     */
    public void actionPerformed(ActionEvent e)
    {
        /**If the component does not have focus return out of this method.
         * this occurs if the action being done on the component is from another method
         * not from user doing the action
         */
        Component component = (Component) e.getSource();
        if (!component.hasFocus())
        {
            return;
        }

        //updates the combo boxes model factory.
        updateCombo(e);
    }

    /**
     * Method to implement a specific panel where clause
     * this is used for filter fields that do not exist in the main table.
     * for example; TANK_GRADE_GROUP_CONF is filtered by scheduling system and system and grade.
     * these fields do not exist in the main table therefore a special where clause had to be created for
     * the Tank Grade Groups panel to handel these filter fields
     *
     * @return - the Where Clause
     */
    public String getWhereClause()
    {
        //get the default where clause for the panel.
        String where = whereClause.buildWhereClause(this);

        if ((where.contains(TankGradeGroupsConstants.systemDBName) || where.contains(
                TankGradeGroupsConstants.schedSystemDBName)) && where.contains(TankGradeGroupsConstants.gradeDBName))
        {
            //if the field populated is the either the system or sched system and the grade
            //then look up data using the following tables
            //BATCH_ROUTE_CONF, BATCH_ROUTE_TANK_XREF, TANK_TANK_GRADE_GROUP_XREF and TANK_GRADE_GROUP_GRADE_XREF
            return TankGradeGroupsConstants.tankGradeGroupDBWhere1;

        }
        else if (where.contains(TankGradeGroupsConstants.systemDBName) || where.contains(
                TankGradeGroupsConstants.schedSystemDBName))
        {
            //if the field populated is the either the system or sched system
            //then look up data using the following tables
            //BATCH_ROUTE_CONF, BATCH_ROUTE_TANK_XREF, TANK_TANK_GRADE_GROUP_XREF
            return TankGradeGroupsConstants.tankGradeGroupDBWhere2;

        }
        else if (where.contains(TankGradeGroupsConstants.gradeDBName))
        {
            //if the field populated is just grade
            //looks up data using the following tables
            //TANK_GRADE_GROUP_GRADE_XREF
            return TankGradeGroupsConstants.tankGradeGroupDBWhere3;

        }
        else
        {
            //if no other combo boxes are populated then tank grade group
            //look up data using
            //TANK_GRADE_GROUP_CONF
            return TankGradeGroupsConstants.tankGradeGroupDBWhere0;

        }
    }


    /**
     * Updates the combo boxes model factory
     *
     * @param e - the Action event to update the combo box
     */
    @Override
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
             *
             * update for system combo box
             * if focus is not on the system combo box and the action is not on the system combo
             * or the system combo box has focus and the selected item is ALL
             */
            if (action != systemCombo.getName() && !systemCombo.hasFocus() ||
                systemCombo.hasFocus() && systemCombo.getSelectedItem().equals(SpecialItems.ALL))
            {
                //If the where clause contains a selected grade (grade combo box populated)
                if (where.contains(TankGradeGroupsConstants.gradeDBName))
                {
                    //The grade was populated, then update the system combo box list using a specific query
                    ComponentFactory.updateCombo(systemCombo, TankGradeGroupsConstants.systemDBWhere1 + where + ")");

                }
                else//grade was not populated
                {
                    //use the default query to update the list for only system combo being populated
                    ComponentFactory.updateCombo(systemCombo, TankGradeGroupsConstants.systemDBWhere0 + where + ")");

                }
            }

            //update for schedule system combo box
            if (action != scheduleSystemCombo.getName() && !scheduleSystemCombo.hasFocus() ||
                scheduleSystemCombo.hasFocus() && scheduleSystemCombo.getSelectedItem() != null &&
                scheduleSystemCombo.getSelectedItem().equals(SpecialItems.ALL))
            {
                //If the where clause contains a selected grade (grade combo box populated)
                if (where.contains(TankGradeGroupsConstants.gradeDBName))
                {

                    ComponentFactory.updateCombo(scheduleSystemCombo,
                                                 TankGradeGroupsConstants.schedSystemDBWhere1 + where + ")");

                }
                else//If grade is still at default 'ALL'
                {
                    ComponentFactory.updateCombo(scheduleSystemCombo,
                                                 TankGradeGroupsConstants.schedSystemDBWhere0 + where + ")");
                }
            }

            /** update for tankGradeGroupCombo
             * if the action is not on tank grade group combo AND tank grade group combo does not have focus
             * OR tank grade group combo has focus AND the selected item is not null AND it is populated as ALL
             */
            if (action != tankGradeGroupCombo.getName() && !tankGradeGroupCombo.hasFocus() ||
                tankGradeGroupCombo.hasFocus() && tankGradeGroupCombo.getSelectedItem() != null &&
                tankGradeGroupCombo.getSelectedItem().equals(SpecialItems.ALL))
            {
                if ((where.contains(TankGradeGroupsConstants.systemDBName) || where.contains(
                        TankGradeGroupsConstants.schedSystemDBName)) && where.contains(
                        TankGradeGroupsConstants.gradeDBName))
                {
                    //if system OR schedule system combo boxes are populated AND grade combo box is populated
                    //return the tank grade groups associated to the elements selected in the combo boxes
                    ComponentFactory.updateCombo(tankGradeGroupCombo,
                                                 TankGradeGroupsConstants.tankGradeGroupDBWhere1 + where + ")");
                }
                else if (where.contains(TankGradeGroupsConstants.systemDBName) || where.contains(
                        TankGradeGroupsConstants.schedSystemDBName))
                {
                    //else if the system OR the schedule system combo box are populated
                    //return the tank grade groups associated to the elements in the system OR schedSystem combo box
                    ComponentFactory.updateCombo(tankGradeGroupCombo,
                                                 TankGradeGroupsConstants.tankGradeGroupDBWhere2 + where + ")");
                }
                else if (where.contains(TankGradeGroupsConstants.gradeDBName))
                {
                    //else if the grade combo box is populated
                    //return the tank grade groups associated to the elements in the grade combo box
                    ComponentFactory.updateCombo(tankGradeGroupCombo,
                                                 TankGradeGroupsConstants.tankGradeGroupDBWhere3 + where + ")");
                }
                else
                {
                    //no other combo boxes are populated
                    //return all of the tank grade groups
                    ComponentFactory.updateCombo(tankGradeGroupCombo,
                                                 TankGradeGroupsConstants.tankGradeGroupDBWhere0 + where + ")");
                }
            }
            /** update for gradeCombo
             * if the action is not on grade combo AND grade combo does not have focus
             * OR grade combo has focus AND the selected item is not null AND it is populated as ALL
             */
            if (action != gradeCombo.getName() && !gradeCombo.hasFocus() ||
                gradeCombo.hasFocus() && gradeCombo.getSelectedItem() != null && gradeCombo.getSelectedItem().equals(
                        SpecialItems.ALL))
            {
                if ((where.contains(TankGradeGroupsConstants.systemDBName) || where.contains(
                        TankGradeGroupsConstants.schedSystemDBName)) && where.contains(
                        TankGradeGroupsConstants.tankGradeGroupDBName))
                {   //if the where clause contains system OR sched system AND tank grade group
                    ComponentFactory.updateCombo(gradeCombo, TankGradeGroupsConstants.gradeDBWhere1 + where + ")");
                }
                else if (where.contains(TankGradeGroupsConstants.systemDBName) || where.contains(
                        TankGradeGroupsConstants.schedSystemDBName))
                {   //else if where clause contains system OR sched system
                    ComponentFactory.updateCombo(gradeCombo, TankGradeGroupsConstants.gradeDBWhere2 + where + ")");
                }
                else
                {   //else only the grade is populated
                    ComponentFactory.updateCombo(gradeCombo, TankGradeGroupsConstants.gradeDBWhere0 + where + ")");
                }
            }
        }
        ui.setDefaultCursor();
    }
}
