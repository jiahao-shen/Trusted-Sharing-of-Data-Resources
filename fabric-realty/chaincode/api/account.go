package api

import (
	"chaincode/model"
	"chaincode/pkg/utils"
	"encoding/json"
	"fmt"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

// QueryAccountList 查询账户列表
func QueryAccountList(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var accountList []model.Account
	results, err := utils.GetStateByPartialCompositeKeys(stub, model.AccountKey, args)
	if err != nil {
		return shim.Error(fmt.Sprintf("%s", err))
	}
	for _, v := range results {
		if v != nil {
			var account model.Account
			err := json.Unmarshal(v, &account)
			if err != nil {
				return shim.Error(fmt.Sprintf("QueryAccountList-反序列化出错: %s", err))
			}
			accountList = append(accountList, account)
		}
	}
	accountListByte, err := json.Marshal(accountList)
	if err != nil {
		return shim.Error(fmt.Sprintf("QueryAccountList-序列化出错: %s", err))
	}
	return shim.Success(accountListByte)
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
				return shim.Error(fmt.Sprintf("QueryAccountList-反序列化出错: %s", err))
			}
			organizationList = append(organizationList, org)
		}
	}

	organizationListByte, err := json.Marshal(organizationList)
	if err != nil {
		return shim.Error(fmt.Sprintf("QueryAccountList-序列化出错: %s", err))
	}
	return shim.Success(organizationListByte)
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
				return shim.Error(fmt.Sprintf("QueryAccountList-反序列化出错: %s", err))
			}
			for _, item := range org.DataItems {
				results, err := utils.GetStateByPartialCompositeKeys(stub, model.DataItemKey, []string{item})
				if err != nil {
					return shim.Error(fmt.Sprintf("%s", err))
				}
				var data model.DataItem
				err = json.Unmarshal(results[0], &data)
				if err != nil {
					return shim.Error(fmt.Sprintf("QueryAccountList-反序列化出错: %s", err))
				}
				dataitemList = append(dataitemList, data)
			}
		}
	}

	dataitemListByte, err := json.Marshal(dataitemList)
	if err != nil {
		return shim.Error(fmt.Sprintf("QueryAccountList-序列化出错: %s", err))
	}
	return shim.Success(dataitemListByte)
}

// 查询API列表
func QueryAPIList(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var apiList []model.API

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
				return shim.Error(fmt.Sprintf("QueryAccountList-反序列化出错: %s", err))
			}
			for _, item := range org.APIList {
				results, err := utils.GetStateByPartialCompositeKeys(stub, model.APIKey, []string{item})
				if err != nil {
					return shim.Error(fmt.Sprintf("%s", err))
				}
				var api model.API
				err = json.Unmarshal(results[0], &api)
				if err != nil {
					return shim.Error(fmt.Sprintf("QueryAccountList-反序列化出错: %s", err))
				}
				apiList = append(apiList, api)
			}
		}
	}

	apiListByte, err := json.Marshal(apiList)
	if err != nil {
		return shim.Error(fmt.Sprintf("QueryAccountList-序列化出错: %s", err))
	}
	return shim.Success(apiListByte)
}
