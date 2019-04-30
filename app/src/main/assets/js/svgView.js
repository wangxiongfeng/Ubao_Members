var PDFDOC, PDFSVG, PDFAPI,PDFZOOM,PDFCOUNT,PDFURL;
/******/ (function(modules) { // webpackBootstrap
/******/ 	// install a JSONP callback for chunk loading
/******/ 	var parentJsonpFunction = window["webpackJsonp"];
/******/ 	window["webpackJsonp"] = function webpackJsonpCallback(chunkIds, moreModules, executeModules) {
/******/ 		// add "moreModules" to the modules object,
/******/ 		// then flag all "chunkIds" as loaded and fire callback
/******/ 		var moduleId, chunkId, i = 0, resolves = [], result;
/******/ 		for(;i < chunkIds.length; i++) {
/******/ 			chunkId = chunkIds[i];
/******/ 			if(installedChunks[chunkId]) {
/******/ 				resolves.push(installedChunks[chunkId][0]);
/******/ 			}
/******/ 			installedChunks[chunkId] = 0;
/******/ 		}
/******/ 		for(moduleId in moreModules) {
/******/ 			if(Object.prototype.hasOwnProperty.call(moreModules, moduleId)) {
/******/ 				modules[moduleId] = moreModules[moduleId];
/******/ 			}
/******/ 		}
/******/ 		if(parentJsonpFunction) parentJsonpFunction(chunkIds, moreModules, executeModules);
/******/ 		while(resolves.length) {
/******/ 			resolves.shift()();
/******/ 		}
/******/
/******/ 	};
/******/
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// objects to store loaded and loading chunks
/******/ 	var installedChunks = {
/******/ 		3: 0
/******/ 	};
/******/
/******/ 	// The require function
/******/ 	function __w_pdfjs_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId]) {
/******/ 			return installedModules[moduleId].exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __w_pdfjs_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/ 	// This file contains only the entry chunk.
/******/ 	// The chunk loading function for additional chunks
/******/ 	__w_pdfjs_require__.e = function requireEnsure(chunkId) {
/******/ 		var installedChunkData = installedChunks[chunkId];
/******/ 		if(installedChunkData === 0) {
/******/ 			return new Promise(function(resolve) { resolve(); });
/******/ 		}
/******/
/******/ 		// a Promise means "currently loading".
/******/ 		if(installedChunkData) {
/******/ 			return installedChunkData[2];
/******/ 		}
/******/
/******/ 		// setup Promise in chunk cache
/******/ 		var promise = new Promise(function(resolve, reject) {
/******/ 			installedChunkData = installedChunks[chunkId] = [resolve, reject];
/******/ 		});
/******/ 		installedChunkData[2] = promise;
/******/
/******/ 		// start chunk loading
/******/ 		var head = document.getElementsByTagName('head')[0];
/******/ 		var script = document.createElement('script');
/******/ 		script.type = 'text/javascript';
/******/ 		script.charset = 'utf-8';
/******/ 		script.async = true;
/******/ 		script.timeout = 120000;
/******/
/******/ 		if (__w_pdfjs_require__.nc) {
/******/ 			script.setAttribute("nonce", __w_pdfjs_require__.nc);
/******/ 		}
/******/ 		script.src = __w_pdfjs_require__.p + "js/" + chunkId + ".svgView.js";
/******/ 		var timeout = setTimeout(onScriptComplete, 120000);
/******/ 		script.onerror = script.onload = onScriptComplete;
/******/ 		function onScriptComplete() {
/******/ 			// avoid mem leaks in IE.
/******/ 			script.onerror = script.onload = null;
/******/ 			clearTimeout(timeout);
/******/ 			var chunk = installedChunks[chunkId];
/******/ 			if(chunk !== 0) {
/******/ 				if(chunk) {
/******/ 					chunk[1](new Error('Loading chunk ' + chunkId + ' failed.'));
/******/ 				}
/******/ 				installedChunks[chunkId] = undefined;
/******/ 			}
/******/ 		};
/******/ 		head.appendChild(script);
/******/
/******/ 		return promise;
/******/ 	};
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__w_pdfjs_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__w_pdfjs_require__.c = installedModules;
/******/
/******/ 	// identity function for calling harmony imports with the correct context
/******/ 	__w_pdfjs_require__.i = function(value) { return value; };
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__w_pdfjs_require__.d = function(exports, name, getter) {
/******/ 		if(!__w_pdfjs_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, {
/******/ 				configurable: false,
/******/ 				enumerable: true,
/******/ 				get: getter
/******/ 			});
/******/ 		}
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__w_pdfjs_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__w_pdfjs_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__w_pdfjs_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__w_pdfjs_require__.p = "";
/******/
/******/ 	// on error function for async loading
/******/ 	__w_pdfjs_require__.oe = function(err) { console.error(err); throw err; };
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __w_pdfjs_require__(__w_pdfjs_require__.s = 3);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */
/***/ (function(module, exports, __w_pdfjs_require__) {

"use strict";


module.exports = {
  import: function _import() {
    throw new Error("System.import cannot be used indirectly");
  }
};

/***/ }),
/* 1 */,
/* 2 */,
/* 3 */
/***/ (function(module, exports, __w_pdfjs_require__) {

"use strict";
/* WEBPACK VAR INJECTION */(function(System) {

var DEFAULT_SCALE = 1;
var query = document.location.href.replace(/^[^?]*(\?([^#]*))?(#.*)?/, '$2');
var queryParams = query ? JSON.parse('{' + query.split('&').map(function (a) {
  return a.split('=').map(decodeURIComponent).map(JSON.stringify).join(': ');
}).join(',') + '}') : {};
var url = queryParams.file || '1.pdf';
function renderDocument(pdf, svgLib) {
  var promise = Promise.resolve();
  for (var i = 1; i <= pdf.numPages; i++) {
    promise = promise.then(function (pageNum) {
      return pdf.getPage(pageNum).then(function (page) {
        var viewport = page.getViewport(DEFAULT_SCALE);
        var container = document.createElement('div');
        container.id = 'pageContainer' + pageNum;
        container.className = 'pageContainer';
        container.style.width = viewport.width + 'px';
        container.style.height = viewport.height + 'px';
        document.getElementById("main").appendChild(container);
        return page.getOperatorList().then(function (opList) {
          var svgGfx = new svgLib.SVGGraphics(page.commonObjs, page.objs);
          return svgGfx.getSVG(opList, viewport).then(function (svg) {
            container.appendChild(svg);
          });
        });
      });
    }.bind(null, i));
  }
}
    function renderDocumentNum(pdf, svgLib, pageNum) {
        return pdf.getPage(pageNum).then(function (page) {
            var viewport = page.getViewport(DEFAULT_SCALE);
            var container = document.createElement('div');
            container.id = 'pageContainer' + pageNum;
            container.className = 'pageContainer';
            container.style.width = viewport.width + 'px';
            container.style.height = viewport.height + 'px';
            document.getElementById("main").appendChild(container);

            return page.getOperatorList().then(function (opList) {
                var svgGfx = new svgLib.SVGGraphics(page.commonObjs, page.objs);
                return svgGfx.getSVG(opList, viewport).then(function (svg) {
                    container.appendChild(svg);
                });
            });
        });
    }
    
Promise.all([__w_pdfjs_require__.e/* import() */(1).then(__w_pdfjs_require__.bind(null, 1)), __w_pdfjs_require__.e/* import() */(2).then(__w_pdfjs_require__.bind(null, 2)), __w_pdfjs_require__.e/* import() */(0).then(__w_pdfjs_require__.bind(null, 4))]).then(function (modules) {
  var api = modules[0],
      svg = modules[1],
      global = modules[2];
  global.PDFJS.workerSrc ='js/pdf.worker.js';
  global.PDFJS.cMapUrl = 'js/bcmaps/';
  global.PDFJS.cMapPacked = true;
  PDFSVG = svg;
  PDFAPI = api;
  //api.getDocument(url).then(function (doc) {
  //    renderDocumentNum(doc, svg,1);
  //});
});
/* WEBPACK VAR INJECTION */}.call(exports, __w_pdfjs_require__(0)))

/***/ })
/******/ ]);
//# sourceMappingURL=svgView.js.map
function ShowPDFFile(Url, pageNum, progress) {
    pageNum = pageNum | 1;
    PDFURL = Url;
    PDFAPI.getDocument(Url, null, null, function (obj) {
        if (progress) progress(obj.loaded / obj.total);
    }).then(function (doc) {
        PDFDOC = doc;
        PDFCOUNT = doc.numPages;
        ShowPDFPage(pageNum, function () {
            if (AfterLoadPDF) AfterLoadPDF(pageNum);
        });
    });
}
function ShowPDFPage(pageNum, callback, ishide) {
    var pdf = PDFDOC;
    var svgLib = PDFSVG;
    if (pdf==null || typeof(pdf)=="undefined" || typeof (pdf.getPage) != "function")
    {
        ShowPDF(PDFURL, pageNum, callback)
        return;
    }
    return pdf.getPage(pageNum).then(function (page) {
        var viewport = page.getViewport(1);
        PDFZOOM = document.body.clientWidth / (viewport.width + 5);
        viewport = page.getViewport(PDFZOOM);
        var container = document.createElement('div');
        container.id = 'pageContainer' + pageNum;
        container.className = 'pageContainer';
        container.style.width = viewport.width + 'px';
        container.style.height = viewport.height + 'px';
        if (typeof (ishide) != "undefined" && ishide == true) {
            container.style.display = "none";
        }
        container.setAttribute('CW', viewport.width);
        document.getElementById("main").appendChild(container);

        return page.getOperatorList().then(function (opList) {
            var svgGfx = new svgLib.SVGGraphics(page.commonObjs, page.objs);
            return svgGfx.getSVG(opList, viewport).then(function (svg) {
                container.appendChild(svg);
                if (callback) callback();
            });
        });
    });
}