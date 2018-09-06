## Create Users
curl -X "POST" "https://api.omartvietnam.com/_internal/users/owners" \
     -H 'Content-Type: application/json; charset=utf-8' \
     -d $'{
  "name": "noname",
  "password": "202cb962ac59075b964b07152d234b70",
  "phoneNumber": "01635064144"
}'

## Update User
curl -X "PUT" "https://api.omartvietnam.com/_internal/users/owners/349e59449776429ea597a3f48ed9d7ab" \
     -H 'Content-Type: application/json; charset=utf-8' \
     -d $'{
  "name": "Long C",
  "password": "359ce06fa7199d202f5132314cac2852",
  "phoneNumber": "0932069434"
}'

## Check account
# #### ENUM:
#   UsernameNotFound(0),
#   UsernameNotFoundCanSignUp(1),
#   UserNotActivatedException(2);
curl "https://api.omartvietnam.com/v1/accounts/0906953809/check"

## Check account only and don't create
# #### ENUM:
#   UsernameNotFound(0),
#   UsernameNotFoundCanSignUp(1),
#   UserNotActivatedException(2);
curl "https://api.omartvietnam.com/v1/accounts/0906953809/checkv2"

## Send activation code
curl -X "POST" "https://api.omartvietnam.com/v1/accounts/0909204891/sendActivationCode"

## Activate account
curl -X "POST" "https://api.omartvietnam.com/v1/accounts/0909204891/verifyActivationCode/270427"

## Set Password
curl -X "POST" "https://api.omartvietnam.com/v1/accounts/0909204891/password/202cb962ac59075b964b07152d234b70"

## Deactivate account
curl -X "DELETE" "https://api.omartvietnam.com/v1/accounts/0909204891"

## Login
curl -X "POST" "https://api.omartvietnam.com/v1/oauth/token" \
     -H 'Content-Type: application/x-www-form-urlencoded; charset=utf-8'

## Refresh Token
curl -X "POST" "https://api.omartvietnam.com/v1/oauth/token" \
     -H 'Content-Type: application/json; charset=utf-8' \
     -d $'{
  "client_id": "omart7FnTbKEStGWK2A8abwbZLsgW7FnTbKEStGWK2A8abwbZLsgW",
  "refresh_token": "adfe1ca2-f14a-4fba-95af-e41162954ccb",
  "client_secret": "wtxbGFJuYt5LEMh4BjjMwQqJ",
  "grant_type": "refresh_token"
}'

## Logout
curl -X "DELETE" "https://api.omartvietnam.com/v1/oauth/token" \
     -H 'Content-Type: application/json; charset=utf-8' \
     -d $'{
  "client_id": "omart7FnTbKEStGWK2A8abwbZLsgW7FnTbKEStGWK2A8abwbZLsgW",
  "client_secret": "wtxbGFJuYt5LEMh4BjjMwQqJ",
  "token": "e4da666a-3f1b-4c17-b8fc-49625b9bdd35"
}'

## Logout NoPayload
curl -X "DELETE" "https://api.omartvietnam.com/v1/oauth/token?t=87a5d407-8650-40ef-b861-fb5b32a2bd12" \
     -u 'omart7FnTbKEStGWK2A8abwbZLsgW7FnTbKEStGWK2A8abwbZLsgW:wtxbGFJuYt5LEMh4BjjMwQqJ'

## Register
curl -X "PUT" "https://api.omartvietnam.com/v1/push/notification" \
     -H 'X-User-Id: 349e59449776429ea597a3f48ed9d7ab' \
     -H 'Content-Type: application/json; charset=utf-8' \
     -d $'{
  "token": "dDcY7MwloUU:APA91bHNUBo82HP7azVeJotgWvcBHYNo2RPzgBbuRZsInrhjG6z_fLxQXePOrot76LPZxl_AyvUxh57pzgyJPwwPgwwMT5eR_KKAXNG2uL4hP53m4YE7TN7w3ckStAgfIgfVS17Nut0b"
}'

## Unregister
curl -X "DELETE" "https://api.omartvietnam.com/v1/push/notification" \
     -H 'X-User-Id: minhbxn'

