// All the content in this article is only for learning and communication use, not for any other purpose, strictly prohibited for commercial use and illegal use, otherwise all the consequences are irrelevant to the author!
// copy from https://github.com/ShilongLee/Crawler/tree/main/lib/js thanks for ShilongLee
function rc4_encrypt(plaintext, key) {
    var s = [];
    for (var i = 0; i < 256; i++) {
        s[i] = i;
    }
    var j = 0;
    for (var i = 0; i < 256; i++) {
        j = (j + s[i] + key.charCodeAt(i % key.length)) % 256;
        var temp = s[i];
        s[i] = s[j];
        s[j] = temp;
    }

    var i = 0;
    var j = 0;
    var cipher = [];
    for (var k = 0; k < plaintext.length; k++) {
        i = (i + 1) % 256;
        j = (j + s[i]) % 256;
        var temp = s[i];
        s[i] = s[j];
        s[j] = temp;
        var t = (s[i] + s[j]) % 256;
        cipher.push(String.fromCharCode(s[t] ^ plaintext.charCodeAt(k)));
    }
    return cipher.join('');
}

function le(e, r) {
    return (e << (r %= 32) | e >>> 32 - r) >>> 0
}

function de(e) {
    return 0 <= e && e < 16 ? 2043430169 : 16 <= e && e < 64 ? 2055708042 : void console['error']("invalid j for constant Tj")
}

function pe(e, r, t, n) {
    return 0 <= e && e < 16 ? (r ^ t ^ n) >>> 0 : 16 <= e && e < 64 ? (r & t | r & n | t & n) >>> 0 : (console['error']('invalid j for bool function FF'),
        0)
}

function he(e, r, t, n) {
    return 0 <= e && e < 16 ? (r ^ t ^ n) >>> 0 : 16 <= e && e < 64 ? (r & t | ~r & n) >>> 0 : (console['error']('invalid j for bool function GG'),
        0)
}

function reset() {
    this.reg[0] = 1937774191,
        this.reg[1] = 1226093241,
        this.reg[2] = 388252375,
        this.reg[3] = 3666478592,
        this.reg[4] = 2842636476,
        this.reg[5] = 372324522,
        this.reg[6] = 3817729613,
        this.reg[7] = 2969243214,
        this["chunk"] = [],
        this["size"] = 0
}

function write(e) {
    var a = "string" == typeof e ? function (e) {
        n = encodeURIComponent(e)['replace'](/%([0-9A-F]{2})/g, (function (e, r) {
                return String['fromCharCode']("0x" + r)
            }
        ))
            , a = new Array(n['length']);
        return Array['prototype']['forEach']['call'](n, (function (e, r) {
                a[r] = e.charCodeAt(0)
            }
        )),
            a
    }(e) : e;
    this.size += a.length;
    var f = 64 - this['chunk']['length'];
    if (a['length'] < f)
        this['chunk'] = this['chunk'].concat(a);
    else
        for (this['chunk'] = this['chunk'].concat(a.slice(0, f)); this['chunk'].length >= 64;)
            this['_compress'](this['chunk']),
                f < a['length'] ? this['chunk'] = a['slice'](f, Math['min'](f + 64, a['length'])) : this['chunk'] = [],
                f += 64
}

function sum(e, t) {
    e && (this['reset'](),
        this['write'](e)),
        this['_fill']();
    for (var f = 0; f < this.chunk['length']; f += 64)
        this._compress(this['chunk']['slice'](f, f + 64));
    var i = null;
    if (t == 'hex') {
        i = "";
        for (f = 0; f < 8; f++)
            i += se(this['reg'][f]['toString'](16), 8, "0")
    } else
        for (i = new Array(32),
                 f = 0; f < 8; f++) {
            var c = this.reg[f];
            i[4 * f + 3] = (255 & c) >>> 0,
                c >>>= 8,
                i[4 * f + 2] = (255 & c) >>> 0,
                c >>>= 8,
                i[4 * f + 1] = (255 & c) >>> 0,
                c >>>= 8,
                i[4 * f] = (255 & c) >>> 0
        }
    return this['reset'](),
        i
}

