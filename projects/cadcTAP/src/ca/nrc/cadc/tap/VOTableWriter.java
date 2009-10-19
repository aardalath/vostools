/*
************************************************************************
*******************  CANADIAN ASTRONOMY DATA CENTRE  *******************
**************  CENTRE CANADIEN DE DONNÉES ASTRONOMIQUES  **************
*
*  (c) 2009.                            (c) 2009.
*  Government of Canada                 Gouvernement du Canada
*  National Research Council            Conseil national de recherches
*  Ottawa, Canada, K1A 0R6              Ottawa, Canada, K1A 0R6
*  All rights reserved                  Tous droits réservés
*                                       
*  NRC disclaims any warranties,        Le CNRC dénie toute garantie
*  expressed, implied, or               énoncée, implicite ou légale,
*  statutory, of any kind with          de quelque nature que ce
*  respect to the software,             soit, concernant le logiciel,
*  including without limitation         y compris sans restriction
*  any warranty of merchantability      toute garantie de valeur
*  or fitness for a particular          marchande ou de pertinence
*  purpose. NRC shall not be            pour un usage particulier.
*  liable in any event for any          Le CNRC ne pourra en aucun cas
*  damages, whether direct or           être tenu responsable de tout
*  indirect, special or general,        dommage, direct ou indirect,
*  consequential or incidental,         particulier ou général,
*  arising from the use of the          accessoire ou fortuit, résultant
*  software.  Neither the name          de l'utilisation du logiciel. Ni
*  of the National Research             le nom du Conseil National de
*  Council of Canada nor the            Recherches du Canada ni les noms
*  names of its contributors may        de ses  participants ne peuvent
*  be used to endorse or promote        être utilisés pour approuver ou
*  products derived from this           promouvoir les produits dérivés
*  software without specific prior      de ce logiciel sans autorisation
*  written permission.                  préalable et particulière
*                                       par écrit.
*                                       
*  This file is part of the             Ce fichier fait partie du projet
*  OpenCADC project.                    OpenCADC.
*                                       
*  OpenCADC is free software:           OpenCADC est un logiciel libre ;
*  you can redistribute it and/or       vous pouvez le redistribuer ou le
*  modify it under the terms of         modifier suivant les termes de
*  the GNU Affero General Public        la “GNU Affero General Public
*  License as published by the          License” telle que publiée
*  Free Software Foundation,            par la Free Software Foundation
*  either version 3 of the              : soit la version 3 de cette
*  License, or (at your option)         licence, soit (à votre gré)
*  any later version.                   toute version ultérieure.
*                                       
*  OpenCADC is distributed in the       OpenCADC est distribué
*  hope that it will be useful,         dans l’espoir qu’il vous
*  but WITHOUT ANY WARRANTY;            sera utile, mais SANS AUCUNE
*  without even the implied             GARANTIE : sans même la garantie
*  warranty of MERCHANTABILITY          implicite de COMMERCIALISABILITÉ
*  or FITNESS FOR A PARTICULAR          ni d’ADÉQUATION À UN OBJECTIF
*  PURPOSE.  See the GNU Affero         PARTICULIER. Consultez la Licence
*  General Public License for           Générale Publique GNU Affero
*  more details.                        pour plus de détails.
*                                       
*  You should have received             Vous devriez avoir reçu une
*  a copy of the GNU Affero             copie de la Licence Générale
*  General Public License along         Publique GNU Affero avec
*  with OpenCADC.  If not, see          OpenCADC ; si ce n’est
*  <http://www.gnu.org/licenses/>.      pas le cas, consultez :
*                                       <http://www.gnu.org/licenses/>.
*
*  $Revision: 4 $
*
************************************************************************
*/

package ca.nrc.cadc.tap;

import ca.nrc.cadc.tap.parser.adql.TapSelectItem;
import ca.nrc.cadc.tap.schema.TapSchema;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;

import ca.nrc.cadc.uws.ExecutionPhase;

import java.util.List;
import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.DefaultValueInfo;
import uk.ac.starlink.table.DescribedValue;
import uk.ac.starlink.table.RowListStarTable;
import uk.ac.starlink.table.ValueInfo;
import uk.ac.starlink.votable.DataFormat;

public class VOTableWriter implements TableWriter
{
    public VOTableWriter() { }

    public String getExtension()
    {
        return "xml";
    }

    public void setSelectList(List<TapSelectItem> items)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setTapSchema(TapSchema schema)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
	public void write( ResultSet rs, OutputStream output )
	{
		throw new UnsupportedOperationException( "Don't know where to get a result set from yet." );
	}
	
	public void write( Throwable thrown, OutputStream output )
		throws IOException
	{
	    //  Define table.
		ColumnInfo[] columnInfo = new ColumnInfo[1];
	    columnInfo[0] = new ColumnInfo( "Message", String.class, "Message text" );
	    RowListStarTable table = new RowListStarTable( columnInfo );
	    
	    //  Since this method is in response to a Throwable,
	    //  it is safe to assume that this is an error document.
	    ValueInfo valInfo = new DefaultValueInfo( "QUERY_STATUS");
	    DescribedValue descVal = new DescribedValue( valInfo, ExecutionPhase.ERROR );
	    table.setParameter( descVal );

	    //  Populate table.
	    table.addRow( new Object[] { thrown.getMessage() } );
	    Throwable cause = thrown.getCause();
	    while ( cause != null )
	    {
	    	String message = cause.getMessage();
		    table.addRow( new Object[] { message } );
		    cause = cause.getCause();
	    }
	    
        //  Specify output format as XML, rather than FITS or binary.
        uk.ac.starlink.votable.VOTableWriter voWriter = new uk.ac.starlink.votable.VOTableWriter( DataFormat.TABLEDATA, true );

        //  Write table.
        voWriter.writeStarTable( table, output );
	}

}
