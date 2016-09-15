package dao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.el.MethodNotFoundException;

import com.annotations.Parameter;

import com.mapping.DataEntity;
import com.mapping.ListPaginner;
import dao.Connecteur.DatabaseType;

public class DaoModele {
	private static DaoModele instance=null;
    public DaoModele()
    {
    	instance=this;
    }
    public static DaoModele getInstance(){
    	if(instance==null)
    		return instance=new DaoModele();
    	return instance;
    }
    public String getRequette(int page,String nomTable,DataEntity objet){
    	String reponse=" SELECT * FROM "+nomTable;
    	if(objet.findNomChampOrder()!=null && objet.findOrdering()!=null)
    		reponse+=" order by "+objet.findNomChampOrder()+" "+objet.findOrdering();
    	
    	if(Connecteur.dataBaseTypeList.get(objet.findDataBaseKey())==DatabaseType.PSQL)
    		reponse= " SELECT * FROM ("+reponse+") as t offset "+(page * objet.findPackSize() - objet.findPackSize())+" limit "+objet.findPackSize();
		else if(Connecteur.dataBaseTypeList.get(objet.findDataBaseKey())==DatabaseType.ORACLE)
			reponse= " SELECT * FROM (SELECT  rownum rnum, sous.*  FROM (SELECT *  FROM ("+reponse+") as t ) sous WHERE rownum <= " + ((page * objet.findPackSize() - objet.findPackSize()) +objet.findPackSize())+ " ) WHERE rnum > " + (page * objet.findPackSize() - objet.findPackSize()) + "";
		else if(Connecteur.dataBaseTypeList.get(objet.findDataBaseKey())==DatabaseType.MYSQL)
			reponse= " SELECT * FROM ("+reponse+") as t  limit "+objet.findPackSize()+" offset "+(page * objet.findPackSize() - objet.findPackSize());
    	
    	return reponse;
    }
    public int getNbPage(String nomTable,DataEntity objet,Connection connection)throws Exception
    {
    	int reponse=0;
		Statement stat=null;
		ResultSet rs=null;
		try{
            String query="select count(*) as sum from "+nomTable+"";
            
            stat=connection.createStatement();
             rs=stat.executeQuery(query);
            while(rs.next()){
            	reponse=calculePage(rs.getInt("sum"), objet.findPackSize());
            	
            }
		}
		catch(Exception ex){
			ex.printStackTrace();
			throw ex;
		}
		finally{
			if(rs!=null)rs.close();
			if(stat!=null)stat.close();
		}
		return reponse;
    }
    static int calculePage(int nb,int nbppage)
    {
        int reponse = 0;
        float inter = (float)nb / (float)nbppage;
        
        if (inter -((int)inter) > 0.0)
        {
            reponse = nb / nbppage+1;
        }
        else
        {
            reponse=nb / nbppage;
        }
        return reponse;
    }
    public Boolean isPageExiste(int nbpage,int page)
    {
        if (page > nbpage)
            return false;
        return true;
    }
    public <T extends DataEntity> List<T> findPageGenerique(int page,T objet,String apresWhere) throws Exception{
		Connection connection=null;
		try{
			connection=Connecteur.getConnection(objet.findDataBaseKey());
            return findPageGenerique(page, objet,connection,apresWhere);
		}
		catch(Exception ex){
			ex.printStackTrace();
			throw ex;
		}
		finally{
			if(connection!=null)connection.close();
			
		}
    }
    public <T extends DataEntity> List<T> findPageGenerique(int page,T objet) throws Exception{
		Connection connection=null;
		try{
			connection=Connecteur.getConnection(objet.findDataBaseKey());
            return findPageGenerique(page, objet,connection,"");
		}
		catch(Exception ex){
			ex.printStackTrace();
			throw ex;
		}
		finally{
			if(connection!=null)connection.close();
			
		}
    }
    public <T extends DataEntity> List<T> findStat(int page,T objet,Connection connection,String apresWhere)throws Exception
    {
    	Field[]fields=objet.getAllFields();
    	List<Field> summ=objet.findSummField();
    	List<Field> group=objet.findGroupField();
    	String toSumm="";
    	String toGroup="";
    	if(group.size()==0){
    		throw new Exception("Les colonnes dans le group by sont obligatoires");
    	}
    	for(Field f:summ){
    		if(!isExisteChamp(objet.getReferenceForField(f), objet.findReference(), connection))
    			continue;
    		toSumm+=",summ("+objet.getReferenceForField(f)+") as "+objet.getReferenceForField(f);
    	}
    	boolean isFirst=true;
    	for(Field f:group){
    		if(!isExisteChamp(objet.getReferenceForField(f), objet.findReference(), connection))
    			continue;
    		if(!isFirst)
    			toGroup+=",";
    		isFirst=false;
    		toGroup+=""+objet.getReferenceForField(f);
    	}
    	String requette="(select count(*) as count "+toSumm+" from "+objet.findReference()+" group by "+toGroup+" ) as interstat ";
    	objet.setNomTable(requette);
    	return findPageGenerique(page, objet, connection, apresWhere);
    }
    public <T extends DataEntity> List<T> findPageGenerique(int page,T objet,Connection connection,String apresWhere) throws Exception{
    	
    	List<T> reponse=new ListPaginner<T>();
		Statement stat=null;
		ResultSet rs=null;
		String nomTable;
		try{
			nomTable=getNomTable(objet,connection,apresWhere);
			((ListPaginner<T>)(reponse)).nbPage = this.getNbPage(nomTable,objet,connection);
			if(page<=0)
				return reponse;
			int nbpage=((ListPaginner<DataEntity>)(reponse)).nbPage;
			if(nbpage<=0)return reponse;
            if (this.isPageExiste(((ListPaginner<DataEntity>)(reponse)).nbPage, page) == false)
                return this.findPageGenerique(((ListPaginner<DataEntity>)(reponse)).nbPage-1,objet,connection,apresWhere);
            
            String query=this.getRequette(page,nomTable,objet);
            stat=connection.createStatement();
             rs=stat.executeQuery(query);
            reponse=resoudre(rs,objet);
            
            ((ListPaginner<T>)(reponse)).nbPage = nbpage;
            
		}
		catch(Exception ex){
			ex.printStackTrace();
			throw ex;
		}
		finally{
			if(rs!=null)rs.close();
			if(stat!=null)stat.close();
		}
			
		return reponse;
    }
    public <T extends DataEntity> List<T> resoudre(ResultSet rs,T objet)throws Exception{
    	
    	List<T> reponse=new ListPaginner<T>();
    	try{
    	
    	T inter=null;
    	Field[]interv=objet.getAllFields();
    	ResultSetMetaData meta=rs.getMetaData();
        while(rs.next()){
        	inter=(T) objet.getClass().newInstance();
        	for(int i=0;i<interv.length;i++){
        		if(isBaseType((interv[i]).getType())==false){
        			continue;
        		}
        		String getting=setMaj(interv[i].getType().getSimpleName());
        		if(isExisteColonne(rs,inter.getReferenceForField(interv[i]))==false){
        			continue;
        		}
        		Object valeur=rs.getClass().getMethod("get"+getting,new Class[]{String.class}).invoke(rs, inter.getReferenceForField(interv[i]));
        		if(rs.wasNull()){
        			continue;
        		}
        		Method meth=objet.getClass().getMethod("set"+setMaj(interv[i].getName()), new Class[]{interv[i].getType()});
        		if(java.util.Date.class.equals(interv[i].getType())){
        			meth.invoke(inter,new java.util.Date(((java.sql.Date)valeur).getTime()));
        		}
        		else{
        			if(interv[i].getType().equals(String.class)){
        				if(objet.findConcatString().containsKey(interv[i].getName())){
        					valeur=objet.findConcatString().get(interv[i].getName())+((String)valeur);
        				}
        			}
        			meth.invoke(inter, valeur);
        			
        		}
        	}
        	if(isExisteColonne(rs, "count"))
        	{
        		inter.setCount(rs.getInt("count"));
        	}
        	reponse.add(inter);
        }
    }
	catch(Exception ex){
		ex.printStackTrace();
		throw ex;
	}
    	return reponse;
    }
    public boolean isExisteColonne(ResultSet rs,String nom) throws SQLException{
    	 ResultSetMetaData rsmd = rs.getMetaData();
    	    int columns = rsmd.getColumnCount();
    	    for (int x = 1; x <= columns; x++) {
    	        if (nom.compareToIgnoreCase(rsmd.getColumnName(x))==0 && nom.length()==rsmd.getColumnName(x).length()) {
    	            return true;
    	        }
    	    }
    	    return false;
    }
    public static String setMaj(String nom){
    	return nom.toUpperCase().substring(0, 1)+""+nom.substring(1);
    }
    public boolean isBaseType(Class classe){
    	Class[] liste={int.class,Double.class,String.class,double.class,Number.class,java.util.Date.class,java.sql.Date.class,Boolean.class,boolean.class};
    	for(int i=0;i<liste.length;i++){
    		if(classe.getName()==liste[i].getName())
    			return true;
    	}
    	return false;
    }
    boolean isExisteChamp(String champ,String nomTable,Connection conn) throws Exception{
    	Statement stat=null;
    	ResultSet rs=null;
    	try{
    		stat=conn.createStatement();
    		rs=stat.executeQuery("select * from "+nomTable+" where 1=2");
    		ResultSetMetaData rsmd = rs.getMetaData();
    		int columns = rsmd.getColumnCount();
    	    for (int x = 1; x <= columns; x++) {
    	        if (champ.compareToIgnoreCase(rsmd.getColumnName(x))==0 && champ.length()==rsmd.getColumnName(x).length()) {
    	        
    	        	return true;
    	        }
    	    }
    		
		    return false;
    	}
    	catch(Exception ex){
    		
    		throw ex;
    	}
    	finally{
    		if(rs!=null)rs.close();
    		if(stat!=null)stat.close();
    	}
    	
    }
    public void save(List<DataEntity> data, Connection con) throws Exception
	{
		PreparedStatement pst=null;

		try
		{
			if(data.size()==0)
				return;
			String requete=buildQueryInsert(data.get(0));
			
			pst=con.prepareStatement(requete,Statement.RETURN_GENERATED_KEYS);
			int indiceStat=1;
			Field[] champs=data.get(0).getAllFields();
			for(int ii=0;ii<data.size();ii++){
				if(!data.get(ii).isValide())
				{
					throw new Exception("erreur");
				}
				for(int i=0; i<champs.length;i++)
				{
					if(isExisteChamp(data.get(0).getReferenceForField(champs[i]),data.get(0).findReference(),con)==false)continue;
					if(champs[i].getName().compareToIgnoreCase(data.get(0).getPkName())==0)continue;
					if(isBaseType(champs[i].getType())==false)continue;
					
					Method m=null;
					try{
						m=data.get(ii).getClass().getMethod("get"+setMaj(champs[i].getName()), null);
					}
					catch(NoSuchMethodException ex){
						try{
							m=data.get(ii).getClass().getMethod("find"+setMaj(champs[i].getName()), null);
						}
						catch(Exception e){
						}
					}
					Object val=null;
					if(champs[i].getType().equals(java.util.Date.class)){
						Object inter=m.invoke(data.get(ii), null);
						val=(inter!=null) ? new java.sql.Date(((java.util.Date) inter).getTime()) : null ;
					}
					else val=m.invoke(data.get(ii), null);
					pst.setObject(indiceStat, val);
					indiceStat++;
				}
				indiceStat=1;
				pst.execute();
				try(ResultSet generatedKey=pst.getGeneratedKeys()){
					if(generatedKey.next()){
						try{
							data.get(ii).getClass().getMethod("set"+setMaj(data.get(0).getPkName()), int.class).invoke(data.get(ii), generatedKey.getInt(data.get(0).getPkName()));
						}
						catch(Exception ex){
							
						}
						
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		finally
		{
			if(pst!=null) pst.close();	
		}
	}
	public String buildQueryInsert(DataEntity bm) throws Exception 
	{
		Connection conn=null;
		try{
		
			conn=Connecteur.getConnection(bm.findDataBaseKey());
		String champs="";
		String valeurs="";
			
		Field[] fields=bm.getAllFields();
		boolean index=false;
		
		for(int i=0; i<fields.length; i++)
		{
			if(isExisteChamp(bm.getReferenceForField(fields[i]),bm.findReference(),conn)==false)continue;
			if(fields[i].getName().compareToIgnoreCase(bm.getPkName())==0)continue;
			if(isBaseType(fields[i].getType())==false)continue;
			if(index==true) 
			{
				champs=champs+", ";
				valeurs=valeurs+", ";
			}
			index=true;
			
			champs=champs+fields[i].getName().toString();
			valeurs=valeurs+"?";
			
			
		}		
		String requete="insert into "+bm.findReference()+" ("+champs+") values ("+valeurs+")";
		return requete;
		}
		catch(Exception ex){
			ex.printStackTrace();
			throw ex;
		}
		finally{
			if(conn!=null)
				conn.close();
		}
	}
	public String buildId(DataEntity bm, Connection con) throws Exception
	{
		return "";
		
	}
	String getCondition(DataEntity objet,Connection conn,String apresWhere)throws Exception{
		String reponse="";
		if(objet!=null){
			
			Map<Field,Object>filters=objet.getFieldsFilter();
			Set<Entry<Field, Object>> fil=filters.entrySet();
			
			for(Entry<Field, Object> entry:fil){
				if(entry.getKey().getType().equals(java.sql.Date.class)){
					DateFormat df = new SimpleDateFormat("MM/dd/yyyy");  
					String text = df.format((java.sql.Date)entry.getValue());
					reponse+=" "+objet.getReferenceForField(entry.getKey())+"='"+text+"' and ";
				}
				else if(entry.getKey().getType().equals(java.util.Date.class)){
					DateFormat df = new SimpleDateFormat("MM/dd/yyyy");  
					String text = df.format(new java.sql.Date(((java.util.Date)entry.getValue()).getTime()));
					reponse+=" "+objet.getReferenceForField(entry.getKey())+"='"+text+"' and ";
				}
				else if(entry.getKey().getType().equals(String.class))  
					reponse+=" "+objet.getReferenceForField(entry.getKey())+" like '"+entry.getValue()+"' and";
				else 
					reponse+=" "+objet.getReferenceForField(entry.getKey())+"="+entry.getValue()+" and";
			}
			
		}
		reponse+=" 1=1 "+apresWhere;
		return reponse;
	}
	String getNomTable(DataEntity objet,Connection conn,String apresWhere)throws Exception{
		return objet.findReference()+" WHERE "+getCondition(objet,conn,apresWhere);
	}
	public void update(List<DataEntity> liste,Connection conn,String apresWhere)throws Exception{
		String[]col=colModif(liste.get(0));
		String query=getRequetteModif(liste.get(0),col);
		if(apresWhere!=null)
			query+=" "+apresWhere;
		PreparedStatement prstat=conn.prepareStatement(query);
		Field[]attr=liste.get(0).getAllFields();
		boolean isValable=false;
		int comptset=1;
		for(int ii=0;ii<liste.size();ii++){
			if(!liste.get(ii).isValide())
			{
				throw new Exception("erreur");
			}
			for(String s:col){
				if(s!=null){
					Object valeur=null;
					try{
						valeur=liste.get(ii).getClass().getMethod("get"+setMaj(s), null).invoke(liste.get(ii), null);
					}
					catch(NoSuchMethodException ex){
						valeur=liste.get(ii).getClass().getMethod("find"+setMaj(s), null).invoke(liste.get(ii), null);
					}
					
					if(valeur!= null && valeur.getClass().equals(java.util.Date.class))
						prstat.setObject(comptset, new java.sql.Date(((java.util.Date)valeur).getTime()));
					else {
						prstat.setObject(comptset, valeur);
					}
					comptset++;
				}
				else break;
			}
			prstat.setObject(comptset,liste.get(ii).getClass().getMethod("get"+setMaj(((DataEntity)liste.get(ii)).getPkName()), null).invoke(liste.get(ii), null));
			comptset=1;
			prstat.executeUpdate();
		}
	}
	public void update(List<DataEntity> liste)throws Exception{
		Connection conn = null;
		try{
			conn=Connecteur.getConnection(liste.get(0).findDataBaseKey());
			conn.setAutoCommit(false);
			update(liste,conn,"");
			conn.commit();
		}
		catch(Exception ex){
			if(conn!=null){
				conn.rollback();
			}
			ex.printStackTrace();
			throw ex;
		}
		finally{
			if(conn!=null){
				conn.close();
			}
		}
	}
	String [] colModif(DataEntity modele)throws Exception{
		
		Field[]attr=modele.getAllFields();
		String reponse[]=new String[attr.length];
		int irep=0;
		
		boolean isIndex=true;
		
		for(int i=0;i<attr.length;i++){
			if(!isBaseType(attr[i].getType()))
				continue;
			if(attr[i].getName().compareToIgnoreCase(modele.getPkName())==0)
				continue;
			Object valeur=null;
			try{
				valeur=modele.getClass().getMethod("get"+setMaj(attr[i].getName()), null).invoke(modele, null);
			}
			catch(NoSuchMethodException ex){
				try{
					valeur=modele.getClass().getMethod("find"+setMaj(attr[i].getName()), null).invoke(modele, null);
				}
				catch(Exception e){
				}
			}
			
			reponse[irep]=attr[i].getName();
			irep++;
			isIndex=false;
			
		}
		return reponse;
	}
	String getRequetteModif(DataEntity modele,String[]field)throws Exception{
		String reponse=" update "+modele.findReference()+" set ";
		boolean isIndex=true;
		for(String s:field){
			if(s!=null){
				if(isIndex==false)
					reponse+=" ,";
				reponse+=" "+modele.getReferenceForField(modele.getFieldByName(s))+"=?";
				isIndex=false;
			}
			else break;
		}
		reponse+=" WHERE "+modele.getPkName()+"=?";
		return reponse;
	}
	public void save(DataEntity av, Connection connection)throws Exception {
		List<DataEntity> liste=new ArrayList<DataEntity>();
		liste.add(av);
		this.save(liste, connection);
	}
	public void save(DataEntity av)throws Exception {
		Connection conn=null;
		try{
			conn=Connecteur.getConnection(av.findDataBaseKey());
			save(av,conn);
		}
		catch(Exception ex){
			throw ex;
		}
		finally{
			if(conn!=null)
				conn.close();	
			}
	}
	
	public void update(DataEntity objet,String apresWhere)throws Exception {
		Connection conn=null;
		try{
			conn=Connecteur.getConnection(objet.findDataBaseKey());
			update(objet,conn,apresWhere);
		}
		catch(Exception ex){
			
			throw ex;
		}
		finally{
			if(conn!=null)
				conn.close();
		}
		
	}
	public void update(DataEntity objet)throws Exception {
			update(objet,"");
	}
	public void update(DataEntity objet,Connection conn,String apresWhere)throws Exception {
		List<DataEntity> liste=new ArrayList<DataEntity>();
		liste.add(objet);
		update(liste,conn,apresWhere);
	}
	public void update(DataEntity objet,Connection conn)throws Exception {
		List<DataEntity> liste=new ArrayList<DataEntity>();
		liste.add(objet);
		update(liste,conn,"");
	}
	public void delete(DataEntity objet)throws Exception{
		Connection connexion=null;
		try{
			connexion=Connecteur.getConnection(objet.findDataBaseKey());
			delete(objet, connexion);
		}
		catch(Exception ex){
			throw ex;
		}
		finally{
			if(connexion!=null)
				connexion.close();
		}
	}
	public void delete(DataEntity objet,Connection connexion)throws Exception{
		try{
			List<DataEntity> inter=new ArrayList<DataEntity>();
			inter.add(objet);
			delete(inter,connexion);
		}
		catch(Exception ex){
			throw ex;
		}
	}
	public void delete(List<DataEntity> objet,Connection connexion)throws Exception{
		PreparedStatement stmt=null;
		String condition=" "+objet.get(0).getPkName()+"=?";
		
		try{
			stmt=connexion.prepareStatement("delete from "+objet.get(0).findReference()+" where "+condition);
			int nbDelet=0;
			for(DataEntity b:objet)
			{
					Object valeur=null;
					try{
						valeur=b.getClass().getMethod("get"+setMaj(objet.get(0).getPkName()), null).invoke(b, null);
					}
					catch(NoSuchMethodException ex){
						try{
							valeur=b.getClass().getMethod("find"+setMaj(objet.get(0).getPkName()), null).invoke(b, null);
						}
						catch(Exception e){
							
						}
					}
					stmt.setObject(1, valeur);
				
				nbDelet+=stmt.executeUpdate();
			}
		}
		catch(Exception ex){
			throw ex;
		}
		finally{
			if(stmt!=null)
				stmt.close();
		}
	}
	public void delete(List<DataEntity> objet)throws Exception{
		Connection connexion=null;
		try{
			connexion=Connecteur.getConnection(objet.get(0).findDataBaseKey());
			delete(objet,connexion);
		}
		catch(Exception ex){
			
			throw ex;
		}
		finally{
			if(connexion!=null)
				connexion.close();
		}
	}
	public List<Map<String, Object>> excecuteQuery(String query,String dataBaseKey)throws Exception{
		Connection conn=null;
		try
		{
			conn=Connecteur.getConnection(dataBaseKey);
			
			return excecuteQuery(query, conn);
		}
		catch(SQLException ex){
			throw ex;
		}
		catch(NullPointerException ex){
			throw ex;
		}
		catch(Exception ex){
			
			throw ex;
				
		}
		finally{
			if(conn!=null)
				try
				{
					conn.close();
				} catch (SQLException e)
				{
					
				}
		}
	}
	public List<Map<String, Object>> excecuteQuery(String query,Connection connection)throws Exception{
		Statement stat=null;
		ResultSet resultat=null;
		try
		{
			stat=connection.createStatement();
			resultat=stat.executeQuery(query);
			ResultSetMetaData rsmd = resultat.getMetaData();
			
			List<Map<String, Object>> reponse=new ArrayList<Map<String, Object>>();
			
    	    int columnsCount = rsmd.getColumnCount();
    	    
    	    String[] columns=new String[columnsCount];
    	    
    	    for (int i = 0; i < columnsCount ; i++) {
    	        	columns[i]=rsmd.getColumnName(i+1);
    	    }
			
    	    while(resultat.next()){
    	    	Map<String, Object> map=new HashMap<String, Object>();
    	    	for(String col:columns)
    	    		map.put(col,resultat.getObject(col));
    	    	reponse.add(map);
    	    }
			return reponse;
		}
		catch(SQLException ex){
			throw ex;
		}
		catch(NullPointerException ex){
			throw ex;
		}
		catch(Exception ex){
			
			throw ex;
				
		}
		finally{
			if(resultat!=null)resultat.close();
			if(stat!=null)stat.close();
		}
	}
	public void executeUpdate(String query)throws Exception{
		Connection conn=null;
		try{
			executeUpdate(query,conn);
		}
		catch(NullPointerException ex){
			throw ex;
		}
		catch(Exception ex){
			throw ex;
		}
		finally{
			if(conn!=null)
				try
				{
					conn.close();
				} catch (SQLException e)
				{
					throw e;
				}
		}
	}
	public void executeUpdate(String query,Connection conn)throws Exception{
		try{
			conn.createStatement().executeUpdate(query);
		}
		catch(SQLException ex){
			throw ex;
		}
		catch(NullPointerException ex){
			throw ex;
		}
		catch(Exception ex){
			throw ex;
		}
	}
	public int executeUpdateWithAutoGeneratedKey(String query,String dataBaseKey)throws Exception{
		Connection conn=null;
		try{
			conn=Connecteur.getConnection(dataBaseKey);
			return executeUpdateWithAutoGeneratedKey(query,conn);
		}
		catch(NullPointerException e){
			throw e;
		}
		catch(Exception e){
			throw e;
		}
		finally{
			if(conn!=null)
				try
				{
					conn.close();
				} catch (SQLException e)
				{
					throw e;
				}
		}
	}
	public int executeUpdateWithAutoGeneratedKey(String query,Connection conn)throws Exception{
		PreparedStatement	pst=null;
		try{
			pst=conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			pst.execute();
			try(ResultSet generatedKey=pst.getGeneratedKeys()){
				if(generatedKey.next()){
					try{
						return generatedKey.getInt(0);
					}
					catch(Exception ex){
						throw ex;
					}
					
				}
			}
		}
		catch(SQLException ex){
			throw ex;
		}
		catch(NullPointerException ex){
			throw ex;
		}
		catch(Exception ex){
			throw ex;
		}
		finally{
			if(pst!=null)
				try
				{
					pst.close();
				} catch (SQLException e)
				{
					throw e;
				}
		}
		return 0;
	}
	

}
