class Background {
  constructor() {
    this.body_colors = new Array("#E8E8E8", "#CFCFCF", "#E6E4C1", "#DEBDBD", "#B4D1B4",
                            "#FFEABF", "#FFD7A3", "#D6C8E0", "#B9D8ED");
    this.hex_col = new Array('0','1','2','3','4','5','6','7','8','9',
                            'A','B','C','D','E','F')
    this.pause = 2000;
    this.speed = 20;
    this.step = 40;
    this.steps = 1;

    this.red_col_1, this.red_col_2, this.red_col_1_b, this.red_col_2_b;
    this.green_col_1, this.green_col_2, this.green_col_1_b, this.green_col_2_b;
    this.blue_col_1, this.blue_col_2, this.blue_col_1_b, this.blue_col_2_b;

    this.rgd_red_from, this.rgb_green_from, this.rgb_blue_from;
    this.rgd_red_to, this.rgb_green_to, this.rgb_blue_to;
    this.rgd_red_now, this.rgb_green_now, this.rgb_blue_now;

    this.rgd_red_from_b, this.rgb_green_from_b, this.rgb_blue_from_b;
    this.rgd_red_to_b, this.rgb_green_to_b, this.rgb_blue_to_b;
    this.rgd_red_now_b, this.rgb_green_now_b, this.rgb_blue_now_b;

    this.color_A = 0;
    this.color_B = 1;
    this.color_C = 1;
    this.color_D = 2;

    this.browser_infos = navigator.userAgent;
    this.ie_4 = document.all&&!document.getElementById;
    this.ie_5 = document.all&&document.getElementById&&!this.browser_infos.match( / Opera / );
    this.ns_4 = document.layers;
    this.ns_6 = document.getElementById&&!document.all;
    this.Opera = this.browser_infos.match( / this.Opera / );
    this.browser_ok = this.ie_4||this.ie_5||this.ns_4||this.ns_6||this.Opera;
  }

  change() {
    let timer;
    this.rgd_red_now = this.rgd_red_now - ((this.rgd_red_from - this.rgd_red_to) / this.speed);
    this.rgb_green_now = this.rgb_green_now - ((this.rgb_green_from - this.rgb_green_to) / this.speed);
    this.rgb_blue_now = this.rgb_blue_now - ((this.rgb_blue_from-this.rgb_blue_to) / this.speed);

    this.rgd_red_now_b = this.rgd_red_now_b - ((this.rgd_red_from_b - this.rgd_red_to_b) / this.speed);
    this.rgb_green_now_b = this.rgb_green_now_b - ((this.rgb_green_from_b - this.rgb_green_to_b) / this.speed);
    this.rgb_blue_now_b = this.rgb_blue_now_b - ((this.rgb_blue_from_b - this.rgb_blue_to_b) / this.speed);

    if (this.rgd_red_now > 255) {
        this.rgd_red_now = 255;
    }
    if (this.rgd_red_now < 0) {
        this.rgd_red_now = 0;
    }
    if (this.rgb_green_now > 255) {
        this.rgb_green_now = 255;
    }
    if (this.rgb_green_now < 0) {
        this.rgb_green_now = 0;
    }
    if (this.rgb_blue_now > 255) {
        this.rgb_blue_now = 255;
    }
    if (this.rgb_blue_now < 0) {
        this.rgb_blue_now = 0;
    }

    if (this.rgd_red_now_b > 255) {
        this.rgd_red_now_b = 255;
    }
    if (this.rgd_red_now_b < 0) {
        this.rgd_red_now_b = 0;
    }
    if (this.rgb_green_now_b > 255) {
        this.rgb_green_now_b = 255;
    }
    if (this.rgb_green_now_b < 0) {
        this.rgb_green_now_b = 0;
    }
    if (this.rgb_blue_now_b > 255) {
        this.rgb_blue_now_b = 255;
    }
    if (this.rgb_blue_now_b < 0) {
        this.rgb_blue_now_b = 0;
    }

    if (this.steps <= this.speed) {
        this.red_col_1 = this.hex_col[Math.floor(this.rgd_red_now / 16)];
        this.red_col_2 = this.hex_col[Math.floor(this.rgd_red_now) % 16];
        this.green_col_1 = this.hex_col[Math.floor(this.rgb_green_now / 16)];
        this.green_col_2 = this.hex_col[Math.floor(this.rgb_green_now) % 16];
        this.blue_col_1 = this.hex_col[Math.floor(this.rgb_blue_now / 16)];
        this.blue_col_2 = this.hex_col[Math.floor(this.rgb_blue_now) % 16];

        this.red_col_1_b = this.hex_col[Math.floor(this.rgd_red_now_b / 16)];
        this.red_col_2_b = this.hex_col[Math.floor(this.rgd_red_now_b) % 16];
        this.green_col_1_b = this.hex_col[Math.floor(this.rgb_green_now_b / 16)];
        this.green_col_2_b = this.hex_col[Math.floor(this.rgb_green_now_b) % 16];
        this.blue_col_1_b = this.hex_col[Math.floor(this.rgb_blue_now_b / 16)];
        this.blue_col_2_b = this.hex_col[Math.floor(this.rgb_blue_now_b) % 16];
        let back_color = "#" + this.red_col_1 + this.red_col_2 + this.green_col_1 +
                        this.green_col_2 + this.blue_col_1 + this.blue_col_2;
        let back_color_B = "#" + this.red_col_1_b + this.red_col_2_b + this.green_col_1_b +
                        this.green_col_2_b + this.blue_col_1_b + this.blue_col_2_b;
        if (this.ie_5) {
            document.body.style.filter =
            "progid:DXImageTransform.Microsoft.Gradient(startColorstr=" +
            back_color + ", endColorstr=" + back_color_B + ")";
        } else {
            document.body.style.background = back_color;
        }

        this.steps++;
        let me = this;
        timer = setTimeout(function() {
            me.change();
        }, this.step);
    }
    else {
        let me = this;
        clearTimeout(timer);
        this.steps = 1;
        timer = setTimeout(function() {
            me.translate_rgb();
        }, this.pause);
    }
  }

