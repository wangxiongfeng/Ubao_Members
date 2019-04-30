var PDFMAP = new JsMap();
var PDFCOUNT = 0;
var PDFURL = "";
var CurrentScale = 1;
var MIN_SCALE = 0.25, MAX_SCALE = 5.0;

function GetImagePageUrl(Url, pageNum)
{
    return Url.replace(/\<\d+\>/ig, pageNum);
}
function GetImageCount(Url)
{
    var re = Url.match(/\<\d+\>/ig);
    if (!re || re.length == 0)
    {
        return 1;
    }
    return parseInt(re[0].replace(/\<|\>/ig, ""));
}
function AfterDownloadFile(Url, pageNum)
{
    ShowLog("AfterDownloadFile:" + pageNum);
    var callback = GetLoadFileCallBack(Url, pageNum);
    if (callback) callback();
}
function SetLoadFileCallBack(Url, pageNum, callback)
{
    PDFMAP.set(pageNum + "<>" + Url, callback);
}
function GetLoadFileCallBack(Url, pageNum)
{
    var callback = PDFMAP.get(pageNum + "<>" + Url);
    PDFMAP.remove(pageNum + "<>" + Url);
    return callback;
}
function PreLoadFile(Url, pageNum, callback, ishide)
{
    console.log("PreLoadFile:" + pageNum);
    var ispc = location.href.toLowerCase().indexOf("http") == 0;
    if (ispc || Url.toLowerCase().indexOf("http") == 0)
    {
        if (callback) callback();
        return;
    }
    SetLoadFileCallBack(Url, pageNum, callback);
    var showLoading = true;
    if (typeof (ishide) != "undefined" && ishide == true)
    {
        showLoading = false;
    }
    try
    {
        if (ISANDROID)
        {
            window.AnalyticsWebInterface.preLoadFileFunction(Url, pageNum, showLoading);
        }
        else
        {
            window.webkit.messageHandlers.PreLoadFileFunction.postMessage({ "Url": Url, "pageNum": pageNum, "showLoading": showLoading });
        }
    }
    catch (ex) { }
}
function ShowPDFFile(Url, pageNum, callback)
{
    PDFCOUNT = GetImageCount(Url);
    PDFURL = Url;
    $("#main").empty();
    CurrentScale = 1;
    ShowPDFPageEx(pageNum, function (issuccess)
    {
        if (AfterLoadPDF) AfterLoadPDF(pageNum);
        if (callback) callback(true);
    }, false, GetImagePageUrl(Url, pageNum), false);
}
function ShowPDFPage(pageNum, callback, ishide, needScale)
{
    PreLoadFile(PDFURL, pageNum, function ()
    {
        ShowPDFPageEx(pageNum, function (issuccess)
        {
            if (callback) callback(issuccess);
        }, ishide, GetImagePageUrl(PDFURL, pageNum), needScale);
    }, ishide);
}
function ShowPDFPageEx(pageNum, callback, ishide, url, needScale)
{
    if ($("#pageContainer" + pageNum).length > 0)
    {
        $("#pageContainer" + pageNum).remove();
    }
    var img = new Image();
    img.src = url;
    //console.log($(img).width());
    var container = document.createElement('div');
    container.id = 'pageContainer' + pageNum;
    container.className = 'pageContainer';
    container.style.width = getViewWidth() + 'px';
    container.style.height = getViewHeight()   + 'px';
    if (typeof (ishide) != "undefined" && ishide == true)
    {
        container.style.display = "none";
    }
    else
    {
        ishide = false;
        CurrentScale = 1;
    }
    container.setAttribute('CW', getViewWidth());
    container.innerHTML = "<div id='canvas" + pageNum + "' style='position:absolute;top:0px;left:0px;'><img id='img_" + pageNum + "' style='width:100%;'></div><svg id='svg" + pageNum + "' style='position:relative;top:0px;left:0px;'  width='100%' height='100%' version='1.1' xmlns='http://www.w3.org/2000/svg' xlink='http://www.w3.org/1999/xlink'></svg>";
    document.getElementById("main").appendChild(container);
    $("#canvas" + pageNum).width(getViewWidth());
    var img = gid("img_" + pageNum);
    img.onload = function ()
    {
        console.log('width:' + img.width + ',height:' + img.height);
        //debugger;
        //gid(this.id.replace("img_", "pageContainer")).style.height = this.height + "px";
        if (ishide)
        {
            $("#" + $(this).attr("id").replace("img_", "pageContainer")).height(getViewWidth()*this.height / this.width);
        } else
        {
            $("#" + $(this).attr("id").replace("img_", "pageContainer")).height(this.height);
            //$("#main").height(this.height);
        }
        
    };
    img.src = url;
    if (callback) callback(true);
}