function _compress(t) {
    if (t < 64)
        console.error("compress error: not enough data");
    else {
        for (var f = function (e) {
            for (var r = new Array(132), t = 0; t < 16; t++)
                r[t] = e[4 * t] << 24,
                    r[t] |= e[4 * t + 1] << 16,
                    r[t] |= e[4 * t + 2] << 8,
                    r[t] |= e[4 * t + 3],
                    r[t] >>>= 0;
            for (var n = 16; n < 68; n++) {
                var a = r[n - 16] ^ r[n - 9] ^ le(r[n - 3], 15);
                a = a ^ le(a, 15) ^ le(a, 23),
                    r[n] = (a ^ le(r[n - 13], 7) ^ r[n - 6]) >>> 0
            }
            for (n = 0; n < 64; n++)
                r[n + 68] = (r[n] ^ r[n + 4]) >>> 0;
            return r
        }(t), i = this['reg'].slice(0), c = 0; c < 64; c++) {
            var o = le(i[0], 12) + i[4] + le(de(c), c)
                , s = ((o = le(o = (4294967295 & o) >>> 0, 7)) ^ le(i[0], 12)) >>> 0
                , u = pe(c, i[0], i[1], i[2]);
            u = (4294967295 & (u = u + i[3] + s + f[c + 68])) >>> 0;
            var b = he(c, i[4], i[5], i[6]);
            b = (4294967295 & (b = b + i[7] + o + f[c])) >>> 0,
                i[3] = i[2],
                i[2] = le(i[1], 9),
                i[1] = i[0],
                i[0] = u,
                i[7] = i[6],
                i[6] = le(i[5], 19),
                i[5] = i[4],
                i[4] = (b ^ le(b, 9) ^ le(b, 17)) >>> 0
        }
        for (var l = 0; l < 8; l++)
            this['reg'][l] = (this['reg'][l] ^ i[l]) >>> 0
    }
}

function _fill() {
    var a = 8 * this['size']
        , f = this['chunk']['push'](128) % 64;
    for (64 - f < 8 && (f -= 64); f < 56; f++)
        this.chunk['push'](0);
    for (var i = 0; i < 4; i++) {
        var c = Math['floor'](a / 4294967296);
        this['chunk'].push(c >>> 8 * (3 - i) & 255)
    }
    for (i = 0; i < 4; i++)
        this['chunk']['push'](a >>> 8 * (3 - i) & 255)

}

function SM3() {
    this.reg = [];
    this.chunk = [];
    this.size = 0;
    this.reset()
}
SM3.prototype.reset = reset;
SM3.prototype.write = write;
SM3.prototype.sum = sum;
SM3.prototype._compress = _compress;
SM3.prototype._fill = _fill;

function result_encrypt(long_str, num = null) {
    let s_obj = {
        "s0": "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",
        "s1": "Dkdpgh4ZKsQB80/Mfvw36XI1R25+WUAlEi7NLboqYTOPuzmFjJnryx9HVGcaStCe=",
        "s2": "Dkdpgh4ZKsQB80/Mfvw36XI1R25-WUAlEi7NLboqYTOPuzmFjJnryx9HVGcaStCe=",
        "s3": "ckdp1h4ZKsUB80/Mfvw36XIgR25+WQAlEi7NLboqYTOPuzmFjJnryx9HVGDaStCe",
        "s4": "Dkdpgh2ZmsQB80/MfvV36XI1R45-WUAlEixNLwoqYTOPuzKFjJnry79HbGcaStCe"
    }
    let constant = {
        "0": 16515072,
        "1": 258048,
        "2": 4032,
        "str": s_obj[num],
    }

    let result = "";
    let lound = 0;
    let long_int = get_long_int(lound, long_str);
    for (let i = 0; i < long_str.length / 3 * 4; i++) {
        if (Math.floor(i / 4) !== lound) {
            lound += 1;
            long_int = get_long_int(lound, long_str);
        }
        let key = i % 4;
        switch (key) {
            case 0:
                temp_int = (long_int & constant["0"]) >> 18;
                result += constant["str"].charAt(temp_int);
                break;
            case 1:
                temp_int = (long_int & constant["1"]) >> 12;
                result += constant["str"].charAt(temp_int);
                break;
            case 2:
                temp_int = (long_int & constant["2"]) >> 6;
                result += constant["str"].charAt(temp_int);
                break;
            case 3:
                temp_int = long_int & 63;
                result += constant["str"].charAt(temp_int);
                break;
            default:
                break;
        }
    }
    return result;
}

