package org.zm.miki;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.IOUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.zm.dao.DBShardDO;
import org.zm.model.DBShard;

import controllers.SecurityApp;
import controllers.SecurityApp.SessionUser;
import play.api.Mode;
import scala.Enumeration.Value;

public class ConnectionManager {

	private static final ConnectionManager INSTANCE = new ConnectionManager();
	
	private SqlSessionFactory factory;
	private final ConcurrentHashMap<Long, SqlSessionFactory> fa_map;
	
	private String getEnv() {
		Value v = play.api.Play.current().mode();
		if(Mode.Dev() == v) {
			//default
			return "dev";
		} else if(Mode.Prod() == v) {
			return "prod";
		} else if(Mode.Test() == v) {
			return "test";
		} else {
			throw new RuntimeException("Unknown mode "+v);
		}
	}
	
	private ConnectionManager() {
		try {
			factory = new SqlSessionFactoryBuilder().build(Resources
					.getResourceAsStream("mybatis-config.xml"), getEnv());
			fa_map = new ConcurrentHashMap<Long, SqlSessionFactory>();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static ConnectionManager getInstance() {
		return INSTANCE;
	}
	
	SqlSession getFrameworkSession(boolean autoCommit) {
		return factory.openSession(autoCommit);
	}
	
	SqlSession getFrameworkSession() {
		return getFrameworkSession(true);
	}
	
	SqlSession getUserSession() {
		return getUserSession(true);
	}
	
	SqlSession getUserSession(boolean autoCommit) {
		SessionUser user = SecurityApp.getCurrentUser();
		if(user == null) {
			throw new RuntimeException("User Context not set");
		}
		
		return getNewUserSession(user.shard, autoCommit);
	}
	
	SqlSession getNewUserSession(long shard, boolean autoCommit) {
		DBShard shardObj = DBShardDO.getInstance().get(shard);
		SqlSessionFactory fa = fa_map.get(shard);

		if (fa == null) { // factory not yet associated
			try {
				fa = createSSFactory(shardObj);
				fa_map.put(shard, fa);
			} catch (IOException e) {
				throw new RuntimeException(
						"Exception while getting connection", e);
			}
		}
		return fa.openSession(autoCommit);
	}
	
	private synchronized SqlSessionFactory createSSFactory(DBShard shard) throws IOException {
		SqlSessionFactory fa = null;

		fa = fa_map.get(shard.getId());
		if (fa != null) {
			return fa;
		}

		InputStream in = getConfig(shard);
		fa = new SqlSessionFactoryBuilder().build(in);
		return fa;
	}
	
	private InputStream getConfig(DBShard shard) throws IOException {
		InputStream in = Resources
				.getResourceAsStream("mybatis-user-config.xml");
		
		String conf = IOUtils.toString(in);
		conf = MessageFormat.format(conf, shard.getDriver(), shard.getUrl(), shard.getUsr(), shard.getPasswd());
		return IOUtils.toInputStream(conf);
	}
}
