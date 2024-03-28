"use strict";
const common_vendor = require("../../common/vendor.js");
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
const _sfc_main = {
  __name: "register",
  setup(__props) {
    common_vendor.ref("Hello");
    common_vendor.onReady(() => {
      console.log("hello");
    });
    const valiFormData = common_vendor.ref({
      userEmail: "",
      userPassword: "",
      code: ""
    });
    const rules = {
      userEmail: [{
        required: true,
        message: "请输入邮箱",
        trigger: "blur"
      }],
      userPassword: [{
        required: true,
        message: "请输入密码",
        trigger: "blur"
      }]
    };
    const valiForm = common_vendor.ref(null);
    const submit = async (ref) => {
      try {
        const res = await valiForm.value.validate();
        console.log("success", res);
        common_vendor.index.showToast({
          title: `校验通过`
        });
      } catch (err) {
        console.log("err", err);
      }
    };
    return (_ctx, _cache) => {
      return {
        a: common_vendor.o(($event) => valiFormData.value.userEmail = $event),
        b: common_vendor.p({
          placeholder: "请输入邮箱",
          modelValue: valiFormData.value.userEmail
        }),
        c: common_vendor.p({
          label: "邮箱",
          required: true,
          name: "userEmail"
        }),
        d: common_vendor.o(($event) => valiFormData.value.userPassword = $event),
        e: common_vendor.p({
          placeholder: "请输入密码",
          modelValue: valiFormData.value.userPassword
        }),
        f: common_vendor.p({
          label: "密码",
          required: true,
          name: "userPassword"
        }),
        g: common_vendor.sr(valiForm, "27a91450-1,27a91450-0", {
          "k": "valiForm"
        }),
        h: common_vendor.p({
          rules,
          modelValue: valiFormData.value,
          ["label-position"]: "top"
        }),
        i: common_vendor.o(($event) => submit()),
        j: common_vendor.p({
          title: "表单校验",
          type: "line"
        })
      };
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__file", "E:/Senior_2/cinema/vueDemo/pages/user/register.vue"]]);
wx.createPage(MiniProgramPage);