## Get Conversation
curl "https://api.omartvietnam.com/v1/messenger/conversations" \
     -H 'X-User-Id: 6a642e4ffa144bd3965d486db90ec852'

## Get History
curl "https://api.omartvietnam.com/v1/messenger/histories/69a8b53829fd4d7aa9ecce1bff9c7568?limit=1" \
     -H 'X-User-Id: 69a8b53829fd4d7aa9ecce1bff9c7568'

## Sender
curl -X "POST" "https://api.omartvietnam.com/v1/messenger" \
     -H 'X-User-Id: 69a8b53829fd4d7aa9ecce1bff9c7568' \
     -H 'Content-Type: application/json; charset=utf-8' \
     -d $'{
  "payload": {
    "text": "Bắt đi bạn! 😀"
  },
  "recipientId": "69a8b53829fd4d7aa9ecce1bff9c7568"
}'

## Receiver
curl "https://api.omartvietnam.com/v1/messenger" \
     -H 'X-User-Id: 349e59449776429ea597a3f48ed9d7ab'

## Dashboard
curl "https://api.omartvietnam.com/v1/console/quota" \
     -H 'X-User-Id: b789dcd31f764054afb16da4a0037e53'

## GET category
curl "https://api.omartvietnam.com/v1/console/categories/119" \
     -H 'Authorization: Bearer e4d6cf4e-a8d3-47d4-928f-43c55e9df3d6' \
     -H 'X-User-Id: b789dcd31f764054afb16da4a0037e53'

## Send Code update Category
curl -X "POST" "https://api.omartvietnam.com/v1/console/categories/119" \
     -H 'X-User-Id: b789dcd31f764054afb16da4a0037e53'

## Update category
curl -X "PUT" "https://api.omartvietnam.com/v1/console/categories/119" \
     -H 'X-User-Id: b789dcd31f764054afb16da4a0037e53' \
     -H 'Content-Type: application/json; charset=utf-8' \
     -d $'{
  "name": "Nhà Hàng",
  "verifyCode": "142864",
  "imageUrl": "https://firebasestorage.googleapis.com/v0/b/omart-d161e.appspot.com/o/category%2Fcat_nhahang_sangtrong%403x.png?alt=media&token=035b8e7c-532d-4632-9e23-84523a298f5e"
}'

## Provinces
curl "https://api.omartvietnam.com/v1/app/provinces?latitude=10.759288&longitude=106.702196"

## Districts
curl "https://api.omartvietnam.com/v1/app/provinces/5/districts"

## Wards
curl "https://api.omartvietnam.com/v1/app/districts/74/wards"

## Upload Image Body Binary
curl -X "POST" "https://api.omartvietnam.com/v1/app/image?filename=food3" \
     -H 'Content-Type: image/jpeg'

## Upload Image MultiPart
curl -X "POST" "https://api.omartvietnam.com/v1/app/image" \
     -H 'Content-Type: multipart/form-data; charset=utf-8; boundary=__X_PAW_BOUNDARY__' \
     -F "file="

## Update Profile
curl -X "PUT" "https://api.omartvietnam.com/v1/users/profile" \
     -H 'X-User-Id: cf8647f7963611e7b2cc0242ac110002' \
     -H 'Content-Type: application/json; charset=utf-8' \
     -d $'{
  "avatar": "https://thumbor.omartvietnam.com/akfLRM-Wix5JRAIsnESj26VszZ4=/5a1e79f3cb4245bcbc06a5c272548764/file.jpg",
  "name": "Minh, Bùi Xuân Nhật"
}'

## Salesman change password
curl -X "PUT" "https://api.omartvietnam.com/v1/users/changePassword/9546368a81dea9bfda8218b5873c4a7d" \
     -H 'X-User-Id: cf8647f7963611e7b2cc0242ac110002'

## Get Salers
curl "https://api.omartvietnam.com/v1/users/salesman" \
     -H 'X-User-Id: cf8652e7963611e7b2cc0242ac110002'

