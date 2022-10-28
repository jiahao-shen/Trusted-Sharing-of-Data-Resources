package com.trustchain.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.trustchain.model.APIInvoke;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trustchain.model.APIInvokeApplyInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface APIInvokeMapper extends BaseMapper<APIInvoke> {

    @Select("select t1.*, t2.name as api_name, t3.name as organization_name from api_invoke as t1 " +
            "left join api as t2 on t1.id=t2.id " +
            "left join organization as t3 on t2.organization=t3.id " +
            "where t1.applicant=#{applicant}")
    List<APIInvokeApplyInfo> getAPIInvokeApplyList(@Param("applicant") Long applicant,
                                                   @Param(Constants.WRAPPER) Wrapper wrapper);
}