function get_long_int(round, long_str) {
    round = round * 3;
    return (long_str.charCodeAt(round) << 16) | (long_str.charCodeAt(round + 1) << 8) | (long_str.charCodeAt(round + 2));
}

function gener_random(random, option) {
    return [
        (random & 255 & 170) | option[0] & 85, // 163
        (random & 255 & 85) | option[0] & 170, //87
        (random >> 8 & 255 & 170) | option[1] & 85, //37
        (random >> 8 & 255 & 85) | option[1] & 170, //41
    ]
}

//////////////////////////////////////////////
function generate_rc4_bb_str(url_search_params, user_agent, window_env_str, suffix = "cus", Arguments = [0, 1, 14]) {
    let sm3 = new SM3()
    let start_time = Date.now()
    /**
     * 进行3次加密处理
     * 1: url_search_params两次sm3之的结果
     * 2: 对后缀两次sm3之的结果
     * 3: 对ua处理之后的结果
     */
        // url_search_params两次sm3之的结果
    let url_search_params_list = sm3.sum(sm3.sum(url_search_params + suffix))
    // 对后缀两次sm3之的结果
    let cus = sm3.sum(sm3.sum(suffix))
    // 对ua处理之后的结果
    let ua = sm3.sum(result_encrypt(rc4_encrypt(user_agent, String.fromCharCode.apply(null, [0.00390625, 1, Arguments[2]])), "s3"))
    //
    let end_time = Date.now()
    // b
    let b = {
        8: 3, // 固定
        10: end_time, //3次加密结束时间
        15: {
            "aid": 6383,
            "pageId": 6241,
            "boe": false,
            "ddrt": 7,
            "paths": {
                "include": [
                    {},
                    {},
                    {},
                    {},
                    {},
                    {},
                    {}
                ],
                "exclude": []
            },
            "track": {
                "mode": 0,
                "delay": 300,
                "paths": []
            },
            "dump": true,
            "rpU": ""
        },
        16: start_time, //3次加密开始时间
        18: 44, //固定
        19: [1, 0, 1, 5],
    }

    //3次加密开始时间
    b[20] = (b[16] >> 24) & 255
    b[21] = (b[16] >> 16) & 255
    b[22] = (b[16] >> 8) & 255
    b[23] = b[16] & 255
    b[24] = (b[16] / 256 / 256 / 256 / 256) >> 0
    b[25] = (b[16] / 256 / 256 / 256 / 256 / 256) >> 0

    // 参数Arguments [0, 1, 14, ...]
    // let Arguments = [0, 1, 14]
    b[26] = (Arguments[0] >> 24) & 255
    b[27] = (Arguments[0] >> 16) & 255
    b[28] = (Arguments[0] >> 8) & 255
    b[29] = Arguments[0] & 255

    b[30] = (Arguments[1] / 256) & 255
    b[31] = (Arguments[1] % 256) & 255
    b[32] = (Arguments[1] >> 24) & 255
    b[33] = (Arguments[1] >> 16) & 255

    b[34] = (Arguments[2] >> 24) & 255
    b[35] = (Arguments[2] >> 16) & 255
    b[36] = (Arguments[2] >> 8) & 255
    b[37] = Arguments[2] & 255

    // (url_search_params + "cus") 两次sm3之的结果
    /**let url_search_params_list = [
     91, 186,  35,  86, 143, 253,   6,  76,
     34,  21, 167, 148,   7,  42, 192, 219,
     188,  20, 182,  85, 213,  74, 213, 147,
     37, 155,  93, 139,  85, 118, 228, 213
     ]*/
    b[38] = url_search_params_list[21]
    b[39] = url_search_params_list[22]

    // ("cus") 对后缀两次sm3之的结果
    /**
     * let cus = [
     136, 101, 114, 147,  58,  77, 207, 201,
     215, 162, 154,  93, 248,  13, 142, 160,
     105,  73, 215, 241,  83,  58,  51,  43,
     255,  38, 168, 141, 216, 194,  35, 236
     ]*/
    b[40] = cus[21]
    b[41] = cus[22]

    // 对ua处理之后的结果
    /**
     * let ua = [
     129, 190,  70, 186,  86, 196, 199,  53,
     99,  38,  29, 209, 243,  17, 157,  69,
     147, 104,  53,  23, 114, 126,  66, 228,
     135,  30, 168, 185, 109, 156, 251,  88
     ]*/
    b[42] = ua[23]
    b[43] = ua[24]

    //3次加密结束时间
    b[44] = (b[10] >> 24) & 255
    b[45] = (b[10] >> 16) & 255
    b[46] = (b[10] >> 8) & 255
    b[47] = b[10] & 255
    b[48] = b[8]
    b[49] = (b[10] / 256 / 256 / 256 / 256) >> 0
    b[50] = (b[10] / 256 / 256 / 256 / 256 / 256) >> 0


    // object配置项
    b[51] = b[15]['pageId']
    b[52] = (b[15]['pageId'] >> 24) & 255
    b[53] = (b[15]['pageId'] >> 16) & 255
    b[54] = (b[15]['pageId'] >> 8) & 255
    b[55] = b[15]['pageId'] & 255

    b[56] = b[15]['aid']
    b[57] = b[15]['aid'] & 255
    b[58] = (b[15]['aid'] >> 8) & 255
    b[59] = (b[15]['aid'] >> 16) & 255
    b[60] = (b[15]['aid'] >> 24) & 255

    // 中间进行了环境检测
    // 代码索引:  2496 索引值:  17 （索引64关键条件）
    // '1536|747|1536|834|0|30|0|0|1536|834|1536|864|1525|747|24|24|Win32'.charCodeAt()得到65位数组
    /**
     * let window_env_list = [49, 53, 51, 54, 124, 55, 52, 55, 124, 49, 53, 51, 54, 124, 56, 51, 52, 124, 48, 124, 51,
     * 48, 124, 48, 124, 48, 124, 49, 53, 51, 54, 124, 56, 51, 52, 124, 49, 53, 51, 54, 124, 56,
     * 54, 52, 124, 49, 53, 50, 53, 124, 55, 52, 55, 124, 50, 52, 124, 50, 52, 124, 87, 105, 110,
     * 51, 50]
     */
    let window_env_list = [];
    for (let index = 0; index < window_env_str.length; index++) {
        window_env_list.push(window_env_str.charCodeAt(index))
    }
    b[64] = window_env_list.length
    b[65] = b[64] & 255
    b[66] = (b[64] >> 8) & 255

    b[69] = [].length
    b[70] = b[69] & 255
    b[71] = (b[69] >> 8) & 255

    b[72] = b[18] ^ b[20] ^ b[26] ^ b[30] ^ b[38] ^ b[40] ^ b[42] ^ b[21] ^ b[27] ^ b[31] ^ b[35] ^ b[39] ^ b[41] ^ b[43] ^ b[22] ^
        b[28] ^ b[32] ^ b[36] ^ b[23] ^ b[29] ^ b[33] ^ b[37] ^ b[44] ^ b[45] ^ b[46] ^ b[47] ^ b[48] ^ b[49] ^ b[50] ^ b[24] ^
        b[25] ^ b[52] ^ b[53] ^ b[54] ^ b[55] ^ b[57] ^ b[58] ^ b[59] ^ b[60] ^ b[65] ^ b[66] ^ b[70] ^ b[71]
    let bb = [
        b[18], b[20], b[52], b[26], b[30], b[34], b[58], b[38], b[40], b[53], b[42], b[21], b[27], b[54], b[55], b[31],
        b[35], b[57], b[39], b[41], b[43], b[22], b[28], b[32], b[60], b[36], b[23], b[29], b[33], b[37], b[44], b[45],
        b[59], b[46], b[47], b[48], b[49], b[50], b[24], b[25], b[65], b[66], b[70], b[71]
    ]
    bb = bb.concat(window_env_list).concat(b[72])
    return rc4_encrypt(String.fromCharCode.apply(null, bb), String.fromCharCode.apply(null, [121]));
}



