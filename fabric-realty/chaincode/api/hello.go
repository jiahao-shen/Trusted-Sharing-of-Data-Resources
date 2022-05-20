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

// Hello 测试
func Hello(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	err := utils.WriteLedger(map[string]interface{}{"msg": "hello"}, stub, "hello", []string{})
	if err != nil {
		return shim.Error(err.Error())
	}
	return shim.Success([]byte("hello world"))
}

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
			return shim.Error(fmt.Sprintf("QueryAccountList-反序列化出错: %s", err))
		}
		orgSuperior.Subordinates = append(orgSuperior.Subordinates, orgID)

		if err := utils.WriteLedger(orgSuperior, stub, model.OrganizationKey, []string{orgSuperiorID}); err != nil {
			return shim.Error(fmt.Sprintf("更新索引失败:%s", err))
		}
	}

	return shim.Success([]byte(orgID))
}

func CreateDataItem(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) != 8 {
		return shim.Error("参数个数不满足")
	}

	dataName := args[0]
	dataID := uuid.New().String()
	dataIntroduction := args[1]
	dataAuthor := args[2]
	dataType := args[3]
	dataShared := args[4]
	dataResource := args[5]
	dataClassified := args[6]
	dataLocation := args[7]

	if dataName == "" || dataAuthor == "" {
		return shim.Error("参数存在空值")
	}

	dataItem := &model.DataItem{
		Name:         dataName,
		ID:           dataID,
		Introduction: dataIntroduction,
		Author:       dataAuthor,
		Type:         dataType,
		Shared:       dataShared,
		Resource:     dataResource,
		Classified:   dataClassified,
		Version:      "1.0.0",
		Location:     dataLocation,
		Created:      time.Now(),
	}

	if err := utils.WriteLedger(dataItem, stub, model.DataItemKey, []string{dataID}); err != nil {
		return shim.Error(fmt.Sprintf("保存数据项失败:%s", err))
	}

	results, err := utils.GetStateByPartialCompositeKeys(stub, model.OrganizationKey, []string{dataAuthor})
	if err != nil {
		return shim.Error(fmt.Sprintf("不存在当前机构:%s", err))
	}

	var org model.Organization
	err = json.Unmarshal(results[0], &org)
	if err != nil {
		return shim.Error(fmt.Sprintf("QueryAccountList-反序列化出错: %s", err))
	}
	org.DataItems = append(org.DataItems, dataID)

	if err := utils.WriteLedger(org, stub, model.OrganizationKey, []string{org.ID}); err != nil {
		return shim.Error(fmt.Sprintf("更新索引失败:%s", err))
	}

	return shim.Success([]byte(dataID))
}
