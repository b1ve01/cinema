"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  data() {
    return {
      // 校验表单数据
      valiFormData: {
        userEmail: "",
        userPassword: "",
        code: ""
      },
      // 校验规则
      rules: {
        userEmail: {
          rules: [{
            required: true,
            errorMessage: "邮箱不能为空"
          }]
        },
        userPassword: {
          rules: [{
            required: true,
            errorMessage: "密码不能为空"
          }]
        },
        code: {
          rules: [{
            required: true,
            errorMessage: "验证码不能为空"
          }]
        }
      }
    };
  },
  methods: {
    //submit函数
    submit(ref) {
      let user = {
        "userEmail": this.valiFormData.userEmail,
        "userPassword": this.valiFormData.userPassword,
        "code": this.valiFormData.code
      };
      console.log(user);
      common_vendor.index.request({
        url: "http://localhost:8080/user/register",
        method: "POST",
        dataType: "json",
        data: user,
        success(res) {
          console.log("成功", res.data);
        },
        fail: (res) => {
          console.log(res.errMsg);
          console.log("调用接口失败");
        }
      });
    }
  }
};
if (!Array) {
  const _easycom_uni_easyinput2 = common_vendor.resolveComponent("uni-easyinput");
  const _easycom_uni_forms_item2 = common_vendor.resolveComponent("uni-forms-item");
  const _easycom_uni_forms2 = common_vendor.resolveComponent("uni-forms");
  const _easycom_uni_section2 = common_vendor.resolveComponent("uni-section");
  (_easycom_uni_easyinput2 + _easycom_uni_forms_item2 + _easycom_uni_forms2 + _easycom_uni_section2)();
}
const _easycom_uni_easyinput = () => "../../uni_modules/uni-easyinput/components/uni-easyinput/uni-easyinput.js";
const _easycom_uni_forms_item = () => "../../uni_modules/uni-forms/components/uni-forms-item/uni-forms-item.js";
const _easycom_uni_forms = () => "../../uni_modules/uni-forms/components/uni-forms/uni-forms.js";
const _easycom_uni_section = () => "../../uni_modules/uni-section/components/uni-section/uni-section.js";
if (!Math) {
  (_easycom_uni_easyinput + _easycom_uni_forms_item + _easycom_uni_forms + _easycom_uni_section)();
}
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_vendor.o(($event) => $data.valiFormData.userEmail = $event),
    b: common_vendor.p({
      placeholder: "请输入邮箱",
      modelValue: $data.valiFormData.userEmail
    }),
    c: common_vendor.p({
      label: "邮箱",
      required: true,
      name: "userEmail"
    }),
    d: common_vendor.o(($event) => $data.valiFormData.userPassword = $event),
    e: common_vendor.p({
      placeholder: "请输入密码",
      modelValue: $data.valiFormData.userPassword
    }),
    f: common_vendor.p({
      label: "密码",
      required: true,
      name: "userPassword"
    }),
    g: common_vendor.o(($event) => $data.valiFormData.code = $event),
    h: common_vendor.p({
      placeholder: "请输入验证码",
      modelValue: $data.valiFormData.code
    }),
    i: common_vendor.p({
      label: "验证码",
      required: true,
      name: "code"
    }),
    j: common_vendor.sr("valiForm", "27a91450-1,27a91450-0"),
    k: common_vendor.p({
      rules: $data.rules,
      modelValue: $data.valiFormData,
      ["label-position"]: "top"
    }),
    l: common_vendor.o(($event) => $options.submit("valiForm")),
    m: common_vendor.p({
      title: "表单校验",
      type: "line"
    })
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__file", "E:/Senior_2/cinema/vueDemo/pages/user/register.vue"]]);
wx.createPage(MiniProgramPage);
