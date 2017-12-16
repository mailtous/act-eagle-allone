/**
 * Created by lee on 12/15/17.
 * App.js
 */
var loadingtip = "数据加载中...请稍候...";
var errortip = "程序异常，请联系管理员";
var layer;
layui.use(['layer'], function () { // 加载 layui的功能模块
    layer = layui.layer;
});


window.eventBus = riot.observable();  // riot 的事件观察


/**
 * 提交
 * @param url
 * @param params
 * @param callback
 */
var post = function (url, params, callback) {
    if (!url || url == "") return;
    $.ajax({
        url: url,
        type: 'post',
        data: params,
        dataType: "json",
        success: function (data) {
            if (typeof callback == "function") {
                callback(data);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            layer.msg(errortip);
            console.log(errortip);
        }
    });
}


/**
 * 功能：页面HTML加载
 * @url 页面url
 * @data 搜索参数序列化对象
 * @$pageListPanel 页面HTML容器，默认为$("#htmlbox")
 */
var loadPage = function(url,params,$pageListPanel,callback) {
    if (!url || url == "") return;
    ajaxHTML(url,params,function (html) {
        var $panel = $pageListPanel || $("#htmlbox");
        $panel.html(html);
        if(typeof callback == "function"){
            callback();
        }
    });
}

var ajaxHTML= function(url,params,callback) {
    if (!url || url == "") return;
    var _url = url;
    _url += url.indexOf("?") == -1 ? "?" : "&";
    _url += "timer=" + new Date().getTime();
    console.log(loadingtip);
    layer.msg(loadingtip);
    $.ajax({
        url: url,
        data: params,
        dataType: "html",
        success: function (html) {
            if (typeof callback == "function") {
                callback(html);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            layer.msg(errortip);
            console.log(errortip);
        }
    });
}