package app

import (
	"application/pkg/app"
	"fmt"
	"net/http"

	"github.com/gin-gonic/gin"
)

type QueryPrescriptionRequest struct {
	Identity string `json:"identity"`
	Medicine string `json:"medicine"`
}

func QueryPrescription(c *gin.Context) {
	appG := app.Gin{C: c}
	body := new(QueryPrescriptionRequest)

	if err := c.ShouldBind(body); err != nil {
		appG.Response(http.StatusBadRequest, "失败", fmt.Sprintf("参数出错:%s", err))
		return
	}

	if body.Identity == "" || body.Medicine == "" {
		appG.Response(http.StatusBadRequest, "失败", "参数不能为空")
		return
	}

	appG.Response(http.StatusOK, "成功", "买药记录")
}
