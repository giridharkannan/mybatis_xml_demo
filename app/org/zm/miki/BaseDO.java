package org.zm.miki;

import org.apache.ibatis.session.SqlSession;

import controllers.SecurityApp;
import controllers.SecurityApp.SessionUser;

public abstract class BaseDO {
	
	private final boolean isFramework;
	private final String tableName;
	
	public BaseDO(boolean isFramework, String tableName) {
		this.isFramework = isFramework;
		this.tableName = tableName;
	}
	
	protected String getTableName() {
		if(isFramework) { return tableName; }
		
		SessionUser user = SecurityApp.getCurrentUser();
		if(user == null) { throw new RuntimeException("User Context not set"); }
		
		return tableName+"_"+user.id;
	}
	
	protected SqlSession getSession(boolean autoCommit) {
		if(isFramework) {
			return ConnectionManager.getInstance().getFrameworkSession(autoCommit);
		} else {
			return ConnectionManager.getInstance().getUserSession(autoCommit);
		}
	}
	
	protected SqlSession getSession() {
		return getSession(true);
	}

}
