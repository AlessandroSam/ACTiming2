package ac;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named; 

@Named
@RequestScoped
public class SessionListBean implements Serializable {

	private static final long serialVersionUID = -3647653563154027000L;
	private SessionDAO sessionDao;
	
	SessionListBean() {
		sessionDao = new SessionDAO();
	}
	
	/* Возвращает список сессий для отображения на странице */
	public List<Session> getSessionList() {
		return sessionDao.getAllSessions();
	}	
	
}
