package cn.com.clubank.weihang.common.jpush;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.SMS;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;


/**
 * java后台极光推送方式二：使用Java SDK
 */

public class JiguangPush {
	
	private static final Logger log = LoggerFactory.getLogger(JiguangPush.class);

    public static final String APP_KEY ="8638ca091cbd5f01285e4a2e";//JPush服务器端下发的app_key
    public static final String MASTER_SECRET = "94204cac4c9bfe8f9e3a1de0";//JPush服务器端下发的master_key

    /**
     * 构建推送对象：对所有平台，所有设备，内容为 alert的通知
     * @return
     */
    public static PushPayload buildPushObject_all_all_alert(String alter) {
        return PushPayload.alertAll(alter);
    }

    /**
     * 所有平台，推送目标是别名为 "registrationId"，通知内容为 alert
     * @param registrationId
     * @param alert
     * @return
     */
    public static PushPayload buildPushObject_all_alias_alert(String registrationId,Object alert) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())//设置接受的平台
                .setAudience(Audience.registrationId(registrationId))//Audience如果设置为all，说明采用广播方式推送，所有用户都可以接收到,
                // 可以设置对点推送
                .setNotification(Notification.alert(alert))
                .build();
    }

    /**
     * 构建推送对象：平台是 Android，目标是 tag的设备，通知内容是alert，并且标题为title。
     * @param tag
     * @param alert
     * @param title
     * @param extras
     * @return
     */
    public static PushPayload buildPushObject_android_tag_alertWithTitle(String tag,String alert,String title,Map<String, String> extras) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.tag())
                .setNotification(Notification.android(alert, title, extras))
                .build();
    }

    /**
     * 构建推送对象：平台是 iOS，推送目标是 tags(可以是一个设备也可以是多个设备)，推送内容同时包括通知与消息 - 通知信息是alert，消息内容是 msgContent，角标数字为badge(应用程序左上角或者右上角的数字)，通知声音为sound，并且附加字段 from = "JPush"。
     * 通知是 APNs 推送通道的，消息是 JPush 应用内消息通道的。
     * APNs 的推送环境是“生产”（如果不显式设置的话，Library 会默认指定为开发）
     * @param alert
     * @param msgContent
     * @param badge
     * @param sound
     * @param tags
     * @return
     */
    public static PushPayload buildPushObject_ios_tagAnd_alertWithExtrasAndMessage(Object alert,String msgContent,int badge,String sound,String...tags) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.tag_and(tags))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(alert)
                                .setBadge(badge)
                                .setSound(sound)
                                .addExtra("from", "JPush")
                                .build())
                        .build())
                 .setMessage(Message.content(msgContent))
                 .setOptions(Options.newBuilder()
                         .setApnsProduction(true)//true-推送生产环境 false-推送开发环境（测试使用参数）
                         .build())
                 .build();
    }
    
    /**
     * 构建推送对象：平台是 Andorid 与 iOS，推送的设备有（推送目标为tags和推送目标别名为aliases），推送内容是 - 内容为 msg_content的消息，并且附加字段 from = JPush。
     * @param msg_content
     * @param tags
     * @param aliases
     * @return
     */
    public static PushPayload buildPushObject_ios_audienceMore_messageWithExtras(String msg_content,String[] tags,String[] aliases) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.newBuilder()
                        .addAudienceTarget(AudienceTarget.tag(tags))
                        .addAudienceTarget(AudienceTarget.alias(aliases))
                        .build())
                .setMessage(Message.newBuilder()
                        .setMsgContent(msg_content)
                        .addExtra("from", "JPush")
                        .build())
                .build();
    }
    
    /**
     * 构建推送对象：推送内容包含SMS信息
     * @param title
     * @param sendSMSContent
     * @param delayTime
     * @param aliases
     */
    public static void testSendWithSMS(String title,String sendSMSContent,int delayTime,String... aliases) {
        JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY);
        try {
            SMS sms = SMS.content(sendSMSContent, delayTime);
            PushResult result = jpushClient.sendAndroidMessageWithAlias(title, sendSMSContent, sms, aliases);
            log.info("Got result - " + result);
        } catch (APIConnectionException e) {
        	log.error("Connection error. Should retry later. "+e);
        } catch (APIRequestException e) {
        	log.error("Error response from JPush server. Should review and fix it. "+e);
        	log.info("HTTP Status: " + e.getStatus());
        	log.info("Error Code: " + e.getErrorCode());
        	log.info("Error Message: " + e.getErrorMessage());
        }
    }
    
    
    
    
    public static void main(String[] args) {
        String master_secret=JiguangPush.MASTER_SECRET;
        String app_key=JiguangPush.APP_KEY;
        String registrationId="141fe1da9eae407f7b8";
        String alert="Hello";
        JPushClient jpushClient = new JPushClient(master_secret,app_key, null, ClientConfig.getInstance());
        //PushPayload payload = JiguangPush.buildPushObject_all_all_alert("消息推送");
        //PushPayload payload=PhicommPush.buildPushObject_android_tag_alertWithTitle("tag1", "123", "123", null);
        
        PushPayload payload=JiguangPush.buildPushObject_all_alias_alert(registrationId, alert);
        try {
        	
            PushResult result = jpushClient.sendPush(payload);
            log.info("Got result - " + result);

        } catch (APIConnectionException e) {
            // Connection error, should retry later
        	log.error("Connection error, should retry later "+e);

        } catch (APIRequestException e) {
            // Should review the error, and fix the request
            log.error("根据返回的错误信息核查请求是否正确"+e);
            log.info("HTTP 状态信息码: " + e.getStatus());
            log.info("JPush返回的错误码: " + e.getErrorCode());
            log.info("JPush返回的错误信息: " + e.getErrorMessage());
        }
    }
}