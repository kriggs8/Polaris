package polaris.administration.tables.facilities;

import polaris.administration.tables.GenericTableColumnModel;

/**
 *  Table Column Model Class for FACILITY_CONF
 *  It defines the columns to display
 *
 * @Author TCI Krista Riggs
 * Created 3/15/2017.
 */
public class FacilitiesTableColumnModel extends GenericTableColumnModel
{
    public final static String[] names = {"EDIT_MODE","NAME","ALIAS","DESCRIPTION","FACILITY_NUMBER","ENTITY_COMPANY_ID"};

    public FacilitiesTableColumnModel()
    {
        super(names);
    }
}