## Get Saler
curl "https://api.omartvietnam.com/v1/users/salesman/02f5f681047a4c8cb753087a65ae911f" \
     -H 'X-User-Id: cf8652e7963611e7b2cc0242ac110002'

## Create Or Update Saler
curl -X "PUT" "https://api.omartvietnam.com/v1/users/salesman" \
     -H 'X-User-Id: cf8652e7963611e7b2cc0242ac110002' \
     -H 'Content-Type: application/json; charset=utf-8' \
     -d $'{
  "name": "ABC",
  "phoneNumber": "123456789"
}'

## Derole from Salesman to User
curl -X "DELETE" "https://api.omartvietnam.com/v1/users/salesman/5145ead36f0e4335969e5ed8863813cd" \
     -H 'X-User-Id: cf8652e7963611e7b2cc0242ac110002'

## Get Supervisors
curl "https://api.omartvietnam.com/v1/users/supervisors" \
     -H 'X-User-Id: b789dcd31f764054afb16da4a0037e53'

## Create Or Update Supervisors
curl -X "PUT" "https://api.omartvietnam.com/v1/users/supervisors" \
     -H 'X-User-Id: b789dcd31f764054afb16da4a0037e53' \
     -H 'Content-Type: application/json; charset=utf-8' \
     -d $'{
  "name": "ABC",
  "phoneNumber": "123456789"
}'

## GET POIs Having banner
curl "https://api.omartvietnam.com/v1/_admin/banners/pois" \
     -H 'X-User-Id: cf8652e7963611e7b2cc0242ac110002'

## Search POIs
curl "https://api.omartvietnam.com/v1/_admin/banners/pois/search?name=Hoa%20H%E1%BB%93ng" \
     -H 'X-User-Id: cf8652e7963611e7b2cc0242ac110002'

## GET POI
curl "https://api.omartvietnam.com/v1/_admin/banners/pois/460" \
     -H 'X-User-Id: cf8652e7963611e7b2cc0242ac110002'

## Set Banner
curl -X "PUT" "https://api.omartvietnam.com/v1/_admin/banners/pois/853" \
     -H 'X-User-Id: cf8652e7963611e7b2cc0242ac110002' \
     -H 'Content-Type: application/json; charset=utf-8' \
     -d $'{
  "imageUrl": "https://abcasdqweo.com/asdawrqxc.jpeg"
}'

## Approve banner appear
curl -X "POST" "https://api.omartvietnam.com/v1/_admin/banners/pois/853/approve" \
     -H 'X-User-Id: cf8652e7963611e7b2cc0242ac110002' \
     -H 'Content-Type: application/json; charset=utf-8' \
     -d $'{}'

## Unapprove banner disappear
curl -X "POST" "https://api.omartvietnam.com/v1/_admin/banners/pois/853/unapprove" \
     -H 'X-User-Id: cf8652e7963611e7b2cc0242ac110002' \
     -H 'Content-Type: application/json; charset=utf-8' \
     -d $'{}'

## Subcategories
curl "https://api.omartvietnam.com/v1/apps/sales/subcategories" \
     -H 'X-User-Id: system'

## Create Category
curl -X "POST" "https://api.omartvietnam.com/v1/_admin/categories/" \
     -H 'X-User-Id: system' \
     -H 'Content-Type: application/json; charset=utf-8' \
     -d $'{
  "order": 200,
  "imageUrl": "http://via.placeholder.com/3.png",
  "name": "Test",
  "parentId": 14
}'

## Update Category
curl -X "PUT" "https://api.omartvietnam.com/v1/_admin/categories/15" \
     -H 'Content-Type: application/json; charset=utf-8' \
     -H 'X-User-Id: system' \
     -d $'{
  "name": "Âm Thanh 1",
  "imageUrl": "https://firebasestorage.googleapis.com/v0/b/omart-d161e.appspot.com/o/category%2Fcat_dientu_amthanh%403x.png?alt=media&token=fc1d87bc-b6a9-4d05-ae4c-aa3e771f0459"
}'

