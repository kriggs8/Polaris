package polaris.administration.tables.contacts;

import polaris.administration.tables.*;
import polaris.dates.NrGDate;
import polaris.db.ConnectionPool;
import polaris.db.DBConn;
import polaris.frame.PolarisAction;
import polaris.frame.PolarisUI;
import polaris.ticket.shared.VisiMessage;
import polaris.util.BigDecimalTextField;
import polaris.util.VisiCheckbox;
import polaris.util.VisiComboBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @Author TCI - Krista Riggs
 * Created 2/2/2017.
 * <p>
 * Updated :
 * 3/2/17 - TCI-KRiggs - Added Null check on the Last and First fields to avoid error when editing a record and leaving
 * one or the other blank
 * 3/6/17 - TCI-KRiggs - Added ID field to Similar Records query so that the contact being edited is not returned
 * as a similar record.
 */
public class ContactsMaintenancePanel extends GenericTabPanel implements ActionListener, TextListener
{

    //Instantiate variables
    private JButton           clearButton;
    private JButton           companyButton;
    private JPanel            buttonPanel;
    private ClearAction       clearAction;
    private CopyCompanyAction copyCompanyAction;
    private ContactPanel      contactPanel;

    /**
     * Instantiate the Maintenance Panel
     *
     * @param mainPanel
     * @throws Exception
     */
    public ContactsMaintenancePanel(MainPanel mainPanel) throws Exception
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
        //no table to display on panel
        return new String[0];
    }

    @Override
    public String[] getForeignKeyConstColNames()
    {
        //no table to display on panel
        return new String[0];
    }

    @Override
    public String[][] getSelectColNames()
    {
        //no table to display on panel
        return new String[0][];
    }

    @Override
    public String getOrderBy()
    {
        //no table to display on panel
        return null;
    }

    /**
     * Create the panel for Maintenance Tab
     */
    @Override
    public void createPanel()
    {
        contactPanel = new ContactPanel(this, tableModel, true);
        contactPanel.displayNamePanel();
        contactPanel.displayAddressPanel();
        contactPanel.displayPhoneNumberPanel();
        contactPanel.displayMiscPanel();

        addToThisPanelAutoLayout(1, contactPanel);

        buttonPanel = new JPanel(new FlowLayout());
        clearButton = new JButton(PolarisUI.getMessage("CLEAR"));
        clearButton.setToolTipText(PolarisUI.getMessage("CLEAR"));
        companyButton = new JButton(PolarisUI.getMessage("CF_COPY_COMPANY"));
        companyButton.setToolTipText(PolarisUI.getMessage("CF_COPY_COMPANY"));
        buttonPanel.add(clearButton);
        buttonPanel.add(companyButton);

        this.c.gridx = 0;
        this.c.gridy = 100;//buttonPanelYCoord
        this.c.gridwidth = GridBagConstraints.REMAINDER;
        this.c.fill = GridBagConstraints.HORIZONTAL;
        this.c.anchor = GridBagConstraints.CENTER;
        this.addToThisPanel(buttonPanel);


        clearAction = new ClearAction();
        clearButton.addActionListener(clearAction);

        copyCompanyAction = new CopyCompanyAction();
        companyButton.addActionListener(copyCompanyAction);

        //sets the tableModel class name for each data entry component.
        //since all the components in this panel are associated to one tableModel
        //call the ComponentFactory setTableModelClassName.  This method is used only if you
        //have one tableModel in a panel.
        ComponentFactory.setTableModelClassName(this, tableModel.getClass().getName());
    }

    /**
     * Enable/Disable the the clear and company buttons bases on the Active/Inactive view status
     * @param activeView
     */
    @Override
    public void updatePanel (boolean activeView)
    {
        super.updatePanel(activeView);

        //Enable/Disable buttons based on view
        this.clearButton.setEnabled(!this.resultRS.isReadOnly());
        this.companyButton.setEnabled(!this.resultRS.isReadOnly());
    }

    /**
     * This method is triggered when the components in the maintenance panel
     * have an action being done on it
     * <p>
     * This also sets the field to updated and the table row object to updated.
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e)
    {
        //get tablerow object from the current selected row.
        TableRow tableRow = resultRS.getTableRowAtCursor();
        //makes sure all required data is valid and valuechanged is not ignored
        //before it processes the action.
        if (resultRS.isEmpty() || tableRow == null || resultRS.ignoreValueChanged)
        {
            //no data in the main grid and tableRow is not valid
            return;
        }

        //set the update flag to true on the tablerow.field
        //and also on the tablerow
        resultRS.setUpdated(e.getSource(), tableRow);
        //sets the dirty flag to panel to true.

        setDirty(true);
    }

    /**
     * Set the Validators as Company_id and Description (also checks for similar records)
     */
    @Override
    protected void setValidators()
    {
        //sets the validator for a column in the ColumnRecordSet.
        //since this tab does not have a table in the tab, do nothing in this method
        for (int i = 0; i < this.mainPanel.mainTableCrs.size(); i++)
        {
            Column column = this.mainPanel.mainTableCrs.elementAt(i);
            if (column.columnName.equals("COMPANY_ID"))
            {
                column.setValidator(defaultValidator(column));
            }
            if (column.columnName.equals("DESCRIPTION"))
            {
                column.setValidator(similarRecordValidator(column));
            }
        }
    }

    /**
     * Sets all of the fields to their default values when the clear button is selected.
     * Will not set the fields in the NamePanel to default
     *
     * @param panel - the default values for the panel
     */
    public void setDefaultValues(JPanel panel)
    {
        Component[] pc = panel.getComponents();

        for (int i = 0; i < pc.length; i++)
        {
            String pcName = (pc[i].getName() == null) ? "" : pc[i].getName().substring(1, pc[i].getName().length());
            if (pcName.equals("TITLE_ID") || pcName.equals("LAST_NAME") || pcName.equals("FIRST_NAME") || pcName.equals(
                    "DESCRIPTION") || pcName.equals("COMPANY_ID") || pcName.equals("SCHEDULER_TYPE_ID"))
            {
                continue;
            }
            else
            {
                if (pc[i] instanceof VisiCheckbox)
                {
                    ((VisiCheckbox) pc[i]).setSelected(false);
                }
                if (pc[i] instanceof VisiComboBox)
                {
                    //if it has a special item
                    //display the special item (All, NOne)
                    if (((VisiComboBox) pc[i]).hasSpecialItems())
                    {
                        ((VisiComboBox) pc[i]).selectFirstSpecialItem();
                    }
                    else
                    {
                        if (pc[i].getName().equals(Configuration.inactiveIndicatorDBName))
                        {
                            ((VisiComboBox) pc[i]).setSelectedItem(0);
                        }
                        else
                        {
                            ((VisiComboBox) pc[i]).clearSelection();
                        }
                    }
                }
                if (pc[i] instanceof NrGDate)
                {
                    ((NrGDate) pc[i]).setValue("");
                }

                if (pc[i] instanceof JTextField)
                {
                    if (pc[i] instanceof BigDecimalTextField)
                    {
                        ((BigDecimalTextField) pc[i]).setRealValue(0, false);
                    }
                    else
                    {
                        ((JTextField) pc[i]).setText("");
                    }
                }

                // Check if pc is a JPanel
                if (pc[i] instanceof JPanel && !(pc[i] instanceof NrGDate))
                {
                    // Populate the children
                    setDefaultValues((JPanel) pc[i]);
                }
            }
        }
    }

    /**
     * Validates similar records found when saving and edited or new record
     *
     * @param column
     * @return - the validator
     */
    private ColumnValidator similarRecordValidator(Column column)
    {
        ColumnValidator validator = new ColumnValidator(column)
        {
            String errorMessage = "";

            public boolean validate(String text, String id) throws SQLException
            {
                // Return false as this method should not be called in this scenario.
                return false;
            }

            public boolean validate(Object value, String id) throws SQLException
            {
                // Check if empty string
                if (value == null || value.toString().isEmpty())
                {
                    errorMessage = PolarisUI.getMessage("CF_VALIDATION_SAVE_ERROR",
                                                        mainPanel.getTableModelFocus().getPanelName()) + "\n";

                    errorMessage += PolarisUI.getMessage("CF_VALIDATION_NULL", column.columnName);

                    return false;
                }

                //get tablerow object from the current selected row.
                TableRow tableRow = resultRS.getTableRowAtCursor();

                //make sure the tableRow is not null
                if (tableRow == null)
                {
                    //no data in the main grid and tableRow is not valid
                    return false;
                }

                // Get the first name, last name, and description from the result record set.
                Field firstNameField   = ResultRecordSet.getField("FIRST_NAME", tableRow);
                Field lastNameField    = ResultRecordSet.getField("LAST_NAME", tableRow);
                Field descriptionField = ResultRecordSet.getField("DESCRIPTION", tableRow);
                Field idField          = ResultRecordSet.getField("ID", tableRow);


                // Call getSimilarRecords to build a string of other records that are similar to the one being created.
                // TCI-KR - added check for nulls as it causes an exception. Convert null to emptyString
                String similarContacts = getSimilarRecords(descriptionField.getValue().toString(),
                                                           ((firstNameField.getValue() == null) ? "" : firstNameField
                                                                   .getValue().toString()),
                                                           ((lastNameField.getValue() == null) ? "" : lastNameField
                                                                   .getValue().toString()),
                                                           ((idField.getValue().toString().equals(Configuration.newKey) || tableRow.isInserted()) ? "0" : idField
                                                                   .getValue().toString()));
                if (similarContacts.isEmpty())
                {
                    // If string is empty, then no similar records, return true.
                    return true;
                }

                //Similar contacts exist.
                //ask the user if they wish to proceed with the new contact.
                //if user selects yes, the Contact will be created
                //if user selects no, the save will cancel you the user is allowed to make changes.
                Object[] options   = {"Yes", "No"};
                int      selection = 0;
                selection = VisiMessage.warningOverride(
                        PolarisUI.getMessage("CF_VALIDATION_SIMILAR_CONTACT", similarContacts), options);
                if (selection == 1)
                {
                    errorMessage = "";
                    return true;
                }
                else
                {
                    errorMessage = PolarisUI.getMessage("CF_VALIDATION_SAVE_CANCELLED", "contact");
                    return false;
                }
            }

            public String getErrorMessage(Object value)
            {
                return errorMessage;
            }
        };
        return validator;
    }

    /**
     * Gets a String list of similar records based on description, first name, and last name values.
     *
     * @param description
     * @param firstName
     * @param lastName
     * @return - The string value of the similar records found
     * @throws SQLException
     */
    private String getSimilarRecords(String description, String firstName, String lastName, String id)
            throws SQLException
    {
        StringBuilder result = new StringBuilder();

        DBConn    dbconn = ConnectionPool.getConnection();
        Statement stmt   = null;
        try
        {
            stmt = dbconn.connection.createStatement();
            String query = getQuery(QueryType.SIMILAR_RECORDS, description.toUpperCase(), firstName.toUpperCase(),
                                    lastName.toUpperCase(), id);
            ResultSet rs = stmt.executeQuery(query);

            int count = 0;
            while (rs.next())
            {
                count++;
                if (count <= 10)
                {
                    String descriptionValue = rs.getString(1);
                    result.append(descriptionValue);
                    result.append('\n');
                }
            }

            if (count > 10)
            {
                result.append("...\nTop 10 shown out of " + count + " contacts.\n");
            }
            rs.close();
        }
        catch (SQLException ex)
        {
            throw ex;
        }
        finally
        {
            if (stmt != null)
            {
                stmt.close();
            }
            ConnectionPool.releaseConnection(dbconn);
        }
        return result.toString();
    }


    /**
     * triggered when the following occurs:
     * validatedtextfield has lost focus and field value has changed.
     * <p>
     * If this method is triggered, it sets the field to updated and the tablerow object
     * to updated.
     *
     * @param e
     */
    public void textValueChanged(TextEvent e)
    {
        //get tablerow object at cursor.
        TableRow tableRow = resultRS.getTableRowAtCursor();

        //no data in the main grid
        if (resultRS.isEmpty() || tableRow == null)
        {
            return;
        }

        //set the update flag to true on the tablerow.field
        //and also on the tablerow
        resultRS.setUpdated(e.getSource(), tableRow);
        //sets the dirty flag to panel to true.
        setDirty(true);
    }

    /**
     * Copies the company address
     *
     * @param id - the company id
     * @return - the result
     * @throws SQLException
     */
    private boolean copyCompanyAddress(String id) throws SQLException
    {
        boolean   result = true;
        DBConn    dbconn = ConnectionPool.getConnection();
        Statement stmt   = null;
        try
        {
            stmt = dbconn.connection.createStatement();
            String    query = getQuery(QueryType.COPY_COMPANY_ADDRESS, id);
            ResultSet rs    = stmt.executeQuery(query);

            // If not empty, then sequence is already in use
            if (rs.next())
            {
                //get tablerow object from the current selected row.
                TableRow tableRow = resultRS.getTableRowAtCursor();
                //makes sure all required data is valid and valuechanged is not ignored
                //before it process the action.
                if (tableRow != null)
                {
                    Field addressLine1Field = ResultRecordSet.getField("ADDRESS_LINE_1", tableRow);
                    Field addressLine2Field = ResultRecordSet.getField("ADDRESS_LINE_2", tableRow);
                    Field addressLine3Field = ResultRecordSet.getField("ADDRESS_LINE_3", tableRow);
                    Field cityField         = ResultRecordSet.getField("CITY", tableRow);
                    Field stateField        = ResultRecordSet.getField("STATE_PROVINCE_ID", tableRow);
                    Field zipCodeField      = ResultRecordSet.getField("ZIP_CODE", tableRow);
                    Field workPhoneField    = ResultRecordSet.getField("WORK_PHONE", tableRow);
                    Field faxField          = ResultRecordSet.getField("FAX", tableRow);

                    addressLine1Field.setValue(rs.getString("ADDRESS_LINE_1"));
                    addressLine1Field.setUpdated(true);
                    addressLine2Field.setValue(rs.getString("ADDRESS_LINE_2"));
                    addressLine2Field.setUpdated(true);
                    addressLine3Field.setValue(rs.getString("ADDRESS_LINE_3"));
                    addressLine3Field.setUpdated(true);
                    cityField.setValue(rs.getString("CITY"));
                    cityField.setUpdated(true);
                    stateField.setLvalue(rs.getLong("STATE_PROVINCE_ID"));
                    stateField.setUpdated(true);
                    zipCodeField.setValue(rs.getString("ZIP_CODE"));
                    zipCodeField.setUpdated(true);
                    workPhoneField.setValue(rs.getString("WORK_PHONE"));
                    workPhoneField.setUpdated(true);
                    faxField.setValue(rs.getString("FAX"));
                    faxField.setUpdated(true);
                    tableRow.setUpdated();

                    this.populateDataEntryFields(true);
                }
            }
            rs.close();
        }
        catch (SQLException ex)
        {
            throw ex;
        }
        finally
        {
            if (stmt != null)
            {
                stmt.close();
            }
            ConnectionPool.releaseConnection(dbconn);
        }
        return result;
    }

    /**
     * This method has the queries to run for copyCompanyAddress and to find similarRecords
     *
     * @param queryType       - the type of query to run
     * @param queryParameters - the parameters for the query
     * @return - the query
     */
    private String getQuery(QueryType queryType, String... queryParameters)
    {
        String query = "";

        switch (queryType)
        {
            case COPY_COMPANY_ADDRESS:
                query = "SELECT \n" + " ADDRESS_LINE_1,\n" + " ADDRESS_LINE_2,\n" + " ADDRESS_LINE_3, \n" + " CITY,\n" +
                        " STATE_PROVINCE_ID,\n" + " ZIP_CODE,\n" + " USER_NOTE,\n" + " WORK_PHONE,\n" + " FAX\n" +
                        "FROM \n" + " POLARIS.COMPANY_CONF \n" + "WHERE ID = " + queryParameters[0] + "\n";
                break;
            case SIMILAR_RECORDS:
                // Pass the DESCRIPTION as param0, FIRST_NAME as param1, LAST_NAME as param2, and ID as param3
                // ID param was added to eliminate the return of the record being edited as a similar record
                query = "SELECT DESCRIPTION FROM (\n" + "SELECT DESCRIPTION FROM POLARIS.CONTACT_CONF C\n" +
                        "WHERE UPPER(C.DESCRIPTION) LIKE '%" + queryParameters[0].toUpperCase() + "%'\n" +
                        "AND C.ID <> " + queryParameters[3] + "\n" + "UNION\n" +
                        "SELECT DESCRIPTION FROM POLARIS.CONTACT_CONF C\n" + "WHERE (UPPER(C.FIRST_NAME) LIKE '%" +
                        queryParameters[1].toUpperCase() + "%'\n" + "AND UPPER(C.LAST_NAME) LIKE '%" +
                        queryParameters[2].toUpperCase() + "%'\n" + "AND C.ID <> " +
                        queryParameters[3] + ")\n" + "OR UPPER(C.LAST_NAME) LIKE '%" +
                        queryParameters[2].toUpperCase() + "%'\n" + "AND C.ID <> " +
                        queryParameters[3] + "\n" + ") ORDER BY DESCRIPTION ASC\n";
                break;
        }
        return query;
    }

    /**
     * the enum for the Query Type
     */
    private enum QueryType
    {
        COPY_COMPANY_ADDRESS,
        SIMILAR_RECORDS
    }

    /**
     * The class to run when the clear button is selected
     */
    class ClearAction extends PolarisAction
    {
        public ClearAction()
        {
            super(PolarisUI.getMessage("CF_COMPANIES"), PolarisUI.getMessage("CLEAR"),
                  PolarisUI.getMessage("CLEAR_ICON"), PolarisUI.getMessage("CLEAR_PANEL"), null,
                  KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_MASK), true);
        }

        public void actionPerformed(ActionEvent event)
        {
            setDefaultValues(contactPanel);
        }
    }

    /**
     * The class to run when the copy company button is selected
     */
    class CopyCompanyAction extends PolarisAction
    {
        public CopyCompanyAction()
        {
            super(PolarisUI.getMessage("CF_COMPANIES"), PolarisUI.getMessage("COPY"), PolarisUI.getMessage("COPY_ICON"),
                  PolarisUI.getMessage("COPY_COMPANY"), null, KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_MASK),
                  true);
        }

        public void actionPerformed(ActionEvent event)
        {
            //get tablerow object from the current selected row.
            TableRow tableRow = resultRS.getTableRowAtCursor();
            //makes sure all required data is valid and valuechanged is not ignored
            //before it process the action.
            if (resultRS.isEmpty() || tableRow == null)
            {
                //no data in the main grid and tableRow is not valid
                return;
            }

            Field  company   = ResultRecordSet.getField("COMPANY_ID", tableRow);
            Field  contact   = ResultRecordSet.getField("ID", tableRow);
            String companyId = company.getValue() == null ? "" : company.getValue();
            String contactId = contact.getValue() == null ? "" : contact.getValue();

            if (companyId.isEmpty() || contactId.isEmpty())
            {
                JOptionPane.showMessageDialog(KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),
                                              PolarisUI.getMessage("CF_COPY_COMPANY_NO_ID"));
                return;
            }
            Object[] options   = {"Yes", "No"};
            int      selection = 0;
            selection = VisiMessage.warningOverride(PolarisUI.getMessage("CF_COPY_COMPANY_ADDRESS"), options);

            if (selection == 1)
            {
                try
                {
                    copyCompanyAddress(companyId);
                }
                catch (SQLException sqlex)
                {
                    JOptionPane.showMessageDialog(null, sqlex.getMessage(), "SQL Error:" + sqlex.getErrorCode(),
                                                  JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}

