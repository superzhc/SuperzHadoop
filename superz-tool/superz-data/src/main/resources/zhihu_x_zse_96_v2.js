function Func_s() {
    this.e = function(data) {
        data.o = false;
    };
}
function Func_i(data) {
    this.t = (2048 & data) >> 11;
    this.s = (1536 & data) >> 9;
    this.i = 511 & data;
    this.h = 511 & data;

    this.e = function(data) {
        switch (this.t) {
            case 0:
                data.r[this.s] = this.i;
                break;
            case 1:
                data.r[this.s] = data.k[this.h];
        }
    }
}
function Func_h(data) {
    this.s = (3072 & data) >> 10;
    this.h = 1023 & data;

    this.e = function(data) {
        data.k[this.h] = data.r[this.s];
    }
}
function Func_a(data) {
    this.a = (3072 & data) >> 10;
    this.c = (768 & data) >> 8;
    this.n = (192 & data) >> 6;
    this.t = 63 & data;

    this.e = function(data) {
        switch (this.t) {
            case 0:
                data.r[this.a] = data.r[this.c] + data.r[this.n];
                break;
            case 1:
                data.r[this.a] = data.r[this.c] - data.r[this.n];
                break;
            case 2:
                data.r[this.a] = data.r[this.c] * data.r[this.n];
                break;
            case 3:
                data.r[this.a] = data.r[this.c] / data.r[this.n];
                break;
            case 4:
                data.r[this.a] = data.r[this.c] % data.r[this.n];
                break;
            case 5:
                data.r[this.a] = data.r[this.c] === data.r[this.n];
                break;
            case 6:
                data.r[this.a] = data.r[this.c] >= data.r[this.n];
                break;
            case 7:
                data.r[this.a] = data.r[this.c] || data.r[this.n];
                break;
            case 8:
                data.r[this.a] = data.r[this.c] && data.r[this.n];
                break;
            case 9:
                data.r[this.a] = data.r[this.c] !== data.r[this.n];
                break;
            case 10:
                data.r[this.a] = Func_t();
                break;
            case 11:
                data.r[this.a] = data.r[this.c] in data.r[this.n];
                break;
            case 12:
                data.r[this.a] = data.r[this.c] > data.r[this.n];
                break;
            case 13:
                data.r[this.a] = - data.r[this.c];
                break;
            case 14:
                data.r[this.a] = data.r[this.c] < data.r[this.n];
                break;
            case 15:
                data.r[this.a] = data.r[this.c] & data.r[this.n];
                break;
            case 16:
                data.r[this.a] = data.r[this.c] ^ data.r[this.n];
                break;
            case 17:
                data.r[this.a] = data.r[this.c] << data.r[this.n];
                break;
            case 18:
                data.r[this.a] = data.r[this.c] >>> data.r[this.n];
                break;
            case 19:
                data.r[this.a] = data.r[this.c] | data.r[this.n];
                break;
            case 20:
                data.r[this.a] = !data.r[this.c];
        }
    }
}
function Func_c(data) {
    this.s = data >> 10 & 3;
    this.i = 1023 & data;

    this.e = function(data) {
        data.Q.push(data.C);
        data.B.push(data.k);
        data.C = data.r[this.s];
        data.k = [];
        for (var t = 0; t < this.i; t++)
            data.k.unshift(data.f.pop());
        data.g.push(data.f);
        data.f = [];
    }
}
function Func_n() {
    this.e = function(data) {
        data.C = data.Q.pop();
        data.k = data.B.pop();
        data.f = data.g.pop();
    }
}
function Func_e(data) {
    this.a = (3072 & data) >> 10;
    this.c = (768 & data) >> 8;
    this.t = 63 & data;

    this.e = function(data) {
        switch (this.t) {
            case 0:
                data.u = data.r[this.a] >= data.r[this.c];
                break;
            case 1:
                data.u = data.r[this.a] <= data.r[this.c];
                break;
            case 2:
                data.u = data.r[this.a] > data.r[this.c];
                break;
            case 3:
                data.u = data.r[this.a] < data.r[this.c];
                break;
            case 4:
                data.u = data.r[this.a] === data.r[this.c];
                break;
            case 5:
                data.u = data.r[this.a] !== data.r[this.c];
                break;
            case 6:
                data.u = data.r[this.a];
                break;
            case 7:
                data.u = !data.r[this.a];
        }
    }
}
function Func_o(data) {
    this.h = (4095 & data) >> 2;
    this.t = 3 & data;

    this.e = function(data) {
        switch (this.t) {
            case 0:
                data.C = this.h;
                break;
            case 1:
                data.u && (data.C = this.h);
                break;
            case 2:
                data.u || (data.C = this.h);
                break;
            case 3:
                data.C = this.h;
                data.w = null;
        }
        data.u = false;
    }
}
function Func_r(data) {
    this.s = data >> 10 & 3;
    this.i = data >> 2 & 255;
    this.t = 3 & data;

    this.e = function(data) {
        var t = [];
        switch (this.t) {
            case 0:
                for (var n = 0; n < this.i; n++)
                    t.unshift(data.f.pop());
                data.r[3] = data.r[this.s](t[0], t[1]);
                break;
            case 1:
                var r = data.f.pop();
                for (var i = 0; i < this.i; i++)
                    t.unshift(data.f.pop());
                data.r[3] = data.r[this.s][r](t[0], t[1]);
                break;
            case 2:
                for (var s = 0; s < this.i; s++)
                    t.unshift(data.f.pop());
                data.r[3] = new data.r[this.s](t[0],t[1]);
        }
    };
}
function Func_Q(data) {
    this.t = (4095 & data) >> 10;
    this.s = (1023 & data) >> 8;
    this.i = 1023 & data;
    this.h = 63 & data;

    this.e = function(data) {
        switch (this.t) {
            case 0:
                data.f.push(data.r[this.s]);
                break;
            case 1:
                data.f.push(this.i);
                break;
            case 2:
                data.f.push(data.k[this.h]);
                break;
            case 3:
                data.f.push(Func_k(data.b[this.h]));
        }
    }
}
function Func_C(data) {
    this.t = (4095 & data) >> 10;
    this.a = (1023 & data) >> 8;
    this.c = (255 & data) >> 6;

    this.e = function(data) {
        var t = data.f.pop();
        switch (this.t) {
            case 0:
                data.r[this.a] = data.r[this.c][t];
                break;
            case 1:
                data.r[this.c][t] = data.f.pop();
                break;
            case 2:
                data.r[this.a] = eval(t)
        }
    };
}
function Func_B(data) {
    this.s = (3072 & data) >> 10;
    this.h = 1023 & data;

    this.e = function(data) {
        data.r[this.s] = Func_k(data.b[this.h]);
    }
}
function Func_f(data) {
    this.h = 4095 & data;

    this.e = function(data) {
        data.w = this.h;
    }
}
function Func_g(data) {
    this.s = (3072 & data) >> 10;

    this.e = function(data) {
        // throw data.r[this.s];
    }
}
function Func_u(data) {
    this.h = 4095 & data;

    this.e = function(data, val) {
        var o = new Func_G;
        o.k = [val];
        o.v(data.G, this.h, data.b);
        return o.r[3]
    }
}
function Func_w(data) {
    this.t = (3840 & data) >> 8;
    this.s = (192 & data) >> 6;
    this.i = 63 & data;

    this.e = function(data) {
        var t = {}, o = [];
        switch (this.t) {
            case 0:
                for (var n = 0; n < this.i; n++) {
                    t[data.f.pop()] = data.f.pop()
                }
                data.r[this.s] = t;
                break;
            case 1:
                for (var n = 0; n < this.i; n++)
                    o.unshift(data.f.pop());
                data.r[this.s] = o
        }
    }
}

