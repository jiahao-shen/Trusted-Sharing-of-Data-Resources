package v1

import (
	bc "application/blockchain"
	"application/pkg/app"
	"bytes"
	"encoding/json"
	"fmt"
	"net/http"

	"github.com/gin-gonic/gin"
)

type CreateFileOperationRequestBody struct {
	FilePath string `json:"filePath"`
	Type     string `json:"type"`
}

// 查询时使用filePath作为主键
type QueryFileOperationListRequestBody struct {
	FilePath string `json:"filePath"`
}

/**
* 创建文件操作记录
**/
func CreateFileOperation(c *gin.Context) {
	appG := app.Gin{C: c}
	body := new(CreateFileOperationRequestBody)
	//解析Body参数
	if err := c.ShouldBind(body); err != nil {
		appG.Response(http.StatusBadRequest, "失败", fmt.Sprintf("参数出错%s", err.Error()))
		return
	}
	if body.FilePath == "" || body.Type == "" {
		appG.Response(http.StatusBadRequest, "失败", "参数不能为空")
		return
	}
	var bodyBytes [][]byte
	bodyBytes = append(bodyBytes, []byte(body.FilePath))
	bodyBytes = append(bodyBytes, []byte(body.Type))
	//调用智能合约
	resp, err := bc.ChannelExecute("createFileOperation", bodyBytes)
	if err != nil {
		appG.Response(http.StatusInternalServerError, "失败", err.Error())
		return
	}
	var data map[string]interface{}
	if err = json.Unmarshal(bytes.NewBuffer(resp.Payload).Bytes(), &data); err != nil {
		appG.Response(http.StatusInternalServerError, "失败", err.Error())
		return
	}
	appG.Response(http.StatusOK, "成功", data)
}

func QueryFileOperationList(c *gin.Context) {
	appG := app.Gin{C: c}
	body := new(QueryFileOperationListRequestBody)
	//解析Body参数
	if err := c.ShouldBind(body); err != nil {
		appG.Response(http.StatusBadRequest, "失败", fmt.Sprintf("参数出错%s", err.Error()))
		return
	}
	// if body.FilePath == "" {
	// 	appG.Response(http.StatusBadRequest, "失败", "参数不能为空")
	// 	return
	// }
	var bodyBytes [][]byte
	if body.FilePath != "" {
		bodyBytes = append(bodyBytes, []byte(body.FilePath))
	}
	//调用智能合约
	resp, err := bc.ChannelQuery("queryFileOperationList", bodyBytes)
	if err != nil {
		appG.Response(http.StatusInternalServerError, "失败", err.Error())
		return
	}
	// 反序列化json
	var data []map[string]interface{}
	if err = json.Unmarshal(bytes.NewBuffer(resp.Payload).Bytes(), &data); err != nil {
		appG.Response(http.StatusInternalServerError, "失败", err.Error())
		return
	}
	appG.Response(http.StatusOK, "成功", data)
}
