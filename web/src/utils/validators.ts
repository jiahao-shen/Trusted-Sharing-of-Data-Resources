export const required = (message?: string) => {
  return {
    required: true,
    message: (message || '') + '不能为空'
  }
}