## POIs by Creator
curl "https://api.omartvietnam.com/v1/_admin/pois" \
     -H 'Authorization: bearer 90b8d06b-a6a9-4e83-beb3-8da9105eb9ea' \
     -H 'X-User-Id: cf8650ca963611e7b2cc0242ac110002'

## Get POI
curl "https://api.omartvietnam.com/v1/_admin/pois/1" \
     -H 'X-User-Id: system'

## Province By User Permission
curl "https://api.omartvietnam.com/v1/_admin/provinces" \
     -H 'X-User-Id: d58cd63e2c8d45e4a5e6a96ef0464f46'

## Create POI
curl -X "POST" "https://api.omartvietnam.com/v1/_admin/pois/" \
     -H 'X-Location-Geo: 10.759628,106.672469' \
     -H 'Content-Type: application/json; charset=utf-8' \
     -H 'X-User-Id: f86bf5979d7c48b492b376e32e79c34d' \
     -d $'{
  "description": "Siêu thị điện máy chợ lớn, Q5, TpHCM...",
  "wardId": 1023,
  "districtId": 68,
  "phone": [
    "1234",
    "5678",
    "9876"
  ],
  "snapshotMap": "abc",
  "openHour": 9.25,
  "closeHour": 21,
  "longitude": 106.672469,
  "avatarImage": [
    {
      "url": "http://via.placeholder.com/300x300"
    }
  ],
  "latitude": 10.759628,
  "ownerId": "74b3385e5dd841c094221b078e242e20",
  "coverImage": [
    {
      "url": "https://dienmaycholon.vn/public/dienmaycholon/general/img/gioi-thieu.jpg"
    }
  ],
  "address": "Lô G, Chung cư Hùng Vương",
  "provinceId": 5,
  "featuredImage": [
    {
      "url": "http://via.placeholder.com/300x300"
    }
  ],
  "name": "Điện máy chợ lớn Test 1",
  "categoryId": 21
}'

## Update POI
curl -X "PUT" "https://api.omartvietnam.com/v1/_admin/pois/1814" \
     -H 'Content-Type: application/json; charset=utf-8' \
     -H 'X-User-Id: 7589b5dab5c24e7ea571357334c81cbe' \
     -d $'{
  "description": "Siêu thị điện máy chợ lớn, Q5, TpHCM...",
  "wardId": 1023,
  "districtId": 68,
  "phone": [
    "1234",
    "5678",
    "9876"
  ],
  "snapshotMap": "abc",
  "openHour": 9.25,
  "closeHour": 21,
  "longitude": 106.672469,
  "avatarImage": [
    {
      "url": "http://via.placeholder.com/300x300"
    }
  ],
  "latitude": 10.759628,
  "ownerId": 1,
  "coverImage": [
    {
      "url": "https://dienmaycholon.vn/public/dienmaycholon/general/img/gioi-thieu.jpg"
    }
  ],
  "address": "Lô G, Chung cư Hùng Vương",
  "provinceId": 5,
  "featuredImage": [
    {
      "url": "http://via.placeholder.com/300x300"
    }
  ],
  "name": "Điện máy chợ lớn Test",
  "categoryId": 21
}'

## Items
curl "https://api.omartvietnam.com/v1/_admin/pois/1/items" \
     -H 'X-User-Id: system'

## Item
curl "https://api.omartvietnam.com/v1/_admin/pois/1/items/7" \
     -H 'X-User-Id: system'

## Create Item
curl -X "POST" "https://api.omartvietnam.com/v1/_admin/pois/1/items" \
     -H 'Content-Type: application/json; charset=utf-8' \
     -H 'X-User-Id: system' \
     -d $'{
  "coverImage": [
    {
      "url": "https://igx.4sqi.net/img/general/width960/45442676_MxkidWnaJc31VIgZROYx-8AQ-sXL9DQCLrjf9A-kOVc.jpg"
    }
  ],
  "featuredImage": [
    {
      "url": "http://via.placeholder.com/300x300"
    }
  ],
  "unitPrice": 61000,
  "avatarImage": [
    {
      "url": "http://via.placeholder.com/300x300"
    }
  ],
  "name": "Cơm tấm sườn bì chả trứng"
}'

