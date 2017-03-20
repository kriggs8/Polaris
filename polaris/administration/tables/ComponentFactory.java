package polaris.administration.tables;


import polaris.constants.SpecialItems;
import polaris.dates.NrGDate;
import polaris.frame.PolarisUI;
import polaris.modelFactory.FileFormatTypeModelFactory;
import polaris.modelFactory.GradeModelFactory;
import polaris.util.*;
import polaris.util.table.TableModelClassName;
import polaris.util.tableCombobox.TableComboBoxModel;
import polaris.util.tableCombobox.VisiTableComboBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.TextListener;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by CE7 on 4/29/2016.
 */
public class ComponentFactory
{
    protected static String displayValue = "NAME || ' - ' || DESCRIPTION";


    public ComponentFactory()
    {

    }

    @Deprecated
    public static JLabel getUpdateUserLabel()
    {

        return new JLabel(PolarisUI.getMessage("UPDATE_USER"), SwingConstants.RIGHT);
    }

    @Deprecated
    public static JLabel getUserUpdateDateLabel()
    {

        return new JLabel(PolarisUI.getMessage("USER_UPDATE_DATE"), SwingConstants.RIGHT);
    }

    @Deprecated
    public static JLabel getOSAreaLabel()
    {

        return new JLabel(PolarisUI.getMessage("OS_AREA"), SwingConstants.RIGHT);
    }

    @Deprecated
    public static JLabel getAliasLabel()
    {

        return new JLabel(PolarisUI.getMessage("ALIAS"), SwingConstants.RIGHT);
    }

    @Deprecated
    public static JLabel getNameLabel()
    {

        return new JLabel(PolarisUI.getMessage("NAME"), SwingConstants.RIGHT);
    }

    @Deprecated
    public static JLabel getRecSchedLabel()
    {

        return new JLabel(PolarisUI.getMessage("CF_RD_SCHED_NOTE", PolarisUI.getMessage("CF_REC")),
                          SwingConstants.RIGHT);
    }

    @Deprecated
    public static JLabel getDelSchedLabel()
    {

        return new JLabel(PolarisUI.getMessage("CF_RD_SCHED_NOTE", PolarisUI.getMessage("CF_DEL")),
                          SwingConstants.RIGHT);
    }

    @Deprecated
    public static JLabel getPhysDescLabel()
    {

        return new JLabel(PolarisUI.getMessage("CF_PHYS_DESC"), SwingConstants.RIGHT);
    }

    @Deprecated
    public static JLabel getAvgTransDayLabel()
    {

        return new JLabel(PolarisUI.getMessage("CF_AVG_TRANS_DAYS"), SwingConstants.RIGHT);
    }

    @Deprecated
    public static JLabel getDefBatchVolumeLabel()
    {

        return new JLabel(PolarisUI.getMessage("CF_DEF_BATCH_VOLUME"), SwingConstants.RIGHT);
    }

    @Deprecated
    public static JLabel getDescriptionLabel()
    {

        return new JLabel(PolarisUI.getMessage("DESCRIPTION"), SwingConstants.RIGHT);
    }

    @Deprecated
    public static JLabel getScheduleSystemLabel()
    {

        return new JLabel(PolarisUI.getMessage("SCHED_SYSTEM"), SwingConstants.RIGHT);
    }

    @Deprecated
    public static JLabel getSystemLabel()
    {

        return new JLabel(PolarisUI.getMessage("SYSTEM"), SwingConstants.RIGHT);
    }

    @Deprecated
    public static JLabel getBatchRouteLabel()
    {

        return new JLabel(PolarisUI.getMessage("BATCH_ROUTE"), SwingConstants.RIGHT);
    }

    @Deprecated
    public static JLabel getRecLocationLabel()
    {

        return new JLabel(PolarisUI.getMessage("REC_LOCATION"), SwingConstants.RIGHT);
    }

