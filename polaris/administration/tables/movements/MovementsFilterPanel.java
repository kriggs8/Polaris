package polaris.administration.tables.movements;

import polaris.administration.tables.ComponentFactory;
import polaris.administration.tables.Configuration;
import polaris.administration.tables.GenericFilterPanel;
import polaris.administration.tables.MainPanel;
import polaris.constants.SpecialItems;
import polaris.util.VisiComboBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 *  Filter Panel Class for MOVEMENT_CONF. This panel will be displayed in the Tabbed Panel class.
 * @Author: Waleed Elsaid on 1/9/2017.
 */

public class MovementsFilterPanel extends GenericFilterPanel {

    // all the combo boxes for the filter tab pan
    private VisiComboBox systemGroupCombo;
    private VisiComboBox lineSpaceCombo;
    private VisiComboBox gradeSpecCombo;
    private VisiComboBox movementCombo;
    private VisiComboBox recLocationCombo;
    private VisiComboBox recConCarrierCombo;
    private VisiComboBox delLocationCombo;
    private VisiComboBox delConCarrierCombo;

    public MovementsFilterPanel(MainPanel mainPanel) throws Exception {
        super();
        this.mainPanel = mainPanel;
        this.mainTable = mainPanel.mainTable;
        // Movements Filter Tab Components are constructed here via the ComponentFactory Util class
        systemGroupCombo = ComponentFactory.getComboBox(Configuration.systemGroupTableName,
                                                        MovementsContants.SYSTEM_GROUP_WHERE_CLAUSE,
                                                        Configuration.systemGroupDBName,
                                                        SpecialItems.ALL);
        lineSpaceCombo = ComponentFactory.getComboBox(Configuration.lineSpaceTableName,
                                                      MovementsContants.LINE_SPACE_WHERE_CLAUSE,
                                                      Configuration.linespaceDBName,
                                                      SpecialItems.ALL);
        gradeSpecCombo = ComponentFactory.getComboBox(Configuration.gradeSpecTableName,
                                                      MovementsContants.GRADE_SPEC_WHERE_CLAUSE,
                                                      Configuration.gradeSpecDBName,
                                                      SpecialItems.ALL);
        movementCombo = ComponentFactory.getComboBox(Configuration.movementTableName,
                                                     MovementsContants.MOVEMENT_WHERE_CLAUSE,
                                                     Configuration.movementDBName,
                                                     SpecialItems.ALL);
        recLocationCombo = ComponentFactory.getComboBox(Configuration.locationTableName,
                                                        MovementsContants.REC_LOCATION_WHERE_CLAUSE,
                                                        Configuration.recLocationDBName,
                                                        SpecialItems.ALL);
        recConCarrierCombo = ComponentFactory.getComboBox(Configuration.conCarrierTableName,
                                                          MovementsContants.REC_CON_CARRIER_WHERE_CLAUSE,
                                                          Configuration.recConCarrierDBName,
                                                          SpecialItems.ALL);
        delLocationCombo = ComponentFactory.getComboBox(Configuration.locationTableName,
                                                        MovementsContants.DEL_LOCATION_WHERE_CLAUSE,
                                                        Configuration.delLocationDBName,
                                                        SpecialItems.ALL);
        delConCarrierCombo = ComponentFactory.getComboBox(Configuration.conCarrierTableName,
                                                          MovementsContants.DEL_CON_CARRIER_WHERE_CLAUSE,
                                                          Configuration.delConCarrierDBName,
                                                          SpecialItems.ALL);
        // Now let's put them in the tabbed pane
        this.resetGridBag();
        this.addToThisPanelAutoLayout(2, "SYSTEM_GROUP", systemGroupCombo);
        this.addToThisPanelAutoLayout(2, "LINESPACE", lineSpaceCombo);
        this.addToThisPanelAutoLayout(2, "GRADE_SPEC", gradeSpecCombo);
        this.addToThisPanelAutoLayout(2, "MOVEMENT", movementCombo);
        this.addToThisPanelAutoLayout(2, "REC_LOCATION", recLocationCombo);
        this.addToThisPanelAutoLayout(2, "REC_CON_CARRIER", recConCarrierCombo);
        this.addToThisPanelAutoLayout(2, "DEL_LOCATION", delLocationCombo);
        this.addToThisPanelAutoLayout(2, "DEL_CON_CARRIER", delConCarrierCombo);
        this.addToThisPanelAutoLayout(2, "", null); // just to keep the activeInactive combo on the right like the FRDD
        //---END Module Specific Code
        //adds the inactive indicator flag to this panel.
        addActiveInactiveComboBox(2);
        ComponentFactory.addListeners(this);
    }

    @Override
    public void setMenu(boolean onFlag) {
    }

