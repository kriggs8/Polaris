package polaris.administration.tables.movements;

import polaris.administration.tables.GenericTableColumnModel;

/**
 * Table Column Model Class for MOVEMENT_CONF
 * Tabbed Panel Class for MOVEMENT_CONF
 * Creation Date: 1/9/2017
 * @Author: TCI- Waleed Elsaid
 */
public class MovementsTableColumnModel extends GenericTableColumnModel
{
    public final static String[] names = {"EDIT_MODE","NAME", "DESCRIPTION", "GRADE_SPEC_ID", "SYSTEM_GROUP_ID"};


    public MovementsTableColumnModel()
    {
        super(names);
    }
}