    @Deprecated
    public static JLabel getDelLocationLabel()
    {

        return new JLabel(PolarisUI.getMessage("DEL_LOCATION"), SwingConstants.RIGHT);
    }

    @Deprecated
    public static JLabel getRecConCarrierLabel()
    {

        return new JLabel(PolarisUI.getMessage("REC_CON_CARRIER"), SwingConstants.RIGHT);
    }

    @Deprecated
    public static JLabel getDelConCarrierLabel()
    {

        return new JLabel(PolarisUI.getMessage("DEL_CON_CARRIER"), SwingConstants.RIGHT);
    }

    @Deprecated
    public static JLabel getInactiveLabel()
    {

        return new JLabel(PolarisUI.getMessage("INACTIVE_INDICATOR_FLAG"), SwingConstants.RIGHT);
    }

    /**
     * Add displayValue
     *
     * @param lookUpTable
     * @return
     */
    public static String getComboBoxDisplayValue(String lookUpTable)
    {
        String displayValue = ComponentFactory.displayValue;

        if (lookUpTable.equals("CONTACT_CONF"))
        {
            displayValue = "LAST_NAME || ', ' || FIRST_NAME || ' - ' || DESCRIPTION NAME";
        }

        return displayValue;
    }

    public static VisiComboBox getLocationComboBox(String where, String dbName, String specialValue)
    {
        String whereClause = where.isEmpty() ? null : where + ")";
        String groupBy     = null;
        VisiComboBox comboBox = new VisiComboBox(
                new DBIdNameFactory(Configuration.locationTableName, "ID", displayValue, whereClause, groupBy), dbName,
                specialValue);
        return comboBox;
    }

    public static VisiComboBox getMeterComboBox(String where, String dbName, String specialValue)
    {
        String whereClause = where.isEmpty() ? null : where + ")";
        String groupBy     = null;
        VisiComboBox comboBox = new VisiComboBox(
                new DBIdNameFactory(Configuration.meterTableName, "ID", displayValue, whereClause, groupBy), dbName,
                specialValue);
        return comboBox;
    }

    public static VisiComboBox getManifoldComboBox(String where, String dbName, String specialValue)
    {
        String whereClause = where.isEmpty() ? null : where + ")";
        String groupBy     = null;
        VisiComboBox comboBox = new VisiComboBox(
                new DBIdNameFactory(Configuration.manifoldTableName, "ID", displayValue, whereClause, groupBy), dbName,
                specialValue);
        return comboBox;
    }

    public static VisiComboBox getConCarrierComboBox(String where, String dbName, String specialValue)
    {
        String whereClause = where.isEmpty() ? null : where + ")";
        String groupBy     = null;

        VisiComboBox comboBox = new VisiComboBox(
                new DBIdNameFactory(Configuration.conCarrierTableName, "ID", displayValue, whereClause, groupBy),
                dbName, specialValue);
        return comboBox;
    }

    public static VisiComboBox getFileFormatComboBox(boolean editable, String dbName, String specialValue)
    {
        VisiComboBox comboBox = new VisiComboBox(new FileFormatTypeModelFactory(), dbName, specialValue);
        comboBox.setEnabled(editable);

        return comboBox;
    }

    public static VisiComboBox getInactiveStatusComboBox(String dbName)
    {

        VisiComboBox comboBox = new VisiComboBox(new InactiveStatusVector(), dbName);
        return comboBox;
    }

    public static VisiComboBox getScheduleSystemComboBox(String where, String dbName, String specialValue)
    {
        String whereClause = where.isEmpty() ? null : where + ")";
        String groupBy     = null;

        VisiComboBox comboBox = new VisiComboBox(
                new DBIdNameFactory(Configuration.scheduleSystemTableName, "ID", displayValue, whereClause, groupBy),
                dbName, specialValue);
        return comboBox;
    }

