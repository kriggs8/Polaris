package polaris.administration.tables.movements;

import polaris.administration.tables.GenericTabbedPane;
import polaris.frame.PolarisUI;

import javax.swing.*;

/**
 * Tabbed Panel Class for MOVEMENT_CONF
 * @Author: Waleed Elsaid on 1/9/2017.
 */

public class MovementsTabbedPane extends GenericTabbedPane
{
    private MovementsFilterPanel movementsFilterPanel;
    public MovementsTabbedPane() throws Exception
    {
        super();

        mainPanel = MovementsPanel.mainPanel;
        mainTable = mainPanel.mainTable;

        PolarisUI ui = PolarisUI.getUIInstance();
        ImageIcon tabIcon = (ImageIcon) ui.getValue(ui.DEFAULT_TAB_ICON);

        //---BEGIN Module Specific Code
        // This section will be used to add the tabs to the Tabbed Pane.
        // Filter
        //tab0
        movementsFilterPanel = new MovementsFilterPanel(mainPanel);
        addTab(PolarisUI.getMessage("CF_FILTER"), tabIcon, movementsFilterPanel,
                PolarisUI.getMessage("CF_FILTER_PANEL", PolarisUI.getMessage("MOVEMENT")));
        // END tab0
        //---END Module Specific Code

        setTabPlacement(JTabbedPane.TOP);

        // Setup listeners/actions
        addChangeListener(this);
    }


}
