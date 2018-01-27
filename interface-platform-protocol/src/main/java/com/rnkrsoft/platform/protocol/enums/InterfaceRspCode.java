/**
 * RNKRSOFT OPEN SOURCE SOFTWARE LICENSE TERMS ver.1
 * - 氡氪网络科技(重庆)有限公司 开源软件许可条款(版本1)
 * 氡氪网络科技(重庆)有限公司 以下简称Rnkrsoft。
 * 这些许可条款是 Rnkrsoft Corporation（或您所在地的其中一个关联公司）与您之间达成的协议。
 * 请阅读本条款。本条款适用于所有Rnkrsoft的开源软件项目，任何个人或企业禁止以下行为：
 * .禁止基于删除开源代码所附带的本协议内容、
 * .以非Rnkrsoft的名义发布Rnkrsoft开源代码或者基于Rnkrsoft开源源代码的二次开发代码到任何公共仓库,
 * 除非上述条款附带有其他条款。如果确实附带其他条款，则附加条款应适用。
 * <p/>
 * 使用该软件，即表示您接受这些条款。如果您不接受这些条款，请不要使用该软件。
 * 如下所述，安装或使用该软件也表示您同意在验证、自动下载和安装某些更新期间传输某些标准计算机信息以便获取基于 Internet 的服务。
 * <p/>
 * 如果您遵守这些许可条款，将拥有以下权利。
 * 1.阅读源代码和文档
 * 如果您是个人用户，则可以在任何个人设备上阅读、分析、研究Rnkrsoft开源源代码。
 * 如果您经营一家企业，则禁止在任何设备上阅读Rnkrsoft开源源代码,禁止分析、禁止研究Rnkrsoft开源源代码。
 * 2.编译源代码
 * 如果您是个人用户，可以对Rnkrsoft开源源代码以及修改后产生的源代码进行编译操作，编译产生的文件依然受本协议约束。
 * 如果您经营一家企业，不可以对Rnkrsoft开源源代码以及修改后产生的源代码进行编译操作。
 * 3.二次开发拓展功能
 * 如果您是个人用户，可以基于Rnkrsoft开源源代码进行二次开发，修改产生的元代码同样受本协议约束。
 * 如果您经营一家企业，不可以对Rnkrsoft开源源代码进行任何二次开发，但是可以通过联系Rnkrsoft进行商业授予权进行修改源代码。
 * 完整协议。本协议以及开源源代码附加协议，共同构成了Rnkrsoft开源软件的完整协议。
 * <p/>
 * 4.免责声明
 * 该软件按“原样”授予许可。 使用本文档的风险由您自己承担。Rnkrsoft 不提供任何明示的担保、保证或条件。
 * 5.版权声明
 * 本协议所对应的软件为 Rnkrsoft 所拥有的自主知识产权，如果基于本软件进行二次开发，在不改变本软件的任何组成部分的情况下的而二次开发源代码所属版权为贵公司所有。
 */
package com.rnkrsoft.platform.protocol.enums;

import com.rnkrsoft.interfaces.EnumStringCode;

/**
 * Created by rnkrsoft.com on 2018/5/7.
 * 接口通信层错误码
 */
