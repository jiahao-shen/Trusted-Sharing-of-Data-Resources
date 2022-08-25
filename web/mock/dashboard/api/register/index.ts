import { v4 } from 'uuid'
import moment from 'moment'
import type { MockMethod } from 'vite-plugin-mock'

const randomChoice = (array: any[]) => {
  return array[Math.floor(Math.random() * array.length)]
}

const statusList = ['正在审核', '审核通过', '审核失败']

export default [
	{
		url: '/api/registerList',
		method: 'get',
		statusCode: 200,
		response: () => {
      let registerList = []
      for (let i = 65; i <= 90; ++i) {
        registerList.push({
          name: `API-${String.fromCharCode(i)}`,
          id: v4(),
          status: randomChoice(statusList),
          createdTime: moment().format('YYYY-MM-DD HH:mm:ss')
        })
      }
			return registerList
		},
	},
] as MockMethod[]
