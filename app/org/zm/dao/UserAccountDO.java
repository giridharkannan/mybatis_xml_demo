package org.zm.dao;

import org.apache.ibatis.session.SqlSession;
import org.zm.mapper.UserAccountMapper;
import org.zm.miki.BaseDO;
import org.zm.miki.UserUtil;
import org.zm.model.UserAccount;

public class UserAccountDO extends BaseDO {

	private static final UserAccountDO INSTANCE = new UserAccountDO();
	
	private UserAccountDO() {
		super(true, "UserAccount");
	}
	
	public static UserAccountDO getInstance() {
		return INSTANCE;
	}
	
	public UserAccount get(long id) {
		try(SqlSession session = getSession()) {
			return session.getMapper(UserAccountMapper.class).get(id);
		}
	}
	
	public UserAccount getByUname(String uname) {
		try(SqlSession session = getSession()) {
			return session.getMapper(UserAccountMapper.class).getByUname(uname);
		}
	}
	
	public UserAccount create(UserAccount.Builder builder) {
		try(SqlSession session = getSession(false)) {
			long shard = DBShardDO.getInstance().incAndGetFree();
			UserAccount acc = builder.build(shard);
			session.getMapper(UserAccountMapper.class).create(acc);
			UserUtil.getInstance().createTables(acc);
			session.commit();
			return acc;
		}
	}
	
	public void changePasswd(UserAccount acc, String oldPwd, String newPwd) {
		if(acc.getPasswd().equals(oldPwd)) {
			throw new RuntimeException("Passwd mismatch");
		}
		
		try(SqlSession session = getSession()) {
			session.getMapper(UserAccountMapper.class).changePasswd(acc.getId(), newPwd);
		}
	}
	
	public void delete(long id) {
		try(SqlSession session = getSession()) {
			session.getMapper(UserAccountMapper.class).delete(id);
		}
	}

}
