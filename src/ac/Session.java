/**
 * 
 */
package ac;

import java.io.Serializable;
import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named; 

@Named("session")
@RequestScoped
public class Session implements Serializable {

	private static final long serialVersionUID = 1L;
	private final int AC_UNKNOWN     = -1;
	private final int AC_PRACTICE    = 0;
	private final int AC_QUALIFY     = 1;
	private final int AC_RACE        = 2;
	private final int AC_HOTLAP      = 3;
	private final int AC_TIME_ATTACK = 4;
	private final int AC_DRIFT       = 5;
	private final int AC_DRAG        = 6;
	
	private long id;
	private long sessionId;
	private Date startTime;
	private int sessionType;
	private String car;
	private String track;
	private String trackConfig;  // отсутствует в структуре sim_info, поэтому (пока) не используется
	private String playerName;
	private String sessionDescr;
	
	public Session() {
		sessionId = -1;
		startTime = null;
		sessionType = -2;
		car = "";
		track = "";
		trackConfig = "";
		playerName = "";	
		sessionDescr = setSessionDescr();	
	}
	
	private String setSessionDescr() {
		switch(sessionType) {
		case AC_UNKNOWN: return "неизвестно";
		case AC_PRACTICE: return "практика";
		case AC_QUALIFY: return "квалификация";
		case AC_RACE: return "гонка";
		case AC_HOTLAP: return "хотлап"; 
		case AC_TIME_ATTACK: return "time attack"; // особый и весьма странный режим в AC
		case AC_DRIFT: return "дрифт";
		case AC_DRAG: return "драг";
		default: return "";
		}
	}
	
	public Session(int sessionId, Date startTime, int sessionType, String car, String track, String trackConfig, String playerName) {
		this.setSessionId(sessionId);
		this.setStartTime(startTime);
		this.setCar(car);
		this.setTrack(track);
		this.trackConfig = trackConfig;
		this.setPlayerName(playerName);
		sessionDescr = setSessionDescr();
	}

	public long getSessionId() {
		return sessionId;
	}

	public void setSessionId(long id) {
		this.sessionId = id;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public int getSessionType() {
		return sessionType;
	}

	public void setSessionType(int sessionType) {
		this.sessionType = sessionType;
		sessionDescr = setSessionDescr();
	}

	public String getTrack() {
		return track;
	}

	public void setTrack(String track) {
		this.track = track;
	}

	public String getCar() {
		return car;
	}

	public void setCar(String car) {
		this.car = car;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	public String getSessionDescr() {
		return sessionDescr;
	}
	
	
	
}
