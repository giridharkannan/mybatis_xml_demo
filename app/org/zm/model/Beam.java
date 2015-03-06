package org.zm.model;

import java.util.List;

import org.zm.dao.BeamDO;

public class Beam {
	
	private long id;
	private long ctime;
	private String content;
	
	private Beam() {}
	
	private Beam(String content) {
		this.ctime = System.currentTimeMillis();
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public long getCtime() {
		return ctime;
	}

	public String getContent() {
		return content;
	}
	
	public static Beam create(String content) {
		Beam b = new Beam(content);
		BeamDO.getInstance().create(b);
		return b;
	}
	
	public static void delete(long id) {
		BeamDO.getInstance().delete(id);
	}
	
	public static List<Beam> getRange(long start, int limit) {
		return BeamDO.getInstance().getRange(start, limit);
	}

}
