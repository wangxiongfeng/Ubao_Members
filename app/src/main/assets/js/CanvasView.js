PDFJS.workerSrc = 'js/pdf.worker.js';
PDFJS.cMapUrl = 'js/bcmaps/';
PDFJS.cMapPacked = true;
PDFJS.disableRange = true;
PDFJS.disableStream = true;
PDFJS.disableWorker = true;
var PDFOBJ = null;
var PDFLoadDoc = null;
var PDFRender = null;
var PDFCOUNT = 0;
var PDFURL = "";
var PDFMAP = new JsMap();
var CurrentScale = 2;

function GetPdfPageUrl(Url, pageNum)
{
    return Url.replace(/\<\d+\>/ig, pageNum);
}
function GetPdfCount(Url)
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
    PDFMAP.set(pageNum+"<>"+Url, callback);
}
function GetLoadFileCallBack(Url, pageNum)
{
    var callback = PDFMAP.get(pageNum + "<>" + Url);
    PDFMAP.remove(pageNum + "<>" + Url);
    return callback;
}
function PreLoadFile(Url, pageNum, callback, ishide)
{
    var ispc = location.href.toLowerCase().indexOf("http") == 0;
    if (ispc)
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
function ShowPDFFile(Url, pageNum, progress, callback)
{
    PDFURL = Url;
    PDFLoadDoc = PDFJS.getDocument(Url, null, null, function (obj)
    {
        if (progress) progress(obj.loaded / obj.total);
    });
    PDFLoadDoc.then(function (pdf)
    {
        PDFOBJ = pdf;
        PDFCOUNT = pdf.numPages;

        ShowPDFPageEx(pageNum, function (issuccess)
        {
            IsLoadingPage = false;
            if (issuccess == false)
            {
                if (callback) callback(false);
                return;
            }
            if (AfterLoadPDF) AfterLoadPDF(pageNum);
            if (callback) callback(true);
        }, false, pdf,true);
    }, function (err)
    {
        IsLoadingPage = false;
        PDFLoadDoc = null;
        if (err.message != "Loading aborted")
        {
            if (callback) callback(false);
        }
    });
    //task.destroy();
    //task.destroyed=true;
}
function ShowPDFPage(pageNum, callback, ishide, needScale)
{
    ShowLog("ShowPDFPage:" + pageNum);
    if (typeof (needScale) == "undefined")
    {
        needScale = false;
    }
    //ShowLog("ShowPDFPage:" + pageNum);

    ShowPDFPageEx(pageNum, function (issuccess)
    {
        if (callback) callback(issuccess);
    }, ishide, PDFOBJ, needScale);

    return;
    PreLoadFile(PDFURL, pageNum, function ()
    {
        PDFLoadDoc = PDFJS.getDocument(GetPdfPageUrl(PDFURL, pageNum));
        PDFLoadDoc.then(function (pdf)
        {
            ShowPDFPageEx(pageNum, function (issuccess)
            {
                if (callback) callback(issuccess);
            }, ishide, pdf, needScale);
        }, function (err)
            {
                //ShowPDFPage(pageNum, callback, ishide);
                IsLoadingPage = false;
                PDFLoadDoc = null;
                if (err.message != "Loading aborted")
                {
                    if (callback) callback(false);
                }
            });
    }, ishide);
}
function ShowPDFPageEx(pageNum, callback, ishide, pdf, needScale)
{
    ShowLog("ShowPDFPageEx:" + pageNum);
    if (PDFOBJ == null && pdf)
    {
        PDFOBJ = pdf;
    }
    if (PDFOBJ == null)
    {
        //setTimeout(function () { ShowPDFPage(pageNum, callback, ishide) }, 200);
        if (PDFURL != "")
        {
            ShowPDFFile(PDFURL, pageNum, null, callback);
        }
        return;
    }
    if (typeof (needScale) == "undefined")
    {
        needScale = false;
    }
    //PDFOBJ = pdf;
    PDFOBJ.getPage(pageNum).then(function (page)
    {
        //console.log("render page:" + pageNum);
        var scale = window.devicePixelRatio || 1;;
        var viewport = page.getViewport(scale);
        if (needScale == true)
        {
            if (IsToolBarShow || (document.body.clientWidth / document.body.clientHeight < viewport.width / viewport.height))
            {
                scale = document.body.clientWidth / (viewport.width + 0);
            }
            else
            {
                //scale = document.body.clientHeight / (viewport.height + 0);
                scale = document.body.clientWidth / (viewport.width + 0);
            }
            CurrentScale = scale;
        }
        else
        {
            scale = CurrentScale;
        }
        //viewport = page.getViewport(scale);
        if ($("#pageContainer" + pageNum).length > 0)
        {
            $("#pageContainer" + pageNum).remove();
        }
        var container = document.createElement('div');
        container.id = 'pageContainer' + pageNum;
        container.className = 'pageContainer';
        container.style.width = ((viewport.width * scale) | 0) + 'px';
        container.style.height = ((viewport.height * scale) | 0) + 'px';
        //document.getElementById("maincontainer").style.width = container.style.width;
        //document.getElementById("maincontainer").style.height = container.style.height;
        
        if (typeof (ishide) != "undefined" && ishide == true)
        {
            container.style.display = "none";
        }
        container.setAttribute('CW', viewport.width * scale | 0);
        //var canvasW = Math.max(document.body.clientWidth, ((viewport.width * scale) | 0));
        //var canvasH = Math.max(document.body.clientHeight, ((viewport.height * scale) | 0));
        //container.innerHTML = "<canvas id='canvas" + pageNum + "' style='position:absolute;top:0px;left:0px;zoom:" + scale + ";'></canvas><svg id='svg" + pageNum + "' style='position:absolute;top:0px;left:0px;'  width='" + canvasW + "'px' height='" + canvasH + "px' version='1.1' xmlns='http://www.w3.org/2000/svg' xlink='http://www.w3.org/1999/xlink'></svg>";
        container.innerHTML = "<canvas id='canvas" + pageNum + "' style='position:absolute;top:0px;left:0px;zoom:" + scale + ";'></canvas><svg id='svg" + pageNum + "' style='position:relative;top:0px;left:0px;'  width='100%' height='100%' version='1.1' xmlns='http://www.w3.org/2000/svg' xlink='http://www.w3.org/1999/xlink'></svg>";
        document.getElementById("main").appendChild(container);
        var canvas = document.getElementById('canvas' + pageNum);
        var context = canvas.getContext('2d');
        canvas.height = viewport.height;
        canvas.width = viewport.width;
        var renderContext = {
            canvasContext: context,
            viewport: viewport
        };
        //var dt1 = new Date();
        page.render(renderContext).then(function () {
            //page.cleanup();
            var dt2 = new Date();
            //ShowLog("render pdf:" + (dt2 - dt1));
            ShowLog("ShowPDFPageEx success:" + pageNum);
            if (needScale == true && IsToolBarShow)
            {
                Record_view();
            }
            if (callback) callback(true);
        }, function (err)
        {
            ShowLog("ShowPDFPageEx error:" + pageNum);
            PDFRender = null;
            if (err != "cancelled" && callback) callback(false);
        });
    }, function(err)
    {
        PDFOBJ = null;
        if (err.message != "Transport destroyed" && callback) callback(false);
    });
}
function CancelLoadPdf()
{
    ShowLog("CancelLoadPdf");
    IsLoadingPage = false;
    return new Promise(function(resolve, reject)
    {
        //if (PDFRender != null)
        //{
        //    PDFRender.cancel();
        //    PDFRender = null;
        //}
        //if (PDFOBJ != null)
        //{
        //    PDFOBJ.destroy();
        //    PDFOBJ = null;
        //}
        //PDFMAP.clear();
        //if (PDFLoadDoc != null)
        //{
        //    PDFLoadDoc.destroy().then(function()
        //    {
        //        resolve(true);
        //    }, function()
        //        {
        //            resolve(false);
        //        });
        //    PDFLoadDoc = null;
        //}
        //else
        //{
        //    resolve(true);
        //}
        resolve(true);
    });
}