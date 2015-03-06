package org.zm.mapper;

import org.apache.ibatis.annotations.Param;
import org.zm.model.UserAccount;

public interface UserAccountMapper {
	
	UserAccount get(long id);
	
	UserAccount getByUname(String uname);
	
	void create(UserAccount acc);
	
	void changePasswd(@Param("id") long id, @Param("passwd") String newPasswd);
	
	void delete(long id);

}
