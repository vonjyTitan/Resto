package com.action;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mapping.Commande;
import com.mapping.ConstantEtat;
import com.mapping.MenuCommande;
import com.mapping.OptionObject;
import com.mapping.Table;
import com.rooteur.Action;

import dao.Connecteur;
import dao.DaoModele;
import utilitaire.UtileAffichage;
import utilitaire.Utilitaire;

public class CommandeAction extends Action {
	public void ajout(HttpServletRequest request,HttpServletResponse response)throws Exception{
		String [] menus=request.getParameterValues("menu");
		String [] quantite=request.getParameterValues("quantite");
		String [] remarque=request.getParameterValues("remarque");
		if(menus==null || menus.length==0){
			throw new Exception("Une commande doit avoir un menu au moins");
		}
		String nbClient=request.getParameter("client");
		int client=Integer.valueOf("0"+nbClient);
		if(client<=0){
			throw new Exception("le nombre de client doit etre positif");
		}
		String table=request.getParameter("idtable");
		int idtable=Integer.valueOf("0"+table);
		Table t=DaoModele.getInstance().findById(new Table(), idtable);
		if(t==null || t.getEtat()!=ConstantEtat.ETAT_OCCUPER_SANS_COMMANDE){
			throw new Exception("Table selectionnEe invalide");
		}
		Connection conn=null;
		try{
			conn=Connecteur.getConnection();
			conn.setAutoCommit(false);
			int idEnsemble=0;
			Commande comm=new Commande();
			List<java.util.Map<String, Object>> rep=DaoModele.getInstance().excecuteQuery("select case when max(idensemble) is null then 0 else max(idensemble)  end as idensemble from commande", conn);
			if(rep.size()==0)
				throw new Exception("Problem au niveau serveur");
			idEnsemble=Integer.valueOf(String.valueOf(rep.get(0).get("idensemble")) )+ 1;
			
			comm.setIdensemble(idEnsemble);
			comm.setLastensemble(idEnsemble);
			comm.setNbPersonnes(client);
			comm.setDaty(new java.sql.Date(UtileAffichage.getDateNow().getTime()));
			DaoModele.getInstance().save(comm, conn);
			
			MenuCommande mc=null;
			int taille=menus.length;
			List<MenuCommande> mcs=new ArrayList<MenuCommande>(taille);
			for(int i=0;i<taille;i++){
				mc=new MenuCommande();
				mc.setRemarque(remarque[i]);
				mc.setIdmenu(Integer.valueOf("0"+menus[i]));
				mc.setIdcommande(comm.getIdcommande());
				mc.setQuantite(Integer.valueOf("0"+quantite[i]));
				mcs.add(mc);
			}
			DaoModele.getInstance().save(mcs, conn);
			
			DaoModele.getInstance().executeUpdate("insert into commande_ensemble_table (idtable,idensemble) values("+idEnsemble+","+t.getIdtable()+")", conn);
			
			t.setEtat(ConstantEtat.ETAT_OCCUPER_AVEC_COMMANDE);
			DaoModele.getInstance().update(t, conn);
			conn.commit();
			goTo(request, response , "get","main.jsp?cible=commande/commande-liste&idensemble="+idEnsemble+"&idcommande="+comm.getIdcommande());
		}
		catch(Exception ex){
			if(conn!=null)
				conn.rollback();
			throw ex;
		}
		finally{
			if(conn!=null)
				conn.close();
		}
	}
}