## Update Item
curl -X "PUT" "https://api.omartvietnam.com/v1/_admin/pois/1/items/7" \
     -H 'Content-Type: application/json; charset=utf-8' \
     -H 'X-User-Id: system' \
     -d $'{
  "coverImage": [
    {
      "url": "https://igx.4sqi.net/img/general/width960/45442676_MxkidWnaJc31VIgZROYx-8AQ-sXL9DQCLrjf9A-kOVc.jpg"
    }
  ],
  "featuredImage": [
    {
      "url": "http://via.placeholder.com/300x300"
    }
  ],
  "unitPrice": 61000,
  "avatarImage": [
    {
      "url": "http://via.placeholder.com/300x300"
    }
  ],
  "name": "Cơm tấm sườn bì trứng cuộn"
}'

## GET Owners
curl "https://api.omartvietnam.com/v1/_admin/owners" \
     -H 'X-User-Id: 7589b5dab5c24e7ea571357334c81cbe'

## Create POI Owner
curl -X "POST" "https://api.omartvietnam.com/v1/_admin/owners" \
     -H 'X-User-Id: cf8647f7963611e7b2cc0242ac110002' \
     -H 'Content-Type: application/json; charset=utf-8' \
     -d $'{
  "name": "A.HUy",
  "password": "202cb962ac59075b964b07152d234b70",
  "phoneNumber": "0963999997"
}'

## Update POI Owner
curl -X "PUT" "https://api.omartvietnam.com/v1/_admin/owners/349e59449776429ea597a3f48ed9d7ab" \
     -H 'X-User-Id: system' \
     -H 'Content-Type: application/json; charset=utf-8' \
     -d $'{
  "phoneNumber": "0909204891",
  "password": "7546f752d80e6fb3ebdab2b372f7366a",
  "name": "Hoàng Long",
  "avatar": "https://thumbor.omartvietnam.com/OQTnuFCRjpFbyI8v7TA4ZIcXCQg=/f52abfbc9fe24b82ada3880b4d3ffee4/LongAvatar.jpg"
}'

## GET List Category
# Screen - Mobile Client - HOME PAGE
curl "https://api.omartvietnam.com/v1/categories" \
     -H 'Content-Type: application/json; charset=utf-8'

## Search
curl "https://api.omartvietnam.com/v1/pois?text=coca%20cola" \
     -H 'X-Location-Geo: 10.78887869139275,106.7001767178128'

## GET POI: NearBy & Manual
# Screen - Mobile Client - LIST SHOP
curl "https://api.omartvietnam.com/v1/categories/15/pois?province=all&district=all&ward=all" \
     -H 'X-Location-Geo: 10.78887869139275,106.7001767178128'

## GET POI Detail
# Screen - Mobile Client - LIST SHOP INTRO
curl "https://api.omartvietnam.com/v1/categories/119/pois/3936" \
     -H 'Content-Type: application/json; charset=utf-8'

## GET List item
# Screen - Mobile Client - LIST SHOP Item (MENU - PRODUCT)
curl "https://api.omartvietnam.com/v1/categories/21/pois/2/items" \
     -H 'Content-Type: application/json; charset=utf-8'

## GET Item Detail
# Screen - Mobile Client - LIST SHOP ITEM (MENU - PRODUCT) DETAIL
# Noted: Data test Category = 1 ItemDetail = false: không thể call service này
# Tested: categories/4/pois/2/items/4 -> Iphone 7
#
curl "https://api.omartvietnam.com/v1/categories/21/pois/2/items/4" \
     -H 'Content-Type: application/json; charset=utf-8'

## GET POIs by Owner
curl "https://api.omartvietnam.com/v1/me/pois" \
     -H 'X-User-Id: 349e59449776429ea597a3f48ed9d7ab'

## GET Profile Owner
curl "https://api.omartvietnam.com/v1/me" \
     -H 'X-User-Id: 7589b5dab5c24e7ea571357334c81cbe'

