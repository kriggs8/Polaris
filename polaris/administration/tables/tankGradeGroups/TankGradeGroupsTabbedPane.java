package polaris.administration.tables.tankGradeGroups;

import polaris.administration.tables.GenericTabbedPane;
import polaris.frame.PolarisUI;

import javax.swing.*;

/**
 * Tabbed Panel Class for TANK_GRADE_GROUP_CONF.
 * It instantiates the Filter, Maintenance and Grade Tabs.
 *
 * @Author: TCI - Krista Riggs
 * Created  1/10/17
 * Updated  2/6/17 - TCI Krista Riggs - Updated comments
 */
public class TankGradeGroupsTabbedPane extends GenericTabbedPane
{
    //Instantiate the Panels
    private TankGradeGroupsFilterPanel      tankGradeGroupsFilterPanel;
    private TankGradeGroupsMaintenancePanel tankGradeGroupsMaintenancePanel;
    private TankGradeGroupsGradesPanel      tankGradeGroupsGradesPanel;

    /**
     * Main method for Tank Grade Groups Tabbed Pane
     * @throws Exception
     */
    public TankGradeGroupsTabbedPane() throws Exception
    {
        super();

        mainPanel = TankGradeGroupsPanel.mainPanel;
        mainTable = mainPanel.mainTable;

        //get the UI and the tabIcon
        PolarisUI ui      = PolarisUI.getUIInstance();
        ImageIcon tabIcon = (ImageIcon) ui.getValue(ui.DEFAULT_TAB_ICON);

        //---BEGIN Module Specific Code
        // This section will be used to add the tabs to the Tabbed Pane.

        //tab0--Filter Panel
        tankGradeGroupsFilterPanel = new TankGradeGroupsFilterPanel(mainPanel);
        addTab(PolarisUI.getMessage("CF_FILTER"), tabIcon, tankGradeGroupsFilterPanel,
                PolarisUI.getMessage("CF_FILTER_PANEL", PolarisUI.getMessage("TANK_GRADE_GROUP")));

        //tab1--Maintenance Panel
        tankGradeGroupsMaintenancePanel = new TankGradeGroupsMaintenancePanel(mainPanel);
        addTab(PolarisUI.getMessage("CF_MAINTENANCE"), tabIcon, new JScrollPane(tankGradeGroupsMaintenancePanel),
                PolarisUI.getMessage("CF_MAINTENANCE_PANEL", PolarisUI.getMessage("TANK_GRADE_GROUP")));

        //tab2--Grades Panel
        tankGradeGroupsGradesPanel = new TankGradeGroupsGradesPanel(mainPanel);
        addTab(PolarisUI.getMessage("CF_GRADES"), tabIcon, new JScrollPane(tankGradeGroupsGradesPanel),
                PolarisUI.getMessage("CF_GRADES_PANEL", PolarisUI.getMessage("TANK_GRADE_GROUP")));

        //---END Module Specific Code
        //set the placement for the tabbed panels
        setTabPlacement(JTabbedPane.TOP);

        // Setup listeners/actions
        addChangeListener(this);
    }
}
