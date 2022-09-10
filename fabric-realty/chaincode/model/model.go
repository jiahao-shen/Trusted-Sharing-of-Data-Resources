package model

// Account 账户，虚拟管理员和若干业主账号
type Account struct {
	AccountId string  `json:"accountId"` //账号ID
	UserName  string  `json:"userName"`  //账号名
	Balance   float64 `json:"balance"`   //余额
}

// RealEstate 房地产作为担保出售、捐赠或质押时Encumbrance为true，默认状态false。
// 仅当Encumbrance为false时，才可发起出售、捐赠或质押
// Proprietor和RealEstateID一起作为复合键,保证可以通过Proprietor查询到名下所有的房产信息
type RealEstate struct {
	RealEstateID string  `json:"realEstateId"` //房地产ID
	Proprietor   string  `json:"proprietor"`   //所有者(业主)(业主AccountId)
	Encumbrance  bool    `json:"encumbrance"`  //是否作为担保
	TotalArea    float64 `json:"totalArea"`    //总面积
	LivingSpace  float64 `json:"livingSpace"`  //生活空间
}

// Selling 销售要约
// 需要确定ObjectOfSale是否属于Seller
// 买家初始为空
// Seller和ObjectOfSale一起作为复合键,保证可以通过seller查询到名下所有发起的销售
type Selling struct {
	ObjectOfSale  string  `json:"objectOfSale"`  //销售对象(正在出售的房地产RealEstateID)
	Seller        string  `json:"seller"`        //发起销售人、卖家(卖家AccountId)
	Buyer         string  `json:"buyer"`         //参与销售人、买家(买家AccountId)
	Price         float64 `json:"price"`         //价格
	CreateTime    string  `json:"createTime"`    //创建时间
	SalePeriod    int     `json:"salePeriod"`    //智能合约的有效期(单位为天)
	SellingStatus string  `json:"sellingStatus"` //销售状态
}

// SellingStatusConstant 销售状态
var SellingStatusConstant = func() map[string]string {
	return map[string]string{
		"saleStart": "销售中", //正在销售状态,等待买家光顾
		"cancelled": "已取消", //被卖家取消销售或买家退款操作导致取消
		"expired":   "已过期", //销售期限到期
		"delivery":  "交付中", //买家买下并付款,处于等待卖家确认收款状态,如若卖家未能确认收款，买家可以取消并退款
		"done":      "完成",  //卖家确认接收资金，交易完成
	}
}

// SellingBuy 买家参与销售
// 销售对象不能是买家发起的
// Buyer和CreateTime作为复合键,保证可以通过buyer查询到名下所有参与的销售
type SellingBuy struct {
	Buyer      string  `json:"buyer"`      //参与销售人、买家(买家AccountId)
	CreateTime string  `json:"createTime"` //创建时间
	Selling    Selling `json:"selling"`    //销售对象
}

// Donating 捐赠要约
// 需要确定ObjectOfDonating是否属于Donor
// 需要指定受赠人Grantee，并等待受赠人同意接收
type Donating struct {
	ObjectOfDonating string `json:"objectOfDonating"` //捐赠对象(正在捐赠的房地产RealEstateID)
	Donor            string `json:"donor"`            //捐赠人(捐赠人AccountId)
	Grantee          string `json:"grantee"`          //受赠人(受赠人AccountId)
	CreateTime       string `json:"createTime"`       //创建时间
	DonatingStatus   string `json:"donatingStatus"`   //捐赠状态
}

// DonatingStatusConstant 捐赠状态
var DonatingStatusConstant = func() map[string]string {
	return map[string]string{
		"donatingStart": "捐赠中", //捐赠人发起捐赠合约，等待受赠人确认受赠
		"cancelled":     "已取消", //捐赠人在受赠人确认受赠之前取消捐赠或受赠人取消接收受赠
		"done":          "完成",  //受赠人确认接收，交易完成
	}
}

