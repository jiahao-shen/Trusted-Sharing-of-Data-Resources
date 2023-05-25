import { v4 } from 'uuid'
import moment from 'moment'
import type { MockMethod } from 'vite-plugin-mock'

export default [
	{
		url: '/api/register/apply/list',
		method: 'get',
		statusCode: 200,
		response: () => {
			return [
        {
          serialNumber: v4(),
          name: 'test',
          method: 'POST',
          status: 'PROCESSED',
          applyTime: moment().format('YYYY-MM-DD HH:mm:ss')
        }
      ]
		},
	},
  {
		url: '/api/invoke/apply/list',
		method: 'get',
		statusCode: 200,
		response: () => {
			return [
        {
          serialNumber: v4(),
          apiName: 'test',
          organizationName: '北京航空航天大学计算机学院',
          status: 'PROCESSED',
          invokeMethod: 'WEB',
          applyTime: moment().format('YYYY-MM-DD HH:mm:ss')
        }
      ]
		},
	},
  {
		url: '/api/list/all',
		method: 'get',
		statusCode: 200,
		response: () => {
      let res: any[] = []
      for (let i = 0; i < 10; i++) {
        res.push({
          id: v4(),
          name: `API-${i}`,
          method: 'POST',
          category: '无',
          organizationName: '北京航空航天大学计算机学院',
          organizationType: 'EDUCATION',
          createdTime: moment().format('YYYY-MM-DD HH:mm:ss'),
        })
      }
      return res
		},
	},
  {
		url: '/api/invoke/approval/list',
		method: 'get',
		statusCode: 200,
		response: () => {
      return [
        {
          serialNumber: v4(),
          apiName: 'test',
          applicantName: 'admin',
          invokeMethod: 'WEB',
          status: 'REJECT',
          applyTime: moment().format('YYYY-MM-DD HH:mm:ss'),
        }
      ]
		},
	},
  {
    url: '/api/list/my',
		method: 'get',
		statusCode: 200,
		response: () => {
      let res: any[] = []
      for (let i = 0; i < 5; i++) {
        res.push({
          id: v4(),
          name: `API-${i}`,
          method: 'POST',
          category: '无',
          organizationName: '北京航空航天大学计算机学院',
          organizationType: 'EDUCATION',
          version: 'v1.0.0',
          createdTime: moment().format('YYYY-MM-DD HH:mm:ss'),
        })
      }
      return res
		},
  }
] as MockMethod[]
