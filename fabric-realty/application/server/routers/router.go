package routers

import (
	"application/api/app"
	v1 "application/api/v1"
	"net/http"

	"github.com/gin-gonic/gin"
)

// InitRouter 初始化路由信息
func InitRouter() *gin.Engine {
	r := gin.Default()

	// 合约API
	apiV1 := r.Group("/api/v1")
	{
		apiV1.GET("/hello", v1.Hello)
		apiV1.POST("/queryAccountList", v1.QueryAccountList)
		apiV1.POST("/createRealEstate", v1.CreateRealEstate)
		apiV1.POST("/queryRealEstateList", v1.QueryRealEstateList)
		apiV1.POST("/createSelling", v1.CreateSelling)
		apiV1.POST("/createSellingByBuy", v1.CreateSellingByBuy)
		apiV1.POST("/querySellingList", v1.QuerySellingList)
		apiV1.POST("/querySellingListByBuyer", v1.QuerySellingListByBuyer)
		apiV1.POST("/updateSelling", v1.UpdateSelling)
		apiV1.POST("/createDonating", v1.CreateDonating)
		apiV1.POST("/queryDonatingList", v1.QueryDonatingList)
		apiV1.POST("/queryDonatingListByGrantee", v1.QueryDonatingListByGrantee)
		apiV1.POST("/updateDonating", v1.UpdateDonating)
		// 创建文件操作记录
		apiV1.POST("/createFileOperation", v1.CreateFileOperation)
		// 查询文件操作记录
		apiV1.POST("/queryFileOperationList", v1.QueryFileOperationList)
		// 创建机构
		apiV1.POST("/createOrganization", v1.CreateOrganization)
		// 查询机构
		apiV1.POST("/queryOrganizationList", v1.QueryOrganizationList)
		// 创建数据项
		apiV1.POST("/createData", v1.CreateData)
		// 查询数据项
		apiV1.POST("/queryDataList", v1.QueryDataList)
		// 创建API
		apiV1.POST("/createAPI", v1.CreateAPI)
		// 调用API
		apiV1.POST("/requestAPI", v1.RequestAPI)
		// 查询API
		apiV1.POST("/queryAPIList", v1.QueryAPIList)
	}

	// 业务API
	apiAPP := r.Group("api/app")
	{
		apiAPP.POST("/case1", app.Case1)
		apiAPP.POST("/registration", app.Registration)
		apiAPP.POST("/queryPrescription", app.QueryPrescription)
	}

	// 静态文件路由
	r.StaticFS("/web", http.Dir("./dist/"))
	return r
}
