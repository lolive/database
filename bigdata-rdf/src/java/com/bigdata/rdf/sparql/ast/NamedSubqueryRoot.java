/**

Copyright (C) SYSTAP, LLC 2006-2011.  All rights reserved.

Contact:
     SYSTAP, LLC
     4501 Tower Road
     Greensboro, NC 27410
     licenses@bigdata.com

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; version 2 of the License.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package com.bigdata.rdf.sparql.ast;

import com.bigdata.rdf.sail.QueryType;

/**
 * A subquery with a named solution set which can be referenced from other parts
 * of the query.
 * 
 * @see NamedSubqueryInclude
 */
public class NamedSubqueryRoot extends SubqueryBase {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    interface Annotations extends SubqueryRoot.Annotations {
        
        /**
         * The name of the temporary solution set.
         */
        String SUBQUERY_NAME = "subqueryName";
        
        /**
         * A {@link VarNode}[] specifying the join variables that will be used
         * when the named result set is join with the query. The join variables
         * MUST be bound for a solution to join.
         * <p>
         * Note: This can be different for each context in the query in which a
         * given named result set is included. When there are different join
         * variables for different INCLUDEs, then we need to build a hash index
         * for each set of join variable context that will be consumed within
         * the query.
         * <p>
         * Note: If no join variables are specified, then the join will consider
         * the N x M cross product, filtering for solutions which join. This is
         * very expensive. Whenever possible you should identify one or more
         * variables which must be bound for the join and specify those as the
         * join variables.
         * 
         * TODO This should be an array of arrays in order to handle cases
         * where we need more than one join index (alternatively, we can just
         * use NO join variables for such cases and let performance suffer).
         */
        String JOIN_VARS = "joinVars";

    }
    
//    private String name;

    /**
     * 
     * @param queryType
     * @param name
     *            The name of the subquery result set.
     */
    public NamedSubqueryRoot(final QueryType queryType, final String name) {

        super(queryType);

        setName(name);

    }

    /**
     * The name associated with the subquery.
     */
    public String getName() {

        return (String) getProperty(Annotations.SUBQUERY_NAME);
        
    }

    /**
     * Set the name associated with the subquery.
     * 
     * @param name
     */
    public void setName(String name) {

        if(name == null)
            throw new IllegalArgumentException();
        
        setProperty(Annotations.SUBQUERY_NAME, name);
        
    }

    /**
     * The join variables to be used when the named result set is included into
     * the query.
     */
    public VarNode[] getJoinVars() {

        return (VarNode[]) getProperty(Annotations.JOIN_VARS);

    }

    /**
     * Set the join variables.
     * 
     * @param joinVars
     *            The join variables.
     */
    public void setJoinVars(final VarNode[] joinVars) {

        setProperty(Annotations.JOIN_VARS, joinVars);

    }

    @Override
    public String toString(int indent) {

        final StringBuilder sb = new StringBuilder();
        
        sb.append(super.toString(indent));
        
        sb.append("\n");
        
        sb.append(indent(indent));

        sb.append("AS ").append(getName());

        final VarNode[] joinVars = getJoinVars();

        if (joinVars != null) {

            sb.append(" JOIN ON (");

            boolean first = true;

            for (VarNode var : joinVars) {

                if (!first)
                    sb.append(",");

                sb.append(var);

                first = false;

            }

            sb.append(")");

        }

        return sb.toString();

    }

}
