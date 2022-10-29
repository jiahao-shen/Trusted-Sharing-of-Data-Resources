package com.trustchain.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.trustchain.model.Organization;
import com.trustchain.model.OrganizationInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface OrganizationMapper extends BaseMapper<Organization> {

    @Select("select t1.*, t2.name as superior_name from (select * from organization where id=#{id}) as t1 " +
            "left join organization as t2 on t1.superior=t2.id")
    OrganizationInfo getOrganizationInformation(@Param("id") Long id);

}
