package dao;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import utilitaire.ConfigUtil;

import com.mchange.v2.c3p0.ComboPooledDataSource;


/**
 * Cette classe g�re le Pool de connexions 
 * 
 *
 */
public class Connecteur 
{
	private static Map<String, ComboPooledDataSource> dataBaseList=null;
	
	public static 	Map<String, DatabaseType> dataBaseTypeList=null;
	
	
	/**
	 * Donne un objet Connexion, vers une base de donn�e, de la pool de connexions. 
	 * @param dataBaseKey : nom de la base de donn�e
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection getConnection(String dataBaseKey) throws ClassNotFoundException, SQLException
	{
		if(dataBaseList==null)
			instance();
		return dataBaseList.get(dataBaseKey).getConnection();
	}
	
	
	/**
	 * Donne un objet Connexion, vers la base de donn�e "operator", de la pool de connexions.
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection getConnection() throws ClassNotFoundException, SQLException
	{
		if(dataBaseList==null)
			instance();
		return dataBaseList.get("resto").getConnection();
	}

	/**
	 * Necessit� du patron Singleton. getInstance s'assure qu'une seule et unique instance de connecteur est cr��. 
	 * Une nouvelle instance est cr��e UNIQUEMENT quand l'inctance actuelle est nulle  
	 * @return
	 */
	/**
	 * on cache le constructeur pour que l'acc�s se fasse uniquement par l'intermediaire de getInstance
	 */
	private static void instance() {
		
		if(dataBaseList==null)
		{
			
			ResourceBundle configuration=ConfigUtil.getBundle();
			dataBaseList=new HashMap<String,ComboPooledDataSource>();
			dataBaseTypeList=new HashMap<String,DatabaseType>();
			
			String[] databasekeyList=configuration.getString("bdd.key").split(";");
			String[] databasenameList=configuration.getString("bdd.name").split(";");
			String[] databaseList=configuration.getString("bdd.url").split(";");
			String[] typedatabaseliste=configuration.getString("bdd.typeBase").split(";");
			String[] userdatabaseliste=configuration.getString("bdd.utilisateur").split(";");
			String[] passeDatabaseList=configuration.getString("bdd.passe").split(";");
			String[] poolDatabaseList=configuration.getString("bdd.pool").split(";");
			
			for(int x=0;x<databasenameList.length;x++)
			{
				try
				{
					ComboPooledDataSource inter=new ComboPooledDataSource();
					if(typedatabaseliste[x].compareTo("postgresql")==0)
					{
						inter.setDriverClass("org.postgresql.Driver");
						dataBaseTypeList.put(databasekeyList[x], DatabaseType.PSQL);
					}				
					else if(typedatabaseliste[x].compareTo("oracle")==0)
					{
						inter.setDriverClass("oracle.jdbc.driver.OracleDriver");
						dataBaseTypeList.put(databasekeyList[x], DatabaseType.ORACLE);
					}
						
					else if(typedatabaseliste[x].compareTo("mysql")==0)
					{
						inter.setDriverClass("com.mysql.jdbc.Driver");
						dataBaseTypeList.put(databasekeyList[x], DatabaseType.MYSQL);
					}
					
					inter.setJdbcUrl( "jdbc:"+typedatabaseliste[x]+"://"+databaseList[x]+"/"+databasenameList[x] );
					inter.setUser(userdatabaseliste[x]);
					inter.setPassword(passeDatabaseList[x]);
					
					inter.setMinPoolSize(5);
					inter.setAcquireIncrement(5);
					inter.setMaxPoolSize(Integer.parseInt(poolDatabaseList[x]));
					
					dataBaseList.put(databasekeyList[x], inter);
				} 
				catch(PropertyVetoException ex)
				{
					dataBaseTypeList.put(databasekeyList[x], DatabaseType.ERROR);
					ex.printStackTrace();
				}
			}
		}
			
			
			
			
	}

	
	/**
	 * Systeme SGBDR connus
	 *
	 */
	public enum DatabaseType 
	{
		MYSQL,
		PSQL,
		ORACLE,
		ERROR;
	}
}