function Func_t() {
    return false;
}

function Func_k(data) {
    var n = [], t = 66;
    for (var r = 0; r < data.length; r++) {
        var o = 24 ^ data[r] ^ t;
        n.push(String.fromCharCode(o));
        t = o
    }
    return n.join("")
}
function Func_G() {
    this.r = [0, 0, 0, 0];
    this.C = 0;
    this.Q = [];
    this.k = [];
    this.B = [];
    this.f = [];
    this.g = [];
    this.G = [
        57351, 37632, 39936, 43008, 39937, 41984, 0, 4096, 8194, 39938, 43008, 12298, 46083, 13385,
        25606, 28754, 39938, 43008, 36864, 8194, 6146, 39940, 40960, 8195, 39941, 43008, 8196, 6146,
        12308, 6658, 39942, 41280, 47111, 14725, 13447, 25606, 28842, 45064, 6656, 13376, 37120, 9216,
        6147, 12308, 6659, 39945, 41280, 13588, 13383, 25606, 28898, 45066, 6656, 13376, 37120, 9216,
        45067, 36864, 6147, 39945, 40960, 39948, 32769, 39949, 35845, 4096, 13062, 24582, 28970, 45070,
        6656, 13376, 37120, 9216, 6146, 39951, 40960, 6658, 39952, 41280, 13383, 6146, 39953, 40960,
        12551, 24582, 29042, 45074, 6656, 13376, 37120, 9216, 6146, 39955, 40960, 6658, 39956, 41280,
        13383, 25606, 29098, 45077, 6656, 13376, 37120, 9216, 6146, 39958, 40960, 24582, 29138, 45079,
        6656, 13376, 37120, 9216, 6146, 39960, 40960, 24582, 29178, 45081, 6656, 13376, 37120, 9216,
        6147, 39962, 40960, 24582, 29218, 45083, 6656, 13376, 37120, 9216, 6146, 39964, 40960, 6658,
        39965, 41280, 13383, 25606, 29274, 45086, 6656, 13376, 37120, 9216, 6148, 39967, 40960, 12308,
        24582, 29318, 45088, 6656, 13376, 37120, 9216, 6147, 36864, 45065, 36864, 6148, 39967, 32777,
        27654, 29374, 45089, 6656, 13376, 37120, 9216, 6147, 36864, 45082, 36864, 6148, 39967, 32777,
        27654, 29430, 45090, 6656, 13376, 37120, 9216, 45091, 36864, 6148, 39967, 40960, 36864, 39972,
        43008, 39973, 40960, 39974, 40960, 39975, 32773, 39949, 35845, 4096, 13070, 24582, 29530, 45096,
        6656, 13376, 37120, 9216, 6145, 4650, 13383, 37120, 9217, 45097, 8197, 6144, 39978, 40960, 4611,
        13380, 9222, 6150, 4609, 13381, 25606, 29618, 6144, 46123, 13376, 9216, 6150, 4610, 13381, 25606,
        29654, 6144, 46124, 13376, 9216, 4096, 8199, 45101, 8200, 6144, 39978, 40960, 4609, 13377, 9225,
        6153, 4608, 13382, 25606, 30250, 4104, 6663, 5121, 14720, 10247, 5124, 14724, 13442, 9226, 6153,
        36864, 6144, 39982, 32773, 6145, 6666, 13394, 4351, 12559, 13072, 8203, 4104, 6663, 5121, 14720,
        10247, 5124, 14724, 13442, 37120, 9226, 6145, 6666, 13394, 4351, 12559, 36864, 8202, 6155, 6665,
        5121, 14721, 37376, 6656, 39982, 33797, 6666, 14160, 5128, 14737, 13459, 9227, 4104, 6663, 5121,
        14720, 10247, 5124, 14724, 13442, 37120, 9226, 6145, 6666, 13394, 4351, 12559, 36864, 8202, 6155,
        6665, 5122, 14721, 37376, 6656, 39982, 33797, 6666, 14160, 5136, 14737, 13459, 9227, 6149, 6667,
        5183, 14735, 37376, 6664, 39983, 33797, 13504, 9221, 6149, 6667, 5126, 14738, 4671, 13903, 37120,
        6664, 39983, 33797, 13504, 9221, 6149, 6667, 5132, 14738, 4671, 13903, 37120, 6664, 39983, 33797,
        13504, 9221, 6149, 6667, 5138, 14738, 4671, 13903, 37120, 6664, 39983, 33797, 13504, 9221, 6153,
        4611, 13377, 9225, 29692, 7685, 20480
    ];
    this.b = [
        [5, 24, 32],
        [5, 34, 19, 21, 9, 19, 17, 28],
        [45, 6, 31, 18, 19, 0],
        [47, 3, 18, 25, 27, 23, 31, 19, 25],
        [52, 23, 15, 7, 22, 30, 13, 3, 5],
        [21, 53, 16, 23, 30, 15],
        [52, 23, 20, 16],
        [52, 25, 19, 25, 23, 1],
        [74],
        [47, 30, 14, 15, 43, 62, 26, 19, 2],
        [75],
        [50, 21, 28, 29, 16, 17, 14, 24],
        [46, 3, 59, 59, 0, 10, 15, 41, 58, 10, 14],
        [51, 31, 18, 25, 5, 47, 49],
        [72],
        [57, 26, 21, 24, 36, 32, 17, 23, 2, 3, 26],
        [5, 55, 0, 17, 23, 2, 3, 26],
        [5, 24, 55, 0, 17, 23, 2, 3, 26, 20, 10],
        [73],
        [56, 15, 11, 24, 27, 15],
        [24, 47, 11, 24, 27, 15],
        [78],
        [63, 16, 28, 5],
        [79],
        [41, 27, 9, 14, 1],
        [76],
        [45, 10, 31, 30, 14, 3, 7, 11, 15],
        [77],
        [62, 19, 26, 52, 44, 25, 3, 26, 20, 13, 5, 30, 25],
        [62, 19, 26, 52, 44, 25, 3, 26, 20, 13, 5, 30, 25, 53, 52, 25, 2, 30, 5, 27, 24, 17, 15],
        [66],
        [61, 26, 9, 35, 32, 1, 38, 58, 5, 7, 13, 15, 30, 21, 37, 57, 14, 8, 9, 3, 1, 28, 3, 5],
        [67],
        [64],
        [65],
        [1, 45, 23, 13, 5, 7, 11, 93, 91, 20, 19, 25, 32],
        [28, 43, 3, 21, 15, 5, 30, 25],
        [42, 26, 5, 3, 3, 3, 21, 17, 13],
        [46, 3, 36, 63, 30, 3, 31, 17],
        [57, 26, 21, 24],
        [70],
        [],
        [54, 17, 19, 17, 11, 4],
        [90, 24],
        [90],
        [8, 63, 61, 60, 52, 55, 23, 16, 60, 36, 57, 11, 114, 89, 16, 27, 61, 126, 71, 39, 13, 23, 20, 1, 118, 98, 107, 68, 56, 18, 20, 32, 92, 122, 35, 48, 22, 55, 49, 101, 66, 53, 61, 46, 14, 19, 23, 112, 100, 59, 1, 65, 79, 30, 40, 36, 65, 76, 16, 22, 72, 90, 2, 65],
        [57, 19, 17, 11, 41, 52, 19, 25, 60, 45],
        [57, 19, 17, 11, 43, 45]
    ];
    this.F = [];
    this.J = {
        0: Func_s,
        1: Func_i,
        2: Func_h,
        3: Func_a,
        4: Func_c,
        5: Func_n,
        6: Func_e,
        7: Func_o,
        8: Func_r,
        9: Func_Q,
        10: Func_C,
        11: Func_B,
        12: Func_f,
        13: Func_g,
        14: Func_u,
        15: Func_w
    };

    this.v = function(e, t, n) {
        this.G = e;
        this.C = t || 0;
        this.b = n || [];

        while (true) {
            // console.log(this.C);
            var r = this.G[this.C++];
            if ("number" != typeof r) {
                break;
            }

            try {
                this.e(r);
            }
            catch (e){
                // console.log(e)
            }
        }
    }

    this.e = function(data) {
        var t = (61440 & data) >> 12;
        new this.J[t](data).e(this)
    }
}

function b(e) {
    return (new Func_u(7)).e((new Func_G), e);
}