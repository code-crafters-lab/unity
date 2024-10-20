# Authorization Server

## 获取授权服务器相关端点

http://127.0.0.1:9000/.well-known/oauth-authorization-server

# 1. 授权码流程
# 1.1 获取授权码 浏览器打开以下地址 redirect_url 不能使用 localhost,(用户必须登录)
http://localhost:9000/oauth2/authorize?client_id=demo&response_type=code&scope=openid&redirect_uri=http://127.0.0.1:8090/callback

###
# 1.2 根据授权码获取 token (client_secret_post)
POST http://localhost:9000/oauth2/token
Content-Type: application/x-www-form-urlencoded

client_id=demo&client_secret=123456&grant_type=authorization_code&code=QB-wYllFKzKU2_tjYBuc3VAwCOT3oxUNNXlLWunyDFqJCtOd87U7I6_103sw2xGap_WqwGcLZaMhfMPp55uyi7TgJVZJLerARGGXv-RElgnqUMoNXR391Gyni-4TR40L&redirect_uri=http://127.0.0.1:8090/callback
