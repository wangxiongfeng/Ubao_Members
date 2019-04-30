function _attachEvent(obj, evt, func) {
    if (obj.addEventListener) {
        obj.addEventListener(evt, func, true);
    } else if (obj.attachEvent) {
        obj.attachEvent("on" + evt, func);
    } else {
        eval("var old" + func + "=" + obj + ".on" + evt + ";");
        eval(obj + ".on" + evt + "=" + func + ";");
    }
}
function _detachEvent(obj, evt, func) {
    if (obj.removeEventListener) {
        obj.removeEventListener(evt, func, true);
    } else if (obj.detachEvent) {
        obj.detachEvent("on" + evt, func);
    } else {
        eval(obj + ".on" + evt + "=old" + func + ";");
    }
}
function _cancelDefault(e) {
    if (e.preventDefault) e.preventDefault();
    else e.returnValue = false;
}
function _cancelBubble(e) {
    if (e.stopPropagation) e.stopPropagation();
    else e.cancelBubble = true;
}
function ResizeWithShadow(resizeObj, event, direction, minWidth, minHeight, maxX, maxY) {
    if (typeof (resizeObj.CanResize) != 'undefined' && !resizeObj.CanResize)
        return;
    var objShadow;
    var parentOffsetLeft = 0, parentOffsetTop = 0;
    var obj = resizeObj;

    while (obj.parentNode && obj.parentNode.tagName != "BODY") {
        parentOffsetLeft += obj.parentNode.offsetLeft;
        parentOffsetTop += obj.parentNode.offsetTop;
        obj = obj.parentNode;
    }

    if (!document.getElementById('shadow')) {
        objShadow = document.createElement("DIV");
        objShadow.id = 'shadow';
        objShadow.style.position = 'absolute';
        objShadow.style.filter = 'alpha(opacity=30,style=0)';
        objShadow.style.backgroundColor = '#ccc';
        document.body.appendChild(objShadow);
    }
    else
        objShadow = document.getElementById('shadow');
    objShadow.style.zIndex = resizeObj.style.zIndex + 1;
    objShadow.style.left = parentOffsetLeft + resizeObj.offsetLeft + 'px';
    objShadow.style.top = parentOffsetTop + resizeObj.offsetTop + 'px';
    objShadow.style.width = resizeObj.offsetWidth + 'px';
    objShadow.style.height = resizeObj.offsetHeight + 'px';

    objShadow.style.display = '';

    Resize(objShadow, event, direction, minWidth, minHeight, maxX, maxY)
    _attachEvent(document, 'touchend', dragEnd);
    _cancelBubble(event);

    _cancelDefault(event);
    function dragEnd() {
        var chgX = 1;
        var chgY = 1;
        switch (direction) {
            case 'e':
                chgX = 1;
                chgY = 0;
                break;
            case 'n':
                chgX = 0;
                chgY = -1;
                break;
            case 'ne':
                chgX = 1;
                chgY = -1;
                break;
            case 'nw':
                chgX = -1;
                chgY = -1;
                break;
            case 's':
                chgX = 0;
                chgY = 1;
                break;
            case 'se':
                chgX = 1;
                chgY = 1;
                break;
            case 'sw':
                chgX = -1;
                chgY = 1;
                break;
            case 'w':
                chgX = -1;
                chgY = 0;
                break;
        }

        if (chgX < 0) {
            resizeObj.style.left = objShadow.offsetLeft - parentOffsetLeft + 'px';
        }
        if (chgY < 0) {
            resizeObj.style.top = objShadow.offsetTop - parentOffsetTop + 'px';
        }

        resizeObj.style.width = objShadow.style.width;
        resizeObj.style.height = objShadow.style.height;

        if (resizeObj.onsizechanged)
            eval(resizeObj.onsizechanged);
        else if (resizeObj.onSizeChanged)
            eval(resizeObj.onSizeChanged);

        if (resizeObj.onResizeCompleted)
            eval(resizeObj.onResizeCompleted);

        resizeObj.ResizeState = false;
        _detachEvent(document, 'mouseup', dragEnd);

        document.body.removeChild(objShadow);

        _cancelBubble(event);
    }
}
function Resize(resizeObj, event, direction, minWidth, minHeight, maxX, maxY) {
    if (typeof (resizeObj.CanResize) != 'undefined' && !resizeObj.CanResize) return;
    resizeObj.style.width = resizeObj.offsetWidth;
    resizeObj.style.height = resizeObj.offsetHeight;
    resizeObj.ResizeState = true;

    var oldX = event['touches'][0].clientX;
    var oldY = event['touches'][0].clientY;
    var oldWidth = resizeObj.offsetWidth;
    var oldHeight = resizeObj.offsetHeight;
    oldOffsetRight = screen.width - resizeObj.offsetLeft - resizeObj.offsetWidth;
    oldOffsetBottom = screen.height - resizeObj.offsetTop - resizeObj.offsetHeight;

    var isChanged = false;
    var newHeight = 0, newWidth = 0;

    _attachEvent(document, "touchmove", moveHandler);
    _attachEvent(document, 'touchend', resizeEnd);
    _cancelBubble(event);
    _cancelDefault(event);
    function moveHandler(e) {
        var evt = e;
        e = fixE(e);
        var chgX = 1;
        var chgY = 1;
        switch (direction) {
            case 'e':
                chgX = 1;
                chgY = 0;
                break;
            case 'n':
                chgX = 0;
                chgY = -1;
                break;
            case 'ne':
                chgX = 1;
                chgY = -1;
                break;
            case 'nw':
                chgX = -1;
                chgY = -1;
                break;
            case 's':
                chgX = 0;
                chgY = 1;
                break;
            case 'se':
                chgX = 1;
                chgY = 1;
                break;
            case 'sw':
                chgX = -1;
                chgY = 1;
                break;
            case 'w':
                chgX = -1;
                chgY = 0;
                break;
        }
        if (chgX < 0) {
            newWidth = (oldWidth + oldX - e.clientX);
        }
        else if (chgX > 0) {
            newWidth = (oldWidth + e.clientX - oldX);
        }
        if (chgY < 0) {
            newHeight = (oldHeight + oldY - e.clientY);
        }
        else if (chgY > 0) {
            newHeight = (oldHeight + e.clientY - oldY);
        }
        if (chgX != 0 && minWidth && newWidth < minWidth)
            newWidth = minWidth;

        if (chgY != 0 && minHeight && newHeight < minHeight)
            newHeight = minHeight;

        if (chgX < 0) {
            resizeObj.style.left = screen.width - oldOffsetRight - newWidth + 'px';
        }

        if (chgY < 0) {
            resizeObj.style.top = screen.height - oldOffsetBottom - newHeight + 'px';
        }

        if (newWidth > 0)
            resizeObj.style.width = newWidth + "px";
        if (newHeight > 0)
            resizeObj.style.height = newHeight + "px";

        if (resizeObj.onSizeChange) resizeObj.onSizeChange();
        _cancelDefault(evt);
        _cancelBubble(evt);
    }

    function resizeEnd() {
        if (resizeObj.offsetWidth != oldWidth || resizeObj.offsetHeight != oldHeight)
            isChanged = true;

        if (isChanged && resizeObj.getAttributeNode("onsizechanged"))
            eval(resizeObj.getAttributeNode("onsizechanged").nodeValue);
        else if (isChanged && resizeObj.getAttributeNode("onSizeChanged"))
            eval(resizeObj.resizeObj.getAttributeNode("onSizeChanged").nodeValue);


        if (resizeObj.getAttributeNode("onResizeCompleted"))
            eval(resizeObj.getAttributeNode("onResizeCompleted").nodeValue);
        if (resizeObj.onSizeChanged) resizeObj.onSizeChanged();

        resizeObj.ResizeState = false;
        _detachEvent(document, 'touchmove', moveHandler);
        _detachEvent(document, 'touchend', resizeEnd);

        _cancelBubble(event);
        if (resizeObj.layerid)
            ChangeLayer(resizeObj);
    }
    function fixE(e) {
        var result = null;
        if (typeof e == 'undefined') e = window.event;
        result = e['touches'][0];
        if (typeof result.layerX == 'undefined') result.layerX = result.offsetX;
        if (typeof result.layerY == 'undefined') result.layerY = result.offsetY;
        return result;
    }
}