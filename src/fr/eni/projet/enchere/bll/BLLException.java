package fr.eni.projet.enchere.bll;

import java.util.ArrayList;
import java.util.List;

public class BLLException extends Exception {

	private List<Exception> erreurs = new ArrayList<Exception>();
	
	public BLLException() {
		super();
	}
	
	public BLLException(String message) {
		super(message);
	}
	
	public BLLException(String message, Throwable exception) {
		super(message, exception);
	}
	
	/**
	 * Ajoute une exception � la liste d'exception.
	 * @param e L'exception � ajouter.
	 */
	public void ajouterErreur(Exception e) {
		erreurs.add(e);
	}
	
	/**
	 * Indique si la liste des erreurs est vide ou non
	 * @return true s'il y a des erreurs, false sinon.
	 */
	public boolean hasErreur() {
		return !erreurs.isEmpty();
	}

	//Méthodes
	@Override
	public String getMessage() {
		StringBuffer sb = new StringBuffer();
		for(Exception e : erreurs) {
			sb.append(e.getMessage()).append(System.lineSeparator());
		}		
		return sb.toString() ;
	}
}