    public static VisiComboBox getSystemComboBox(String where, String dbName, String specialValue)
    {
        String whereClause = where.isEmpty() ? null : where + ")";
        String groupBy     = null;
        VisiComboBox comboBox = new VisiComboBox(
                new DBIdNameFactory(Configuration.systemTableName, "ID", displayValue, whereClause, groupBy), dbName,
                specialValue);
        return comboBox;
    }

    public static VisiComboBox getBatchRouteComboBox(String where, String dbName)
    {
        String whereClause = where.isEmpty() ? null : where + ")";
        String groupBy     = null;
        VisiComboBox comboBox = new VisiComboBox(
                new DBIdNameFactory(Configuration.batchRouteTableName, "ID", displayValue, whereClause, groupBy),
                dbName, SpecialItems.ALL);
        return comboBox;
    }

    public static VisiComboBox getOSAreaComboBox(String where, String dbName, String specialValue)
    {
        String whereClause = where.isEmpty() ? null : where + ")";
        String groupBy     = null;
        VisiComboBox comboBox = new VisiComboBox(
                new DBIdNameFactory(Configuration.osAreaTableName, "ID", displayValue, whereClause, groupBy), dbName,
                specialValue);
        return comboBox;
    }

    public static VisiComboBox getRevAllocMethodComboBox(String where, String dbName, String specialValue)
    {
        String whereClause = where.isEmpty() ? null : where + ")";
        String groupBy     = null;
        VisiComboBox comboBox = new VisiComboBox(
                new DBIdNameFactory(Configuration.revAllocMethodTableName, "ID", displayValue, whereClause, groupBy), dbName,
                specialValue);
        return comboBox;
    }

    public static VisiComboBox getBookInventoryComboBox(String where, String dbName, String specialValue)
    {
        String whereClause = where.isEmpty() ? null : where + ")";
        String groupBy     = null;
        VisiComboBox comboBox = new VisiComboBox(
                new DBIdNameFactory(Configuration.bookInvLocationTableName, "ID", displayValue, whereClause, groupBy), dbName,
                specialValue);
        return comboBox;
    }

    public static VisiComboBox getComboBox(boolean editable, String tableName, String where, String dbName,
                                           String specialValue)
    {
        String displayValue = getComboBoxDisplayValue(tableName);
        String whereClause  = where.isEmpty() ? null : where + ")";
        String groupBy      = null;
        VisiComboBox comboBox = new VisiComboBox(
                new DBIdNameFactory(tableName, "ID", displayValue, whereClause, groupBy), dbName, specialValue);
        comboBox.setEnabled(editable);
        return comboBox;
    }

    public static ValidatedTextField getTextField(boolean editable, String dbName, int max)
    {
        LengthTextField textField = new LengthTextField(0, max, 17);
        textField.setEditable(editable);
        textField.setName(dbName);
        textField.setShowFeedback(true);
        return textField;
    }

    public static ValidatedTextField getTextField(boolean editable, String dbName, int max,int col)
    {
        LengthTextField textField = new LengthTextField(0, max, col);
        textField.setEditable(editable);
        textField.setName(dbName);
        textField.setShowFeedback(true);
        return textField;
    }

    /**
     * Get a getBigDecimalTextField instance (Number based Text Field).
     *
     * @param editable True = editable, false = not editable.
     * @param dbName The table column name using in "(COLUMN_NAME" format.
     * @param cols The number of columns for this text field
     * @param scale The number of digits to the right of decimal point.
     * @param unscaledValueDigitLimit The number of digits to the left of decimal point.
     * @param roundingMode a BigDecimal.? value specifying the rounding mode for the control.
     * @param formatString a Formats.? or custom format string for how the number should be displayed.
     * @return
     */
    public static BigDecimalTextField getBigDecimalTextField(boolean editable, String dbName, int cols, int scale, int unscaledValueDigitLimit, int roundingMode, String formatString)
    {
        // Instantiate the object
        BigDecimalTextField textField = new BigDecimalTextField(cols);

        // Set the Scale (right of .) and rounding mode
        textField.setScale(scale, roundingMode);

        // Set the unscaled value (left of .)
        textField.setUnscaledValueDigitLimit(unscaledValueDigitLimit);

        // Set the format string and parser
        textField.setFormatter(new DecimalFormat(formatString));
        textField.addParser(new DecimalFormat(formatString));

        // Set whether the field is editable
        textField.setEditable(editable);

        // set the dbName
        textField.setName(dbName);

        // set whether to show feedback
        textField.setShowFeedback(true);

        return textField;
    }