function wordsToBytes(e, n) {
    e.constructor == String ? e = n && "binary" === n.encoding ? l.stringToBytes(e) : l.stringToBytes(e) : o(e) ? e = Array.prototype.slice.call(e, 0) : !Array.isArray(e) && e.constructor !== Uint8Array && (e = e.toString());
    for (var i = t.bytesToWords(e), a = 8 * e.length, d = 1732584193, h = -271733879, c = -1732584194, m = 271733878, u = 0; u < i.length; u++)
        i[u] = (i[u] << 8 | i[u] >>> 24) & 16711935 | (i[u] << 24 | i[u] >>> 8) & 4278255360;
    i[a >>> 5] |= 128 << a % 32,
        i[(a + 64 >>> 9 << 4) + 14] = a;
    for (var b = s._ff, P = s._gg, f = s._hh, p = s._ii, u = 0; u < i.length; u += 16) {
        var v = d
            , g = h
            , G = c
            , y = m;
        d = b(d, h, c, m, i[u + 0], 7, -680876936),
            m = b(m, d, h, c, i[u + 1], 12, -389564586),
            c = b(c, m, d, h, i[u + 2], 17, 606105819),
            h = b(h, c, m, d, i[u + 3], 22, -1044525330),
            d = b(d, h, c, m, i[u + 4], 7, -176418897),
            m = b(m, d, h, c, i[u + 5], 12, 1200080426),
            c = b(c, m, d, h, i[u + 6], 17, -1473231341),
            h = b(h, c, m, d, i[u + 7], 22, -45705983),
            d = b(d, h, c, m, i[u + 8], 7, 1770035416),
            m = b(m, d, h, c, i[u + 9], 12, -1958414417),
            c = b(c, m, d, h, i[u + 10], 17, -42063),
            h = b(h, c, m, d, i[u + 11], 22, -1990404162),
            d = b(d, h, c, m, i[u + 12], 7, 1804603682),
            m = b(m, d, h, c, i[u + 13], 12, -40341101),
            c = b(c, m, d, h, i[u + 14], 17, -1502002290),
            h = b(h, c, m, d, i[u + 15], 22, 1236535329),
            d = P(d, h, c, m, i[u + 1], 5, -165796510),
            m = P(m, d, h, c, i[u + 6], 9, -1069501632),
            c = P(c, m, d, h, i[u + 11], 14, 643717713),
            h = P(h, c, m, d, i[u + 0], 20, -373897302),
            d = P(d, h, c, m, i[u + 5], 5, -701558691),
            m = P(m, d, h, c, i[u + 10], 9, 38016083),
            c = P(c, m, d, h, i[u + 15], 14, -660478335),
            h = P(h, c, m, d, i[u + 4], 20, -405537848),
            d = P(d, h, c, m, i[u + 9], 5, 568446438),
            m = P(m, d, h, c, i[u + 14], 9, -1019803690),
            c = P(c, m, d, h, i[u + 3], 14, -187363961),
            h = P(h, c, m, d, i[u + 8], 20, 1163531501),
            d = P(d, h, c, m, i[u + 13], 5, -1444681467),
            m = P(m, d, h, c, i[u + 2], 9, -51403784),
            c = P(c, m, d, h, i[u + 7], 14, 1735328473),
            h = P(h, c, m, d, i[u + 12], 20, -1926607734),
            d = f(d, h, c, m, i[u + 5], 4, -378558),
            m = f(m, d, h, c, i[u + 8], 11, -2022574463),
            c = f(c, m, d, h, i[u + 11], 16, 1839030562),
            h = f(h, c, m, d, i[u + 14], 23, -35309556),
            d = f(d, h, c, m, i[u + 1], 4, -1530992060),
            m = f(m, d, h, c, i[u + 4], 11, 1272893353),
            c = f(c, m, d, h, i[u + 7], 16, -155497632),
            h = f(h, c, m, d, i[u + 10], 23, -1094730640),
            d = f(d, h, c, m, i[u + 13], 4, 681279174),
            m = f(m, d, h, c, i[u + 0], 11, -358537222),
            c = f(c, m, d, h, i[u + 3], 16, -722521979),
            h = f(h, c, m, d, i[u + 6], 23, 76029189),
            d = f(d, h, c, m, i[u + 9], 4, -640364487),
            m = f(m, d, h, c, i[u + 12], 11, -421815835),
            c = f(c, m, d, h, i[u + 15], 16, 530742520),
            h = f(h, c, m, d, i[u + 2], 23, -995338651),
            d = p(d, h, c, m, i[u + 0], 6, -198630844),
            m = p(m, d, h, c, i[u + 7], 10, 1126891415),
            c = p(c, m, d, h, i[u + 14], 15, -1416354905),
            h = p(h, c, m, d, i[u + 5], 21, -57434055),
            d = p(d, h, c, m, i[u + 12], 6, 1700485571),
            m = p(m, d, h, c, i[u + 3], 10, -1894986606),
            c = p(c, m, d, h, i[u + 10], 15, -1051523),
            h = p(h, c, m, d, i[u + 1], 21, -2054922799),
            d = p(d, h, c, m, i[u + 8], 6, 1873313359),
            m = p(m, d, h, c, i[u + 15], 10, -30611744),
            c = p(c, m, d, h, i[u + 6], 15, -1560198380),
            h = p(h, c, m, d, i[u + 13], 21, 1309151649),
            d = p(d, h, c, m, i[u + 4], 6, -145523070),
            m = p(m, d, h, c, i[u + 11], 10, -1120210379),
            c = p(c, m, d, h, i[u + 2], 15, 718787259),
            h = p(h, c, m, d, i[u + 9], 21, -343485551),
            d = d + v >>> 0,
            h = h + g >>> 0,
            c = c + G >>> 0,
            m = m + y >>> 0
    }
    return  t.bytesToHex(t.wordsToBytes(t.endian([d, h, c, m])));
}

