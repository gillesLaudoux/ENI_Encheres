package fr.eni.projet.enchere.bll;

import java.time.LocalDateTime;
import java.util.List;

import fr.eni.projet.enchere.bo.Article;
import fr.eni.projet.enchere.bo.User;
import fr.eni.projet.enchere.dal.ArticleDAO;
import fr.eni.projet.enchere.dal.DALException;
import fr.eni.projet.enchere.dal.UserDAO;
import fr.eni.projet.enchere.dal.UserDAOFactory;

public class ArticleManager {
	
	private static ArticleManager instance;
	
	private static ArticleDAO dao;
	
	private ArticleManager() {
		dao = UserDAOFactory.getArticleDAO();
	};

	public static ArticleManager getInstance() {
		if(instance == null) {
			instance = new ArticleManager();
		}
		return instance;
	}
	
	
	public Article ajouterArticle(Article nouvelArticle) throws DALException, BLLException {
		
		BLLException ex = new BLLException();
		
		
		validationNo_article(nouvelArticle.getNo_article(), ex);
		validationDate_Debut(nouvelArticle.getDate_debut_encheres(), ex);
		validationDate_Fin(nouvelArticle.getDate_fin_encheres(), ex);
		validationNo_Categorie(nouvelArticle.getNo_categorie(), ex);
		validationDescription(nouvelArticle.getDescription(), ex);
		validationNom_Article(nouvelArticle.getNom_article(), ex);
		validationNo_utilisateur(nouvelArticle.getNo_utilisateur(), ex);

		Article article = dao.insertArticle(nouvelArticle);
		
		return article;
	
		
	}
	
	
	public List<Article> getListArticle(int no_categorie) throws BLLException {
		
		
		BLLException ex = new BLLException();
		
		
		
		if(ex.hasErreur()) {
			throw ex;
		}
		
		try {
			return dao.selectByCategorie(no_categorie);
		} catch (DALException e) {
			e.printStackTrace();
			ex.ajouterErreur(e);
			throw ex;
		}
		
		
	}
	
	
	private void validationNo_article(int no_article, BLLException ex) throws BLLException {
		if(no_article < 1) {
			ex.ajouterErreur(new ParameterException("Le numero d'article doit être un entier positif >= 1"));
		}
	}
	
	private void validationNom_Article(String nom_article, BLLException ex) {
		if(nom_article == null || nom_article.isEmpty()|| nom_article.length() > 30) {
			ex.ajouterErreur(new ParameterException("Le nom de l'article est obligatoire et doit avoir une longueur comprise entre 1 et 30"));
		}		
	}
	
	private void validationDescription(String description, BLLException ex) {
		if(description == null || description.isEmpty()|| description.length() > 300) {
			ex.ajouterErreur(new ParameterException("La description est obligatoire et doit avoir une longueur comprise entre 1 et 300"));
		}		
	}
	
	private void validationDate_Debut(LocalDateTime date_debut, BLLException ex) {
		if(date_debut == null) {
			ex.ajouterErreur(new ParameterException("La date de début est obligatoire "));
		}		
	}
	private void validationDate_Fin(LocalDateTime date_fin, BLLException ex) {
		if(date_fin == null) {
			ex.ajouterErreur(new ParameterException("La date de fin est obligatoire "));
		}		
	}
	
	
	private void validationNo_utilisateur(int no_user, BLLException ex) throws BLLException {
		if(no_user < 1) {
			ex.ajouterErreur(new ParameterException("Le numero d'article doit être un entier positif >= 1"));
		}
	}
	
	private void validationNo_Categorie(int no_categorie, BLLException ex) throws BLLException {
		if(no_categorie < 1) {
			ex.ajouterErreur(new ParameterException("Le numero d'article doit être un entier positif >= 1"));
		}
	}
	
	

	
	
	
	

}
