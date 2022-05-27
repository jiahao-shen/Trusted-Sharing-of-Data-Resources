<template>
    <div>
        <div v-for="fileOperation in fileOperationList" :key="fileOperation.hash">
            {{ fileOperation.filePath }}
            {{ fileOperation.type }}
            {{ fileOperation.createTime }}
            {{ fileOperation.hash }}
            <br />
        </div>
        <button @click="resort">排序</button>
    </div>
</template>

<script>
import { queryFileOperationList } from '@/api/fileManage'

export default {
    name: 'FileOperationList',
    data() {
        return {
            fileOperationList: [],
            sortMethod: 0,
        }
    },
    created() {
        // 发送文件操作记录查询请求
        queryFileOperationList().then(response => {
            if (response !== null) {
                this.fileOperationList = response
            }
        })
    },
    methods: {
        sortfromSmallestToLargest(foA, foB) {
            // 从小到大排序
            if (foA > foB) return 1
            else return -1
        },
        sortFromLargestToSmallest(foA, foB) {
            // 从大到小排序
            if (foA < foB) return 1
            else return -1
        },
        resort() {
            // 默认从大到小
            if (this.sortMethod == 0) {
                this.fileOperationList = this.fileOperationList.sort(this.sortfromSmallestToLargest)
                this.sortMethod = 1
            }
            else {
                this.fileOperationList = this.fileOperationList.sort(this.sortFromLargestToSmallest)
                this.sortMethod = 0
            }
            console.log(this.fileOperationList)
        }
    }
}
</script>