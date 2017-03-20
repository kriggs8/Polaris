package polaris.administration.tables.facilities;

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
 * @Author TCI Krista Riggs
 * Created 3/15/2017.
 */

public class FacilitiesMaintenancePanel extends GenericTabPanel //implements ActionListener, TextListener
{
    public FacilitiesMaintenancePanel(MainPanel mainPanel) throws Exception
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

    }
    /**
     * Set the Validators as
     */
    @Override
    protected void setValidators()
    {

    }
}
