"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  data() {
    return {
      isRegister: false,
      isLogin: false,
      codeTime: 0,
      sendEmailCount: 0,
      // 校验表单数据
      loginData: {
        userEmail: "",
        userPassword: ""
      },
      registerData: {
        userEmail: "",
        userPassword: "",
        userCode: ""
      },
      userData: {
        userId: "",
        userName: "",
        userEmail: "",
        userPassword: "",
        userPhone: "",
        userUrl: "",
        userProfile: ""
      },
      // 校验规则
      rules_login: {
        userEmail: {
          rules: [{
            required: true
          }, {
            format: "email",
            errorMessage: " "
          }]
        },
        userPassword: {
          rules: [{
            required: true
          }]
        }
      },
      rules_register: {
        userEmail: {
          rules: [{
            required: true
          }, {
            format: "email",
            errorMessage: " "
          }]
        },
        userPassword: {
          rules: [{
            required: true
          }]
        },
        userCode: {
          rules: [{
            required: true
          }]
        }
      },
      orderIcon: {
        color: "#f9da49",
        size: "60rpx",
        type: "cart"
      },
      infoIcon: {
        color: "#999999",
        size: "200rpx",
        type: "contact-filled"
      },
      setIcon: {
        color: "#f9da49",
        size: "60rpx",
        type: "gear"
      },
      logoutIcon: {
        color: "#f9da49",
        size: "60rpx",
        type: "arrow-left"
      }
    };
  },
  onLoad: function(option) {
    this.userId = common_vendor.index.getStorageSync("userId");
    this.token = common_vendor.index.getStorageSync("token");
    this.isLogin = common_vendor.index.getStorageSync("isLogin");
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
      }
    });
  },
  methods: {
    err_submit() {
      if (this.loginData.userEmail && !this.loginData.userPassword) {
        common_vendor.index.showToast({
          title: "请输入密码",
          icon: "error",
          mask: "true"
        });
      } else {
        common_vendor.index.showToast({
          title: "请输入邮箱",
          icon: "error",
          mask: "true"
        });
      }
    },
    err_submit_register() {
      if (this.registerData.userEmail && (!this.registerData.userPassword || !this.registerData.userCode)) {
        if (this.registerData.userPassword && !this.registerData.userCode) {
          common_vendor.index.showToast({
            title: "请输入验证码",
            icon: "error",
            mask: "true"
          });
        } else {
          common_vendor.index.showToast({
            title: "请输入密码",
            icon: "error",
            mask: "true"
          });
        }
      } else {
        common_vendor.index.showToast({
          title: "请输入邮箱",
          icon: "error",
          mask: "true"
        });
      }
    },
    //submit_login函数
    submit_login(ref) {
      this.$refs[ref].validate().then((res) => {
        let user = {
          "userEmail": this.loginData.userEmail,
          "userPassword": this.loginData.userPassword
        };
        common_vendor.index.request({
          url: "/api/user/login",
          method: "POST",
          dataType: "json",
          data: user,
          success: (res2) => {
            if (res2.data.code == 0) {
              console.log("调用成功", res2.data.data);
              common_vendor.index.setStorageSync("userId", res2.data.data.user.userId);
              common_vendor.index.setStorageSync("token", res2.data.data.token);
              common_vendor.index.setStorageSync("isLogin", true);
              common_vendor.index.reLaunch({
                url: "/pages/user/login"
              });
            }
            if (res2.data.code == 1) {
              common_vendor.index.showToast({
                title: res2.data.message,
                icon: "error",
                mask: "true"
              });
            }
          },
          fail: (res2) => {
            console.log(res2.errMsg);
            console.log("调用接口失败");
          }
        });
      }).catch((err) => {
        common_vendor.index.showToast({
          title: "请正确填写邮箱",
          icon: "error",
          mask: "true"
        });
        console.log("err", err);
      });
    },
    submit_register(ref) {
      this.$refs[ref].validate().then((res) => {
        let user_register = {
          "userEmail": this.registerData.userEmail,
          "userPassword": this.registerData.userPassword,
          "userCode": this.registerData.userCode
        };
        common_vendor.index.request({
          url: "/api/user/register?code=" + this.registerData.userCode,
          method: "POST",
          dataType: "json",
          data: user_register,
          success: (res2) => {
            console.log(user_register);
            console.log(res2.data);
            if (res2.data.code == 0) {
              common_vendor.index.request({
                url: "/api/user/login",
                method: "POST",
                dataType: "json",
                data: user_register,
                success: (res3) => {
                  if (res3.data.code == 0) {
                    console.log("调用成功", res3.data.data);
                    common_vendor.index.setStorageSync("userId", res3.data.data.user.userId);
                    common_vendor.index.setStorageSync("token", res3.data.data.token);
                    common_vendor.index.setStorageSync("isLogin", true);
                    common_vendor.index.reLaunch({
                      url: "/pages/user/login"
                    });
                  }
                }
              });
            }
            if (res2.data.code == 1) {
              console.log(res2.data);
              common_vendor.index.showToast({
                title: res2.data.message,
                icon: "error",
                mask: "true"
              });
            }
          }
        });
      }).catch((err) => {
        common_vendor.index.showToast({
          title: "请正确填写邮箱",
          icon: "error",
          mask: "true"
        });
        console.log("err", err);
      });
    },
    sendEmail() {
      this.sendEmailCount++;
      let email_reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
      if (email_reg.test(this.registerData.userEmail)) {
        let user = {
          "userEmail": this.registerData.userEmail,
          "userPassword": this.registerData.userPassword
        };
        common_vendor.index.request({
          url: "/api/user/sendEmail",
          method: "POST",
          dataType: "json",
          data: user,
          success: (res) => {
            if (res.data.code == 0) {
              this.codeTime = 120;
              let timer = setInterval(() => {
                this.codeTime--;
                if (this.codeTime < 1) {
                  clearInterval(timer);
                  this.codeTime = 0;
                }
              }, 1e3);
              common_vendor.index.showToast({
                title: "请前往邮箱查看验证码",
                icon: "success",
                mask: true
              });
            }
            if (res.data.code == 1) {
              if (res.data.data != null) {
                this.codeTime = 120 - res.data.data.time_gap;
                if (this.codeTime > 0) {
                  common_vendor.index.showToast({
                    title: res.data.message,
                    icon: "error",
                    mask: "true"
                  });
                  if (this.sendEmailCount == 1) {
                    let timer = setInterval(() => {
                      this.codeTime--;
                      if (this.codeTime < 1) {
                        clearInterval(timer);
                        this.codeTime = 0;
                      }
                    }, 1e3);
                  }
                  return;
                }
              } else {
                common_vendor.index.showToast({
                  title: res.data.message,
                  icon: "error",
                  mask: "true"
                });
              }
            }
          }
        });
      }
      if (!email_reg.test(this.registerData.userEmail) && this.registerData.userEmail) {
        common_vendor.index.showToast({
          title: "请正确填写邮箱",
          icon: "error",
          mask: "true"
        });
      }
      if (!this.registerData.userEmail) {
        common_vendor.index.showToast({
          title: "请输入邮箱",
          icon: "error",
          mask: "true"
        });
      }
    },
    to_logout() {
      common_vendor.index.clearStorageSync();
      common_vendor.index.reLaunch({
        url: "/pages/user/login"
      });
    },
    to_info() {
      common_vendor.index.navigateTo({
        url: "/pages/user/info",
        animationType: "pop-in",
        animationDuration: 300
      });
    }
  }
};
if (!Array) {
  const _easycom_uni_icons2 = common_vendor.resolveComponent("uni-icons");
  const _easycom_uni_easyinput2 = common_vendor.resolveComponent("uni-easyinput");
  const _easycom_uni_forms_item2 = common_vendor.resolveComponent("uni-forms-item");
  const _easycom_uni_forms2 = common_vendor.resolveComponent("uni-forms");
  const _easycom_uni_list_item2 = common_vendor.resolveComponent("uni-list-item");
  const _easycom_uni_list2 = common_vendor.resolveComponent("uni-list");
  (_easycom_uni_icons2 + _easycom_uni_easyinput2 + _easycom_uni_forms_item2 + _easycom_uni_forms2 + _easycom_uni_list_item2 + _easycom_uni_list2)();
}
const _easycom_uni_icons = () => "../../uni_modules/uni-icons/components/uni-icons/uni-icons.js";
const _easycom_uni_easyinput = () => "../../uni_modules/uni-easyinput/components/uni-easyinput/uni-easyinput.js";
const _easycom_uni_forms_item = () => "../../uni_modules/uni-forms/components/uni-forms-item/uni-forms-item.js";
const _easycom_uni_forms = () => "../../uni_modules/uni-forms/components/uni-forms/uni-forms.js";
const _easycom_uni_list_item = () => "../../uni_modules/uni-list/components/uni-list-item/uni-list-item.js";
const _easycom_uni_list = () => "../../uni_modules/uni-list/components/uni-list/uni-list.js";
if (!Math) {
  (_easycom_uni_icons + _easycom_uni_easyinput + _easycom_uni_forms_item + _easycom_uni_forms + _easycom_uni_list_item + _easycom_uni_list)();
}
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: !$data.isLogin
  }, !$data.isLogin ? common_vendor.e({
    b: !$data.isRegister
  }, !$data.isRegister ? common_vendor.e({
    c: !$data.isRegister
  }, !$data.isRegister ? {
    d: common_vendor.o(($event) => $data.isRegister = true)
  } : {}, {
    e: $data.isRegister
  }, $data.isRegister ? {
    f: common_vendor.o(($event) => $data.isRegister = false)
  } : {}, {
    g: !$data.isRegister
  }, !$data.isRegister ? {
    h: common_vendor.o(($event) => $data.isRegister = true)
  } : {}, {
    i: $data.isRegister
  }, $data.isRegister ? {
    j: common_vendor.o(($event) => $data.isRegister = false)
  } : {}, {
    k: common_vendor.p({
      type: "contact-filled",
      size: "150rpx",
      color: "#999999"
    }),
    l: common_vendor.o(($event) => $data.loginData.userEmail = $event),
    m: common_vendor.p({
      prefixIcon: "email-filled",
      placeholder: "请输入邮箱",
      modelValue: $data.loginData.userEmail
    }),
    n: common_vendor.p({
      name: "userEmail"
    }),
    o: common_vendor.o(($event) => $data.loginData.userPassword = $event),
    p: common_vendor.p({
      prefixIcon: "locked-filled",
      type: "password",
      placeholder: "请输入密码",
      modelValue: $data.loginData.userPassword
    }),
    q: common_vendor.p({
      name: "userPassword"
    }),
    r: common_vendor.sr("loginForm", "6163e5ce-1"),
    s: common_vendor.p({
      rules: $data.rules_login,
      modelValue: $data.loginData
    }),
    t: !$data.loginData.userEmail || !$data.loginData.userPassword
  }, !$data.loginData.userEmail || !$data.loginData.userPassword ? {
    v: common_vendor.o(($event) => $options.err_submit())
  } : {}, {
    w: $data.loginData.userEmail && $data.loginData.userPassword
  }, $data.loginData.userEmail && $data.loginData.userPassword ? {
    x: common_vendor.o(($event) => $options.submit_login("loginForm"))
  } : {}) : {}, {
    y: $data.isRegister
  }, $data.isRegister ? common_vendor.e({
    z: !$data.isRegister
  }, !$data.isRegister ? {
    A: common_vendor.o(($event) => $data.isRegister = true)
  } : {}, {
    B: $data.isRegister
  }, $data.isRegister ? {
    C: common_vendor.o(($event) => $data.isRegister = false)
  } : {}, {
    D: !$data.isRegister
  }, !$data.isRegister ? {
    E: common_vendor.o(($event) => $data.isRegister = true)
  } : {}, {
    F: $data.isRegister
  }, $data.isRegister ? {
    G: common_vendor.o(($event) => $data.isRegister = false)
  } : {}, {
    H: common_vendor.p({
      type: "contact-filled",
      size: "150rpx",
      color: "#999999"
    }),
    I: common_vendor.o(($event) => $data.registerData.userEmail = $event),
    J: common_vendor.p({
      prefixIcon: "email-filled",
      placeholder: "请输入邮箱",
      modelValue: $data.registerData.userEmail
    }),
    K: common_vendor.p({
      name: "userEmail"
    }),
    L: common_vendor.o(($event) => $data.registerData.userPassword = $event),
    M: common_vendor.p({
      prefixIcon: "locked-filled",
      type: "password",
      placeholder: "请输入密码",
      modelValue: $data.registerData.userPassword
    }),
    N: common_vendor.p({
      name: "userPassword"
    }),
    O: common_vendor.t(!$data.codeTime ? "获取验证码" : $data.codeTime + "s"),
    P: common_vendor.o(($event) => $options.sendEmail()),
    Q: common_vendor.o(($event) => $data.registerData.userCode = $event),
    R: common_vendor.p({
      prefixIcon: "info-filled",
      placeholder: "请输入验证码",
      modelValue: $data.registerData.userCode
    }),
    S: common_vendor.p({
      name: "userCode"
    }),
    T: common_vendor.sr("registerForm", "6163e5ce-7"),
    U: common_vendor.p({
      rules: $data.rules_register,
      modelValue: $data.registerData
    }),
    V: !$data.registerData.userEmail || !$data.registerData.userPassword || !$data.registerData.userCode
  }, !$data.registerData.userEmail || !$data.registerData.userPassword || !$data.registerData.userCode ? {
    W: common_vendor.o(($event) => $options.err_submit_register())
  } : {}, {
    X: $data.registerData.userEmail && $data.registerData.userPassword && $data.registerData.userCode
  }, $data.registerData.userEmail && $data.registerData.userPassword && $data.registerData.userCode ? {
    Y: common_vendor.o(($event) => $options.submit_register("registerForm"))
  } : {}) : {}) : {}, {
    Z: $data.isLogin
  }, $data.isLogin ? {
    aa: common_vendor.o(($event) => $options.to_info()),
    ab: common_vendor.p({
      ["show-extra-icon"]: true,
      showArrow: true,
      ["extra-icon"]: $data.infoIcon,
      title: this.userData.userName,
      clickable: true
    }),
    ac: common_vendor.p({
      ["show-extra-icon"]: true,
      showArrow: true,
      ["extra-icon"]: $data.orderIcon,
      title: "购票记录"
    }),
    ad: common_vendor.p({
      ["show-extra-icon"]: true,
      showArrow: true,
      ["extra-icon"]: $data.setIcon,
      title: "设定"
    }),
    ae: common_vendor.o(($event) => $options.to_logout()),
    af: common_vendor.p({
      ["show-extra-icon"]: true,
      ["extra-icon"]: $data.logoutIcon,
      title: "登出",
      clickable: true
    })
  } : {});
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-6163e5ce"], ["__file", "E:/Senior_2/cinema/vueDemo/pages/user/login.vue"]]);
wx.createPage(MiniProgramPage);
