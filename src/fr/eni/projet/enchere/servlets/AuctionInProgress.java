package fr.eni.projet.enchere.servlets;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import fr.eni.projet.enchere.bll.ArticleManager;
import fr.eni.projet.enchere.bll.AuctionManager;
import fr.eni.projet.enchere.bll.BLLException;
import fr.eni.projet.enchere.bo.Article;
import fr.eni.projet.enchere.bo.Auction;
import fr.eni.projet.enchere.bo.User;
import sun.java2d.d3d.D3DDrawImage;
import sun.util.resources.fr.LocaleNames_fr;

/**
 * Servlet implementation class ArticleBuying
 */
@WebServlet("/achatArticle")
public class AuctionInProgress extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String Details = "/WEB-INF/JSP/AuctionInProgress.jsp";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Auction enchereAuction = null;
		Article article = null;
		System.out.println("salut");
		
		int numeroArticle;

		String StringArticle = request.getParameter("noArticle");
		
		HttpSession session = request.getSession();
		if (StringArticle==null) {
			
			 numeroArticle = (int) session.getAttribute("no_article");
		} else {
			
			 numeroArticle = Integer.parseInt(StringArticle);

		}
		

		session.setAttribute("no_article", numeroArticle );
		
		
		
		System.out.println(numeroArticle);
		try {
			article = ArticleManager.getInstance().selectArticle(numeroArticle);
		} catch (BLLException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		request.setAttribute("articledesciption", article.getDescription());
		request.setAttribute("articlesStartPrice", article.getPriceStart());
// request.setAttribute("articlemeilleuroffre", article.getPriceSold());

		LocalDateTime date = article.getDateEndAuction();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.FRENCH);
		String formatDateTime = date.format(formatter);

		request.setAttribute("articlefinenchere", formatDateTime);

		try {
			enchereAuction = AuctionManager.getInstance().selectbynoarticle(numeroArticle);
		} catch (BLLException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int enchereprix;

		System.out.println(enchereAuction.getMontant());
// Integer encherepourArticle = Integer.valueOf(enchereAuction.getMontant());

		request.setAttribute("encherefaite", enchereAuction.getMontant());

		request.setAttribute("articleretraitname", article.getNameArticle());
		request.setAttribute("articleretraitstreet", article.getSeller().getStreet());
		request.setAttribute("articleretraitpostal", article.getSeller().getPostalCode());
		request.setAttribute("articleretraitCity", article.getSeller().getCity());
		request.setAttribute("articleretraitname", article.getSeller().getName());

		request.setAttribute("articleretraitpseudo", article.getSeller().getPseudo());

//pour la servlet afficher profil

		request.setAttribute("articleencherepseudo", article.getSeller().getPseudo());

		request.getSession().setAttribute("no_article", numeroArticle);

		String articlebuyerpseudo;

		if (article.getBuyer().getPseudo() == "") {
			articlebuyerpseudo = "pas d'enchere en cours";
			request.setAttribute("articlebuyerpseudo", article.getBuyer().getPseudo());
		} else {

			articlebuyerpseudo = article.getBuyer().getPseudo();
		}

		request.setAttribute("articlebuyerpseudo", articlebuyerpseudo);

		System.out.println(request.getAttribute("articledesciption"));
		RequestDispatcher dispatcher = request.getRequestDispatcher(Details);
		dispatcher.forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Auction enchereAuction = null;

		User useractuel = (User) request.getSession().getAttribute("user");

		int numero_article = (int) request.getSession().getAttribute("no_article");

		try {
			enchereAuction = AuctionManager.getInstance().selectbynoarticle(numero_article);
		} catch (BLLException e) {

			e.printStackTrace();
		}

		int no_user_actuel = useractuel.getNo_utilisateur();

		int nouveloffre = Integer.valueOf(request.getParameter("offre"));
		System.out.println(nouveloffre);

// int credituser =useractuel.getCredit();

		int credituser = useractuel.getCredit();

		if (nouveloffre > enchereAuction.getMontant() && credituser >= nouveloffre) {

			System.out.println(enchereAuction);
			if (enchereAuction.getMontant() == 0) {

				enchereAuction = new Auction(LocalDateTime.now(), nouveloffre, numero_article, no_user_actuel);
				System.out.println(enchereAuction);
				try {
					AuctionManager.getInstance().ajouterArticle(enchereAuction);
				} catch (BLLException e) {
// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				enchereAuction = new Auction(enchereAuction.getNo_enchere(), LocalDateTime.now(), nouveloffre,
						numero_article, no_user_actuel);

				try {
					AuctionManager.getInstance().modfierAuction(enchereAuction);
				} catch (BLLException e) {
// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			this.doGet(request, response);

		} else {

			this.doGet(request, response);

		}

	}

}
