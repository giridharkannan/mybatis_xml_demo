package org.zm.mapper;

import org.zm.dao.DBShardDO.ShardKey;
import org.zm.model.DBShard;

public interface DBShardMapper {
	
	DBShard get(long id);
	
	void incAndGetFree(ShardKey key);
	
	DBShard getForUser(long user);

}