    public static VisiComboBox getSystemGroupComboBox(String where, String dbName, String specialValue)
    {
        String whereClause = where.isEmpty() ? null : where + ")";
        String groupBy     = null;
        VisiComboBox comboBox = new VisiComboBox(
                new DBIdNameFactory(Configuration.systemGroupTableName, "ID", displayValue, whereClause, groupBy), dbName,
                specialValue);
        return comboBox;
    }

    public static VisiComboBox getLineSpaceComboBox(String where, String dbName, String specialValue)
    {
        String whereClause = where.isEmpty() ? null : where + ")";
        String groupBy     = null;
        VisiComboBox comboBox = new VisiComboBox(
                new DBIdNameFactory(Configuration.lineSpaceTableName, "ID", displayValue, whereClause, groupBy), dbName,
                specialValue);
        return comboBox;
    }

    public static VisiComboBox getGradeSpecComboBox(String where, String dbName, String specialValue)
    {
        String whereClause = where.isEmpty() ? null : where + ")";
        String groupBy     = null;
        VisiComboBox comboBox = new VisiComboBox(
                new DBIdNameFactory(Configuration.gradeSpecTableName, "ID", displayValue, whereClause, groupBy), dbName,
                specialValue);
        return comboBox;
    }

    public static VisiComboBox getMovementComboBox(String where, String dbName, String specialValue)
    {
        String whereClause = where.isEmpty() ? null : where + ")";
        String groupBy     = null;
        VisiComboBox comboBox = new VisiComboBox(
                new DBIdNameFactory(Configuration.movementTableName, "ID", displayValue, whereClause, groupBy), dbName,
                specialValue);
        return comboBox;
    }

    public static VisiTextField getUserTextField(boolean editable, String dbName)
    {
        VisiTextField textField = new VisiTextField(20, editable, new DBIdNameFactory(Configuration.userTableName, "ID",
                                                                                      displayValue));
        textField.setName(dbName);
        return textField;
    }


    public static VisiTextField getUserUpdateDateTextField(boolean editable, String dbName)
    {
        VisiTextField textField = new VisiTextField(20, editable);
        textField.setName(dbName);
        return textField;
    }

    public static ValidatedTextField getDescriptionTextField()
    {
        LengthTextField textField = new LengthTextField(0, 255, 35);
        textField.setName(Configuration.descriptionDBName);
        textField.setShowFeedback(true);
        return textField;
    }

    public static ValidatedTextField getAliasTextField()
    {
        LengthTextField textField = new LengthTextField(0, 50, 35);
        textField.setName(Configuration.aliasDBName);
        textField.setShowFeedback(true);
        return textField;
    }

    public static ValidatedTextField getNameTextField()
    {
        LengthTextField textField = new LengthTextField(0, 30, 35);
        textField.setName(Configuration.nameDBName);
        textField.setShowFeedback(true);

        return textField;
    }

    public static ValidatedTextField getNameTextField(String dbName)
    {
        LengthTextField textField = new LengthTextField(0, 30, 35);
        textField.setShowFeedback(true);
        textField.setName(dbName);
        return textField;
    }

    public static ValidatedTextField getNoteTextField(String dbName)
    {
        LengthTextField textField = new LengthTextField(0, 255, 35);
        textField.setShowFeedback(true);
        textField.setName(dbName);
        return textField;
    }

