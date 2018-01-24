/**
 * Created by lee on 12/15/17.
 * App.js
 */
var loadingtip = "数据加载中...请稍候...";
var errortip = "程序异常，请联系管理员";
var layer,laypage,laytable,layform;
layui.use(['layer','laypage','table','form'], function () { // 加载 layui的功能模块
    layer = layui.layer;
    laypage = layui.laypage;
    laytable = layui.table;
    layform = layui.form;
});

window.eventBus = riot.observable();  // riot 的事件观察


/**
 * 提交
 * @param url
 * @param params
 * @param callback
 */
var sumbit = function (url, params, callback) {
    if (!url || url == "") return;
    $.ajax({
        url: url,
        type: 'post',
        data: params,
        dataType: "json",
        success: function (data) {
            if (typeof callback == "function") {
                callback(data);
            }else {
                layer.msg(data.msg);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            layer.msg(errortip);
            console.log("ERROR= "+data);
        }
    });
    return true;
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
    // console.log(loadingtip);
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

//页面分页搜索功能
var page={
    pageNoName:"pn",
    $pageListPanel:$("#htmlContainer"),
    pageUrl:'',
    loadPageHTML:function(url,data,$pageListPanel){
        $pageListPanel
    },
    search:function($queryForm,url,pn,callback){
        var This=this;
        var p = url.indexOf("?") == -1 ? "?" : "&";
        window.loadPage(url+p+This.pageNoName+'='+pn, $queryForm.serialize(),(this.$panel_edit?this.$pageListPanel:undefined),function () {
            if(typeof callback == "function"){
                callback();
            }
        });
    },
    initPageBar:function($page,$queryForm,url,pageOptions){
        var This=this;
        pageOptions.buttonClickCallback=function(pn){This.search($queryForm,url,pn)};
        $page.pager(pageOptions);
    },
    initSearch:function($queryForm,$searchBtn,url){
        var This=this;
        $searchBtn.on("click", function(){
            This.search($queryForm,url,1);
        });
    }
}