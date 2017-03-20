package polaris.administration.tables.contacts;

import polaris.administration.tables.GenericTableColumnModel;
/**
 *  Table Column Model Class for CONTACT_CONF. It
 * defines the columns to display
 *
 * @Author TCI - Krista Riggs
 * Created on 2/2/2017.
 *
 *
 */
public class ContactsTableColumnModel extends GenericTableColumnModel
{
    public final static String[] names = {"EDIT_MODE", "COMPANY_ID", "DESCRIPTION", "SCHEDULER_TYPE_ID"};

    public ContactsTableColumnModel()
    {
        super(names);
    }
}
