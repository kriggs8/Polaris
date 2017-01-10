package polaris.administration.tables.batchRoutes;

import polaris.administration.tables.GenericTabbedPane;
import polaris.administration.tables.GenericTableModel;
import polaris.administration.tables.MainPanel;
import polaris.frame.PolarisUI;

import javax.swing.*;

/**
 * Prototype Code - Tabbed Panel Class for BATCH_ROUTES_CONF. It instantiates the Filter, Maintenance, Segment
 * Meters, Manifolds, Tanks and Agg Movements Tabs.
 * <p>
 * TODO: MORE COMMENTS
 *
 * @Author: Jean Wurster
 */
public class BatchRoutesTabbedPane extends GenericTabbedPane
{


    private BatchRoutesFilterPanel       batchRoutesFilterPanel;
    private BatchRoutesMaintenancePanel  batchRoutesMaintenancePanel;
    private BatchRoutesGradesPanel       batchRoutesGradesPanel;
    private BatchRoutesSegmentsPanel     batchRoutesSegmentsPanel;
    private BatchRoutesMetersPanel       batchRoutesMetersPanel;
    private BatchRoutesManifoldsPanel    batchRoutesManifoldsPanel;
    private BatchRoutesTanksPanel        batchRoutesTanksPanel;
    private BatchRoutesAggMovementsPanel batchRoutesAggMovementsPanel;

    public BatchRoutesTabbedPane() throws Exception
    {
        super();

        mainPanel = BatchRoutesPanel.mainPanel;
        mainTable = mainPanel.mainTable;

        PolarisUI ui      = PolarisUI.getUIInstance();
        ImageIcon tabIcon = (ImageIcon) ui.getValue(ui.DEFAULT_TAB_ICON);

        //---BEGIN Module Specific Code
        // This section will be used to add the tabs to the Tabbed Pane.
        // Filter
        //tab0
        batchRoutesFilterPanel = new BatchRoutesFilterPanel(mainPanel);
        addTab(PolarisUI.getMessage("CF_FILTER"), tabIcon, batchRoutesFilterPanel,
               PolarisUI.getMessage("CF_FILTER_PANEL", PolarisUI.getMessage("BATCH_ROUTE")));

        //tab1
        batchRoutesMaintenancePanel = new BatchRoutesMaintenancePanel(mainPanel);
        addTab(PolarisUI.getMessage("CF_MAINTENANCE"), tabIcon, new JScrollPane(batchRoutesMaintenancePanel),
               PolarisUI.getMessage("CF_MAINTENANCE_PANEL", PolarisUI.getMessage("BATCH_ROUTE")));

        //tab2
        batchRoutesSegmentsPanel = new BatchRoutesSegmentsPanel(mainPanel);
        addTab(PolarisUI.getMessage("CF_SEGMENTS"), tabIcon, new JScrollPane(batchRoutesSegmentsPanel),
               PolarisUI.getMessage("CF_SEGMENTS_PANEL", PolarisUI.getMessage("BATCH_ROUTE")));

        //tab3
        batchRoutesMetersPanel = new BatchRoutesMetersPanel(mainPanel);
        addTab(PolarisUI.getMessage("CF_METERS"), tabIcon, new JScrollPane(batchRoutesMetersPanel),
               PolarisUI.getMessage("CF_METERS_PANEL", PolarisUI.getMessage("BATCH_ROUTE")));

        //tab4
        batchRoutesManifoldsPanel = new BatchRoutesManifoldsPanel(mainPanel);
        addTab(PolarisUI.getMessage("CF_MANIFOLDS"), tabIcon, new JScrollPane(batchRoutesManifoldsPanel),
               PolarisUI.getMessage("CF_MANIFOLDS_PANEL", PolarisUI.getMessage("BATCH_ROUTE")));

        //tab5
        batchRoutesTanksPanel = new BatchRoutesTanksPanel(mainPanel);
        addTab(PolarisUI.getMessage("CF_TANKS"), tabIcon, new JScrollPane(batchRoutesTanksPanel),
               PolarisUI.getMessage("CF_TANKS_PANEL", PolarisUI.getMessage("BATCH_ROUTE")));

        //tab6
        batchRoutesGradesPanel = new BatchRoutesGradesPanel(mainPanel);
        addTab(PolarisUI.getMessage("CF_GRADES"), tabIcon, new JScrollPane(batchRoutesGradesPanel),
               PolarisUI.getMessage("CF_GRADES_PANEL", PolarisUI.getMessage("BATCH_ROUTE")));

        //tab7
        batchRoutesAggMovementsPanel = new BatchRoutesAggMovementsPanel(mainPanel);
        addTab(PolarisUI.getMessage("CF_AGG_MOVEMENTS"), tabIcon, new JScrollPane(batchRoutesAggMovementsPanel),
               PolarisUI.getMessage("CF_AGG_MOVEMENTS_PANEL", PolarisUI.getMessage("BATCH_ROUTE")));
        //---END Module Specific Code

        setTabPlacement(JTabbedPane.TOP);

        // Setup listeners/actions
        addChangeListener(this);


    }




}
