package action;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.affichage.HTMLBuilder;
import com.mapping.Table;

import dao.Connecteur;
import dao.DaoModele;

public class TableAction extends Action {
	public void modifplace(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Connection conn=null;
		try{
			Table crit=new Table();
			
			conn=Connecteur.getConnection(crit.findDataBaseKey());
			conn.setAutoCommit(false);
			
			String []ids= request.getParameterValues("id");
			String []positionx= request.getParameterValues("posx");
			String []positiony= request.getParameterValues("posy");
			if(ids==null || ids.length==0){
				throw new Exception("Liste de table invalide");
			}
			List<Table> tables=new ArrayList<Table>();
			Table t=null;
			for(int i=0;i<ids.length;i++){
				t=DaoModele.getInstance().findById(crit, Integer.valueOf("0"+ids[i]));
				t.setPositionx(Integer.valueOf("0"+positionx[i]));
				t.setPositiony(Integer.valueOf("0"+positiony[i]));
				tables.add(t);
			}
			DaoModele.getInstance().update(tables,conn,"");
			conn.commit();
			goTo(request,response,"main.jsp?cible=configuration/table-gestion");
			return;
		}
		catch(Exception ex){
			if(conn!=null)
					conn.rollback();
			throw ex;
		}
		finally{
			if(conn!=null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
	public void modif(HttpServletRequest request,HttpServletResponse response) throws Exception{
		try{
			Table table=(Table) new HTMLBuilder(new Table(), request).getEntity();
			Table ancien=DaoModele.getInstance().findById(new Table(), table.getIdtable());
			if(ancien==null)
			{
				throw new Exception("Table introuvable");
			}
			table.setPositionx(ancien.getPositionx());
			table.setPositiony(ancien.getPositiony());
			table.setEtat(ancien.getEtat());
			
			DaoModele.getInstance().update(table);
			
			goTo(request,response,"main.jsp?cible=configuration/table-gestion");
			return;
		}
		catch(Exception ex){
			throw ex;
		}
	}
}
