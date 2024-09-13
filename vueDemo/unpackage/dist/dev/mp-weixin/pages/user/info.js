"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  data() {
    return {
      userData: {
        userId: "",
        userName: "",
        userEmail: "",
        userPassword: "",
        userPhone: "",
        userUrl: "",
        userProfile: ""
      }
    };
  },
  onLoad: function(option) {
    this.userId = common_vendor.index.getStorageSync("userId");
    console.log("id在这", this.userId);
    this.token = common_vendor.index.getStorageSync("token");
    console.log("token在这", this.token);
    let user = {
      "userId": this.userId
    };
    common_vendor.index.request({
      url: "/api/user/info",
      method: "GET",
      dataType: "json",
      data: user,
      header: {
        "Authorization": this.token
      },
      success: (res) => {
        console.log("调用成功", res.data.data);
        this.userData.userId = res.data.data.userId;
        this.userData.userName = res.data.data.userName;
        this.userData.userEmail = res.data.data.userEmail;
        this.userData.userPassword = res.data.data.userPassword;
        this.userData.userPhone = res.data.data.userPhone;
        this.userData.userUrl = res.data.data.userUrl;
        this.userData.userProfile = res.data.data.userProfile;
      },
      fail: (res) => {
        console.log(res.errMsg);
        console.log("调用接口失败");
      }
    });
  },
  methods: {}
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_vendor.t(_ctx.userId),
    b: common_vendor.t($data.userData.userName),
    c: common_vendor.t($data.userData.userEmail),
    d: common_vendor.t($data.userData.userPassword),
    e: common_vendor.t($data.userData.userPhone),
    f: common_vendor.t($data.userData.userUrl),
    g: common_vendor.t($data.userData.userProfile)
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-aab93774"], ["__file", "E:/Senior_2/cinema/vueDemo/pages/user/info.vue"]]);
wx.createPage(MiniProgramPage);
