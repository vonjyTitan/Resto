package com.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.affichage.HTMLBuilder;
import com.mapping.DataEntity;
import com.rooteur.Action;

import dao.DaoModele;
import utilitaire.SessionUtil;

public class CrudAction extends Action {
	public void insert(HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		String classe=SessionUtil.getValForAttr(request, "classenom");
		if(classe==null || classe.isEmpty() || classe.compareToIgnoreCase("null")==0)
			throw new Exception("Nom de Class obligatoire");
		Class cl=Class.forName(classe);
		if(cl==null)
			throw new Exception("Nom de Class invalide");
		DataEntity entity=null;
		try{
			entity=(DataEntity) cl.newInstance();
		}
		catch(Exception ex){
			throw new Exception("La class doit heritE de la class DataEntity!");
		}
		entity=new HTMLBuilder(entity, request).getEntity();
		if(!entity.isValide())
		{
			goTo(request,response,SessionUtil.getValForAttr(request, "refereur")+"&erreur=Champ invalide");
			return;
		}
		DaoModele.getInstance().save(entity);
		String cible=SessionUtil.getValForAttr(request, "cible");
		goTo(request,response,"main.jsp?cible="+cible);
	}
	public void update(HttpServletRequest request,HttpServletResponse response)throws Exception{
		String classe=SessionUtil.getValForAttr(request, "classenom");
		if(classe==null || classe.isEmpty() || classe.compareToIgnoreCase("null")==0)
			throw new Exception("Nom de Class obligatoire");
		Class cl=Class.forName(classe);
		if(cl==null)
			throw new Exception("Nom de Class invalide");
		DataEntity entity=null;
		try{
			entity=(DataEntity) cl.newInstance();
		}
		catch(Exception ex){
			throw new Exception("La class doit heritE de la class DataEntity!");
		}
		entity=new HTMLBuilder(entity, request).getEntity();
		if(!entity.isValide())
		{
			goTo(request,response,SessionUtil.getValForAttr(request, "refereur")+"&erreur=Champ invalide");
			return;
		}
		if(DaoModele.getInstance().findById(entity)==null)
		{
			throw new Exception("Objet introuvable");
		}
		DaoModele.getInstance().update(entity);
		String cible=SessionUtil.getValForAttr(request, "cible");
		goTo(request,response,"main.jsp?cible="+cible);
	}
	public void delete(HttpServletRequest request,HttpServletResponse response)throws Exception{
		String classe=SessionUtil.getValForAttr(request, "classenom");
		if(classe==null || classe.isEmpty() || classe.compareToIgnoreCase("null")==0)
			throw new Exception("Nom de Class obligatoire");
		Class cl=Class.forName(classe);
		if(cl==null)
			throw new Exception("Nom de Class invalide");
		DataEntity entity=null;
		try{
			entity=(DataEntity) cl.newInstance();
		}
		catch(Exception ex){
			throw new Exception("La class doit heritE de la class DataEntity!");
		}
		entity.setValueForField(entity.getFieldByName(entity.getPkName()), SessionUtil.getValForAttr(request, "id"));
		if(DaoModele.getInstance().findById(entity)==null)
		{
			throw new Exception("Objet introuvable");
		}
		DaoModele.getInstance().delete(entity);
		String cible=SessionUtil.getValForAttr(request, "cible");
		goTo(request,response,"main.jsp?cible="+cible);
	}
}
