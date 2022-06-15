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
		return shim.Error(fmt.Sprintf("反序列化出错: %s", err))
	}
	org.DataItems = append(org.DataItems, dataID)

	if err := utils.WriteLedger(org, stub, model.OrganizationKey, []string{org.ID}); err != nil {
		return shim.Error(fmt.Sprintf("更新索引失败:%s", err))
	}

	dataItemByte, err := json.Marshal(dataItem)
	if err != nil {
		return shim.Error(fmt.Sprintf("序列化出错: %s", err))
	}

	return shim.Success(dataItemByte)
}

// 查询数据项目录
func QueryDataItemList(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var dataitemList []model.DataItem

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
			for _, item := range org.DataItems {
				results, err := utils.GetStateByPartialCompositeKeys(stub, model.DataItemKey, []string{item})
				if err != nil {
					return shim.Error(fmt.Sprintf("%s", err))
				}
				var data model.DataItem
				err = json.Unmarshal(results[0], &data)
				if err != nil {
					return shim.Error(fmt.Sprintf("反序列化出错: %s", err))
				}
				dataitemList = append(dataitemList, data)
			}
		}
	}

	dataitemListByte, err := json.Marshal(dataitemList)
	if err != nil {
		return shim.Error(fmt.Sprintf("序列化出错: %s", err))
	}

	return shim.Success(dataitemListByte)
}
