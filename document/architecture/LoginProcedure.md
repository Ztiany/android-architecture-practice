1 `user/v1/client/login http/1.1` login to get token

post

```json
{
  "diskName": "MI 6",
  "password": "Kl2NKoLUjKLn04irlJfdQQ==\n",
  "phone": "19999999999",
  "imei": "865873037406729",
  "client": "1",
  "uuid": "865873037406729",
  "mac": "EC:D0:9F:87:E0:AB",
  "ipAddr": "192.168.253.105"
}
```

result

```json
{
  "status": 0,
  "msg": "登录成功",
  "data": {
    "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyYW5kb20iOiI4NjM0OSIsImV4cCI6MTY0NDQ2NDMzNywidXNlcm5hbWUiOiJjU1FGUDE2Mzg4Njc2NjUifQ.RgzQMtdyuh3cn557itBOostGkByertPo9HajsswbmzM",
    "nextCloudIp": "vclusters.imwork.net:1138",
    "nextLocalNetworkIp": "vclusters.imwork.net:1138",
    "username": "cSQFP1638867665",
    "id": 189,
    "couponList": []
  }
}
```

After token taken:

1 connect
WebSocket: `ws://demo.ysj.vclusters.com/push?deviceId=865873037406729&username=cSQFP1638867665`[must]

2 `user/v1/client/personal_info` load person info[no need for now]

```json
{
  "status": 0,
  "msg": "获取信息成功",
  "data": {
    "info": {
      "id": 189,
      "surfaceName": "19999999999",
      "sex": "",
      "birthday": "2000-01-01 00:00:00",
      "bloodType": "A型",
      "censusRegister": "广东省珠海市",
      "workAddr": "",
      "phone": "19999999999",
      "email": "",
      "education": "",
      "job": ""
    }
  }
}
```
