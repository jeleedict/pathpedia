/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.pathinfo.repository;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import kr.ac.knu.ml.db.DBConnection;
import kr.ac.knu.ml.pathinfo.unit.PathInfoOrganNormalize;

public class PathInfoOrganNormalizeRepository implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<PathInfoOrganNormalize> normlize; 	
	
	public <E> void constructRepository( ArrayList<E> normlize ) {
		this.normlize = new ArrayList<PathInfoOrganNormalize>();
		
		for ( E e : normlize ) {
			if ( e instanceof String[] ) {
				PathInfoOrganNormalize pidn = new PathInfoOrganNormalize ( (String[]) e );
				this.normlize.add( pidn );
			}
			else if ( e instanceof PathInfoOrganNormalize ) {
				this.normlize.add( (PathInfoOrganNormalize) e );
			}
		}		
	}

	public ArrayList<String> getOrganNames () {
		ArrayList<String> organNames = new ArrayList<String>();
		for ( PathInfoOrganNormalize pio : normlize ) {
			organNames.add( pio.getExtracted_OrganName() );			
		}
		return organNames;
	}

	public ArrayList<String> getOrganNamesFromDB() {
		try {
			String q = "select OrganName from pathinfo.organ";
			PreparedStatement ps;
			ps = DBConnection.getDBConnection().prepareStatement(q);
//			ps.setString(1, token + "%");
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst())
				return null;
//			rs.first();
			ArrayList<String> organNames = new ArrayList<String>();
			while (rs.next()) {
				organNames.add(rs.getString("OrganName"));
			}
			return organNames;

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
