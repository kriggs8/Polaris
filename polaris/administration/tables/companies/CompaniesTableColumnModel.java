package polaris.administration.tables.companies;

import polaris.administration.tables.GenericTableColumnModel;


/**
 *  Table Column Model Class for COMPANY_CONF. It
 * defines the columns to display
 *
 * @Author TCI-Krista Riggs
 * Created 3/6/2017
 */
public class CompaniesTableColumnModel extends GenericTableColumnModel
{
    public final static String[] names = {"EDIT_MODE","NAME", "ALIAS", "DESCRIPTION", "ENTITY_IDENTIFICATION"};

    public CompaniesTableColumnModel()
    {
        super(names);
    }
}