    public static ValidatedTextField getPhysRouteDescTextField(String dbName)
    {
        LengthTextField textField = new LengthTextField(0, 30, 10);
        textField.setShowFeedback(true);
        textField.setName(dbName);
        return textField;
    }

    public static VisiCheckbox getInactiveIndicatorCheckBox()
    {
        VisiCheckbox checkBox = new VisiCheckbox(PolarisUI.getMessage("CF_INACTIVE"));
        checkBox.setName(Configuration.inactiveIndicatorDBName);
        return checkBox;
    }


    public static void resetComboBoxModel(JPanel panel)
    {


        Component[] pc = panel.getComponents();

        for (int i = 0; i < pc.length; i++)
        {

            if (pc[i] instanceof VisiComboBox)
            {

                //if this is the activeinactive combobox, dont reset.
                if(!((VisiComboBox) pc[i]).getName().equals(Configuration.inactiveIndicatorDBName))
                {
                    if (panel instanceof GenericFilterPanel)
                    {
                        //once it call this method, it needs to break out of the for loop so that the action performed
                        //is not called multiple times.
                        ((GenericFilterPanel) panel).updateCombo(new ActionEvent(pc[i], 0, ""));
                        break;
                    }
                    else
                        updateCombo((SortedIdNameComboBox) pc[i], "");


                }

            }

            // Check if cp is a JPanel
            if (pc[i] instanceof JPanel && !(pc[i] instanceof NrGDate))
            {
                // Populate the children
                resetComboBoxModel((JPanel) pc[i]);
            }


        }


    }

    public static void updateCombo(SortedIdNameComboBox comboBox, String where)
    {

        //ui.setBusyCursor();

        String oldItem = null;

        String tableName = "DUAL";

        // From the combo box model, grab the table name as well as special items added to the model.  Special items
        // are items that do not actually exist in the data source.
        if (comboBox.getModel() instanceof FSIdNameComboBoxModel)
        {
            tableName = ((FSIdNameComboBoxModel) comboBox.getModel()).getFactory().getTableName();
        }


        //get the prev selected item.
        if (comboBox.getSelectedId() > 0)
        {
            oldItem = comboBox.getSelectedName();
        }
        try
        {


            //gets the model factory for the the given table and filter criteria.
            DBIdNameFactory model = getDBIdNameFactory(tableName, where);

            // reset the data model.
            FSIdNameComboBoxModel newModel = new FSIdNameComboBoxModel(model);
            comboBox.setModel(newModel);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }
        finally
        {
            //ui.setDefaultCursor();
        }
        if (oldItem != null)
        {
            comboBox.setSelectedItem(oldItem);
        } else
        {
            //if value is null, then default
            //the selected item to a specialItem.  ie.. ALL or NONE
            //else clear selection.
            if (comboBox.hasSpecialItems())
            {
                comboBox.selectFirstSpecialItem();
            } else
            {
                comboBox.clearSelection();
            }


        }
    }

    public static void updateCombo(VisiTableComboBox comboBox, String where)
    {

        //ui.setBusyCursor();

        String oldItem = null;

        String tableName = "DUAL";

        // From the combo box model, grab the table name as well as special items added to the model.  Special items
        // are items that do not actually exist in the data source.
        if (comboBox.getModel() instanceof TableComboBoxModel)
        {
            //tableName = ((TableComboBoxModel) comboBox.getModel()).getTableName();
            tableName = comboBox.getName().replace("(", "");
        }


        //get the prev selected item.
        if (comboBox.getSelectedId() > 0)
        {
            oldItem = comboBox.getSelectedName();
        }
        try
        {


            //gets the model factory for the the given table and filter criteria.
            DBIdNameFactory model = getDBIdNameFactory(tableName, where);
            comboBox.setModel(model, 1);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }
        finally
        {
            //ui.setDefaultCursor();
        }
        if (oldItem != null)
        {
            comboBox.setSelectedItem(oldItem);
        } else
        {
            //if value is null, then default
            //the selected item to a specialItem.  ie.. ALL or NONE
            //else clear selection.
            if (comboBox.hasSpecialItems())
            {
                comboBox.selectFirstSpecialItem();
            } else
            {
                comboBox.clearSelection();
            }
        }
    }


