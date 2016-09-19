package com.affichage;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import com.mapping.DataEntity;

import dao.Connecteur;
import dao.DaoModele;
import utilitaire.SessionUtil;
import utilitaire.UtileAffichage;

public class PageFiche extends HTMLBuilder<DataEntity> {

	public PageFiche(DataEntity entity, HttpServletRequest request) throws Exception {
		super(entity, request);
	}
	public String getBody() throws Exception{
		Connection conn=null;
			try{
				conn=Connecteur.getConnection(entity.findDataBaseKey());
				entity=DaoModele.getInstance().findById(entity, Integer.valueOf(SessionUtil.getValForAttr(request, "id")),conn);
				if(entity==null)
					throw new Exception("Pas de resultat pour votre consultation");
				String reponse="";
				
				
				for(Champ field: fieldsAvalaible){
					if(!DaoModele.getInstance().isExisteChamp(entity.getReferenceForField(field.getField()), entity.findReference(), conn))
						continue;
					if(isNotVisible(field))
						continue;
					Object value=field.getMethodForChamp().invoke(entity, null);
					reponse+="<div class=\"form-group col-lg-12\">"
							+"<p class=\"col-lg-6\">"+field.getLibelle()+" : </p>";
					reponse+="<p class=\"col-lg-6\">"+UtileAffichage.getNonNullValue(value,field.getField().getType())+"</p>";
					reponse+="</div>";
				}
				
				return reponse;
		}
		catch(Exception ex){
			throw ex;
		}
		finally
		{
			if(conn!=null)
				conn.close();
		}		
	}
	public String getHTML() throws Exception{
		return getHTML("Informations general",8);
	}
	public String getHTML(String titre,int taille) throws Exception{
		String reponse="";
		reponse+=beginPanel(titre, taille);
		reponse+=getBody();
		reponse+=endPanel();
		return reponse;
	}

}