public enum InterfaceRspCode implements EnumStringCode {
    SUCCESS("000","成功"),
    PARAM_IS_NULL("001","参数不能为空"),
    TIMESTAMP_ILLEGAL("002","系统时间与北京时间不一致，请校准后再重试"),
    REQUEST_SIGN_ILLEGAL("003","请求签字信息无效"),
    ACCOUNT_HAS_LOGIN_ON_OTHER_DEVICE("004","您的帐号已在其它设备登录"),
    PARAM_TYPE_NOT_MATCH("005","参数类型不匹配"),
    USER_IS_NOT_LOGIN("006","用户未登录"),
    INTERFACE_IS_ILLEGAL("007","接口无效"),
    INTERFACE_IS_DEV("008","接口正在开发"),
    INTERFACE_HAPPEN_UNKNOWN_ERROR("009","接口发生未知错误"),
    INTERFACE_EXECUTE_HAPPENS_ERROR("010","接口执行发生错误"),
    INTERFACE_NOT_DEFINED("011","接口未定义"),
    INTERFACE_SERVICE_CLASS_NOT_FOUND("012","接口对应的服务类不存在"),
    INTERFACE_SERVICE_METHOD_NOT_EXIST("013","接口对应的服务方法不存在"),
    INTERFACE_EXISTS_OTHER_VERSION("014","存在其他版本的接口，但不存在当前版本号的接口"),
    CHANNEL_NOT_EXISTS("015","接口通道不存在"),
    DECRYPT_HAPPENS_FAIL("016","解密发生错误"),
    VERIFY_HAPPENS_FAIL("017","验签发生错误"),
    SIGN_HAPPENS_FAIL("018","签字发生错误"),
    ENCRYPT_HAPPENS_FAIL("019","加密发生错误"),
    TOKEN_ILLEGAL("020","TOKEN无效"),
    UPDATE_REQUEST_HAPPENS_ERROR("021","更新请求信息发生错误"),
    UPDATE_RESPONSE_HAPPENS_ERROR("022","更新应答信息发生错误"),
    REQUEST_DATA_IS_NULL("023","请求数据为空"),
    TX_NO_IS_NULL("024","交易码为空"),
    TOKEN_SERVICE_NOT_EXISTS("025","TOKEN服务不存在"),
    DATA_CONVERT_HAPPENS_ERROR("026","数据转换发生错误"),
    DATA_CONVERT_SERVICE_EXISTS_ERROR("027","数据转换服务存在错误"),
    DEVICE_CAN_NOT_ACCESS_INTERNET("028", "设备不能访问互联网"),
    NOT_SUPPORTED_ENCRYPT_DECRYPT_ALGORITHM("029", "不支持的加密解密算法"),
    NOT_SUPPORTED_SIGN_VERIFY_ALGORITHM("030", "不支持的签字验签算法"),
    INVALID_COMMUNICATION_MESSAGE("031", "无效的通信报文"),
    RESPONSE_DATA_IS_NULL("032","应答数据为空"),
    SOCKET_PERMISSION_DENIED("033","无网络权限"),
    INTERFACE_PLATFORM_GATEWAY_NOT_FOUND("034","接口平台网关未发现"),
    CONFIGURE_GATEWAY_NOT_FOUND("035","配置网关未发现"),
    USE_TOKEN_AS_PASSWORD_BUT_TOKEN_IS_NULL("036", "使用TOKEN作为密码，但是TOKEN为空"),
    ENCRYPT_DATA_IS_NULL("037","加密数据为空"),
    DECRYPT_DATA_IS_NULL("038","解密数据为空"),
    SYNC_SEND_SERVICE_HAPPENS_ERROR("039","同步发送服务发生错误"),
    UNSUPPORTED_DIRECTION("040","不支持的方向"),
    GATEWAY_CONFIGURE_IS_NOT_CONFIG("041","网关配置信息未配置"),
    SIGN_ILLEGAL("042", "签字信息无效"),
    VERSION_ILLEGAL("043","版本号无效"),
    INTERFACE_CONNECTOR_IS_NOT_CONFIG("044","接口连接器未配置"),
    OVER_FLOW_LIMITING("045","超过流量限制"),
    CONVERT_REQUEST_DATA_FAIL("046","转换请求数据失败"),
    CONVERT_RESPONSE_DATA_FAIL("047","转换应答数据失败"),
    REQUEST_DATA_ILLEGAL("048","请求数据无效，请重新输入！"),
    ENVIRONMENT_IS_ALREADY_DISABLED("049","环境已经禁用"),
    LOCATION_PROVIDER_IS_NOT_CONFIG("050","定位提供者未配置！"),
    CONFIGURE_PROVIDER_IS_NOT_CONFIG("051","配置提供者未配置！"),
    CLIENT_IS_NOT_INITIALIZED("052","客服端未初始化！"),
    INTERFACE_FALLBACK_GATEWAY_IS_NOT_CONFIG("053","接口失败回退网关未配置！"),
    FETCH_INTERFACE_METADATA_IS_FAILURE("054","获取接口元信息失败！"),
    INTERFACE_HAPPENS_SERVER_ERROR("055","执行接口发生服务器错误！"),
    SYSTEM_MAINTENANCE("998","系统正在维护，请稍后重试！"),
    FAIL("999","错误");
    String code;
    String desc;

    InterfaceRspCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public static InterfaceRspCode valueOfCode(String code) {
        for (InterfaceRspCode value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return FAIL;
    }
}
