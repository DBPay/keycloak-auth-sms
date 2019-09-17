# keycloak-auth-sms


### 安装步骤:
```
    添加jar文件到Keycloak server:
        $ cp target/keycloak-auth-sms.jar _KEYCLOAK_HOME_/providers/

    Add two templates to the Keycloak server:
        $ cp themes/dbpay _KEYCLOAK_HOME_/themes/
```

### 配置Realm使用SMS Authentication
Under Authentication > Flows:

    Copy 'Browse' flow to 'openstandia browser' flow
    Click on 'Actions > Add execution on the 'Openstandia Browser Forms' line and add the 'Twilio SMS Authentication'
    Set 'Twilio SMS Authentication' to 'REQUIRED'
    To configure the SMS Authernticator, click on Actions Config and fill in the attributes.

Under Authentication > Bindings:

    Select 'Browser with SMS' as the 'Browser Flow' for the REALM.


message:
  sms:
    yunpian:
      weight: 5
      enabled: true
      apiKey: 7cd57c21f9f1d9d6406f15169e81908d
      apiKeyMarketing: 71531f72203286b5656b6e8512aab108
    chuanglan:
      enabled: true
      weight: 2
      #校验是否国内手机号正则表达式
      pattern: ^(\+?0?86\-?)?(1[3456789])\d{9}$
      url:
        verifyDomestic: http://smssh1.253.com/msg/send/json
        verifyInter: http://intapi.sgap.253.com/send/json
        group: http://smssh1.253.com/msg/send/json
        yuyin: http://zapi.253.com/msg/HttpBatchSendSM
      account:
        verifyDomestic: N413154_N3310471
        verifyInter: I7835957
        group: M4717573
        yuyin: V1273636
      password:
        verifyDomestic: 3ldnar9y05c97e
        verifyInter: Q60Yw9aoT
        group: zDtxy76Uh
        yuyin: HlOW1acXRU3b76