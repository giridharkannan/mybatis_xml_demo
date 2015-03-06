package org.zm.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.zm.mapper.BeamMapper;
import org.zm.miki.BaseDO;
import org.zm.model.Beam;

public class BeamDO extends BaseDO {
	
	private static final BeamDO INSTANCE = new BeamDO();
	
	private BeamDO() {
		super(false, "beam");
	}
	
	public static BeamDO getInstance() {
		return INSTANCE;
	}
	
	public void create(Beam beam) {
		try(SqlSession session = getSession()) {
			session.getMapper(BeamMapper.class).create(getTableName(), beam);
		}
	}
	
	public Beam get(long id) {
		try(SqlSession session = getSession()) {
			return session.getMapper(BeamMapper.class).get(getTableName(), id);
		}
	}
	
	public List<Beam> getRange(long start, int limit) {
		try(SqlSession session = getSession()) {
			return session.getMapper(BeamMapper.class).getRange(getTableName(), start, limit);
		}
	}
	
	public void delete(long id) {
		try(SqlSession session = getSession()) {
			session.getMapper(BeamMapper.class).delete(getTableName(), id);
		}
	}

}
