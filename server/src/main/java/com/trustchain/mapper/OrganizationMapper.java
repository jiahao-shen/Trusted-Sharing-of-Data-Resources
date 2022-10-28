package com.trustchain.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.trustchain.model.Organization;
import com.trustchain.model.OrganizationInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface OrganizationMapper extends BaseMapper<Organization> {

    @Select("select t1.*, t2.name as superior_name from organization as t1 " +
            "left join organization as t2 on t1.superior=t2.id where t1.id=#{id}")
    OrganizationInfo getOrganizationInformation(@Param("id") Long id);

}
