package org.zm.miki;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.ibatis.session.SqlSession;
import org.zm.model.UserAccount;

public final class UserUtil {

	private static final UserUtil INSTANCE = new UserUtil();
	private UserUtil() {}
	
	public static UserUtil getInstance() {
		return INSTANCE;
	}
	
	private final String serial = "CREATE SEQUENCE uk_seq_{0}";
	
	private final String beam_table = "CREATE TABLE BEAM_{0} ("
			+ "id bigint DEFAULT nextval('uk_seq_{0}') primary KEY,"
			+ "ctime bigint NOT NULL,"
			+ "content text NOT NULL)";
			
	public void createTables(UserAccount acc) {
		Connection conn = null;
		try(SqlSession session = ConnectionManager.getInstance().getNewUserSession(acc.getShard(), false)) {
			conn = session.getConnection();
			createTables(acc.getId(), conn);
			conn.commit();
		} catch (SQLException e) {
			try {
				if(conn != null) { conn.rollback(); }
			} catch (SQLException ex) {
				ex.addSuppressed(e);
				throw new RuntimeException("Unable to rollback", ex);
			}
			throw new RuntimeException("Unable to create user tables "+acc.getUname(), e);
		}
	}
	
	private void createTables(long id, Connection conn) throws SQLException {
		String userId = Long.toString(id);
		try (Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(serial.replace("{0}", userId));
			stmt.executeUpdate(beam_table.replace("{0}", userId));
		}
	}
}