    /**
     * creates the model factory for a combobox for a given lookup table.
     * always load the factory.
     *
     * @param lookUpTable - table to create the model factory for.
     * @param where  - where clause for the model Factory
     * @return
     */
    public static DBIdNameFactory getDBIdNameFactory(String lookUpTable, String where)
    {
        return getDBIdNameFactory(lookUpTable,where,true);
    }


    /**
     * creates the model factory for a combobox for a given lookup table.
     *
     *
     * @param lookUpTable - table to create the model factory for.
     * @param where         - where clause for the model Factory
     * @param loadNow       - loads the factory immediately.
     * @return
     */

    public static DBIdNameFactory getDBIdNameFactory(String lookUpTable, String where,boolean loadNow)
    {

        DBIdNameFactory factory = null;

        if (lookUpTable.equals("GRADE_CONF"))
        {
            String query =
                    "SELECT G.ID, G.NAME || ' - ' || F.DESCRIPTION || ' - ' || G.DESCRIPTION FROM POLARIS.GRADE_CONF G, POLARIS.FLUID_TYPE_DESC F WHERE G.FLUID_TYPE_ID = F.ID ";

            if (where != null && where.length() > 0)
            {
                query += " AND G.ID IN (SELECT ID FROM GRADE_CONF WHERE " + where + ") ";
            }

            query += " ORDER BY F.DESCRIPTION, G.NAME ";
            factory = new GradeModelFactory(query);

            // Joe Hunsaker - Fix a defect where grade_conf table name is lost from the factory
            factory.setTableName(lookUpTable);
        } else
        {
            //gets the display value for the combobox
            String displayValue = ComponentFactory.getComboBoxDisplayValue(lookUpTable);

            //create the model factory.
            if (where == null || where.isEmpty())
            {
                factory = new DBIdNameFactory(lookUpTable, "ID", displayValue, null, null);
            } else
            {
                factory = new DBIdNameFactory(lookUpTable, "ID", displayValue, where, null);
            }
        }

        //if not loaded now
        //return the factory.
        //typically set the factory to load later for JTable cells that has comobo boxes.
        if (!loadNow)
            return factory;

        //check if the factory exists in cache.
        FactoryProduct fp = DBIdNameFactory.getProduct(factory);
        if (fp == null)
        {
            //if the factory doesnt exist in cache,
            //load the data immediately.
            factory.loadData();
        } else
        {
            //if the factory exist in cache
            //set the model to the cached model
            factory = (DBIdNameFactory) fp.getFactory();
            if (!factory.hasProduced())
            {
                //if Factory has not been produced
                //load the data.
                factory.loadData();
            }

        }


        return factory;


    }

    /**
     * Creates a VisiTableComboBox for a combobox for a given lookup table.
     *
     * @param lookUpTable - table to create the model factory for.
     * @param where       - where clause for the model Factory
     * @return
     */
    public static VisiTableComboBox getVisiTableComboBoxFactory(String lookUpTable, String where)
    {

        VisiTableComboBox visiTableComboBox = null;
        String            query;

        switch (lookUpTable.toUpperCase())
        {
            case "GRADE_CONF":
                query =
                        "SELECT G.ID \"ID\", G.NAME \"Name\", F.DESCRIPTION \"Fluid Type\", G.DESCRIPTION \"Description\" FROM POLARIS.GRADE_CONF G, POLARIS.FLUID_TYPE_DESC F\n" +
                        "WHERE G.FLUID_TYPE_ID = F.ID\n" +
                        "AND G.INACTIVE_INDICATOR_FLAG = 0\n";

                if (where != null && where.length() > 0)
                {
                    query += " AND G.ID IN (SELECT ID FROM GRADE_CONF WHERE " + where + ") ";
                }

                query += " ORDER BY F.NAME, G.NAME ";

                visiTableComboBox = new VisiTableComboBox(query, 1, lookUpTable);
                break;
            default:
                // The table model is not defined for this field.
                // remove exception, and just log to the log
                PolarisUI.logError("The VisiTableComboBox is not defined for this table: " + lookUpTable);
                break;
        }

        return visiTableComboBox;
    }

