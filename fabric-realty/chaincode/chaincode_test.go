package main

import (
	"bytes"
	"chaincode/model"
	"encoding/json"
	"fmt"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

func initTest(t *testing.T) *shim.MockStub {
	scc := new(BlockChainRealEstate)
	stub := shim.NewMockStub("ex01", scc)
	checkInit(t, stub, [][]byte{[]byte("init")})
	return stub
}

func checkInit(t *testing.T, stub *shim.MockStub, args [][]byte) {
	res := stub.MockInit("1", args)
	if res.Status != shim.OK {
		fmt.Println("Init failed", string(res.Message))
		t.FailNow()
	}
}

func checkInvoke(t *testing.T, stub *shim.MockStub, args [][]byte) pb.Response {
	res := stub.MockInvoke("1", args)
	if res.Status != shim.OK {
		fmt.Println("Invoke", args, "failed", string(res.Message))
		t.FailNow()
	}
	return res
}

// 测试链码初始化
func TestBlockChainRealEstate_Init(t *testing.T) {
	initTest(t)
}

func TestInitaliaze(t *testing.T) {
	stub := initTest(t)
	start := time.Now()
	fmt.Println(string(checkInvoke(t, stub, [][]byte{
		[]byte("queryOrganizationList"),
	}).Payload))
	end := time.Since(start)
	fmt.Println("执行时间:", end)
	start = time.Now()
	fmt.Println(string(checkInvoke(t, stub, [][]byte{
		[]byte("queryAPIList"),
		[]byte("org-hospital"),
	}).Payload))
	end = time.Since(start)
	fmt.Println("执行时间:", end)
	start = time.Now()
	fmt.Println(string(checkInvoke(t, stub, [][]byte{
		[]byte("queryAPIList"),
		[]byte("org-pharmacy"),
	}).Payload))
	end = time.Since(start)
	fmt.Println("执行时间:", end)
}

func Test_Network(t *testing.T) {
	stub := initTest(t)
	fmt.Println(string(checkInvoke(t, stub, [][]byte{
		[]byte("net"),
	}).Payload))
}

func Test_CreateOrganization(t *testing.T) {
	stub := initTest(t)

	org1ID := uuid.New().String()
	resp1 := checkInvoke(t, stub, [][]byte{
		[]byte("createOrganization"),
		[]byte(org1ID),
		[]byte("中华人民共和国国务院"),
		[]byte("government"),
		[]byte(""),
		[]byte(""),
		[]byte(time.Now().String()),
	})
	fmt.Println("创建机构:", string(resp1.Payload))

	org2ID := uuid.New().String()
	resp2 := checkInvoke(t, stub, [][]byte{
		[]byte("createOrganization"),
		[]byte(org2ID),
		[]byte("中华人民共和国国家卫生健康委员会"),
		[]byte("government"),
		[]byte(""),
		[]byte(org1ID),
		[]byte(time.Now().String()),
	})
	fmt.Println("创建机构:", string(resp2.Payload))

	data1ID := uuid.New().String()
	data1field, _ := json.Marshal([]model.Field{{Name: "Identity Card", Type: "string"}, {Name: "NATT Result", Type: "string"}, {Name: "Time", Type: "time.time"}})
	resp3 := checkInvoke(t, stub, [][]byte{
		[]byte("createData"),
		[]byte(data1ID),
		[]byte("核酸检测结果"),
		[]byte("新冠肺炎-核酸检测结果"),
		[]byte(org2ID),
		[]byte(data1field),
		[]byte("Medical"),
		[]byte("Shared"),
		[]byte("Public"),
		[]byte("v1.0.0"),
		[]byte("https://gjzwfw.www.gov.cn/fwmh/healthCode/indexNucleic.do"),
		[]byte(time.Now().String()),
	})
	fmt.Println("创建数据:", string(resp3.Payload))

	resp4 := checkInvoke(t, stub, [][]byte{
		[]byte("queryOrganizationList"),
	})
	fmt.Println("查询机构(全部):", string(resp4.Payload))

	resp5 := checkInvoke(t, stub, [][]byte{
		[]byte("queryOrganizationList"),
		[]byte(org1ID),
	})
	fmt.Println("查询机构(单个):", string(resp5.Payload))

	resp6 := checkInvoke(t, stub, [][]byte{
		[]byte("queryDataList"),
		[]byte(org2ID),
	})
	fmt.Println("查询数据:", string(resp6.Payload))

	org3ID := uuid.New().String()
	resp7 := checkInvoke(t, stub, [][]byte{
		[]byte("createOrganization"),
		[]byte(org3ID),
		[]byte("中国信息通信研究院"),
		[]byte("government"),
		[]byte(""),
		[]byte(org1ID),
		[]byte(time.Now().String()),
	})
	fmt.Println("创建机构:", string(resp7.Payload))

	api1ID := uuid.New().String()
	api1Request, _ := json.Marshal([]model.Field{{Name: "Identity Card", Type: "string"}})
	api1Response, _ := json.Marshal([]model.Field{{Name: "Status", Type: "integer"}, {Name: "Message", Type: "string"}, {Name: "Data", Type: "list"}})
	resp8 := checkInvoke(t, stub, [][]byte{
		[]byte("createAPI"),
		[]byte(api1ID),
		[]byte("获取行程数据"),
		[]byte("个人轨迹数据"),
		[]byte(org3ID),
		[]byte("http://127.0.0.1:8080/test"),
		[]byte("POST"),
		[]byte(api1Request),
		[]byte(api1Response),
		[]byte("v1.0.0"),
		[]byte(time.Now().String()),
	})
	fmt.Println("创建API:", string(resp8.Payload))

	resp9 := checkInvoke(t, stub, [][]byte{
		[]byte("queryAPIList"),
		[]byte(org3ID),
	})
	fmt.Println("查询API:", string(resp9.Payload))

	// log1ID := uuid.New().String()
	// resp10 := checkInvoke(t, stub, [][]byte{
	// 	[]byte("requestAPI"),
	// 	[]byte(log1ID),
	// 	[]byte(org1ID),
	// 	[]byte(api1ID),
	// 	[]byte(`{"identityCard":"320506199612168412"}`),
	// 	[]byte(time.Now().String()),
	// })
	// fmt.Println("调用API:", string(resp10.Payload))
}

// 测试获取账户信息
func Test_QueryAccountList(t *testing.T) {
	stub := initTest(t)
	fmt.Println(fmt.Sprintf("1、测试获取所有数据\n%s",
		string(checkInvoke(t, stub, [][]byte{
			[]byte("queryAccountList"),
		}).Payload)))
	fmt.Println(fmt.Sprintf("2、测试获取多个数据\n%s",
		string(checkInvoke(t, stub, [][]byte{
			[]byte("queryAccountList"),
			[]byte("5feceb66ffc8"),
			[]byte("6b86b273ff34"),
		}).Payload)))
	fmt.Println(fmt.Sprintf("3、测试获取单个数据\n%s",
		string(checkInvoke(t, stub, [][]byte{
			[]byte("queryAccountList"),
			[]byte("4e07408562be"),
		}).Payload)))
	fmt.Println(fmt.Sprintf("4、测试获取无效数据\n%s",
		string(checkInvoke(t, stub, [][]byte{
			[]byte("queryAccountList"),
			[]byte("0"),
		}).Payload)))
}

// 测试创建房地产
func Test_CreateRealEstate(t *testing.T) {
	stub := initTest(t)
	//成功
	checkInvoke(t, stub, [][]byte{
		[]byte("createRealEstate"),
		[]byte("5feceb66ffc8"), //操作人
		[]byte("6b86b273ff34"), //所有者
		[]byte("50"),           //总面积
		[]byte("30"),           //生活空间
	})
	//操作人权限不足
	checkInvoke(t, stub, [][]byte{
		[]byte("createRealEstate"),
		[]byte("6b86b273ff34"), //操作人
		[]byte("4e07408562be"), //所有者
		[]byte("50"),           //总面积
		[]byte("30"),           //生活空间
	})
	//操作人应为管理员且与所有人不能相同
	checkInvoke(t, stub, [][]byte{
		[]byte("createRealEstate"),
		[]byte("5feceb66ffc8"), //操作人
		[]byte("5feceb66ffc8"), //所有者
		[]byte("50"),           //总面积
		[]byte("30"),           //生活空间
	})
	//业主proprietor信息验证失败
	checkInvoke(t, stub, [][]byte{
		[]byte("createRealEstate"),
		[]byte("5feceb66ffc8"),    //操作人
		[]byte("6b86b273ff34555"), //所有者
		[]byte("50"),              //总面积
		[]byte("30"),              //生活空间
	})
	//参数个数不满足
	checkInvoke(t, stub, [][]byte{
		[]byte("createRealEstate"),
		[]byte("5feceb66ffc8"), //操作人
		[]byte("6b86b273ff34"), //所有者
		[]byte("50"),           //总面积
	})
	//参数格式转换出错
	checkInvoke(t, stub, [][]byte{
		[]byte("createRealEstate"),
		[]byte("5feceb66ffc8"), //操作人
		[]byte("6b86b273ff34"), //所有者
		[]byte("50f"),          //总面积
		[]byte("30"),           //生活空间
	})
}

//手动创建一些房地产
func checkCreateRealEstate(stub *shim.MockStub, t *testing.T) []model.RealEstate {
	var realEstateList []model.RealEstate
	var realEstate model.RealEstate
	//成功
	resp1 := checkInvoke(t, stub, [][]byte{
		[]byte("createRealEstate"),
		[]byte("5feceb66ffc8"), //操作人
		[]byte("6b86b273ff34"), //所有者
		[]byte("50"),           //总面积
		[]byte("30"),           //生活空间
	})
	resp2 := checkInvoke(t, stub, [][]byte{
		[]byte("createRealEstate"),
		[]byte("5feceb66ffc8"), //操作人
		[]byte("6b86b273ff34"), //所有者
		[]byte("80"),           //总面积
		[]byte("60.8"),         //生活空间
	})
	resp3 := checkInvoke(t, stub, [][]byte{
		[]byte("createRealEstate"),
		[]byte("5feceb66ffc8"), //操作人
		[]byte("4e07408562be"), //所有者
		[]byte("60"),           //总面积
		[]byte("40"),           //生活空间
	})
	resp4 := checkInvoke(t, stub, [][]byte{
		[]byte("createRealEstate"),
		[]byte("5feceb66ffc8"), //操作人
		[]byte("ef2d127de37b"), //所有者
		[]byte("80"),           //总面积
		[]byte("60"),           //生活空间
	})
	json.Unmarshal(bytes.NewBuffer(resp1.Payload).Bytes(), &realEstate)
	realEstateList = append(realEstateList, realEstate)
	json.Unmarshal(bytes.NewBuffer(resp2.Payload).Bytes(), &realEstate)
	realEstateList = append(realEstateList, realEstate)
	json.Unmarshal(bytes.NewBuffer(resp3.Payload).Bytes(), &realEstate)
	realEstateList = append(realEstateList, realEstate)
	json.Unmarshal(bytes.NewBuffer(resp4.Payload).Bytes(), &realEstate)
	realEstateList = append(realEstateList, realEstate)
	return realEstateList
}

// 测试获取房地产信息
func Test_QueryRealEstateList(t *testing.T) {
	stub := initTest(t)
	realEstateList := checkCreateRealEstate(stub, t)

	fmt.Println(fmt.Sprintf("1、测试获取所有数据\n%s",
		string(checkInvoke(t, stub, [][]byte{
			[]byte("queryRealEstateList"),
		}).Payload)))
	fmt.Println(fmt.Sprintf("2、测试获取指定数据\n%s",
		string(checkInvoke(t, stub, [][]byte{
			[]byte("queryRealEstateList"),
			[]byte(realEstateList[0].Proprietor),
			[]byte(realEstateList[0].RealEstateID),
		}).Payload)))
	fmt.Println(fmt.Sprintf("3、测试获取无效数据\n%s",
		string(checkInvoke(t, stub, [][]byte{
			[]byte("queryRealEstateList"),
			[]byte("0"),
		}).Payload)))
}

// 测试发起销售
func Test_CreateSelling(t *testing.T) {
	stub := initTest(t)
	realEstateList := checkCreateRealEstate(stub, t)
	//成功
	checkInvoke(t, stub, [][]byte{
		[]byte("createSelling"),
		[]byte(realEstateList[0].RealEstateID), //销售对象(正在出售的房地产RealEstateID)
		[]byte(realEstateList[0].Proprietor),   //卖家(卖家AccountId)
		[]byte("50"),                           //价格
		[]byte("30"),                           //智能合约的有效期(单位为天)
	})
	//验证销售对象objectOfSale属于卖家seller失败
	checkInvoke(t, stub, [][]byte{
		[]byte("createSelling"),
		[]byte(realEstateList[0].RealEstateID), //销售对象(正在出售的房地产RealEstateID)
		[]byte(realEstateList[2].Proprietor),   //卖家(卖家AccountId)
		[]byte("50"),                           //价格
		[]byte("30"),                           //智能合约的有效期(单位为天)
	})
	checkInvoke(t, stub, [][]byte{
		[]byte("createSelling"),
		[]byte("123"),                        //销售对象(正在出售的房地产RealEstateID)
		[]byte(realEstateList[0].Proprietor), //卖家(卖家AccountId)
		[]byte("50"),                         //价格
		[]byte("30"),                         //智能合约的有效期(单位为天)
	})
	//参数错误
	checkInvoke(t, stub, [][]byte{
		[]byte("createSelling"),
		[]byte(realEstateList[0].RealEstateID), //销售对象(正在出售的房地产RealEstateID)
		[]byte(realEstateList[0].Proprietor),   //卖家(卖家AccountId)
		[]byte("50"),                           //价格
	})
	checkInvoke(t, stub, [][]byte{
		[]byte("createSelling"),
		[]byte(""),                           //销售对象(正在出售的房地产RealEstateID)
		[]byte(realEstateList[0].Proprietor), //卖家(卖家AccountId)
		[]byte("50"),                         //价格
		[]byte("30"),                         //智能合约的有效期(单位为天)
	})
}

// 测试销售发起、购买等操作
func Test_QuerySellingList(t *testing.T) {
	stub := initTest(t)
	realEstateList := checkCreateRealEstate(stub, t)
	//先发起
	fmt.Println(fmt.Sprintf("发起\n%s", string(checkInvoke(t, stub, [][]byte{
		[]byte("createSelling"),
		[]byte(realEstateList[0].RealEstateID), //销售对象(正在出售的房地产RealEstateID)
		[]byte(realEstateList[0].Proprietor),   //卖家(卖家AccountId)
		[]byte("500000"),                       //价格
		[]byte("30"),                           //智能合约的有效期(单位为天)
	}).Payload)))
	fmt.Println(fmt.Sprintf("发起\n%s", string(checkInvoke(t, stub, [][]byte{
		[]byte("createSelling"),
		[]byte(realEstateList[2].RealEstateID), //销售对象(正在出售的房地产RealEstateID)
		[]byte(realEstateList[2].Proprietor),   //卖家(卖家AccountId)
		[]byte("600000"),                       //价格
		[]byte("40"),                           //智能合约的有效期(单位为天)
	}).Payload)))
	//查询成功
	fmt.Println(fmt.Sprintf("1、查询所有\n%s", string(checkInvoke(t, stub, [][]byte{
		[]byte("querySellingList"),
	}).Payload)))
	fmt.Println(fmt.Sprintf("2、查询指定%s\n%s", realEstateList[0].Proprietor, string(checkInvoke(t, stub, [][]byte{
		[]byte("querySellingList"),
		[]byte(realEstateList[0].Proprietor),
	}).Payload)))
	//购买
	fmt.Println(fmt.Sprintf("3、购买前先查询%s的账户余额\n%s", realEstateList[2].Proprietor, string(checkInvoke(t, stub, [][]byte{
		[]byte("queryAccountList"),
		[]byte(realEstateList[2].Proprietor),
	}).Payload)))
	fmt.Println(fmt.Sprintf("4、开始购买\n%s", string(checkInvoke(t, stub, [][]byte{
		[]byte("createSellingByBuy"),
		[]byte(realEstateList[0].RealEstateID), //销售对象(正在出售的房地产RealEstateID)
		[]byte(realEstateList[0].Proprietor),   //卖家(卖家AccountId)
		[]byte(realEstateList[2].Proprietor),   //买家(买家AccountId)
	}).Payload)))
	fmt.Println(fmt.Sprintf("》购买后再次查询%s的账户余额\n%s", realEstateList[2].Proprietor, string(checkInvoke(t, stub, [][]byte{
		[]byte("queryAccountList"),
		[]byte(realEstateList[2].Proprietor),
	}).Payload)))
	fmt.Println(fmt.Sprintf("》卖家查询购买成功信息\n%s", string(checkInvoke(t, stub, [][]byte{
		[]byte("querySellingList"),
		[]byte(realEstateList[0].Proprietor), //买家(买家AccountId)
	}).Payload)))
	fmt.Println(fmt.Sprintf("》买家查询购买成功信息\n%s", string(checkInvoke(t, stub, [][]byte{
		[]byte("querySellingListByBuyer"),
		[]byte(realEstateList[2].Proprietor), //买家(买家AccountId)
	}).Payload)))
	fmt.Println(fmt.Sprintf("》确认收款前卖家%s的账户余额\n%s", realEstateList[0].Proprietor, string(checkInvoke(t, stub, [][]byte{
		[]byte("queryAccountList"),
		[]byte(realEstateList[0].Proprietor),
	}).Payload)))
	fmt.Println(fmt.Sprintf("》确认收款前买家%s的账户余额\n%s", realEstateList[2].Proprietor, string(checkInvoke(t, stub, [][]byte{
		[]byte("queryAccountList"),
		[]byte(realEstateList[2].Proprietor),
	}).Payload)))
	fmt.Println(fmt.Sprintf("》确认收款前卖家%s的房产信息\n%s", realEstateList[0].Proprietor, string(checkInvoke(t, stub, [][]byte{
		[]byte("queryRealEstateList"),
		[]byte(realEstateList[0].Proprietor),
	}).Payload)))
	fmt.Println(fmt.Sprintf("》确认收款前买家%s的房产信息\n%s", realEstateList[2].Proprietor, string(checkInvoke(t, stub, [][]byte{
		[]byte("queryRealEstateList"),
		[]byte(realEstateList[2].Proprietor),
	}).Payload)))
	fmt.Println(fmt.Sprintf("》卖家确认收款\n%s", string(checkInvoke(t, stub, [][]byte{
		[]byte("updateSelling"),
		[]byte(realEstateList[0].RealEstateID), //销售对象(正在出售的房地产RealEstateID)
		[]byte(realEstateList[0].Proprietor),   //卖家(卖家AccountId)
		[]byte(realEstateList[2].Proprietor),   //买家(买家AccountId)
		[]byte("done"),                         //确认收款
	}).Payload)))
	//fmt.Println(fmt.Sprintf("》卖家取消收款\n%s", string(checkInvoke(t, stub, [][]byte{
	//	[]byte("updateSelling"),
	//	[]byte(realEstateList[0].RealEstateID), //销售对象(正在出售的房地产RealEstateID)
	//	[]byte(realEstateList[0].Proprietor),   //卖家(卖家AccountId)
	//	[]byte(realEstateList[2].Proprietor),   //买家(买家AccountId)
	//	[]byte("cancelled"),                    //取消收款
	//}).Payload)))
	fmt.Println(fmt.Sprintf("》确认收款后卖家%s的账户余额\n%s", realEstateList[0].Proprietor, string(checkInvoke(t, stub, [][]byte{
		[]byte("queryAccountList"),
		[]byte(realEstateList[0].Proprietor),
	}).Payload)))
	fmt.Println(fmt.Sprintf("》确认收款后买家%s的账户余额\n%s", realEstateList[2].Proprietor, string(checkInvoke(t, stub, [][]byte{
		[]byte("queryAccountList"),
		[]byte(realEstateList[2].Proprietor),
	}).Payload)))
	fmt.Println(fmt.Sprintf("》确认收款后卖家%s的房产信息\n%s", realEstateList[0].Proprietor, string(checkInvoke(t, stub, [][]byte{
		[]byte("queryRealEstateList"),
		[]byte(realEstateList[0].Proprietor),
	}).Payload)))
	fmt.Println(fmt.Sprintf("》确认收款后买家%s的房产信息\n%s", realEstateList[2].Proprietor, string(checkInvoke(t, stub, [][]byte{
		[]byte("queryRealEstateList"),
		[]byte(realEstateList[2].Proprietor),
	}).Payload)))
	fmt.Println(fmt.Sprintf("》卖家查询购买成功信息\n%s", string(checkInvoke(t, stub, [][]byte{
		[]byte("querySellingList"),
		[]byte(realEstateList[0].Proprietor), //买家(买家AccountId)
	}).Payload)))
	fmt.Println(fmt.Sprintf("》买家查询购买成功信息\n%s", string(checkInvoke(t, stub, [][]byte{
		[]byte("querySellingListByBuyer"),
		[]byte(realEstateList[2].Proprietor), //买家(买家AccountId)
	}).Payload)))
}

// 测试捐赠合约
func Test_Donating(t *testing.T) {
	stub := initTest(t)
	realEstateList := checkCreateRealEstate(stub, t)

	fmt.Println(fmt.Sprintf("获取房地产信息\n%s",
		string(checkInvoke(t, stub, [][]byte{
			[]byte("queryRealEstateList"),
		}).Payload)))
	//先发起
	fmt.Println(fmt.Sprintf("发起捐赠\n%s", string(checkInvoke(t, stub, [][]byte{
		[]byte("createDonating"),
		[]byte(realEstateList[0].RealEstateID),
		[]byte(realEstateList[0].Proprietor),
		[]byte(realEstateList[2].Proprietor),
	}).Payload)))

	fmt.Println(fmt.Sprintf("获取房地产信息\n%s",
		string(checkInvoke(t, stub, [][]byte{
			[]byte("queryRealEstateList"),
		}).Payload)))

	fmt.Println(fmt.Sprintf("1、查询所有\n%s", string(checkInvoke(t, stub, [][]byte{
		[]byte("queryDonatingList"),
	}).Payload)))
	fmt.Println(fmt.Sprintf("2、查询指定%s\n%s", realEstateList[0].Proprietor, string(checkInvoke(t, stub, [][]byte{
		[]byte("queryDonatingList"),
		[]byte(realEstateList[2].Proprietor),
	}).Payload)))
	fmt.Println(fmt.Sprintf("3、查询指定受赠%s\n%s", realEstateList[0].Proprietor, string(checkInvoke(t, stub, [][]byte{
		[]byte("queryDonatingListByGrantee"),
		[]byte(realEstateList[2].Proprietor),
	}).Payload)))

	//fmt.Println(fmt.Sprintf("4、接受受赠%s\n%s", realEstateList[0].Proprietor, string(checkInvoke(t, stub, [][]byte{
	//	[]byte("updateDonating"),
	//	[]byte(realEstateList[0].RealEstateID),
	//	[]byte(realEstateList[0].Proprietor),
	//	[]byte(realEstateList[2].Proprietor),
	//	[]byte("done"),
	//}).Payload)))
	fmt.Println(fmt.Sprintf("4、取消受赠%s\n%s", realEstateList[0].Proprietor, string(checkInvoke(t, stub, [][]byte{
		[]byte("updateDonating"),
		[]byte(realEstateList[0].RealEstateID),
		[]byte(realEstateList[0].Proprietor),
		[]byte(realEstateList[2].Proprietor),
		[]byte("cancelled"),
	}).Payload)))

	fmt.Println(fmt.Sprintf("获取房地产信息\n%s",
		string(checkInvoke(t, stub, [][]byte{
			[]byte("queryRealEstateList"),
		}).Payload)))
}
