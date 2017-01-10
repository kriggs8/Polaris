package polaris.administration.tables.batchRoutes;

import polaris.administration.tables.GenericTableColumnModel;


/**
 * Prototype Code - Table Column Model Class for BATCH_ROUTES_CONF. It defines the columns
 * to display
 *   TODO: MORE COMMENTS
 *   @Author: Jean Wurster
 */
public class BatchRoutesTableColumnModel extends GenericTableColumnModel
{
    public final static String[] names = {"EDIT_MODE","NAME", "DESCRIPTION", "PHYSICAL_ROUTE_NAME", "SCHED_SYSTEM_ID"};


    public BatchRoutesTableColumnModel()
    {
        super(names);
    }

}