// l.stringToBytes: 简单的 binary 编码，直接按字符编码值转成字节（0-255）
const l = {
    stringToBytes: function(e) {
        for (var n = [], i = 0; i < e.length; i++)
            n.push(255 & e.charCodeAt(i));
        return n
    },
    bytesToString: function(e) {
        for (var n = [], i = 0; i < e.length; i++)
            n.push(String.fromCharCode(e[i]));
        return n.join("")
    }
};
t = {
    rotl: function(e, n) {
        return e << n | e >>> 32 - n
    },
    rotr: function(e, n) {
        return e << 32 - n | e >>> n
    },
    endian: function(e) {
        if (e.constructor == Number)
            return 16711935 & t.rotl(e, 8) | 4278255360 & t.rotl(e, 24);
        for (var n = 0; n < e.length; n++)
            e[n] = t.endian(e[n]);
        return e
    },
    randomBytes: function(e) {
        for (var n = []; e > 0; e--)
            n.push(Math.floor(256 * Math.random()));
        return n
    },
    bytesToWords: function(e) {
        for (var n = [], i = 0, t = 0; i < e.length; i++,
            t += 8)
            n[t >>> 5] |= e[i] << 24 - t % 32;
        return n
    },
    wordsToBytes: function(e) {
        for (var n = [], i = 0; i < 32 * e.length; i += 8)
            n.push(e[i >>> 5] >>> 24 - i % 32 & 255);
        return n
    },
    bytesToHex: function(e) {
        for (var n = [], i = 0; i < e.length; i++)
            n.push((e[i] >>> 4).toString(16)),
                n.push((15 & e[i]).toString(16));
        return n.join("")
    },
    hexToBytes: function(e) {
        for (var n = [], i = 0; i < e.length; i += 2)
            n.push(parseInt(e.substr(i, 2), 16));
        return n
    },
    bytesToBase64: function(e) {
        for (var i = [], t = 0; t < e.length; t += 3) {
            for (var r = e[t] << 16 | e[t + 1] << 8 | e[t + 2], o = 0; o < 4; o++)
                8 * t + 6 * o <= 8 * e.length ? i.push(n.charAt(r >>> 6 * (3 - o) & 63)) : i.push("=")
        }
        return i.join("")
    },
    base64ToBytes: function(e) {
        e = e.replace(/[^A-Z0-9+\/]/ig, "");
        for (var i = [], t = 0, r = 0; t < e.length; r = ++t % 4)
            0 != r && i.push((n.indexOf(e.charAt(t - 1)) & Math.pow(2, -2 * r + 8) - 1) << 2 * r | n.indexOf(e.charAt(t)) >>> 6 - 2 * r);
        return i
    }
}
s={
    _ff : function(e, n, i, t, r, o, l) {
        var s = e + (n & i | ~n & t) + (r >>> 0) + l;
        return (s << o | s >>> 32 - o) + n
    },
    _gg : function(e, n, i, t, r, o, l) {
        var s = e + (n & t | i & ~t) + (r >>> 0) + l;
        return (s << o | s >>> 32 - o) + n
    }
    ,
    _hh : function(e, n, i, t, r, o, l) {
        var s = e + (n ^ i ^ t) + (r >>> 0) + l;
        return (s << o | s >>> 32 - o) + n
    }
    ,
    _ii : function(e, n, i, t, r, o, l) {
        var s = e + (i ^ (n | ~t)) + (r >>> 0) + l;
        return (s << o | s >>> 32 - o) + n
    }
}


