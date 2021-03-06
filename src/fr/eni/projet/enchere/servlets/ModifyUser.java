package fr.eni.projet.enchere.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import fr.eni.projet.enchere.bll.BLLException;
import fr.eni.projet.enchere.bll.UserManager;
import fr.eni.projet.enchere.bo.User;
import fr.eni.projet.enchere.dal.DALException;

/**
 * Servlet implementation class ModifyUser
 */
@WebServlet(name = "ModifyUserServlet", urlPatterns = { "/ModifyUser" })
public class ModifyUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String USER = "user";
	private static final String PSEUDO = "pseudo"; 
	private static final String NAME = "name";
	private static final String SURNAME = "surname";
	private static final String STREET = "street";
	private static final String EMAIL = "email";
	private static final String TELEPHONE = "telephone";
	private static final String POSTAL_CODE = "postalCode";
	private static final String CITY = "city";
	private static final String PASSWORD = "password";
	private static final String PASSWORD_NEW = "passwordNew";
	private static final String PASSWORD_CURRENT = "passwordCurrent";
	private static final String PASSWORD_REWRITE = "passwordRewrite";
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ModifyUser() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		this.getServletContext().getRequestDispatcher("/WEB-INF/JSP/ModifyUser.jsp").forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		User userInSession = (User) session.getAttribute(USER);
		
		// Comme la jsp est divis?? en 2 formulaires, on vient v??rifier quel 
		// formulaire est rempli.
		
		if (request.getParameter(PSEUDO)!=null) {
			
			
			// Dans le cas o?? le premier formulaire est rempli, on a besoin
			// de r??cup??rer le num??ro de l'utilisateur, son password, son 
			// cr??tit et son statut administrateur pour pouvoir instancier un
			// user, afin de modifier la base de donn??
		
			int noUser = userInSession.getNo_utilisateur();
			String password = userInSession.getPassword();
			int credit = userInSession.getCredit();
			boolean administrateur = userInSession.isAdministrateur();
			
			String pseudo = request.getParameter(PSEUDO);
			String name = request.getParameter(NAME);
			String surname = request.getParameter(SURNAME);
			String street = request.getParameter(STREET);
			String mail = request.getParameter(EMAIL);
			String phone = request.getParameter(TELEPHONE);
			String postalCode = request.getParameter(POSTAL_CODE);
			String city = request.getParameter(CITY);
			
			User userModifie = new User(noUser, pseudo, name, surname, mail, phone, street, postalCode, city, password, credit, administrateur);
		
			try {
				UserManager.getInstance().mettreajourUser(userModifie);
			} catch (BLLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			userInSession.setPseudo(pseudo);
			userInSession.setName(name);
			userInSession.setNom(surname);
			userInSession.setStreet(street);
			userInSession.setMail(mail);
			userInSession.setPhone(phone);
			userInSession.setPostalCode(postalCode);
			userInSession.setCity(city);
			
			session.setAttribute("user", userInSession);
		
		} else {
			
			String passwordCurrent = request.getParameter(PASSWORD_CURRENT);
			String passwordNew = request.getParameter(PASSWORD_NEW);
			String passwordRewrite = request.getParameter(PASSWORD_REWRITE);
			
			String password = (String) session.getAttribute(PASSWORD);
			// On vient v??rifier que le bon password a ??t?? renseign??, (est-ce bien
			// l'utilisateur
			if (passwordCurrent.equals(password)) {
				
				// On vient v??rifier que les deux nouveaux password sont ??gaux
				if(passwordNew.equals(passwordRewrite)) {
					
					int noUser = userInSession.getNo_utilisateur();
					String pseudo = userInSession.getPseudo();
					String name = userInSession.getName();
					String surname = userInSession.getSurname();
					String street = userInSession.getStreet();
					String mail = userInSession.getMail();
					String phone = userInSession.getPhone();
					String postalCode = userInSession.getPostalCode();
					String city = userInSession.getCity();
					int credit = userInSession.getCredit();
					boolean administrateur = userInSession.isAdministrateur();
					
					User userModifie = new User(noUser, pseudo, name, surname, mail, phone, street, postalCode, city, password, credit, administrateur);
					
					try {
						UserManager.getInstance().mettreajourUser(userModifie);
						
						userInSession.setPassword(password);
						session.setAttribute("user", userInSession);
						
					} catch (BLLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
	
			}
			

		}
		request.getRequestDispatcher("/WEB-INF/JSP/profil.jsp").forward(request, response);
	}
}