## Owner: Change Password
curl -X "PUT" "https://api.omartvietnam.com/v1/me/password" \
     -H 'X-User-Id: cf8647f7963611e7b2cc0242ac110002' \
     -H 'Content-Type: application/json; charset=utf-8' \
     -d $'{
  "password": "202cb962ac59075b964b07152d234b70"
}'

## Owner: Update POI
curl -X "PUT" "https://api.omartvietnam.com/v1/me/pois/408" \
     -H 'Content-Type: application/json; charset=utf-8' \
     -H 'X-User-Id: 7589b5dab5c24e7ea571357334c81cbe' \
     -d $'{
  "description": "Siêu thị điện máy chợ lớn, Q5, TpHCM...",
  "wardId": 1023,
  "districtId": 68,
  "phone": [
    "1234",
    "5678",
    "9876"
  ],
  "snapshotMap": "abc",
  "openHour": 9.25,
  "closeHour": 21,
  "longitude": 106.672469,
  "avatarImage": [
    {
      "url": "http://via.placeholder.com/300x300"
    }
  ],
  "latitude": 10.759628,
  "ownerId": "74b3385e5dd841c094221b078e242e20",
  "coverImage": [
    {
      "url": "https://dienmaycholon.vn/public/dienmaycholon/general/img/gioi-thieu.jpg"
    }
  ],
  "address": "Lô G, Chung cư Hùng Vương",
  "provinceId": 5,
  "featuredImage": [
    {
      "url": "http://via.placeholder.com/300x300"
    }
  ],
  "name": "Điện máy chợ lớn Test",
  "categoryId": 21
}'

## GET: Ads Banner
curl "https://api.omartvietnam.com/v1/ads/banner?categoryId=15"

## Test Push Notification
curl "https://api.omartvietnam.com/v1/messenger/push"

## Slack alert Channel
curl -X "POST" "https://hooks.slack.com/services/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" \
     -H 'Content-Type: text/plain; charset=utf-8' \
     -d $'{
  "username": "android",
  "text": "This is a line of text in a channel.\\nAnd this is another line of text."
}'

## Slack Contact Channel
curl -X "POST" "https://hooks.slack.com/services/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" \
     -H 'Content-Type: text/plain; charset=utf-8' \
     -d $'{
  "text": "This is a line of text in a channel.\\nAnd this is another line of text."
}'

## Indexing
curl "https://api.omartvietnam.com/v1/indexing"

## Info
curl "https://api.omartvietnam.com/info" \
     -H 'X-Force-Zone: blue'

## Send SMS
curl "https://cloudsms.vietguys.biz:4438/api/?u=o-mart.vn&pwd=z1r5k&from=O-MART&phone=0916267677&sms=Thong%20bao%20O%20Mart%20dang%20thuc%20hien%20xac%20thuc%20tai%20khoan%20cua%20ban%20!%20Ma%20xac%20thuc%20la%20xxxxx"

## check_token
curl -X "DELETE" "https://api.omartvietnam.com/oauth/check_token?token=1c6b3296-3a26-4b13-86ae-515ae717ab5f" \
     -H 'Content-Type: application/json; charset=utf-8' \
     -d $'{
  "client_id": "omart7FnTbKEStGWK2A8abwbZLsgW7FnTbKEStGWK2A8abwbZLsgW",
  "client_secret": "wtxbGFJuYt5LEMh4BjjMwQqJ"
}'

## Upload Thumbor
curl -X "POST" "http://localhost:8000/image" \
     -H 'Content-Type: image/jpeg'

## Delete Thumbor
curl -X "DELETE" "http://localhost:8000/image/a21a4f330d4e4092840c8f01cb558955/image.jpg"

## Delete Thumbor Duplicate
curl -X "DELETE" "http://localhost:8000/image/a21a4f330d4e4092840c8f01cb558955/image.jpg"

## Info
# Noted: ADD HOSTS
curl "http://localhost:9200"