  translate_rgb() {
    let hexa = this.body_colors[this.color_A];
    let hexa_red = hexa.substring(1, 3);
    let hexa_green = hexa.substring(3, 5);
    let hexa_blue = hexa.substring(5, 7);
    this.rgd_red_from = parseInt("0x" + hexa_red);
    this.rgb_green_from = parseInt("0x" + hexa_green);
    this.rgb_blue_from = parseInt("0x" + hexa_blue);
    this.rgd_red_now = this.rgd_red_from;
    this.rgb_green_now = this.rgb_green_from;
    this.rgb_blue_now = this.rgb_blue_from;

    hexa = this.body_colors[this.color_B];
    hexa_red = hexa.substring(1, 3);
    hexa_green = hexa.substring(3, 5);
    hexa_blue = hexa.substring(5, 7);
    this.rgd_red_to = parseInt("0x" + hexa_red);
    this.rgb_green_to = parseInt("0x" + hexa_green);
    this.rgb_blue_to = parseInt("0x" + hexa_blue);

    hexa = this.body_colors[this.color_C];
    hexa_red = hexa.substring(1, 3);
    hexa_green = hexa.substring(3, 5);
    hexa_blue = hexa.substring(5, 7);
    this.rgd_red_from_b = parseInt("0x" + hexa_red);
    this.rgb_green_from_b = parseInt("0x" + hexa_green);
    this.rgb_blue_from_b = parseInt("0x" + hexa_blue);
    this.rgd_red_now_b = this.rgd_red_from_b;
    this.rgb_green_now_b = this.rgb_green_from_b;
    this.rgb_blue_now_b = this.rgb_blue_from_b;

    hexa = this.body_colors[this.color_D];
    hexa_red = hexa.substring(1,3);
    hexa_green = hexa.substring(3,5);
    hexa_blue = hexa.substring(5,7);
    this.rgd_red_to_b = parseInt("0x" + hexa_red);
    this.rgb_green_to_b = parseInt("0x" + hexa_green);
    this.rgb_blue_to_b = parseInt("0x" + hexa_blue);

    this.color_A++;
    this.color_B++;
    this.color_C++;
    this.color_D++;

    if (this.color_A >= this.body_colors.length) {
        this.color_A = 0;
    }
    if (this.color_B >= this.body_colors.length) {
        this.color_B = 0;
    }
    if (this.color_C >= this.body_colors.length) {
        this.color_C = 0;
    }
    if (this.color_D >= this.body_colors.length) {
        this.color_D = 0;
    }

    this.change();
  }
}

window.addEventListener("load", () => {
    let back = new Background();
    if (back.browser_ok) {
        back.translate_rgb();
    }
})