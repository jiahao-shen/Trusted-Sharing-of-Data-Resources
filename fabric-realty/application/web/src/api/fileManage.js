import request from '@/utils/request'

export function queryFileOperationList() {
    return request({
        url: '/queryFileOperationList',
        method: 'post',
    })
}