    /**
     * set Help Topic Id
     */
    @Override
    public void setHelp() {
        this.mainPanel.setContextHelpTopicId("MovementsConfigurationModule_FilterTab");
    }


    /**
     * method is triggered when components (comboboxes) in the filter panel has an action being done on it
     * Actions on comboboxes will update dependent comboboxes model factory.
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        //if the component does not have focus
        //return out of this method.
        //this occurs if action being done on the component is from another method
        //not from user doing the action
        Component component = (Component) e.getSource();
        if (!component.hasFocus()) {
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
    public void updateCombo(ActionEvent e) {


        //loop thru the panel's components
        //and build a where clause string.
        String where = whereClause.buildWhereClause(this);
        if (!where.isEmpty()) {
            where = " WHERE \n" + where;
        }

        ui.setBusyCursor();
        String action = e.getActionCommand();
        if (e.getSource() instanceof JComboBox) {
            /* In the following logic statements check if actions are on the corresponding ComboBox then
               update the dependent compboBoxes, or if the focus is not on the ComboBox and the action is not on it
               then, update  systemGroup Combobox, or finally if the focus is on ComboBox and the user selects All,
               which causes the ComboBox to reload the model factory.
            */
            if (action != systemGroupCombo.getName() && !systemGroupCombo.hasFocus() ||
                    systemGroupCombo.hasFocus() && systemGroupCombo.getSelectedItem().equals(SpecialItems.ALL)) {
                ComponentFactory.updateCombo(systemGroupCombo, MovementsContants.SYSTEM_GROUP_WHERE_CLAUSE + where + ")");
            }

            if (action != lineSpaceCombo.getName() && !lineSpaceCombo.hasFocus() ||
                    lineSpaceCombo.hasFocus() && lineSpaceCombo.getSelectedItem() != null &&
                            lineSpaceCombo.getSelectedItem().equals(SpecialItems.ALL)) {
                ComponentFactory.updateCombo(lineSpaceCombo, MovementsContants.LINE_SPACE_WHERE_CLAUSE + where + ")");

            }

            if (action != gradeSpecCombo.getName() && !gradeSpecCombo.hasFocus() ||
                    gradeSpecCombo.hasFocus() && gradeSpecCombo.getSelectedItem() != null &&
                            gradeSpecCombo.getSelectedItem().equals(SpecialItems.ALL)) {
                ComponentFactory.updateCombo(gradeSpecCombo, MovementsContants.GRADE_SPEC_WHERE_CLAUSE + where + ")");
            }

            if (action != movementCombo.getName() && !movementCombo.hasFocus() ||
                    movementCombo.hasFocus() && movementCombo.getSelectedItem() != null &&
                            movementCombo.getSelectedItem().equals(SpecialItems.ALL)) {
                ComponentFactory.updateCombo(movementCombo, MovementsContants.MOVEMENT_WHERE_CLAUSE + where + ")");
            }

            if (action != recLocationCombo.getName() && !recLocationCombo.hasFocus() ||
                    recLocationCombo.hasFocus() && recLocationCombo.getSelectedItem() != null &&
                            recLocationCombo.getSelectedItem().equals(SpecialItems.ALL)) {
                ComponentFactory.updateCombo(recLocationCombo, MovementsContants.REC_LOCATION_WHERE_CLAUSE + where + ")");
            }

            if (action != recConCarrierCombo.getName() && !recConCarrierCombo.hasFocus() ||
                    recConCarrierCombo.hasFocus() && recConCarrierCombo.getSelectedItem() != null &&
                            recConCarrierCombo.getSelectedItem().equals(SpecialItems.ALL)) {
                ComponentFactory.updateCombo(recConCarrierCombo, MovementsContants.REC_CON_CARRIER_WHERE_CLAUSE + where + ")");
            }

            if (action != delLocationCombo.getName() && !delLocationCombo.hasFocus() ||
                    delLocationCombo.hasFocus() && delLocationCombo.getSelectedItem() != null &&
                            delLocationCombo.getSelectedItem().equals(SpecialItems.ALL)) {
                ComponentFactory.updateCombo(delLocationCombo, MovementsContants.DEL_LOCATION_WHERE_CLAUSE + where + ")");
            }

            if (action != delConCarrierCombo.getName() && !delConCarrierCombo.hasFocus() ||
                    delConCarrierCombo.hasFocus() && delConCarrierCombo.getSelectedItem() != null &&
                            delConCarrierCombo.getSelectedItem().equals(SpecialItems.ALL)) {
                ComponentFactory.updateCombo(delConCarrierCombo, MovementsContants.DEL_CON_CARRIER_WHERE_CLAUSE + where + ")");
            }


        }
        ui.setDefaultCursor();
    }
}