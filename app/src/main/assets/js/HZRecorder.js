(function (window) {
    window.URL = window.URL || window.webkitURL;
    navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia;
    var HZRecorder = function (stream, config) {
        config = config || {};
        config.sampleBits = config.sampleBits || 8;
        config.sampleRate = config.sampleRate || (44100 / 6);
        var context = new (window.webkitAudioContext || window.AudioContext)();
        var audioInput = context.createMediaStreamSource(stream);
        var createScript = context.createScriptProcessor || context.createJavaScriptNode;
        var recorder = createScript.apply(context, [4096, 1, 1]);
        var audioData = {
            size: 0
            , buffer: []
            , inputSampleRate: context.sampleRate
            , inputSampleBits: 16 
            , outputSampleRate: config.sampleRate
            , oututSampleBits: config.sampleBits
            , input: function (data) {
                this.buffer.push(new Float32Array(data));
                this.size += data.length;
            }
            , compress: function () {
                var data = new Float32Array(this.size);
                var offset = 0;
                for (var i = 0; i < this.buffer.length; i++) {
                    data.set(this.buffer[i], offset);
                    offset += this.buffer[i].length;
                }
                var compression = parseInt(this.inputSampleRate / this.outputSampleRate);
                var length = data.length / compression;
                var result = new Float32Array(length);
                var index = 0, j = 0;
                while (index < length) {
                    result[index] = data[j];
                    j += compression;
                    index++;
                }
                return result;
            }
            , encodeWAV: function () {
                var sampleRate = Math.min(this.inputSampleRate, this.outputSampleRate);
                var sampleBits = Math.min(this.inputSampleBits, this.oututSampleBits);
                var bytes = this.compress();
                var dataLength = bytes.length * (sampleBits / 8);
                var buffer = new ArrayBuffer(44 + dataLength);
                var data = new DataView(buffer);
                var channelCount = 1;
                var offset = 0;
                var writeString = function (str) {
                    for (var i = 0; i < str.length; i++) {
                        data.setUint8(offset + i, str.charCodeAt(i));
                    }
                }
                writeString('RIFF'); offset += 4;
                data.setUint32(offset, 36 + dataLength, true); offset += 4;
                writeString('WAVE'); offset += 4;
                writeString('fmt '); offset += 4;
                data.setUint32(offset, 16, true); offset += 4;
                data.setUint16(offset, 1, true); offset += 2;
                data.setUint16(offset, channelCount, true); offset += 2;
                data.setUint32(offset, sampleRate, true); offset += 4;
                data.setUint32(offset, channelCount * sampleRate * (sampleBits / 8), true); offset += 4;
                data.setUint16(offset, channelCount * (sampleBits / 8), true); offset += 2;
                data.setUint16(offset, sampleBits, true); offset += 2;
                writeString('data'); offset += 4;
                data.setUint32(offset, dataLength, true); offset += 4;
                if (sampleBits === 8) {
                    for (var i = 0; i < bytes.length; i++, offset++) {
                        var s = Math.max(-1, Math.min(1, bytes[i]));
                        var val = s < 0 ? s * 0x8000 : s * 0x7FFF;
                        val = parseInt(255 / (65535 / (val + 32768)));
                        data.setInt8(offset, val, true);
                    }
                } else {
                    for (var i = 0; i < bytes.length; i++, offset += 2) {
                        var s = Math.max(-1, Math.min(1, bytes[i]));
                        data.setInt16(offset, s < 0 ? s * 0x8000 : s * 0x7FFF, true);
                    }
                }

                return new Blob([data], { type: 'audio/wav' });
            }
        };
        this.start = function () {
            audioInput.connect(recorder);
            recorder.connect(context.destination);
        }
        this.stop = function () {
            recorder.disconnect();
        }
        this.getBlob = function () {
            this.stop();
            return audioData.encodeWAV();
        }
        this.play = function (audio) {
            audio.src = window.URL.createObjectURL(this.getBlob());
        }
        this.upload = function (url, callback) {
            var fd = new FormData();
            fd.append("audioData", this.getBlob());
            var xhr = new XMLHttpRequest();
            if (callback) {
                xhr.upload.addEventListener("progress", function (e) {
                    callback('uploading', e);
                }, false);
                xhr.addEventListener("load", function (e) {
                    callback('ok', e);
                }, false);
                xhr.addEventListener("error", function (e) {
                    callback('error', e);
                }, false);
                xhr.addEventListener("abort", function (e) {
                    callback('cancel', e);
                }, false);
            }
            xhr.open("POST", url);
            xhr.send(fd);
        }
        this.getCurrentTime = function () {
            return context.currentTime;
        }
        this.getDestination = function () {
            return context.destination;
        }
        recorder.onaudioprocess = function (e) {
            audioData.input(e.inputBuffer.getChannelData(0));
        }
    };
    HZRecorder.throwError = function (message) {
        alert(message);
        throw new function () { this.toString = function () { return message; } }
    }
    HZRecorder.canRecording = (navigator.getUserMedia != null);
    HZRecorder.get = function (callback, config) {
        if (callback) {
            if (navigator.getUserMedia) {
                navigator.getUserMedia(
                    { audio: true }
                    , function (stream) {
                        var rec = new HZRecorder(stream, config);
                        callback(rec);
                    }
                    , function (error) {
                        switch (error.code || error.name) {
                            case 'PERMISSION_DENIED':
                            case 'PermissionDeniedError':
                                HZRecorder.throwError('用户拒绝提供信息。');
                                break;
                            case 'NOT_SUPPORTED_ERROR':
                            case 'NotSupportedError':
                                HZRecorder.throwError('浏览器不支持硬件设备。');
                                break;
                            case 'MANDATORY_UNSATISFIED_ERROR':
                            case 'MandatoryUnsatisfiedError':
                                HZRecorder.throwError('无法发现指定的硬件设备。');
                                break;
                            default:
                                HZRecorder.throwError('无法打开麦克风。异常信息:' + (error.code || error.name));
                                break;
                        }
                    });
            } else {
                HZRecorder.throwErr('当前浏览器不支持录音功能。'); return;
            }
        }
    }
    window.HZRecorder = HZRecorder;
})(window);