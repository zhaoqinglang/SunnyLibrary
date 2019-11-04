package com.android.ss.demo;

/**
 * url地址常量类
 * Date&Time: 2015-10-12 11:23
 */
public class UrlsConstant {
    /**
     * 地址
     */
//    public static final String BASE_URL = "https://www.jswx.mobi/index.php/Api/";//正式地址
//    public static final String BASE_URL = "http://192.168.1.225/index.php/Api/";//测试地址

    public static final String BASE_URL="http://zjtest.demo.zhoushijt.cn/index.php/Api/";


    /**
     *
     * */
    public static final String ISCHECKAPPALONE = BASE_URL + "ApiLogin/ischeckappAlone";

    public static final String AddSharerecord=BASE_URL+"Categoryapi/addsharerecord";

    public static final String Getservicelist=BASE_URL+"Customerservice/getservicelist";

    public static final String SendMessage=BASE_URL+"Customerservice/sendmessage";

    public static final String HotArticle=BASE_URL+"Categoryapi/getrandomlist";

    /**
     * 检验登录账号失效
     */
    public static final String CHECKUSERMID = BASE_URL + "ApiLogin/checkUserMid";

    /**
     * 2.1登陆接口
     */
    public static final String LOGIN_URL = BASE_URL + "ApiLogin/login";
    /**
     * 2.2注册接口
     */
    public static final String REGISTER_URL = BASE_URL + "ApiLogin/register";
    /**
     * 2.3发送验证码
     */
    public static final String SEND_VERIFY_URL = BASE_URL + "ApiLogin/sendcode";
    /**
     * 2.4找回密码
     */
    public static final String FIND_PWD_URL = BASE_URL + "ApiLogin/findpassword";
    /**
     * 5验证原账号
     */
    public static final String CHECK_OLDPHONENUM_URL = BASE_URL + "Membercenter/replacehao";
    /**
     * 2.5服务协议
     */
    public static final String INTERNET_PROTOCOL_URL = BASE_URL + "ApiLogin/agreement";
    /**
     * 2.8修改登录密码
     */
    public static final String CHANGE_PASSWORD_URL = BASE_URL + "Membercenter/editpaasword";
    /**
     * 2.8修改登录密码
     */
    public static final String CHECK_CODE_URL = BASE_URL + "ApiLogin/checkcode";
    /**
     * 2.9安装接口
     */
    public static final String BASECINSTALL_URL = BASE_URL + "";
    /**
     * 3.1国家地区列表
     */
    public static final String COUNTRY_URL = BASE_URL + "ApiLogin/countrylist";
    /**
     * 3.2首页
     */
    public static final String HOMEDATA_URL = BASE_URL + "Categoryapi/categorylist";
    /**
     * 3.3首页二级栏目
     */
    public static final String TWO_CATEGORY_URL = BASE_URL + "Categoryapi/categorytwolist";
    /**
     * 3.4轮播详情
     */
    public static final String BANNER_DETAIL_URL = BASE_URL + "Categoryapi/getbannbercontent";
    /**
     * 3.5文章详情
     */
    public static final String ARTICLE_DETAIL_URL = BASE_URL + "Categoryapi/getproductsinfo";
    /**
     * 3.5产品列表
     */
    public static final String PRODUCT_LIST_URL = BASE_URL + "Categoryapi/lists";
    /**
     * 3.6乐趣
     */
    public static final String PLEASURE_LIST_URL = BASE_URL + "Categoryapi/pleasurelist";
    /**
     * 3.7评论列表
     */
    public static final String COMMENT_LIST_URL = BASE_URL + "Categoryapi/commontlist";
    /**
     * 3.8提交列表
     */
    public static final String SUBMIT_COMMENT_URL = BASE_URL + "Categoryapi/commontadd";
    /**
     * 3.9点赞
     */
    public static final String SUBMIT_ZAN_URL = BASE_URL + "Categoryapi/givefab";
    /**
     * 3.10收藏
     */
    public static final String COLLECTION_URL = BASE_URL + "Categoryapi/favoritesadd";
    /**
     * 3.11获取省市联动数据
     */
    public static final String PROVINCE_CITY_URL = BASE_URL + "Categoryapi/province";
    /**
     * 3.12收藏列表
     */
    public static final String COLLECTION_LIST_URL = BASE_URL + "Categoryapi/favoriteslist";
    /**
     * 3.13取消收藏
     */
    public static final String CANCEL_COLLECTION_URL = BASE_URL + "Categoryapi/cancelfavorites";
    /**
     * 4.1意见反馈
     */
    public static final String FEEDBACK_URL = BASE_URL + "Membercenter/addfeedback";
    /**
     * 4.2意见反馈列表
     */
    public static final String FEEDBACK_LIST_URL = BASE_URL + "Membercenter/feedbacklist";
    /**
     * 4.3修改头像
     */
    public static final String UPLOAD_IMAGE_URL = BASE_URL + "Membercenter/editAvatar";
    /**
     * 4.4修改个人信息
     */
    public static final String CHANGE_INFO_URL = BASE_URL + "Membercenter/editcenter";
    /**
     * 4.5版本更新
     */
    public static final String UP_VERSION_URL = BASE_URL + "Categoryapi/checkversion";
    /**
     * 4.6取消绑定第三方账号
     */
    public static final String CANCEL_BIND_URL = BASE_URL + "ApiLogin/removebinding";
    /**
     * 4.7绑定第三方账号
     */
    public static final String BIND_ACCOUNT_URL = BASE_URL + "ApiLogin/binding";
    /**
     * 4.8邀请链接(不要动这个链接)
     */
    public static final String INVITE_URL = BASE_URL + "Show/invitationRegister/param/";
    /**
     * 4.9第三方登陆
     */
//    public static final String THREE_LOGIN_URL = BASE_URL + "ApiLogin/wxLogin";
    public static final String THREE_LOGIN_URL = BASE_URL + "ApiLogin/GrantLogin";
    /**
     * 4.10个人中心
     */
    public static final String PERSON_INFO_URL = BASE_URL + "Membercenter/memberceter";
    /**
     * 4.10 支付回调接口
     */
    public static final String ALIPAY_NOTIFY_URL = BASE_URL + "Payment/alipayPayment";
    /**
     * 5.1 银行卡列表
     */
    public static final String ALL_BANKCARD_LIST_URL = BASE_URL + "Distribution/banklist";
    /**
     * 5.2 添加银行卡
     */
    public static final String ADD_BANKCARD_URL = BASE_URL + "Distribution/adadbank";
    /**
     * 5.3 用户银行卡列表
     */
    public static final String USER_BANKCARD_LIST_URL = BASE_URL + "Distribution/memberbanklist";
    /**
     * 5.3 删除银行卡
     */
    public static final String DELETE_BANKCARD_URL = BASE_URL + "Distribution/membeRremoveBank";
    /**
     * 5.4 提现佣金
     */
    public static final String EXTRACT_COMMISSION_URL = BASE_URL + "Distribution/withdrawals";
    /**
     * 5.5 提现记录
     */
    public static final String WITHDRAW_RECORDING_URL = BASE_URL + "Distribution/withdrawalslist";
    /**
     * 5.6 分销佣金
     */
    public static final String DISTRIBUTION_MONEY_URL = BASE_URL + "Distribution/sales";
    /**
     * 5.7 分销商详情
     */
    public static final String DISTRIBUTION_DETAIL_URL = BASE_URL + "Distribution/salestotal";
    /**
     * 5.8 分销商详情
     */
    public static final String DISTRIBUTION_LIST_URL = BASE_URL + "Distribution/saleslist";
    /**
     * 5.9 佣金明细
     */
    public static final String COMMISSION_DETAIL_URL = BASE_URL + "Distribution/commissionlist";
    /**
     * 5.11
     */
    public static final String RECHARGE_DETAIL_URL = BASE_URL + "Distribution/join";
    /**
     * 5.12个人网站域名
     */
    public static final String PERSON_WEB_URL = BASE_URL + "Membercenter/memberper";
    /**
     * 5.13充值成功返回时间信息
     */
    public static final String RECHARGE_SUCCESS_URL = BASE_URL + "Distribution/RechargeInfo";
    /**
     * 6.1获取支付信息
     */
    public static final String PAY_INFO_URL = BASE_URL + "Payment/getpaytypeinfo";
    /**
     * 6.2余额支付
     */
    public static final String OVERAGE_PAY_URL = BASE_URL + "Payment/BalancePayment";
    /**
     * 6.3获取启动页广告内容
     */
    public static final String GET_BANNER_CONTENT_URL = BASE_URL + "Categoryapi/guide";
    /**
     * 6.4paypal支付成功调用方法
     */
    public static final String PAYPAL_PAY_SUCCESS_URL = BASE_URL + "Payment/paypalnotifyurl";
    /**
     * 6.4paypal支付成功调用方法
     */
    public static final String CHECK_EMAIL_URL = BASE_URL + "ApiLogin/checkMail";

    public static final String GETFILTER_URL = BASE_URL + "Categoryapi/getFilter";


    public static final String UPLOAD_WX_CODE_IMAGE_URL = BASE_URL + "Membercenter/uploadWxqrcode";

    public static final String PRODUCT_SEARCH = BASE_URL + "Categoryapi/productsearch";

    public static final String SHARE_URL = BASE_URL + "show/showurl";

    public static final  String DENGLUSHOW_URL = BASE_URL + "Quick/dengshow";

    //查询自己分享的文章
    public static final String MY_SHARED = BASE_URL + "Categoryapi/getmysharelist";
    //删除自己分享的文章
    public static final String MY_SHARED_DELETE = BASE_URL + "Categoryapi/delsharerecord";
    //查询自己分享文章的他人浏览记录
    public static final String MY_SHARED_RECORD = BASE_URL + "Categoryapi/getviewsharelistbyrecordid";
    //获取我的家谱
    public static final String MY_GENEALOGY = BASE_URL + "Membercenter/getmytree";
    //获取名片模板列表
    public static final String LIST_CARD = BASE_URL + "Show/getcardlist";
    //上传选择的名片模板
    public static final String USE_CARD = BASE_URL + "Membercenter/editcenter";
}