    /**
     * Gets the where clauses for DBIdNameFactory.  This is the default where clause.
     * filters on active records for the active view inactive records for inactive view.
     *
     * @param activeView
     * @return
     */
    public static String getDBIdNameFactoryWhereClause(boolean activeView)
    {

        String whereClause = "";
        //where clause for the model factory.
        //if this is in readonly/InactiveView
        //then get all records from the database
        //else if viewing in activeView/not readonly
        //then get only records that are active.
        if (activeView)
        {
            whereClause = " INACTIVE_INDICATOR_FLAG = 0 ";
        }


        return whereClause;

    }

    /**
     * Method loops through all components adds listeners to the following components
     * VisiCheckbox,JComboBox and JTextField
     *
     * @param currentPanel
     */
    public static void addListeners(JPanel currentPanel)
    {
        addListeners(currentPanel, currentPanel);
    }


    /**
     * Method loops through all components adds listeners to the following components
     * VisiCheckbox,JComboBox and JTextField
     *
     * @param currentPanel
     * @param actionPanel
     */
    protected static void addListeners(JPanel currentPanel, JPanel actionPanel)
    {

        Component[] cp = currentPanel.getComponents();


        for (int i = 0; i < cp.length; i++)
        {
            if (actionPanel instanceof ActionListener)
            {
                if (cp[i] instanceof VisiCheckbox)
                {
                    VisiCheckbox c = (VisiCheckbox) cp[i];
                    c.addActionListener((ActionListener) actionPanel);
                    c.setActionCommand(c.getName());

                }
                if (cp[i] instanceof JComboBox)
                {
                    JComboBox c = (JComboBox) cp[i];
                    c.addActionListener((ActionListener) actionPanel);
                    c.setActionCommand(c.getName());

                }
                if (cp[i] instanceof NrGDate)
                {
                    NrGDate c = (NrGDate) cp[i];
                    c.addActionListener((ActionListener) actionPanel);
                    c.setActionCommand(c.getName());

                }
            }


            if (actionPanel instanceof TextListener)
            {

                if (cp[i] instanceof JTextField)
                {
                    if (cp[i] instanceof BigDecimalTextField)
                    {
                        BigDecimalTextField c = (BigDecimalTextField) cp[i];
                        c.addTextListener((TextListener) actionPanel);
                        c.setActionCommand(c.getName());

                    } else if (cp[i] instanceof ValidatedTextField)
                    {
                        ValidatedTextField c = (ValidatedTextField) cp[i];
                        c.addTextListener((TextListener) actionPanel);
                        c.setActionCommand(c.getName());
                    } else
                    {
                        VisiTextField c = (VisiTextField) cp[i];
                        c.addTextListener((TextListener) actionPanel);
                        c.setActionCommand(c.getName());

                    }
                }
            }

            if (actionPanel instanceof MouseListener)
            {
                if (cp[i] instanceof JTextField ||
                    cp[i] instanceof VisiCheckbox ||
                    cp[i] instanceof JComboBox ||
                    cp[i] instanceof NrGDate)
                {
                    cp[i].addMouseListener((MouseListener) actionPanel);
                }
            }

            // Check if cp is a JPanel
            if (cp[i] instanceof JPanel && !(cp[i] instanceof NrGDate))
            {
                // Populate the children
                addListeners((JPanel) cp[i], actionPanel);
            }
        }

    }

