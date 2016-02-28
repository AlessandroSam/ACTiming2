package ac;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

public class SessionDAO {
	/**
	 * Работа с БД. В данном случае используется PostgreSQL. 
	 */
	static {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("pgSQL JDBC driver class not found");
		}
	}
	
	private Connection getConnection() throws SQLException {
		String url = "jdbc:postgresql://localhost:5432/acbackend";
		String username = "postgres";
		String password = "123";		
		Connection conn = DriverManager.getConnection(url, username, password);
		return conn;	
	}
	
	private void closeConnection(Connection conn) {
		if (conn == null) return;
		try {
			conn.close();
		} catch (SQLException e) {}
	}
	
	public List<Session> getAllSessions() {
		/**
		 * Возвращает полный список сессий из БД.
		 */
		ArrayList<Session> sessionList = new ArrayList<Session>();
		Connection conn = null;
		String sql = "SELECT id, start_time, player_name, session_type, car, track FROM session";
		try {
			conn = getConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				Session session = new Session();
				session.setSessionId(rs.getLong("id"));
				session.setStartTime(rs.getDate("start_time"));
				session.setSessionType(rs.getInt("session_type"));
				session.setPlayerName(rs.getString("player_name"));
				session.setCar(rs.getString("car"));
				session.setTrack(rs.getString("track"));
				sessionList.add(session);
			}
		} catch (SQLException e) {
			System.out.println("Не удалось получить список сессий");
			System.out.println(e.getLocalizedMessage());
		} finally {
			closeConnection(conn);
		}
		return sessionList;
	}
	
	public List<Lap> getSessionLaps(long sessionId) {
		/**
		 * Возвращает список кругов для сессии с указанным sessionId.
		 */
		ArrayList<Lap> laps = new ArrayList<Lap>();
		Connection conn = null;
		String sql = "SELECT lap_number, lap_time, is_valid FROM lap"
				+ " WHERE session = ? ORDER BY lap_number ASC";
		try {
			conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, sessionId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Lap lap = new Lap();
				lap.setLapNumber(rs.getInt("lap_number"));
				lap.setLapTime(rs.getInt("lap_time"));
				lap.setSessionId(sessionId);
				lap.setValidFlag(rs.getBoolean("is_valid"));
				laps.add(lap);
			}
		} catch (SQLException e) {
			System.err.println("Cannot retrieve lap info for sessionId = " + sessionId);
			System.out.println(e.getLocalizedMessage());
		} finally {
			closeConnection(conn);
		}
		return laps;
	}
}