## Indexing
curl -X "POST" "http://es.omartvietnam.com/minhbxn/poi/1" \
     -H 'Content-Type: application/json; charset=utf-8' \
     -d $'{
  "id": 202,
  "openHour": 9,
  "ward_name": "Phường 06",
  "province_name": "TP Hồ Chí Minh",
  "phone": [
    "123",
    "456"
  ],
  "closeHour": 21,
  "updatedAt": "2017-09-10T21:31:02Z",
  "longitude": 106.6932983,
  "latitude": 10.7860983,
  "ownerId": "cf8647f7963611e7b2cc0242ac110002",
  "address": "Đường Điện Biên Phủ",
  "createdBy": "system",
  "opening": true,
  "createdAt": "2017-09-10T21:31:02Z",
  "name": "teset",
  "district_name": "Quận 3"
}'

## Searching
# Noted: ADD HOSTS
curl "http://es.omartvietnam.com/omart/pois/_search" \
     -H 'Content-Type: application/json; charset=utf-8' \
     -d $'{
  "query": {
    "multi_match": {
      "query": "hoa cuc vang",
      "fields": [
        "_all"
      ]
    }
  }
}'

#===================================================================================================
# 2018-01-02 - API v1'
#===================================================================================================

#status: LIKE,DISLIKE,FAVORITE
POST https://api.omartvietnam.com/v1/pois/{poi-id}/action
	-H 'Content-Type: application/json; charset=utf-8'
    -H 'X-User-Id: 7589b5dab5c24e7ea571357334c81cbe'
	
RequestBody : {
	"status": "DISLIKE"
}

response sample:
	{
    "userAction": {
        "like": false,
        "dislike": false,
        "favorite": true
    },
    "dislikeNumber": 3,
    "likeNumber": 2
}
	 
	 
curl GET "https://api.omartvietnam.com/v1/pois/{poi-id}/view"
     -H 'Content-Type: application/json; charset=utf-8'
	 response:
	 {
		"count": 12
	 }
	 
# Get Poi Detail
curl GET "https://api.omartvietnam.com/v1/pois/{poi-id}?latitude={latitude}&longitude={longitude}"
 -H 'Content-Type: application/json; charset=utf-8'
 -H 'X-User-Id: 7589b5dab5c24e7ea571357334c81cbe'
 
 #Get comments
 curl GET "https://api.omartvietnam.com/v1/pois/13/comments?page=1&size=5"
 
 #Add comment
 curl POST "https://api.omartvietnam.com/v1//pois/{poi-id}/comment"
 request body : 
 { 
	"comment": "this i a comment"
}
response {} and 201 = created

#====================================================================
curl POST "https://api.omartvietnam.com/v1/app/poi/picture"
request sample
{
	"poi": {
		"id":13
	},
	"imageUrl":"https://thumbor.omartvietnam.com/CEPWnboayWRALanQvCJVbtZp3rY=/b905ff159d784ea3b4666e2b3801ece9/huongfile.jpg",
	"title":"this a bird 1"
}
#response empty json and 201 created
#====================================================================

curl POST "https://api.omartvietnam.com/v1/app/image"
 -H 'X-User-Id: 7589b5dab5c24e7ea571357334c81cbe'
 -F file
 -T filename
 response sample
 {
    "url": "https://thumbor.omartvietnam.com/xVQ9MRJ7a1pNwD4wE9ZV0HamRO4=/f563361936dd4a448b0790ae13d63a88/images2.jpg"
}
#====================================================================
#Populate location address 
 curl GET "https://api.omartvietnam.com/v1/_admin/address?latitude=10.809099&longitude=107.371961"
 response:
 {
    "province": {
        "id": 22,
        "name": "Đồng Nai"
    },
    "district": {
        "id": 246,
        "name": "Huyện Cẩm Mỹ"
    },
    "ward": {
        "id": 3430,
        "name": "Xã Xuân Đông"
    },
    "street": "12,nam street"
}

#====================================================================
#Share POI
 curl GET "https://api.omartvietnam.com/v1/pois/13/share"
 response:
 {
    "count": 2
}

#====================================================================
#Create new POI from shop owner
 curl POST "https://api.omartvietnam.com/v1/pois"
 -H X-User-Id
 -H X-Location-Geo