    /**
     * Method loops through all components and removes listeners to the following components
     * VisiCheckbox,JComboBox and JTextField
     *
     * @param currentPanel
     */
    public static void removeListeners(JPanel currentPanel)
    {
        removeListeners(currentPanel, currentPanel);
    }

    /**
     * Method loops through all components and removes listeners to the following components
     * VisiCheckbox,JComboBox and JTextField
     *
     * @param currentPanel
     */
    protected static void removeListeners(JPanel currentPanel, JPanel actionPanel)
    {
        Component[] pc = currentPanel.getComponents();

        for (int i = 0; i < pc.length; i++)
        {
            if (actionPanel instanceof ActionListener)
            {

                if (pc[i] instanceof VisiCheckbox)
                {
                    ((VisiCheckbox) pc[i]).removeActionListener((ActionListener) actionPanel);
                }
                if (pc[i] instanceof JComboBox)
                {
                    ((JComboBox) pc[i]).removeActionListener((ActionListener) actionPanel);
                }
                if (pc[i] instanceof NrGDate)
                {
                    ((NrGDate) pc[i]).removeActionListener((ActionListener) actionPanel);
                }
            }

            if (actionPanel instanceof TextListener)
            {
                if (pc[i] instanceof JTextField)
                {
                    if (pc[i] instanceof BigDecimalTextField)
                    {
                        BigDecimalTextField c = (BigDecimalTextField) pc[i];
                        c.removeTextListener((TextListener) actionPanel);
                    } else if (pc[i] instanceof ValidatedTextField)
                    {
                        ValidatedTextField c = (ValidatedTextField) pc[i];
                        c.removeTextListener((TextListener) actionPanel);
                    } else
                    {
                        VisiTextField c = (VisiTextField) pc[i];
                        c.removeTextListener((TextListener) actionPanel);

                    }
                }
            }

            if (actionPanel instanceof MouseListener)
            {
                if (pc[i] instanceof JTextField ||
                    pc[i] instanceof VisiCheckbox ||
                    pc[i] instanceof JComboBox ||
                    pc[i] instanceof NrGDate)
                {
                    pc[i].removeMouseListener((MouseListener) actionPanel);
                }
            }

            // Check if cp is a JPanel
            if (pc[i] instanceof JPanel && !(pc[i] instanceof NrGDate))
            {
                // Populate the children
                removeListeners((JPanel) pc[i], actionPanel);
            }


        }
    }

    /**
     * Sets the tableModel class name of each component in the tab panel to one specific tableModel class.
     * This is only called if the panel only has one tableModel.  For instances where the panel has multiple
     * TableModels.  Then the component's setTableModelClassName will have to be called directly.
     *
     * @param currentPanel
     * @param tableModelClassName
     */
    public static void setTableModelClassName(JPanel currentPanel, String tableModelClassName)
    {
        Component[] pc = currentPanel.getComponents();

        for (int i = 0; i < pc.length; i++)
        {
            //check if this component is an instance of TableModelClassName
            //sets the tableModelClass name for each component of a panel.
            if (pc[i] instanceof TableModelClassName)
            {
                ((TableModelClassName) pc[i]).setTableModelClassName(tableModelClassName);
            }


        }
    }

    public static void setDefaultValues(JPanel panel)
    {


        Component[] pc = panel.getComponents();

        for (int i = 0; i < pc.length; i++)
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
                } else
                {
                    if(((VisiComboBox) pc[i]).getName().equals(Configuration.inactiveIndicatorDBName))
                        ((VisiComboBox) pc[i]).setSelectedItem(0);
                    else
                        ((VisiComboBox) pc[i]).clearSelection();
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



            // Check if cp is a JPanel
            if (pc[i] instanceof JPanel && !(pc[i] instanceof NrGDate))
            {
                // Populate the children
                setDefaultValues((JPanel) pc[i]);
            }


        }


    }

}

