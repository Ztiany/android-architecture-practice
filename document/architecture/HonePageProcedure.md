1 `public/v1/announce/count http/1.1` load announcement count

```json
{
  "status": 0,
  "msg": "success",
  "data": 0
}
```

2 `user/v1/client/disk/info` load user's cloud devices

```json
{
  "status": 0,
  "msg": "获取成功",
  "data": {
    "isTrial": false,
    "diskInfo": [
      {
        "id": 98,
        "username": "cSQFP1638867665",
        "exIp": "110.53.221.203",
        "exPort": "13908",
        "sn": "RK3948C1V52022188",
        "buyVipType": "RK_3399",
        "diskName": "R-云手机3",
        "validTime": 27330,
        "myOrderNum": "",
        "overdue": false,
        "exceptTime": "2022-02-08 15:08:31",
        "readme": "",
        "ctime": "2022-01-19 15:08:31",
        "phoneAuthStatus": 0,
        "screenShareStatus": 2,
        "authPhone": 0,
        "androidcardIp": "128.10.56.68",
        "androidcardPort": "9100",
        "areaId": 1,
        "areaName": "长沙",
        "areaStatus": 4,
        "room": "CS",
        "backupStatus": 3,
        "snapRecoveryStatus": 3,
        "cardId": 0,
        "partitionId": 0,
        "overStatus": 0,
        "rbdExtendSize": 0,
        "previewImageId": 0,
        "firstConnectCloudPhone": true,
        "businessPort": "30008"
      },
      {
        "id": 99,
        "username": "cSQFP1638867665",
        "exIp": "110.53.221.203",
        "exPort": "13918",
        "sn": "RK3948C1V52022938",
        "buyVipType": "RK_3399",
        "diskName": "R-云手机4",
        "validTime": 27330,
        "myOrderNum": "",
        "overdue": false,
        "exceptTime": "2022-02-08 15:08:31",
        "readme": "",
        "ctime": "2022-01-19 15:08:31",
        "phoneAuthStatus": 0,
        "screenShareStatus": 2,
        "authPhone": 0,
        "androidcardIp": "128.10.56.78",
        "androidcardPort": "9100",
        "areaId": 1,
        "areaName": "长沙",
        "areaStatus": 4,
        "room": "CS",
        "backupStatus": 3,
        "snapRecoveryStatus": 3,
        "cardId": 0,
        "partitionId": 0,
        "overStatus": 0,
        "rbdExtendSize": 0,
        "previewImageId": 0,
        "firstConnectCloudPhone": true,
        "businessPort": "30018"
      }
    ]
  }
}
```

3 `public/v1/custom/info` customer service info

```json
{
  "status": 0,
  "msg": "获取成功",
  "data": {
    "id": 1,
    "cusTel": "0755-86704358",
    "image": null,
    "imageName": "组3拷贝.png",
    "email": "",
    "forum": "",
    "microblog": "",
    "qqUrl": "https://wpa1.qq.com/GQtFIUOF?_type=wpa&qidian=true"
  }
}
```

4 `public/v1/announce/homePublish` announcement of home page.

```json
{
  "status": 0,
  "msg": "success",
  "data": {
    "list": []
  }
}
```

5 `wsi/v1/config/refresh?userCardId=98` load screenshot of cloud phone.

```json
{
  "status": 0,
  "msg": "success",
  "data": 0
}
```

6 `user/v1/client/login/detection` user agreement

```json
{
  "status": 0,
  "msg": "已授权",
  "data": 1
}
```

7 `upgrade/v1/client/upgrade/version?os=android` check new versions.

```json
{
  "status": 0,
  "msg": "success",
  "data": {
    "id": 7,
    "type": "android",
    "necessary": true,
    "newVersion": "20211229",
    "newVersionInfo": "新增高清摄像功能",
    "versionCode": 2129,
    "createTime": "2021-12-29 09:38:40",
    "isDelete": false,
    "fileId": 37,
    "downUrl": null,
    "fileSize": "61439496",
    "md5": "62c439d9421a69391fb58ad5af30710c"
  }
}
```
