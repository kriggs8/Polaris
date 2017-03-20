package polaris.administration.tables.companies;

import polaris.administration.tables.ComponentFactory;
import polaris.administration.tables.GenericFilterPanel;
import polaris.administration.tables.MainPanel;
import polaris.constants.SpecialItems;
import polaris.util.ValidatedTextField;
import polaris.util.VisiComboBox;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Filter Panel Class for COMPANY_CONF. This panel will be displayed in the Tabbed Panel class.
 * This class creates all components required for the filter tab.
 * The component's have dependencies with each other
 *
 * @Author TCI - Krista Riggs
 * Created  3/7/2017.
 */
public class CompaniesFilterPanel extends GenericFilterPanel
{
    private ValidatedTextField nameTextField;
    private ValidatedTextField descriptionTextField;
    private VisiComboBox       stateComboBox;

    /**
     * Create the Main Panel for Filter Tab
     *
     * @param mainPanel - the main panel
     * @throws Exception
     */
    public CompaniesFilterPanel(MainPanel mainPanel) throws Exception
    {
        super();
        this.mainPanel = mainPanel;
        this.mainTable = mainPanel.mainTable;

        /**---BEGIN Module Specific Code
         * This section will be used to add the text fields and combo boxes to the filter panel.
         */
        nameTextField = ComponentFactory.getTextField(true, CompaniesConstants.nameDBName, 255);

        descriptionTextField = ComponentFactory.getTextField(true, CompaniesConstants.descriptionDBName, 255);

        stateComboBox = ComponentFactory.getComboBox(true, CompaniesConstants.stateTableName,
                                                     CompaniesConstants.stateDBWhere, CompaniesConstants.stateDBName,
                                                     SpecialItems.ALL);

        //arrange the elements on the Panel
        this.resetGridBag();
        this.addToThisPanelAutoLayout(2, "NAME", nameTextField);
        this.addToThisPanelAutoLayout(2, "DESCRIPTION", descriptionTextField);
        this.addToThisPanelAutoLayout(2, "STATE", stateComboBox);
        /**
         * ---END Module Specific Code
         */

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
             * update for system group box
             * if focus is not on the system group combo box and the action is not on the system group combo
             * or the system group combo box has focus and the selected item is ALL
             */
            if (action != stateComboBox.getName() && !stateComboBox.hasFocus() ||
                stateComboBox.hasFocus() && stateComboBox.getSelectedItem().equals(SpecialItems.ALL))
            {
                //use the default query to update the list for only system combo being populated
                ComponentFactory.updateCombo(stateComboBox, CompaniesConstants.stateDBWhere + where + ")");
            }
        }
        ui.setDefaultCursor();
    }
}
