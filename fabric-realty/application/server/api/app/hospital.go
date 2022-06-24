package app

import (
	"application/pkg/app"
	"fmt"
	"net/http"

	"github.com/gin-gonic/gin"
)

type RegistrationRequest struct {
	Name     string `json:"name"`
	Identity string `json:"identity"`
}

func Registration(c *gin.Context) {
	appG := app.Gin{C: c}
	body := new(RegistrationRequest)

	if err := c.ShouldBind(body); err != nil {
		appG.Response(http.StatusBadRequest, "失败", fmt.Sprintf("参数出错:%s", err))
		return
	}

	if body.Name == "" || body.Identity == "" {
		appG.Response(http.StatusBadRequest, "失败", "参数不能为空")
		return
	}

	appG.Response(http.StatusOK, "成功", "挂号成功")
}