function generate_random_str() {
    let random_str_list = []
    random_str_list = random_str_list.concat(gener_random(Math.random() * 10000, [3, 45]))
    random_str_list = random_str_list.concat(gener_random(Math.random() * 10000, [1, 0]))
    random_str_list = random_str_list.concat(gener_random(Math.random() * 10000, [1, 5]))
    return String.fromCharCode.apply(null, random_str_list)
}

function sign(url_search_params, user_agent, arguments) {
    /**
     * url_search_params："device_platform=webapp&aid=6383&channel=channel_pc_web&update_version_code=170400&pc_client_type=1&version_code=170400&version_name=17.4.0&cookie_enabled=true&screen_width=1536&screen_height=864&browser_language=zh-CN&browser_platform=Win32&browser_name=Chrome&browser_version=123.0.0.0&browser_online=true&engine_name=Blink&engine_version=123.0.0.0&os_name=Windows&os_version=10&cpu_core_num=16&device_memory=8&platform=PC&downlink=10&effective_type=4g&round_trip_time=50&webid=7362810250930783783&msToken=VkDUvz1y24CppXSl80iFPr6ez-3FiizcwD7fI1OqBt6IICq9RWG7nCvxKb8IVi55mFd-wnqoNkXGnxHrikQb4PuKob5Q-YhDp5Um215JzlBszkUyiEvR"
     * user_agent："Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36"
     */
    let result_str = generate_random_str() + generate_rc4_bb_str(
        url_search_params,
        user_agent,
        "1536|747|1536|834|0|30|0|0|1536|834|1536|864|1525|747|24|24|Win32",
        "cus",
        arguments
    );
    return result_encrypt(result_str, "s4") + "=";
}

function sign_datail(params, userAgent) {
    return sign(params, userAgent, [0, 1, 14])
}

function sign_reply(params, userAgent) {
    return sign(params, userAgent, [0, 1, 8])
}
