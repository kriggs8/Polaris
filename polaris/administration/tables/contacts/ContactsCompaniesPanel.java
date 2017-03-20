package polaris.administration.tables.contacts;

import polaris.administration.tables.*;
import polaris.util.ValidatedTextField;
import polaris.util.VisiTextField;

import java.awt.event.ActionEvent;
import java.awt.event.TextEvent;

/**
 * @Author TCI - Krista Riggs
 * Created 2/2/2017.
 */
public class ContactsCompaniesPanel extends GenericTabPanel
{
    //Instantiate variables
    private ContactPanel contactPanel;

    /**
     * Instantiate the Companies Panel
     *
     * @param mainPanel
     * @throws Exception
     */

    //main method
    public ContactsCompaniesPanel(MainPanel mainPanel) throws Exception
    {
        super(mainPanel);
    }

    /**
     * The following non-implemented methods are declared
     * as they need to be overridden from the GenericTabPanel
     */
    @Override
    public String[] getColumnNames()
    {
        //no table to display on panel
        return new String[0];
    }

    @Override
    public String[] getTableNames()
    {
        return new String[]{"COMPANY_CONF"};
    }

    @Override
    public String[] getForeignKeyConstColNames()
    {
        return new String[]{"COMPANY_ID"};
    }

    @Override
    public String[][] getSelectColNames()
    {
        //SelectColName not required for this panel
        return new String[][]{{}, {}};
    }

    @Override
    public String getOrderBy()
    {
        //not required on this panel
        return null;
    }

    /**
     *
     */
    @Override
    public void createPanel()
    {
        //populate the table's column definitions
        tabCRS = new ColumnRecordSet(tableNames, selectColNames, foreignKeyConstColNames);
        /**
         * Create all of the components for the panel
         * Do not need validatedtextfield as this tab is not editable
         */
            contactPanel = new ContactPanel(this, tabCRS, false);
            contactPanel.displayNamePanel();
            contactPanel.displayAddressPanel();
            contactPanel.displayPhoneNumberPanel();
            contactPanel.displayMiscPanel();

        addToThisPanelAutoLayout(1, contactPanel);

        //sets the tableModel class name for each data entry component.
        //since all the components in this panel are associated to one tableModel
        //call the ComponentFactory setTableModelClassName.  This method is used only if you
        //have one tableModel in a panel.
        ComponentFactory.setTableModelClassName(this, tableModel.getClass().getName());
    }

    /**
     * The Companies Tab will be populated when a user selects a row in the Main Table
     * @param activeView
     */
    public void populateDataEntryFields(boolean activeView)
    {
        /**
         * makes sure to remove listeners related to the components in the panel when populating
         * the data entry fields to prevent actions from being triggered while data is being populated
         */
        ComponentFactory.removeListeners(this);

        CompanyRecordSet companyRecordSet = getCompanyRecordSet(activeView);
        if (companyRecordSet!=null && companyRecordSet.size()> 0 )
            ResultRecordSet.populateDataEntryFields(this,activeView,(TableRow)companyRecordSet.elementAt(0));
        else
        {
            TableRow emptyRow = ResultRecordSet.getEmptyRow(tabCRS);
            ResultRecordSet.populateDataEntryFields(this,activeView,emptyRow);
        }
        ComponentFactory.addListeners(this);
    }

    /**
     *populateDataEntryFields() requires a company recordset to be populated
     * @param activeView
     * @return
     */
    public CompanyRecordSet getCompanyRecordSet(boolean activeView)
    {

        TableRow tableRow = resultRS.getTableRowAtCursor();
        if (tableRow == null)
        {
            return null;
        }

        Field  field            = resultRS.getField(foreignKeyConstColNames[0], tableRow);

        if (field.getValue() != null && !field.getValue().isEmpty() && !field.getValue().equals(Configuration.newKey))
        {
            foreignKeyConstColValue = new Long(field.getValue());
        } else
        {
            foreignKeyConstColValue = -1;
        }

        StringBuffer where = new StringBuffer(256);
        where.append(" WHERE \n");
        where.append(" ID = " + foreignKeyConstColValue + "\n");

        //view active or inactive records.
        String inactiveIndicatorFlag = activeView ? "0" : "1";
        where.append("AND INACTIVE_INDICATOR_FLAG  = " + inactiveIndicatorFlag + "\n");

        //append order by on the where clause.
        if (orderBy != null)
        {
            where.append(orderBy);
        }

        CompanyRecordSet companyRS = new CompanyRecordSet(tabCRS);
        companyRS.readDB(where.toString());

        return companyRS;
    }

    @Override
    protected void setValidators()
    {

    }
}
