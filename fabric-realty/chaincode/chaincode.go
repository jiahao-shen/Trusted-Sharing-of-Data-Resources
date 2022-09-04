package main

import (
	"chaincode/api"
	"fmt"
	"time"

	"github.com/docker/docker/libnetwork/ipams/null"
	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

type BlockChainRealEstate struct {
}

// Init 链码初始化
func (t *BlockChainRealEstate) Init(stub shim.ChaincodeStubInterface) pb.Response {
	fmt.Println("链码初始化")

	// //初始化默认数据
	// var accountIds = [6]string{
	// 	"5feceb66ffc8",
	// 	"6b86b273ff34",
	// 	"d4735e3a265e",
	// 	"4e07408562be",
	// 	"4b227777d4dd",
	// 	"ef2d127de37b",
	// }
	// var userNames = [6]string{"管理员", "①号业主", "②号业主", "③号业主", "④号业主", "⑤号业主"}
	// var balances = [6]float64{0, 5000000, 5000000, 5000000, 5000000, 5000000}
	// //初始化账号数据
	// for i, val := range accountIds {
	// 	account := &model.Account{
	// 		AccountId: val,
	// 		UserName:  userNames[i],
	// 		Balance:   balances[i],
	// 	}
	// 	// 写入账本
	// 	if err := utils.WriteLedger(account, stub, model.AccountKey, []string{val}); err != nil {
	// 		return shim.Error(fmt.Sprintf("%s", err))
	// 	}
	// }

	// return shim.Success(nil)

	// baseURL := "http://host.docker.internal:8000"
	// baseURL := "http://172.17.0.1:8000"

	// hospital := &model.Organization{
	// 	Name:         "北京第三人民医院",
	// 	ID:           "org-hospital",
	// 	Type:         "医院",
	// 	Introduction: "公立",
	// 	Superior:     "",
	// 	Subordinates: []string{},
	// 	DataList:     []string{},
	// 	APIList:      []string{"api-registration"},
	// 	Created:      time.Now().String(),
	// 	Hash:         "",
	// }

	// if err := utils.WriteLedger(hospital, stub, model.OrganizationKey, []string{"org-hospital"}); err != nil {
	// 	return shim.Error(fmt.Sprintf("保存失败: %s", err))
	// }

	// apiRegistration := &model.API{
	// 	ID:           "api-registration",
	// 	Name:         "挂号",
	// 	Introduction: "挂号",
	// 	Author:       "org-hospital",
	// 	URL:          baseURL + "/api/app/registration",
	// 	Type:         "POST",
	// 	Request:      "[{\"field\":\"name\",\"type\":\"string\"},{\"field\":\"identity\",\"type\":\"string\"}]",
	// 	Response:     "{}",
	// 	Version:      "v1.0.0",
	// 	Created:      time.Now().String(),
	// 	Hash:         "",
	// }

	// if err := utils.WriteLedger(apiRegistration, stub, model.APIKey, []string{"api-registration"}); err != nil {
	// 	return shim.Error(fmt.Sprintf("保存失败: %s", err))
	// }

	// pharmacy := &model.Organization{
	// 	ID:           "org-pharmacy",
	// 	Name:         "北京同仁堂药店",
	// 	Type:         "药店",
	// 	Introduction: "中西医药",
	// 	Superior:     "",
	// 	Subordinates: []string{},
	// 	DataList:     []string{},
	// 	APIList:      []string{"api-queryPrescription"},
	// 	Created:      time.Now().String(),
	// 	Hash:         "",
	// }

	// if err := utils.WriteLedger(pharmacy, stub, model.OrganizationKey, []string{"org-pharmacy"}); err != nil {
	// 	return shim.Error(fmt.Sprintf("保存失败: %s", err))
	// }

	// apiQueryPerscription := &model.API{
	// 	ID:           "api-queryPrescription",
	// 	Name:         "查询处方",
	// 	Introduction: "查询开药信息",
	// 	Author:       "org-pharmacy",
	// 	URL:          baseURL + "/api/app/queryPrescription",
	// 	Type:         "POST",
	// 	Request:      "[{\"field\":\"identity\",\"type\":\"string\"},{\"field\":\"medicine\",\"type\":\"string\"}]",
	// 	Response:     "{}",
	// 	Version:      "v1.0.0",
	// 	Created:      time.Now().String(),
	// 	Hash:         "",
	// }

	// if err := utils.WriteLedger(apiQueryPerscription, stub, model.APIKey, []string{"api-queryPrescription"}); err != nil {
	// 	return shim.Error(fmt.Sprintf("保存失败: %s", err))
	// }

	return shim.Success(nil)
}

// Invoke 实现Invoke接口调用智能合约
func (t *BlockChainRealEstate) Invoke(stub shim.ChaincodeStubInterface) pb.Response {
	funcName, args := stub.GetFunctionAndParameters()
	switch funcName {
	case "hello":
		return api.Hello(stub, args)
	case "net":
		return api.Net(stub, args)
	case "queryAccountList":
		return api.QueryAccountList(stub, args)
	case "createRealEstate":
		return api.CreateRealEstate(stub, args)
	case "queryRealEstateList":
		return api.QueryRealEstateList(stub, args)
	case "createSelling":
		return api.CreateSelling(stub, args)
	case "createSellingByBuy":
		return api.CreateSellingByBuy(stub, args)
	case "querySellingList":
		return api.QuerySellingList(stub, args)
	case "querySellingListByBuyer":
		return api.QuerySellingListByBuyer(stub, args)
	case "updateSelling":
		return api.UpdateSelling(stub, args)
	case "createDonating":
		return api.CreateDonating(stub, args)
	case "queryDonatingList":
		return api.QueryDonatingList(stub, args)
	case "queryDonatingListByGrantee":
		return api.QueryDonatingListByGrantee(stub, args)
	case "updateDonating":
		return api.UpdateDonating(stub, args)
	case "createOrganization":
		return api.CreateOrganization(stub, args)
	case "createData":
		return api.CreateData(stub, args)
	case "createAPI":
		return api.CreateAPI(stub, args)
	case "requestAPI":
		return api.RequestAPI(stub, args)
	case "queryOrganizationList":
		return api.QueryOrganizationList(stub, args)
	case "queryDataList":
		return api.QueryDataList(stub, args)
	case "queryAPIList":
		return api.QueryAPIList(stub, args)
	default:
		return shim.Error(fmt.Sprintf("没有该功能: %s", funcName))
	}
}

func main() {
	timeLocal, err := time.LoadLocation("Asia/Shanghai")
	if err != nil {
		panic(err)
	}
	time.Local = timeLocal
	err = shim.Start(new(BlockChainRealEstate))
	if err != nil {
		fmt.Printf("Error starting Simple chaincode: %s", err)
	}
}