// DonatingGrantee 供受赠人查询的
type DonatingGrantee struct {
	Grantee    string   `json:"grantee"`    //受赠人(受赠人AccountId)
	CreateTime string   `json:"createTime"` //创建时间
	Donating   Donating `json:"donating"`   //捐赠对象
}

const (
	AccountKey         = "account-key"
	RealEstateKey      = "real-estate-key"
	SellingKey         = "selling-key"
	SellingBuyKey      = "selling-buy-key"
	DonatingKey        = "donating-key"
	DonatingGranteeKey = "donating-grantee-key"
	OrganizationKey    = "organization-key"
	DataKey            = "data-key"
	APIKey             = "api-key"
	APIRequestKey      = "api-request-key"
	FileOperationKey   = "file-operation-key"
)

type Organization struct {
	ID           string   `json:"id"`           // 机构ID
	Name         string   `json:"name"`         // 机构名称
	Type         string   `json:"type"`         // 机构类型
	Introduction string   `json:"introduction"` // 机构介绍
	Superior     string   `json:"superior"`     // 上级单位
	Subordinates []string `json:"subordinates"` // 下级单位
	DataList     []string `json:"datalist"`     // 数据项
	APIList      []string `json:"apilist"`      // API列表
	Created      string   `json:"created"`      // 创建时间
	Nodes        []string `json:"nodes"`        // 所属节点
	Hash         string   `json:"hash"`         // Hash值
}

type Data struct {
	ID           string   `json:"id"`           // 数据项ID
	Name         string   `json:"name"`         // 数据项名称
	Introduction string   `json:"introduction"` // 数据项介绍
	Author       string   `json:"author"`       // 所有者
	Field        string   `json:"type"`         // 数据类型
	Shared       string   `json:"shared"`       // 共享属性
	Type         string   `json:"resource"`     // 资源属性
	Classified   string   `json:"classified"`   // 涉密属性
	Version      string   `json:"version"`      // 版本号
	Location     string   `json:"location"`     // 存储路径
	Created      string   `json:"created"`      // 创建时间
	Nodes        []string `json:"nodes"`        // 所属节点
	Hash         string   `json:"hash"`         // Hash值
}

type API struct {
	ID           string   `json:"id"`           // API标识符
	Name         string   `json:"name"`         // API名称
	Introduction string   `json:"introduction"` // API介绍
	Author       string   `json:"author"`       // API所有者
	URL          string   `json:"url"`          // API请求地址
	Method       string   `json:"method"`       // 请求类型(OPTIONS, HEAD, GET, POST, PUT, DELETE, TRACE, CONNECT)
	Header       string   `json:"header"`       // 请求头
	HeaderType   string   `json:"headerType"`   // 请求头类型
	Request      string   `json:"request"`      // 请求体(JSON格式, 关键字和类型)
	RequestType  string   `json:"requestType"`  // 请求体类型(Form或JSON)
	Response     string   `json:"response"`     // 返回参数(JSON格式, 关键字和类型)
	ResponseType string   `json:"responseType"` // 返回体类型(Form或JSON)
	Version      string   `json:"version"`      // API版本信息
	Created      string   `json:"created"`      // 创建时间
	Nodes        []string `json:"nodes"`        // 所属节点
	Hash         string   `json:"hash"`         // Hash值
}

type RequestAPILog struct {
	ID      string `json:"id"`      // 日志标识符
	ReqID   string `json:"reqId"`   // 请求方标识符
	APIID   string `json:"apiId"`   // API标识符
	ReqHash string `json:"reqHash"` // 请求值Hash
	ResHash string `json:"resHash"` // 返回值Hash
	Time    string `json:"time"`    // 调用时间
}

// FileOperation 文件操作信息
type FileOperation struct {
	FilePath   string `json:"filePath"`   // 文件路径
	Type       string `json:"type"`       // 文件操作类型
	CreateTime string `json:"createTime"` // 创建时间
	Hash       string `json:"hash"`       // 前几个字段字符串拼接
}

type Field struct {
	Name string `json:"name"` // 字段名称
	Type string `json:"type"` // 字段类型
}
