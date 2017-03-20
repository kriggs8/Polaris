package polaris.administration.tables.movements;

import polaris.administration.tables.*;
import polaris.constants.SpecialItems;
import polaris.frame.PolarisUI;
import polaris.util.TableSorter;
import polaris.util.VisiComboBox;
import polaris.util.VisiGridBagPanel;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by  TCI-Waleed Elsaid on 2/22/2017.
 */
public class MovementsInventoriesPanel extends GenericTabPanel implements ActionListener {

    private MovementsInventoriesTableModel mvInventoriesTableModel;
    private VisiComboBox defaultRecInvLocationCombo;
    private VisiComboBox defaultDelInvLocationCombo;

    /**
     * Created by  TCI-Waleed Elsaid on 2/22/2017
     * Instanciates the MovementsInventoriesPanel Panel.
     *
     * @param mainPanel
     * @throws Exception
     */
    public MovementsInventoriesPanel(MainPanel mainPanel) throws Exception
    {
        super(mainPanel);
    }

    /**
     * Created by  TCI-Waleed Elsaid on 2/22/2017
     * @return
     */
    @Override
    public String[] getColumnNames()
    {
        return new String[]{"EDIT_MODE", "REC_DEL_ID", "LOCATION_ID"};
    }

    /**
     * Created by  TCI-Waleed Elsaid on 2/22/2017
     * @return
     */
    @Override
    public String[] getTableNames()
    {
        return new String[]{"MOVEMENT_INV_LOCATION_XREF"};
    }

    /**
     * Created by  TCI-Waleed Elsaid on 2/22/2017
     * @return
     */
    @Override
    public String[] getForeignKeyConstColNames()
    {
        return new String[]{"MOVEMENT_ID"};
    }

    /**
     * Created by  TCI-Waleed Elsaid on 2/22/2017
     * @return
     */
    @Override
    public String[][] getSelectColNames()
    {
        return new String[][]{{}, {}};
    }

    @Override
    public String getOrderBy()
    {
        return "ORDER BY REC_DEL_ID";
    }

    /**
     * Created by  TCI-Waleed Elsaid on 2/22/2017
     * Method called by the super constructor
     * @return
     */
    @Override
    public void createPanel()
    {
        //populate the table's column definitions.
        tabCRS = new ColumnRecordSet(tableNames, selectColNames, foreignKeyConstColNames);
        //creates the tables TableModel
        mvInventoriesTableModel = new MovementsInventoriesPanel.MovementsInventoriesTableModel(tabCRS, columnNames);
        //creates the TableColumnModel
        GenericTableColumnModel mvInventoriesColumnModel = new GenericTableColumnModel(columnNames, tabCRS);

        //creates the table.
        tabTable = new GenericTable(new TableSorter(mvInventoriesTableModel));
        tabTable.setMainPanel(mainPanel);
        tabTable.setColumnModel(mvInventoriesColumnModel);
        tabTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabTable.setFillsViewportHeight(true);
        tabTable.setIntercellSpacing(new Dimension(0, 0));

        //makes the Panel scrollable.
        JScrollPane tableScrollPanel = new JScrollPane(tabTable);
        tableScrollPanel.setPreferredSize(new Dimension(300, 100));
        tableScrollPanel.setBorder(
                BorderFactory.createTitledBorder(new LineBorder(Color.GRAY, 1, true), PolarisUI.getMessage("CF_INV_LOCATIONS")));

        //gets table's ResultRecordSet
        tabRS = mvInventoriesTableModel.getResultSet();

        //change listener for the main table.  If users selects a different row
        //on the table grid. it will trigger this listener on the ResultRecordSet.
        ListSelectionModel listSelectionModel = (ListSelectionModel) tabTable.getSelectionModel();
        listSelectionModel.addListSelectionListener(tabRS);

        //listener for any changes to the table Model.
        mvInventoriesTableModel.addTableModelListener(tabRS);

        //Associates the JTable to the ResultRecordSet.
        tabRS.setTable(tabTable);


        defaultRecInvLocationCombo = ComponentFactory.getLocationComboBox("", Configuration.defaultRecInvLocationDBName,
                SpecialItems.NONE);
        defaultDelInvLocationCombo = ComponentFactory.getLocationComboBox("", Configuration.defaultDelInvLocationDBName,
                SpecialItems.NONE);


        //sets the tableModel class name for each data entry component.
        //these components are associated to the main table's table model.
        defaultRecInvLocationCombo.setTableModelClassName(tableModel.getClass().getName());
        defaultDelInvLocationCombo.setTableModelClassName(tableModel.getClass().getName());

        VisiGridBagPanel panelRight = new VisiGridBagPanel();
        panelRight.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY, 1, true),
                PolarisUI.getMessage("CF_DEFAULT") + " " + PolarisUI.getMessage("CF_INV_LOCATIONS")));
        panelRight.addToThisPanelAutoLayout(1, "CF_RECEIPT", defaultRecInvLocationCombo);
        panelRight.addToThisPanelAutoLayout(1, "CF_DELIVERY", defaultDelInvLocationCombo);

        this.resetGridBag();

        this.addToThisPanel(0, 0, 1, 1, tableScrollPanel, true, true);
        this.addToThisPanel(1, 0, 1, 1, panelRight, true, true);
    }

    @Override
    /**
     * Created by  TCI-Waleed Elsaid on 2/22/2017
     * This method sets the validators to be called at the save action
     */
    protected void setValidators()
    {
        //sets the validator for a column in the ColumnRecordSet.
        for (int i = 0; i < tabCRS.size(); i++)
        {
            Column column = tabCRS.elementAt(i);
            if (    column.columnName.equals("REC_DEL_ID") ||
                    column.columnName.equals("LOCATION_ID"))
            {
                column.setValidator(defaultValidator(column));
            }
        }
    }


    /*
     * Created by  TCI-Waleed Elsaid on 2/22/2017
     * the tab table model
     */
    public class MovementsInventoriesTableModel extends GenericTableModel
    {
        public MovementsInventoriesTableModel(ColumnRecordSet crs, String[] names)
        {
            super(crs, names, false, PolarisUI.getMessage("CF_INVENTORIES_PANEL", PolarisUI.getMessage("MOVEMENT")));
        }

        public boolean isCellEditable(int row, int col)
        {
            boolean colEditable = super.isCellEditable(row,col);
            //checks if the main table is readonly.
            //if read only make sure all related tab fields and tables are not editable as well.
            boolean editable = resultRS.isReadOnly() || tabRS.isReadOnly() ? false : true&&colEditable;


            return editable;
        }
    }


    /**
     * method is triggered when components (comboboxes) in the  panel has an action being done on it
     * actions on comboboxes will set the field to updated and the tablerow object to upated.
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

        //get tablerow object from the current selected row.
        TableRow tableRow = resultRS.getTableRowAtCursor();

        if (resultRS.isEmpty() || tableRow == null)
        {
            //no data in the main grid and tableRow is not valid
            return;
        }

        ui.setBusyCursor();

        String action = e.getActionCommand();

        if (e.getSource() instanceof VisiComboBox)
        {
            //set the update flag to true on the tablerow.field
            //and also on the tablerow
            resultRS.setUpdated(e.getSource(), tableRow);
            //sets the dirty flag to panel to true.
            setDirty(true);
        }

        ui.setDefaultCursor();
    }


}
