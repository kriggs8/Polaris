package polaris.administration.tables.batchRoutes;

import polaris.administration.tables.*;
import polaris.frame.PolarisUI;
import polaris.util.TableSorter;

import javax.swing.*;
import java.awt.*;


/**
 * Creates the Grades Tab Panel for a configuration screen.
 */
public class BatchRoutesGradesPanel extends GenericTabPanel
{


    public BatchRoutesGradesTableModel brGradesTableModel;


    /**
     * Grades Tab Panel.
     *
     * @param mainPanel
     * @throws Exception
     */
    public BatchRoutesGradesPanel(MainPanel mainPanel) throws Exception
    {
        super(mainPanel);

    }

    /**
     * gets a list of column names to display on the table.
     *
     * @return
     */
    @Override
    public String[] getColumnNames()
    {
        return new String[]{"EDIT_MODE", "GRADE_ID", "DEFAULT_BATCH_SIZE"};
    }

    /**
     * gets a list of columns that are VisiTableComboBox components.
     *
     * @return
     */
    public int[] getColTableComboBox()
    {
        return new int[]{1};
    }

    /**
     * gets a list of db tables to display on the table.
     *
     * @return
     */
    @Override
    public String[] getTableNames()
    {
        return new String[]{"BATCH_ROUTE_GRADE_XREF"};
    }

    /**
     * gets a list of foreign Key Col Names.  In this example it only has one db table in the list
     * therefore it should only have one foreign Key Col Name.
     *
     * @return
     */
    @Override
    public String[] getForeignKeyConstColNames()
    {
        return new String[]{"BATCH_ROUTE_ID"};

    }

    /**
     * gets a list of table columns to get table defintions.
     * in this instance it grabs all column names from the tables.
     *
     * @return
     */
    @Override
    public String[][] getSelectColNames()
    {

        return new String[][]{{}, {}};
    }

    /**
     * Gets the column name to sort by.
     *
     * @return
     */
    @Override
    public String getOrderBy()
    {
        return "";
    }

    /**
     * Creates the Panel and the controls/components in the panel.
     */
    @Override
    public void createPanel()
    {


        //populate the table's column definitions.
        tabCRS = new ColumnRecordSet(tableNames, selectColNames, foreignKeyConstColNames);
        //creates the tables TableModel
        brGradesTableModel = new BatchRoutesGradesTableModel(tabCRS, columnNames);

        //creates the TableColumnModel
        GenericTableColumnModel brGradeTableColumnModel = new GenericTableColumnModel(columnNames, tabCRS);
        // To support VisiTableComboBox control, make sure to set the setColTableComboBox
        // and getColTableComboBox column indexes.
        brGradeTableColumnModel.setColTableComboBox(getColTableComboBox());

        //creates the table.
        tabTable = new GenericTable(new TableSorter(brGradesTableModel));
        tabTable.setMainPanel(mainPanel);
        tabTable.setColumnModel(brGradeTableColumnModel);
        tabTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabTable.setFillsViewportHeight(true);
        tabTable.setIntercellSpacing(new Dimension(0, 0));
        //makes the Panel scrollable.
        JScrollPane tableScrollPanel = new JScrollPane(tabTable);
        tableScrollPanel.setPreferredSize(new Dimension(124, 124));

        //gets table's ResultRecordSet.
        tabRS = brGradesTableModel.getResultSet();

        //change listener for the table.  If users selects a different row
        //on the table grid. it will trigger this listener on the ResultRecordSet.
        ListSelectionModel listSelectionModel = (ListSelectionModel) tabTable.getSelectionModel();
        listSelectionModel.addListSelectionListener(tabRS);

        //listener for any changes to the table Model.
        brGradesTableModel.addTableModelListener(tabRS);

        //Associates the JTable to the ResultRecordSet.
        tabRS.setTable(tabTable);

        this.addToThisPanel(0, 0, 1, tableScrollPanel, true, true);
    }

    /**
     * set Help Topic Id
     */
    @Override
    public void setHelp()
    {
        this.mainPanel.setContextHelpTopicId("BatchRoutesConfigurationModule_GradesTab");
    }

    @Override
    protected void setValidators()
    {
        ColumnRecordSet crs = this.brGradesTableModel.getColumnRecordSet();

        // Loop through the columnNames and set the default validator
        for (int j = 0; j < columnNames.length; j++)
        {
            for (int i = 0; i < crs.size(); i++)
            {
                // Validate defaults
                Column column = crs.elementAt(i);

                if (column.columnName.equals(columnNames[j]))
                {
                    // Set the default validator
                    column.setValidator(defaultValidator(column));

                    // Found this columnName, break and search for next one
                    break;
                }
            }
        }
    }




    /**
     * TableModelClass for the table in the tab
     */
    public class BatchRoutesGradesTableModel extends GenericTableModel
    {
        public BatchRoutesGradesTableModel(ColumnRecordSet crs, String[] names)
        {
            super(crs, names, false, PolarisUI.getMessage("CF_GRADES_PANEL", PolarisUI.getMessage("BATCH_ROUTE")));
        }


        public boolean isCellEditable(int row, int col)
        {
            boolean colEditable = super.isCellEditable(row, col);
            //checks if the main table is readonly.
            //if read only make sure all related tab fields and tables are not editable as well.
            boolean editable = resultRS.isReadOnly() || tabRS.isReadOnly() ? false : true && colEditable;

            return editable;
        }
    }
}


