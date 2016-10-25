package com.action;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.affichage.HTMLBuilder;
import com.mapping.AnnulationMenu;
import com.mapping.Commande;
import com.mapping.ConstantEtat;
import com.mapping.LivraisonMenu;
import com.mapping.MenuCommande;
import com.mapping.OptionObject;
import com.mapping.Table;
import com.mapping.Utilisateur;
import com.rooteur.Action;
import com.service.CommandeService;

import dao.Connecteur;
import dao.DaoModele;
import utilitaire.SessionUtil;
import utilitaire.UtileAffichage;
import utilitaire.Utilitaire;

public class CommandeAction extends Action {
	synchronized public void ajout(HttpServletRequest request,HttpServletResponse response)throws Exception{
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
		if(t==null || t.getEtat()!=ConstantEtat.ETAT_TABLE_OCCUPER_SANS_COMMANDE){
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
			
			comm.setEtat(1);
			comm.setIdensemble(idEnsemble);
			comm.setLastensemble(idEnsemble);
			comm.setNbPersonnes(client);
			comm.setDaty(new java.sql.Date(UtileAffichage.getDateNow().getTime()));
			DaoModele.getInstance().save(comm, conn);
			
			MenuCommande mc=null;
			int taille=menus.length;
			List<MenuCommande> mcs=new ArrayList<MenuCommande>(taille);
			for(int i=0;i<taille;i++){
				int idmenu=Integer.valueOf("0"+menus[i]);
				int nombre=Integer.valueOf("0"+quantite[i]);
				if(idmenu==0 && nombre==0)
					continue;
				if(idmenu==0 && nombre>0)
					throw new Exception("Selection de menu obligatoire si le quantitE est positif");
				mc=new MenuCommande();
				mc.setRemarque(remarque[i]);
				mc.setIdmenu(Integer.valueOf("0"+menus[i]));
				mc.setIdcommande(comm.getIdcommande());
				mc.setQuantite(Integer.valueOf("0"+quantite[i]));
				mcs.add(mc);
			}
			DaoModele.getInstance().save(mcs, conn);
			
			DaoModele.getInstance().executeUpdate("insert into commande_ensemble_table (idensemble,idtable) values("+idEnsemble+","+t.getIdtable()+")", conn);
			
			t.setEtat(ConstantEtat.ETAT_TABLE_OCCUPER_AVEC_COMMANDE);
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
	public void annuler(HttpServletRequest request,HttpServletResponse response)throws Exception {
		Connection conn=null;
		try{
			conn=Connecteur.getConnection(); 
			conn.setAutoCommit(false);
			
			AnnulationMenu annulation=new AnnulationMenu();
			annulation.setIdcommande_menu(Integer.valueOf("0"+SessionUtil.getValForAttr(request, "id")));
			annulation.setCause(SessionUtil.getValForAttr(request, "motif"));
			annulation.setDate(new java.sql.Date(UtileAffichage.getDateNow().getTime()));
			annulation.setQuantite(Integer.valueOf("0"+SessionUtil.getValForAttr(request, "quantite")));
			
			Utilisateur user=(Utilisateur) request.getSession().getAttribute("utilisateur");
			annulation.setIdutilisateur(user.getIdutilisateur());
			CommandeService.getInstance().annulerMenu(annulation, conn);
			
			conn.commit();
		}
		catch(Exception ex){
			if(conn!=null)
				conn.rollback();
			throw ex;
		}
		finally {
			if(conn!=null)
				conn.close();
		}
		goTo(request, response , "get","main.jsp?cible=commande/commande-fiche&id="+SessionUtil.getValForAttr(request, "idensemble"));
	}
	public void livrer(HttpServletRequest request,HttpServletResponse response)throws Exception {
		Connection conn=null;
		try{
			conn=Connecteur.getConnection(); 
			conn.setAutoCommit(false);
			
			LivraisonMenu livraison=new LivraisonMenu();
			livraison.setIdmenu_commande(Integer.valueOf("0"+SessionUtil.getValForAttr(request, "id")));
			livraison.setDate(new java.sql.Date(UtileAffichage.getDateNow().getTime()));
			livraison.setQuantite(Integer.valueOf(SessionUtil.getValForAttr(request, "quantite")));
			
			DaoModele.getInstance().save(livraison, conn);
			
			CommandeService.getInstance().calculeQuantiteLivrer(Integer.valueOf("0"+SessionUtil.getValForAttr(request, "id")), conn);
			conn.commit();
		}
		catch(Exception ex){
			if(conn!=null)
				conn.rollback();
			throw ex;
		}
		finally {
			if(conn!=null)
				conn.close();
		}
		goTo(request, response , "get","main.jsp?cible=commande/commande-fiche&id="+SessionUtil.getValForAttr(request, "idensemble"));
	}
	public void ajoutMenus(HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		Connection conn=null;
		try{
			conn=Connecteur.getConnection(); 
			conn.setAutoCommit(false);
			
			String [] menus=request.getParameterValues("menu");
			String [] quantite=request.getParameterValues("quantite");
			String [] remarque=request.getParameterValues("remarque");
			if(menus==null || menus.length==0){
				throw new Exception("Nombre invalide");
			}
			int idensemble=Integer.valueOf("0"+SessionUtil.getValForAttr(request, "idensemble"));
			MenuCommande mc=null;
			int taille=menus.length;
			for(int i=0;i<taille;i++){
				int idmenu=Integer.valueOf("0"+menus[i]);
				int nombre=Integer.valueOf("0"+quantite[i]);
				if(idmenu==0 && nombre==0)
					continue;
				if(idmenu==0 && nombre>0)
					throw new Exception("Selection de menu obligatoire si le quantitE est positif");
				mc=new MenuCommande();
				mc.setRemarque(remarque[i]);
				mc.setIdmenu(Integer.valueOf("0"+menus[i]));
				mc.setQuantite(Integer.valueOf("0"+quantite[i]));
				CommandeService.getInstance().addMenuForCommande(mc,idensemble);
			}
			conn.commit();
		}catch(Exception ex){
			if(conn!=null)
				conn.rollback();
			throw ex;
		}
		finally {
			if(conn!=null)
				conn.close();
		}
		goTo(request, response , "get","main.jsp?cible=commande/commande-fiche&id="+SessionUtil.getValForAttr(request, "idensemble"));
	}
	public void rajoutMenu(HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		MenuCommande mc=new HTMLBuilder<MenuCommande>(new MenuCommande(), request).getValue();
		int q=mc.getQuantite();
		mc=DaoModele.getInstance().findById(mc, mc.getIdcommande_menu());
		if(mc==null){
			throw new Exception("Menu introuvable");
		}
		mc.setQuantite(mc.getQuantite()+q);
		DaoModele.getInstance().update(mc);
		goTo(request, response , "get","main.jsp?cible=commande/commande-fiche&id="+SessionUtil.getValForAttr(request, "idensemble"));
	}
	public void annulerTous(HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		CommandeService.getInstance().annulerComande(Integer.valueOf("0"+SessionUtil.getValForAttr(request, "idensemble")),SessionUtil.getValForAttr(request, "motif"));
		goTo(request, response , "get","main.jsp?cible=commande/commande-fiche&id="+SessionUtil.getValForAttr(request, "idensemble"));
	}
	public void jumellage(HttpServletRequest request,HttpServletResponse response)throws Exception{
		String idensembles[]=request.getParameterValues("id");
		if(idensembles==null || idensembles.length==0)
			throw new Exception("Aucune ensemble selectionnee");
		if( idensembles.length==1)
			throw new Exception("Element selectionnee obligatoirement superieurs a 1 en nombre");
		int taille=idensembles.length;
		int[] idens=new int[taille];
		for(int i=0;i<taille;i++){
			idens[i]=Integer.valueOf("0"+idensembles[i]);
		}
		CommandeService.getInstance().jumellerCommande(idens);
		goTo(request, response , "get","main.jsp?cible=commande/commande-fiche&id="+idens[0]);
	}
}
