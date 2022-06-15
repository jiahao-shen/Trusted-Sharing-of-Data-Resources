package api

import (
	"chaincode/model"
	"chaincode/pkg/utils"
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

// 创建机构
func CreateOrganization(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) != 4 {
		return shim.Error("参数个数不满足")
	}

	orgName := args[0]
	orgID := uuid.New().String()
	orgType := args[1]
	orgIntroduction := args[2]
	orgSuperiorID := args[3]

	if orgName == "" || orgType == "" {
		return shim.Error("参数存在空值")
	}

	org := &model.Organization{
		Name:         orgName,
		ID:           orgID,
		Type:         orgType,
		Introduction: orgIntroduction,
		Superior:     orgSuperiorID,
		Subordinates: []string{},
		DataItems:    []string{},
		Created:      time.Now(),
	}

	if err := utils.WriteLedger(org, stub, model.OrganizationKey, []string{orgID}); err != nil {
		return shim.Error(fmt.Sprintf("保存机构失败:%s", err))
	}

	if orgSuperiorID != "" {
		results, err := utils.GetStateByPartialCompositeKeys(stub, model.OrganizationKey, []string{orgSuperiorID})
		if err != nil {
			return shim.Error(fmt.Sprintf("不存在当前机构:%s", err))
		}
		var orgSuperior model.Organization
		err = json.Unmarshal(results[0], &orgSuperior)
		if err != nil {
			return shim.Error(fmt.Sprintf("反序列化出错: %s", err))
		}
		orgSuperior.Subordinates = append(orgSuperior.Subordinates, orgID)

		if err := utils.WriteLedger(orgSuperior, stub, model.OrganizationKey, []string{orgSuperiorID}); err != nil {
			return shim.Error(fmt.Sprintf("更新索引失败:%s", err))
		}
	}

	orgByte, err := json.Marshal(org)
	if err != nil {
		return shim.Error(fmt.Sprintf("反序列化出错: %s", err))
	}

	return shim.Success([]byte(orgByte))
}

// 查询机构列表
func QueryOrganizationList(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var organizationList []model.Organization

	// TODO(获取args)

	results, err := utils.GetStateByPartialCompositeKeys(stub, model.OrganizationKey, args)
	if err != nil {
		return shim.Error(fmt.Sprintf("%s", err))
	}

	for _, val := range results {
		if val != nil {
			var org model.Organization
			err := json.Unmarshal(val, &org)
			if err != nil {
				return shim.Error(fmt.Sprintf("反序列化出错: %s", err))
			}
			organizationList = append(organizationList, org)
		}
	}

	organizationListByte, err := json.Marshal(organizationList)
	if err != nil {
		return shim.Error(fmt.Sprintf("序列化出错: %s", err))
	}

	return shim.Success(organizationListByte)
}
