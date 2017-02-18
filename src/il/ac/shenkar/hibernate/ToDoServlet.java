package il.ac.shenkar.hibernate;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;


/**
 * Created by Arel on 05/01/2017.
 */
@WebServlet("/ToDoServlet")
public class ToDoServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {

    }

    HibernateToDoListDAO dao = HibernateToDoListDAO.getInstance();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher view = null;
        switch (request.getParameter("action")) {
            case "index":
                HttpSession session1 = request.getSession(true);
                if ((Boolean) session1.getAttribute("loggedIn")) {
                    System.out.println("logged in");
                }
                view = request.getRequestDispatcher("index.jsp");
                view.forward(request, response);
                request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
                break;

            case "delete":
                int id = Integer.parseInt(request.getParameter("id"));
                int userID = Integer.parseInt(request.getParameter("userID"));
                Item item = dao.getItemByID(id);
                dao.deleteItem(item);
                request.setAttribute("tasksList", dao.getAllItemsByUserId(userID));//TODO: fix getUserByUserName and add a call to dao
                request.getRequestDispatcher("/tasks.jsp").forward(request, response);
                break;
            case "tasks":
                int user_id = Integer.parseInt(request.getParameter("id"));
                request.setAttribute("tasksList", dao.getAllItemsByUserId(user_id));//TODO: fix getUserByUserName and add a call to dao
                request.getRequestDispatcher("/tasks.jsp").forward(request, response);
            case "log_out":
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (int i = 0; i < cookies.length; i++) {
                        if (cookies[i].getName().equals("logged_in")) {
                            cookies[i].setMaxAge(0);
                            cookies[i].setValue("");
                            response.addCookie(cookies[i]);
                        } else if (cookies[i].getName().equals("user_id")) {
                            cookies[i].setMaxAge(0);
                            cookies[i].setValue("");
                            response.addCookie(cookies[i]);
                        } else if (cookies[i].getName().equals("name")) {
                            cookies[i].setMaxAge(0);
                            cookies[i].setValue("");
                            response.addCookie(cookies[i]);
                        }
                    }
                    view = request.getRequestDispatcher("index.jsp");
                    view.forward(request, response);
                    request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
                    break;
                }

        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        switch(request.getParameter("action")){
            case "login":
                User user = new User(request.getParameter("email"),request.getParameter("password"));
                boolean exist = dao.checkIfUserExists(user);
                if(exist){
                    Cookie cookie = new Cookie("logged_in","true");
                    Cookie cookie1 = new Cookie("user_id",Integer.toString(dao.getUserIdByEmail(user)));
                    Cookie cookie2 = new Cookie("name",user.getUserName());
                    cookie.setMaxAge(60*60*24);
                    cookie1.setMaxAge(60*60*24);
                    cookie2.setMaxAge(60*60*24);
                    response.addCookie(cookie);
                    response.addCookie(cookie1);
                    response.addCookie(cookie2);
                    request.setAttribute("tasksList", dao.getAllItemsByUserId(Integer.parseInt(cookie1.getValue())));//TODO: fix getUserByUserName and add a call to dao
                    request.getRequestDispatcher("/tasks.jsp").forward(request, response);
                }
                else{
                    request.setAttribute("status", "don't exist");
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                }
                break;
            case "create":
                dao.createUser(new User(request.getParameter("email"),request.getParameter("password")));
                request.getRequestDispatcher("/tasks.jsp").forward(request, response);
                break;

            case "editTask":
                int userID = Integer.parseInt(request.getParameter("userID"));
                Item item = dao.getItemByID(Integer.parseInt(request.getParameter("id")));
                item.setStatus(request.getParameter("status"));
                item.setTitle(request.getParameter("title"));
                dao.updateItem(item);
                request.setAttribute("tasksList", dao.getAllItemsByUserId(userID));//TODO: fix getUserByUserName and add a call to dao
                request.getRequestDispatcher("/tasks.jsp").forward(request, response);
                break;
            case "addItem":
                String status = request.getParameter("status");
                String title = request.getParameter("title");
                int user_id = Integer.parseInt(request.getParameter("userID"));
                new Item(user_id,title,status);
                request.setAttribute("tasksList", dao.getAllItemsByUserId(user_id));//TODO: fix getUserByUserName and add a call to dao
                request.getRequestDispatcher("/tasks.jsp").forward(request, response);
                break;
        }

    }
}
