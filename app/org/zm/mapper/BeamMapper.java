package org.zm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.zm.model.Beam;

public interface BeamMapper {

	Beam get(@Param("table") String table, @Param("id") long id);
	
	List<Beam> getRange(
			@Param("table") String table,
			@Param("start") Long start,
			@Param("limit") Integer limit);
	
	void create(@Param("table") String table, @Param("beam") Beam beam);
		
	void delete(@Param("table") String table, @Param("id") long id);
}
