package org.zm.dao;

import org.apache.ibatis.session.SqlSession;
import org.zm.mapper.DBShardMapper;
import org.zm.miki.BaseDO;
import org.zm.model.DBShard;

public class DBShardDO extends BaseDO {

	private static final DBShardDO INSTANCE = new DBShardDO();
	
	public static DBShardDO getInstance() {
		return INSTANCE;
	}
	
	private DBShardDO() {
		super(true, "DBShard");
	}
	
	public static class ShardKey {
		private long id;
		private ShardKey() {}
	}
	
	public DBShard get(long id) {
		try(SqlSession session = getSession()) {
			return session.getMapper(DBShardMapper.class).get(id);
		}
	}
	
	public long incAndGetFree() {
		try(SqlSession session = getSession()) {
			ShardKey key = new ShardKey();
			session.getMapper(DBShardMapper.class).incAndGetFree(key);
			return key.id;
		}
	}
	
	public DBShard getForUser(long user) {
		try(SqlSession session = getSession()) {
			return session.getMapper(DBShardMapper.class).getForUser(user);
		}
	}

}
