package com.trustchain.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.trustchain.model.API;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trustchain.model.APIInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface APIMapper extends BaseMapper<API> {
    @Select("select t1.*, t2.name as organization_name, t2.type as organization_type " +
            "from api as t1 left join organization as t2 on t1.organization=t2.id ${ew.customSqlSegment}")
    List<APIInfo> getAllAPIList(@Param(Constants.WRAPPER) Wrapper wrapper);

}
